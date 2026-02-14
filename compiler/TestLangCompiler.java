package compiler;

import scanner.Lexer;
import parser.Parser;
import ast.ProgramNode;
import codegen.CodeGenerator;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.io.IOException;

/**
 * Main compiler entry point
 * Usage: java compiler.TestLangCompiler input.test output.java
 */
public class TestLangCompiler {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java compiler.TestLangCompiler <input.test> <output.java>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        String outputFile = args[1];
        
        try {
            compile(inputFile, outputFile);
            System.out.println("✓ Compilation successful!");
            System.out.println("  Input:  " + inputFile);
            System.out.println("  Output: " + outputFile);
        } catch (Exception e) {
            System.err.println("✗ Compilation failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void compile(String inputFile, String outputFile) throws Exception {
        // Step 1: Lexical Analysis (Scanning)
        System.out.println("[1/3] Scanning...");
        Lexer lexer = new Lexer(new FileReader(inputFile));
        
        // Step 2: Syntax Analysis (Parsing)
        System.out.println("[2/3] Parsing...");
        Parser parser = new Parser(lexer);
        Symbol parseResult = parser.parse();
        ProgramNode program = (ProgramNode) parseResult.value;
        
        // Validate AST
        validateProgram(program);
        
        // Step 3: Code Generation
        System.out.println("[3/3] Generating code...");
        CodeGenerator generator = new CodeGenerator(program);
        generator.generate(outputFile);
    }
    
    /**
     * Validate the parsed program
     */
    private static void validateProgram(ProgramNode program) {
        // Check that we have at least one test
        if (program.getTests().isEmpty()) {
            throw new RuntimeException("Program must contain at least one test block");
        }
        
        // Validate each test
        for (var test : program.getTests()) {
            if (test.getRequests().isEmpty()) {
                throw new RuntimeException("Test '" + test.getName() + 
                                         "' must contain at least one request");
            }
            if (test.getAssertions().size() < 2) {
                throw new RuntimeException("Test '" + test.getName() + 
                                         "' must contain at least 2 assertions");
            }
        }
    }
}