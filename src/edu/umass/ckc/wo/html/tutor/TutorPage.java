package edu.umass.ckc.wo.html.tutor;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.tutorhut.TutorHomeEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import edu.umass.ckc.wo.woserver.ServletInfo;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/17/13
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 * Frank	10-07-20  Issue #261 change problem header
 */
public class TutorPage {
    public static final String TUTOR_MAIN_JSP = "mathspring.jsp"; // this is the HTML page that is the tutor hut (plugged with global variables below)
    public static final String TUTOR_MAIN_JSP_NEW = "mathspring_new.jsp";
//    public static final String INITIAL_TUTOR_FRAME_CONTENT = "welcome.html"; // when it first comes up it has this welcome HTML content
    public static final String INITIAL_TUTOR_FRAME_CONTENT = "TutorBrain?action=SplashPage"; // show the MPP as the first iframe contents
    public static final String END_TUTOR_FRAME_CONTENT = "farewell.html"; // when it first comes up it has this welcome HTML content
    public static final String NO_MORE_CONTENT = "noMoreProbs.html"; // when it first comes up it has this welcome HTML content
    private static Logger logger = Logger.getLogger(TutorPage.class);
    SessionManager smgr;
    private  ServletInfo info;
    private String servContext;
    private StringBuilder logMsg;

    public TutorPage (ServletInfo info,  SessionManager smgr)  {
        this.info = info;
        this.smgr = smgr;
        this.logMsg = new StringBuilder();
        servContext= info.getRequest().getContextPath();
        if (servContext != null && servContext.length()>1)
            servContext=servContext.substring(1);    // strip off the leading /
    }

    public boolean handleRequest(TutorHomeEvent e) throws Exception {
//        setAttributesForJSP(Settings.html5ProblemURI + INITIAL_TUTOR_FRAME_CONTENT);
        setAttributesForJSP( INITIAL_TUTOR_FRAME_CONTENT+"&sessionId="+e.getSessionId()+"&elapsedTime=0");

        info.getRequest().setAttribute("isBeginningOfSession",true);

        RequestDispatcher disp=null;
        disp = info.getRequest().getRequestDispatcher(isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP);
        disp.forward(info.getRequest(),info.getResponse());
        return false;

    }

    /**
     *
     * @param activityURL The URL to load into the iframe of the tutoring page
     * @throws Exception
     */
    private void setAttributesForJSP (String activityURL) throws Exception {
        info.getRequest().setAttribute("sessionId",smgr.getSessionNum());


        // Path to the Flash client is given by web.xml param for WoTutorServlet
        String flashClientPath = Settings.flashClientPath + smgr.getClient() ;
        info.getRequest().setAttribute("instructions",null);
        info.getRequest().setAttribute("studId",smgr.getStudentId());
        appendLogMsg("studId",Integer.toString(smgr.getStudentId()));
        info.getRequest().setAttribute("userName", smgr.getUserName());
        info.getRequest().setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
        info.getRequest().setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
        info.getRequest().setAttribute("flashClientPath",flashClientPath);
        info.getRequest().setAttribute("formalityServlet",Settings.formalityServletURI);
        LearningCompanion lc = smgr.getLearningCompanion();
        String character = "", strategy = "";
        if (lc != null) {
            character = lc.getCharactersName();
            strategy = lc.getMessageSelectionStrategy();
        }
        info.getRequest().setAttribute("learningCompanion",character);
        info.getRequest().setAttribute("learningCompanionMessageSelectionStrategy",strategy);
        appendLogMsg("learningCompanion",character);
        // We must pass the wayangServletContext of this servlet to 4m so that it can build a URL to call wayang back
        info.getRequest().setAttribute("wayangServletContext",servContext);
        info.getRequest().setAttribute("gritServletContext","gritms");
//        info.getRequest().setAttribute("servletName",info.getServletName());
        info.getRequest().setAttribute("servletName","TutorBrain");
        info.getRequest().setAttribute("gritServletName","GritMouseServlet");
        Settings.problemContentPath = Settings.webContentPath.substring(0,Settings.webContentPath.length()-1);
        // if its a dev env, the html5Content & flash problem content must be under tomcat o/w security issues occur because of slightly diff URL hosts.   So we need to bind a var (problemContentPath) pointing to where this content is (either under tc or apache)
//        info.getRequest().setAttribute("problemContentPath", Settings.isDevelopmentEnv ?  Settings.devWebContentPath : Settings.problemContentPath);
        info.getRequest().setAttribute("problemContentPath", Settings.problemContentPath);
        info.getRequest().setAttribute("webContentPath",  Settings.webContentPath);
        info.getRequest().setAttribute("elapsedTime",0);
        info.getRequest().setAttribute("lastProbId",-1);
        info.getRequest().setAttribute("topicId",-1);
        info.getRequest().setAttribute("probId",-1);
        info.getRequest().setAttribute("probType","");
        info.getRequest().setAttribute("lastProbType","");
        info.getRequest().setAttribute("tutoringMode", "practice");
        info.getRequest().setAttribute("isForceProblem", false);
        info.getRequest().setAttribute("isForceTopic", false);
        info.getRequest().setAttribute("activityURL",activityURL);
        info.getRequest().setAttribute("probplayerPath", Settings.probplayerPath);
        info.getRequest().setAttribute("isDevEnv",Settings.isDevelopmentEnv);
        info.getRequest().setAttribute("probMode", "practice");
        info.getRequest().setAttribute("continueUnsolvedProblem", false);
        info.getRequest().setAttribute("resource", null);
        info.getRequest().setAttribute("answer", null);
        info.getRequest().setAttribute("activityJSON", "null");
        info.getRequest().setAttribute("showMPP", true);
        info.getRequest().setAttribute("resumeProblem",false);
        info.getRequest().setAttribute("eventCounter",smgr.getEventCounter());
        info.getRequest().setAttribute("soundSync",smgr.isSoundSync());
        info.getRequest().setAttribute("mouseSaveInterval",smgr.getMouseSaveInterval());
        info.getRequest().setAttribute("className",smgr.getClassName());
        info.getRequest().setAttribute("teacherName",smgr.getTeacherName());
        info.getRequest().setAttribute("timeInSession", smgr.getTimeInSession());

        if (DbUser.isTestUser(smgr.getConnection(),smgr.getStudentId()))
            info.getRequest().setAttribute("showAnswer", true);
        else
            info.getRequest().setAttribute("showAnswer", false);

        if (DbUser.isShowTestControls(smgr.getConnection(), smgr.getStudentId()))
            info.getRequest().setAttribute("showProblemSelector", true);
        else
            info.getRequest().setAttribute("showProblemSelector", false);

    }

