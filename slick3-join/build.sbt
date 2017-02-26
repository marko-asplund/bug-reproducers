organization  := "com.practicingtechie"

version       := "0.4.2"

scalaVersion  := "2.11.8"

scalacOptions := Seq("-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.postgresql" % "postgresql" % "42.0.0",
  "com.typesafe.slick" %% "slick" % "3.2.0-RC1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0-RC1"
)

