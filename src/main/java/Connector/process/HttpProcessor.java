package Connector.process;
import Connector.HttpConnector;
import Connector.request.HttpRequest;
import Connector.response.HttpResponse;
import Container.core.ServletProcessor;
import Container.core.StaticResourceProcessor;

import java.io.*;
import java.net.Socket;

public class HttpProcessor {

    private  HttpConnector httpConnector = null;
    private HttpRequest httpRequest = null;
    private HttpResponse httpResponse = null;
    private static int BUFFER_SIZE = 4096;

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
            // print(inputStream);
            httpRequest = new HttpRequest(inputStream,BUFFER_SIZE);

            // create http response
            httpResponse = new HttpResponse(outputStream);

            //parse the request
            httpRequest.parseRequest();

            if (httpRequest.getRequestURI() == null){
                // TODO:add the exception
                throw new Exception("the RequestURI is Null");
            }

            // there need some functions to finish the httpResponse
            httpResponse.setRequest(httpRequest);
            httpResponse.setHeaders("Server", "Servlet Container");

            //check whether the request is a static source
            if (httpRequest.getRequestURI().startsWith("servlet/")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(httpRequest, httpResponse);
            } else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(httpRequest, httpResponse);
            }

            //close the socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void print (InputStream is) throws UnsupportedEncodingException{
        InputStreamReader isr =new InputStreamReader(is,"utf-8");
        BufferedReader br = new BufferedReader(isr);
        try {
            int index = 0;
            while (index < 100) {
                System.out.println(br.readLine());
                index ++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
