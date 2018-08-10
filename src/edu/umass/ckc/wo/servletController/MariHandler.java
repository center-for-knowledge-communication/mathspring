package edu.umass.ckc.wo.servletController;

import ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.wo.admin.PedagogyRetriever;
import edu.umass.ckc.wo.assistments.CoopUser;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.internal.MariTeachCommonCoreStandardEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.exc.AdminException;
import edu.umass.ckc.wo.exc.AssistmentsBadInputException;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.pedModel.CCPedagogicalModel;
import edu.umass.ckc.wo.tutor.pedModel.CommonCorePedagogicalModel;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutor.studmod.StudentStandardMastery;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;
import edu.umass.ckc.wo.util.HTTPRequest;
import edu.umass.ckc.wo.woserver.ServletInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
 *
 * How to test this stuff:
 *
 * Goal: We need to test that MARi can invoke Mathspring for a user and see some problems.  After each problem
 * Mathspring writes stuff back to MARi about the users problem solving.
 *
 * 1.  MARi calls mathspring http://tutor.mathspring.org/woj/mathspringSubSession?ccss=4.MD.3&access_token=XYZ
 *    How to get a valid/working access_token:
 *    Goto the MARi website and login and navigate to the page for testing Mathspring.  Click Launch App button.
 *    A page is generated with some simple text in it that shows it called mathspring.  View the page source and go to the very
 *    bottom.  There will be a URL like http://tutor.mathspring.org?access_token=941633e7-6a9b-428c-8f7a-c888a5a761f2
 *    .  Grab this access_token
 *
 * 2.  In the location box of the browser use:
 *    http://tutor.mathspring.org/woj/mathspringSubSession?ccss=4.MD.3&access_token=941633e7-6a9b-428c-8f7a-c888a5a761f2
 *    http://localhost:8080/mt/mathspringSubSession?ccss=4.MD.3&access_token=941633e7-6a9b-428c-8f7a-c888a5a761f2
 *    Fire it off as a GET request
 *    You should enter Mathspring viewing a problem that is in the requested ccss 4.MD.3
 *    Set a breakpoint in the method  logToMariProblemEnd and see what happens after working with each problem.
 * 3.  If logging back to MARi fails it will be necessary to use Google Postman REST tool and put in POST requets like:
 *     https://api.mari.com/v2/developer/write    with a JSON payload like:
 *     {
 "session_token": "21de47c291dbd81aed27f0f1e64bc8e4ee1cb675bc16475b237e522b8bc83367",
 "data" : [{
 'pa': '4.MD.3',
 'value': '0.85',
 'confidence': 1.0,
 }]
 }
 *
 */
public class MariHandler extends Handler {

    private static Logger logger = Logger.getLogger(MariHandler.class);


    public static final String END_PAGE = "farewell.html";
    private static final String SESSION_TOKEN_JSON_FIELD = "session_token";
    private static final String ERR_MSG_JSON_FIELD = "error_description";
    private static final String ERR_TYPE_JSON_FIELD = "error";
    public static final String API_KEY = "5be1-5adf-6fec-4d02-2009-48e2-68bb-8708-4a3a-e453-7250-8bb3";  // a key assigned to our Mathspring app in the MARi setup (see mari.com/developer)

    private String mariSessionURL = "https://www.mari.com/get_session/?access_token=%s&api_key=%s";
    private static final String MARI_WRITEBACK_URL = "https://api.mari.com/v2/developer/write";

    //    private SessionManager smgr;
    private ServletInfo servletInfo;
    private Connection conn;

    public static final String assistmentServletName = "partnerProblemLog";
    public static String assistmentsLogbackURL = "https://test1.assistments.org/api2/partnerProblemLog"; // this will be overwritten by value from web.xml
//    private static String assistmentsLogbackURL = "http://rose.cs.umass.edu/mt/TutorBrain?action=EnterTutor";

    public MariHandler() {

    }

