organization := "com.softwaremill.stringmask"
name := "stringmask"

scalaVersion := "2.12.6"

crossScalaVersions := Seq(scalaVersion.value, "2.10.6", "2.11.11")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect"  % scalaVersion.value,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.typelevel"  %% "macro-compat"  % "1.1.1",
  "org.scalatest"  %% "scalatest"     % "3.0.5" % "test"
)

smlBuildSettings

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
