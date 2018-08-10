package edu.umass.ckc.wo.woserver;

import ckc.servlet.servbase.BaseServlet;
import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.mrcommon.Names;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/17/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathSpringTutorServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(MathSpringTutorServlet.class);

    public String getDataSource(ServletConfig servletConfig) {
        return servletConfig.getServletContext().getInitParameter("wodb.datasource");
    }



    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {

    }


    /**
     * Overrides the handleRequest method in BaseServlet.  This handles all requests coming into this servlet.
     *
     * @param servletContext
     * @param conn
     * @param request
     * @param response
     * @param params
     * @param servletOutput
     * @return whether to flush output to the servlet output stream
     */
    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws IOException {
        setHostAndContextPath(this.getServletName(),servletContext,request);
        try {
            MathSpringTutorHandler h = new MathSpringTutorHandler(new ServletInfo(servletContext,conn,request,response,params,servletOutput,this.hostPath,this.contextPath,this.getServletName()));
            h.handleRequest();
        } catch (Throwable throwable) {
            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throwable.printStackTrace(response.getWriter());

        }
        return true;
     }
}
