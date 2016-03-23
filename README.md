# stringmask
A micro-library for macro-based case class field masking in .toString

Example usage:
```scala
import com.softwaremill.tostringmask.{customize, mask}
import java.util.Date

@customize
case class User(id: Long, @mask email: String, @mask dob: Date)
val user = User(1, "johndoe@email.com, new Date(1458732695220L))

user.toString
```
should return `User(1,***,***)`
