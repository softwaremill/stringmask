package com.softwaremill.macros.customize.restricted
import com.softwaremill.macros.customize.{customize, mask}

@customize
private[restricted] case class ClassWithAccessControl(@mask value: String)

object RestrictedObjectProvider {
  def provide(): ClassWithAccessControl = ClassWithAccessControl("text")
}
