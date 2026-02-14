#!/bin/bash
# Setup script - downloads JFlex and CUP if not present

LIB_DIR="lib"
JFLEX_VERSION="1.9.1"
CUP_VERSION="11b"
JUNIT_VERSION="1.10.0"

mkdir -p "$LIB_DIR/junit"

# Download JFlex if not present
if [ ! -f "$LIB_DIR/jflex-full-$JFLEX_VERSION.jar" ]; then
    echo "Downloading JFlex $JFLEX_VERSION..."
    wget -O "$LIB_DIR/jflex-full-$JFLEX_VERSION.jar" \
        "https://repo1.maven.org/maven2/de/jflex/jflex/$JFLEX_VERSION/jflex-$JFLEX_VERSION.jar" 2>/dev/null || \
    curl -L -o "$LIB_DIR/jflex-full-$JFLEX_VERSION.jar" \
        "https://repo1.maven.org/maven2/de/jflex/jflex/$JFLEX_VERSION/jflex-$JFLEX_VERSION.jar"
else
    echo "✓ JFlex already downloaded"
fi

# Download CUP if not present
if [ ! -f "$LIB_DIR/java-cup-$CUP_VERSION.jar" ] || [ ! -f "$LIB_DIR/java-cup-$CUP_VERSION-runtime.jar" ]; then
    echo "Downloading CUP $CUP_VERSION..."
    TMP_TAR="/tmp/cup-$CUP_VERSION.tar.gz"
    wget -O "$TMP_TAR" \
        "http://www2.cs.tum.edu/projects/cup/releases/java-cup-bin-$CUP_VERSION-20160615.tar.gz" 2>/dev/null || \
    curl -L -o "$TMP_TAR" \
        "http://www2.cs.tum.edu/projects/cup/releases/java-cup-bin-$CUP_VERSION-20160615.tar.gz"
    
    # Extract the JAR files (they're at the root of the archive)
    tar -xzf "$TMP_TAR" -C "$LIB_DIR" 2>/dev/null
    
    # Rename files only if they don't already have the target name
    if [ -f "$LIB_DIR/java-cup-11b.jar" ] && [ ! -f "$LIB_DIR/java-cup-$CUP_VERSION.jar" ]; then
        mv "$LIB_DIR/java-cup-11b.jar" "$LIB_DIR/java-cup-$CUP_VERSION.jar"
    fi
    if [ -f "$LIB_DIR/java-cup-11b-runtime.jar" ] && [ ! -f "$LIB_DIR/java-cup-$CUP_VERSION-runtime.jar" ]; then
        mv "$LIB_DIR/java-cup-11b-runtime.jar" "$LIB_DIR/java-cup-$CUP_VERSION-runtime.jar"
    fi
    
    rm -f "$TMP_TAR"
else
    echo "✓ CUP already downloaded"
fi

# Download JUnit if not present
if [ ! -f "$LIB_DIR/junit/junit-platform-console-standalone-$JUNIT_VERSION.jar" ]; then
    echo "Downloading JUnit $JUNIT_VERSION..."
    wget -O "$LIB_DIR/junit/junit-platform-console-standalone-$JUNIT_VERSION.jar" \
        "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/$JUNIT_VERSION/junit-platform-console-standalone-$JUNIT_VERSION.jar" 2>/dev/null || \
    curl -L -o "$LIB_DIR/junit/junit-platform-console-standalone-$JUNIT_VERSION.jar" \
        "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/$JUNIT_VERSION/junit-platform-console-standalone-$JUNIT_VERSION.jar"
else
    echo "✓ JUnit already downloaded"
fi

echo ""
echo "✓ All dependencies ready in $LIB_DIR/"
echo ""
echo "Downloaded files:"
ls -lh "$LIB_DIR"/*.jar "$LIB_DIR/junit"/*.jar 2>/dev/null | awk '{print "  - " $9 " (" $5 ")"}'
