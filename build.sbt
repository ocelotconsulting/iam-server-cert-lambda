
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

lazy val root = (project in file(".")).
  settings(
    name := "scala-letsencrypt-iam-lambda",
    version := "1.0",
    scalaVersion := "2.11.4",
    retrieveManaged := true,
    libraryDependencies ++= {
      Seq(
        "com.amazonaws" % "aws-lambda-java-core"   % "1.1.0",
        "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
        "com.fasterxml.jackson.module"  %% "jackson-module-scala" % "2.7.8" % "test",
        "com.typesafe" % "config" % "1.3.1"
      )
    }
  )

mergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
