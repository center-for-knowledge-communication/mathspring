package edu.umass.ckc.wo.tutor;

import edu.umass.ckc.wo.admin.LessonMap;
import edu.umass.ckc.wo.admin.LoginMap;
import edu.umass.ckc.wo.tutormeta.*;
import edu.umass.ckc.wo.admin.PedMap;
import edu.umass.ckc.wo.tutor.probSel.AdventureProblemSelector;
import edu.umass.ckc.wo.tutor.probSel.FixedSequencePrePostProblemSelectorImpl;
import edu.umass.ckc.wo.tutor.probSel.PrePostProblemSelectorImpl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 * Frank 5-24-2020 issue #130 change mailServer="mailsrv.cs.umass.edu"
 */

public class Settings {


    // These are the default selectors.  They will get overwritten if the servlet is called with
  // an initialization-param for a mode. (see TutorBrainHandler.setMode)

//  public static ProblemSelector mlProblemSelector = new MLProblemSelector();

  public static final int duplicateRowError = 2627;
  public static final int keyConstraintViolation = 1062;
//  public static InterventionSelector bayesianInterventionSelector= new BayesianMotivationalSelector() ;
    

//  public static InterventionSelector engagementTimeAdaptiveInterventionSelector = new EngagementTimeAdaptiveInterventionSelector() ;
//  public static InterventionSelector engagementTimeRandomInterventionSelector = new EngagementTimeRandomInterventionSelector() ;
//  public static InterventionSelector fixedTimeRandomInterventionSelector = new FixedTimeRandomIS() ;
//  public static InterventionSelector immFeedbackEngTimeAdaptiveInterventionSelector = new ImmFeedbackEngTimeAdaptiveIS() ;
    // only used by the test module.  These are subclasses of the random and prepost that behave in a non-random way.
//  public static ProblemSelector randomProblemSelectorTester= new FixedSequenceSATProblemSelector() ;
  public static final boolean debugAllClasses = false ;
    public static final long sessionDemonCleanupInterval = 10 * 60 * 1000;  // cleanup idle sessions every 10 minutes

//  public static InterventionSelector bayesianInterventionSelectorTester= new BayesianMotivationalSelector() ;
//  public static ProblemSelector mlProblemSelectorTester = new MLProblemSelector();
    public static final int sessionIdleTimeout = 18 * 60 * 60 * 1000; // session becomes idle after 18 hours of non-use
    public static final int session4MonthsOldTimeout = 4 * 30 * 24 * 60 * 60 * 1000; // session becomes idle after 18 hours of non-use
  public static AdventureProblemSelector adventureProblemSelector_= new AdventureProblemSelector();
  public static AdventureProblemSelector userAdventureProblemSelector_= adventureProblemSelector_;  // only 1 choice so everyone gets it
  public static PrePostProblemSelector prepostProblemSelectorTester= new FixedSequencePrePostProblemSelectorImpl() ;
  public static PrePostProblemSelector prepostProblemSelector = new PrePostProblemSelectorImpl();
  public static String dbhost = "localhost";
  public static boolean isDevelopmentEnv = false; // read from web.xml to determine if html5 comes from tomcat or apache
  public static String webContentPath = "should be URI of path to mathspring under apache webroot";  // has trailing slash: root of apache content read from web.xml as context param webContentPath
  public static String html5Probs = "partial path to the html5 root folder under webContentPath"; // has trailing slash: location of html5 problems under webContentPath read as context param from web.xml as html5Probs
  public static String html5ProblemURI = "URI to html5 problem root dir"; // concat of above two
    // "cadmium.cs.umass.edu";
  public static String probplayerPath; // read from web.xml file
  public static String probPreviewerPath; // read from web.xml file
  public static String videoURI = "http://chinacat.cs.umass.edu/wayang/video/";
  public static String emoteServletURI = "http://localhost:8082/emote/EmoteServlet";
  public static String formalityServletURI =null; // full URL to the Formality servlet
  public static  String host = "should be set to the name of the host (e.g. localhost or rose.cs.umass.edu)";
  public static  String port = "should be set to the name of the port only when its not 80";
  public static String gui = "old"; // sets to either huy or old to control which GUI is displayed
  public static String tomcatDatasourceURL;
//    public static final int sessionIdleTimeout = 30 * 1000; // every 24 hours
    public static String policyFile = "";
    public static String mlLogFile = "";

    public static PedMap pedagogyGroups; // maps pedagogy id to Pedagogy objects
    public static LessonMap lessonMap; // maps lesson name to LessonXML objects
    public static LoginMap loginMap; // maps login name to LoginXML objects

    public static String FORMALITY_URL_FIRST_PART = "http://cadmium.cs.umass.edu/formality/FormalityServlet?fxn=wayang&mode=viewq&qID=" ;
    public static String FORMALITY_URL_LAST_PART = "&un=1864&wayangStudId=11501&wayangSessId=35151&elapsedTime=10000" ;

    public static String PROB_PLAYER_REQUEST_PATH = "/wayang2/flash/Problems/probplayer.swf";
    public static String flashClientPath;   // read from web.xml WoTutorServlet param.   Must be full URL to Flash client folder (no trailing /)

    public static  boolean useLearningCompanions = true;
    public static String prePostProblemURI= "";
    public static String techEmail="marshall@cs.umass.edu";
    public static String mailServer="mailsrv.cs.umass.edu";
    public static boolean useAdminServletSession = true; // controls whether servlet sessions are used in the WoAdminServlet
                                                // They should be on in normal use.   For debugging the servlet I turn off
    public static int adminServletSessionTimeoutSeconds= 60 * 60; // default is 60 min.

    public static File emailLogFile;

    public static String preSurvey="";
    public static String postSurvey="";

    public static boolean useHybridTutor = true;
    public static String flashClientURI;  // not needed


    public static double externalActivityPercentage=5.0; // controls the intervention selector that picks external acts
    public static String problemContentPath;
    public static boolean loggingBackToAssistments=true;
    public static boolean usingAssistments=false;

    public static boolean sendClassStatusEmail = false;
    public static boolean sendStudentStatusEmail = false;

    public static int interleavedTopicID;  // id of a problemGroup row in the db with the description Interleaved Problem Set.
    public static boolean useStrategyCaching = false;

    public static String surveyDir;
    public static String surveyURI;

    public static void setSurveys (Connection conn, String preSurvey, String postSurvey) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "update globalsettings set preSurvey=?, postSurvey=? ";
            stmt = conn.prepareStatement(q);
            stmt.setString(1,preSurvey);
            stmt.setString(2, postSurvey);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static void getSurveys (Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select presurvey, postsurvey from globalsettings";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String s = rs.getString(1);
                if (rs.wasNull())
                    Settings.preSurvey="";
                else Settings.preSurvey=s;
                s = rs.getString(2);
                if (rs.wasNull())
                    Settings.postSurvey="";
                else Settings.postSurvey=s;
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static String problemPreviewerPath () {
        return probplayerPath.replace("probplayer","problem_checker");
    }

    public static boolean useNewGUI () {
        return Settings.gui.equals("huy");
    }

    public static void setGui (String s) {
        if (s != null) {
            Settings.gui = s;
        }
    }
}