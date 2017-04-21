package com.softwaremill.macros.customize

import java.time.ZonedDateTime

import org.scalatest.{FlatSpec, Matchers}

class ToStringMaskTest extends FlatSpec with Matchers {

  behavior of "masking"

  @customize
  case class User20(id: Int, @mask name: String, @mask email: String, something: Long, @mask secretDob: ZonedDateTime)

  it should "mask fields" in {
    // given
    val u = User20(1, "Secret Person", "secret@email.com", 15, ZonedDateTime.now())

    // then
    u.toString should be("User20(1,***,***,15,***)")
  }

  it should "work on case classes which have custom companion objects" in {
    // given
    val u = User30(2)

    // then
    u.toString should be("User30(2,***)")
  }

  it should "work on case classes with private constructors" in {
    // given
    val u = UserPrivate("some@email2.com")

    // then
    u.toString should be("UserPrivate(1,***)")
  }

  it should "work on fields with null value" in {
    // given
    val u = User40(3, null, null)

    // then
    u.toString should be("User40(3,***,null)")
  }

  it should "work on classes without any fields" in {
    // given
    val ec = EmptyClass()

    // then
    ec.toString should be("EmptyClass()")
  }

  it should "work on classes with 1 field" in {
    // given
    val sc = SmallClass(100)

    // then
    sc.toString should be("SmallClass(***)")
  }

}

@customize
case class User30(id: Int, @mask email: String)

@customize
case class User40(id: Int, @mask email: String, something: String)

@customize
case class UserPrivate private (id: Int, @mask email: String)

@customize
case class EmptyClass()

@customize
case class SmallClass(@mask n: Int)

object UserPrivate {
  def apply(email: String) = new UserPrivate(1, email)
}

object User30 {
  def apply(id: Int) = new User30(id, "default@email.com")
}
