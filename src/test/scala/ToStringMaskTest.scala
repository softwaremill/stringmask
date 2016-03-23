import java.time.ZonedDateTime
import java.util.Date

import com.softwaremill.macros.customize.{customize, mask}
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
}

@customize
case class User30(id: Int, @mask email: String)

object User30 {
  def apply(id: Int) = new User30(id, "default@email.com")
}