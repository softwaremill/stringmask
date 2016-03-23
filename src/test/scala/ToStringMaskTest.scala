import java.time.ZonedDateTime

import com.softwaremill.tostringmask.{customize, mask}
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
}
