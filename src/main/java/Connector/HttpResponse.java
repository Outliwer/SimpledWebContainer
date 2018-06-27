package Connector;

import java.io.*;

public class HttpResponse {
    //the request
    private HttpRequest httpRequest;
    //set the BUFFER_SIZE equal 4096
    public static int BUFFER_SIZE = 4096;
    //output the result
    OutputStream output;

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
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1) {
                output.write(bytes, 0, ch);
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
            if (fis!=null)
                fis.close();
        }
    }
}