    private void appendLogMsg(String var, String val) {
        logMsg.append(String.format("[%s,%s]",var,val));
    }

    private void appendLogMsg(String var, int val) {
        logMsg.append(String.format("[%s,%d]",var,val));
    }

    // This is called when the MPP Return to Tutor button is clicked.
    public void createTutorPageFromState(long elapsedTime, long probElapsedTime, int topicId,
                                         int probId, String chalRevOrPracticeMode, String demoOrPracticeMode, String lastProbType,
                                         boolean solved, String resource, String answer, boolean isBeginningOfSession, boolean showMPP) throws Exception {
        setAttributesForJSP("");
        info.getRequest().setAttribute("isBeginningOfSession", isBeginningOfSession);
        info.getRequest().setAttribute("elapsedTime",elapsedTime);
        info.getRequest().setAttribute("studId",smgr.getStudentId());
        info.getRequest().setAttribute("probElapsedTime", probElapsedTime);
        info.getRequest().setAttribute("topicId", topicId);
        appendLogMsg("topicId",topicId);
        info.getRequest().setAttribute("probId", probId);
        appendLogMsg("probId",probId);
        info.getRequest().setAttribute("isForceProblem", probId > 0);
        info.getRequest().setAttribute("isForceTopic", topicId > 0);
        info.getRequest().setAttribute("tutoringMode", chalRevOrPracticeMode);
        info.getRequest().setAttribute("showMPP", showMPP);

        // renamed to probMode (from mode) so it is clear that this is the mode of the problem being played (or requested to
        // be played) - For some reason the JSP wasn't even settings its globals.probMode to this value until now.
        info.getRequest().setAttribute("probMode", demoOrPracticeMode);
        info.getRequest().setAttribute("continueUnsolvedProblem", !solved);
        info.getRequest().setAttribute("resource", resource);
        info.getRequest().setAttribute("answer", answer);
        appendLogMsg("answer",answer);
        if (smgr.getLearningCompanion() != null)
            if (Settings.isDevelopmentEnv) {

//                info.getRequest().setAttribute("learningCompanionMovie",  Settings.devWebContentPath + "/LearningCompanion/" + smgr.getLearningCompanion().getCharactersName()+ "/idle.html");
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath + "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName() + "/idle.html");
            } else
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath + "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName()+ "/idle.html");

        else  info.getRequest().setAttribute("learningCompanionMovie","");

        info.getRequest().setAttribute("probType", lastProbType==null ? "" : lastProbType);
        info.getRequest().setAttribute("lastProbType", lastProbType==null ? "" : lastProbType);



        // this is gonna have to do a lot more or the JSP will need to change because the JSON for the problem is what needs
        // to be sent back so the page can have all that it needs about the problem
        RequestDispatcher disp=null;
        disp = info.getRequest().getRequestDispatcher(isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP);
        disp.forward(info.getRequest(),info.getResponse());
        logger.info("<< JSP: " + (isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP) + " " + logMsg.toString());
    }

