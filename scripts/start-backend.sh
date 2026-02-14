#!/bin/bash
# Start the Spring Boot backend server

set -e

BACKEND_DIR="backend"

if [ ! -d "$BACKEND_DIR" ]; then
    echo "Error: Backend directory not found"
    exit 1
fi

cd "$BACKEND_DIR"

# Check if JAR exists
JAR_FILE="target/testlang-demo-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "Backend JAR not found. Building with Maven..."
    
    if ! command -v mvn &> /dev/null; then
        echo "Error: Maven not installed. Please install Maven first."
        exit 1
    fi
    
    mvn clean package -DskipTests
fi

echo "=== Starting Backend Server ==="
echo "Server will be available at: http://localhost:8080"
echo ""

java -jar "$JAR_FILE"
