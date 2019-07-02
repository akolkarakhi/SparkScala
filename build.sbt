name := "someproject"

version := "0.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.1"// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.3.1" % "runtime"


scalaVersion := "2.11.12"