package edu.umass.ckc.wo.smgr;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.admin.PedagogyRetriever;
import edu.umass.ckc.wo.admin.StrategyAssigner;
import edu.umass.ckc.wo.beans.ClassConfig;
import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.event.AdventurePSolvedEvent;
import edu.umass.ckc.wo.event.tutorhut.LogoutEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.handler.NavigationHandler;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.login.LoginResult;
import edu.umass.ckc.wo.mrcommon.Names;
import edu.umass.ckc.wo.state.ExtendedStudentState;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.strat.ClassStrategyComponent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.strat.StrategyMgr;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * Frank	09-14-20	issue #237 added isTrialUser method changed text message
 * Frank	09-23-20	issue #237 added event logging
 */

public class SessionManager {

    public static final String LOGIN_USER_PASS = "uname_password_login_check";
    public static final int NUM_GROUPS = 20;
    public static final int STUDENT_TYPED_GROUP = 5;
    private static final Logger logger = Logger.getLogger(SessionManager.class);
    private static final String OTHER_ACTIVE_SESSIONS = "other_active_sessions";
    private static final String WRIST_BRACELET_ID_ = "wrist_bracelet_active_session";
    private static final String LOGIN_USER_MOM = "uname_momname_login_check";
    private static final String MESSAGE = "message";
    private static int guestIDCounter = 0; // used for generating guest user IDS
    
    private static final String ACCESS_PAUSED = "Access paused";
    private static final String LOGIN_SUCCESSFUL = "Login successful";
    
    private Connection connection;


    private int studId = -1;
    private int classId = -1;
    private int sessionId = -1;
    private int pedagogyId = -1;
    private int strategyId = -1;
    private boolean usesStrategy = false;
    private boolean usesPedagogy= false;
    private String client = null; // the name of the Flash client that the user should use
    private WoProps woProps;
    private StudentState studState; // state variables about student stored in db
    private ExtendedStudentState extStudState;
    //    private StudentProfile profile; // contains satv,satm, gender, and group
    private Random ran = new Random();
    private StudentModel studentModel = null;
    private PedagogicalModel pedagogicalModel = null;
    private String loginResponse = null;
    private LearningCompanion learningCompanion = null;
    private LoginResult loginResult;
    private long timeInSession;
    private String hostPath; // The piece of the request URL that gives http://chinacat.../
    private String contextPath; // The full URL up to /TutorBrain (e.g.  http://localhost:8082/wo4
    private Locale locale = new Locale("en","US");
    private boolean assistmentsUser = false;
    private long elapsedTime = 0;
    private boolean testUser;
    private User user;
    private int collaboratingWith;
    private int eventCounter; // a counter that gets incremented on every tutor event (used to keep events ordered)
    private int mouseSaveInterval=0; // tells the client how often to save mouse coords to the server; <= 0 indicates no mouse tracking

    public SessionManager(Connection connection) {
        this.connection = connection;
        timeInSession = 0;
    }

    public SessionManager(Connection conn, int sessionId) {
        this.connection = conn;
        this.sessionId = sessionId;
    }

    /**
     * Build a SessionManager which is a container for all student information for this student's request.
     * <p/>
     * Recent addition: Using the group number a student is assigned to, a Pedagogy object is retrieved.
     * From that we can get the name of the StudentModel class that is appropriate for the student.
     * We then can construct the StudentModel now and have it ready thoughout the life of this HTTP request.
     * <p/>
     * The hostPath is a partial URL to the servlet (just through the host and port).   This is necessary for
     * calls that will eventually come from a JSP client page to find the Flash client.   It is presumed to be running on the same host.
     *
     * @param connection
     * @param sessionId
     * @param hostPath
     * @param contextPath
     * @throws Exception
     */
    public SessionManager(Connection connection,
                          int sessionId, String hostPath, String contextPath) throws Exception {
        this(connection, sessionId);
        this.hostPath = hostPath;
        this.contextPath = contextPath;
    }

