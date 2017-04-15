
lazy val buildSettings = Seq(
  organization := "com.github.saint",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1")
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Xlint"
)

lazy val publishSettings = Seq(
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  publishMavenStyle := true,
  publishArtifact := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  pomExtra :=
    <developers>
      <developer>
        <id>Saint1991</id>
        <name>Seiya Mizuno</name>
      </developer>
    </developers>
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
) ++ buildSettings ++ publishSettings


lazy val util = (project in file("util"))
  .settings(commonSettings)
  .settings(
    name := "ml-scala-util",
    libraryDependencies ++= Seq(

    )
  )

lazy val kmeans = (project in file("clustering/kmeans"))
  .settings(buildSettings)
  .settings(publishSettings)
  .settings(
    name := "ml-scala-kmeans",
    libraryDependencies ++= Seq(

    )
  )
  .dependsOn(util % "compile->compile;test->test")
