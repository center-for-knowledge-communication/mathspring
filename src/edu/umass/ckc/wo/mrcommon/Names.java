package edu.umass.ckc.wo.mrcommon;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Names {

/* the following 3 are different ways to run the servlet.  They imply a
  problem selector and a hint selector. */

  public static final String HOST = "host";
  public static final String SERVLET_PORT = "servletPort";
  public static final String GUI = "gui";
  public static final String POLICY_FILE = "policyFile"; // servlet setServletInfo param name for policy file
  public static final String ML_LOG_FILE = "mlLogFile"; // servlet setServletInfo param name for mach learn log file
  public static final String PEDAGOGIES_FILE = "pedagogiesFile"; // servlet setServletInfo param name for path to pedagogies XML file

  public static final String RESULT_TIME = "time";
  public static final String RESULT_ISCORRECT = "isCorrect";
  public static final String RESULT_PROBLEMNAME = "probName";

  public static final String VITALS = "<vitals>";
  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  public static final String VITALS_END = "</vitals>";
  public static final String VITALS_FOUND = "found";
  public static final String VITALS_NOTFOUND = "notfound";
  public static final String SPATIAL_SCORES = "<spatialScores>";
  public static final String SPATIAL_SCORES_END = "</spatialScores>";
  public static final String ACTION_LOGIN = "action=login";
  public static final String ACTION_MRLOGIN = "action=mrlogin";
  public static final String ACTION_GETVITALS = "action=getVitals";
  public static final String ACTION_SETVITALS = "action=setVitals";
  public static final String ACTION_SETRESULTS = "action=setResults";
  public static final String VITALS_SATM = "satm";
  public static final String VITALS_SATV = "satv";
  public static final String VITALS_GENDER = "gender";
  public static final String RESULTS = "results";
  public static final String FNAME = "fname";
  public static final String LNAME = "lname";
  public static final String PASSWORD = "password";
  public static final String GROUP = "group";
  public static final String SCHOOL = "school";
  public static final String CLASSID = "classId";
  public static final String ADVENTURE_PROBLEM_STORED_SUCCESS= "ack=true" ;
  public static final String ADVENTURE_PROBLEM_STORED_FAILURE= "ack=false" ;
  public static final String LOGIN_SUCCESS = "<login status=\"true\"/>";
  public static final String LOGIN_FAILURE = "<login status=\"false\"/>";
  public static final String XML_HEADER = "<?xml version = \"1.0\" encoding = \"ISO-8859-1\" ?>";
    public static final String PROB_PLAYER_PATH = "probplayerPath";
    public static final String PROB_PREVIEWER_PATH = "probPreviewerPath";
    public static final String USE_HYBRID_TUTOR = "useHybridTutor";
    public static final String USE_EMOTE_SERVLET = "useEmoteServlet";
    public static final String USE_LEARNING_COMPANIONS= "useLearningCompanions";
    public static final String VIDEO_URI = "videoURI";
    public static final String ASSISTMENTS_LOGBACK_URL = "assistmentsLogBackURL";
    public static String EMOTE_SERVLET_URI = "emoteServletURI";
    public static String FORMALITY_SERVLET_URI = "formalityServletURI";
    public static String ERROR_SMTP_SERVER = "error.smtpserver";
    public static String EMAIL_LOG_FILENAME = "emailLogFilename";
    public static String FLASH_CLIENT_PATH = "flashClientPath";
    public static String WEB_CONTENT_PATH = "webContentPath";  // context param in web.xml
    public static String DEV_WEB_CONTENT_PATH = "devWebContentPath";  // context param in web.xml
    public static String HTML5_PROBLEMS = "html5Probs";  // context param in web.xml.  The path under WEB_CONTENT_PATH where HTML5 content is
    public static String IS_DEVELOPMENT_ENVIRONMENT = "isDevelopmentEnv";  // context param in web.xml.  true/false
    public static String CLIENT_HOST = "clientHost";
    public static final String EXTERNAL_ACTIVITY_PERCENTAGE = "externalActivityPercentage";

}