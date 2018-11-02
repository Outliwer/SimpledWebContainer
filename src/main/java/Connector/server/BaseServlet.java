package Connector.server;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseServlet implements Servlet {

    private final static String methodGet = "GET";
    private final static String methodPost = "POST";

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }



    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException {
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

    private void service(HttpServletRequest request,HttpServletResponse response){
        String method = request.getMethod();
        if (method.equals(methodGet)){

        } else if (method.equals(methodPost)){

        } else {

        }

    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
