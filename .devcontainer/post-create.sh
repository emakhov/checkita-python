#!/bin/bash

sudo apt-get update

# Initialize sbt (downloads dependencies)
sbt sbtVersion

# Set appropriate permissions
# sudo chown -R $USER:$USER /workspace

# Print versions for verification
echo "Checking installed versions:"
echo "Java version:"
java -version
echo "Scala version:"
scala -version
echo "SBT version:"
sbt --version