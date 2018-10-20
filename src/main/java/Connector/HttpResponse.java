package Connector;

import Util.ContentTypeFind;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpResponse {

    /**
     * the request
     */
    private HttpRequest httpRequest;

    /**
     * set the BUFFER_SIZE equal 4096
     */
    public static int BUFFER_SIZE = 4096;

    /**
     * output the result
     */
    OutputStream output;

    /**
     * check the outputStream is Committed
     */
    private boolean committed = false;

    /**
     * the headers of the response
     */
    private HashMap headers = new HashMap();

    /**
     * the length of the response
     */
    private int contentLength;

    /**
     * the contentType of response
     */
    private String contentType;

    public HttpResponse(OutputStream outputStream) {
        this.output = outputStream;
    }

    public void setRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /*
     * send the static resource
     */
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(Constants.WEB_ROOT,httpRequest.getRequestURI());
        try {
            fis = new FileInputStream(file);
            int messageLength = 0;
            String fileName = file.getName();
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1) {
                messageLength += ch;
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
            setHeaders("content-length",String.valueOf(messageLength));
            setHeaders("content-type", ContentTypeFind.findTheType(fileName));
            // send the header
            sendHeaders();
            // get the content
            fis = new FileInputStream(file);
            ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1){
                output.write(bytes);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        } catch (FileNotFoundException e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            output.write(errorMessage.getBytes());
        } finally {
            if (fis!=null) {
                fis.close();
            }
        }
    }



    /**
     * the basic function to set the response header
     * @param name
     * @param value
     */
    public void setHeaders(String name, String value) {
        if (isCommitted()){
            return;
        }
        ArrayList values = new ArrayList();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }
        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (contentLength >= 0)
                setContentLength(contentLength);
        }
        else if (match.equals("content-type")) {
            setContentType(value);
        }
    }


    /**
     * the function to join the response
     */
    public void sendHeaders(){
        if (isCommitted()){
            return;
        }
        StringBuilder sb = new StringBuilder();

        committed = true;
    }

    /**
     * some function to get the value
     */

    private boolean isCommitted(){
        return committed;
    }

    private void setContentLength(int length){
        this.contentLength = length;
    }

    private void setContentType(String type){
        this.contentType = type;
    }

    private OutputStream getOutputStream(){
        return output;
    }
}
