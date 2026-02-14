#!/bin/bash
# Compile and run the generated JUnit tests

set -e

if [ $# -lt 1 ]; then
    echo "Usage: $0 <GeneratedTests.java>"
    echo "Example: $0 output/GeneratedTests.java"
    exit 1
fi

GENERATED_FILE="$1"

if [ ! -f "$GENERATED_FILE" ]; then
    echo "Error: Generated test file '$GENERATED_FILE' not found"
    exit 1
fi

# Download JUnit 5 JARs if not present
JUNIT_DIR="lib/junit"
mkdir -p "$JUNIT_DIR"

if [ ! -f "$JUNIT_DIR/junit-platform-console-standalone-1.10.0.jar" ]; then
    echo "Downloading JUnit 5..."
    wget -O "$JUNIT_DIR/junit-platform-console-standalone-1.10.0.jar" \
        "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.0/junit-platform-console-standalone-1.10.0.jar"
else
    echo "✓ JUnit already downloaded"
fi

JUNIT_JAR="$JUNIT_DIR/junit-platform-console-standalone-1.10.0.jar"
TEST_BUILD="build/tests"
mkdir -p "$TEST_BUILD"

echo "=== Compiling Generated Tests ==="
javac -d "$TEST_BUILD" -cp "$JUNIT_JAR" "$GENERATED_FILE"

echo ""
echo "=== Running JUnit Tests ==="
java -jar "$JUNIT_JAR" --class-path "$TEST_BUILD" --scan-class-path | sed 's/\[OK\]/✓/g; s/\[X\]/✗/g'

echo ""
echo "✓ Tests completed!"
