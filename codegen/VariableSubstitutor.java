package codegen;

import ast.VariableNode;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles variable substitution in strings and paths
 * Replaces $varName with actual values
 */
public class VariableSubstitutor {
    private Map<String, VariableNode> variables;
    private static final Pattern VAR_PATTERN = Pattern.compile("\\$([A-Za-z_][A-Za-z0-9_]*)");
    
    public VariableSubstitutor(Map<String, VariableNode> variables) {
        this.variables = variables;
    }
    
    /**
     * Substitute all variables in a string
     * Example: "/api/users/$id" with id=42 becomes "/api/users/42"
     */
    public String substitute(String input) {
        if (input == null) {
            return null;
        }
        
        Matcher matcher = VAR_PATTERN.matcher(input);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            VariableNode var = variables.get(varName);
            
            if (var == null) {
                throw new RuntimeException("Undefined variable: $" + varName);
            }
            
            String replacement = var.getValue().toString();
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        
        matcher.appendTail(result);
        return result.toString();
    }
    
    /**
     * Check if a string contains any variables
     */
    public boolean containsVariables(String input) {
        if (input == null) {
            return false;
        }
        return VAR_PATTERN.matcher(input).find();
    }
}