package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;
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
public class TeachTopicEvent extends ActionEvent {
    // for debugging
//    public static final String FORCE_INTERVENTION = "forceIntervention";
//    public static final String DEBUG_INTERVENTION = "debugIntervention";
//    public static final String PROB_NAME = "probName";
//    public static final String PROB_ID = "probID";

    public static final String MODE = "mode";
    public static final String ASSIGNMENT = "assignment";
    public static final String ASSIGNMENTP = "p3";
    public static final String ASSISTMENT = "assistment";
    public static final String ASSISTMENTP = "p4";
    public static final String PROBLEM = "problem";
    public static final String PROBLEMP = "p5";
    public static final String SHOW_ASSISTMENTS_TRANSITION_PAGE = "showTransitionPage";
    public static final String USER = "user";
    public static final String USERP = "p1";
    public static final String ACCESS = "access";
    public static final String CLASS = "class";
    public static final String CLASSP = "p2";
    public static final String TOPIC = "topic";
    public static final String CCSS = "ccss";
    public static final String SHOW_INTRO = "showIntro";
    public static final String FORCE_FIRST_PROBLEM = "forceFirstProblem";
    public static final String MAXTIME = "maxtime";
    public static final String MAXPROBS = "maxprobs";
    public static final String PROBLEM_NUM = "problemNumber"; // THe 3 digit number appended to the problem name (NOT the ID)
    public static final String PROBLEM_ID = "problemId"; // db internal ID of our problem
    public static final String MASTERY_THRESHOLD = "mastery";
    public static final String IS_TEST_USER = "isTest";
    public static final String CU_ID = "cuId";  // curriculum Unit ID
    public static final String LESSON_ID = "lessonId";  // lesson ID




    private boolean showIntro =false;
    private String probName=null;
    private String problemNumber =null;
    private String problemId = null;
    private String mode=null;
    private int topic =-1 ;
    private String ccss;
    private boolean showTransitionPage=false;
    private String user;
    private String access;
    private String assignment;  // an Assistments input that must be passed back
    private String assistment;    // an Assistments input that must be passed back
    private String problem;     // an Assistments input that must be passed back
    private int maxtime;
    private int maxprobs;
    private float masteryLevel;
    private boolean forceFirstProb;
    private String assistmentsClass;
    private boolean isTestUser;
    private boolean assistmentsUser; // will set to true if valid params given indicating call is from Assistments
    private int cuId;
    private int lessonId;

    public TeachTopicEvent(ServletParams p) throws Exception {
        super(p);
        try {

            String intro = p.getString(SHOW_INTRO, "false");
            mode = p.getString(MODE, "Practice");  // practice is default mode if not passed.
            ccss = p.getString(CCSS, null);

            if (!(mode.equals("Practice") || mode.equals("ExamplePractice") || mode.equals("Example")))
                throw new AssistmentsBadInputException(String.format("Illegal mode given %s", mode));
            topic = p.getInt(TOPIC, -1); // if a topic is not provided, we set it to -1 with the idea that a problemNumber must be provided
            user = p.getString(USERP, null);
            if (user == null)
                user = p.getString(USER);
            access = p.getString(ACCESS);
            showTransitionPage = p.getBoolean(SHOW_ASSISTMENTS_TRANSITION_PAGE, false);
            setShowIntro(Boolean.parseBoolean(intro));
            maxtime = p.getInt(MAXTIME, 10);
            maxprobs = p.getInt(MAXPROBS, 10);
            cuId = p.getInt(CU_ID,-1);
            lessonId = p.getInt(LESSON_ID,-1);
//        forceFirstProb = p.getBoolean(FORCE_FIRST_PROBLEM, false);
            // no topic provided means a problemNumber or a problemId is mandatory
            if (topic == -1 && ccss == null && cuId == -1 && lessonId == -1) {
                problemId = p.getString(PROBLEM_ID, null);
                problemNumber = p.getString(PROBLEM_NUM, null);
                if (problemId == null && problemNumber == null)
                    throw new AssistmentsBadInputException("No topic provided.  When omitting topic, you must provide a problemNumber,problemId, ccss, cuId");
            }
            else {
                problemNumber = p.getString(PROBLEM_NUM, null);
                problemId = p.getString(PROBLEM_ID, null);
            }
            forceFirstProb = problemNumber != null;
            assistmentsClass = p.getString(CLASSP, null);
            if (assistmentsClass == null)
                assistmentsClass = p.getString(CLASS);
            assignment = p.getString(ASSIGNMENTP, null);
            if (assignment == null)
                assignment = p.getString(ASSIGNMENT);
            assistment = p.getString(ASSISTMENTP, null);
            if (assistment == null)
                assistment = p.getString(ASSISTMENT);
            problem = p.getString(PROBLEMP, null);
            if (problem == null)
                problem = p.getString(PROBLEM);
            masteryLevel = p.getFloat(MASTERY_THRESHOLD, 1);  // Not sure what the default should be here.
            isTestUser = p.getBoolean(IS_TEST_USER,false);  // The default if no param is included is to make it behave as a NON TEST
            assistmentsUser = assistmentsClass!=null && assignment!=null && assistment!=null && user!=null;

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


    public boolean isForceFirstProb() {
        return forceFirstProb;
    }


    public boolean isShowTransitionPage() {
        return showTransitionPage;
    }


    public String getMode() {
        return mode;
    }

    public String getProbName() {
        return probName;
    }

    public String getProblemNumber() {
        return problemNumber;
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


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAssignment() {
        return assignment;
    }


    public String getAssistment() {
        return assistment;
    }


    public String getProblem() {
        return problem;
    }

    public String getAssistmentsClass() {
        return assistmentsClass;
    }

    public String getProblemId() {
        return problemId;
    }

    public String getCcss() {
        System.out.println(ccss);
        return ccss;
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

    public boolean isAssistmentsUser() {
        return assistmentsUser;
    }
}
