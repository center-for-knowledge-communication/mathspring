package edu.umass.ckc.wo.gaze;

import edu.umass.ckc.servlet.servbase.HeadServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;

import edu.umass.ckc.wo.login.ActionFactory;

import edu.umass.ckc.wo.mrcommon.Names;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.woserver.ServletInfo;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Dec 1, 2009
 * Time: 2:17:42 PM
 * Here is the gaze handler process:
 *
*/

public class WoGazeServlet extends HeadServlet {
    private static Logger logger = Logger.getLogger(WoGazeServlet.class);

    public String getDataSource(ServletConfig servletConfig) {
         return servletConfig.getServletContext().getInitParameter("partytime.datasource");
     }

    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {

//        GazeServletAction action = ActionFactory.buildAction(params);
        ServletInfo servletInfo = new ServletInfo(servletContext,conn,request,response,params,servletOutput,hostPath,contextPath,this.getServletName());

        logger.debug(">>" + params.toString());

//        if (action instanceof GazeEvent))   {
//        	System.out.println("Process Gaze Event");
//        }
        
        return false;
}

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
        System.out.println("Begin initialize() of WOGazeServlet");
        logger.debug("Begin initialize() of WOGazeServlet");
        Settings.setGui(servletConfig.getInitParameter(Names.GUI));
        logger.debug("end initialize() of WOLoginServlet");

    }
    
}
