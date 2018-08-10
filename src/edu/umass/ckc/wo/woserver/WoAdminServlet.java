package edu.umass.ckc.wo.woserver;


import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.event.WoAdminEventFactory;
import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.mrcommon.Names;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

// servlet for dealing with user registration and reports
public class WoAdminServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(WoAdminServlet.class);


    public String getDataSource(ServletConfig servletConfig) {
        return servletConfig.getServletContext().getInitParameter("wodb.datasource");
    }

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
        logger.debug("Beginning setServletInfo of WoAdminServlet");
        ServletUtil.initialize(servletContext, connection);
        Settings.setGui(servletConfig.getInitParameter(Names.GUI));
        String prepostURI = servletConfig.getInitParameter("prepostProblemURI");
        String useServletSessions = servletConfig.getInitParameter("useServletSession");
        if (useServletSessions == null || useServletSessions.equals("true"))
            Settings.useAdminServletSession = true;
        else Settings.useAdminServletSession = false;
        String sessTimeoutMin = servletConfig.getInitParameter("sessionTimeoutMinutes");
        if (sessTimeoutMin != null) {
            int minutes = Integer.parseInt(sessTimeoutMin);
            Settings.adminServletSessionTimeoutSeconds = minutes * 60;
        }
        Settings.prePostProblemURI = ServletUtil.getURIForEnvironment(Settings.isDevelopmentEnv,Settings.host,Settings.port,
                servletContext.getContextPath(),Settings.webContentPath, prepostURI);
        Settings.getSurveys(connection); // loads the pre/post Survey URLS

        // DM 8/23/11 Added this because of mysterious dependency on ProbMgr.allProblems by this admin tool when
        // adjusting the problems in a topic.
        if (!ProblemMgr.isLoaded()) {
            ProblemMgr.loadProbs(connection);
        }
        logger.debug("End setServletInfo of WoAdminServlet");

    }

    public boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                 HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {
        try {
            logger.info(">>" + params.toString());
            ServletContext sc = this.getServletContext();
            ServletEvent e;
            WoAdminEventFactory f = new WoAdminEventFactory();
            e = f.buildEvent(params);

            boolean wroteToBuffer;
            wroteToBuffer = new AdminHandler().handleEvent(request, response, servletContext, conn, e, servletOutput);
            if (wroteToBuffer) {
                logger.info("<<");
            }
            return wroteToBuffer;
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use Options | File Templates.
            throw e1;
        }
    }
}
