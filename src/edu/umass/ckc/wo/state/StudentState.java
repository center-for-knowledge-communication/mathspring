package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;
import edu.umass.ckc.wo.smgr.*;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionState;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.TutorEventHandler;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2005
 * Time: 9:12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class StudentState extends State implements TutorEventHandler {

    public static final String HINT_EVENT = "hint";
    public static final String ATTEMPT_EVENT = "attempt";
    public static final String NO_EVENT = null;
    public static final String CORRECT_ATTEMPT_EVENT = "correct attempt";
    public static final String INCORRECT_ATTEMPT_EVENT = "incorrect attempt";
    private static final String END_PROBLEM_EVENT = "endProblem";
     private static Logger logger = Logger.getLogger(StudentState.class);

   // variables that are only needed for the duration of the student's session
    private ProblemState problemState;
    private TopicState topicState;
    private LessonState2 lessonState;
    private SessionState sessionState;
    private WorkspaceState workspaceState;
    private PrePostState ppState;
    private SessionManager smgr;
    private int curProblemIndexInTopic;
    private boolean curProblemIsTopicIntro;
    private String pedagogicalModelInternalState;


    public StudentState(Connection conn, SessionManager smgr) throws SQLException {
        this.smgr = smgr;
        this.conn = conn;
        this.problemState = new ProblemState(conn);
        this.topicState = new TopicState(conn);
        this.lessonState = new LessonState2(conn);
        this.sessionState = new SessionState(conn);
        this.workspaceState = new WorkspaceState(conn);
        this.ppState = new PrePostState(conn);
    }


     ProblemState getProblemState () {
        return this.problemState;
     }

    public void extractProps(WoProps props) throws SQLException {
//        problemState.extractProps(props);
        problemState.load(objid);
        topicState.extractProps(props);
        lessonState.load(objid);
        sessionState.extractProps(props);
        workspaceState.extractProps(props);
        ppState.extractProps(props);

    }

    public void save () throws SQLException {
        problemState.save();
        lessonState.save();
    }


    public void setObjid(int objid) {
        super.setObjid(objid);
        problemState.setObjid(objid);
        topicState.setObjid(objid);
        lessonState.setObjid(objid);
        sessionState.setObjid(objid);
        workspaceState.setObjid(objid);
        ppState.setObjid(objid);
    }

   //////////////////   Beginning of Topic State methods

    /* Methods that are dealing with state during teaching a topic */

    public int getCurProblem() {
//        return topicState.getCurProblem();
        return lessonState.getCurProblem();
    }

    public void setCurProblem(int curProblem) throws SQLException {
//        topicState.setCurProblem(curProblem);
        lessonState.setCurProblem(curProblem);
    }

    public int getLastProblem () {
//        return topicState.getLastProblem();
        return lessonState.getLastProblem();
    }

    public void setLastProblem (int lastProblem) throws SQLException {
//       topicState.setLastProblem(lastProblem);
       lessonState.setLastProblem(lastProblem);
    }

    public int getNextProblem () {
//        return topicState.getNextProblem();
        return lessonState.getNextProblem();
    }

    public void setNextProblem (int nextProblem) throws SQLException {
//        topicState.setNextProblem(nextProblem);
        lessonState.setNextProblem(nextProblem);
    }

    public String getCurProblemMode() {
//        return topicState.getCurProblemMode();
        return lessonState.getCurProblemMode();
    }

    public void setCurProblemMode(String curProblemMode) throws SQLException {
//        topicState.setCurProblemMode(curProblemMode);
        lessonState.setCurProblemMode(curProblemMode);
    }

    public String getLastProblemMode() {
//        return topicState.getLastProblemMode();
        return lessonState.getLastProblemMode();
    }

    public void setLastProblemMode(String mode) throws SQLException {
//        topicState.setLastProblemMode(mode);
        lessonState.setLastProblemMode(mode);
    }

    public String getCurProbType() {
        return problemState.getPojo().getProblemType();
    }

    public void setCurProbType(String type) throws SQLException {
        problemState.getPojo().setProblemType(type);

    }

    public boolean isTopicIntroShown() {
        return topicState.isTopicIntroShown();
    }

    public void setTopicIntroShown(boolean b) throws SQLException {
        topicState.setTopicIntroShown(b);
    }

    public boolean isSecondHardestSolvedWell() {
        return topicState.isSecondHardestSolvedWell();

    }

    public void setSecondHardestSolvedWell(boolean b) throws SQLException {
        topicState.setSecondHardestSolvedWell(b);
    }

    public boolean isSecondEasiestProblemFailedSolve() {
        return topicState.isSecondEasiestProblemFailedSolve();
    }

    public void setSecondEasiestProblemFailedSolve(boolean x) throws SQLException {
        topicState.setSecondEasiestProblemFailedSolve(x);
    }

    public boolean isSecondHardestShown() {
        return topicState.isSecondHardestShown();
    }

    public void setSecondHardestShown(boolean b) throws SQLException {
        topicState.setSecondHardestShown(b);
    }

    public boolean isHardestSolvedWell() {
        return topicState.isHardestSolvedWell();
    }

    public void setHardestSolvedWell(boolean b) throws SQLException {
        topicState.setSecondHardestSolvedWell(b);
    }

    public boolean isHardestShown() {
        return topicState.isHardestShown();
    }

    public void setHardestShown(boolean b) throws SQLException {
        topicState.setHardestShown(b);

    }

    public boolean isEasiestShown() {
        return topicState.isEasiestShown();
    }

    public void setEasiestShown(boolean x) throws SQLException {
       topicState.setEasiestShown(x);
    }

    public boolean isHardestProblemSolvedWell() {
        return topicState.isHardestProblemSolvedWell();
    }

    public void setHardestProblemSolvedWell(boolean hardestProblemSolvedWell) throws SQLException {
        topicState.setHardestProblemSolvedWell(hardestProblemSolvedWell);
    }

    public boolean isEasiestProblemFailedSolve() {
        return topicState.isEasiestProblemFailedSolve() ;
    }

    public void setEasiestProblemFailedSolve(boolean easiestProblemFailedSolve) throws SQLException {
        topicState.setEasiestProblemFailedSolve(easiestProblemFailedSolve);
    }

    public boolean isSecondEasiestShown() {
        return topicState.isSecondEasiestShown();
    }


    public void setSecondEasiestShown(boolean x) throws SQLException {
        topicState.setSecondEasiestShown(x);
    }

    /**
     * Each time a problem is selected by the AdaptiveProblemGroup selector this records if the selector wanted a harder,easier, or
     * same difficulty problem.   Puts them all into a list stored in the state
     * @param criterion
     */
    public void addProblemSelectionCriterion(String criterion) throws SQLException {
        topicState.addProblemSelectionCriterion(criterion);
    }

    public String getLastIntervention() {
        return topicState.getLastIntervention();
    }

    public void setLastIntervention(String lastIntervention) throws SQLException {
        topicState.setLastIntervention(lastIntervention);
    }

    public void setLastAnswer(String lastAnswer) throws SQLException {
        topicState.setLastAnswer(lastAnswer);
    }


    public boolean getInBtwProbIntervention () {
        return topicState.getInBtwProbIntervention();
    }

    public void setInBtwProbIntervention (boolean b) throws SQLException {
        topicState.setInBtwProbIntervention(b);
    }

    public int getTopicNumProbsSeen () {
        return topicState.getTopicNumProbsSeen();
    }

    public void setTopicNumProbsSeen(int n) throws SQLException {
       topicState.setTopicNumProbsSeen(n);
    }

    public int getTopicNumPracticeProbsSeen () {
        return topicState.getTopicNumPracticeProbsSeen();
    }

    public void setTopicNumPracticeProbsSeen(int n) throws SQLException {
        topicState.setTopicNumPracticeProbsSeen(n);
    }

    public int getStudentSelectedTopic() {
        return topicState.getStudentSelectedTopic();
    }

    public void setStudentSelectedTopic(int studentSelectedTopic) throws SQLException {
        topicState.setStudentSelectedTopic(studentSelectedTopic);
    }


    public int getSidelinedTopic() {
        return topicState.getSidelinedTopic();
    }

    public void setSidelinedTopic(int topicId) throws SQLException {
        topicState.setSidelinedTopic(topicId);
    }

    public boolean isInReviewMode() {
        return topicState.isInReviewMode();
    }

    public void setInReviewMode(boolean inReviewMode) throws SQLException {
        topicState.setInReviewMode(inReviewMode);
    }

    public boolean isInChallengeMode() {
        return topicState.isInChallengeMode();
    }

    public void setInChallengeMode(boolean inChallengeMode) throws SQLException {
        topicState.setInChallengeMode(inChallengeMode);
    }

    public boolean isTopicSwitch() {
        return topicState.isTopicSwitch();
    }

    public void setTopicSwitch(boolean topicSwitch) throws SQLException {
        topicState.setTopicSwitch(topicSwitch);
    }

    public boolean isContentFailureTopicSwitch() {
        return topicState.isContentFailureTopicSwitch();
    }

    public void setContentFailureTopicSwitch(boolean contentFailureTopicSwitch) throws SQLException {
        topicState.setContentFailureTopicSwitch(contentFailureTopicSwitch);
    }

    public void setTeachTopicMode(boolean b) throws SQLException {
        topicState.setTeachTopicMode(b);
    }

    public boolean inTeachTopicMode () {
        return topicState.inTeachTopicMode();
    }

    public void setInPracticeMode(boolean inPracticeMode) throws SQLException {
        topicState.setInPracticeMode(inPracticeMode);
    }

    public void setIsExampleShown(boolean b) throws SQLException {
        topicState.setIsExampleShown(b);
    }

    public boolean isExampleShown () {
        return topicState.isExampleShown();
    }

    public int getCurProblemIndexInTopic() {
        return topicState.getCurProblemIndexInTopic();
    }

    public void setCurProblemIndexInTopic(int curProblemIndexInTopic) throws SQLException {
        topicState.setCurProblemIndexInTopic(curProblemIndexInTopic);
    }

    private String getNextProblemMode () {
//        return topicState.getNextProblemMode();
        return lessonState.getNextProblemMode();
    }

    public void setNextProblemMode(String mode) throws SQLException {
//        topicState.setNextProblemMode(mode);
        lessonState.setNextProblemMode(mode);
    }

    public long getTimeInTopic() {
        return topicState.getTimeInTopic();
    }

    public void setTimeInTopic(long t) throws SQLException {
        topicState.setTimeInTopic(t);
    }

    public int getContentFailureCounter() {
        return topicState.getContentFailureCounter();
    }

    public void setContentFailureCounter(int contentFailureCounter) throws SQLException {
        topicState.setContentFailureCounter(contentFailureCounter);
    }

    public String getTopicInternalState () {
        return topicState.getInternalState();
    }

    public void setTopicInternalState (String st) throws SQLException {
        topicState.setInternalState(st);
    }

    public int getTopicProblemsSolved () {
        return topicState.getTopicProblemsSolved();
    }

    public void setTopicProblemsSolved (int n) throws SQLException {
        topicState.setTopicProblemsSolved(n);
    }

    public void clearReviewTopics () throws SQLException {
        topicState.clearReviewTopics();
    }

    public List<String> getReviewTopics () {
        return topicState.getReviewTopics();
    }

    /** WHen an interleaved topic is given we save the topics from which problems are pulled.   This is only used
     * for two purposes.
     * @param topicIds
     */
    public void setReviewTopics (List<String> topicIds) throws SQLException {
        topicState.setReviewTopics(topicIds);

    }

    public boolean isCurProbBroken () { return topicState.isCurProblemBroken(); }

    public void setCurProbBroken (boolean b) throws SQLException { topicState.setCurProblemBroken(b);}


    /////////////////  End of Topic State methods

    //////////////////   Beginning of Session State methods.

    public List<String> getClips () {
        return sessionState.getClips();
    }

    public List<String> getClipCounters () {
        return sessionState.getClipCounters();
    }

    public int getLcClipCount (String clipName) {
        return sessionState.getLcClipCount(clipName);
    }

    public void setLcClipCount (String clipName, int count) throws SQLException {
        sessionState.setLcClipCount(clipName, count);
    }

    public long getLastInterventionTime() {
        return sessionState.getLastInterventionTime();
    }

    public void setLastInterventionTime(long time) throws SQLException  {
        sessionState.setLastInterventionTime(time);
    }

    public long getTimeLastChange() {
        return sessionState.getTimeLastChange();
    }

    public void setTimeLastChange(long timeLastChange) throws SQLException{
        sessionState.setTimeLastChange(timeLastChange);
    }


    // Given a 1-based clip-number, update the woproperty at this position number (clipNumber) to
    // be count.
    // Then update the instance variable (0 based) that holds the list of clip counters corresponding
    // with whats in woproperties.
    public void incrClipCount (int clipNumber, int count) throws SQLException {
        sessionState.incrClipCount(clipNumber, count);
    }

    public void setClipCount (String clip, int count ) throws SQLException {

        sessionState.setClipCount(clip, count);
    }





    /* end of lesson state  methods */

/* Methods that are dealing with state during a problem..   THese just forward to the problemState object but allow convenience for accessing through StudentState */


    public int getProbExamplesShown() {
        return problemState.getPojo().getExamplesShown();
    }

    public long getProbElapsedTime() {
        return problemState.getPojo().getElapsedTime();
    }


    public void setProbElapsedTime(long probElapsed) throws SQLException {
        problemState.getPojo().setProbElapsedTime(probElapsed);
    }

    public void setIsTextReaderUsed(boolean b) throws SQLException {
        problemState.getPojo().setIsTextReaderUsed(b);
    }

    public void setIsVideoShown(boolean b) throws SQLException {
        problemState.getPojo().setIsVideoShown(b);
    }

    public void setProblemIdleTime(int t) throws SQLException {
        problemState.getPojo().setIdleTime(t);
    }

    public int getCurHintId () {
        return problemState.getPojo().getCurHintId();
    }

    public String getCurHint () {
        return problemState.getPojo().getCurHint();
    }

    public long getProbStartTime() {
        return problemState.getPojo().getStartTime();
    }

    public long getHintStartTime() {
        return problemState.getPojo().getHintStartTime();
    }

    public long getAttemptStartTime() {
        return problemState.getPojo().getAttemptStartTime();
    }

    public double getCurProblemAvgTimeBetweenAttempts() {
        return problemState.getPojo().getAvgTimeBetweenAttempts();
    }

    public long getInterventionStartTime() {
        return problemState.getPojo().getInterventionStartTime();
    }

    public int getNumAttemptsOnCurProblem() {
        return problemState.getPojo().getNumAttempts();
    }

    public int getNumMistakesOnCurProblem() {
        return problemState.getPojo().getNumMistakes();
    }

    public int getNumHintsGivenOnCurProblem() {
        return problemState.getPojo().getNumHintsGiven();
    }

    public int getNumHelpAidsGivenOnCurProblem() {
        return problemState.getPojo().getNumHelpAidsGiven();
    }

    // Returns true only if they solved it correctly
    public boolean isProblemSolved() {
        return problemState.getPojo().isProblemSolved();
    }

    public long getTimeToSolve() {
        return problemState.getPojo().getTimeToSolve();
    }

    public long getTimeToFirstEvent() {
        return problemState.getPojo().getTimeToFirstEvent();
    }

    public long getTimeToFirstHint() {
        return problemState.getPojo().getTimeToFirstHint();
    }

    public long getTimeToSecondHint() {
        return problemState.getPojo().getTimeToSecondHint();
    }

    public long getTimeToThirdHint() {
        return problemState.getPojo().getTimeToThirdHint();
    }

    public long getTimeInHintsBeforeCorrect() {
        return problemState.getPojo().getTimeInHintsBeforeCorrect();
    }

    public long getTimeToFirstAttempt() {
        return problemState.getPojo().getTimeToFirstAttempt();
    }

    public long getTimeToSecondAttempt() {
        return problemState.getPojo().getTimeToSecondAttempt();
    }

    public long getTimeToThirdAttempt() {
        return problemState.getPojo().getTimeToThirdAttempt();
    }

    public int getNumHintsBeforeCorrect() {
        return problemState.getPojo().getNumHintsBeforeCorrect();
    }

    public int getNumHelpAidsBeforeCorrect() {
        return problemState.getPojo().getNumHelpAidsBeforeCorrect();
    }

    public String getFirstEvent() {
        return problemState.getPojo().getFirstEvent();
    }

    public String getLastEvent() {
        return problemState.getPojo().getLastEvent();
    }

    public boolean isStrategicHintShown () {
        return problemState.getPojo().isStrategicHintShown();
    }

    public void setStrategicHintShown(boolean b) throws SQLException {
        problemState.getPojo().setStrategicHintShown(b);
    }

    public boolean isVideoShown () {
        return problemState.getPojo().isVideoShown();
    }

    public boolean isTextReaderUsed () {
        return problemState.getPojo().isTextReaderUsed();
    }

    public boolean isSolutionHintGiven() {
        return problemState.getPojo().isSolutionHintGiven();
    }

    public String getCurIntervention () {
        return problemState.getPojo().getCurIntervention();
    }

    public void setCurIntervention(String s) throws SQLException {
        problemState.getPojo().setCurIntervention(s);
    }

    public boolean isInProblem() {
        return problemState.getPojo().isInProblem();
    }

    public void setInProblem(boolean inProblem) {
        problemState.getPojo().setInProblem(inProblem);
    }

    public boolean isLastEvent(String ev) {
        return (problemState.getPojo().getLastEvent() != NO_EVENT && problemState.getPojo().getLastEvent().equals(ev));
    }


    public void interventionGiven(StudentState state, Intervention intervention) throws SQLException {
        problemState.getPojo().setInterventionStartTime(getTime());
    }

    public String getProblemBinding()
    {
        return problemState.getPojo().getProblemBinding();
    }

    public void setProblemBinding(String binding) throws SQLException {
        problemState.getPojo().setProblemBinding(binding);
    }

    public String getProblemAnswer() {
        return problemState.getPojo().getAnswer();
    }

    public void setProblemAnswer(String ans) throws SQLException {
        problemState.getPojo().setAnswer(ans);
    }

    public List<String> getPossibleShortAnswers() {
        return problemState.getPojo().getPossibleShortAnswersList();
    }

    public void setPossibleShortAnswers(List<String> possibleShortAnswers) throws SQLException {
        problemState.getPojo().setPossibleShortAnswersList(possibleShortAnswers);
    }

    public int getProbLangIndex() {
        return problemState.getPojo().getProbLangIndex();
    }

    public void setProbLangIndex(int probLangIndex) throws SQLException {
        problemState.getPojo().setProbLangIndex(probLangIndex);
    }

    public int getTranslateProbId() {
        return problemState.getPojo().getTranslateProbId();
    }

    public void setTranslateProbId(int translateProbId) throws SQLException {
        problemState.getPojo().setTranslateProbId(translateProbId);
    }

    public int getAltProbId() {
        return problemState.getPojo().getAltProbId();
    }

    public void setAltProbId(int altProbId) throws SQLException {
        problemState.getPojo().setAltProbId(altProbId);
    }

   
    /* end of problem state methods */




/*The following methods have to do with the session state*/

    public String getCurLocation() {
        return sessionState.getCurLocation();
    }

    public  void setCurLocation(String curLocation) throws SQLException {
        sessionState.setCurLocation(curLocation);
    }

    public long getTime() {
        return sessionState.getTime();
    }

    public void setTime(long time) throws SQLException {
        sessionState.setTime(time);
    }

    public int getNumProblemsThisTutorSession() {
        return sessionState.getNumProblemsThisTutorSession();
    }

    public void setNumProblemsThisTutorSession(int n) throws SQLException {
        sessionState.setNumProblemsThisTutorSession(n);
    }

    public int getNumRealProblemsThisTutorSession() {
        return sessionState.getNumRealProblemsThisTutorSession();
    }

    public void setNumRealProblemsThisTutorSession(int n) throws SQLException {
        sessionState.setNumRealProblemsThisTutorSession(n);
    }


    public long getTutorEntryTime () {
        return sessionState.getTutorEntryTime();
    }

    public void setTutorEntryTime(long time) throws SQLException {
        sessionState.setTutorEntryTime(time);
    }

    public List<String> getExternalActivitiesGiven () {
        return sessionState.getExternalActivitiesGiven();
    }

    public void addExternalActivityGiven (int id) throws SQLException {
        sessionState.addExternalActivityGiven(id);
    }

    public List<String> getExampleProblemsGiven() {
        return sessionState.getExampleProblemsGiven();
    }

    public void addExampleProblemGiven(int probId) throws SQLException {
        sessionState.addExampleProblemGiven(probId);
    }

    public int getTempProblemIndex() {
        return sessionState.getTempProblemIndex();
    }

    public void setTempProblemIndex(int tempProblemIndex) throws SQLException {
        sessionState.setTempProblemIndex(tempProblemIndex);
    }

    public void addTopicIntrosSeen(int topicId) throws SQLException {
        sessionState.addTopicIntrosSeen(topicId);
    }

    public boolean isTopicIntroSeen (int topicId) {
       return sessionState.isTopicIntroSeen(topicId);
    }

    public void addExampleSeen(int topicId) throws SQLException {
        sessionState.addTopicExampleSeen(topicId);
    }

    public boolean isExampleSeen (int topicId) {
        return sessionState.isTopicExampleSeen(topicId);
    }



    /* end of session state methods */

    /* The following methods have to do with the workspace state (state that is preserved after the student logs out) */



    public int getCurLesson (){
        return workspaceState.getCurLesson();
    }

    public void setCurLesson (int lessonId) throws SQLException {
        workspaceState.setCurLesson(lessonId);
    }

    public int getCurCU () {
        return workspaceState.getCurCU();
    }

    public void setCurCU (int cu) throws SQLException {
        workspaceState.setCurCU(cu);
    }

    public int getCurCluster () {
        return workspaceState.getCurCluster();
    }

    public void setCurCluster (int clust) throws SQLException {
        workspaceState.setCurCluster(clust);
    }

    public String getCurStd () {
        return workspaceState.getCurStd();
    }

    public void setCurStd (String std) throws SQLException {
        workspaceState.setCurStd(std);
    }

    public String getPrereqStd () {
        return workspaceState.getPrereqStd();
    }

    public void setPrereqStd (String std) throws SQLException {
        workspaceState.setPrereqStd(std);
    }

    public List<String> getPrereqStdStack () {
        return workspaceState.getPrereqStdStack();
    }

    public void addPrereqStdStack (String std) throws SQLException {
        workspaceState.addPrereqStdStack(std);
    }

    public List<String> getBrokenProblemIds () {
        return workspaceState.getBrokenProblemIds();
    }

    public void addBrokenProblemId (String pid) throws SQLException {
        workspaceState.addBrokenProblemId(pid);
    }


    public int getCurTopic() {
        return workspaceState.getCurTopic();
    }

    public void setCurTopic(int pgroupID) throws SQLException {
        workspaceState.setCurTopic(pgroupID);
    }

    public int getLastTopic() {
        return workspaceState.getLastTopic();
    }

    /*end of workspace state methods */

    /* Methods for pre / post tests */

    public boolean getPretestCompleted() {
        return ppState.getPretestCompleted();
    }

    public boolean getSatHutCompleted() {
        return ppState.getSatHutCompleted();
    }

    public boolean getPosttestCompleted() {
        return ppState.getPosttestCompleted();
    }

    public int getPretestProblemSet() {
        return ppState.getPretestProblemSet();
    }

    public void setPretestProblemSet (int p) throws SQLException {
        ppState.setPretestProblemSet(p);
    }

    public int getPosttestProblemSet() {
        return ppState.getPosttestProblemSet();
    }

    public void setPosttestProblemSet(int problemSet) throws SQLException {
        ppState.setPosttestProblemSet(problemSet);
    }

    public List getPretestProblemsGiven() {
        return ppState.getPretestProblemsGiven();
    }

    public List getPosttestProblemsGiven() {
        return ppState.getPosttestProblemsGiven();
    }

    public boolean isPretestCompleted() {
        return ppState.isPretestCompleted();
    }

    public void setPretestCompleted(boolean pretestCompleted) throws SQLException {
        ppState.setPretestCompleted(pretestCompleted);
    }

    public boolean isPosttestCompleted() {
        return ppState.isPosttestCompleted();
    }

    public void setPosttestCompleted(boolean b) throws SQLException {
        ppState.setPosttestCompleted(b);
    }

    public boolean isSatHutCompleted(){
        return false;
    }

    public void addPretestProblem(int id) throws SQLException {
        ppState.addPretestProblem(id);
    }

    public void addPosttestProblem(int id) throws SQLException {
        ppState.addPosttestProblem(id);
    }

    public void setCurPreProblem(int id) throws SQLException {
        ppState.setCurPreProblem(id);
    }

    public void setCurPostProblem(int id) throws SQLException {
        ppState.setCurPostProblem(id);
    }

        /* End of Methods for pre / post tests */

    public void newTopic () throws SQLException {
        lessonState.initializeState();
        topicState.initializeState();

    }

    public void topicDone () throws SQLException {
        lessonState.cleanupState();
        topicState.cleanupState();
    }

    public void newSession (SessionManager smgr) throws SQLException {
        InterventionState.clearState(conn, smgr.getStudentId());  // on new session interventions need to clear their states.
        sessionState.initializeState();
        lessonState.initializeState();
        topicState.initializeState();
        problemState.initializeState();


    }



    private void initializeProblemState (Problem p) throws SQLException {
        this.problemState.initializeState();
    }

    // At the time NextProblem button is clicked, the tutor selects the next prob (or intervention).   If
    // it selects a next problem, this method gets called but the new problem doesn't really start
    // playing until the beginProblem event comes in (after an endProblem event terminating the previous
    //  problem).    Note no previous endProblem event can be expected for the first problem in a session.
    //
    // So all state variables associated with the newly
    // selected problem (which used to be initialized here) are now initialized in beginProblem
    public void newProblem(StudentState state, Problem p) throws SQLException {
        if (p == null) {
            setCurProblem(-1);
        } else {
            this.setNextProblem(p.getId());  // Why next?
            this.setNextProblemMode(p.getMode());

//            this.setLastProblemMode(lessonState.getCurProblemMode());
//            this.setCurProblemMode(p.getMode()); // We set the mode now because we have the actual Problem object and its mode.
            // When the beginProblem event happens, we don't know the mode anymore

            // When the student select a problem + topic, we need to set a state variable to hold the topicId.
            // This topicId will be logged with events associated with this problem only.   When the endProblem event
            // comes in,   we remove this topic from the state so that the tutor can revert to logging with the topicId
            // held by the smgr.
            this.setStudentSelectedTopic(p.getInTopicId());

        }
        // moved initializeState to beginProblem so that the endProblem event has the correct
        // problem settings at the time it is called (after this method and before beginProblem)
    }



    // This is called when a problem is put on-screen in client
    public void beginProblem(SessionManager smgr, BeginProblemEvent e) throws SQLException {
        long t = System.currentTimeMillis();
        this.setInProblem(true);
        problemState.beginProblem(smgr, e);
//        System.out.println("In StudentState.beginProblem, after problemState.beginProblem " + (System.currentTimeMillis() - t));

        this.setLastProblem(getCurProblem());
        this.setCurProblem(getNextProblem());


        this.setCurProblemMode(getNextProblemMode());
        Problem prob = ProblemMgr.getProblem(this.getNextProblem());
//        System.out.println("In StudentState.beginProblem, after ProblemMgr.getProblem " + (System.currentTimeMillis() - t));

        initializeProblemState(ProblemMgr.getProblem(this.getCurProblem()));
        //  a TOpicIntro won't be found here.
        if (prob != null) {
            this.setCurProbType(prob.getType());
        }
        else if (this.getCurProblemMode().equals(Problem.TOPIC_INTRO))
            this.setCurProbType(Problem.TOPIC_INTRO_PROB_TYPE);

//        System.out.println("In StudentState.beginProblem, after initializeState " + (System.currentTimeMillis() - t));


    }

    // When an EndProblem comes in at the end of a topic there are usually several interventions that play between
    // the NextProblem event and this EndProblem event.   They are usually TopicSwitchAsk and TopicIntro interventions
    // At the beginning of the new topic the lesson state is wiped clean.  This means that this EndProblem event
    // might have ....
    // This gets called in places where there may not be a previous problem so its best not to assume a cur problem.
    public void endProblem(SessionManager smgr, int studId,long probElapsedTime, long elapsedTime) throws SQLException {
        // At the end of each problem the timeInTopic is increased by the time spent in the problem.
        this.setTimeInTopic(this.getTimeInTopic()+ probElapsedTime);
        // check if the previous event was a hint and problem not solved yet.  If so, then update hint_time
       if ((this.isLastEvent(HINT_EVENT)) && (! isProblemSolved())) {
            long curr_hint_time = this.getTimeInHintsBeforeCorrect();
            long extra_hint_time = this.getProbElapsedTime() - this.getHintStartTime();
            problemState.getPojo().setTimeInHintsBeforeCorrect(curr_hint_time + extra_hint_time);
        }
        problemState.getPojo().setLastEvent(END_PROBLEM_EVENT);
        setLastProblemMode(getCurProblemMode());
        // We save the current problem into the workspace state only when it is completed.   This means that the next time a user logs in (who is using
        // common core pedagogy) they will use the current problem to find a location in the lesson structure.
        if (this.getCurProblemMode() != null && this.getCurProblemMode().equals(Problem.PRACTICE))
            workspaceState.setCurProb(this.getCurProblem());
        else workspaceState.setCurProb(-1); // If its not practice remove the probId from workspace state.
        this.setInProblem(false);  // DM 1/25/10
        // NOTE:  It is critical that the client end every problem because a problem that was started with a forced probID + topicId
        // is temporarily saving the topicId in the student state.   While the state variable is set to a value (other than -1)
        // the system will log all events with this topic ID.   When the problem ends we reset the this state var to -1
        this.setStudentSelectedTopic(-1);  // if the student has forced a problem+topic for this problem,  remove the topic
    }






    public void helpAidGiven(StudentState s) throws SQLException {
        problemState.getPojo().setNumHelpAidsGiven(problemState.getPojo().getNumHelpAidsGiven() + 1);
        if (this.getTimeToFirstEvent() < 0) {
            problemState.getPojo().setTimeToFirstEvent(this.getProbElapsedTime());
        }
        if ( ! isProblemSolved() )
            problemState.getPojo().setNumHelpAidsBeforeCorrect(this.getNumHelpAidsBeforeCorrect() + 1) ;

    }

    public void videoGiven (StudentState s) throws SQLException {
        problemState.getPojo().setNumHelpAidsGiven(problemState.getPojo().getNumHelpAidsGiven() + 1);
        smgr.getStudentState().setIsVideoShown(true);
        if (this.getTimeToFirstEvent() < 0) {
            problemState.getPojo().setTimeToFirstEvent(this.getProbElapsedTime());
        }
        if ( ! isProblemSolved() )
            problemState.getPojo().setNumHelpAidsBeforeCorrect(this.getNumHelpAidsBeforeCorrect() + 1) ;
    }

    public void exampleGiven(StudentState s, int exampleId) throws SQLException {
        problemState.getPojo().setNumHelpAidsGiven(problemState.getPojo().getNumHelpAidsGiven() + 1);
        setIsExampleShown(true);   // this says whether an example is seen within the topic.
        problemState.getPojo().setExamplesShown(problemState.getPojo().getExamplesShown() + 1); // counts how many examples given in the cur problem
        addExampleProblemGiven(exampleId);
        if (this.getTimeToFirstEvent() < 0) {
            problemState.getPojo().setTimeToFirstEvent(this.getProbElapsedTime());
        }
        if ( ! isProblemSolved() )
            problemState.getPojo().setNumHelpAidsBeforeCorrect(this.getNumHelpAidsBeforeCorrect() + 1) ;
    }


    public void hintGiven(StudentState state, Hint hint) throws SQLException {

        // check if the previous event was a hint and problem not solved yet.  If so, then update hint_time
        if ((this.isLastEvent(HINT_EVENT)) && (! isProblemSolved())) {
            long curr_hint_time = this.getTimeInHintsBeforeCorrect();
            long extra_hint_time = this.getProbElapsedTime() - this.getHintStartTime();
            problemState.getPojo().setTimeInHintsBeforeCorrect(curr_hint_time + extra_hint_time);
        }
        // now update things
        problemState.getPojo().setLastEvent(HINT_EVENT);
        if (hint == null) return;
        problemState.getPojo().setIdleTime(0);
        problemState.getPojo().setNumHintsGiven(problemState.getPojo().getNumHintsGiven() + 1);
        if (!isProblemSolved()) {
            problemState.getPojo().setNumHintsBeforeCorrect(problemState.getPojo().getNumHintsBeforeCorrect() + 1);
        }
        problemState.getPojo().setHintStartTime(this.getProbElapsedTime());
        boolean atEnd = hint.getLabel().equals(problemState.getPojo().getCurHint());
        problemState.getPojo().setCurHint(hint.getLabel());
        problemState.getPojo().setCurHintId(hint.getId());
        if (!isProblemSolved() && hint.getGivesAnswer())
            problemState.getPojo().setSolutionHintGiven(true);
        if (!isProblemSolved() && getNumHintsGivenOnCurProblem() == 1) {
            problemState.getPojo().setTimeToFirstHint(this.getProbElapsedTime());
        }
        else if (!isProblemSolved() && getNumHintsGivenOnCurProblem() == 2) {
            problemState.getPojo().setTimeToSecondHint(this.getProbElapsedTime());
        }
        else if (!isProblemSolved() && getNumHintsGivenOnCurProblem() == 3) {
            problemState.getPojo().setTimeToThirdHint(this.getProbElapsedTime());
        }
        if (this.getTimeToFirstEvent() < 0) {
            problemState.getPojo().setTimeToFirstEvent(this.getProbElapsedTime());
        }
        // for jeff
        if (this.getFirstEvent() == null) {
            problemState.getPojo().setFirstEvent(HINT_EVENT);
            problemState.getPojo().setTimeToFirstEvent(this.getProbElapsedTime());
        }
        // don't tamper with the problem history if this hint is requested after the problem has been solved or all hints are given
        if (!isProblemSolved() && !atEnd)
            smgr.getStudentModel().getStudentProblemHistory().hint(smgr, this.getProbElapsedTime(), hint.getGivesAnswer());

    }

    /**
     * Any action (e.g. drawing) that indicates the student is doing something
     * can result in an update of the state so that the idle counter is reset.
     * @throws java.sql.SQLException
     */
    public void studentNonIdleEvent () throws SQLException {
        problemState.getPojo().setIdleTime(0);
    }


    // the only reason a StudentState is a parameter is because this implements an interface requiring it
    public void studentAttempt(StudentState state, String answer, boolean isCorrect, long probElapsed) throws SQLException {
        // there are certain counters and stats that shouldn't be touched if this problem has already been solved and the student is
        // just clicking on other answers.
        boolean previouslySolved = isProblemSolved();
        if (!previouslySolved && isCorrect)
            topicState.setTopicProblemsSolved(topicState.getTopicProblemsSolved()+1);
        // I have no idea what this now time is coming from.   Its always 0 and
        // causes bad calculations
//        long now = this.getTime();
        long now = this.getProbElapsedTime();
        // check if the previous event was a hint and problem not solved yet.  If so, then update hint_time
        if ((this.isLastEvent(HINT_EVENT)) && (! isProblemSolved())) {
            long curr_hint_time = this.getTimeInHintsBeforeCorrect();
            long extra_hint_time = probElapsed - this.getHintStartTime();
            problemState.getPojo().setTimeInHintsBeforeCorrect(curr_hint_time + extra_hint_time);
        }
        this.setLastAnswer(answer);
        if ( ! isProblemSolved())
            problemState.getPojo().setNumAttempts(problemState.getPojo().getNumAttempts() + 1);
        long lastAttemptTime = this.getAttemptStartTime();
        problemState.getPojo().setAttemptStartTime(this.getTime());
        problemState.getPojo().setIdleTime(0);
        problemState.getPojo().setHintStartTime(-1); // once an attempt is given, we need to reset the last hint
//        this.setCurHint(null);
//        this.setCurHintId(-1);
        problemState.getPojo().setLastEvent(ATTEMPT_EVENT);
        if (this.getTimeToFirstAttempt() == -1)
            problemState.getPojo().setTimeToFirstAttempt(probElapsed);
        else if (this.getTimeToSecondAttempt() == -1)
            problemState.getPojo().setTimeToSecondAttempt(probElapsed);
        else if (this.getTimeToThirdAttempt() == -1)
            problemState.getPojo().setTimeToThirdAttempt(probElapsed);
        if (this.getTimeToFirstEvent() < 0) {
            problemState.getPojo().setTimeToFirstEvent(probElapsed);
        }
        // for jeff
        if (this.getFirstEvent() == null) {
            if (isCorrect)
                problemState.getPojo().setFirstEvent(CORRECT_ATTEMPT_EVENT);
            else
                problemState.getPojo().setFirstEvent(INCORRECT_ATTEMPT_EVENT);
            problemState.getPojo().setTimeToFirstEvent(probElapsed);
        }
        if (isCorrect) {
            // for jeff  - when correct, note the number of hints prior
            problemState.getPojo().setNumHintsBeforeCorrect(this.getNumHintsGivenOnCurProblem());
            problemState.getPojo().setProblemSolved(true);
            problemState.getPojo().setTimeToSolve(this.getProbElapsedTime());

        }
        else if (! isProblemSolved()) {
            problemState.getPojo().setNumMistakes(problemState.getPojo().getNumMistakes() + 1);
        }
        if (lastAttemptTime == -1 && !previouslySolved ) {
            problemState.getPojo().setAttemptStartTime(now);
            problemState.getPojo().setAvgTimeBetweenAttempts(probElapsed);
        } else if (!previouslySolved) {
            long diff = now - lastAttemptTime;
            double x = updateRunningAverage(problemState.getPojo().getAvgTimeBetweenAttempts(), diff);
            problemState.getPojo().setAvgTimeBetweenAttempts(x);
        }
        if (!previouslySolved)
            smgr.getStudentModel().getStudentProblemHistory().attempt(smgr,isCorrect,probElapsed,problemState.getPojo().getNumAttempts());

    }

    private double updateRunningAverage(double runningAvg, double curVal) {
        if (problemState.getPojo().getNumAttempts() == 0) {
            // when a variable's curVal has an initialization value of -1 , we want to return 0 for the avg.
            if (curVal < 0)
                return 0.0;
            else
                return curVal;
        } else if (problemState.getPojo().getNumAttempts() == 1)
            return curVal;
        else
            return (runningAvg * problemState.getPojo().getNumAttempts() + curVal) / (problemState.getPojo().getNumAttempts() + 1);

    }






































    /**
     * A utility that clears student state so that the tutor hut can be re-entered as if the student
     * has never used the tutor hut.    The WoAdmin tools allow teachers/admins to make this happen.
     * @throws java.sql.SQLException
     */

    public void clearTutorHutState() throws SQLException {
        ProblemState.clearState(conn, objid);
        TopicState.clearState(conn, objid);
        SessionState.clearState(conn, objid);
        InterventionState.clearState(conn,objid);
        WorkspaceState.clearState(conn,objid);
    }

    public WorkspaceState getWorkspaceState () {
        return this.workspaceState;
    }

    public LessonState2 getLessonState () {
        return this.lessonState;
    }

    public TopicState getTopicState() {
        return topicState;
    }

    public SessionState getSessionState () {
        return sessionState;
    }

    public PrePostState getPrePostState() {
        return ppState;
    }


    public boolean curTopicHasEasierProblem() {
        return topicState.curTopicHasEasierProblem();
    }

    public boolean curTopicHasHarderProblem() {
        return topicState.curTopicHasHarderProblem();
    }


    public void setCurTopicHasEasierProblem(boolean b) throws SQLException {
        topicState.setCurTopicHasEasierProblem(b);
    }

    public void setCurTopicHasHarderProblem(boolean b) throws SQLException {
        topicState.setCurTopicHasHarderProblem(b);
    }

    public String getNextProblemDesiredDifficulty() {
       return sessionState.getNextProblemDesiredDifficulty();
    }

    public void setNextProblemDesiredDifficulty(String diff) throws SQLException {
        sessionState.setNextProblemDesiredDifficulty(diff);
    }

    public int getNumProbsSinceLastIntervention() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    public void setNumProbsSinceLastIntervention(int i) {
        //To change body of created methods use File | Settings | File Templates.
    }


    public int getCurProblemIndexInLesson() {
        return lessonState.getCurProblemIndex();
    }

    public void setCurProblemIndexInLesson(int curProblemIndexInLesson) {
        lessonState.setCurProblemIndex(curProblemIndexInLesson);
    }

}