    public SessionManager(Connection connection,
            int sessionId, String hostPath, String contextPath, Locale loc) throws Exception {
    	this(connection, sessionId);
    	this.hostPath = hostPath;
    	this.contextPath = contextPath;
    	this.locale = loc;
    }

    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection();
            SessionManager smgr = new SessionManager(conn);
            smgr.checkSessionTimes(conn, 15574);
//            DbSession.cleanupStaleSessions(conn);
//            FileReader fis = new FileReader("u:\\wo\\100users\\sequenceGroupTest.csv");
//            BufferedReader bis = new BufferedReader(fis);
//            String line;
//            line = bis.readLine(); // get rid of headers
//            while ((line = bis.readLine()) != null) {
//                String[] x = CSVParser.parse(line);
//                String fname= x[2];
//                String lname= x[3];
//                String password= fname;
//                String userName = fname+lname.substring(0,1);
//                int group = Integer.parseInt(x[1]);
//                int id = smgr.createUser(conn,fname,lname,userName,password,"");
//                StudentProfile prof = new StudentProfile(0,0,id,"",group);
//                smgr.insertStudentProfile(prof);
//                smgr.updateProfileGroup(id,group);
//                System.out.println(fname + " " + lname + ": id=" + id + " userName=" + userName + " password=" + password
//                + " group=" + group);
//            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    // Each event coming in from a session has an eventCounter param.  This counter is incremented each time the client sends
    // an event so that each call from the client can be put into the correct sequence.  This is to protect against events
    // arriving out of order because of network lag.  The server verifies that each event coming in has a counter one greater
    // than the last event (the last event's counter is stored in the session table).
    // Possible situations:
    // o  the eventCounter is 1 greater than the last one - what we want
    // o  the eventCounter is more than 1 greater than the last one -  we must have missed an event or not received it yet.
    //      It's not clear what the best solution to this is:
    //         FOR NOW:  we will print an error in the output log so we can at least know that this happened
    //         Maybe its possible to wait a short time for the missing event(s) by sleeping.  If events come in within the waiting period
    //         we can put them in a list in the correct order and then process them one at a time in sequence.
    // o  the eventCounter is not included - a programming mistake.  We'll print a warning
    // Returns the event counter that is set into the Session in the db or -1 when the event counter sent in the event
    // is erroneous.
    private int checkEventCounter(Connection conn, ServletParams params) throws SQLException {
        int c = params.getInt("eventCounter", -1);
        if (c == -1)
            logger.warn("Got an event that did not have an eventCounter " + params);
        else {
            int sessId = params.getInt("sessionId");
            int lc = DbSession.updateEventCounter(conn, sessId, c, false); // bump the counter only if this event is 1 greater than last
            if (lc == c)
                return c; // the only non-error. what is expected when the events are received in correct sequence. return the counter
            else if (lc == -1)
                logger.error("Failed to find session id " + sessId + " when trying to update event counter");
                // Now we have to figure out how to recover.
                // Case 1: c is greater than lc by more than 1 (c > lc).  This means we haven't received events with counters between lc+1 and c-1 (inclusive)
            else if (c > lc) {
                logger.error("Events out of sequence: Event received TOO SOON. Last event counter was " + lc + ". This event counter is " + c);
                // jump the counter forward so that when we get the event with counter c+1 we can resume without errors
                DbSession.updateEventCounter(conn, sessId, c, true);
                return c; // return the counter even though its an error because we need to have it in the session mgr.
            }
            // Case 2: This event was received after we processed an event with higher counter (the result of a previous case 1 jump-ahead)
            else if (c < lc) {
                // NOte that we will almost always generate a Case 2 error after a Case 1 error because Case 1 jumps the counter ahead
                // and when we receive the event that we were missing it is now considered late.
                logger.error("Events out of sequence: Event received TOO LATE. Last event counter was " + lc + ". This event counter is " + c);
            }

        }
        return -1;  // error cases return -1
    }

    public SessionManager buildExistingSession(ServletParams params) throws Exception {
        int lc = checkEventCounter(connection, params);
        if (lc != -1)
            this.setEventCounter(lc);
        return buildExistingSession();
    }

    public SessionManager buildExistingSession() throws Exception {

        buildSession(connection, sessionId);
        return this;
    }

    public LoginResult login(String uname, String password, long sessBeginTime, boolean logoutExistingSession, Locale loc) throws Exception {
        return attemptSessionCreation(uname, password, sessBeginTime, logoutExistingSession, loc);
    }

    /**
     * Constructor only used when the user is logging in from Assistments
    */
    public SessionManager assistmentsLoginSession(int studId) throws Exception {
        int newSessId = DbSession.newSession(connection, studId, System.currentTimeMillis(), true);
        timeInSession = 0;
        loginResult = new LoginResult(newSessId, null);
        sessionId = loginResult.getSessId();
        buildSession(connection, loginResult.getSessId());
        studState.getSessionState().initializeState();
        return this;
    }


    /**
     * Constructor only used when the user is logging in as guest.
     */
    public SessionManager guestLoginSession(int studId) throws Exception {
        int newSessId = DbSession.newSession(connection, studId, System.currentTimeMillis(), false);
        timeInSession = 0;
        loginResult = new LoginResult(newSessId, null);
        sessionId = loginResult.getSessId();
        buildSession(connection, loginResult.getSessId());
        studState.getSessionState().initializeState();
        return this;
    }

