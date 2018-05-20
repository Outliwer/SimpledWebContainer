package Connector;

import java.io.InputStream;

public class HttpRequest {

    private String requestURI = null;
    private InputStream inputStream = null;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getRequestURI() {
        return requestURI;
    }


}
