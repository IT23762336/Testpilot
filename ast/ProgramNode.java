package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Root node representing the entire program
 */
public class ProgramNode extends ASTNode {
    private ConfigNode config;
    private Map<String, VariableNode> variables;
    private List<TestNode> tests;
    
    public ProgramNode() {
        this.config = null;
        this.variables = new HashMap<>();
        this.tests = new ArrayList<>();
    }
    
    public ConfigNode getConfig() {
        return config;
    }
    
    public void setConfig(ConfigNode config) {
        this.config = config;
    }
    
    public Map<String, VariableNode> getVariables() {
        return variables;
    }
    
    public void addVariable(VariableNode variable) {
        this.variables.put(variable.getName(), variable);
    }
    
    public VariableNode getVariable(String name) {
        return variables.get(name);
    }
    
    public List<TestNode> getTests() {
        return tests;
    }
    
    public void addTest(TestNode test) {
        this.tests.add(test);
    }
    
    @Override
    public String toString() {
        return String.format("Program(config=%s, vars=%d, tests=%d)", 
            config != null ? "present" : "absent", variables.size(), tests.size());
    }
}