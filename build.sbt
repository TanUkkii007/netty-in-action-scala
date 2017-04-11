name := "netty-in-action-scala"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "io.netty" % "netty-all" % "4.1.9.Final",
  "com.typesafe" % "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)