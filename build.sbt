name := """play-forms-tutorial"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.7"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
routesGenerator := InjectedRoutesGenerator
libraryDependencies += filters
