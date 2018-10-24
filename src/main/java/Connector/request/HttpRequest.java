package Connector.request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    /**
     *   the param
     */
    private Map<String,String> paramMap = new HashMap<String, String>();

    /**
     *  the param in the URI
     */
    private Map<String,String> uriParamMap = new HashMap<String, String>();

    /**
     *  the tokens of the inputStream
     */
    InputStream inputStream;

    /**
     *  the offset of the tokens
     */
    int currentToken = -1;

    /**
     * the buffer
     */
    
    byte[] buffer;

    /**
     * Last valid byte.
     */
    private int count;
    
    /**
     * Position in the buffer.
     */
    private int pos;

    /**
     * get the current char
     */
    private char current;

    /**
     * Read byte
     * @return
     * @throws IOException
     */
    private void nextToken() throws IOException{
        if (pos >= count) {
            fill();
            if (pos >= count) {
                // TODO: add the Exception
                System.out.println("too many characters");
                current = (char)-1;
                return;
            }
        }
        current = (char)(buffer[pos++] & 0xff);
    }

    /**
     * Fill the internal buffer by the inputStram
     * @throws IOException
     */

    private void fill() throws IOException{
        pos = 0;
        count = 0;
        int nRead = inputStream.read(buffer, 0, buffer.length);
        if (nRead > 0) {
            count = nRead;
        }
    }

    /**
     * parse the request
     * @throws IOException
     */
    public void parseRequest() throws IOException{
        // start
        nextToken();
        if (current == 'G'){
            parseGetMethod();
        } else if (current == 'P'){

        } else {
            // TODO: error message need
            System.out.println("the input is wrong");
            return;
        }
    }


    /*
     * get the tokens
     */
    public HttpRequest(InputStream inputStream,int bufferSize) throws IOException {
        buffer = new byte[bufferSize];
        this.inputStream = inputStream;
    }

    public String getHttpVersion(){
        return paramMap.get("httpVersion");
    }

    /*
     * get the URI
     */
    public String getRequestURI() {
        return paramMap.get("requestURI");
    }

    /*
     * parse Get
     */
    private void parseGetMethod() throws IOException {
        nextToken();
        if (current == 'E'){
            nextToken();
            if (current == 'T'){
                paramMap.put("METHOD","GET");
                // TODO: current message need
                System.out.println("the method : get");
                parseURI();
            } else {
                // TODO: error message need
                System.out.println("the method is wrong");
            }
        } else {
            // TODO: error message need
            System.out.println("the method is wrong");
        }
    }


    /*
     * parse URI
     */
    private void parseURI() throws IOException {
        nextToken();
        while (current == ' ' || current == '\t'){
            nextToken();
        }
        if (current != '/'){
            // TODO: error message need
            System.out.println("there need a '/' , The URI is wrong");
        } else {
            StringBuilder sb = new StringBuilder();
            nextToken();
            while (current != '?' && current != ' '){
                sb.append(current);
                nextToken();
            }
            paramMap.put("requestURI", String.valueOf(sb));
            // TODO: current message need
            System.out.println("the requestURI :" + String.valueOf(sb));
            if (current == ' '){
                // There is no param in the request Param
                // TODO: current message need
                System.out.println("There is no param in the request Param");
                parseHttpVersion();
            } else {
                // TODO: current message need
                System.out.println("There is some params in the request Param");
                parseParamUriRequest();
                System.out.println("params have been recorded");
                parseHttpVersion();
            }
        }
    }

    /*
     * parse the param of the uriRequest
     */
    private void parseParamUriRequest() throws IOException {
        StringBuilder sb_key = new StringBuilder();
        StringBuilder sb_value = new StringBuilder();
        nextToken();
        while (current != '='){
            sb_key.append(current);
            nextToken();
        }
        nextToken();
        while (current != '&' && current != ' '){
            sb_value.append(current);
            nextToken();
        }
        uriParamMap.put(String.valueOf(sb_key),String.valueOf(sb_value));
        // TODO: current message need
        System.out.println(" params in the URI :  " +String.valueOf(sb_key)+ " : " +String.valueOf(sb_value));
        if (current == '&'){
            parseParamUriRequest();
        }
    }

    /*
     * parse the version of the Http
     */
    private void parseHttpVersion() throws IOException {
        nextToken();
        while (current == ' ' || current == '\t'){
            nextToken();
        }
        if (current == 'H'){
            StringBuilder sb_key = new StringBuilder();
            StringBuilder sb_value = new StringBuilder();
            while (current != '/'){
                sb_key.append(current);
                nextToken();
            }
            nextToken();
            while (current != '\n' && current != '\t' && current != ' ' && current != '\r'){
                sb_value.append(current);
                nextToken();
            }
            paramMap.put("httpVersion",String.valueOf(sb_value));
            // TODO: current message need
            System.out.println(" HTTP Version : " +String.valueOf(sb_key)+ " : " +String.valueOf(sb_value));
            nextToken();
        }
    }
}