    public boolean teachStandard(TutorSubSessionEvent e) throws AssistmentsBadInputException {
        try {
            // Params that indicate its really from Assistments are present (must include user)
            if (e.isFromParentApp())
                return teachStandardToMariUser(e);

                // An external call (no Mari params) so its going to be temp user for this call only
            else
                return teachStandardExternalTempUser(e);

        } catch (Exception exc) {
            throw new AssistmentsBadInputException(exc.getMessage());
        }
    }


    private String genUser() throws SQLException {
        SecureRandom r = new SecureRandom();
        String s = new BigInteger(130, r).toString(20);
        while (DbCoopUsers.getUser(conn, s) != null)
            s = new BigInteger(130, r).toString(20);
        return s;
    }



    public boolean teachStandardExternalTempUser (TutorSubSessionEvent e) throws Exception, AssistmentsBadInputException {
        // This call is for testing the API only.   So we create a temp user that is allowed to test.
        UserRegistrationHandler.genName(conn, "externalCaller");
        // absence of isTest has meaning isTest=false (a switch from Tom's original spec which was confusing).
        boolean isTest = e.isTestUser();

        int studId = -1;
        if (isTest)
            studId=UserRegistrationHandler.registerTemporaryUser(conn, DbClass.MARI_CLASS_NAME, User.UserType.externalTempTest);
        else
            studId=UserRegistrationHandler.registerTemporaryUser(conn, DbClass.MARI_CLASS_NAME, User.UserType.externalTempNonTest);

        if (e.isShowTransitionPage()) {
            return showIntroPage(e);
        } else return processTeachTopicRequest(e, studId, null);
    }


    /**
     * MARi invokes Mathspring with a URL like:
     * http://tutor.mathspring.org/mt/mathspringSubSession?ccss=4.MD.3&access_token=XYZ
     *
     * Mathspring then makes a GET request back to MARi with this access_token + the Mathspring API key (which identifies Mathspring as legit)
     * https://www.mari.com/get_session/?access_token=12345678901234567890&api_key=12345678901234567890
     * Mari then returns JSON that includes a session token which will identify this user for the duration of MARi/Mathspring usage.
     * {
     "session_token": "12345678901234567890"
     */
    public boolean teachStandardToMariUser(TutorSubSessionEvent e) throws Exception {
        int studId = -1;
        String accessToken = e.getAccessToken();
        String sessionToken = callbackForSessionToken(accessToken);
        if (sessionToken == null)
            throw new AssistmentsBadInputException("Cannot get a session token for the MARi token " + accessToken);
//        user = UserRegistrationHandler.genName(conn, "externalCaller");  // Temporarily generate a new user name based on guest counts
        boolean isTestUser = e.isTestUser(); //  If MARi passes ?test=true (default is false)
        // Find if this sessionToken has been in the system previously
        CoopUser u = DbCoopUsers.getUser(conn, sessionToken);
        // This is the first we've ever heard of this user.
        if (u == null) {
            // creates a wayang user and assigns a pedagogy to him
            // legit user params are given from Assistments, so we create a user in our system that has persisting user/data
            if (isTestUser) {
                User.UserType ut = User.UserType.coopStudentTest;
                studId = UserRegistrationHandler.registerExternalUser(servletInfo.getConn(), DbClass.MARI_CLASS_NAME, sessionToken, ut, true);
                u = DbCoopUsers.insertUserInDb(conn, sessionToken, accessToken, studId);
            }
//            a call made by MARi that is not a test.
            else {
                User.UserType ut = User.UserType.coopStudent;
                studId = UserRegistrationHandler.registerExternalUser(servletInfo.getConn(), DbClass.MARI_CLASS_NAME, sessionToken, ut, true);
                u = DbCoopUsers.insertUserInDb(conn, sessionToken, accessToken, studId);
            }
            if (e.isShowTransitionPage())
                return showIntroPage(e);
        } else {
            // We may have heard of this user but we still want this session to run correctly based on the isTest param (e.g. a previous
            // session for this user may have had isTest=false and this one may have isTest=true)

            User.UserType ut;
            if (isTestUser)
                ut = User.UserType.coopStudentTest;
            else ut =  User.UserType.coopStudent;
            UserRegistrationHandler.setUserTestProperty(servletInfo.getConn(),u.getStudId(),ut);
            studId = u.getStudId();
        }
        return processTeachTopicRequest(e, studId, u);

    }

