package ast;

/**
 * Represents a variable declaration (let name = value;)
 */
public class VariableNode extends ASTNode {
    private String name;
    private Object value; // String or Integer
    
    public VariableNode(String name, Object value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public Object getValue() {
        return value;
    }
    
    public boolean isStringValue() {
        return value instanceof String;
    }
    
    public boolean isIntegerValue() {
        return value instanceof Integer;
    }
    
    @Override
    public String toString() {
        return String.format("Variable(%s = %s)", name, value);
    }
}