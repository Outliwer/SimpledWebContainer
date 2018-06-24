package Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    //the param
    private Map<String,String> paramMap = new HashMap<String, String>();
    //the param in the URI
    private Map<String,String> uriParamMap = new HashMap<String, String>();
    //the tokens of the inputStream
    char[] tokens;
    //the offset of the tokens
    int currentToken = -1;
    //the value of the token
    char current;

    /*
     * get the tokens
     */
    public HttpRequest(InputStream inputStream) {
        tokens = convertStreamToCharArray(inputStream);
    }

    /*
     * get the URI
     */
    public String getRequestURI() {
        System.out.println("paramMap.size() " + paramMap.size());
        return paramMap.get("requestURI");
    }

    /*
     * the next token
     */
    private void nextToken(){
        if (currentToken < tokens.length){
            currentToken ++;
            current = tokens[currentToken];
        } else {
            // TODO: error message need
            System.out.println("the end of the tokens");
        }
    }
    /*
     * parse
     */
    public void parseRequest(){
        if (tokens == null || tokens.length == 0){
            // TODO: error message need
            System.out.println("the input is null");
            return;
        }
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
     * parse Get
     */
    private void parseGetMethod(){
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
    private void parseURI(){
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
    private void parseParamUriRequest(){
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
    private void parseHttpVersion(){
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
            while (current != '\n' && current != '\t' && current != ' '){
                sb_value.append(current);
                nextToken();
            }
            paramMap.put(String.valueOf(sb_key),String.valueOf(sb_value));
            // TODO: current message need
            System.out.println(" HTTP Version : " +String.valueOf(sb_key)+ " : " +String.valueOf(sb_value));
            nextToken();
            // TODO: 下一次分析开始  H
//            System.out.println(current);
        }
    }

    /*
     * change the inputStream to the CharArray
     */
    private char[] convertStreamToCharArray(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + ' ');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().toCharArray();
    }

}