    //    This is called when Assistments or MARi makes a call to our system.    It loads a page with a problem.
    // This is also called on MPPReturnToHutEvent, MPPContinueTopicEvent, MPPReviewTopicEvent, MPPChallengeTopicEvent, MPPTryProblemEvent
    public void createTutorPageFromState(long elapsedTime, long probElapsedTime, int topicId,
                                         ProblemResponse problemResponse, String chalRevOrPracticeMode, String lastProbType,
                                         boolean solved, String resource, String answer, boolean isBeginningOfSession, int lastProbId,
                                         boolean showMPP) throws Exception {
        setJavascriptVars(elapsedTime,probElapsedTime,topicId,problemResponse,chalRevOrPracticeMode,lastProbType,solved,resource,answer,isBeginningOfSession,lastProbId,showMPP);

        // this is gonna have to do a lot more or the JSP will need to change because the JSON for the problem is what needs
        // to be sent back so the page can have all that it needs about the problem
        RequestDispatcher disp=null;
        disp = info.getRequest().getRequestDispatcher(isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP);
        disp.forward(info.getRequest(),info.getResponse());
        logger.info("<< JSP: " + (isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP) + " " + logMsg.toString());
    }



    //    This is called when Assistments makes a call to our system.    It loads a page with a problem.
    // This is also called on MPPReturnToHutEvent, MPPContinueTopicEvent, MPPReviewTopicEvent, MPPChallengeTopicEvent, MPPTryProblemEvent
    public void createTutorPageForResumingPreviousProblem(long elapsedTime, long probElapsedTime, int topicId,
                                                          ProblemResponse problemResponse, String chalRevOrPracticeMode, String lastProbType,
                                                          boolean solved, String resource, String answer, boolean isBeginningOfSession, int lastProbId,
                                                          boolean showMPP) throws Exception {
        setJavascriptVars(elapsedTime,probElapsedTime,topicId,problemResponse,chalRevOrPracticeMode,lastProbType,solved,resource,answer,isBeginningOfSession,lastProbId,showMPP);
        info.getRequest().setAttribute("resumeProblem",true);  // tells tutorhut to not send EndProblem followed by BeginProblem events.  Instead will send ResumeProblem
        RequestDispatcher disp=null;
        disp = info.getRequest().getRequestDispatcher(isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP);
        disp.forward(info.getRequest(),info.getResponse());
        logger.info("<< JSP: " + (isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP) + " " + logMsg.toString());
    }



    // This builds a tutor page that is going to show an intervention
    public void createTutorPageFromState(long elapsedTime, long probElapsedTime, int topicId,
                                         InterventionResponse intervResponse, String chalRevOrPracticeMode, String lastProbType,
                                         boolean solved, String resource, String answer, boolean isBeginningOfSession, int lastProbId, boolean showMPP) throws Exception {

        setAttributesForJSP("");
        info.getRequest().setAttribute("isBeginningOfSession", isBeginningOfSession);
        info.getRequest().setAttribute("elapsedTime",elapsedTime);
        info.getRequest().setAttribute("studId",smgr.getStudentId());
        appendLogMsg("studId",smgr.getStudentId());
        info.getRequest().setAttribute("probElapsedTime", probElapsedTime);
        info.getRequest().setAttribute("topicId", topicId);
        appendLogMsg("topicId",topicId);
        info.getRequest().setAttribute("probId", lastProbId); // Going back to tutor needs to have an id set 
        appendLogMsg("probId",-1);
        info.getRequest().setAttribute("lastProbId",lastProbId);
        info.getRequest().setAttribute("isForceProblem", true);
        info.getRequest().setAttribute("isForceTopic", topicId > 0);
        info.getRequest().setAttribute("tutoringMode", chalRevOrPracticeMode);
        info.getRequest().setAttribute("showMPP", showMPP);

        // renamed to probMode (from mode) so it is clear that this is the mode of the problem being played (or requested to
        // be played) - For some reason the JSP wasn't even settings its globals.probMode to this value until now.
        info.getRequest().setAttribute("probMode", null);
        info.getRequest().setAttribute("continueUnsolvedProblem", !solved);
        info.getRequest().setAttribute("resource", resource);
        info.getRequest().setAttribute("answer", answer);
        info.getRequest().setAttribute("probType", "intervention"); // This is how we tell the client its getting an intervention in the activityJSON
        info.getRequest().setAttribute("activityJSON", intervResponse.getJSON().toString());
        appendLogMsg("activity",intervResponse.getJSON().toString());
        if (smgr.getLearningCompanion() != null)
            if (Settings.isDevelopmentEnv) {
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath +  "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName()+ "/idle.html");
            }
            else {
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath + "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName() + "/idle.html");
            }
        else  info.getRequest().setAttribute("learningCompanionMovie","");

        info.getRequest().setAttribute("lastProbType", lastProbType==null ? "" : lastProbType);
        // this is gonna have to do a lot more or the JSP will need to change because the JSON for the problem is what needs
        // to be sent back so the page can have all that it needs about the problem
        RequestDispatcher disp=null;
        disp = info.getRequest().getRequestDispatcher(isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP);
        disp.forward(info.getRequest(),info.getResponse());
        logger.info("<< JSP: " + (isUsingNewUI() ? TUTOR_MAIN_JSP_NEW : TUTOR_MAIN_JSP) + " " + logMsg.toString());
    }

