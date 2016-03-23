organization := "com.softwaremill.stringmask"
name := "stringmask"

version := "1.0.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.7")

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.typelevel" %% "macro-compat" % "1.1.1",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
