package edu.umass.ckc.wo.enumx;

/**
 * these are the actions that can be initiated from the GUI
 */

public class Actions {
    // these two events allow loggin in as systemTest and then at the end of the session making a call to remove
    // all the data inserted by that session.
  public static final String removeSystemTestSessionData = "removeSystemTestSessionData";
  public static final String systemTestLogin = "systemTestLogin";

  public static final String login = "login";
  public static final String guestLogin = "guestLogin";
  public static final String logout = "logout";
  public static final String killOtherSessions = "killOtherSessions";
  public static final String killAllSessions = "killAllSessions";
  public static final String getActivity = "getActivity";
  public static final String endActivity= "endActivity";
  public static final String adventureProblemSolved = "adventureProblemSolved";
  public static final String replay = "replay";
  public static final String getClasses = "getClasses";
  public static final String navigation = "navigation";
  public static final String topicDetail = "TopicDetail";
  // these actions are used for the Transactions logged to the db
  public static final String beginProblem = "beginProblem";
  public static final String endProblem = "endProblem";
  public static final String hint = "hint";
  public static final String hintAccepted = "hintAccepted";
  public static final String hintRejected = "hintRejected";
  public static final String attempt = "attempt";
  public static final String setMFRResult = "setMFRResult";
  public static final String setMRResult = "setMRResult";
  public static final String setPreResult = "setPreResult";
  public static final String setPostResult = "setPostResult";
  public static final String setStudentPeers = "SetStudentPeers";
  public static final String provideName = "ProvideName";

  // these actions can be intiated from the Mental rotation application
  public static final String mrlogin = "mrlogin";
  public static final String getVitals = "getVitals";
  public static final String setVitals = "setVitals";
  public static final String setResults = "setResults";
  public static final String changeGroup = "changeGroup" ;

  // actions for class admin
  public static final String createUser2 = "UserRegistrationAuthenticationInfo";
  public static final String createUser3 = "UserRegistrationClassSelection";
  public static final String createUser4 = "UserRegistrationMoreInfo";
  public static final String createClass = "createClass";
  public static final String getReport = "getReport";


    public static final String saveComment = "SaveComment";
    public static final String flashDebugLogin = "FlashDebugLogin";
    public static final String dbTest = "dbTest";
}
