package Connector.server;

import Util.HttpResponseMessage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet implements Servlet {

    private final static String methodGet = "GET";
    private final static String methodPost = "POST";

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }



    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;
        try {
            request = (HttpServletRequest)servletRequest;
            response = (HttpServletResponse)servletResponse;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
        this.service(request, response);
    }

    private void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if (method.equals(methodGet)){
            this.doGet(request,response);
        } else if (method.equals(methodPost)){
            this.doPost(request,response);
        } else {
            String errMsg = HttpResponseMessage.SC_METHOD_NOT_IMPLEMENTED.getMessage(501);
            response.sendError(501, errMsg);
        }

    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }

    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        String protocol = request.getProtocol();
        String msg;
        if (protocol.endsWith("1.1")) {
            msg = HttpResponseMessage.SC_METHOD_GET_NOT_SUPPORTED_1_1.getMessage(405);
            response.sendError(405, msg);
        } else {
            msg = HttpResponseMessage.SC_METHOD_GET_NOT_SUPPORTED_1_1.getMessage(401);
            response.sendError(400, msg);
        }
    }

    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }
}
