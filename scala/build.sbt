import sbt._
import scalariform.formatter.preferences._

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "wikipedia",
      scalaVersion := "2.12.11",
      version      := "1.0.0"
    )),
    name := "mostvisited",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.4.6",
      "org.apache.spark" %% "spark-sql" % "2.4.6",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
