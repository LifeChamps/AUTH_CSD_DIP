//name := "lifechamps_flink"

ThisBuild / version := "0.1"

//scalaVersion := "2.12.12"

ThisBuild / scalaVersion := "2.12.12"

ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal,
  "MQTT Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/"
)


val flinkVersion = "1.13.1"
//% "provided"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "compile",
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion % "compile",
  "org.apache.flink" %% "flink-clients" % flinkVersion % "compile",
  "org.apache.flink" %% "flink-cep-scala" % flinkVersion % "compile",
)
libraryDependencies += "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.2.0"
libraryDependencies += "org.bouncycastle" % "bcprov-jdk16" % "1.45"
//libraryDependencies += "net.liftweb" %% "lift-json" % "3.4.0"
//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M1.1"
//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-modelimport" % "1.0.0-M1.1"
//libraryDependencies += "org.nd4j" % "nd4j-native-platform" % "1.0.0-M1.1"



libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.5"
libraryDependencies += "org.openjdk.jol" % "jol-core" % "0.16"

libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.13"

libraryDependencies += "com.lihaoyi" %% "upickle" % "2.0.0"

// For the tests
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
libraryDependencies +="org.apache.flink" %% "flink-test-utils" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-runtime" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-streaming-java" % flinkVersion % "test" classifier "tests"
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.3.0.0-SNAP3" % Test

ThisBuild / resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"


lazy val root = (project in file("."))
  .settings(
    name := "lifechamps_flink",
    libraryDependencies ++= flinkDependencies
  )

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

Test / parallelExecution := false
assembly / assemblyJarName := "lc_data_ingestion.jar"
assembly / mainClass := Some("org.auth.csd.datalab.Main_Module")