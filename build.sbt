name := "spark-example"
version := "0.1"
scalaVersion := "2.12.18"

resolvers ++= Seq(
  "Maven Central" at "https://repo1.maven.org/maven2/"
)

// Use provided scope for Spark dependencies since they're available in the container
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.5" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.5.5" % "provided",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.apache.hadoop" % "hadoop-aws" % "3.4.1",
  "com.amazonaws" % "aws-java-sdk-bundle" % "1.12.261"
)

// Add Spark and Hadoop jars to the classpath
Compile / unmanagedJars ++= {
  val sparkHome = sys.env.getOrElse("SPARK_HOME", "/opt/spark")
  val hadoopHome = sys.env.getOrElse("HADOOP_HOME", "/opt/hadoop")
  val sparkJars = (file(sparkHome) / "jars").listFiles()
  val hadoopJars = (file(hadoopHome) / "share/hadoop/common").listFiles() ++
                   (file(hadoopHome) / "share/hadoop/hdfs").listFiles() ++
                   (file(hadoopHome) / "share/hadoop/mapreduce").listFiles() ++
                   (file(hadoopHome) / "share/hadoop/tools/lib").listFiles()
  (sparkJars ++ hadoopJars).filter(_.getName.endsWith(".jar")).toSeq
} 