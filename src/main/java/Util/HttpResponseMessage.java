package Util;

public enum  HttpResponseMessage{
    SC_OK("OK",200),
    SC_NOT_FOUND("File Not Found",404),
    SC_METHOD_NOT_IMPLEMENTED("http.method_not_implemented",501),
    SC_METHOD_GET_NOT_SUPPORTED_1_0("http.method_get_not_supported",401),
    SC_METHOD_GET_NOT_SUPPORTED_1_1("http.method_get_not_supported",405);

    /**
     * the message of the response
     */
    private String message;

    /**
     * the status of the response
     */
    private int status;

    HttpResponseMessage(String message, int status){
        this.message = message;
        this.status = status;
    }

    public String getMessage(int status) {
        for (HttpResponseMessage c : HttpResponseMessage.values()) {
            if (c.getStatus() == status) {
                return c.message;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }
}
