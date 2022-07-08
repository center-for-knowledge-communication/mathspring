package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicState extends State {


    public static final String TOPIC_PROBLEMS_SOLVED = "st.problemsSolvedInTopic";
    public static final String BEGINNING_OF_TOPIC = "BeginningOfTopic";
    public static final String IN_TOPIC = "InTopic";
    public static final String END_OF_TOPIC = "EndOfTopic";
    private static final String CUR_PROBLEM = "st.curProblem";
    private static final String LAST_PROBLEM = "st.lastProblem";
    private static final String NEXT_PROBLEM = "st.nextProblem";
    private static final String CUR_PROBLEM_MODE = "st.curProblemMode";
    private static final String NEXT_PROBLEM_MODE = "st.nextProblemMode";
    private static final String CUR_PROB_TYPE= "st.curProbType";
    private static final String TOPIC_INTRO_SHOWN = "st.isTopicIntroShown";
    private static final String PENULTIMATE_PROBLEM_SOLVED_WELL = "st.penultimateProblemSolvedWell";
    private static final String SECOND_EASIEST_PROBLEM_FAILED_SOLVE = "st.secondEasiestProblemFailedSolve";
    private static final String PENULTIMATE_PROBLEM_SHOWN = "st.penultimateProblemShown";
    private static final String SECOND_EASIEST_PROBLEM_SHOWN = "st.secondEasiestProblemShown";
    private static final String HARDEST_PROBLEM_SHOWN = "st.hardestProblemShown";
    private static final String EASIEST_PROBLEM_SHOWN = "st.easiesttProblemShown";
    private static final String HARDEST_PROBLEM_SOLVED_WELL= "st.hardestProblemSolvedWell";
    private static final String EASIEST_PROBLEM_SOLVED_FAILED= "st.easiestProblemFailedSolve";
    private static final String PROBLEM_SELECTION_CRITERION= "st.probSelectionCriterion";
    private static final String LAST_INTERVENTION= "st.lastIntervention";
    private static final String LAST_ANSWER= "st.lastAnswer";
    private static final String LAST_PREVIEW_DIFF= "st.lastPreviewDiff";
    private static final String MIN_PREVIEW_DIFF= "st.minPreviewDiff";
    private static final String MAX_PREVIEW_DIFF= "st.maxPreviewDiff";
    private static final String LAST_PROBLEM_MODE = "st.lastProblemMode";   //Example, Intro or Practice mode of last problem
    private static final String IN_BTW_PROB_INTERVENTION = "st.inBTWProbIntervention";
    private static final String TOPIC_NUM_PROBS_SEEN = "st.topicNumProbsSeen";
    private static final String TOPIC_NUM_PRACTICE_PROBS_SEEN = "st.topicNumPracticeProbsSeen";
    private static final String TIME_IN_TOPIC = "st.TIME_IN_TOPIC";
    private static final String CONTENT_FAILURE_COUNTER = "st.CONTENT_FAILURE_COUNTER";
    private static final String CLIP_NAMES = "st.clipNames";
    private static final String CLIP_COUNTERS = "st.clipCounters";
    private static final String STUDENT_SELECTED_TOPIC = "st.studentSelectedTopic";
    private static final String SIDELINED_TOPIC = "st.sidelinedTopic";
    private static final String REVIEW_MODE = "st.inReviewMode";
    private static final String CHALLENGE_MODE = "st.inChallengeMode";
    private static final String TEACH_TOPIC_MODE = "st.teachTopicMode";
    private static final String TOPIC_SWITCH = "st.topicSwitch";
    private static final String CONTENT_FAILURE_TOPIC_SWITCH = "st.contentFailureTopicSwitch";
    private static final String TOPIC_INTROS_SEEN = "st.topicIntrosSeen";
    private static final String TOPIC_HAS_EASIER_PROBLEM = "st.topicHasEasierProblem";
    private static final String TOPIC_HAS_HARDER_PROBLEM = "st.topicHasHarderProblem";
    private static final String EXAMPLE_SHOWN = "st.exampleShown";
    private static final String CUR_PROB_INDEX_IN_TOPIC = "st.curProbIndexInTopic";
    private static final String CUR_PROB_BROKEN = "st.curProbBroken";
    private static final String TOPIC_INTERNAL_STATE = "st.topicInternalState"; // one of Begin, In, End
    private static final String REVIEW_TOPICS = "st.reviewTopics"; //
    private static String[] ALL_PROPS = new String[] { CUR_PROBLEM , LAST_PROBLEM , NEXT_PROBLEM , CUR_PROBLEM_MODE , NEXT_PROBLEM_MODE, LAST_PROBLEM_MODE ,
            CUR_PROB_TYPE, TOPIC_INTRO_SHOWN , PENULTIMATE_PROBLEM_SOLVED_WELL ,
            SECOND_EASIEST_PROBLEM_FAILED_SOLVE , PENULTIMATE_PROBLEM_SHOWN ,
            SECOND_EASIEST_PROBLEM_SHOWN , HARDEST_PROBLEM_SHOWN , EASIEST_PROBLEM_SHOWN ,
            HARDEST_PROBLEM_SOLVED_WELL, EASIEST_PROBLEM_SOLVED_FAILED,
            PROBLEM_SELECTION_CRITERION, LAST_INTERVENTION, LAST_ANSWER, LAST_PREVIEW_DIFF,
            IN_BTW_PROB_INTERVENTION , TOPIC_NUM_PROBS_SEEN ,
            TOPIC_NUM_PRACTICE_PROBS_SEEN , TIME_IN_TOPIC , CONTENT_FAILURE_COUNTER , CLIP_NAMES ,
            CLIP_COUNTERS , STUDENT_SELECTED_TOPIC , SIDELINED_TOPIC , REVIEW_MODE ,EXAMPLE_SHOWN,
            CHALLENGE_MODE , TEACH_TOPIC_MODE
             , TOPIC_SWITCH , CONTENT_FAILURE_TOPIC_SWITCH,TOPIC_HAS_EASIER_PROBLEM,TOPIC_HAS_HARDER_PROBLEM,
            CUR_PROB_INDEX_IN_TOPIC, TOPIC_INTERNAL_STATE, TOPIC_PROBLEMS_SOLVED, REVIEW_TOPICS, CUR_PROB_BROKEN} ;
    private int curProblem;
    private int lastProblem;
    private int nextProblem;
    private String curProblemMode;
    private String nextProblemMode;
    private String curProbType;
    private boolean topicIntroSeen;
    private boolean penultimateProblemSolvedWell;
    private boolean secondEasiestProblemFailedSolve=false;
    private boolean secondHardestShown;
    private boolean hardestProblemSolvedWell=false;
    private boolean easiestProblemFailedSolve=false;
    private boolean hardestShown=false;
    private boolean easiestShown=false;
    private boolean secondEasiestShown;
    private List<String> problemSelectionCriterion;
    private String lastIntervention;
    private String lastAnswer;
    private String lastPreviewDiff;
    private String minPreviewDiff;
    private String maxPreviewDiff;
    private long lastInterventionTime;
    private String lastProblemMode;
    private boolean inBtwProbIntervention;
    private int topicNumProbsSeen;
    private int topicNumPracticeProbsSeen;
    private long timeInTopic;
    private int contentFailureCounter;
    // parallel lists that will keep a counter for each movieclip name
    private List<String> clipNames;
    private List<String> clipCounters;
    private int studentSelectedTopic;
    private int sidelinedTopic;
    private boolean inReviewMode;
    private boolean inChallengeMode;
    private boolean teachTopicMode;
    private boolean topicSwitch;
    private boolean contentFailureTopicSwitch;
    private boolean curTopicHasEasierProblem;
    private boolean curTopicHasHarderProblem;
    private boolean exampleShown;
    private int curProblemIndexInTopic;
    private String internalState;
    private int topicProblemsSolved;
    private List<String> reviewTopics;
    private boolean curProblemBroken;


    public TopicState(Connection conn) {
        this.conn = conn;
    }

    public static void clearState (Connection conn, int objid) throws SQLException {
        for (String prop : ALL_PROPS)
            clearProp(conn,objid,prop)  ;
    }

    public static String getTopicIntroShown() {
        return TOPIC_INTRO_SHOWN;
    }

    public void extractProps(WoProps props) throws SQLException {
        Map m = props.getMap();
        this.curProblem = mapGetPropInt(m, CUR_PROBLEM, -1);
        this.lastProblem = mapGetPropInt(m, LAST_PROBLEM, -1);
        this.nextProblem = mapGetPropInt(m, NEXT_PROBLEM, -1);
        this.curProblemMode = mapGetPropString(m, CUR_PROBLEM_MODE, null);
        this.nextProblemMode = mapGetPropString(m, NEXT_PROBLEM_MODE, null);
        this.lastProblemMode = mapGetPropString(m, LAST_PROBLEM_MODE, null);
        this.curProbType = mapGetPropString(m, CUR_PROB_TYPE, null);
        topicIntroSeen =    mapGetPropBoolean(m, TOPIC_INTRO_SHOWN, false);
        penultimateProblemSolvedWell = mapGetPropBoolean(m, PENULTIMATE_PROBLEM_SOLVED_WELL, false);
        secondEasiestProblemFailedSolve = mapGetPropBoolean(m,SECOND_EASIEST_PROBLEM_FAILED_SOLVE,false);
        secondHardestShown = mapGetPropBoolean(m, PENULTIMATE_PROBLEM_SHOWN, false);
        hardestProblemSolvedWell = mapGetPropBoolean(m, HARDEST_PROBLEM_SOLVED_WELL, false);
        easiestProblemFailedSolve = mapGetPropBoolean(m, EASIEST_PROBLEM_SOLVED_FAILED, false);
        hardestShown = mapGetPropBoolean(m, HARDEST_PROBLEM_SHOWN, false);
        easiestShown = mapGetPropBoolean(m, EASIEST_PROBLEM_SHOWN, false);
        secondEasiestShown = mapGetPropBoolean(m, SECOND_EASIEST_PROBLEM_SHOWN, false);
        problemSelectionCriterion = mapGetPropList(m, PROBLEM_SELECTION_CRITERION);
        lastIntervention = mapGetPropString(m, LAST_INTERVENTION, null);
        lastAnswer = mapGetPropString(m, LAST_ANSWER, null);
        lastPreviewDiff = mapGetPropString(m, LAST_PREVIEW_DIFF, null);
        minPreviewDiff = mapGetPropString(m, MIN_PREVIEW_DIFF, null);
        maxPreviewDiff = mapGetPropString(m, MAX_PREVIEW_DIFF, null);
        inBtwProbIntervention = mapGetPropBoolean(m, IN_BTW_PROB_INTERVENTION, false);
        topicNumProbsSeen = mapGetPropInt(m, TOPIC_NUM_PROBS_SEEN, 0);
        topicNumPracticeProbsSeen = mapGetPropInt(m, TOPIC_NUM_PRACTICE_PROBS_SEEN, 0);
        timeInTopic = mapGetPropLong(m, TIME_IN_TOPIC, 0);
        contentFailureCounter = mapGetPropInt(m, CONTENT_FAILURE_COUNTER, 0);
        clipNames = mapGetPropList(m, CLIP_NAMES);
        clipCounters = mapGetPropList(m, CLIP_COUNTERS);
        studentSelectedTopic = mapGetPropInt(m, STUDENT_SELECTED_TOPIC, -1);
        sidelinedTopic = mapGetPropInt(m,SIDELINED_TOPIC,-1);
        inReviewMode = mapGetPropBoolean(m, REVIEW_MODE, false);
        inChallengeMode = mapGetPropBoolean(m, CHALLENGE_MODE, false);
        teachTopicMode = mapGetPropBoolean(m, TEACH_TOPIC_MODE, false);
        topicSwitch = mapGetPropBoolean(m, TOPIC_SWITCH, false);
        contentFailureTopicSwitch = mapGetPropBoolean(m, CONTENT_FAILURE_TOPIC_SWITCH, false);
        curTopicHasEasierProblem =  mapGetPropBoolean(m, TOPIC_HAS_EASIER_PROBLEM,true);
        curTopicHasHarderProblem =  mapGetPropBoolean(m,TOPIC_HAS_HARDER_PROBLEM,true);
        this.exampleShown = mapGetPropBoolean(m, EXAMPLE_SHOWN, false);
        this.curProblemIndexInTopic = mapGetPropInt(m, CUR_PROB_INDEX_IN_TOPIC, -1);
        this.internalState = mapGetPropString(m,TOPIC_INTERNAL_STATE,TopicState.BEGINNING_OF_TOPIC);
        this.topicProblemsSolved = mapGetPropInt(m,TOPIC_PROBLEMS_SOLVED,0);
        this.reviewTopics = mapGetPropList(m,REVIEW_TOPICS);
        this.curProblemBroken = mapGetPropBoolean(m, CUR_PROB_BROKEN, false);

    }

    public void initializeState () throws SQLException {
        setCurProblem(-1);
        setLastProblem(-1);
        setNextProblem(-1);
        setCurProblemMode(null);
        setNextProblemMode(null);
        setCurProbType(null);
        setTopicIntroShown(false);
        setSecondHardestSolvedWell(false);
        setSecondHardestSolvedWell(false);
        setSecondHardestShown(false);
        setHardestSolvedWell(false);
        setEasiestProblemFailedSolve(false);
        setHardestShown(false);
        setEasiestShown(false);
        setSecondEasiestShown(false);
        clearProblemSelectionCriterion();
        setLastIntervention(null);
        setLastAnswer(null);
        setLastPreviewDiff("0.715~SAME");
        setMinPreviewDiff("0.6");
        setMaxPreviewDiff("0.75");
        setLastProblemMode(null);
        setInBtwProbIntervention(false);
        setTopicNumProbsSeen(0);
        setTopicNumPracticeProbsSeen(0);
        setTimeInTopic(0);
        setContentFailureCounter(0);
        setStudentSelectedTopic(-1);
        setSidelinedTopic(-1);
        setInReviewMode(false);
        setInChallengeMode(false);
        setTeachTopicMode(false);
        setTopicSwitch(false);
        setContentFailureTopicSwitch(false);
        // The assumption is that a new topic will have both easier/harder flags true
        // Must make sure that list of problems in topic is >= 3 for this to hold.   If not,  TopicSelector must make adjustment to these flags.
        setCurTopicHasEasierProblem(true);
        setCurTopicHasHarderProblem(true);
        setCurProblemIndexInTopic(-1);
        setIsExampleShown(false);
        // each time we initialize the TopicState we set it back to a BeginningOfTopic state.
        setInternalState(TopicState.BEGINNING_OF_TOPIC);
        setTopicProblemsSolved(0);
        setCurProblemBroken(false);
    }

    public void cleanupState () throws SQLException {
        clearReviewTopics();
    }

    public int getCurProblem() {
        return curProblem;
    }

    public void setCurProblem(int curProblem) throws SQLException {
        this.curProblem = curProblem;
        setProp(this.objid, CUR_PROBLEM, curProblem);
    }

    public int getLastProblem () {
        return this.lastProblem;
    }

    public void setLastProblem (int lastProblem) throws SQLException {
        this.lastProblem = lastProblem;
        setProp(this.objid, LAST_PROBLEM, this.lastProblem);
    }

    public int getNextProblem () {
        return this.nextProblem;
    }

    public void setNextProblem (int nextProblem) throws SQLException {
        this.nextProblem = nextProblem;
        setProp(this.objid, NEXT_PROBLEM, this.nextProblem);
    }

    public String getCurProblemMode() {
        return curProblemMode;
    }

    public void setCurProblemMode(String curProblemMode) throws SQLException {
        this.curProblemMode = curProblemMode;
        setProp(this.objid, CUR_PROBLEM_MODE, curProblemMode);
    }

    public String getCurProbType() {
        return curProbType;
    }

    public void setCurProbType(String type) throws SQLException {
        this.curProbType = type;
        setProp(this.objid, CUR_PROB_TYPE, type);
    }

    public boolean isTopicIntroShown() {
        return this.topicIntroSeen;
    }

    public void setTopicIntroShown(boolean b) throws SQLException {
        this.topicIntroSeen = b;
        setProp(this.objid,TOPIC_INTRO_SHOWN,b);
    }

    public boolean isSecondHardestSolvedWell() {
        return this.penultimateProblemSolvedWell;

    }

    public void setSecondHardestSolvedWell(boolean b) throws SQLException {
        this.penultimateProblemSolvedWell = b;
        setProp(this.objid,PENULTIMATE_PROBLEM_SOLVED_WELL,b);
    }

    public boolean isSecondEasiestProblemFailedSolve() {
        return secondEasiestProblemFailedSolve;
    }

    public void setSecondEasiestProblemFailedSolve(boolean x) throws SQLException {
        this.secondEasiestProblemFailedSolve = x;
        setProp(this.objid,SECOND_EASIEST_PROBLEM_FAILED_SOLVE,x);
    }

    public boolean isSecondHardestShown() {
        return secondHardestShown;
    }

    public void setSecondHardestShown(boolean b) throws SQLException {
        secondHardestShown=b;
        setProp(this.objid,PENULTIMATE_PROBLEM_SHOWN,b);
    }

    public boolean isHardestSolvedWell() {
        return this.hardestProblemSolvedWell;
    }

    public void setHardestSolvedWell(boolean b) throws SQLException {
        this.hardestProblemSolvedWell = b;
        setProp(this.objid,HARDEST_PROBLEM_SOLVED_WELL,b);
    }

    public boolean isHardestShown() {
        return hardestShown;
    }

    public void setHardestShown(boolean b) throws SQLException {
        hardestShown=b;
        setProp(this.objid,HARDEST_PROBLEM_SHOWN,b);

    }

    public boolean isEasiestShown() {
        return easiestShown;
    }

    public void setEasiestShown(boolean x) throws SQLException {
        this.easiestShown = x;
        setProp(this.objid,EASIEST_PROBLEM_SHOWN,x);
    }

    public boolean isHardestProblemSolvedWell() {
        return hardestProblemSolvedWell;
    }

    public void setHardestProblemSolvedWell(boolean hardestProblemSolvedWell) throws SQLException {
        this.hardestProblemSolvedWell = hardestProblemSolvedWell;
        setProp(this.objid,HARDEST_PROBLEM_SOLVED_WELL,hardestProblemSolvedWell);
    }

    public boolean isEasiestProblemFailedSolve() {
        return easiestProblemFailedSolve;
    }

    public void setEasiestProblemFailedSolve(boolean easiestProblemFailedSolve) throws SQLException {
        this.easiestProblemFailedSolve = easiestProblemFailedSolve;
        setProp(this.objid,EASIEST_PROBLEM_SOLVED_FAILED,easiestProblemFailedSolve);
    }

    public boolean isSecondEasiestShown() {
        return secondEasiestShown;
    }

    public void setSecondEasiestShown(boolean x) throws SQLException {
        this.secondEasiestShown = x;
        setProp(this.objid,SECOND_EASIEST_PROBLEM_SHOWN,x);
    }

    /**
     * Each time a problem is selected by the AdaptiveProblemGroup selector this records if the selector wanted a harder,easier, or
     * same difficulty problem.   Puts them all into a list stored in the state
     * @param criterion
     */
    public void addProblemSelectionCriterion(String criterion) throws SQLException {
        this.problemSelectionCriterion.add(criterion);
        addProp(this.objid, PROBLEM_SELECTION_CRITERION,criterion);
    }

    public void clearProblemSelectionCriterion () throws SQLException {
        this.problemSelectionCriterion = null;
        clearProp(this.objid,PROBLEM_SELECTION_CRITERION);
    }

    public String getLastIntervention() {
        return lastIntervention;
    }

    public void setLastIntervention(String lastIntervention) throws SQLException {
        this.lastIntervention = lastIntervention;
        setProp(this.objid,LAST_INTERVENTION,this.lastIntervention);
    }

    public void setLastAnswer(String lastAnswer) throws SQLException {
        this.lastAnswer = lastAnswer;
        setProp(this.objid,LAST_ANSWER,this.lastAnswer);
    }

    public void setLastPreviewDiff(String lastPreviewDiff) throws SQLException {
        this.lastPreviewDiff = lastPreviewDiff;
        setProp(this.objid,LAST_PREVIEW_DIFF,this.lastPreviewDiff);
    }

    public String getLastPreviewDiff() {
        return this.lastPreviewDiff;
    }


    public void setMinPreviewDiff(String minPreviewDiff) throws SQLException {
        this.minPreviewDiff = minPreviewDiff;
        setProp(this.objid,MIN_PREVIEW_DIFF,this.minPreviewDiff);
    }

    public String getMinPreviewDiff() {
        return this.minPreviewDiff;
    }


    public void setMaxPreviewDiff(String maxPreviewDiff) throws SQLException {
        this.maxPreviewDiff = maxPreviewDiff;
        setProp(this.objid,MAX_PREVIEW_DIFF,this.maxPreviewDiff);
    }

    public String getMaxPreviewDiff() {
        return this.maxPreviewDiff;
    }

    public String getLastProblemMode() {
        return lastProblemMode;
    }

    public void setLastProblemMode(String lastProblemMode) throws SQLException {
        this.lastProblemMode = lastProblemMode;
        setProp(this.objid, LAST_PROBLEM_MODE, lastProblemMode);
    }

    public boolean getInBtwProbIntervention () {
        return this.inBtwProbIntervention;
    }

    public void setInBtwProbIntervention (boolean b) throws SQLException {
        this.inBtwProbIntervention = b;
        setProp(this.objid,IN_BTW_PROB_INTERVENTION,b);
    }

    public int getTopicNumProbsSeen () {
        return this.topicNumProbsSeen;
    }

    public void setTopicNumProbsSeen(int n) throws SQLException {
        this.topicNumProbsSeen = n;
        setProp(this.objid,TOPIC_NUM_PROBS_SEEN,n);
    }

    public int getTopicNumPracticeProbsSeen () {
        return this.topicNumPracticeProbsSeen;
    }

    public void setTopicNumPracticeProbsSeen(int n) throws SQLException {
        this.topicNumPracticeProbsSeen = n;
        setProp(this.objid,TOPIC_NUM_PRACTICE_PROBS_SEEN,n);
    }

    public long getTimeInTopic() {
        return timeInTopic;
    }

    public void setTimeInTopic(long t) throws SQLException {
        this.timeInTopic = t;
        setProp(this.objid,TIME_IN_TOPIC,t);
    }

    public int getContentFailureCounter() {
        return contentFailureCounter;
    }

    public void setContentFailureCounter(int contentFailureCounter) throws SQLException {
        this.contentFailureCounter = contentFailureCounter;
        setProp(this.objid,CONTENT_FAILURE_COUNTER,contentFailureCounter);
    }

    public int getStudentSelectedTopic() {
        return studentSelectedTopic;
    }

    public void setStudentSelectedTopic(int studentSelectedTopic) throws SQLException {
        this.studentSelectedTopic = studentSelectedTopic;
        setProp(this.objid,STUDENT_SELECTED_TOPIC,studentSelectedTopic);
    }

    public int getSidelinedTopic() {
        return sidelinedTopic;
    }

    public void setSidelinedTopic(int topicId) throws SQLException {
        this.sidelinedTopic = topicId;
        setProp(this.objid,SIDELINED_TOPIC,topicId);
    }

    public void setInPracticeMode(boolean inPracticeMode) throws SQLException {
        this.setInReviewMode(false);
        this.setInChallengeMode(false);
    }

    public void setTeachTopicMode(boolean b) throws SQLException {
        this.teachTopicMode= b;
        setProp(this.objid,TEACH_TOPIC_MODE,b);
    }

    public boolean inTeachTopicMode () {
        return this.teachTopicMode;
    }

    public boolean isInReviewMode() {
        return inReviewMode;
    }

    public void setInReviewMode(boolean inReviewMode) throws SQLException {
        this.inReviewMode = inReviewMode;
        setProp(this.objid,REVIEW_MODE,inReviewMode);
        this.inChallengeMode = false;
        setProp(this.objid,CHALLENGE_MODE,inChallengeMode);
    }

    public boolean isInChallengeMode() {
        return inChallengeMode;
    }

    public void setInChallengeMode(boolean inChallengeMode) throws SQLException {
        this.inChallengeMode = inChallengeMode;
        setProp(this.objid,CHALLENGE_MODE,inChallengeMode);
        this.inReviewMode = false;
        setProp(this.objid,REVIEW_MODE,inReviewMode);
    }

    public boolean isTopicSwitch() {
        return topicSwitch;
    }

    public void setTopicSwitch(boolean topicSwitch) throws SQLException {
        this.topicSwitch = topicSwitch;
        setProp(this.objid,TOPIC_SWITCH,topicSwitch);
    }

    public boolean isContentFailureTopicSwitch() {
        return contentFailureTopicSwitch;
    }

    public void setContentFailureTopicSwitch(boolean contentFailureTopicSwitch) throws SQLException {
        this.contentFailureTopicSwitch = contentFailureTopicSwitch;
        setProp(this.objid,CONTENT_FAILURE_TOPIC_SWITCH,contentFailureTopicSwitch);
    }

    public void setCurTopicHasEasierProblem(boolean curTopicHasEasierProblem) throws SQLException {
        this.curTopicHasEasierProblem = curTopicHasEasierProblem;
        setProp(objid,TOPIC_HAS_EASIER_PROBLEM,curTopicHasEasierProblem);
    }

    public void setCurTopicHasHarderProblem(boolean curTopicHasHarderProblem) throws SQLException {
        this.curTopicHasHarderProblem = curTopicHasHarderProblem;
        setProp(objid,TOPIC_HAS_HARDER_PROBLEM,curTopicHasHarderProblem);

    }

    public boolean curTopicHasEasierProblem() {
        return this.curTopicHasEasierProblem;
    }

    public boolean curTopicHasHarderProblem() {
        return this.curTopicHasHarderProblem;
    }

    public void setIsExampleShown (boolean seenExample) throws SQLException {
        this.exampleShown = seenExample;
        setProp(this.objid, EXAMPLE_SHOWN, seenExample);
    }

    public boolean isExampleShown () {
        return this.exampleShown;
    }

    public int getCurProblemIndexInTopic() {
        return curProblemIndexInTopic;
    }

    public void setCurProblemIndexInTopic(int curProblemIndexInTopic) throws SQLException {
        this.curProblemIndexInTopic = curProblemIndexInTopic;
        setProp(this.objid,CUR_PROB_INDEX_IN_TOPIC,curProblemIndexInTopic);
    }

    public String getNextProblemMode() {
        return nextProblemMode;
    }
    
    public void setNextProblemMode(String nextProblemMode) throws SQLException {
        this.nextProblemMode = nextProblemMode;
        setProp(this.objid,NEXT_PROBLEM_MODE,nextProblemMode);
    }

    public String getInternalState() {
        return internalState;
    }

    public void setInternalState(String topicState) throws SQLException {
        this.internalState = topicState;
        setProp(this.objid,TOPIC_INTERNAL_STATE,topicState);
    }

    public int getTopicProblemsSolved() {
        return topicProblemsSolved;
    }

    public void setTopicProblemsSolved(int topicProblemsSolved) throws SQLException {
        this.topicProblemsSolved = topicProblemsSolved;
        setProp(this.objid,TOPIC_PROBLEMS_SOLVED,topicProblemsSolved);
    }

    public void clearReviewTopics () throws SQLException {
        this.reviewTopics = null;
        clearProp(this.objid,REVIEW_TOPICS);
    }

    public List<String> getReviewTopics() {
        return reviewTopics;
    }

    public void setReviewTopics(List<String> reviewTopics) throws SQLException {
        this.reviewTopics = reviewTopics;
        for (String topicId: reviewTopics)
            addProp(this.objid,REVIEW_TOPICS, topicId);
    }

    public boolean isCurProblemBroken() {
        return curProblemBroken;
    }

    public void setCurProblemBroken(boolean curProblemBroken) throws SQLException {

        this.curProblemBroken = curProblemBroken;
        setProp(this.objid,CUR_PROB_BROKEN,curProblemBroken);
    }
}
