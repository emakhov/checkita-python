# Spark Development Environment

This repository contains a development container setup for working with Apache Spark, Python, and Scala. It includes:
- Java 11
- Python 3
- Scala 2.12.18
- Apache Spark 3.5.2
- MinIO (S3-compatible storage)
- Apache Hive Metastore
- MySQL (for Hive Metastore)

## Prerequisites

- Docker
- Docker Compose
- VS Code with Remote - Containers extension

## Getting Started

1. Clone this repository
2. Open the repository in VS Code
3. When prompted, click "Reopen in Container"
4. Wait for the container to build and start

## Services

The following services are available:
- MinIO (S3): http://localhost:9000
  - Access Key: test
  - Secret Key: test
- Hive Metastore: localhost:9083
- MySQL: localhost:3306

## Sample Application

The repository includes a sample Spark application in Scala that demonstrates reading and writing data to S3.

To run the sample application:

1. First, create a bucket in MinIO:
```bash
mc alias set myminio http://localhost:9000 test test
mc mb myminio/test-bucket
```

2. Build and run the Scala application:
```bash
sbt clean package
spark-submit --class com.example.S3Example target/scala-2.12/spark-s3-example_2.12-0.1.jar
```

## Development

- Python packages are installed from `requirements.txt`
- Scala dependencies are managed by SBT in `build.sbt`
- The development container includes VS Code extensions for Python, Scala, and Java development

## Project Structure

```
.
├── .devcontainer/          # Development container configuration
├── src/                    # Source code
│   └── main/
│       └── scala/         # Scala source files
├── build.sbt              # SBT build configuration
└── requirements.txt       # Python dependencies
```