import sbtrelease.Version
import ReleaseTransformations._

lazy val sonatypePassword = sys.env.get("SONATYPE_PASS")

lazy val buildSettings = Seq(
  organization := "com.github.saint1991",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")
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

val publishSettings = Seq(
  publishArtifact := true,
  publishArtifact in Test := false,
  publishMavenStyle := true,
  credentials ++= Seq(sonatypePassword match {
    case Some(pass) => Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", "Saint1991", pass)
    case None => Credentials(Path.userHome / ".sbt" / ".sonatype")
  }),
  useGpg := true,
  pgpReadOnly := false,
  pgpPublicRing := Path.userHome / ".sbt" / "gpg" / "pubring.asc",
  pgpSecretRing := Path.userHome / ".sbt" / "gpg" / "secring.asc",
  scmInfo := Some(ScmInfo(url("https://github.com/Saint1991/ml-scala"), "scm:git:git@github.com:Saint1991/ml-scala.git")),
  releaseCrossBuild := true,
  releaseVersion := { ver => Version(ver).map(_.withoutQualifier.string).getOrElse(throw new Exception("Version format error")) },
  releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.asSnapshot.string).getOrElse(throw new Exception("Version format error")) },
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    pushChanges,
    ReleaseStep(action = Command.process("sonatypeRelease", _))
  ),
  sonatypeProfileName := "com.github.saint1991",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  publishTo in ThisBuild := Some(
    if (version.value.trim.endsWith("SNAPSHOT") || isSnapshot.value) Opts.resolver.sonatypeSnapshots
    else Opts.resolver.sonatypeStaging
  ),
  licenses in ThisBuild := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  pomExtra in ThisBuild :=
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

lazy val mlScala = (project in file("."))
  .aggregate(util, kmeans)
  .dependsOn(util, kmeans)
  .settings(commonSettings)

lazy val util = (project in file("util"))
  .settings(commonSettings)
  .settings(
    name := "ml-scala-util"
  )

lazy val kmeans = (project in file("clustering/kmeans"))
  .settings(commonSettings)
  .settings(
    name := "ml-scala-kmeans"
  )
  .dependsOn(util % "compile->compile;test->test")
