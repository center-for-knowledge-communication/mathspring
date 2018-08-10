package edu.umass.ckc.wo.login;

import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCContentMgr;
import edu.umass.ckc.wo.content.LessonMgr;
import edu.umass.ckc.wo.login.interv.LoginIntervention;
import edu.umass.ckc.wo.login.interv.LoginInterventionSelector;
import edu.umass.ckc.wo.mrcommon.Names;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import edu.umass.ckc.wo.woserver.ServletInfo;
import edu.umass.ckc.wo.woserver.ServletUtil;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
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
 * Here is the login process:
 *
 * Student arrives at the login page which is a URL : http://localhost:8080/mt/WoLoginServlet?action=LoginK12_1
 *     They typically type in a username and password and click the LOGIN button
 * The username/pw is processed by the login.LoginK12_2 class.
 *     If the there is already a session for the user, we return the  login/loginExistingSessionK12.jsp   which asks them if they want to blow away the existing session and continue
 *     If there is no session for the user,  we create a new LoginSequence object
 *
 * Here is the login process:
 *
 * 1. Student arrives at the login page which is a URL : http://localhost:8080/mt/WoLoginServlet?action=LoginK12_1 .  This is handled in the second part of handleRequest (below)
 by sending it to the login.LoginK12_1 class's process method and results in a JSP of login/loginK12.jsp which prompts for user and password (note that the
 LoginResult produced this first time has a status of PRE_LOGIN and IS forwarded to a JSP)
 * They typically type in a username and password and click the LOGIN button
 * 2. The username/pw comes back to this with an action of LoginK12_2 and is handled by the second part of handleRequest which is then
 processed by the login.LoginK12_2 and Login2 class.
 *     a. If the there is already a session for the user (LoginResult is returned with status=ALREADY_LOGGED_IN and forwarded=TRUE) , we return the  login/loginExistingSessionK12.jsp
 which asks them if they want to blow away the existing session and sends another LoginK12_2 event with logoutExistingSessions=true flag so that login can proceed.
 *     b. If there is no session for the user (a LoginResult is returned with status=NEW_SESSION and forwarded=FALSE),  we create a new LoginSequence object and call its processAction method
 It selects a login intervention (e.g. the class GetNeighbors which comes with has a JSP view of "neighbors.js") and then forwards to
 logink12Outer.jsp with an inner DIV (innerJSP) set to the JSP of the intervention.

 3. When the intervention is submitted (a button within it's form) this results in a LoginInterventionInput event which carries the user inputs as parameters.
 These inputs are then sent back to that intervention (via its processInput method) and it is assumed to do something with those inputs and then return null which
 indicates no follow-up activities.
 a. If the intervention does not return a follow-up intervention
 a new LoginSequence is then built and we proceed as in step 2b. above by calling processAction
 b. If a follow-up activity (intervention) is returned by the original intervention's processInput, it then forwards to
 logink12Outer.jsp with an inner DIV (innerJSP) set to the JSP of the intervention (using LoginSequence.processAction)
 c.  In a rare case it may return a follow-up intervention marked "top-level".  In this situation we just forward to the intervention's JSP
 */

public class WoLoginServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(WoLoginServlet.class);

    public String getDataSource(ServletConfig servletConfig) {
         return servletConfig.getServletContext().getInitParameter("wodb.datasource");
     }

    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {
        LoginServletAction action = ActionFactory.buildAction(params);
        ServletInfo servletInfo = new ServletInfo(servletContext,conn,request,response,params,servletOutput,hostPath,contextPath,this.getServletName());

        logger.debug(">>" + params.toString());
        // after the user/pw has been accepted all the other actions are LoginEvent or LoginInterventionInput
//        if (action instanceof LoginEvent)   {
//            LoginSequence ls = new LoginSequence(servletInfo,params.getInt("sessionId"));
//            ls.processAction(params);
//            return false;
//        }
//        // When an intervention is complete, the form is submitted with an action=LoginInterventionInput and interventionClass=InterventionSelector
//        // so that we can send the form inputs to the intervention selector that generated the intervention.
//        else
        // All actions are either inputs to a Login intervention or the first login screen with user/pw
        if (action instanceof LoginInterventionInput) {
            String cl = params.getString("interventionClass");
            int sessId = params.getInt("sessionId");
            Class c = Class.forName(cl);
            LoginSequence ls = new LoginSequence(servletInfo, sessId);
            SessionManager smgr = ls.getSmgr();
            LoginInterventionSelector is = (LoginInterventionSelector) ls.getInterventionSelectorFromClass(c);
            // DM 10/16 Bug fix:  A class name is passed back.  We need to use the LoginSequence to build the login intervention selectors correctly
            // and then ask it for the selector with the class name.   We then ask it to process the inputs.
//            SessionManager smgr = new SessionManager(conn,sessId,servletInfo.getHostPath(),servletInfo.getContextPath()).buildExistingSession();
//            LoginInterventionSelector is = (LoginInterventionSelector) c.getConstructor(SessionManager.class).newInstance(smgr);
//            is.setServletInfo(servletInfo);
//            is.init(smgr,smgr.getPedagogicalModel());
            is.init(smgr,smgr.getPedagogicalModel());
            LoginIntervention interv = is.processInput(params);
            if (interv == null) {
                // Now find the next intervention
//                LoginSequence ls = new LoginSequence(servletInfo, params.getInt("sessionId"));
                LoginIntervention li = ls.getNextIntervention(params);
                ls.processAction(params,li);
                return false;
            }
            // The Pretest intervention is going to return follow-up interventions when its processAction is called.  So the below
            // needs to to be handled by LoginSequence.processAction which forwards to an outer JSP that has the intervention in an inner JSP.
            // The one oddball case is this ChildAssent intervention that essentially needs to exit the tutor if the child refuses to assent.   So LoginInterventions
            // have a property isTopLevel.  If true (in the case of the child refusing) we get back an intervention that we forward to its JSP.
            else if (interv.isTopLevel()) {
//                servletInfo.getRequest().setAttribute("innerjsp",innerJSP);
                // This is kind of hacked for now.   The only LoginIntervention that ever returns another intervention is the ChildAssent.  If the child does not assent, we need
                // to go back to the first login page.  We achieve this by returning LoginPage1Intervention which has a JSP in it that is the first login page.  So we just forward to it.
                servletInfo.getRequest().setAttribute("servletContext",servletInfo.getServletContext().getContextPath());
                servletInfo.getRequest().setAttribute("servletName",servletInfo.getServletName());
                servletInfo.getRequest().setAttribute("sessionId",smgr.getSessionNum());
                RequestDispatcher disp = servletInfo.getRequest().getRequestDispatcher(interv.getView());
                disp.forward(servletInfo.getRequest(),servletInfo.getResponse());
                logger.debug("<< forwarding to JSP " + interv.getView() );
                return false;
            }
            // This is a follow-up intervention being returned by the original intervention.  We need to handle it with LoginSequence.
            else {
//                LoginSequence ls = new LoginSequence(servletInfo, params.getInt("sessionId"));
                ls.processAction(params,interv);
                return false;
            }
        }
        else {  // processes the first login event which is the user id/pw (e.g. called LoginK12_1).  This generates a second event
            // (e.g. LoginK12_2) which is also processed here.  It validates the submitted user/pw and then, if no errors, returns
            // a LoginResult where newSession is true and its not forwarded to a JSP.   This causes it to enter the next phase which
            // is a LoginSequence comprised of a set of interventions that are selected and returned to the user.  When the user replies
            // to the intervention it is handled in the if statement above and the login sequence continues.
            LoginResult lr = action.process(servletInfo);

            // WHen the login fails it is processed by forwarding to a JSP, so just return false because a page is already generated
            // that may ask the user to try again or to proceed by killing existing sessions.
            if (lr.isForwardedToJSP()) {
                logger.debug("<< forwarded to login JSP");
                return false;
            }
            // When login succeeds, it is processed here
            else {
                LoginSequence ls = new LoginSequence(servletInfo,lr.getSessId());
                // state variables that prevent login interventions from running twice might
                // be leftover from a previous login sequence that failed recently.  This
                // will clean them out so that this sequence is fresh.
                if (lr.isNewSession())
                    ls.clearInterventionState();
                LoginIntervention li = ls.getNextIntervention(params);
                ls.processAction(params,li);
                return false;
            }

        }
    }

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
        logger.debug("Begin setServletInfo of WOLoginServlet");

        Settings.setGui(servletConfig.getInitParameter(Names.GUI));
        ServletUtil.initialize(servletContext, connection);
        Settings.formalityServletURI = servletConfig.getInitParameter(Names.FORMALITY_SERVLET_URI);
        servletContext.setAttribute("flashClientURI", Settings.flashClientPath);
        Settings.getSurveys(connection); // loads the pre/post Survey URLS
        // Loads all content into a cache for faster access during runtime
        if (!ProblemMgr.isLoaded())  {
            ProblemMgr.loadProbs(connection);
            CCContentMgr.getInstance().loadContent(connection);
            LessonMgr.getAllLessons(connection);  // only to check integrity of content so we see errors early

        }
        logger.debug("end setServletInfo of WOLoginServlet");

    }

    
}
