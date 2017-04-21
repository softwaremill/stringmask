package com.softwaremill.macros.customize

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import macrocompat.bundle

class customize extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro CustomizeImpl.impl
}

class mask extends StaticAnnotation

@bundle
class CustomizeImpl(val c: whitebox.Context) {

  def impl(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractCaseClassesParts(classDecl: ClassDef) = classDecl match {
      case q"""case class $className(..$fields) extends ..$parents { ..$body }""" =>
        (className, fields, parents, body)
      case q"""case class $className private(..$fields) extends ..$parents { ..$body }""" =>
        (className, fields, parents, body)
      case _ => c.abort(c.enclosingPosition, "Unsupported case class type. Cannot rewrite toString().")
    }

    def extractNewToString(typeName: TypeName, allFields: List[Tree]) = {
      val fieldListTree = allFields.foldLeft(List.empty[Tree]) {
        case (accList, fieldTree) =>
          fieldTree match {
            case q"$m val $field: $fieldType = $sth" =>
              m.annotations match {
                case List(q"new mask()") =>
                  accList :+ Literal(Constant("***"))
                case _ =>
                  accList :+ q""""" + $field """
              }
            case _ => c.abort(c.enclosingPosition, s"Cannot call .toString on field.")
          }
      }
      val treesAsString = fieldListTree.reduceLeftOption { (commaSeparatedFieldsString, fieldString) =>
        q"$commaSeparatedFieldsString + ',' + $fieldString"
      }
      val typeNameStrTree = Literal(Constant(typeName.toString))
      q"""
         override def toString = {
          $typeNameStrTree + '(' + ${treesAsString.getOrElse(Literal(Constant("")))} + ')'
         }
      """
    }

    def modifiedDeclaration(classDecl: ClassDef, tail: List[Tree] = List.empty) = {
      val (className, fields, parents, body) = extractCaseClassesParts(classDecl)
      val newToString                        = extractNewToString(className, fields)
      val params = fields.asInstanceOf[List[ValDef]] map { p =>
        p.duplicate
      }
      val e          = q"""
        case class $className ( ..$params ) extends ..$parents {
          $newToString
          ..$body
        }
      """
      val blockItems = e +: tail
      c.Expr[Any](q"{..$blockItems}")
    }

    annottees map (_.tree) toList match {
      case (classDecl: ClassDef) :: Nil =>
        modifiedDeclaration(classDecl)
      case (classDecl: ClassDef) :: tail =>
        modifiedDeclaration(classDecl, tail)
      case other =>
        c.abort(c.enclosingPosition, "Invalid annottee, expected case class.")
    }

  }
}
