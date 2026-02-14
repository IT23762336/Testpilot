#!/bin/bash
# Run the TestLang++ compiler on a .test file

set -e

if [ $# -lt 1 ]; then
    echo "Usage: $0 <input.test> [output.java]"
    echo "Example: $0 input/all-tests.test output/GeneratedTests.java"
    echo ""
    echo "Note: Per assignment spec, all tests should go in ONE .test file"
    echo "      and generate a SINGLE GeneratedTests.java file"
    exit 1
fi

INPUT_FILE="$1"
OUTPUT_FILE="${2:-output/GeneratedTests.java}"

if [ ! -f "$INPUT_FILE" ]; then
    echo "Error: Input file '$INPUT_FILE' not found"
    exit 1
fi

# Ensure output directory exists
mkdir -p "$(dirname "$OUTPUT_FILE")"

# Paths
BUILD_DIR="build"
CUP_RUNTIME="lib/java-cup-11b-runtime.jar"

# Classpath separator (':' on Unix, ';' on Windows)
CPSEP=":"
case "$(uname -s)" in
    *MINGW*|*MSYS*|*CYGWIN*|*WindowsNT*)
        CPSEP=";"
        ;;
esac

if [ ! -d "$BUILD_DIR" ]; then
    echo "Build directory not found. Run ./scripts/compile.sh first"
    exit 1
fi

echo "=== Running TestLang++ Compiler ==="
echo "Input:  $INPUT_FILE"
echo "Output: $OUTPUT_FILE"
echo ""

java -cp "$BUILD_DIR${CPSEP}$CUP_RUNTIME" compiler.TestLangCompiler "$INPUT_FILE" "$OUTPUT_FILE"

echo ""
echo "✓ Generated: $OUTPUT_FILE"
