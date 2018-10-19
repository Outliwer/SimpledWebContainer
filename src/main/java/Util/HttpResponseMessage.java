package Util;

public enum  HttpResponseMessage{
    SC_OK("OK",200),SC_NOT_FOUND("File Not Found",404);

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
