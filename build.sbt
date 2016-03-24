organization := "com.softwaremill.stringmask"
name := "stringmask"

version := "1.1.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.8")

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.typelevel" %% "macro-compat" % "1.1.1",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra :=
  <scm>
    <url>git@github.com:softwaremill/stringmask.git</url>
    <connection>scm:git:git@github.com:softwaremill/stringmask.git</connection>
  </scm>
    <developers>
      <developer>
        <id>kciesielski</id>
        <name>Krzysztof Ciesielski</name>
      </developer>
    </developers>
licenses := ("Apache2", new java.net.URL("http://www.apache.org/licenses/LICENSE-2.0.txt")) :: Nil
homepage := Some(new java.net.URL("http://www.softwaremill.com"))

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
