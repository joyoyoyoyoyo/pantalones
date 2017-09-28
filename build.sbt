name := "pantalones"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.2"
)

mainClass in (Compile, run) := Some("ValidateApprovals")
fork in (Test, run) := true


logLevel in (Compile, run) := Level.Error
logLevel := Level.Error