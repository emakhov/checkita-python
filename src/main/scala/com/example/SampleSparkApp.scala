package com.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

object SampleSparkApp {
  def main(args: Array[String]): Unit = {
    // Create SparkSession
    val spark = SparkSession.builder()
      .appName("Sample Spark Application")
      .master("local[*]")  // Use local mode with all available cores
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
    salesDF.groupBy("category")
      .agg(
        sum(col("price") * col("quantity")).alias("total_sales"),
        avg("price").alias("avg_price"),
        sum("quantity").alias("total_quantity")
      )
      .show()

    // Example 2: Window functions
    println("\nProduct rank by price within category:")
    val windowSpec = Window.partitionBy("category").orderBy(col("price").desc)
    salesDF.withColumn("price_rank", rank().over(windowSpec))
      .show()

    // Example 3: SQL query example
    println("\nTop selling products by quantity (using SQL):")
    spark.sql("""
      SELECT product, category, SUM(quantity) as total_quantity
      FROM sales
      GROUP BY product, category
      ORDER BY total_quantity DESC
      LIMIT 3
    """).show()

    // Example 4: Data transformations
    println("\nSales data with derived columns:")
    salesDF
      .withColumn("total_amount", col("price") * col("quantity"))
      .withColumn("date", to_date(col("date")))
      .withColumn("is_expensive", when(col("price") > 500, true).otherwise(false))
      .show()

    // Clean up
    spark.stop()
  }
} 