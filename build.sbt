import sbt.Keys._
import sbt._

name := """to-do-sample"""
organization := "com.example"
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalaVersion := "2.12.8"
libraryDependencies += guice
resolvers ++= Seq(
  "IxiaS Releases" at "http://maven.ixias.net.s3-ap-northeast-1.amazonaws.com/releases"
)

libraryDependencies ++= Seq(
  guice,
  evolutions,
  "net.ixias"              %% "ixias"                 % "1.1.36",
  "net.ixias"              %% "ixias-aws"             % "1.1.36",
  "net.ixias"              %% "ixias-play"            % "1.1.36",
  "ch.qos.logback"          % "logback-classic"       % "1.1.+",

  // 最新のplay-slickを指定。
  "org.scalatestplus.play" %% "scalatestplus-play"    % "5.0.0" % Test,

  "com.typesafe.play"      %% "play-slick"            % "5.0.0",

  // https://github.com/playframework/play-slick#all-releases
  "com.typesafe.play"      %% "play-slick-evolutions" % "5.0.0",

  // play-slickの5.0.0ではslick 3.3.2を利用しているため、codegenも同様に3.3.2を指定しています。
  "com.typesafe.slick"     %% "slick-codegen"         % "3.3.2",

  "mysql"                   % "mysql-connector-java"  % "5.1.+",

  "com.typesafe"            % "config"                % "1.4.0"
)

// add code generation task
lazy val slickCodeGen = taskKey[Unit]("execute Slick CodeGen")
slickCodeGen         := (runMain in Compile).toTask(" tasks.CustomSlickCodeGen").value