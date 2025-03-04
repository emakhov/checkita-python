package com.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

object SampleSparkApp {
  def main(args: Array[String]): Unit = {
    // Create SparkSession with S3 configuration
    val spark = SparkSession.builder()
      .appName("Sample Spark Application")
      .master("local[*]")  // Use local mode with all available cores
      .config("spark.hadoop.fs.s3a.access.key", sys.env.getOrElse("AWS_ACCESS_KEY_ID", "minioadmin"))
      .config("spark.hadoop.fs.s3a.secret.key", sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", "minioadmin"))
      .config("spark.hadoop.fs.s3a.endpoint", sys.env.getOrElse("AWS_ENDPOINT", "http://minio:9000"))
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
      .getOrCreate()

    // Sample sales data
    val salesData = Seq(
      ("2024-01-01", "Electronics", "Laptop", 1200.0, 5),
      ("2024-01-01", "Electronics", "Phone", 800.0, 10),
      ("2024-01-02", "Books", "Programming Guide", 50.0, 20),
      ("2024-01-02", "Electronics", "Tablet", 600.0, 8),
      ("2024-01-03", "Books", "Science Fiction", 25.0, 15),
      ("2024-01-03", "Electronics", "Headphones", 100.0, 25)
    )

    // Create DataFrame with schema
    val salesDF = spark.createDataFrame(salesData)
      .toDF("date", "category", "product", "price", "quantity")

    // Register as temp view for SQL queries
    salesDF.createOrReplaceTempView("sales")

    println("Original Sales Data:")
    salesDF.show()

    // Example 1: Basic aggregations
    println("\nTotal sales by category:")
    val categorySales = salesDF.groupBy("category")
      .agg(
        sum(col("price") * col("quantity")).alias("total_sales"),
        avg("price").alias("avg_price"),
        sum("quantity").alias("total_quantity")
      )
    
    categorySales.show()

    // Example 2: Window functions
    println("\nProduct rank by price within category:")
    val windowSpec = Window.partitionBy("category").orderBy(col("price").desc)
    val rankedProducts = salesDF.withColumn("price_rank", rank().over(windowSpec))
    rankedProducts.show()

    // Example 3: SQL query example
    println("\nTop selling products by quantity (using SQL):")
    val topProducts = spark.sql("""
      SELECT product, category, SUM(quantity) as total_quantity
      FROM sales
      GROUP BY product, category
      ORDER BY total_quantity DESC
      LIMIT 3
    """)
    topProducts.show()

    // Example 4: Data transformations and write to S3
    println("\nWriting transformed data to S3:")
    val transformedDF = salesDF
      .withColumn("total_amount", col("price") * col("quantity"))
      .withColumn("date", to_date(col("date")))
      .withColumn("is_expensive", when(col("price") > 500, true).otherwise(false))

    // Write DataFrames to S3 in Parquet format
    transformedDF.write
      .mode("overwrite")
      .parquet("s3a://spark-data/transformed_sales")

    categorySales.write
      .mode("overwrite")
      .parquet("s3a://spark-data/category_sales")

    rankedProducts.write
      .mode("overwrite")
      .parquet("s3a://spark-data/ranked_products")

    topProducts.write
      .mode("overwrite")
      .parquet("s3a://spark-data/top_products")

    println("Data has been written to S3 successfully!")

    // Clean up
    spark.stop()
  }
} 