    // will look something like http://cadmium.cs.umass.edu/  or http://localhost/  (port removed)
    public String getHostPath() {
        return this.hostPath;
    }

    // returns something like http://localhost:8082/wo4
    public String getContextPath() {
        return this.contextPath;
    }

    public void setLocale(Locale loc) {
    	this.locale = loc;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void createSessionForStudent(int studId) {

    }

    public LearningCompanion getLearningCompanion() {
        return learningCompanion;
    }

    public String getLoginResponse() {
        return loginResponse;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    private Pedagogy getPedagogyOrStrategyForStudent (Connection conn, int studId, int classId) throws Exception {
        TutorStrategy strat = StrategyMgr.getStrategy(connection,studId); // will return null if student doesn't have a strategyId
        if (strat != null) {
            this.usesStrategy=true;
            this.usesPedagogy=false;
            this.strategyId= Integer.parseInt(strat.getId());
            return strat;

        }
        // It's possible that the class is using tutoring strategies instead of pedagogies.  The way to find out is to try to get
        // the students pedagogy.  If it's there, we use pedagogies.  If nothing is there, we will then see if the class has some strategies assigned
        // and will select a strategy and assign it to the student NOW.
        Pedagogy ped = PedagogyRetriever.getPedagogy(conn, studId);
        if (ped != null) {
            this.usesPedagogy = true;
            this.usesStrategy=false;
            this.pedagogyId = Integer.parseInt(ped.getId());
            return ped;

        }
        // The student is not set up with either a pedagogy or a strategy.
        else {
            List<TutorStrategy> strats = DbStrategy.getStrategies(conn,classId);
            // If the class is set up with strategies and this is the students first login we can randomly assign a strategy
            // to the student at this time using a load balancing strategy.
            if (strats.size() > 0) {
                this.usesStrategy=true;
                this.usesPedagogy=false;
                // assign a strategy to the student
                strat= StrategyAssigner.assignStrategy(conn,studId,classId);
                this.strategyId = Integer.parseInt(strat.getId());
                return StrategyMgr.getStrategyFromCache(conn,this.strategyId);

            }
            // Class is not set to use strategies, so use a default pedagogy and this student might be able to get something done.
            else {
                this.usesPedagogy = true;
                this.usesStrategy=false;
                List<Pedagogy> peds = PedagogyRetriever.getDefaultPedagogies();
                ped = peds.get(0);
                this.pedagogyId = Integer.parseInt(ped.getId());
                return ped;
            }
        }
    }

    private void buildSession(Connection connection, int sessionId) throws Exception {
        DbSession.updateSessionLastAccessTime(connection, sessionId);
        int[] ids = DbSession.setSessionInfo(connection, sessionId);
        this.studId = ids[0];
        this.classId = ids[1];
   
        ClassInfo cl = DbClass.getClass(connection, this.classId);
        
        String language = "en";
        language = cl.getClassLanguageCode();
        if (language.startsWith("es")) {
        	language = "es";
            this.locale = new Locale(language,"Ar");
        }
        System.out.println("Locale from Class " + this.locale.toString());
        this.user = DbUser.getStudent(connection, this.studId);
        this.setClient(DbSession.getClientType(connection, sessionId));
        woProps = new WoProps(connection);
        woProps.load(studId);   // get all properties for studId
        Timestamp lastLoginTime = DbSession.getLastLogin(connection, studId);
        if (lastLoginTime != null)
            this.timeInSession = System.currentTimeMillis() - lastLoginTime.getTime();

        setStudentState(woProps);   // pull out student state props from all properties
        // Will either return a TutorStrategy or a Pedagogy depending on what the student is using.
        Pedagogy ped = getPedagogyOrStrategyForStudent(connection, studId, this.classId);
        instantiatePedagogicalModel(ped);

        // these are the parameters as dfined in the XML file pedagogies.xml


        // pedagogical model needs to be instantiated as last thing because its constructor takes the smgr instance (this)
        // and makes calls to get stuff so we want this as fully constructed as possible before the call to instantiate
        // so that the smgr is fully functional except for its ped model.
//        PedagogicalModelParameters pedModelParams = getPedagogicalModelParametersForUser(connection, ped);
        // build the Pedagogical model for the student.  The PedagogicalModel constructor is responsible for
        // creating the StudentModel which also gets set in the below method



        // set theparams on the ped model
//        pedagogicalModel.setParams(pedModelParams);

//            if (userParams != null)
//                ((ConfigurablePedagogy) pedagogicalModel).configure(userParams);
//       }
        this.learningCompanion = this.pedagogicalModel.getLearningCompanion();
    }

    // only used by test driver
    public void assignUserToGroup(String group_string) throws SQLException {
        int group = 0;

        group = ran.nextInt(NUM_GROUPS);
        this.pedagogyId = group;

    }

    public StudentState getStudentState() {
        return studState;
    }

    private void setStudentState(WoProps props) throws SQLException {
        studState = new StudentState(this.getConnection(), this);
        studState.setObjid(this.getStudentId());
        studState.extractProps(props);  // pull out student state props from all properties
        this.extStudState = new ExtendedStudentState(this);
    }

    private String getXML(boolean success) {
        return Names.XML_HEADER + (success ? Names.LOGIN_SUCCESS : Names.LOGIN_FAILURE);

    }

    public String adventureProblemSolved(AdventurePSolvedEvent e) throws Exception {
        long elapsedTime = e.getElapsedTime();
        String userInput = e.getUserInput();
        int probId = e.getProbId();
        int studId = getStudentId();

//        if (elapsedTime <= 0 || probId <= 0 || studId <= 0)
        if (elapsedTime <= 0 || studId <= 0)
            return Names.ADVENTURE_PROBLEM_STORED_FAILURE;

        DbAdventureLogger.logAdventureProblemSolved(getConnection(), e, elapsedTime, userInput, probId, studId, this.getSessionNum(), e.getAdventureName());
        return Names.ADVENTURE_PROBLEM_STORED_SUCCESS;
    }

    public int getStudentClass(int studId) throws SQLException {
        return DbUser.getStudentClass(this.getConnection(), studId);
    }

    public int getClassID() {
        return this.classId;
    }

    public int findMaxActiveSession(int studId) throws SQLException {
        return DbSession.findMaxActiveSession(getConnection(), studId);
    }

    private GregorianCalendar calcCurTime(String clientBeginTime) {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String[] cbt = {"", ""};

        if (clientBeginTime != null) {
            cbt = clientBeginTime.split(",");
            if (cbt.length > 1) {
                String[] clientTime = cbt[0].split(":");
                String[] clientDate = cbt[1].split("-");
                if (clientDate.length > 1) {
                    // the month in Calendar is 0 indexed, so need to subtract 1
                    month = (new Integer(clientDate[0])).intValue() - 1;
                    day = (new Integer(clientDate[1])).intValue();
                    year = (new Integer(clientDate[2])).intValue();
                }
                if (clientTime.length > 1) {
                    hour = (new Integer(clientTime[0])).intValue();
                    minute = (new Integer(clientTime[1])).intValue();
                    second = (new Integer(clientTime[2])).intValue();
                }
            }
        }


        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, hour, minute, second);
        return cal;
    }

    private void clearDemoUsers(String uname) throws java.lang.Exception {
        String demoUsers = "'test','6thtest1', '6thtest2', 'testch2', 'testch3', " +
                "'Jakedemo6', 'Janedemo6','Jakedemo10','Jakedemo10'," +
                "'testch21', 'testch22'";

        if (demoUsers.contains("'" + uname + "'")) {

            String sql = "delete from episodicdata2 where studid in " +
                    "(select id from student where username='" + uname + "')";
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.executeUpdate();

            sql = "delete from woproperty where property not like '%pretest%' " +
                    "and objid in (select id from student where username ='" + uname + "')";

            ps = this.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }
    }

    public String getLoginView(String check, String checkVal, String message, int sessionId, int studentId, LearningCompanion learningCompanion) {
        StringBuffer result = new StringBuffer();
        result.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
        if (check != null && checkVal != null)
            result.append("&" + check + "=" + checkVal + "\n");
        if (message != null)
            result.append("&" + MESSAGE + "=" + message + "\n");
        result.append("&allow_skip_intro=" + NavigationHandler.TRUE);  // fixed value for now
        result.append("&sessionId=" + sessionId); // dam added 6/14/05 so client keeps session state
        result.append(getLC(learningCompanion));  // dam 6/24/05  LC provided by PedagogicalModel which is constructed before this call
        return result.toString();
    }

    private String getLC(LearningCompanion lc) {

        if (lc != null)
            return ("&learningCompanion=" + lc.getCharactersName());
        else return "";
    }

    /**
     * If the user/pw is valid combo,  this looks for an active session for this user.   If it finds one, it deactivates it (we don't
     * want a user logged in twice).   It then creates a new session
     *
     * @param uname
     * @param password
     * @param sessBeginTime
     * @param logoutExistingSession
     * @return
     * @throws Exception
     */
    // Frank TBD translate
    public LoginResult attemptSessionCreation(String uname, String password, long sessBeginTime, boolean logoutExistingSession, Locale loc) throws Exception {
    	ResourceBundle rb = null;
        try {
        	
    		// Multi=lingual enhancement
    		rb = ResourceBundle.getBundle("MathSpring",loc);
        }
        catch (MissingResourceException e ) {
            return new LoginResult(-1, e.getMessage(), LoginResult.ERROR);        	
    	}
        if (uname.trim().length() == 0 || password.trim().length() == 0)
            return new LoginResult(-1, rb.getString("invalid_username_password_combination"), LoginResult.ERROR);
        else {
            studId = DbUser.getStudent(this.getConnection(), uname, password);

            if (studId == -1) {
            	String msg = rb.getString("invalid_username_password_combination") + "  " + rb.getString("ask_teacher_for_password");
                return new LoginResult(-1, msg, LoginResult.ERROR);
            } else {
                this.user = DbUser.getStudent(this.getConnection(), studId);
                int classId = DbUser.getStudentClass(this.getConnection(), studId);
                this.classId = classId;
                // We delete a student that does not have a classID because we expect them to re-register with
                // the same user/passwd and need to get this old bogus user out of the way.
                if (classId == -1) {
                    DbUser.deleteStudent(connection, studId);
                    return new LoginResult(-1, rb.getString("user_invalid_not_in_class"), LoginResult.ERROR);
                }

                  //Remove collaboration requests and pairings for students who have just logged in, as any such data is erroneous.
                CollaborationManager.clearOldData(studId);
                int oldSessId = DbSession.findActiveSession(getConnection(), studId);
                Pedagogy ped;
                if (oldSessId != -1) {
                    String msg = String.format("%s <b>%s</b> %s <b>%s %s</b> %s", rb.getString("the_user_name"),user.getUname(), rb.getString("whose_name_is"), user.getFname(), user.getLname(),rb.getString("is_already_logged_in_instructions"));
                    if (!logoutExistingSession) {
                        if (DbUser.isLoginPaused(connection, studId)) {
                            new TutorLogger(this).logStudentAccess(studId, ACCESS_PAUSED);
       	                    return new LoginResult(-1, rb.getString("login_is_paused"), LoginResult.ERROR);                	
                        }
                        else {
                            return new LoginResult(-1, msg, LoginResult.ALREADY_LOGGED_IN);
                        }
                    }
                    else {
                        inactivateAllUserSessions();
                        this.sessionId = DbSession.newSession(getConnection(), studId, sessBeginTime, false);
                        if (DbUser.isLoginPaused(connection, studId)) {
                            new TutorLogger(this).logStudentAccess(studId, ACCESS_PAUSED);
    	                    return new LoginResult(-1, rb.getString("login_is_paused"), LoginResult.ERROR);                	
                        }
                        else {
	                        ped = getPedagogyOrStrategyForStudent(getConnection(),studId, classId);
	                        woProps = new WoProps(connection);
	                        woProps.load(studId);   // get all properties for studId
	                        Timestamp lastLoginTime = DbSession.getLastLogin(connection, studId);
	                        if (lastLoginTime != null)
	                            this.timeInSession = System.currentTimeMillis() - lastLoginTime.getTime();
	                        setStudentState(woProps);   // pull out student state props from all properties
	                        studState.getSessionState().initializeState();
	
	                        instantiatePedagogicalModel(ped);
	                        pedagogicalModel.newSession(sessionId); // tells the pedagogical that its a new session so it can initialize.
	                        new TutorLogger(this).logStudentAccess(studId, LOGIN_SUCCESSFUL);
    	                	return new LoginResult(sessionId, null, LoginResult.NEW_SESSION);                        	
                        }
                    }
                } else {
                    ped = getPedagogyOrStrategyForStudent(getConnection(),studId, classId);
                    woProps = new WoProps(connection);
                    woProps.load(studId);   // get all properties for studId
                    Timestamp lastLoginTime = DbSession.getLastLogin(connection, studId);
                    if (lastLoginTime != null)
                        this.timeInSession = System.currentTimeMillis() - lastLoginTime.getTime();

                    setStudentState(woProps);   // pull out student state props from all properties
                    this.sessionId = DbSession.newSession(getConnection(), studId, sessBeginTime, false);
                    studState.getSessionState().initializeState();
                    instantiatePedagogicalModel(ped);
                    if (DbUser.isLoginPaused(connection, studId)) {
                        new TutorLogger(this).logStudentAccess(studId, ACCESS_PAUSED);
	                    return new LoginResult(-1, rb.getString("login_is_paused"), LoginResult.ERROR);                	
                    }
                    else {
                        new TutorLogger(this).logStudentAccess(studId, LOGIN_SUCCESSFUL);
	                	return new LoginResult(sessionId, null);                    	
                    } 
                }

            }
        }
        
    }

    /**
     * Only used by defunct server test code.
     *
     * @param uname
     * @param momName
     * @param password
     */
    public String serverTester_LoginStudent(String uname, String momName, String password, long clientBeginTime, String wristID) throws Exception {


        clearDemoUsers(uname);

        StringBuffer result = new StringBuffer();
        String loginCheck = null;
        int studId = -1;
        // given a password so use uname/pwd
        if (password != null) {
            studId = DbUser.getStudent(this.getConnection(), uname, password);
            int sessId = DbSession.findActiveSession(getConnection(), studId);
            if (studId == -1)
                return getLoginView(LOGIN_USER_PASS,
                        NavigationHandler.FALSE, "Invalid user name/password combination", -1, -1, null);
                // login was rejected because of other active
                // sessions and then prompt user to see if they want to log them all out automatically and relogin.
                // Give them a sessionId so that if they choose to logout the other sessions, this session will be left
                // active.
            else if (sessId != -1) {
                int newSessId = DbSession.newSession(getConnection(), studId, clientBeginTime, false);
                return getLoginView(OTHER_ACTIVE_SESSIONS, NavigationHandler.TRUE, null, newSessId, studId, null);
            } else
                loginCheck = LOGIN_USER_PASS;
        } else if (momName != null) {
            studId = DbUser.getStudentByMomsName(this.getConnection(), uname, momName);
            if (studId == -1)
                return getLoginView(LOGIN_USER_MOM,
                        NavigationHandler.FALSE, "Invalid user name/mothers name combination", -1, -1, null);
            else
                loginCheck = LOGIN_USER_MOM;
        } else {
            throw new UserException("must provide uname and password or uname and momName");
        }

        this.sessionId = DbSession.newSession(getConnection(), studId, clientBeginTime, false);
        // At login time we need a PedagogicalModel for the user (so that a learning companion can be returned to the client)
        // Only way to get it is to construct another (full) smgr using our new sessionID.   We can then
        // get its PedagogicalModel's learning companion
        SessionManager smgr = new SessionManager(this.getConnection(), this.sessionId, hostPath, contextPath).buildExistingSession();  // doing this because we need a PedagogicalModel for the user
        PedagogicalModel pm = smgr.getPedagogicalModel();
        LearningCompanion lc = pm.getLearningCompanion();  // A PM must return an LC or null (no longer in the pedagogies.xml)
//        setNumProbsSolved(studId);  // no longer need to figure this out from EpiData - its in the student state.
        return getLoginView(loginCheck, NavigationHandler.TRUE, null, this.getSessionNum(), studId, lc);
    }

    public String logoutStudent(LogoutEvent logoutEvent, String ipAddr) throws Exception {

        DbSession.inactivateSession(getConnection(), logoutEvent.getSessionId());
        // create an HttpSessionObject for this student id
        return "ack=true";
    }

    public int getStudentId() {
        return this.studId;
    }

    public String getUserName() {
        return this.user.getUname();
    }

    public int getSessionNum() throws Exception {
        if (sessionId != -1) {
            return sessionId;
        } else
            throw new UserException("Attempt to get a session failed.  Make sure you are logged in");
    }

    // call this to get around the exception throw when you know there is a session.
    public int getSessionId () {
        return sessionId;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    private void instantiatePedagogy (Pedagogy p) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, DeveloperException, InstantiationException, SQLException, IllegalAccessException {
        try {
            Class c = Class.forName(p.getPedagogicalModelClass());
            Constructor constr = c.getConstructor(SessionManager.class, Pedagogy.class);
            logger.debug("Instantiating a pedagogical model of type: " + c.getName());
            String smClassName = p.getStudentModelClass();
            this.studentModel = (StudentModel) Class.forName(smClassName).getConstructor(SessionManager.class).newInstance(this);
            if (studentModel == null) {
                throw new DeveloperException("A StudentModel object was not created by the constructor of " + p.getPedagogicalModelClass());
            }
            studentModel.init(woProps, studId, classId);
            this.pedagogicalModel = (PedagogicalModel) constr.newInstance(this, p);
        } catch (Exception e) {
            logger.fatal(this.pedagogicalModel, e);
            throw (e);
        }
    }

    private void instantiateStrategy (TutorStrategy strategy) throws NoSuchMethodException, DeveloperException, InstantiationException, SQLException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        try {

            // The strategy holds the information about the pedagogical model in the tutor strategy component.
            // Begin by getting the className of the pedagogical model.
            ClassStrategyComponent tutor_sc = strategy.getTutor_sc();
//            Class c = Class.forName(tutor_sc.getClassName()); // the pedagogical model class name
            // The pedModel class is now stored as an SC parameter on the tutor_sc rather than as a special field of it.
            Class c = Class.forName(strategy.getPedagogicalModelClass());
            Constructor constr = c.getConstructor(SessionManager.class, Pedagogy.class);
            logger.debug("Instantiating a pedagogical model of type: " + c.getName());

            String smClassName = strategy.getStudentModelClass();
            this.studentModel = (StudentModel) Class.forName(smClassName).getConstructor(SessionManager.class).newInstance(this);
            if (studentModel == null) {
                throw new DeveloperException("A StudentModel object was not created by the constructor of " + strategy.getPedagogicalModelClass());
            }
            studentModel.init(woProps, studId, classId);
            this.pedagogicalModel = (PedagogicalModel) constr.newInstance(this, strategy);
        } catch (Exception e) {
            logger.fatal(this.pedagogicalModel, e);
            throw (e);
        }
    }

    /**
     * Extract the PedagogicalModel class name from the Pedagogy object and
     * construct the actual object.   PedagogicalModel constructors are also responsible for creating
     * a StudentModel.
     */
    public void instantiatePedagogicalModel(Pedagogy p) {
        try {
            if (p instanceof TutorStrategy) {
                instantiateStrategy((TutorStrategy) p);
            }
            else {
                instantiatePedagogy(p);

            }
        } catch (Exception e) {
            logger.fatal(this.pedagogicalModel, e);
        }
    }

    // can go away when we eliminate the old event model of WO
    private StudentModel setStudentModelClassname(String smClassName) {

        StudentModel sm = null;

        try {
            Class c = Class.forName(smClassName);
            Constructor constr = c.getConstructor(Connection.class);
            sm = (StudentModel) constr.newInstance(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return sm;
    }

    // can go away when we eliminate the old event model of WO
    private LearningCompanion setLearningCompanion(String learningCompanionClassName) {

        LearningCompanion lc = null;

        try {
            Class c = Class.forName(learningCompanionClassName);
            lc = (LearningCompanion) c.newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lc;
    }

    public StudentModel getStudentModel() {
        return this.studentModel;
    }

    public void setStudentModel(StudentModel studentModel) {
        this.studentModel = studentModel;
    }

    public Connection getConnection() {
        return this.connection;
    }

    // To remove all evidence of a student in the system, delete the eventlog for his session, delete the studentproblemhistory for his session
    // delete the session
    public void removeTestSessionData() throws Exception {
        PreparedStatement ps = null;
        try {

            String q = "delete from eventLog where sessNum=?";
            ps = this.connection.prepareStatement(q);
            ps.setInt(1, this.getSessionNum());
            ps.executeUpdate();
            q = "delete from session where id=?";
            ps = this.connection.prepareStatement(q);
            ps.setInt(1, this.getSessionNum());
            ps.executeUpdate();
//            studState.clearUserProperties(this.studId);
        } finally {
            ps.close();
        }
    }

    public void inactivateTempUserSessions(int sessionId) throws SQLException {
        String q = "delete from session where id=?";
        PreparedStatement ps = this.getConnection().prepareStatement(q);
        long now = System.currentTimeMillis();
        ps.setInt(1, sessionId);
        int u = ps.executeUpdate();

    }

    /**
     * Make all sessions for a given studId inactive except for the current session
     *
     * @throws SQLException
     */
    public void inactivateUserSessions() throws Exception {
        String q = "update session set isActive=0, endTime=? where isActive = 1 and studId = ? and id != ?";
        PreparedStatement ps = this.getConnection().prepareStatement(q);
        long now = System.currentTimeMillis();
        ps.setTimestamp(1, new Timestamp(now));
        ps.setInt(2, studId);
        ps.setInt(3, this.getSessionNum());
        System.out.println(ps.toString());
        int u = ps.executeUpdate();
        System.out.println(u);
    }

    public void inactivateAllUserSessions() throws Exception {
        String q = "update session set isActive=0, endTime=? where isActive=1 and studId=?";
        PreparedStatement ps = this.getConnection().prepareStatement(q);
        long now = System.currentTimeMillis();
        ps.setTimestamp(1, new Timestamp(now));
        ps.setInt(2, this.getStudentId());
        ps.executeUpdate();
    }

    private void checkSessionTimes(Connection conn, int id) throws SQLException {
        String s = "select beginTime, endTime, lastAccessTime from session where id=?";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Timestamp bt = rs.getTimestamp(1);
            Timestamp et = rs.getTimestamp(2);
            Timestamp lt = rs.getTimestamp(3);
        }
    }

    public double getClassMasteryThreshold() throws SQLException {
        return DbClass.getClassMastery(connection, this.classId);
    }

    // This is a stub that replaces StudentProfile.getStudentSequenceGroup.
    // Currently there is no mechanism for assigning a sequence group to a student so
    // we used a fixed sequence group of zero which is what the comment in ProblemSetProblemSelector
    // stated was happening anyway.
    // Hopefully, we can get by with this.   If we need different students using different
    // problem sequences, then we've got a problem because students are simply assigned a pedagogy
    // as bunch of selectors - not a bunch of selectors + some flags like what sequenceGroup to use.
    public int getStudentSequenceGroup() {
        return 0;  //TODO write the code for this.  To change body of created methods use File | Settings | File Templates.
    }

    public PedagogicalModel getPedagogicalModel() {
        return pedagogicalModel;
    }

    public void setPedagogicalModel(PedagogicalModel pm) {
        this.pedagogicalModel = pm;
    }

    public void clearUserProperties() {
        int studId = this.getStudentId();

    }


    public long getTimeInSession() {
        return timeInSession;
    }

    public boolean isFirstLogin() throws SQLException {
        return DbSession.getStudentSessions(connection, this.getStudentId()).size() == 1;
    }

    public boolean isAssistmentsUser() {
        return assistmentsUser;
    }

    public void setAssistmentsUser(boolean assistmentsUser) {
        this.assistmentsUser = assistmentsUser;
    }
//
//    public void initializeTopicTeaching(int topicId) throws SQLException {
//        studState.setTutorEntryTime(System.currentTimeMillis());
//        studState.setCurTopic(topicId);
//        studState.setTopicNumProbsSeen(0);
//        studState.setTimeInTopic(0);
//        studState.setTutorEntryTime(System.currentTimeMillis());
//        studState.setNumProblemsThisTutorSession(0);
//        studState.setNumRealProblemsThisTutorSession(0);
//        studState.setNumProbsSinceLastIntervention(0);
//        studState.setStudentSelectedTopic(-1);
//        studState.setInReviewMode(false);
//        studState.setInChallengeMode(false);
//    }

    public String getClassTeacher() throws SQLException {
        return DbClass.getClass(getConnection(), this.getStudentClass(this.getStudentId())).getTeacherName();
    }


    public String getClient() {
        return client + ".swf";
    }

    public void setClient(String client) {
        this.client = client;
    }

    public WoProps getStudentProperties() {
        return this.woProps;
    }

    // We don't have a separate flag for this, so we make it be connected to whether they are seeing
    // controls for testing
    public boolean showTestableContent() throws SQLException {
        return DbUser.isShowTestControls(this.connection, this.studId);
    }

    public boolean showTestUserControls() throws SQLException {
        return DbUser.isShowTestControls(this.connection, this.studId);
    }

    public boolean isTestUser() throws SQLException {
        return DbUser.isTestUser(this.connection, this.studId);
    }

    public void setPedagogyId(int pedagogyId) {
        this.pedagogyId = pedagogyId;
    }

    public int getCollaboratingWith() {
        return collaboratingWith;
    }

    public void setCollaboratingWith(int collaboratingWith) {
        this.collaboratingWith = collaboratingWith;
    }

    public int getEventCounter() {
        return this.eventCounter;  //To change body of created methods use File | Settings | File Templates.
    }

    public void setEventCounter(int eventCounter) {
        this.eventCounter = eventCounter;
    }

    public ExtendedStudentState getExtendedStudentState () {
        return this.extStudState;
    }

    public boolean isSoundSync () throws SQLException {
        ClassConfig c= DbClass.getClassConfig(this.connection,this.classId) ;
        return c.isSoundSync();
    }

    // The mouseSaveInterval tells the client to wait a given number of seconds and then report mouse tracking data to the server.
    // If this number is <= 0, this won't happen.  The number comes from the classConfig of the student's class.
    public int getMouseSaveInterval() throws SQLException {
        ClassConfig c= DbClass.getClassConfig(this.connection,this.classId) ;
        return c.getMouseSaveInterval();
    }

    // Not used.
    // would be better if the SessionManager took a ClassConfig object at the time the session is started and could save some aspects
    // of the classConfig that affect the student in the sessionMgr. (e.g soundSync and mouseSaveInterval)
    public void setMouseSaveInterval (int numSeconds) {
        this.mouseSaveInterval = numSeconds;
    }
}