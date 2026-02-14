#!/bin/bash
# Compile the TestLang++ compiler: Scanner + Parser + Code Generator

set -e

echo "=== Compiling TestLang++ Compiler ==="

# Paths
LIB_DIR="lib"
BUILD_DIR="build"
JFLEX_JAR="$LIB_DIR/jflex-full-1.9.1.jar"
CUP_JAR="$LIB_DIR/java-cup-11b.jar"
CUP_RUNTIME="$LIB_DIR/java-cup-11b-runtime.jar"

# Classpath separator: ':' on Unix-like, ';' on Windows (msys/cygwin)
CPSEP=":"
case "$(uname -s)" in
    *MINGW*|*MSYS*|*CYGWIN*|*WindowsNT*)
        CPSEP=";"
        ;;
esac

# Check if dependencies exist
if [ ! -f "$JFLEX_JAR" ] || [ ! -f "$CUP_JAR" ]; then
    echo "Dependencies not found. Run ./scripts/setup-deps.sh first"
    exit 1
fi

# Create build directory
mkdir -p "$BUILD_DIR"

echo "[1/5] Generating Scanner with JFlex..."
java -cp "$JFLEX_JAR${CPSEP}$CUP_RUNTIME" jflex.Main -d scanner scanner/lexer.flex

echo "[2/5] Generating Parser with CUP..."
java -cp "$CUP_JAR${CPSEP}$CUP_RUNTIME" java_cup.Main -destdir parser -parser Parser -symbols sym parser/parser.cup

echo "[3/5] Compiling AST classes..."
javac -d "$BUILD_DIR" -cp "$CUP_RUNTIME" ast/*.java

echo "[4/5] Compiling Scanner and Parser..."
javac -d "$BUILD_DIR" -cp "$CUP_RUNTIME${CPSEP}$BUILD_DIR" scanner/*.java parser/*.java

echo "[5/5] Compiling Code Generator and Compiler..."
javac -d "$BUILD_DIR" -cp "$CUP_RUNTIME${CPSEP}$BUILD_DIR" codegen/*.java compiler/*.java

echo "✓ Compilation successful! Output in $BUILD_DIR/"
