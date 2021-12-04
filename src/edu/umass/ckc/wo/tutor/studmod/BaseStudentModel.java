package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemStats;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.StudentEffort;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the variables for a basic student model.  In combination with the StudentState
 * this object allows a ProblemSelector or HintSelector to make decisions.
 * <p/>
 * Student modeling variables that support a particular strategy of tutoring (e.g. IRT or motivation based)
 * should not be added here.  Subclasses of this should be built to support the particular
 * strategies.  A new pedagogy can then be put in the pedagogies.xml file.   The pedagogy configures
 * the selectors and student model to use.
 * 
 * Frank	12-26-20	Issue #329 use Multi-lingual topic names
 * Frank	12-04-2021	Issue #555 - TopicMastery computation incorrectly updates when student abandons a problem without making any attempts by going to My Progress Page which ends the problem.  Added test in endProblem(), to only update if number of mistakes is > 0.
 */
public class BaseStudentModel extends StudentModel {

    private static Logger logger = Logger.getLogger(BaseStudentModel.class);

    /* These are being used by reports system to access woproperty */
    /* The reports that use woproperty are now going to be broken because this info is now in basestudentmodel  table */
    public static final String PRE_TEST_NUM_CORRECT = "pretestNumCorrect";
    public static final String POST_TEST_NUM_CORRECT = "posttestNumCorrect";
    public static final String PRE_TEST_NUM_INCORRECT = "pretestNumIncorrect";
    public static final String POST_TEST_NUM_INCORRECT = "posttestNumIncorrect";
    private static final String  TABLE_NAME= "baseStudentModel";
    public static final String[] TABLE_COLS = new String[] {"avgTimeInProb","avgTimeInAssistedProb",
            "avgAttemptsInProb","avgAttemptsInAssistedProb","avgTimeBetweenAttempts","avgHintsGivenPerProb",
            "avgHintsGivenPerAssistedProb","pretestNumCorrect","posttestNumCorrect","pretestNumIncorrect",
            "posttestNumIncorrect","numProbsReceivedAssistance","numProbsSeen","numProbsSolved","numHintsTotal",
            "numHelpAidsTotal","numHintsAfterSolveTotal","gender","avgSolveTime","totalSolveTime","avgMistakesInProblem",
            "totalProbMistakes","avgHintTime","totalHintTime","totalTimeBetweenAttempts","totalAttempts"};

    protected StudentEffort effort;

    /* NOTE WELL:  DO NOT CHANGE instance variable names.  They must be same as database table that corresponds with this class */
    /* BEGINNING OF INSTANCE VARS CORRESPONDING TO DATABASE TABLE BASESTUDENTMODEL */
    public double avgTimeInProb=0; // must be updated when user exits a problem
    public double avgTimeInAssistedProb=0; // must be updated when user exits an assisted problem
    public double avgAttemptsInProb=0;        // must be updated when user exits a problem
    public double avgAttemptsInAssistedProb=0;   // must be updated when user exits an assisted problem
    public double avgTimeBetweenAttempts=0;     //  updated when user exits a problem  (student state maintains avg for a problem)
    public int totalAttempts=0;     //  updated on attempt
    public int totalProbMistakes=0;     //  updated on attempt
    public double avgHintsGivenPerProb=0;       // must be updated when user exits a problem
    public double avgHintsGivenPerAssistedProb=0;     // must be updated after each hint is given
    public int pretestNumCorrect=0;               // updated as each pre-test problem is given
    public int posttestNumCorrect=0;               // updated as each post-test problem is given
    public int pretestNumIncorrect=0;               // updated as each pre-test problem is given
    public int posttestNumIncorrect=0;               // updated as each post-test problem is given
    public int numProbsReceivedAssistance=0;           // must be updated when user exits a problem
    public int numProbsSeen=0;                        // must be updated when user exits a problem
    public int numProbsSolved=0;                        // must be updated when user exits a problem
    public int numHintsTotal=0;                       // updated on hintGiven
    public int numHelpAidsTotal=0;
    public int numHintsAfterSolveTotal=0;             // updated on hintGiven
    public double avgSolveTime = 0;                  // must be updated when user exits a problem
    public double avgMistakesInProblem = 0;          // must be updated when user exits a problem
    public long totalHintTime = 0;    // updated on Attempt or NextProblem after a hintGiven
    public long totalSolveTime = 0;    // updated on correct Attempt
    private long totalTimeBetweenAttempts = 0;    // updated on Attempt
    public double avgHintTime = 0; // totalHintTime / numProblemsSeen
    public String gender="unknown";
    protected int sessId;
    protected SessionManager smgr;


     /* END OF INSTANCE VARS CORRESPONDING TO DATABASE TABLE BASESTUDENTMODEL */



    public int classId;
    private static final String INITIAL_TOPIC_MASTERY_ESTIMATE = "0.5";
    public static final double INITIAL_TOPIC_MASTERY_ESTIMATE_FL = 0.1;
    public static final double INITIAL_STANDARD_MASTERY_ESTIMATE = 0.1;



