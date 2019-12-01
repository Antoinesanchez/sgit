import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "1.0"

lazy val root = (project in file("."))
  .settings(
    name := "sgit",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalactic,
      scopt,
      better
    )
  )

parallelExecution in Test := false