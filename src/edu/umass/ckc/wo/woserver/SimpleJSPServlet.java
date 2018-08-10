package edu.umass.ckc.wo.woserver;

import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/12/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleJSPServlet extends BaseServlet {

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
        // sub classes should override this method
    }


    public boolean handleRequest(Connection conn, HttpServletRequest request,
                                 HttpServletResponse response, ServletParams params, StringBuffer output) throws Exception {

        String myVar = params.getString("myVar");
        HttpSession sess = request.getSession();
        sess.setAttribute("myVarCookie",myVar);
        request.setAttribute("person", "dave");
        request.getRequestDispatcher("test/test.jsp").forward(request,response);
        return false;

    }

}
