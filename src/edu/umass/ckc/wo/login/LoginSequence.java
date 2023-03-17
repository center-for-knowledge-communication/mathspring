package edu.umass.ckc.wo.login;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.config.LoginXML;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.interv.LoginIntervention;
import edu.umass.ckc.wo.login.interv.LoginInterventionSelector;
import edu.umass.ckc.wo.login.interv.Pretest;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelector;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutor.model.InterventionGroup;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.woserver.ServletInfo;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/13/15
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginSequence {
    public static final String INNERJSP = "innerjsp";
    public static final String SKIN = "skin";
    public static final String SERVLET_CONTEXT = "servletContext";
    public static final String SERVLET_NAME = "servletName";
    public static final String SESSION_ID = "sessionId";
    public static final String LCPROFILE = "lcprofile";
    public static final String URL = "url";
    
    private static Logger logger = Logger.getLogger(LoginSequence.class);
    private SessionManager smgr;
    private PedagogicalModel pedagogicalModel;
    private InterventionGroup interventionGroup;
    private ServletInfo servletInfo;
    private Connection conn;
    private int sessId;
    private int studId;


     // need to be able to use this class before there is a sessionId passed on the params

    public LoginSequence(ServletInfo servletInfo, int sessId) throws Exception {
        this.servletInfo = servletInfo;
        this.conn = servletInfo.getConn();
        ServletParams params = servletInfo.getParams();
        this.sessId = sessId;
        this.smgr = new SessionManager(servletInfo.getConn(),sessId,servletInfo.getHostPath(),servletInfo.getContextPath()).buildExistingSession();

        // Frank s. - added locale to sessionManager object to support multi-lingual capability
        Locale loc = (Locale) servletInfo.getRequest().getLocales().nextElement();
        this.smgr.setLocale(loc);
        System.out.println("Locale = " + this.smgr.getLocale());
        
        pedagogicalModel = smgr.getPedagogicalModel();
        Pedagogy ped = pedagogicalModel.getPedagogy();
        buildInterventions(ped);


    }

    public SessionManager getSmgr () {
        return this.smgr;
    }

    private void buildInterventions (Pedagogy ped) throws Exception {
        if (ped instanceof TutorStrategy) {
            List<InterventionSelectorSpec> isels = ((TutorStrategy) ped).getLogin_sc().getInterventionSelectors();
            interventionGroup = new InterventionGroup(isels);
        }
        else {
            LoginXML loginXML = ped.getLoginXML();
            interventionGroup = new InterventionGroup(loginXML.getInterventions());
        }

        interventionGroup.buildInterventions(smgr,pedagogicalModel);
        for (InterventionSelector s : interventionGroup.getAllInterventions()) {
            LoginInterventionSelector ls = (LoginInterventionSelector) s;
            ls.setServletInfo(servletInfo);
        }
    }

    // GIven a class like 'edu.umass.ckc.wo.login.interv.PreSurvey',  find the intervention selector object of that class.
    public InterventionSelector getInterventionSelectorFromClass (Class theClass) {
        for (InterventionSelector s: interventionGroup.getAllInterventions()) {
            if (s.getClass() == theClass)
                return s;
        }
        return null;
    }

    public LoginIntervention getNextIntervention (ServletParams params) throws Exception {
        LoginIntervention li = (LoginIntervention) interventionGroup.selectIntervention(smgr,new SessionEvent(params,this.sessId),"Login");
        return li;
    }


    public void processAction (ServletParams params, LoginIntervention li) throws Exception {

        if (li != null) {
            final boolean isUsingNewUI = Settings.useNewGUI();
            String innerJSP = li.getView();
            String skin = params.getString(SKIN);
            String loginJSP = isUsingNewUI
                ? "login/logink12Outer_new.jsp"
                : "login/logink12Outer.jsp";
            if (skin != null && skin.equalsIgnoreCase("adult"))
                loginJSP = isUsingNewUI
                    ? "login/loginAdultOuter_new.jsp"
                    : "login/loginAdultOuter.jsp";
            servletInfo.getRequest().setAttribute(INNERJSP,innerJSP);
            servletInfo.getRequest().setAttribute(SERVLET_CONTEXT,servletInfo.getServletContext().getContextPath());
            servletInfo.getRequest().setAttribute(SERVLET_NAME,servletInfo.getServletName());
            servletInfo.getRequest().setAttribute(SESSION_ID,smgr.getSessionNum());
            servletInfo.getRequest().setAttribute(LCPROFILE, li.getLCprofile());
            servletInfo.getRequest().setAttribute(URL, li.getURL());
            RequestDispatcher disp = servletInfo.getRequest().getRequestDispatcher(loginJSP);
            logger.debug("<< forward to JSP " + loginJSP);
            disp.forward(servletInfo.getRequest(),servletInfo.getResponse());
        }
        else {
            // At the end of the login sequence, remove any state in interventions that were specified as ONCE_PER_SESSION
            // so that the next login will run them again.
            clearInterventionState();
            logger.debug("<< Landing page");
            new LandingPage(servletInfo,smgr).handleRequest();
        }
    }

    public void clearInterventionState() throws SQLException {
        List<InterventionSelectorSpec> specs = interventionGroup.getInterventionsSpecs();
        for (InterventionSelectorSpec s: specs) {
        	String isName = s.getClassName();
        	if (isName == null)
        		isName = "";
            if (isName.equals("edu.umass.ckc.login.interv.StudentPedagogy")) {
            	System.out.println("isName = " + "edu.umass.ckc.login.interv.StudentPedagogy");
            }
            else {
	        	if (s.getRunFreq().equals(InterventionSelectorSpec.ONCE_PER_SESSION)) {
	                LoginInterventionSelector lis = (LoginInterventionSelector) s.getSelector();
	                lis.clearState();
	             }
            }
        }

    }
}
