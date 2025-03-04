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
ARG HADOOP_VERSION="3.4.1"

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV SPARK_HOME=/opt/spark
ENV HADOOP_HOME=/opt/hadoop
ENV PATH=$PATH:$SPARK_HOME/bin:$HADOOP_HOME/bin

# Download and install Coursier
RUN curl -fL "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz" | gzip -d > /usr/local/bin/cs \
    && chmod +x /usr/local/bin/cs

# Download and install Hadoop
RUN wget https://downloads.apache.org/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz \
    && tar -xzf hadoop-${HADOOP_VERSION}.tar.gz \
    && mv hadoop-${HADOOP_VERSION} ${HADOOP_HOME} \
    && rm hadoop-${HADOOP_VERSION}.tar.gz

# Configure Hadoop
RUN echo "export HADOOP_HOME=${HADOOP_HOME}" >> /etc/profile.d/hadoop.sh \
    && echo "export PATH=\$PATH:\$HADOOP_HOME/bin" >> /etc/profile.d/hadoop.sh \
    && echo "export HADOOP_CONF_DIR=\$HADOOP_HOME/etc/hadoop" >> /etc/profile.d/hadoop.sh \
    && echo "export HADOOP_CLASSPATH=\$HADOOP_HOME/share/hadoop/tools/lib/*" >> /etc/profile.d/hadoop.sh

# Download and install Spark
RUN wget https://downloads.apache.org/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && tar -xzf spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && mv spark-${SPARK_VERSION}-bin-hadoop3 ${SPARK_HOME} \
    && rm spark-${SPARK_VERSION}-bin-hadoop3.tgz

# Set working directory
WORKDIR /workspace

# Default command
CMD ["/bin/bash"]