    private List<TopicMastery> topicMasteryLevels;




    public BaseStudentModel() {
    }

    /** This is a constructor called only through Java reflection (e.g. see BasePedagogicalModel constructor
     *
     * @param smgr
     * @throws Exception
     */
    public BaseStudentModel (SessionManager smgr) throws Exception {
        this(smgr.getConnection());
        this.sessId= smgr.getSessionNum();
        this.smgr = smgr;
    }

    public BaseStudentModel(Connection conn) throws SQLException {
        this.conn = conn;
        this.heuristic = new StudentModelMasteryHeuristic(conn);
        this.problemHistory = new StudentProblemHistory();
        dbWorker = new DbStateTableMgr(conn);

    }



    public int getNumProbsSeen () {
        return this.numProbsSeen;
    }



    public double getAvgMistakesInProblem() {
        return avgMistakesInProblem;
    }

    public double getAvgHintTime() {
        return avgHintTime;
    }

    public double getAvgSolveTime() {
        return avgSolveTime;
    }

    /**
     * Load all student model key/value pairs.
     * Called right after StudentModel is constructed.
     *
     * @param studId
     * @param classId
     * @throws java.sql.SQLException
     */
    public void init(WoProps props, int studId, int classId) throws SQLException {
        this.objid = studId;
        this.classId = classId;
        try {
            dbWorker.load(studId,this, TABLE_NAME, TABLE_COLS, BaseStudentModel.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IntrospectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // load topic mastery levels must set up initial levels if they are not present.   It also must make
        // sure that there is a level for every topic in the system (topics get added dynamically so it must keep a level for every topic)
        loadTopicMasteryLevels();
        // creates the StudentProblemHistory object which contains a list of records (one for each encounter with a
        // problem).
        this.problemHistory = new StudentProblemHistory(conn,studId);
        this.effort = problemHistory.getEffort(this.sessId);
    }

    // If there are no levels,  then we insert them into the studenttopicMastery table

    // If there are levels, test to see if there are the same number of rows in problemGroup table as in the studentTopicMastery

    //  If the # is different (probably a new topic was added to problemgroup) then we need to add some new rows to studentTopicMastery for
    // the new rows in problemgroup
    private void loadTopicMasteryLevels() throws SQLException {
        List<TopicMastery> levels = DbUser.getTopicMasteryLevels(this.getConnection(),this.objid);
        List<Topic> topics = DbTopics.getAllTopics(this.getConnection(),this.objid);
        if (levels.size() == 0) {
            List<TopicMastery> result = new ArrayList<TopicMastery>();
            // insert the initial estimate for all topics
            for (Topic t: topics) {
                TopicMastery mast = new TopicMastery(t,INITIAL_TOPIC_MASTERY_ESTIMATE_FL, false);
                result.add(mast);
                DbUser.insertTopicMastery(conn,this.objid,mast);
            }
            this.topicMasteryLevels = result;
        }
        else if (levels.size() != topics.size()) {
            // SAFE ASSUMPTION:  No topic in the problemgroup has been deleted
            // for each topic if there is no level, insert one with initial estimate
            List<TopicMastery> result = new ArrayList<TopicMastery>();
            for (Topic t: topics) {
                TopicMastery tm=null;
                // if there is a level for this topic, we use it, otherwise we create one with initial estimate
                if ((tm=findLevel(levels,t)) == null) {
                    tm= new TopicMastery(t,INITIAL_TOPIC_MASTERY_ESTIMATE_FL, false);
                    result.add(tm);
                    DbUser.insertTopicMastery(conn,this.objid,tm);
                }
                else result.add(tm);
            }
            this.topicMasteryLevels = result;
        }
        else this.topicMasteryLevels = levels;
    }

    private TopicMastery findLevel(List<TopicMastery> levels, Topic t) {
        for (TopicMastery m: levels)
            if (m.getTopic().getId() == t.getId())
                return m;
        return null;
    }



    /**
     * Write the updated values back to the database.
     *
     * @return boolean
     */
    public boolean save() throws SQLException {
        logger.debug("Save BEGIN");
        long n =System.currentTimeMillis() ;
        dbWorker.save(this,this.objid, TABLE_NAME, TABLE_COLS, BaseStudentModel.class);
        smgr.getStudentState().save();
        // because the topicmasterylevels are updated in the db by this classes access method, we don't need to write them when the
        // student model is saved.
        logger.debug("Save END took:" + (System.currentTimeMillis()-n));
        return true;


    }




    public void updateEmotionalState(SessionManager smgr, long probElapsedTime, long elapsedTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // *****************  User events that trigger a call to the student model ***********
    // ***********************************************************************************


    /**
     * Called by ProblemSelector objects whenever it selects a new Problem object to show.  This should not be called for
     * Motivational items; they are considered to be Interventions and should be passed to the interventionGiven method below.
     *
     * @param state
     * @param p
     * @throws java.sql.SQLException
     */
    public void newProblem(StudentState state, Problem p) throws SQLException {
        state.newProblem(null, p);
    }

    /**
     * When a BeginProblemEvent comes in, we simply update the student state.   The student model
     * is not affected.
     *
     *
     * @param smgr
     * @param e
     * @throws java.sql.SQLException
     */
    public void beginProblem(SessionManager smgr, BeginProblemEvent e) throws SQLException {
        this.numProbsSeen++;
        long t = System.currentTimeMillis();
        smgr.getStudentState().beginProblem(null, e);
//        System.out.println("In SM.beginProblem, after studentState.beginProblem " + (System.currentTimeMillis() - t));
        if (CollaborationManager.requestExists(smgr.getStudentId())) {
            smgr.setCollaboratingWith(CollaborationManager.getRequestedPartner(smgr.getStudentId()));
//           System.out.println("In SM.beginProblem, after CollaborationManager.requestExists " + (System.currentTimeMillis() - t));
        }
        this.problemHistory.beginProblem(smgr,e);
//        System.out.println("In SM.beginProblem, after problemHistory.beginProblem " + (System.currentTimeMillis() - t));

    }


    /**
     * Called by ProblemSelector objects whenever a Problem is completed (user presses the nextProblem button).  This should not be called for
     * Motivational items; they are considered to be Interventions and should be passed to the interventionGiven method below.
     *
     * @param  smgr
     * @throws java.sql.SQLException
     */
    public void endProblem(SessionManager smgr, int studId, long probElapsedTime, long elapsedTime) throws Exception {
        StudentState state = smgr.getStudentState();
        // If the student forced this problem + topic, then topic is stored in the state until this prob ends
        int topicId = (state.getStudentSelectedTopic() != -1) ? state.getStudentSelectedTopic() : state.getCurTopic();
        int probId =-1, mistakes=0;
        boolean isCorrect=false;
        // demos and examples send EndProblem events when done.   Don't want to update certain things if this is the case.
        boolean isExample = Problem.isExampleOrDemo(state.getCurProblemMode());
        long timeInLastProb = state.getProbElapsedTime();
        if (!isExample) {
            if (state.getNumHintsGivenOnCurProblem() > 0)
                this.numProbsReceivedAssistance++;


        // running averages that must be computed before numProbsSeen is incremented

            this.avgTimeInProb = perProbAvg(avgTimeInProb, timeInLastProb);

            // we update the avgTimeBetweenAttempts on each problem - The StudentState maintains an avgTimeBetweeAttempts
            // for the current problem.  We don't do the update if no attempts were made on the last problem
//        if (state.getCurProblemAvgTimeBetweenAttempts() > 0)
//            this.avgTimeBetweenAttempts = perProbAvg(avgTimeBetweenAttempts, state.getCurProblemAvgTimeBetweenAttempts());
            avgHintsGivenPerProb = (numProbsSeen > 0) ? ((double) numHintsTotal / (double) numProbsSeen) : 0;

            if (state.getTimeToSolve() > 0)
                this.numProbsSolved++;
                // if student fails to solve problem add in full time spent in problem to total solve time
                // this insures that the avgSolveTime increases when problems are not solved.
            else
                this.totalSolveTime += state.getProbElapsedTime();
            this.avgSolveTime = (numProbsSeen > 0) ? ((double) totalSolveTime / (double) numProbsSeen) : 0;
            this.avgMistakesInProblem = (numProbsSeen > 0) ? ((double) totalProbMistakes / (double) numProbsSeen) : 0;
            // if student received hints on last problem update avgSecondsInAssistedProblems
            if (state.getNumHintsGivenOnCurProblem() > 0)
                this.avgTimeInAssistedProb = perAssistedProbAvg(avgTimeInAssistedProb, timeInLastProb);
            this.avgAttemptsInProb = perProbAvg(avgAttemptsInProb, state.getNumAttemptsOnCurProblem());
            // update variables that are about problems with assistance
            if (state.getNumHintsGivenOnCurProblem() > 0) {
                this.avgAttemptsInAssistedProb = perAssistedProbAvg(avgAttemptsInAssistedProb, state.getNumAttemptsOnCurProblem());
                avgHintsGivenPerAssistedProb = perAssistedProbAvg(avgHintsGivenPerAssistedProb, state.getNumHintsGivenOnCurProblem());

            }
            // increments totalHintTime if student was previously in a hint .
            // N.B. The above may be true but if user went to MPP probElapsedTime will be 0 resulting in a negative number.
            if (state.isLastEvent(StudentState.HINT_EVENT)) {
                long probElapsed = state.getProbElapsedTime();
                long hintStart = state.getHintStartTime();
                if ((probElapsed - hintStart) > 0)
                    totalHintTime += probElapsed - hintStart;
            }
            this.avgHintTime = (numProbsSeen > 0) ? (totalHintTime / numProbsSeen) : 0;
            // update these variables after computations about the last problem are made
            probId = state.getCurProblem();

            int numHelpAids = state.getNumHelpAidsBeforeCorrect();
            int numHints = state.getNumHintsBeforeCorrect();
            mistakes = state.getNumMistakesOnCurProblem();
            isCorrect = state.isProblemSolved();
            if ((!isCorrect) && (mistakes > 0)) {
                // We only update mastery if the problem has not been seen recently (either never seen before or when it was is outside
                // the problemReuseInterval)
                PedagogicalModelParameters pmParams = smgr.getPedagogicalModel().getParams();
                if (!getStudentProblemHistory().answeredRecently(probId, smgr.getSessionId(),
                        pmParams.getProblemReuseIntervalDays(), pmParams.getProblemReuseIntervalSessions())) {

                    this.topicUpdate(state, probId, topicId, numHints + numHelpAids, isCorrect, mistakes, probElapsedTime);
                    this.standardUpdate(state, probId, numHints + numHelpAids, isCorrect, mistakes, probElapsedTime);
                }
            }
        }
        state.endProblem(smgr, studId, probElapsedTime, elapsedTime);  // this call resets state.studentSelectedTopic to -1
        this.problemHistory.endProblem(smgr,problemHistory.getCurProblem(), topicId);
        this.effort = this.problemHistory.getEffort(smgr.getSessionNum());

        // Only update the statistics about a problem if the user is allowed to
        if (!isExample && DbUser.isUpdateStats(conn,studId) )  {
            if (state.getCurProblemMode() != null && state.getCurProblemMode().equals(Problem.PRACTICE))
                // TODO temporary protection to make sure we don't try to update stats on an empty problem
                //  The CommonCOre ped model has a bad interaction with this that causes an error.   Because
                // when a CU inits it sets curProb to -1 and then this can't update the stats of the last problem.
                if (probId > 0)
                    updateProblemStatistics(probId,isCorrect,mistakes,state.getNumHintsGivenOnCurProblem(),probElapsedTime);
        }
    }

    /**
     * Knuth's algorithm for updating running stddev:
     * Sk = Sk-1 + (Xk - Mk-1)*(Xk - Mk)
     * Mk = Mk-1 + (Xk - Mk-1) / k
     * Note that Sk = stdDev^2 * (n-1)
     * @param probId
     * @param isCorrect
     * @param nMistakes
     * @param numHints
     * @param probElapsedTime
     * @throws SQLException
     */
    private void updateProblemStatistics(int probId, boolean isCorrect, int nMistakes, int numHints, long probElapsedTime) throws SQLException {
        int numAttempts= nMistakes + (isCorrect ? 1 : 0);
        long timeSecs = probElapsedTime / 1000;
        ProblemStats stats = DbProblem.getStats(this.conn,probId);
        if (stats == null)
            stats = new ProblemStats(probId);
        double n = stats.getN();
        stats.setN(stats.getN()+1);
        n++;

        double oldMeanAttempts = stats.getMeanAttempts();
        double oldMeanHints = stats.getMeanHints();
        double oldMeanTime = stats.getMeanTimeSecs();
        double meanAttempts,meanHints,meanTime;
        double skHints = stats.getVarianceNumeratorHints();
        double skAttempts = stats.getVarianceNumeratorAttempts();
        double skTime = stats.getVarianceNumeratorTime();

        stats.setMeanAttempts(meanAttempts = (numAttempts + oldMeanAttempts*(n-1)) / n);
        stats.setMeanHints(meanHints = (numHints + oldMeanHints*(n-1)) / n);
        stats.setMeanTimeSecs(meanTime = (timeSecs + oldMeanTime*(n-1)) / n);

        // These calculations are numerators of variance (have to divide by n-1 to get real variance

        skAttempts = skAttempts + (numAttempts - oldMeanAttempts)*(numAttempts - meanAttempts);
        stats.setVarianceNumeratorAttempts(skAttempts);

        skHints = skHints + (numHints - oldMeanHints)*(numHints - meanHints);
        stats.setVarianceNumeratorHints(skHints);

        skTime = skTime + (timeSecs - oldMeanTime)*(timeSecs - meanTime);
        stats.setVarianceNumeratorTime(skTime);
        DbProblem.updateStats(conn, stats);
    }

    public StudentEffort getEffort () {
        return this.effort;
    }


    public void videoGiven(StudentState state) throws SQLException {
        this.numHelpAidsTotal++;
        state.videoGiven(null);  // set/initialize student state variables based on hintSelected event
        EffortHeuristic effortComputer = new EffortHeuristic();
        String effort = effortComputer.computeEffort(state);
        getStudentProblemHistory().getCurProblem().setEffort(effort);
        this.problemHistory.saveCurProbEffort(conn,smgr.getStudentId(),effort);
    }

    public void exampleGiven(StudentState state, int exampleId) throws SQLException {
        this.numHelpAidsTotal++;
        state.exampleGiven(null, exampleId);  // set/initialize student state variables based on hintSelected event
        EffortHeuristic effortComputer = new EffortHeuristic();
        String effort = effortComputer.computeEffort(state);
        getStudentProblemHistory().getCurProblem().setEffort(effort);
        this.problemHistory.saveCurProbEffort(conn,smgr.getStudentId(),effort);
    }



    public void hintGiven(StudentState state, Hint hint) throws SQLException {
        // a previous hint was started and now the student is asking for another one.  Increment total hint time
        if (state.isLastEvent(StudentState.HINT_EVENT)) {
            this.totalHintTime += state.getProbElapsedTime() - state.getHintStartTime();
        }
        if (hint == null) return;
        this.numHintsTotal++;
        this.numHelpAidsTotal++;

        if (state.isProblemSolved())
            this.numHintsAfterSolveTotal++;

        state.hintGiven(null, hint);  // set/initialize student state variables based on hintSelected event
        EffortHeuristic effortComputer = new EffortHeuristic();
        String effort = effortComputer.computeEffort(state);
        getStudentProblemHistory().getCurProblem().setEffort(effort);
        this.problemHistory.saveCurProbEffort(conn,smgr.getStudentId(),effort);
    }


    /**
     * Called by PedagogicalModel objects whenever a student attempts to answer a question
     *
     * @param state
     * @param answer
     * @param isCorrect
     * @throws java.sql.SQLException
     */
    public void studentAttempt(StudentState state, String answer, boolean isCorrect, long probElapsed) throws SQLException {

        // If it has been solved
        // there are a bunch of counters and statistics that we don't want to alter even though the student is allowed to keep working
        // with the problem
        boolean isSolved = state.isProblemSolved();
        if (!isSolved && !isCorrect)
            this.totalProbMistakes++;
        // previous event was hint. now the student is attempting.  Increment total hint time
        if (!isSolved && state.isLastEvent(StudentState.HINT_EVENT)) {
            this.totalHintTime += probElapsed - state.getHintStartTime();
        }
        long lastAttemptTime = state.getAttemptStartTime() - state.getHintStartTime();
        long timebtw;
        long now = state.getTime(); // I don't know what this is for.
        if (lastAttemptTime == 0)
            timebtw = 0;
        else timebtw = probElapsed - lastAttemptTime;
        if (!isSolved && isCorrect)
            this.totalSolveTime += probElapsed;
        if (!isSolved)
            setTotalTimeBetweenAttempts(getTotalTimeBetweenAttempts() + timebtw);
        assert getTotalTimeBetweenAttempts() > 0;
        if (!isSolved)
            this.totalAttempts++;
        if (!isSolved)
            this.avgTimeBetweenAttempts = totalTimeBetweenAttempts / totalAttempts;

        int probId = state.getCurProblem();
        // If the student forced this problem + topic, then topic is stored in the state until this prob ends
        int topicId = (state.getStudentSelectedTopic() != -1) ? state.getStudentSelectedTopic() : state.getCurTopic();
        int numHelpAids = state.getNumHelpAidsBeforeCorrect() ;
        int numHints = state.getNumHintsBeforeCorrect();
        int mistakes = state.getNumMistakesOnCurProblem() ;
        // When the student is correct,   we update the mastery stats for this student immediately so that
        // if they see an MPP or move to another topic by force,  the mastery is correctly set.
        if (isCorrect)  {
            // We only update mastery if the problem has not been seen recently (either never seen before or when it was is outside
            // the problemReuseInterval)
            PedagogicalModelParameters pmParams = smgr.getPedagogicalModel().getParams();
            if (!getStudentProblemHistory().answeredRecently(probId,smgr.getSessionId(), pmParams.getProblemReuseIntervalDays(),pmParams.getProblemReuseIntervalSessions())) {
                this.topicUpdate(state, probId, topicId, numHints + numHelpAids, isCorrect, mistakes, probElapsed);
                this.standardUpdate(state, probId, numHints + numHelpAids, isCorrect, mistakes, probElapsed);
            }
        }
        state.studentAttempt(null, answer, isCorrect, probElapsed);  // set/initialize student state variables based on student attempt event
        EffortHeuristic effortComputer = new EffortHeuristic();
        String effort = effortComputer.computeEffort(state);
        getStudentProblemHistory().getCurProblem().setEffort(effort);
        this.problemHistory.saveCurProbEffort(conn,smgr.getStudentId(),effort);
    }

    /**
     * Called whenever a PedagogicalModel or ProblemSelector chooses to show an intervention (e.g. a graph motivator,
     * a use-the-help-button-ya-dumb-ass dialog, etc)
     *
     * @param state
     * @param intervention
     * @throws java.sql.SQLException
     */
    public void interventionGiven(StudentState state, Intervention intervention) throws SQLException {
        // todo more todo here
        state.interventionGiven(null, intervention);
    }

    // ************* Student Model accessors ******************************
    // ********************************************************************


    private double perProbAvg(double runningAvg, double curVal) {
        if (numProbsSeen == 1) {
            // when a variable's curVal has an initialization value of -1 , we want to return 0 for the avg.
            if (curVal < 0)
                return 0.0;
            else
                return curVal;
        }
        else
            return (runningAvg * (numProbsSeen - 1) + curVal) / numProbsSeen;
    }


    private double perAssistedProbAvg(double runningAvg, double curVal) {
        if (numProbsReceivedAssistance == 1) {
            if (curVal < 0)
                return 0;
            else
                return curVal;
        }
        else
            return (runningAvg * (numProbsReceivedAssistance - 1) + curVal) / numProbsReceivedAssistance;
    }


    public void incrementPreTestNumCorrect() throws SQLException {
        this.pretestNumCorrect++;
        dbWorker.updateIntColumn(this.objid, PRE_TEST_NUM_CORRECT, this.pretestNumCorrect, TABLE_NAME);
    }

    public void incrementPreTestNumIncorrect() throws SQLException {
        this.pretestNumIncorrect++;
        dbWorker.updateIntColumn(this.objid, PRE_TEST_NUM_INCORRECT, this.pretestNumCorrect, TABLE_NAME);
    }


    public void incrementPostTestNumCorrect() throws SQLException {
        this.posttestNumCorrect++;
        dbWorker.updateIntColumn(this.objid, POST_TEST_NUM_CORRECT, this.pretestNumCorrect, TABLE_NAME);
    }

    public void incrementPostTestNumIncorrect() throws SQLException {
        this.posttestNumIncorrect++;
        dbWorker.updateIntColumn(this.objid, POST_TEST_NUM_INCORRECT, this.pretestNumCorrect, TABLE_NAME);
    }


    public int getPretestNumCorrect() {
        return this.pretestNumCorrect;
    }

    public int getPretestNumTotal() {
        return this.pretestNumIncorrect + this.pretestNumCorrect;
    }

    public int getPosttestNumCorrect() {
        return this.posttestNumCorrect;
    }

    public String getStudentName(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select " +name+ " from Student where id=" + this.objid;
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString(name);
                if (name.equals(""))
                    return "student";
            }
        } catch (SQLException e) {
        }
        return name;
    }

    public String getStudentFirstName () {
        return getStudentName("fname");
    }

    public String getStudentLastName () {
        return getStudentName("lname");
    }



    /**
     * Update the student model topic mastery for the given topicId.   The update depends on whether the last
     * problem was correctly answered or not and probability of slip (incorrect) and probability of guessing (.25 for multi-choice problem)
     * @param probID
     * @param topicID
     * @param numHelpAids
     * @return
     * @throws java.sql.SQLException
     */
    public double topicUpdate(StudentState state, int probID, int topicID, int numHelpAids, boolean isCorrect, int mistakes, long probElapsed) throws SQLException {
        double topicMastery = getTopicMastery(topicID);
        int numPracticeProbsSeenInTopicAcrossSessions = smgr.getStudentModel().getStudentProblemHistory().getNumPracticeProbsSeenInTopicAcrossSessions(topicID);
//        this.heuristic = new StudentModelMasteryHeuristic(conn);
        this.heuristic = new StudentModelMasteryHeuristic(conn);
        long ttfa = state.getTimeToFirstAttempt();
        topicMastery = this.heuristic.computeTopicMastery(ttfa,topicMastery,probID,
                topicID,numHelpAids,isCorrect,mistakes, numPracticeProbsSeenInTopicAcrossSessions, state.getCurProblemMode());
        setTopicMasteryLevel(topicID,topicMastery); // alter the mastery level in the db and in the SM instance variable for mastery levs
//        this.setProp(objid,var,topicMastery);
        return topicMastery;
    }

    public void standardUpdate(StudentState state, int probID, int numHelpAids, boolean isCorrect, int mistakes, long probElapsed) throws SQLException {
        Problem p = ProblemMgr.getProblem(probID);
        if (p != null && p.getStandards() != null) {
            for (CCStandard std: ProblemMgr.getProblem(probID).getStandards())  {
                StudentStandardMastery m = DbStudentStandardMastery.getMastery(conn,smgr.getStudentId(),std.getCode());
                double mastery = INITIAL_STANDARD_MASTERY_ESTIMATE;
                int numProbsSeen = 1;
                long ttfa = state.getTimeToFirstAttempt();
                this.heuristic = new StudentModelMasteryHeuristic(conn);
                // will be null if the standard has not been encountered previously
                if (m != null) {
                    mastery= m.getVal();
                    numProbsSeen = m.getNumProbs() + 1;

                }
                mastery = this.heuristic.computeStandardMastery(ttfa, mastery, probID,
                        numHelpAids, isCorrect, mistakes, numProbsSeen, state.getCurProblemMode());

                if (m == null)
                    DbStudentStandardMastery.insertMastery(conn,smgr.getStudentId(),std.getCode(),mastery,numProbsSeen);
                else
                    DbStudentStandardMastery.updateMastery(conn,smgr.getStudentId(),std.getCode(),mastery,numProbsSeen);
            }
        }

    }


    /**
     * sets a new mastery level for the topic in both this class and in the database.
     * @param topicId
     * @param topicMasteryLevel
     * @throws java.sql.SQLException
     */
    public void setTopicMasteryLevel (int topicId, double topicMasteryLevel) throws SQLException {
        for (TopicMastery m: this.topicMasteryLevels) {
            if (m.getTopic().getId() == topicId) {
                m.setMastery(topicMasteryLevel);
                DbUser.updateTopicMasteryLevel(conn,this.objid,m);
            }
        }
    }

    /**
     * Get the topic mastery level given the topic
     * @param topicId
     * @return
     * @throws java.sql.SQLException
     */
    public double getTopicMastery(int topicId) throws SQLException {
        for (TopicMastery m: this.topicMasteryLevels) {
            if (m.getTopic().getId() == topicId) {
                return m.getMastery();
            }
        }
        return INITIAL_TOPIC_MASTERY_ESTIMATE_FL;
    }

    public boolean getTopicEntered(int topicId) throws SQLException {
        for (TopicMastery m: this.topicMasteryLevels) {
            if (m.getTopic().getId() == topicId) {
                return m.isEntered();
            }
        }
        return false;
    }

    /**
     * Return a list of TopicMastery objects.   N.B.  this only returns one for each topic that is in the classlessonplan.
     * So if a topic is worked on by the student and then the topic is deactivated, it will not be included
     * @return
     * @throws java.sql.SQLException
     */
    public List<TopicMastery> getTopicMasteries () throws SQLException {
        List<Topic> topics = DbTopics.getClassActiveTopics(this.getConnection(),this.classId);
        List<TopicMastery> mastLevs = new ArrayList<TopicMastery>();
        for (Topic t: topics) {
            mastLevs.add(new TopicMastery(t,getTopicMastery(t.getId()), getTopicEntered(t.getId())));
        }
        return mastLevs;
    }




    public String toString() {
        /*
        StringBuffer sb = new StringBuffer();
        sb.append("avgTimeInProb" + "=" + this.avgTimeInProb + "\n");
        sb.append("avgTimeInAssistedProb" + "=" + this.avgTimeInAssistedProb + "\n");
        sb.append(AVG_ATTEMPTS_IN_PROBLEM + "=" + this.avgAttemptsInProb + "\n");
        sb.append(AVG_ATTEMPTS_IN_ASSISTED_PROB + "=" + this.avgAttemptsInAssistedProb + "\n");
        sb.append(AVG_TIME_BETWEEN_ATTEMPTS + "=" + this.avgTimeBetweenAttempts + "\n");
        sb.append(AVG_HINTS_GIVEN_PER_PROB + "=" + this.avgHintsGivenPerProb + "\n");
        sb.append(AVG_HINTS_GIVEN_PER_ASSISTED_PROB + "=" + this.avgHintsGivenPerAssistedProb + "\n");
//        sb.append( PRE_TEST_NUM_CORRECT+"="+ this.preTestNumCorrect +"\n");
//        sb.append( PRE_TEST_NUM_INCORRECT+"="+ this.preTestNumIncorrect +"\n");
        sb.append(NUM_PROBS_RECEIVED_ASSISTANCE + "=" + this.numProbsReceivedAssistance + "\n");
        sb.append(NUM_PROBS_TOTAL + "=" + this.numProbsSeen + "\n");
        sb.append(NUM_PROBS_SOLVED + "=" + this.numProbsSolved + "\n");
        sb.append(NUM_HINTS_TOTAL + "=" + this.numHintsTotal + "\n");
        sb.append(NUM_HINTS_AFTER_SOLVE_TOTAL + "=" + this.numHintsAfterSolveTotal + "\n");
        sb.append(AVG_SOLVE_TIME + "=" + this.avgSolveTime + "\n");
        sb.append(AVG_MISTAKES_IN_PROBLEM + "=" + this.avgMistakesInProblem + "\n");
        sb.append(AVG_HINT_TIME + "=" + this.avgHintTime + "\n");
        sb.append(TOTAL_HINT_TIME + "=" + this.totalHintTime + "\n");
        sb.append(TOTAL_ATTEMPTS + "=" + this.totalAttempts + "\n");
        sb.append(TOTAL_TIME_BETWEEN_ATTEMPTS + "=" + this.totalTimeBetweenAttempts + "\n");
//        sb.append( GENDER+"="+ this.gender);
        return sb.toString();
     */
        return "";

    }


    public double getAvgTimeInProb() {
        return avgTimeInProb;
    }

    public void setAvgTimeInProb(double avgTimeInProb) {
        this.avgTimeInProb = avgTimeInProb;
    }

    public double getAvgTimeInAssistedProb() {
        return avgTimeInAssistedProb;
    }

    public void setAvgTimeInAssistedProb(double avgTimeInAssistedProb) {
        this.avgTimeInAssistedProb = avgTimeInAssistedProb;
    }

    public double getAvgAttemptsInProb() {
        return avgAttemptsInProb;
    }

    public void setAvgAttemptsInProb(double avgAttemptsInProb) {
        this.avgAttemptsInProb = avgAttemptsInProb;
    }

    public double getAvgAttemptsInAssistedProb() {
        return avgAttemptsInAssistedProb;
    }

    public void setAvgAttemptsInAssistedProb(double avgAttemptsInAssistedProb) {
        this.avgAttemptsInAssistedProb = avgAttemptsInAssistedProb;
    }

    public double getAvgTimeBetweenAttempts() {
        return avgTimeBetweenAttempts;
    }

    public void setAvgTimeBetweenAttempts(double avgTimeBetweenAttempts) {
        this.avgTimeBetweenAttempts = avgTimeBetweenAttempts;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public int getTotalProbMistakes() {
        return totalProbMistakes;
    }

    public void setTotalProbMistakes(int totalProbMistakes) {
        this.totalProbMistakes = totalProbMistakes;
    }

    public double getAvgHintsGivenPerProb() {
        return avgHintsGivenPerProb;
    }

    public void setAvgHintsGivenPerProb(double avgHintsGivenPerProb) {
        this.avgHintsGivenPerProb = avgHintsGivenPerProb;
    }

    public double getAvgHintsGivenPerAssistedProb() {
        return avgHintsGivenPerAssistedProb;
    }

    public void setAvgHintsGivenPerAssistedProb(double avgHintsGivenPerAssistedProb) {
        this.avgHintsGivenPerAssistedProb = avgHintsGivenPerAssistedProb;
    }



    public void setPretestNumCorrect(int nc) {
        this.pretestNumCorrect = nc;
    }



    public int getPretestNumIncorrect() {
        return pretestNumIncorrect;
    }

    public void setPretestNumIncorrect(int pretestNumIncorrect) {
        this.pretestNumIncorrect = pretestNumIncorrect;
    }

    public int getPosttestNumIncorrect() {
        return posttestNumIncorrect;
    }

    public void setPosttestNumCorrect(int nc) {
        this.posttestNumCorrect = nc;
    }

    public void setPosttestNumIncorrect(int posttestNumIncorrect) {
        this.posttestNumIncorrect = posttestNumIncorrect;
    }

    public int getNumProbsReceivedAssistance() {
        return numProbsReceivedAssistance;
    }

    public void setNumProbsReceivedAssistance(int numProbsReceivedAssistance) {
        this.numProbsReceivedAssistance = numProbsReceivedAssistance;
    }

    public int getNumProbsSolved() {
        return numProbsSolved;
    }

    public void setNumProbsSolved(int numProbsSolved) {
        this.numProbsSolved = numProbsSolved;
    }

    public int getNumHintsTotal() {
        return numHintsTotal;
    }

    public void setNumHintsTotal(int numHintsTotal) {
        this.numHintsTotal = numHintsTotal;
    }

    public int getNumHelpAidsTotal() {
        return numHelpAidsTotal;
    }

    public void setNumHelpAidsTotal(int numHelpAidsTotal) {
        this.numHelpAidsTotal = numHelpAidsTotal;
    }

    public int getNumHintsAfterSolveTotal() {
        return numHintsAfterSolveTotal;
    }

    public void setNumHintsAfterSolveTotal(int numHintsAfterSolveTotal) {
        this.numHintsAfterSolveTotal = numHintsAfterSolveTotal;
    }

    public long getTotalHintTime() {
        return totalHintTime;
    }

    public void setTotalHintTime(long totalHintTime) {
        this.totalHintTime = totalHintTime;
    }

    public long getTotalSolveTime() {
        return totalSolveTime;
    }

    public void setTotalSolveTime(long totalSolveTime) {
        this.totalSolveTime = totalSolveTime;
    }

    public long getTotalTimeBetweenAttempts() {
        return totalTimeBetweenAttempts;
    }

    public void setTotalTimeBetweenAttempts(long totalTimeBetweenAttempts) {
        this.totalTimeBetweenAttempts = totalTimeBetweenAttempts;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNumProbsSeen(int numProbsSeen) {
        this.numProbsSeen = numProbsSeen;
    }

    public void setAvgSolveTime(double avgSolveTime) {
        this.avgSolveTime = avgSolveTime;
    }

    public void setAvgMistakesInProblem(double avgMistakesInProblem) {
        this.avgMistakesInProblem = avgMistakesInProblem;
    }

    public void setAvgHintTime(double avgHintTime) {
        this.avgHintTime = avgHintTime;
    }


    public void clearTutorHutState () throws SQLException {
        DbStateTableMgr.clear(conn, TABLE_NAME, objid);
    }


    public static void main(String[] args) {
        int studId = Integer.parseInt(args[0]);
        int classId = Integer.parseInt(args[1]);
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection();
            BaseStudentModel m = new BaseStudentModel(conn);

            m.init(null,studId,classId);
            m.setAvgAttemptsInAssistedProb(45.4);
            m.setNumHintsAfterSolveTotal(4);
            m.save();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}