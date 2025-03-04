FROM mcr.microsoft.com/devcontainers/python:3.9-bullseye

# Install system dependencies
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    software-properties-common \
    openjdk-11-jdk \
    git \
    gnupg \
    && rm -rf /var/lib/apt/lists/*

# Arguments for version control
ARG SPARK_VERSION="3.5.5"
ARG SCALA_VERSION="2.13.12"

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV SPARK_HOME=/opt/spark
ENV PATH=$PATH:$SPARK_HOME/bin

# Download and install Coursier
RUN curl -fL "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz" | gzip -d > /usr/local/bin/cs \
    && chmod +x /usr/local/bin/cs

# Install Scala and SBT using Coursier
RUN /usr/local/bin/cs setup --yes \
    && /usr/local/bin/cs install --install-dir /usr/local/bin scala:${SCALA_VERSION} sbt

# Add Coursier-installed binaries to PATH
ENV PATH=$PATH:/root/.local/share/coursier/bin

# Download and install Spark
RUN wget https://downloads.apache.org/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && tar -xzf spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && mv spark-${SPARK_VERSION}-bin-hadoop3 ${SPARK_HOME} \
    && rm spark-${SPARK_VERSION}-bin-hadoop3.tgz

# Set working directory
WORKDIR /workspace

# Default command
CMD ["/bin/bash"]