    // This builds a tutor page for a Response by dispatching to the correct helper.
    public void createTutorPageForResponse(long elapsedTime, long probElapsedTime, int topicId,
                                           Response r, String chalRevOrPracticeMode, String lastProbType,
                                           boolean solved, String answer, boolean isBeginningOfSession, int lastProbId, boolean showMPP) throws Exception {
        if (r instanceof InterventionResponse)
        {
            InterventionResponse ir = (InterventionResponse)  r;
            Intervention i = ir.getIntervention();

            createTutorPageFromState(elapsedTime, probElapsedTime, topicId, ir, chalRevOrPracticeMode, lastProbType, solved, i.getResource(), answer, isBeginningOfSession, lastProbId, showMPP);
        }
        else if (r instanceof ProblemResponse) {
            ProblemResponse pr = (ProblemResponse) r;
            Problem p = pr.getProblem();
            createTutorPageFromState(elapsedTime,probElapsedTime,topicId,pr,chalRevOrPracticeMode,lastProbType,solved,p.getResource(),answer,isBeginningOfSession,lastProbId,showMPP);

        }

    }

    private void setJavascriptVars (long elapsedTime, long probElapsedTime, int topicId,
                                    ProblemResponse response, String chalRevOrPracticeMode, String lastProbType,
                                    boolean solved, String resource, String answer, boolean isBeginningOfSession, int lastProbId, boolean showMPP) throws Exception {

        Problem problem = response.getProblem();
        setAttributesForJSP("");
        info.getRequest().setAttribute("isBeginningOfSession", isBeginningOfSession);
        info.getRequest().setAttribute("elapsedTime",elapsedTime);
        info.getRequest().setAttribute("studId",smgr.getStudentId());
        appendLogMsg("studId",smgr.getStudentId());
        info.getRequest().setAttribute("probElapsedTime", probElapsedTime);
        info.getRequest().setAttribute("topicId", topicId);
        appendLogMsg("topicId",topicId);
        info.getRequest().setAttribute("probId", problem.getId());
        appendLogMsg("probId",problem.getId());
        info.getRequest().setAttribute("lastProbId",lastProbId);
        info.getRequest().setAttribute("tutoringMode", chalRevOrPracticeMode);
        info.getRequest().setAttribute("showMPP", showMPP);
        // renamed to probMode (from mode) so it is clear that this is the mode of the problem being played (or requested to
        // be played) - For some reason the JSP wasn't even settings its globals.probMode to this value until now.
        info.getRequest().setAttribute("probMode", problem.getMode());
        info.getRequest().setAttribute("form", problem.getForm());
        info.getRequest().setAttribute("resource", resource);
        info.getRequest().setAttribute("answer", answer);
        appendLogMsg("answer",answer);
        info.getRequest().setAttribute("probType", problem.getType());
        info.getRequest().setAttribute("activityJSON", response.getJSON().toString());
        appendLogMsg("activity",response.getJSON().toString());
        if (smgr.getLearningCompanion() != null)
            if (Settings.isDevelopmentEnv)
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath  + "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName()+ "/idle.html");
            else
                info.getRequest().setAttribute("learningCompanionMovie", Settings.webContentPath + "LearningCompanion/" + smgr.getLearningCompanion().getCharactersName()+ "/idle.html");

        else  info.getRequest().setAttribute("learningCompanionMovie","");
        info.getRequest().setAttribute("lastProbType", lastProbType==null ? "" : lastProbType);


    }

    private boolean isUsingNewUI() {
        return Settings.useNewGUI();
    }
}