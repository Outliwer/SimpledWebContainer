package Connector.Process;
import Connector.HttpConnector;
import Connector.HttpRequest;
import Connector.HttpResponse;
import Container.ServletProcessor;
import Container.StaticResourceProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private  HttpConnector httpConnector = null;
    private HttpRequest httpRequest = null;
    private HttpResponse httpResponse = null;

    public HttpProcessor(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            // create httprequest
            httpRequest = new HttpRequest(inputStream);

            // create httpresponse
            httpResponse = new HttpResponse(outputStream);
            // there need some functions to finish the httpResponse
            httpResponse.setRequest(httpRequest);

            //check whether the request is a static source
            if (httpRequest.getRequestURI().startsWith("/servlet/")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(httpRequest, httpResponse);
            } else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(httpRequest, httpResponse);
            }

            //close the sotket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
