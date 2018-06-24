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
        return paramMap.get("requestURI");
    }

    private void nextToken(){
        if (currentToken < tokens.length){
            currentToken ++;
            current = tokens[currentToken];
            if (current == ' ' || current == '\t' || current == '\n')
                nextToken();
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
                paramMap.put("method","GET");
                // TODO: current message need
                System.out.println("the method : get");

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
     * change the inputStream to the CharArray
     */
    private char[] convertStreamToCharArray(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
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
