package edu.umass.ckc.wo.assistments;

import ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.wo.admin.PedagogyRetriever;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.*;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.exc.AdminException;
import edu.umass.ckc.wo.exc.AssistmentsBadInputException;
import edu.umass.ckc.wo.handler.NavigationHandler;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.model.TutorModel;
import edu.umass.ckc.wo.tutor.pedModel.CCPedagogicalModel;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.event.internal.BeginningOfTopicEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;
import edu.umass.ckc.wo.util.HTTPRequest;
import edu.umass.ckc.wo.woserver.ServletInfo;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/13/13
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class AssistmentsHandler {

    private static Logger logger = Logger.getLogger(AssistmentsHandler.class);


    public static final String ASSISTMENTS_LINK_USER_URL = "assistmentsLinkUserURL";
    private static final String ACCESS_TOKEN_JSON_FIELD = "token";
    private static final String ERR_MSG_JSON_FIELD = "err";


    private String assUserTokenURL = "https://test1.assistments.org/api2_helper/link_user";

    //    private SessionManager smgr;
    private ServletInfo servletInfo;
    private Connection conn;

    public static final String assistmentServletName = "partnerProblemLog";
    public static String assistmentsLogbackURL = "https://test1.assistments.org/api2/partnerProblemLog"; // this will be overwritten by value from web.xml
//    private static String assistmentsLogbackURL = "http://rose.cs.umass.edu/mt/TutorBrain?action=EnterTutor";

    public AssistmentsHandler(ServletInfo servletInfo) {
        String assLinkUserURL = servletInfo.getServletContext().getInitParameter(ASSISTMENTS_LINK_USER_URL);

        if (assLinkUserURL != null && !assLinkUserURL.equals(""))
            this.assUserTokenURL = assLinkUserURL;
        this.servletInfo = servletInfo;
        this.conn = servletInfo.getConn();
//        getReferer(servletInfo); // doesn't work with the calls coming from ASsistments
        System.out.println(assistmentsLogbackURL);
    }

    private void getReferer(ServletInfo servletInfo) {
        String referer = servletInfo.getRequest().getHeader(HttpHeaders.REFERER);
        if (referer == null)
            ;
        else {
            int i = referer.indexOf(assistmentServletName);
            if (i != -1)
                assistmentsLogbackURL = referer.substring(0, i + assistmentServletName.length());
            else assistmentsLogbackURL = null;
        }
    }

    private String genUser() throws SQLException {
        SecureRandom r = new SecureRandom();
        String s = new BigInteger(130, r).toString(20);
        while (DbCoopUsers.getUser(conn, s) != null)
            s = new BigInteger(130, r).toString(20);
        return s;
    }

    public boolean teachTopic(TeachTopicEvent e) throws AssistmentsBadInputException {
        try {
            // Params that indicate its really from Assistments are present (must include user)
            if (e.isAssistmentsUser())
                return teachTopicAssistmentsUser(e);

                // An external call (no assistments params) so its going to be temp user for this call only
            else
                return teachTopicExternalTempUser(e);

        } catch (Exception exc) {
            throw new AssistmentsBadInputException(exc.getMessage());
        }
    }


    public boolean teachTopicExternalTempUser(TeachTopicEvent e) throws Exception, AssistmentsBadInputException {
        // This call is for testing the API only.   So we create a temp user that is allowed to test.
        UserRegistrationHandler.genName(conn, "externalCaller");
        // absence of isTest has meaning isTest=false (a switch from Tom's original spec which was confusing).
        boolean isTest = e.isTestUser();

        int studId = -1;
        if (isTest)
            studId=UserRegistrationHandler.registerTemporaryUser(conn, edu.umass.ckc.wo.db.DbClass.ASSISTMENTS_CLASS_NAME, User.UserType.externalTempTest);
        else
            studId=UserRegistrationHandler.registerTemporaryUser(conn, edu.umass.ckc.wo.db.DbClass.ASSISTMENTS_CLASS_NAME, User.UserType.externalTempNonTest);

        if (e.isShowTransitionPage()) {
            return showIntroPage(e);
        } else return processTeachTopicRequest(e, studId, null);
    }

    /**
     * ONR Phase 1 processing of TeachTopic.
     *
     * @param e
     * @return
     * @throws Exception Test URL:TutorBrain?action=TeachTopic&forceFirstProblem=true&problemNumber=395&user=user333&topic=1&showIntro=true&maxtime=600000&maxprobs=3&mode=ExamplePractice
     *                   http://localhost:8080/mt/
     *                   http://localhost:8080/mt/TutorBrain?action=TeachTopic&forceFirstProblem=true&problemNumber=395&topic=1&mode=ExamplePractice
     *                   http://localhost:8080/mt/TutorBrain?action=TeachTopic&problemNumber=395&mode=Practice
     */
    public boolean teachTopicAssistmentsUser(TeachTopicEvent e) throws Exception, AssistmentsBadInputException {
        int studId = -1;
        String user = e.getUser();
        // Find if this this assistments user has been in the system previously
        CoopUser u = DbCoopUsers.getUser(conn, user);
        // This is the first we've ever heard of this user.
        if (u == null) {
            String token = callBackForToken(e);  // I think WPI changed the spec and doesn't require that we get a token for the user.
            // creates a wayang user and assigns a pedagogy to him
            boolean isTestUser = e.isTestUser();
            // legit user params are given from Assistments, so we create a user in our system that has persisting user/data
            if (isTestUser) {
                User.UserType ut = User.UserType.coopStudentTest;
                studId = UserRegistrationHandler.registerExternalUser(servletInfo.getConn(), DbClass.ASSISTMENTS_CLASS_NAME, e.getUser(), ut, false);
                u = DbCoopUsers.insertUserInDb(conn, user, token, studId);
            }
            // a call made Assistments that is not a test.
            else {
                User.UserType ut = User.UserType.coopStudent;
                studId = UserRegistrationHandler.registerExternalUser(servletInfo.getConn(), DbClass.ASSISTMENTS_CLASS_NAME, e.getUser(), ut, false);
                u = DbCoopUsers.insertUserInDb(conn, user, token, studId);
            }
            if (e.isShowTransitionPage())
                return showIntroPage(e);
        } else {
            // We may have heard of this user but we still want this session to run correctly based on the isTest param (e.g. a previous
            // session for this user may have had isTest=false and this one may have isTest=true)

            boolean isTestUser = e.isTestUser();
            User.UserType ut;
            if (isTestUser)
                ut = User.UserType.coopStudentTest;
            else ut =  User.UserType.coopStudent;
            UserRegistrationHandler.setUserTestProperty(servletInfo.getConn(),u.getStudId(),ut);
            studId = u.getStudId();
        }
        return processTeachTopicRequest(e, studId, u);

    }

    private boolean showIntroPage(TeachTopicEvent e) throws ServletException, IOException {
        // Build the same URL as what was given except strip off the showTransitionPage=true so that when the user clicks
        // link it will reenter here and not show the video.
        String url = servletInfo.getServletName() + "?" + servletInfo.getRequest().getQueryString().replace("&showTransitionPage=true", "");

        servletInfo.getRequest().setAttribute("teachTopicURL", url);
        servletInfo.getRequest().getRequestDispatcher("portal.jsp").forward(servletInfo.getRequest(), servletInfo.getResponse());
        return BaseServlet.FORWARDED_TO_JSP;
    }


    private boolean processTeachTopicRequest(TeachTopicEvent e, int studId, CoopUser u) throws AssistmentsBadInputException, Exception {
        // TODO Currently the tutor remembers a student who returns through this entry point.   This includes his
        // full student state.  So this affects the tutoring.   If a student saw a topic and saw 3 problems and then
        // this call sends in a maxProblems of 3, it will return noMoreProblems=true.   So we need to figure out
        // what the behavior should be for a returning student.

        int clId = DbUser.getStudentClass(conn, studId);
        List<Pedagogy> peds = DbClassPedagogies.getClassPedagogies(conn, clId);
        // check to make sure this class has one pedagogy because Assistments is going to configure it so it needs to be
        // a configurable Pedagogy
        if (peds.size() > 1) {
            logger.error("Assistments user is in a class with more than one pedagogy.  Only one pedagogy should be assigned to a class");
        }
        Pedagogy pedagogy = peds.get(0);
        if (e.getLessonId() != -1)
            pedagogy = DbClassPedagogies.getAssistmentsCommonCorePedagogy();
        // now configure the pedagogy using the inputs coming from the URL
        String mode = e.getMode();
        String firstProbMode = mode.equals("ExamplePractice") || mode.equals("Example") ? Problem.DEMO : Problem.PRACTICE;
        String problemNumber = e.getProblemNumber();
        String problemId = e.getProblemId();
        Problem prob = null;
        if (problemNumber != null) {
            prob = ProblemMgr.getProblemByName("problem_" + problemNumber);
            if (prob == null)
                throw new AssistmentsBadInputException("Invalid problemNumber.  Cannot find problem_" + problemNumber);
        } else if (problemId != null) {
            prob = ProblemMgr.getProblem(Integer.parseInt(problemId));
            if (prob == null)
                throw new AssistmentsBadInputException("Invalid problemId.  Cannot find problem for " + problemId);
        }
        int probId = -1;
        if (prob != null)
            probId = prob.getId();

        boolean showIntro = e.isShowIntro();
        int cuId = e.getCuId();
        int lessonId = e.getLessonId();
        int topicId = e.getTopic();
        String[] ccss = {e.getCcss()};
        if (topicId != -1) {
            Topic t = ProblemMgr.getTopic(topicId);
            if (t == null && ccss == null)
                throw new AssistmentsBadInputException("Invalid topic.   Cannot find topic " + topicId);
        }
        int maxtime = e.getMaxtime();
        int maxprobs = e.getMaxprobs();
        float mastery = e.getMasteryLevel();
        if (maxtime <= 0)
            throw new AssistmentsBadInputException("maxTime must be greater than 0");
        if (maxprobs <= 0)
            throw new AssistmentsBadInputException("maxProblems must be greater than 0");
        DbUserPedagogyParams.clearUserPedagogyParams(conn, studId);  // get rid of parameters from previous calls from Ass
        DbUserPedagogyParams.saveUserPedagogyParams(conn, studId, mode, showIntro, maxtime, maxprobs, true, ccss, topicId, mastery, cuId, lessonId, Integer.parseInt(pedagogy.getId())); // save the params for this mini-session
        SessionManager smgr = new SessionManager(conn).assistmentsLoginSession(studId);
        // if a lessonId was given then we need to set the Lesson of the CCPedagogicalModel
        setLesson(clId, lessonId, smgr);
        smgr.getPedagogicalModel().newSession(smgr.getSessionNum());
        smgr.getStudentState().newSession(smgr);
//        smgr.initializeTopicTeaching(topicId);
        // if wants showIntro and problemNumber, push problemNumber on a stack in db and proceed with processing
        // based only on showIntro.   The problem selector will pull the problem off the stack after topic intro plays
        if (showIntro && prob != null) {
            DbSessionRequestedProblems.setRequestedProblems(conn, smgr.getSessionNum(), prob.getId());
            // Couldn't get this working.   It was an attempt to handle showIntro=true and problemNumber=444

//            prob =  null;
//            probId = -1;
        }
        // saves the assistments calling parameters in assistmentSessionData so that this session has its unique set of params that will be
        // used at callback time to send logging data back to assistments
        if (u != null)
            DbCoopUsers.saveSessionInfo(conn, smgr.getSessionNum(), u.getUid(), e.getAssignment(),
                    e.getAssistment(), e.getProblem(), e.getAssistmentsClass(), this.assistmentsLogbackURL);
        new NavigationHandler(servletInfo.getServletContext(), smgr, conn, servletInfo.getRequest(), servletInfo.getResponse()).handleRequest(new NavigationEvent(smgr.getSessionNum(), "assistments", NavigationEvent.SAT_HUT, Long.toString(System.currentTimeMillis()), ""));
        // N.B. Passing solved=true so that the page generated does not act as though it is trying to resume an unsolved problem after return from MPP
//        new TutorPage(servletInfo,smgr).createTutorPageFromState(0,0,topicId,probId, Problem.PRACTICE, firstProbMode, null, true, null, null, false);


        // Generate the tutor page so it shows what we want rather than making a next-prob callback to the server.
        PedagogicalModel pedMod = smgr.getPedagogicalModel();
        NextProblemEvent npe;
        ProblemResponse r = null;
//        npe.setUserInput("");
        if (probId > 0) {
            // DM 2014/09/26  I added this.   If the mode is Example or ExamplePractice and a probID is passed,
            // it seems that we should put that mode in the NextProblemEvent so that the selector will show it in that mode.
            npe = new NextProblemEvent(0, 0, Integer.toString(probId), firstProbMode);
            npe.setTopicToForce(topicId);
            smgr.getStudentState().setCurTopic(topicId);
            r = (ProblemResponse) pedMod.processStudentSelectsProblemRequest(npe);
        } else if (topicId > 0) {
            npe = new NextProblemEvent(0, 0, topicId);
            // TODO We shouldn't be assuming a ProblemEvent comes back.  What if an Intervention were selected to begin a topic?
            InternalEvent beginningOfTopicEvent = new BeginningOfTopicEvent(npe,topicId);
            TutorModel tutMod = pedMod.getTutorModel();
            tutMod.processInternalEvent(beginningOfTopicEvent);

            prob = r.getProblem();
        } else if (lessonId > 0) {
            npe = new NextProblemEvent(0, 0);
            r = (ProblemResponse) pedMod.processNextProblemRequest(npe);
            prob = r.getProblem();
        }
//        else if (ccss != null) {
//            r = pedMod.getProblemInStandardSelectedByStudent()
//        }

        smgr.getStudentModel().newProblem(smgr.getStudentState(), prob);  // this does not set curProb = new prob id,
        smgr.getStudentState().setCurProblem(probId);
        smgr.getStudentModel().save();
        boolean showMPP = smgr.getPedagogicalModel().isShowMPP();
        new TutorPage(servletInfo, smgr).createTutorPageFromState(0, 0, topicId, r, "practice", null, true, prob.getResource(), null, false, -1, showMPP);
//        new TutorLogger(smgr).logMPPEvent(e,lastProbId);

        return BaseServlet.FORWARDED_TO_JSP;
    }


    /*
        If the call has a lessonId,  then we fetch the Lesson and add it to the CCPedagogicalModel
     */
    private void setLesson(int clId, int lessonId, SessionManager smgr) throws Exception {
        if (lessonId != -1) {
            CCPedagogicalModel pm = (CCPedagogicalModel) smgr.getPedagogicalModel();
            pm.getStudentLessonMgr().init(smgr, lessonId);
        }
    }

    /*
    This is a temporary workaround.   The Assistments API may pass us a cuID.   This means that we are going to ignore the other params in the call.
    We have to use a different PedagogicalModel (than the one assigned to the AssistmentsUsers class) to handle the request.   We switch to this pedagogical model
    here.   We give it all the same inputs that we would have given to the pedagogical model assigned to the class.
     */
    private void switchToCCPedagogicalModel(SessionManager smgr) throws SQLException, AdminException {
        int studId = smgr.getStudentId();
        Pedagogy ped = PedagogyRetriever.getPedagogy(conn, studId);
        // these are the parameters as dfined in the XML file pedagogies.xml
        PedagogicalModelParameters defaultParams = ped.getParams();
        int pedagogyId = Integer.parseInt(ped.getId());

        smgr.setPedagogyId(pedagogyId);
        // pedagogical model needs to be instantiated as last thing because its constructor takes the smgr instance (this)
        // and makes calls to get stuff so we want this as fully constructed as possible before the call to instantiate
        // so that the smgr is fully functional except for its ped model.
        ped.setPedagogicalModelClass(CCPedagogicalModel.class.getName());
        // build the Pedagogical model for the student.  The PedagogicalModel constructor is responsible for
        // creating the StudentModel which also gets set in the below method
        smgr.instantiatePedagogicalModel(ped);

        // If this is a configurable pedagogy (meaning that it can be given some parameters to guide its behavior),  then
        // see if this user has a set of parameters and if so use them to configure the pedagogy.
        // these params come from settings in the WoAdmin tool for the class.
        PedagogicalModelParameters classParams = DbClass.getPedagogicalModelParameters(conn, DbClass.getClassByName(conn, DbClass.ASSISTMENTS_CLASS_NAME).getClassid());
        // overload the defaults with stuff defined for the class.
        defaultParams.overload(classParams);
//       if (this.pedagogicalModel instanceof ConfigurablePedagogy) {
        // these params are the ones that were passed in by Assistments and saved for the user
        PedagogyParams userParams = DbUserPedagogyParams.getPedagogyParams(conn, studId);
        // overload the params with anything provided for the user.
        defaultParams.overload(userParams);
        // set theparams on the ped model
        smgr.getPedagogicalModel().setParams(defaultParams);
    }

    private void processRequest(CoopUser u, TeachTopicEvent e) {
        // Go ahead and generate the Tutor Page using the requirements passed in.

        // This means setting a pedagogy for this user appropriate for this call's mode.
        // Then set parameters in the pedagogy so that it selects problems according to the other inputs
        //
        // The trick will be to callback assistments each time we finish a problem and log it.  That is
        // do we pass some Assistment object to this area of the system or can we set up some kind of
        // Bean
    }


    private String callBackForToken(TeachTopicEvent e) throws MalformedURLException {
        // do an HTTP POST to get back an access token for this user
        try {

            // temporarily here?   WPI never completed work to give back a the token they said they wanted us to request.
            if (true)
                return "tempToken";
            String response = HTTPRequest.sendPost(assUserTokenURL);
            JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
            String token = json.getString(ACCESS_TOKEN_JSON_FIELD);
            // if no token, then some error message is in the JSON that we should use
            if (token == null) {
                String err = json.getString(ERR_MSG_JSON_FIELD);
                System.out.println("Got Err back from assistments " + err);
                return null;
            }
            return token;

        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            // TODO take apart a different kind of exception and do something with the message
            return null;
        }

    }


    /**
     * ONR Phase 0 processing of TeachTopic.   It does not require any inputs other than a topicId in the Event passed in.
     *
     * @param e
     * @return
     * @throws Exception
     */
    public boolean teachTopic0(TeachTopicEvent e) throws Exception {

//            String flashClientURL = (String) servletContext.getAttribute("flashClientURI");
        int studId = UserRegistrationHandler.registerTemporaryUser(servletInfo.getConn(), DbClass.ASSISTMENTS_CLASS_NAME, User.UserType.coopStudentTest);
        SessionManager smgr = new SessionManager(servletInfo.getConn()).assistmentsLoginSession(studId);
//            int sessId = smgr.getSessionNum();
        int classId = smgr.getClassID();
        ClassInfo c = DbClass.getClass(servletInfo.getConn(), classId);
        smgr.setClient(c.getFlashClient());
        smgr.getStudentState().setTeachTopicMode(true);
        DbSession.setClientType(servletInfo.getConn(), smgr.getSessionNum(), c.getFlashClient());
        String client = smgr.getClient();

//            SessionManager smgr = new SessionManager(this.conn,ee.getSessionId(), hostPath, contextPath).buildExistingSession();
//            String client = smgr.getClient();
        LearningCompanion lc = smgr.getLearningCompanion();
        String name = null;
        if (lc != null)
            name = lc.getCharactersName();
        int topic = e.getTopic();
        smgr.getStudentState().setCurTopic(topic);
        String topicName = DbTopics.getTopicName(servletInfo.getConn(), topic);
        String tutorHutURL = getTeachTopicURL(client, smgr.getSessionNum(), name, topic);
        servletInfo.getRequest().setAttribute("teachTopicURL", tutorHutURL);
        servletInfo.getRequest().setAttribute("topicName", topicName);
        if (e.isShowTransitionPage()) {
            servletInfo.getRequest().getRequestDispatcher("portal.jsp").forward(servletInfo.getRequest(), servletInfo.getResponse());
            return false;
        } else {
            servletInfo.getOutput().append("<META HTTP-EQUIV=\"Refresh\" Content=\"0; URL=" + tutorHutURL + "\"/>");
            return true;
        }
    }

    private String getTeachTopicURL(String client, int sessId, String lc, int topic) {
        String url = Settings.flashClientPath + client;

        String args = "?sessnum=" + sessId + "&learningHutChoice=true&elapsedTime=0&mode=teachStandard" + ((lc != null) ? ("&learningCompanion=" + lc) : "") + "&topicId=" + topic; //"&problemIdString='+problemId;
        System.out.println("URL TO call flash is " + (url + args));
        return url + args;
    }

    public static void logToAssistmentsProblemEnd(SessionManager smgr, EndProblemEvent e) throws Exception {
        if (Settings.loggingBackToAssistments) {
            CoopUser u = DbCoopUsers.getUserFromWayangStudId(smgr.getConnection(), smgr.getStudentId());
            AssistmentSessionData d = DbCoopUsers.getSessionInfo(smgr.getConnection(), smgr.getSessionNum());
            // d will be null if Assistments did not pass all the required params that define its user/session, etc
            if (d == null)
                return;
            // The student model will need to be consulted to get the info about the last problem seen
            // We use this to create a ProblemData record which then is turned into JSON and POSTED to assistments.
            StudentProblemHistory hist = smgr.getStudentModel().getStudentProblemHistory();
            // get the last problem
            StudentProblemData lastProb = hist.getCurProblem();
            String json = new ProblemData(u, d, lastProb).toJSONForAssistments();
            if (d.getLogbackURL() != null)
                post(d.getLogbackURL(), json);
        }
    }

    static void post(String logbackURL, String json) throws IOException {
        HttpPost httppost = new HttpPost(logbackURL);

        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("problemData", json));
//        StringEntity params =new StringEntity(json,
//                "application/json",
//                "UTF-8");
        StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
        httppost.setEntity(params);
        httppost.setHeader("assistments-auth", "partner=\"Wayang-Ref\"");
//        HttpClient httpclient = new DefaultHttpClient();
        HttpClient httpclient = HttpClientBuilder.create().build();

        HttpResponse httpResponse = httpclient.execute(httppost);
        HttpEntity resEntity = httpResponse.getEntity();


        // Get the HTTP Status Code
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        // Get the contents of the response
        InputStream input = resEntity.getContent();
        String responseBody = IOUtils.toString(input);
        input.close();

        // Print the response code and message body
        System.out.println("HTTP Status Code: " + statusCode);
        System.out.println(responseBody);
    }


    public boolean getProblemData(GetProblemDataEvent e) throws SQLException {
        int[] a = DbCoopUsers.getSessionInfo(servletInfo.getConn(), e.getUser(), e.getProblem(), e.getAssistment(), e.getAssignment());

        if (a != null) {
            int sessId = a[0];
            int studId = a[1];
            int id = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(servletInfo.getConn(), studId);
            List<StudentProblemData> history = new ArrayList<StudentProblemData>();
            DbStudentProblemHistory.loadHistory(servletInfo.getConn(), studId, history);
            CoopUser u = new CoopUser(e.getUser(), null, studId);
            AssistmentSessionData sd = DbCoopUsers.getSessionInfo(servletInfo.getConn(), sessId);
            if (history.size() > 0) {
                StudentProblemData d = history.get(history.size() - 1);
                ProblemData pd = new ProblemData(u, sd, d);
                System.out.println(pd.toJSONForAssistments());
                servletInfo.getOutput().append(pd.toJSONForAssistments());
            } else {
                JSONObject o = new JSONObject();
                o.element("failure", true);
                servletInfo.getOutput().append(o.toString());
            }
        } else {
            JSONObject o = new JSONObject();
            o.element("failure", true);
            servletInfo.getOutput().append(o.toString());

        }
        return true;
    }
}
