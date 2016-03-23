package com.softwaremill.macros.customize

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class customize extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro CustomizeImpl.impl
}

class mask extends StaticAnnotation


object CustomizeImpl {

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractCaseClassesParts(classDecl: ClassDef) = classDecl match {
      case q"case class $className(..$fields) extends ..$parents { ..$body }" =>
        (className, fields, parents, body)
    }

    def extractNewToString(typeName: TypeName, allFields: List[Tree]) = {
      val fieldListTree = allFields.foldLeft(List.empty[Tree]) { case (accList, fieldTree) =>
        fieldTree match {
          case q"${Modifiers(fs, nm, List(q"new mask()"))} val $field: $fieldType = $sth" =>
            accList :+ Literal(Constant("***"))
          case q"$m val $field: $fieldType = $sth" =>
            accList :+ q"$field.toString"
          case _ => c.abort(c.enclosingPosition, "Cannot call .toString on tree " + showRaw(fieldTree))
        }
      }
      val treesAsTuple = Apply(Select(Ident(TermName("scala")), TermName("Tuple" + fieldListTree.length)), fieldListTree)
      val typeNameStrTree = Literal(Constant(typeName.toString))
      q"""
         override def toString: ${typeOf[String]} = {
          $typeNameStrTree + $treesAsTuple
         }
      """
    }

    def modifiedDeclaration(classDecl: ClassDef) = {
      val (className, fields, parents, body) = extractCaseClassesParts(classDecl)
      val newToString = extractNewToString(className, fields)

      val params = fields.asInstanceOf[List[ValDef]] map { p => p.duplicate}

      c.Expr[Any](
        q"""
        case class $className ( ..$params ) extends ..$parents {
          $newToString
          ..$body
        }
      """
      )
    }

    annottees map (_.tree) toList match {
      case (classDecl: ClassDef) :: Nil =>
        modifiedDeclaration(classDecl)
      case (classDecl: ClassDef) :: anything =>
        modifiedDeclaration(classDecl)
      case other =>
        c.warning(c.enclosingPosition, showRaw(other))
        c.abort(c.enclosingPosition, "Invalid annottee, expected case class.")
    }
  }
}