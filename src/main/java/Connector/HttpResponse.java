package Connector;

import java.io.OutputStream;

public class HttpResponse {
    private HttpRequest httpRequest = null;

    public HttpResponse(OutputStream outputStream) {
    }

    public void setRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
}
