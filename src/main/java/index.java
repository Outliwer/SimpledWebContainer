import javax.servlet.*;
import java.io.IOException;

public class index implements Servlet {
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("测试是否成功");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
