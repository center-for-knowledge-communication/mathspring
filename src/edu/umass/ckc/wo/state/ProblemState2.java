package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.db.DbStateTableMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemState2 extends State {

    private static final String CUR_HINT = "curHint";
    private static final String CUR_HINT_ID = "curHintId";
    private static final String PROB_ELAPSED_TIME = "elapsedTime";
    private static final String PROB_START_TIME = "startTime";
    private static final String HINT_START_TIME = "hintStartTime";
    private static final String ATTEMPT_START_TIME = "attemptStartTime";
    private static final String CUR_PROB_NUM_ATTEMPTS = "numAttempts";
    private static final String CUR_PROB_AVG_TIME_BETWEEN_ATTEMPTS = "avgTimeBetweenAttempts";
    private static final String CUR_PROB_NUM_MISTAKES = "numMistakes";
    private static final String CUR_PROB_NUM_HINTS_GIVEN = "numHintsGiven";
    private static final String CUR_PROB_NUM_HELPAIDS_GIVEN = "numHelpAidsGiven";
    private static final String CUR_PROB_MAX_HINTS = "maxHints";
    private static final String PROBLEM_SOLVED = "problemSolved";
    private static final String TIME_TO_SOLVE = "timeToSolve";
    private static final String TIME_TO_FIRST_EVENT = "timeToFirstEvent";
    private static final String TIME_TO_FIRST_HINT = "timeToFirstHint";
    private static final String TIME_TO_SECOND_HINT = "timeToSecondHint";
    private static final String TIME_TO_THIRD_HINT = "timeToThirdHint";
    private static final String TIME_TO_FIRST_ATTEMPT = "timeToFirstAttempt";
    private static final String TIME_TO_SECOND_ATTEMPT = "timeToSecondAttempt";
    private static final String TIME_TO_THIRD_ATTEMPT = "timeToThirdAttempt";
    private static final String TIME_IN_HINTS_BEFORE_CORRECT = "timeInHintsBeforeCorrect";
    private static final String NUM_HINTS_BEFORE_CORRECT = "numHintsBeforeCorrect";
    private static final String NUM_HELPAIDS_BEFORE_CORRECT = "numHelpAidsBeforeCorrect";
    private static final String FIRST_EVENT = "firstEvent";
    private static final String LAST_EVENT = "lastEvent";
    private static final String STRATEGIC_HINT_SHOWN= "strategicHintShown";
    private static final String PROB_IDLE_TIME = "idleTime";
    private static final String VIDEO_SHOWN = "videoShown";
    private static final String PROB_EXAMPLES_SHOWN = "examplesShown";

    private static final String TEXT_READER_USED = "textReaderUsed";
    private static final String SOLUTION_HINT_GIVEN = "solutionHintGiven";
    private static final String CUR_INTERVENTION = "curIntervention";
    private static final String INTERVENTION_START_TIME = "interventionStartTime";
    private static final String PROBLEM_BINDING = "problemBinding";
    private static final String PROBLEM_ANSWER = "answer";
    private static final String POSSIBLE_SHORT_ANSWERS = "possibleShortAnswers";
    private static final String PROBLEM_TYPE = "problemType";

    private static final String LANG_INDEX = "langIndex";

    static final String  TABLE_NAME= "studentproblemstate";
    public static final String[] TABLE_COLS  = new String[] { CUR_HINT, CUR_HINT_ID, PROB_ELAPSED_TIME, PROB_START_TIME, HINT_START_TIME, ATTEMPT_START_TIME,
            CUR_PROB_NUM_ATTEMPTS, CUR_PROB_AVG_TIME_BETWEEN_ATTEMPTS, CUR_PROB_NUM_MISTAKES, CUR_PROB_NUM_HINTS_GIVEN, CUR_PROB_NUM_HELPAIDS_GIVEN,
            CUR_PROB_MAX_HINTS, PROBLEM_SOLVED, TIME_TO_SOLVE, TIME_TO_FIRST_EVENT, TIME_TO_FIRST_HINT, TIME_TO_SECOND_HINT, TIME_TO_THIRD_HINT, TIME_TO_FIRST_ATTEMPT, TIME_TO_SECOND_ATTEMPT, TIME_TO_THIRD_ATTEMPT,
            TIME_IN_HINTS_BEFORE_CORRECT,
            NUM_HINTS_BEFORE_CORRECT, NUM_HELPAIDS_BEFORE_CORRECT, FIRST_EVENT, LAST_EVENT, STRATEGIC_HINT_SHOWN, PROB_IDLE_TIME, VIDEO_SHOWN,
            TEXT_READER_USED, SOLUTION_HINT_GIVEN, CUR_INTERVENTION, INTERVENTION_START_TIME, PROB_EXAMPLES_SHOWN, PROBLEM_BINDING, PROBLEM_ANSWER,POSSIBLE_SHORT_ANSWERS,PROBLEM_TYPE, LANG_INDEX};
         // N.B.  If you add a new field above,  make sure clearState deletes its value


    private String curHint;
    private int curHintId;
    private long elapsedTime;
    private long startTime;
    private long hintStartTime;
    private long attemptStartTime;
    private double avgTimeBetweenAttempts;
    private int numAttempts;
    private int numMistakes;
    private int numHintsGiven;
    private int numHelpAidsGiven;
    private int maxHints;
    private boolean problemSolvedB;
    private int problemSolved;
    private long timeToFirstEvent;
    private long timeToFirstHint;
    private long timeToSecondHint;
    private long timeToThirdHint;
    private long timeToFirstAttempt;
    private long timeToSecondAttempt;
    private long timeToThirdAttempt;
    private long timeInHintsBeforeCorrect;
    private int numHintsBeforeCorrect;
    private int numHelpAidsBeforeCorrect;
    private long timeToSolve;
    private String firstEvent;
    private String lastEvent;
    private int strategicHintShown;
    private boolean strategicHintShownB;
    private long idleTime;
    private boolean videoShownB;
    private int videoShown;
    private int examplesShown;
    private boolean textReaderUsedB;
    private int textReaderUsed;
    private boolean solutionHintGivenB;
    private int solutionHintGiven;
    private String curIntervention;
    private long interventionStartTime;
    private boolean inProblemB;
    private int inProblem;
    private String problemBinding;
    private String answer;
    private String possibleShortAnswers;   // comma separated as coming out of db
    private List<String> possibleShortAnswersList;    // parsed version of below
    private String problemType;  // HTML, Flash, or some other.
    private int langIndex;


//    public ProblemState2(Connection conn) throws SQLException {
//        this.conn = conn;
//        dbWorker = new DbStateTableMgr(conn);
//    }

//    public void load( int studId) throws SQLException {
//        this.objid = studId;
//
//        try {
//            dbWorker.load(studId,this, TABLE_NAME, TABLE_COLS, this.getClass());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IntrospectionException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        setShortAnswerList();
//    }

//    /**
//     * Persist this object to the db.  This is called during StudentModel.save.
//     * @return
//     * @throws SQLException
//     */
//    public boolean save () throws SQLException {
//        dbWorker.save(this,this.objid, TABLE_NAME, TABLE_COLS, this.getClass());
//
//        // because the topicmasterylevels are updated in the db by this classes access method, we don't need to write them when the
//        // student model is saved.
//        return true;
//    }

    // The instance var shortAnswers is a String that has the possible short answers separated by a tab char.  This
    // converts that into a list of strings
    void setShortAnswerList () {
        if (possibleShortAnswers != null) {
            String[] answers = possibleShortAnswers.split("\t");
            this.possibleShortAnswersList = Arrays.asList(answers);
        }
        else
            this.possibleShortAnswersList = null;
    }

//
//    public void extractProps(WoProps props) throws SQLException {
//        Map m = props.getMap();
//        this.curHint = mapGetPropString(m, CUR_HINT);
//        this.curHintId = mapGetPropInt(m, CUR_HINT_ID, -1);
//        this.probElapsedTime = mapGetPropLong(m, PROB_ELAPSED_TIME, 0);
//        this.probStartTime = mapGetPropLong(m, PROB_START_TIME, -1);
//        this.hintStartTime = mapGetPropLong(m, HINT_START_TIME, -1);
//        this.attemptStartTime = mapGetPropLong(m, ATTEMPT_START_TIME, -1);
//        this.curProblemAvgTimeBetweenAttempts = mapGetPropDouble(m, CUR_PROB_AVG_TIME_BETWEEN_ATTEMPTS, 0);
//        this.numAttemptsOnCurProblem = mapGetPropInt(m, CUR_PROB_NUM_ATTEMPTS, 0);
//        this.numMistakesOnCurProblem = mapGetPropInt(m, CUR_PROB_NUM_MISTAKES, 0);
//        this.numHintsGivenOnCurProblem = mapGetPropInt(m, CUR_PROB_NUM_HINTS_GIVEN, 0);
//        this.numHelpAidsGivenOnCurProblem = mapGetPropInt(m, CUR_PROB_NUM_HELPAIDS_GIVEN, 0);
//        this.curProblemMaxHintNum = mapGetPropInt(m, CUR_PROB_MAX_HINTS, 0);
//        this.problemSolved = mapGetPropBoolean(m, PROBLEM_SOLVED, false);
//        this.timeToFirstEvent = mapGetPropLong(m, TIME_TO_FIRST_EVENT, 0);
//        this.timeToFirstHint = mapGetPropLong(m, TIME_TO_FIRST_HINT, -1);
//        this.timeToFirstAttempt = mapGetPropLong(m, TIME_TO_FIRST_ATTEMPT, -1);
//        this.timeInHintsBeforeCorrect = mapGetPropLong(m, TIME_IN_HINTS_BEFORE_CORRECT, 0);
//        this.numHintsBeforeCorrect = mapGetPropInt(m, NUM_HINTS_BEFORE_CORRECT, 0);
//        this.numHelpAidsBeforeCorrect = mapGetPropInt(m, NUM_HELPAIDS_BEFORE_CORRECT, 0);
//        this.timeToSolve = mapGetPropLong(m, TIME_TO_SOLVE, -1);
//        this.firstEvent = mapGetPropString(m, FIRST_EVENT, null); // first thing student did in this problem
//        this.lastEvent = mapGetPropString(m, LAST_EVENT, null); // last thing student did in this problem
//        this.strategicHintShown = mapGetPropBoolean(m, STRATEGIC_HINT_SHOWN, false);
//        this.problemIdleTime = mapGetPropLong(m, PROB_IDLE_TIME, 0);
//        this.videoShown = mapGetPropBoolean(m, VIDEO_SHOWN, false);
//        this.probExamplesShown = mapGetPropInt(m, PROB_EXAMPLES_SHOWN, 0);
//        this.textReaderUsed = mapGetPropBoolean(m,TEXT_READER_USED,false);
//        this.solutionHintGiven =  mapGetPropBoolean(m,SOLUTION_HINT_GIVEN,false);
//        this.problemBinding = mapGetPropString(m,PROBLEM_BINDING,"");
//        this.problemAnswer = mapGetPropString(m,PROBLEM_ANSWER,"");
//        this.possibleShortAnswers = mapGetPropList(m,POSSIBLE_SHORT_ANSWERS);
//    }


    // May be given a null problem at beginning of session so correctly initialize either way.
    void initializeState() throws SQLException {

        this.setNumHintsBeforeCorrect(0);
        this.setNumHelpAidsBeforeCorrect(0);
        this.setTimeToFirstEvent(-1);
        this.setTimeToFirstAttempt(-1);
        this.setTimeToSecondAttempt(-1);
        this.setTimeToThirdAttempt(-1);
        this.setTimeInHintsBeforeCorrect(0);
        this.setFirstEvent(null);
        this.setLastEvent(null);
        this.setCurHint(null); // name of current hint
        this.setCurHintId(-1);   // id of current hint
        this.setHintStartTime(-1);
        this.setSolutionHintGiven(false);
        this.setTimeToFirstHint(-1);
        this.setTimeToSecondHint(-1);
        this.setTimeToThirdHint(-1);
        this.setInterventionStartTime(-1);
        this.setIdleTime(0);
        this.setCurIntervention(null);   // no current intervention on beginning a problem
        this.setNumHintsGiven(0);
        this.setNumHelpAidsGiven(0);
        this.setNumMistakes(0);
        this.setNumAttempts(0);
        this.setAvgTimeBetweenAttempts(-1);
        this.setProbElapsedTime(0);
        this.setProblemSolved(false);
        this.setAttemptStartTime(-1);
        this.setStrategicHintShown(false);
        this.setTimeToSolve(-1);
        this.setIsVideoShown(false);
        this.setExamplesShown(0);
        this.setIsTextReaderUsed(false);
        this.possibleShortAnswers = null;
        this.setProblemBinding("");
        this.setProblemType("");

    }

//    public static void clearState (Connection conn, int studId) throws SQLException {
//        DbStateTableMgr.clear(conn, TABLE_NAME, studId);
//    }



//    // This is called when a problem is put on-screen in Flash.
//    public void beginProblem(SessionManager smgr, BeginProblemEvent e) throws SQLException {
//        this.setStartTime(e.getElapsedTime());
//    }




    public void setCurHint(String curHint) throws SQLException {
        this.curHint = curHint;
    }

    public String getCurHint() {
        return curHint;
    }

    public void setCurHintId(int curHintId) throws SQLException {
        this.curHintId = curHintId;
    }

    public int getCurHintId() {
        return curHintId;
    }

    public void setProbElapsedTime(long probElapsedTime) throws SQLException {
        this.elapsedTime = probElapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setStartTime(long probStartTime) throws SQLException {
        this.startTime = probStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setHintStartTime(long hintStartTime) throws SQLException {
        this.hintStartTime = hintStartTime;
    }

    public long getHintStartTime() {
        return hintStartTime;
    }

    public void setAttemptStartTime(long attemptStartTime) throws SQLException {
        this.attemptStartTime = attemptStartTime;
    }

    public long getAttemptStartTime() {
        return attemptStartTime;
    }

    public void setAvgTimeBetweenAttempts(double avgTimeBetweenAttempts) throws SQLException {
        this.avgTimeBetweenAttempts = avgTimeBetweenAttempts;
    }

    public double getAvgTimeBetweenAttempts() {
        return avgTimeBetweenAttempts;
    }

    public void setNumAttempts(int numAttempts) throws SQLException {
        this.numAttempts = numAttempts;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public void setNumMistakes(int numMistakes) throws SQLException {
        this.numMistakes = numMistakes;
    }

    public int getNumMistakes() {
        return numMistakes;
    }

    public void setNumHintsGiven(int numHintsGiven) throws SQLException {
        this.numHintsGiven = numHintsGiven;
    }

    public int getNumHintsGiven() {
        return numHintsGiven;
    }

    public void setNumHelpAidsGiven(int numAidsGiven) throws SQLException {
        this.numHelpAidsGiven = numAidsGiven;
    }

    public int getNumHelpAidsGiven() {
        return numHelpAidsGiven;
    }

    public void setMaxHints(int maxHintNum) throws SQLException {
        this.maxHints = maxHintNum;
    }

    public int getMaxHints() {
        return maxHints;
    }

    public void setProblemSolved(boolean problemSolved) throws SQLException {
        this.problemSolvedB = problemSolved;
    }


    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setProblemSolved(int problemSolved) throws SQLException {
        this.problemSolved = problemSolved;
        this.problemSolvedB = problemSolved==1;
    }

    public boolean isProblemSolved() {
        return problemSolvedB;
    }

    public void setTimeToFirstEvent(long timeToFirstEvent) throws SQLException {
        this.timeToFirstEvent = timeToFirstEvent;
    }

    public long getTimeToFirstEvent() {
        return timeToFirstEvent;
    }

    public void setTimeToFirstHint(long timeToFirstHint) throws SQLException {
        this.timeToFirstHint = timeToFirstHint;
    }

    public void setTimeToSecondHint(long timeToHint2) throws SQLException {
        this.timeToSecondHint = timeToHint2;
    }

    public void setTimeToThirdHint(long timeToHint3) throws SQLException {
        this.timeToThirdHint = timeToHint3;
    }

    public long getTimeToFirstHint() {
        return timeToFirstHint;
    }
    public long getTimeToSecondHint() {
        return timeToSecondHint;
    }
    public long getTimeToThirdHint() {
        return timeToThirdHint;
    }

    public void setTimeToFirstAttempt(long timeToFirstAttempt) throws SQLException {
        this.timeToFirstAttempt = timeToFirstAttempt;
    }

    public void setTimeToSecondAttempt(long timeToSecondAttempt) throws SQLException {
        this.timeToSecondAttempt = timeToSecondAttempt;
    }
    public void setTimeToThirdAttempt(long timeToThirdAttempt) throws SQLException {
        this.timeToThirdAttempt = timeToThirdAttempt;
    }

    public long getTimeToFirstAttempt() {
        return timeToFirstAttempt;
    }
    public long getTimeToSecondAttempt() {
        return timeToSecondAttempt;
    }
    public long getTimeToThirdAttempt() {
        return timeToThirdAttempt;
    }

    public void setTimeInHintsBeforeCorrect(long timeInHintsBeforeCorrect) throws SQLException {
        this.timeInHintsBeforeCorrect = timeInHintsBeforeCorrect;
    }

    public long getTimeInHintsBeforeCorrect() {
        return timeInHintsBeforeCorrect;
    }

    public void setNumHintsBeforeCorrect(int numHintsBeforeCorrect) throws SQLException {
        this.numHintsBeforeCorrect = numHintsBeforeCorrect;
    }

    public int getNumHintsBeforeCorrect() {
        return numHintsBeforeCorrect;
    }

    public void setNumHelpAidsBeforeCorrect(int numHelpAidsBeforeCorrect) throws SQLException {
        this.numHelpAidsBeforeCorrect = numHelpAidsBeforeCorrect;
    }

    public int getNumHelpAidsBeforeCorrect() {
        return numHelpAidsBeforeCorrect;
    }

    // this is called when student answers problem correctly
    public void setTimeToSolve(long timeToSolve) throws SQLException {
        this.timeToSolve = timeToSolve;
    }

    public long getTimeToSolve() {
        return timeToSolve;
    }

    public void setFirstEvent(String firstEvent) throws SQLException {
        this.firstEvent = firstEvent;
    }

    public String getFirstEvent() {
        return firstEvent;
    }

    public void setLastEvent(String lastEvent) throws SQLException {
        this.lastEvent = lastEvent;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void setStrategicHintShown(boolean strategicHintShown) throws SQLException {
        this.strategicHintShownB = strategicHintShown;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setStrategicHintShown(int strategicHintShown) throws SQLException {
        this.strategicHintShown = strategicHintShown;
        this.strategicHintShownB = strategicHintShown== 1;
    }


    public boolean isStrategicHintShown() {
        return strategicHintShownB;
    }

    public void setIdleTime (long t) throws SQLException {
        this.idleTime = t;
    }

    public long getIdleTime () {
        return this.idleTime;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setIsVideoShown (int seenVideo) throws SQLException {
        this.videoShown = seenVideo;
        this.videoShownB = seenVideo==1;
    }

    public void setIsVideoShown (boolean seenVideo) throws SQLException {
        this.videoShownB = seenVideo;
    }

    public boolean isVideoShown () {
        return this.videoShownB;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setIsTextReaderUsed (int textReaderUsed) throws SQLException {
        this.textReaderUsed = textReaderUsed;
        this.textReaderUsedB = textReaderUsed==1;
    }


    public void setIsTextReaderUsed (boolean textReaderUsed) throws SQLException {
        this.textReaderUsedB = textReaderUsed;
    }

    public boolean isTextReaderUsed () {
        return this.textReaderUsedB;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setSolutionHintGiven(int solutionHintGiven) throws SQLException {
        this.solutionHintGiven = solutionHintGiven;
        this.solutionHintGivenB = solutionHintGiven==1;
    }

    public void setSolutionHintGiven(boolean solutionHintGiven) throws SQLException {
        this.solutionHintGivenB = solutionHintGiven;
    }

    public boolean isSolutionHintGiven() {
        return solutionHintGivenB;
    }

    public void setCurIntervention(String curIntervention) throws SQLException {
        this.curIntervention = curIntervention;
    }

    public String getCurIntervention() {
        return curIntervention;
    }

    public void setInterventionStartTime(long interventionStartTime) throws SQLException {
        this.interventionStartTime = interventionStartTime;
    }

    public long getInterventionStartTime() {
        return interventionStartTime;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setInProblem(int inProblem) {
        this.inProblem = inProblem;
        this.inProblemB = inProblem==1;
    }

    public void setInProblem(boolean inProblem) {
        this.inProblemB = inProblem;
    }

    public boolean isInProblem() {
        return inProblemB;
    }


    public void setExamplesShown(int num) throws SQLException {
        this.examplesShown = num;

    }

    public int getExamplesShown () {
        return this.examplesShown;
    }

    public void setProblemBinding(String binding) throws SQLException {
        this.problemBinding = binding;
    }

    public String getProblemBinding()
    {
        return problemBinding;
    }

    public void setAnswer(String problemAnswer) throws SQLException {
        this.answer = problemAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getPossibleShortAnswersList() {
        return possibleShortAnswersList;
    }

    public String getPossibleShortAnswers () {
        return this.possibleShortAnswers;
    }

    public void setPossibleShortAnswers(String possibleShortAnswers) throws SQLException {
        this.possibleShortAnswers = possibleShortAnswers;
        setShortAnswerList();
    }


    public void setPossibleShortAnswersList(List<String> possibleShortAnswersList) throws SQLException {
        this.possibleShortAnswersList = possibleShortAnswersList;
        StringBuilder sb = new StringBuilder();
        for (String a : possibleShortAnswersList)
            sb.append(a+"\t") ;
        sb.deleteCharAt(sb.length()-1);
        this.possibleShortAnswers = sb.toString();
    }



    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setVideoShown(int videoShown) {
        this.videoShown = videoShown;
        this.videoShownB = videoShown==1;
    }

    public void setVideoShown(boolean videoShown) {
        this.videoShownB = videoShown;
    }

    // This is to support the DbStateTableMgr class which automatically sets this object from db columns that are of type tinyint
    public void setTextReaderUsed(int textReaderUsed) {
        this.textReaderUsed = textReaderUsed;
        this.textReaderUsedB = textReaderUsed==1;
    }

    public void setTextReaderUsed(boolean textReaderUsed) {
        this.textReaderUsedB = textReaderUsed;
    }

    public int getProblemSolved() {
        return problemSolved;
    }

    public int getStrategicHintShown() {
        return strategicHintShown;
    }

    public int getVideoShown() {
        return videoShown;
    }

    public int getTextReaderUsed() {
        return textReaderUsed;
    }

    public int getSolutionHintGiven() {
        return solutionHintGiven;
    }

    public int getInProblem() {
        return inProblem;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public void setLangIndex(int langIndex) throws SQLException {
        this.langIndex = langIndex;
    }

    public int getLangIndex() {
        return langIndex;
    }

}
