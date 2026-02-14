package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an HTTP request (GET, POST, PUT, DELETE)
 */
public class RequestNode extends ASTNode {
    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }
    
    private HttpMethod method;
    private String path;
    private List<HeaderNode> headers;
    private String body;
    
    public RequestNode(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
        this.headers = new ArrayList<>();
        this.body = null;
    }
    
    public HttpMethod getMethod() {
        return method;
    }
    
    public String getPath() {
        return path;
    }
    
    public List<HeaderNode> getHeaders() {
        return headers;
    }
    
    public void addHeader(HeaderNode header) {
        this.headers.add(header);
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public boolean hasBody() {
        return body != null;
    }
    
    @Override
    public String toString() {
        return String.format("Request(%s %s, headers=%d, hasBody=%b)", 
            method, path, headers.size(), hasBody());
    }
}