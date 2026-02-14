package ast;

/**
 * Represents an assertion statement
 * Types: STATUS, STATUS_RANGE, HEADER_EQUALS, HEADER_CONTAINS, BODY_CONTAINS
 */
public class AssertionNode extends ASTNode {
    public enum AssertionType {
        STATUS,           // expect status = 200
        STATUS_RANGE,     // expect status in 200..299
        HEADER_EQUALS,    // expect header "K" = "V"
        HEADER_CONTAINS,  // expect header "K" contains "V"
        BODY_CONTAINS     // expect body contains "text"
    }
    
    private AssertionType type;
    private String headerKey;  // For header assertions
    private String expectedValue; // String or Integer as string
    private int rangeMin;      // For status range assertions
    private int rangeMax;      // For status range assertions
    
    // For status assertions
    public AssertionNode(AssertionType type, int statusCode) {
        this.type = type;
        this.expectedValue = String.valueOf(statusCode);
    }
    
    // For status range assertions
    public AssertionNode(AssertionType type, int rangeMin, int rangeMax) {
        this.type = type;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.expectedValue = rangeMin + ".." + rangeMax;
    }
    
    // For header assertions
    public AssertionNode(AssertionType type, String headerKey, String expectedValue) {
        this.type = type;
        this.headerKey = headerKey;
        this.expectedValue = expectedValue;
    }
    
    // For body contains
    public AssertionNode(AssertionType type, String expectedValue) {
        this.type = type;
        this.expectedValue = expectedValue;
    }
    
    public AssertionType getType() {
        return type;
    }
    
    public String getHeaderKey() {
        return headerKey;
    }
    
    public String getExpectedValue() {
        return expectedValue;
    }
    
    public int getExpectedStatusCode() {
        return Integer.parseInt(expectedValue);
    }
    
    public int getRangeMin() {
        return rangeMin;
    }
    
    public int getRangeMax() {
        return rangeMax;
    }
    
    @Override
    public String toString() {
        switch (type) {
            case STATUS:
                return String.format("AssertStatus(%s)", expectedValue);
            case STATUS_RANGE:
                return String.format("AssertStatusRange(%d..%d)", rangeMin, rangeMax);
            case HEADER_EQUALS:
                return String.format("AssertHeaderEquals(%s = %s)", headerKey, expectedValue);
            case HEADER_CONTAINS:
                return String.format("AssertHeaderContains(%s contains %s)", headerKey, expectedValue);
            case BODY_CONTAINS:
                return String.format("AssertBodyContains(%s)", expectedValue);
            default:
                return "Assertion(unknown)";
        }
    }
}