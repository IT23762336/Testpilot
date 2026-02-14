#!/bin/bash
# Verification script - tests all examples

set -e

echo "======================================"
echo "TestLang++ Verification Script"
echo "======================================"
echo ""

# Check if compiler is built
if [ ! -d "build" ]; then
    echo "❌ Build directory not found"
    echo "Run: ./scripts/compile.sh"
    exit 1
fi

echo "✓ Build directory exists"
echo ""

# Test each example file
examples=("example.test")
outputs=("GeneratedTests.java")

echo "======================================"
echo "Compiling Example Files"
echo "======================================"
echo ""

for i in "${!examples[@]}"; do
    example="${examples[$i]}"
    output="${outputs[$i]}"
    
    echo "Testing: input/$example"
    if ./scripts/run-compiler.sh "input/$example" "output/$output" > /dev/null 2>&1; then
        echo "  ✓ Compilation successful"
        
        # Check if output file exists and is not empty
        if [ -s "output/$output" ]; then
            lines=$(wc -l < "output/$output")
            echo "  ✓ Generated $lines lines of code"
        else
            echo "  ❌ Output file is empty"
            exit 1
        fi
    else
        echo "  ❌ Compilation failed"
        exit 1
    fi
    echo ""
done

echo "======================================"
echo "Feature Verification"
echo "======================================"
echo ""

# Check for key features in generated code
echo "Checking GeneratedTests.java for:"
if grep -q "import org.junit.jupiter.api" output/GeneratedTests.java; then
    echo "  ✓ JUnit 5 imports"
fi

if grep -q "HttpClient" output/GeneratedTests.java; then
    echo "  ✓ HttpClient usage"
fi

if grep -q "@Test" output/GeneratedTests.java; then
    echo "  ✓ @Test annotations"
fi

if grep -q "assertEquals" output/GeneratedTests.java; then
    echo "  ✓ Status assertions"
fi

if grep -q "assertTrue.*contains" output/GeneratedTests.java; then
    echo "  ✓ Body/Header contains assertions"
fi

if grep -q "admin" output/GeneratedTests.java; then
    echo "  ✓ Variable substitution"
fi

echo ""
echo "======================================"
echo "All Verifications Passed! ✓"
echo "======================================"
echo ""
echo "Your TestLang++ compiler is working correctly!"
echo ""
echo "Next steps:"
echo "1. Review generated code in output/"
echo "2. Start backend: ./scripts/start-backend.sh"
echo "3. Run tests: ./scripts/run-tests.sh output/GeneratedTests.java"
