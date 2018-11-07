package Util;

public class MessageConstruction {
    private static final String CRLF = new String("\r\n");

    private static final String BLANK = new String(" ");

    private static final String defaultVersion = new String("HTTP/1.1");

    private static final String COLON = new String(":");

    /**
     * the status of response
     */
    private static int status = HttpResponseMessage.SC_OK.getStatus();


    public static String getCRLF() {
        return CRLF;
    }

    public static String getBlank(){
        return BLANK;
    }

    public static String getDefaultVersion(){
        return defaultVersion;
    }

    public static String getColon(){
        return COLON;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public static int getStatus(){
        return status;
    }
}
