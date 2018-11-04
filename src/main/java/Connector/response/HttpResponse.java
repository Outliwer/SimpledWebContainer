package Connector.response;

import Connector.constants.Constants;
import Connector.request.HttpRequest;
import Util.ContentTypeFind;
import Util.HttpResponseMessage;
import Util.MessageConstruction;

import java.io.*;
import java.util.*;

public class HttpResponse {

    /**
     * the request
     */
    private HttpRequest httpRequest;

    /**
     * set the BUFFER_SIZE equal 4096
     */
    private static int BUFFER_SIZE = 4096;

    /**
     * output the result
     */
    OutputStream output;

    /**
     * the writer
     */
    PrintWriter writer;

    /**
     * check the outputStream is Committed
     */
    private boolean committed = false;

    /**
     * the headers of the response
     */
    private HashMap<String, List<String>> headers = new HashMap();

    /**
     * the length of the response
     */
    private int contentLength;

    /**
     * the contentType of response
     */
    private String contentType;

    /**
     * the status of response
     */
    private int status;


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
            this.status = HttpResponseMessage.SC_OK.getStatus();
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
        ArrayList<String> values = new ArrayList<String>();
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
    public void sendHeaders() throws IOException {
        if (isCommitted()){
            return;
        }
        StringBuilder sb = new StringBuilder();
        /**
         * 详细构成可以参见
         * https://github.com/Outliwer/SimpledWebContainer/issues/5
         */
        //TODO: how to find the status
        if (httpRequest != null){
            sb.append("HTTP/");
            sb.append(httpRequest.getHttpVersion());
        } else {
            sb.append(MessageConstruction.getDefaultVersion());
        }
        sb.append(MessageConstruction.getBlank());
        sb.append(status);
        sb.append(MessageConstruction.getBlank());
        sb.append(HttpResponseMessage.SC_OK.getMessage(status));
        sb.append(MessageConstruction.getCRLF());
        //header construction
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append(entry.getKey());
            sb.append(MessageConstruction.getColon());
            List<String> temp = entry.getValue();
            for (String tempValue : temp){
                sb.append(MessageConstruction.getBlank());
                sb.append(tempValue);
            }
            sb.append(MessageConstruction.getCRLF());
        }
        sb.append(MessageConstruction.getCRLF());
        output.write(String.valueOf(sb).getBytes());
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

    /**
     * get the printer of response
     * @return
     */
    public PrintWriter getWriter() {
        writer = new PrintWriter(output);
        return writer;
    }

}
