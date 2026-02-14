package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the config block with base_url and default headers
 */
public class ConfigNode extends ASTNode {
    private String baseUrl;
    private List<HeaderNode> defaultHeaders;
    
    public ConfigNode() {
        this.baseUrl = null;
        this.defaultHeaders = new ArrayList<>();
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public List<HeaderNode> getDefaultHeaders() {
        return defaultHeaders;
    }
    
    public void addHeader(HeaderNode header) {
        this.defaultHeaders.add(header);
    }
    
    @Override
    public String toString() {
        return String.format("Config(baseUrl=%s, headers=%d)", baseUrl, defaultHeaders.size());
    }
}