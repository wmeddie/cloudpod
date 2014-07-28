import play.PlayScala

name := """cloudpod"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    jdbc,
    cache,
    ws,
    "org.webjars" %% "webjars-play" % "2.3.0",
    "org.webjars" % "bootstrap" % "3.2.0",
    "org.webjars" % "angularjs" % "1.2.19",
    "org.webjars" % "jasmine" % "2.0.0",
    "org.webjars" % "angular-file-upload" % "1.6.2",
    "io.github.nremond" %% "pbkdf2-scala" % "0.4",
    "com.h2database" %  "h2" % "1.3.176",
    "mysql" % "mysql-connector-java" % "5.1.21",
    "org.scalikejdbc" %% "scalikejdbc-play-plugin" % "2.3.+",
    "org.skinny-framework" %% "skinny-orm" % "1.1.8",
    "com.github.seratch" %% "awscala" % "0.2.+",
    "com.mpatric" % "mp3agic" % "0.8.2",
    "com.typesafe.akka" %% "akka-testkit" % "2.3.3"
)

initialCommands := """
import scalikejdbc._
import skinny.orm._, feature._
import org.joda.time._
skinny.DBSettings.initialize()
implicit val session = AutoSession
"""

