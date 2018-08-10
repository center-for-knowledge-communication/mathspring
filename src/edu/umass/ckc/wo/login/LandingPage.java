package edu.umass.ckc.wo.login;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.handler.DashboardHandler;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import edu.umass.ckc.wo.woserver.ServletInfo;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/14/14
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LandingPage {

    // public static final String JSP = "tempSplash.jsp";
    public static final String JSP = "Dashboard.jsp";
    public static final String JSP_NEW = "Dashboard_new.jsp";
    private ServletInfo info;
    private SessionManager smgr;

    public LandingPage(ServletInfo info, SessionManager smgr) {
        this.info = info;
        this.smgr = smgr;
    }


    public boolean handleRequest() throws Exception {
//        setAttributesForJSP(Settings.html5ProblemURI + INITIAL_TUTOR_FRAME_CONTENT);

        ProblemMgr.loadProbs(smgr.getConnection());

        DashboardHandler h = new DashboardHandler(info.getServletContext(),smgr,smgr.getConnection(),info.getRequest(),info.getResponse());
        if (Settings.useNewGUI()) {
            h.showNewSplashPage(JSP_NEW, true);
        } else {
            h.showSplashPage(JSP, true);
        }
        return false;

    }
}
