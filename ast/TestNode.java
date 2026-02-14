package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a test block with requests and assertions
 */
public class TestNode extends ASTNode {
    private String name;
    private String description;
    private List<RequestNode> requests;
    private List<AssertionNode> assertions;
    
    public TestNode(String name) {
        this.name = name;
        this.requests = new ArrayList<>();
        this.assertions = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<RequestNode> getRequests() {
        return requests;
    }
    
    public void addRequest(RequestNode request) {
        this.requests.add(request);
    }
    
    public List<AssertionNode> getAssertions() {
        return assertions;
    }
    
    public void addAssertion(AssertionNode assertion) {
        this.assertions.add(assertion);
    }
    
    @Override
    public String toString() {
        return String.format("Test(%s, requests=%d, assertions=%d)", 
            name, requests.size(), assertions.size());
    }
}