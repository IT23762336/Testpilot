package codegen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ast.AssertionNode;
import ast.ConfigNode;
import ast.HeaderNode;
import ast.ProgramNode;
import ast.RequestNode;
import ast.TestNode;

/**
 * Generates JUnit 5 test code from AST
 */
public class CodeGenerator {
    private ProgramNode program;
    private VariableSubstitutor substitutor;
    private PrintWriter writer;
    private int indentLevel = 0;
    private String className = "GeneratedTests";
    
    public CodeGenerator(ProgramNode program) {
        this.program = program;
        this.substitutor = new VariableSubstitutor(program.getVariables());
    }
    
    /**
     * Generate code and write to file
     */
    public void generate(String outputPath) throws IOException {
        // Extract class name from output path
        String fileName = outputPath;
        if (fileName.contains("/") || fileName.contains("\\")) {
            fileName = fileName.substring(Math.max(
                fileName.lastIndexOf("/"), 
                fileName.lastIndexOf("\\")
            ) + 1);
        }
        if (fileName.endsWith(".java")) {
            className = fileName.substring(0, fileName.length() - 5);
        }
        
        writer = new PrintWriter(new FileWriter(outputPath));
        
        try {
            generateImports();
            generateClassHeader();
            generateStaticFields();
            generateSetupMethod();
            generateTestMethods();
            generateClassFooter();
        } finally {
            writer.close();
        }
    }
    
    private void generateImports() {
        println("import org.junit.jupiter.api.*;");
        println("import static org.junit.jupiter.api.Assertions.*;");
        println("import java.net.http.*;");
        println("import java.net.*;");
        println("import java.time.Duration;");
        println("import java.nio.charset.StandardCharsets;");
        println("import java.util.*;");
        println();
    }
    
    private void generateClassHeader() {
        println("public class " + className + " {");
        indentLevel++;
    }
    
    private void generateClassFooter() {
        indentLevel--;
        println("}");
    }
    
    private void generateStaticFields() {
        ConfigNode config = program.getConfig();
        
        // Base URL
        if (config != null && config.getBaseUrl() != null) {
            println("static String BASE = \"" + config.getBaseUrl() + "\";");
        } else {
            println("static String BASE = \"\";");
        }
        
        // Default headers
        println("static Map<String, String> DEFAULT_HEADERS = new HashMap<>();");
        println("static HttpClient client;");
        println();
    }
    
    private void generateSetupMethod() {
        println("@BeforeAll");
        println("static void setup() {");
        indentLevel++;
        
        println("client = HttpClient.newBuilder()");
        indentLevel++;
        println(".connectTimeout(Duration.ofSeconds(5))");
        println(".build();");
        indentLevel--;
        
        // Add default headers from config
        ConfigNode config = program.getConfig();
        if (config != null) {
            for (HeaderNode header : config.getDefaultHeaders()) {
                println("DEFAULT_HEADERS.put(\"" + escapeJava(header.getKey()) + "\", \"" + 
                       escapeJava(header.getValue()) + "\");");
            }
        }
        
        indentLevel--;
        println("}");
        println();
    }
    
    private void generateTestMethods() {
        for (TestNode test : program.getTests()) {
            generateTestMethod(test);
        }
    }
    
    private void generateTestMethod(TestNode test) {
        // Add description as a comment if present
        if (test.getDescription() != null && !test.getDescription().trim().isEmpty()) {
            println("// " + test.getDescription());
        }
        
        println("@Test");
        println("void test_" + test.getName() + "() throws Exception {");
        indentLevel++;
        
        // Generate requests and assertions
        RequestNode lastRequest = null;
        
        for (RequestNode request : test.getRequests()) {
            lastRequest = request;
            generateRequest(request);
            println();
        }
        
        // Generate assertions (they apply to the last request)
        for (AssertionNode assertion : test.getAssertions()) {
            generateAssertion(assertion);
        }
        
        indentLevel--;
        println("}");
        println();
    }
    
    private void generateRequest(RequestNode request) {
        String path = substitutor.substitute(request.getPath());
        
        // Determine full URL
        String url;
        if (path.startsWith("http://") || path.startsWith("https://")) {
            url = path;
        } else {
            url = "BASE + \"" + escapeJava(path) + "\"";
        }
        
        // Create request builder
        println("HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(" + url + "))");
        indentLevel++;
        println(".timeout(Duration.ofSeconds(10))");
        
        // HTTP method and body
        switch (request.getMethod()) {
            case GET:
                println(".GET();");
                break;
            case DELETE:
                println(".DELETE();");
                break;
            case POST:
                if (request.hasBody()) {
                    String body = substitutor.substitute(request.getBody());
                    println(".POST(HttpRequest.BodyPublishers.ofString(\"" + 
                           escapeJava(body) + "\", StandardCharsets.UTF_8));");
                } else {
                    println(".POST(HttpRequest.BodyPublishers.noBody());");
                }
                break;
            case PUT:
                if (request.hasBody()) {
                    String body = substitutor.substitute(request.getBody());
                    println(".PUT(HttpRequest.BodyPublishers.ofString(\"" + 
                           escapeJava(body) + "\", StandardCharsets.UTF_8));");
                } else {
                    println(".PUT(HttpRequest.BodyPublishers.noBody());");
                }
                break;
        }
        indentLevel--;
        
        // Add request-specific headers
        for (HeaderNode header : request.getHeaders()) {
            println("b.header(\"" + escapeJava(header.getKey()) + "\", \"" + 
                   escapeJava(header.getValue()) + "\");");
        }
        
        // Add default headers
        println("for (var e : DEFAULT_HEADERS.entrySet()) {");
        indentLevel++;
        println("b.header(e.getKey(), e.getValue());");
        indentLevel--;
        println("}");
        
        // Send request
        println("HttpResponse<String> resp = client.send(b.build(), " +
               "HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));");
    }
    
    private void generateAssertion(AssertionNode assertion) {
        switch (assertion.getType()) {
            case STATUS:
                println("assertEquals(" + assertion.getExpectedStatusCode() + 
                       ", resp.statusCode());");
                break;
                
            case STATUS_RANGE:
                println("assertTrue(resp.statusCode() >= " + assertion.getRangeMin() + 
                       " && resp.statusCode() <= " + assertion.getRangeMax() + 
                       ", \"Status code should be in range " + assertion.getRangeMin() + 
                       ".." + assertion.getRangeMax() + " but was \" + resp.statusCode());");
                break;
                
            case HEADER_EQUALS:
                println("assertEquals(\"" + escapeJava(assertion.getExpectedValue()) + "\", " +
                       "resp.headers().firstValue(\"" + escapeJava(assertion.getHeaderKey()) + 
                       "\").orElse(\"\"));");
                break;
                
            case HEADER_CONTAINS:
                println("assertTrue(resp.headers().firstValue(\"" + 
                       escapeJava(assertion.getHeaderKey()) + "\").orElse(\"\").contains(\"" +
                       escapeJava(assertion.getExpectedValue()) + "\"));");
                break;
                
            case BODY_CONTAINS:
                String expectedValue = substitutor.substitute(assertion.getExpectedValue());
                println("assertTrue(resp.body().contains(\"" + 
                       escapeJava(expectedValue) + "\"));");
                break;
        }
    }
    
    // Helper methods
    private void println(String line) {
        for (int i = 0; i < indentLevel; i++) {
            writer.print("    ");
        }
        writer.println(line);
    }
    
    private void println() {
        writer.println();
    }
    
    /**
     * Escape special characters for Java strings
     */
    private String escapeJava(String input) {
        if (input == null) {
            return "";
        }
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}