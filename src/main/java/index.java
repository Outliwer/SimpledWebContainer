
import Connector.server.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class index extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _userName = req.getParameter("username");
        String _passWord = req.getParameter("password");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter _printWriter = resp.getWriter();
        _printWriter.println("<html><body>");
        if (_userName == null || _passWord == null){
            _printWriter.println("please intput the _userName or _passWord");
        }else if ("admin".equals(_userName) && "admin".equals(_passWord)) {
            _printWriter.println("successful," + _userName);
        } else {
            _printWriter.println("fail！");
        }
        _printWriter.println("</body></html>");
    }
}
