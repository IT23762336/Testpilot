package ast;

/**
 * Represents a header declaration
 */
public class HeaderNode extends ASTNode {
    private String key;
    private String value;
    
    public HeaderNode(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.format("Header(%s: %s)", key, value);
    }
}