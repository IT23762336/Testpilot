package ast;

/**
 * Base class for all AST nodes
 */
public abstract class ASTNode {
    private int lineNumber;
    
    public ASTNode() {
        this.lineNumber = -1;
    }
    
    public ASTNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    @Override
    public abstract String toString();
}