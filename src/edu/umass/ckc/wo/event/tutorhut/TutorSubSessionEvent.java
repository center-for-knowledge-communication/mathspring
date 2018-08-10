package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.exc.AssistmentsBadInputException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent when user clicks the "next problem" button.
 * Event action: nextProblem
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class TutorSubSessionEvent extends ActionEvent {
    // for debugging
//    public static final String FORCE_INTERVENTION = "forceIntervention";
//    public static final String DEBUG_INTERVENTION = "debugIntervention";
//    public static final String PROB_NAME = "probName";
//    public static final String PROB_ID = "probID";

    public static final String MODE = "mode";
    public static final String SHOW_TRANSITION_PAGE = "showTransitionPage";
    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";
    public static final String CLASS = "class";
    public static final String TOPIC = "topic";
    public static final String CCSS = "ccss";
    public static final String SHOW_INTRO = "showIntro";
    public static final String MAXTIME = "maxtime";
    public static final String MAXPROBS = "maxprobs";
    public static final String PROBLEM_ID = "problemId"; // db internal ID of our problem
    public static final String MASTERY_THRESHOLD = "mastery";
    public static final String IS_TEST_USER = "isTest";
    public static final String CU_ID = "cuId";  // curriculum Unit ID
    public static final String LESSON_ID = "lessonId";  // lesson ID
    public static final String ACCESS_TOKEN = "access_token";  // a token given by the calling system (e.g. MARi)




    private boolean showIntro =false;
    private String problemId = null;
    private String mode=null;
    private int topic =-1 ;
    private String[] standards;
    private boolean showTransitionPage=false;
    private String userName;
    private String userId;
    private int maxtime;
    private int maxprobs;
    private float masteryLevel;
    private boolean isTestUser;
    private int cuId;
    private int lessonId;
    private String accessToken;
    private boolean calledFromParentApp;  // set to true if this came from the parent app; false o/w such as a direct URL in browser

    public TutorSubSessionEvent(ServletParams p) throws  Exception {
        super(p);
        try {
            this.accessToken = p.getString(ACCESS_TOKEN);
            // if an accessToken is not provided it means that this event was not created from a calling app.  It came direct from a
            // HTTP GET (probably a URL location box for testing the API)
            calledFromParentApp =(this.accessToken != null);
            String intro = p.getString(SHOW_INTRO, "false");
            mode = p.getString(MODE, "Practice");  // practice is default mode if not passed.
            standards = p.getStrings(CCSS);

            if (!(mode.equals("Practice") || mode.equals("ExamplePractice") || mode.equals("Example")))
                throw new AssistmentsBadInputException(String.format("Illegal mode given %s", mode));
            topic = p.getInt(TOPIC, -1); // if a topic is not provided, we set it to -1 with the idea that a problemNumber must be provided
            userName = p.getString(USER_NAME, null);
            if (userName == null)
                userName = p.getString(USER_NAME);
            userId = p.getString(USER_ID, null);
            if (userId == null)
                userId = p.getString(USER_ID);

            showTransitionPage = p.getBoolean(SHOW_TRANSITION_PAGE, false);
            setShowIntro(Boolean.parseBoolean(intro));
            maxtime = p.getInt(MAXTIME, 10);
            maxprobs = p.getInt(MAXPROBS, 10);
            cuId = p.getInt(CU_ID,-1);
            lessonId = p.getInt(LESSON_ID,-1);
//        forceFirstProb = p.getBoolean(FORCE_FIRST_PROBLEM, false);
            // no topic provided means a problemNumber or a problemId is mandatory
            if (topic == -1 && standards == null && cuId == -1 && lessonId == -1) {
                problemId = p.getString(PROBLEM_ID, null);
                if (problemId == null)
                    throw new AssistmentsBadInputException("No topic provided.  When omitting topic, you must provide a problemId, standards, cuId");
            }
            else {
                problemId = p.getString(PROBLEM_ID, null);
            }

            masteryLevel = p.getFloat(MASTERY_THRESHOLD, 1);  // Not sure what the default should be here.
            isTestUser = p.getBoolean(IS_TEST_USER,false);  // The default if no param is included is to make it behave as a NON TEST

        } catch (Throwable thr) {
            if (thr instanceof AssistmentsBadInputException)
                throw (AssistmentsBadInputException) thr;
            else throw new AssistmentsBadInputException(thr.getMessage());

        }

    }

    public int getMaxtime() {
        return maxtime;
    }


    public int getMaxprobs() {
        return maxprobs;
    }




    public boolean isShowTransitionPage() {
        return showTransitionPage;
    }


    public String getMode() {
        return mode;
    }


    public void setShowIntro(boolean showIntro) {
        this.showIntro = showIntro;
    }

    public boolean isShowIntro() {
        return this.showIntro;
    }

    public int getTopic() {
        return topic;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




    public String getProblemId() {
        return problemId;
    }

    public String[] getStandards() {
        System.out.println(standards);
        return standards;
    }


    public float getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(float masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public int getCuId() {
        return cuId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public boolean isTestUser() {
        return isTestUser;
    }

    public void setTestUser(boolean testUser) {
        isTestUser = testUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isFromParentApp () {
        return calledFromParentApp;
    }
}