    private boolean showIntroPage(TutorSubSessionEvent e) throws ServletException, IOException {
        // Build the same URL as what was given except strip off the showTransitionPage=true so that when the user clicks
        // link it will reenter here and not show the video.
        String url = servletInfo.getServletName() + "?" + servletInfo.getRequest().getQueryString().replace("&showTransitionPage=true", "");

        servletInfo.getRequest().setAttribute("teachTopicURL", url);
        servletInfo.getRequest().getRequestDispatcher("portal.jsp").forward(servletInfo.getRequest(), servletInfo.getResponse());
        return BaseServlet.FORWARDED_TO_JSP;
    }

    // TODO This is based on the below method.  It's going to be stripped down so that it is only based on being given an standard to teach
    // and perhaps some parameters like max time, min time, etc.
    private boolean processTeachTopicRequest(TutorSubSessionEvent e, int studId, CoopUser u) throws AssistmentsBadInputException, Exception {
        int clId = DbUser.getStudentClass(conn, studId);
        List<Pedagogy> peds = DbClassPedagogies.getClassPedagogies(conn, clId);
        // check to make sure this class has one pedagogy because Assistments is going to configure it so it needs to be
        // a configurable Pedagogy
        if (peds.size() > 1) {
            logger.error("MARi user is in a class with more than one pedagogy.  Only one pedagogy should be assigned to a class");
        }
        Pedagogy pedagogy = peds.get(0);  // This will be known pedagogy CommonCorePedagogicalModel
        String[] ccss = e.getStandards(); // TODO we can't really handle multiple standards passed in yet.   See TODO in CCSSProblemSelector.selectProblem
        int maxtime = e.getMaxtime();
        int maxprobs = e.getMaxprobs();
        float mastery = e.getMasteryLevel();
        if (maxtime <= 0)
            throw new AssistmentsBadInputException("maxTime must be greater than 0");
        if (maxprobs <= 0)
            throw new AssistmentsBadInputException("maxProblems must be greater than 0");
        String mode=null;
        boolean showIntro = false;
        DbUserPedagogyParams.clearUserPedagogyParams(conn, studId);  // get rid of parameters from previous calls from Ass
        DbUserPedagogyParams.saveUserPedagogyParams(conn, studId, maxtime, maxprobs, ccss, mastery, Integer.parseInt(pedagogy.getId())); // save the params for this mini-session
        SessionManager smgr = new SessionManager(conn).assistmentsLoginSession(studId);
        smgr.getPedagogicalModel().newSession(smgr.getSessionNum());
        smgr.getStudentState().newSession(smgr);
        smgr.getStudentState().setCurLocation(NavigationEvent.SAT_HUT);
        // The tutor needs to initialize its state when user first enters it.
        smgr.getStudentState().setTutorEntryTime(System.currentTimeMillis());
        smgr.getStudentState().setCurProblemIndexInLesson(-1); // do this so problem selector will think its the beginning of a lesson
        // Generate the tutor page so it shows what we want rather than making a next-prob callback to the server.
        PedagogicalModel pedMod = smgr.getPedagogicalModel();  // This is going to be CommonCorePM
        NextProblemEvent npe;

//        npe.setUserInput("");

        //   Use a simple subclass of BasePedModel.   It will use a CCSSProblemSelector and the LessonModel.
        // There really isn't any lesson-level intervention going on (as in intros or switching topics) so the lesson model is a no-op.
        // The lesson model holds a few parameters like the CCSS that is being taught, the min/max number of problems, the max Time,  desired
        // mastery level, and diff rate.
        // The problem selector will behave similar to a topic problem selection except it will start with a group of problems that contain
        // the requested standard(s).
        InternalEvent ie = new MariTeachCommonCoreStandardEvent(); //
        pedMod = (CommonCorePedagogicalModel) pedMod;   // TODO This is for debugging only so I can check the API

        Response resp = pedMod.processInternalEvent(ie); // This will call the problem selector to get a problem.

        // For now we assume it is giving back a ProblemResponse rather than something else like an intervention
        // TODO If a MARi user came into a particular standard previously and saw all the problems, coming in again will cause a content failure
        // because the system may not be able to find a problem.  It will not gracefully deal with this.
        if (resp instanceof  ProblemResponse) {
            Problem prob = ((ProblemResponse) resp).getProblem();
            int probId = prob.getId();
            smgr.getStudentModel().newProblem(smgr.getStudentState(), prob);  // this does not set curProb = new prob id,
            smgr.getStudentState().setCurProblem(probId);
            smgr.getStudentModel().save();
            boolean showMPP = smgr.getPedagogicalModel().isShowMPP();
            new TutorPage(servletInfo, smgr).createTutorPageFromState(0, 0, -1, (ProblemResponse) resp, "practice", null, true, prob.getResource(), null, false, -1, showMPP);
        }
        else {
            Intervention interv = ((InterventionResponse) resp).getIntervention();
            boolean showMPP = smgr.getPedagogicalModel().isShowMPP();
            new TutorPage(servletInfo, smgr).createTutorPageFromState(0, 0, -1, (InterventionResponse) resp, "practice", null, true, null, null, true, -1, showMPP);

        }

//        new TutorLogger(smgr).logMPPEvent(e,lastProbId);

        return BaseServlet.FORWARDED_TO_JSP;


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






    private String callbackForSessionToken(String token) {
        try {
            String url = String.format(mariSessionURL,token, API_KEY );
            String response = HTTPRequest.sendGet(url);
            JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
            String sessionToken = json.getString(SESSION_TOKEN_JSON_FIELD);
            // if no token, then some error message is in the JSON that we should use
            if (sessionToken == null) {
                String errMsg = json.getString(ERR_MSG_JSON_FIELD);
                String errType = json.getString(ERR_TYPE_JSON_FIELD);
                logger.error("Could not get a session token from MARi.  Got Error of type " + errType + "from MARI " + errMsg);
                return null;
            }
            else return sessionToken;
        } catch (Exception e) {
            logger.error("Call to MARi server failed!");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

            // TODO I'm getting errors when I try to call the mari URL so I'll return a fake token

        }
        return "a_fake_session_token";
    }





    /*
    Write back to MARi to tell it the students mastery of this standard using a POST to
    https://api.mari.com/v2/developer/write
    with JSON like:
    {
  "session_token": "21de47c291dbd81aed27f0f1e64bc8e4ee1cb675bc16475b237e522b8bc83367",
  "data" : [{
      'pa': 'dD2A-iR2x-EIbW-7uzt',
      'value': '0.85',
      'confidence': 1.0,
    }]
  "local-context": {prop1: 1, prop2: 2, ....}
}

     TODO local-context is a String of JSON that has fields and values that are not understood by MARi but may be used by it
     */
    public static void logToMariProblemEnd(SessionManager smgr, EndProblemEvent e, CoopUser u) throws Exception {
        PedagogyParams params = DbUserPedagogyParams.getPedagogyParams(smgr.getConnection(), smgr.getStudentId());
        String ccss = params.getCcss();

        // The student model will need to be consulted to get the info about the last problem seen
        // We use this to create a ProblemData record which then is turned into JSON and POSTED to assistments.
        StudentProblemHistory hist = smgr.getStudentModel().getStudentProblemHistory();
        // TODO:  At this point ccss is a single standard.  If this lesson were multiple standards , we'd get a CSV of standards from db here.
        // and would have a more complex JSON to build.
        StudentStandardMastery mastery = DbStudentStandardMastery.getMastery(smgr.getConnection(), smgr.getStudentId(), ccss);
        // get the last problem
        StudentProblemData lastProb = hist.getCurProblem();
        String json = getJSONForMari(u, mastery, lastProb);
        post(MARI_WRITEBACK_URL, json);
    }


    /*
    Produce JSON for a single problem and send back to MARi
    {
    "session_token": "21de47c291dbd81aed27f0f1e64bc8e4ee1cb675bc16475b237e522b8bc83367",
    "data" : [{
    'pa': '4.MD.3',
    'value': '0.85',
    'confidence': 1.0,
    'local-context': "{\"a\": 10, \"b\": \"Hi there\"}"
    }]
    }
    The local-context has to have all its double quotes escaped with \
    */
    public static String getJSONForMari(CoopUser u, StudentStandardMastery ccssMastery, StudentProblemData lastProb) {
        JSONObject o = new JSONObject();
        // For some reason Assistments wants a strange form of JSON with everything quoted and subobjects all have quotes escaped.
        o.element("session_token", u.getUid());
        JSONArray a = new JSONArray();
        JSONObject a1 = new JSONObject();
        a1.element("pa", ccssMastery.getStd());
//        a1.element("pa", "bdzL-hVx7-TnFb-CqC8");
        a1.element("value", Double.toString(ccssMastery.getVal()));
        a1.element("confidence", 1.0);
        boolean includeExtra = true;
        if (includeExtra)
            a1.element("local-context", getLocalContextJSON(lastProb));
        a.add(a1);   // because we are only returning one record we have to had it to A
        o.element("data", a);
        String r = o.toString();
        logger.debug(r);
        return r;
    }

    private static String getLocalContextJSON(StudentProblemData lastProb) {
        JSONObject x = new JSONObject();
        x.element("probId",lastProb.getProbId());
        x.element("effort",lastProb.getEffort());
        x.element("numHintsBeforeCorrect", lastProb.getNumHintsBeforeCorrect());
        x.element("numAttemptsToSolve",lastProb.getNumAttemptsToSolve());
        x.element("problemDifficulty",lastProb.getProbDifficulty());
        x.element("timeToSolveMs",lastProb.getTimeToSolve());
        x.element("timeToFirstHintMs",lastProb.getTimeToFirstHint());
        x.element("timeToFirstAttemptMs",lastProb.getTimeToFirstAttempt());
        x.element("timeInProblemSeconds",lastProb.getTimeInProblemSeconds());
        x.element("numHints",lastProb.getNumHints());
        x.element("numMistakes",lastProb.getNumMistakes());
        String r = x.toString();
        r = StringEscapeUtils.escapeJava(r);
        return r;
    }


    static void post(String logbackURL, String json) throws IOException {
        HttpPost httppost = new HttpPost(logbackURL);
        StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
        httppost.setEntity(params);
//        httppost.setHeader("assistments-auth", "partner=\"Wayang-Ref\"");
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



    @Override
    public boolean init(ServletInfo info) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    /**
     * The handler should only get called with TutorSubSessionEvent.  All others will cause an exception
     */
    public boolean handleEvent(ServletInfo info) throws Exception {
        this.servletInfo = info;
        this.conn = info.getConn();
        TutorSubSessionEvent ev = new TutorSubSessionEvent(info.getParams());
        boolean res = teachStandard(ev);
        return res;
    }

    @Override
    public boolean cleanup() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static List<Integer> getStdProblemsForStudent(Connection conn, int studId, String ccstd) throws Exception {

        SessionManager smgr = new SessionManager(conn).assistmentsLoginSession(studId);

        // Generate the tutor page so it shows what we want rather than making a next-prob callback to the server.
        PedagogicalModel pedMod = smgr.getPedagogicalModel();  // This is going to be CommonCorePM
        List<Problem> probs = ProblemMgr.getStandardProblems(smgr.getConnection(), ccstd);
        // Turn this list of Problem objects into a simple list of integer ids.
        List<Integer> probIds = new ArrayList<Integer>();
        for (Problem p : probs) {
            probIds.add(p.getId());
        }
        StudentProblemHistory studentProblemHistory = smgr.getStudentModel().getStudentProblemHistory();
        // get the history records for problems this student has seen on this standard
        List<StudentProblemData> probEncountersInStandard = studentProblemHistory.getCCSSHistoryMostRecentEncounters(ccstd);
        // reduce this history to only the problems that are considered "recently correctly solved"
        List<Integer> recentProbs = smgr.getExtendedStudentState().getRecentExamplesAndCorrectlySolvedProblems(probEncountersInStandard);
        // eliminate these recently solved ones from the list of problems
        probIds.removeAll(recentProbs);
        return probIds;
    }
}
