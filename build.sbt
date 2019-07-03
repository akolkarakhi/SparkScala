name := "someproject"

version := "0.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.3.1" % "runtime"
libraryDependencies += "org.vegas-viz" %% "vegas" % "0.3.11"


scalaVersion := "2.11.12"