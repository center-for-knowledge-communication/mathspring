package edu.umass.ckc.wo.woserver;

import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/14/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDbServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(TestDbServlet.class);

    public String testConnection(Connection conn) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select count(*) from hint";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c = rs.getInt(1);
                String s = "Database working.   There are " + c + " hints in the hint table";
                System.out.println(s);
                return s;
            }
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return "Database failure";
    }

    public String getDataSource (ServletConfig servletConfig) {
        return servletConfig.getServletContext().getInitParameter("wodb.datasource");
    }

    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {
        try {
            response.setContentType("text/html");    // set the response type before sending data
            PrintWriter out = response.getWriter();    // for outputting text
            logger.info(">>" + params.toString());
            String msg = testConnection(conn);
            out.print("<HTML>");
            out.print("<HEAD><TITLE>4mality Test DB Connection Servlet</TITLE></HEAD>");
            out.print("<BODY>");
            out.print("<br>Database is " + tomcatDataSourceURL + "<br>");
            out.print(msg);

            out.print("</BODY>");
            out.print("</HTML>");
            out.close();
            logger.info("<<" + servletOutput.toString());
            return true;
        } catch (Exception e) {
            logger.info("", e);
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            servletOutput.append("ack=false&message=" + e.getMessage());
            return true;
        }
    }
}
