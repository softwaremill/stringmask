stringmask
==========
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.softwaremill.strignmask/stringmask_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.softwaremill.stringmask/stringmask_2.11)  

A micro-library for macro-based case class field masking in `.toString()`.
Inspired by blog by Juan Pedro Moreno @47deg: http://www.47deg.com/blog/scala-macros-annotate-your-case-classes

#Using stringmask
Builds are available for Scala 2.10.x and 2.11.x 

````scala
libraryDependencies ++= Seq(
  "com.softwaremill.stringmask" %% "stringmask" % "1.0.0",
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)
````

Example usage:

````scala
import com.softwaremill.macros.customize.{customize, mask}
import java.util.Date

@customize
case class User(id: Long, @mask email: String, @mask dob: Date)
val user = User(1, "johndoe@email.com", new Date(1458732695220L))

user.toString
````

should return `User(1,***,***)`