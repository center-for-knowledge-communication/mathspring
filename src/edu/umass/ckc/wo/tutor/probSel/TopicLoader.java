package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import edu.umass.ckc.wo.util.Lists;
import edu.umass.ckc.wo.beans.Topic;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jun 29, 2010
 * Time: 11:00:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class TopicLoader {
    public int classID;
    public int topicID;
    public String topicName;
    public List<Problem> probsInTopic;
    public Problem lastProb;
    public String lastProbMode;
    protected Connection conn;
    public int lastProbIndex;
    public List correctlySolvedProbs;
    public List correctlySolvedOnFirstProbs;
    public List reviewedProbs;
    private List<String> reasonsForNullProblem = null ;
    public static final int SOLVE_RECENCY_DAYS = 4;
    public static final int EXAMPLE_RECENCY_DAYS = 4;
    public static final int SOLVE_RECENCY_MS = SOLVE_RECENCY_DAYS * 24 * 60 * 60 * 1000;
    public static final int EXAMPLE_RECENCY_MS = EXAMPLE_RECENCY_DAYS * 24 * 60 * 60 * 1000;

    public static final String TOPIC_SWITCH_ALL_SOLVED = "All problems are solved or seen as examples";
    public static final String TOPIC_SWITCH_MAX_PROBLEMS = "The tutor thinks you've seen enough problems in this topic";
    public static final String TOPIC_SWITCH_MAX_TIME = "The tutor thinks you've spent enough time in this topic";
    public static final String TOPIC_SWITCH_MASTERED = "The tutor thinks you are pretty good at this topic";

    protected SessionManager smgr;

    private static Logger logger = Logger.getLogger(TopicLoader.class);


    public TopicLoader(Connection conn, int classid, SessionManager smgr) {
        this.conn = conn ;
        this.classID = classid ;
        this.smgr = smgr;
    }

    /**
     * The first time the problem selector is called this will determine the first topic for
     * the student.  This topic depends on the students class.  If there is a lesson plan set up for his
     * class, we return the first (0 based) problemGroupId based on that lesson plan.   If the class does not have
     * a lesson plan, it is following the default lesson plan.   In this case we set the classID to be the classId
     * associated with this default plan and then fetch the first topic in that plan.   We also set a flag
     * so that other methods know whether this student is operating from custom or default plan.
     *
     * @throws java.sql.SQLException
     */
    public int determineClass() throws SQLException {
        // if no topicId is returned, its because the class has no lesson plan, so we'll switch the classId to that of the default class
        // which contains a default lesson plan
        int topicID = getClassTopic(conn, classID, 1);  // find the first topic for the class the student is in.
        // if no topic, then this means no lesson plan found for class, so set classId to a default class which does have a
        // default lesson plan (lesson plan is a list of topics)
        if (topicID == -1)
            classID = getDefaultClassId(conn);  // classID that is associated with default plan
        return classID;

        //     }
    }

    /**
     * Get the ID of the current topic that student is in.
     *
     * @param conn
     * @param classID
     * @return the ID of the current topic
     */
    public int getCurTopic(Connection conn, StudentState studState, int classID) throws SQLException {
        int topicID = studState.getCurTopic();
        if (topicID == -1) {
            topicID = getClassTopic(conn, classID, 1); // get first topic
            studState.setCurTopic(topicID);
        }

        return topicID;
    }

    public List<Problem> getProblemsInTopic(Connection conn, int probGroupID) throws SQLException {
        return ProblemMgr.getTopicProblems(probGroupID);
    }

    public List<Problem> getProblemsInStandard(Connection conn, String ccss) throws SQLException {
        return ProblemMgr.getStandardProblems(conn, ccss);
    }

    public Problem getProblem (int probId) throws SQLException {
        return  ProblemMgr.getProblem(probId);
    }









    protected boolean isIDinProblemList(List<Problem> probs, int incPid) {
        for (Problem p : probs) {
            if ( p.getId() == incPid )
                return true;
        }
        return false;
    }

    public int indexOfProblem(List<Problem> problemList, int pId) {
        int index=-1 ;

        Iterator itr = problemList.iterator();
        while (itr.hasNext()) {
            Problem p = (Problem) itr.next();
            index ++ ;
            if ( p.getId() == pId )
                return index;
        }
        return -1;
    }

    protected boolean inList(List<String> ids, int id) {
        for (String s : ids) {
            if (s.equals(Integer.toString(id)))
                return true;
        }
        return false;
    }

    /**
     * Remove from the probs list any problems that are to be excluded for this class/topic
     *
     *
     * @param conn
     * @param classID
     * @param probs
     * @param topicId
     * @return
     * @throws java.sql.SQLException
     */
    protected List<Problem> excludeProblemsOmittedForClass(Connection conn, int classID, List<Problem> probs, int topicId) throws SQLException {
        List omittedProbIds = getClassOmittedProblems(conn, classID, topicId); // ids returned as Strings

        Iterator itr = probs.iterator();
        while (itr.hasNext()) {
            Problem problem = (Problem) itr.next();

            if (problem != null) {
                int id = problem.getId();
                if (inList(omittedProbIds, id))
                    itr.remove();
            }
        }
        return probs;

    }

    public List<Problem> getTopicProblemsForClass (Connection conn, int classID, int topicId) throws SQLException {
        List<Problem> allprobs = getProblemsInTopic(conn,topicId);
        return excludeProblemsOmittedForClass(conn,classID,allprobs,topicId);

    }

    public List<Problem> excludeExternalURLProblems(List<Problem> probs, int exceptProblemID ) throws SQLException {
        Iterator itr = probs.iterator();
        while (itr.hasNext()) {
            Problem problem = (Problem) itr.next();

            if (problem != null) {
                int id = problem.getId();
                if ( problem.isExternalActivity() && id != exceptProblemID )
                    itr.remove();
            }
        }
        return probs;

    }



    /**
     * Return the position of probId is in the topic
     *
     * @param probId
     * @param topicProbs
     * @return position of probId in the topic; -1 if not found
     */
    private int inTopic(int probId, List<Problem> topicProbs) {
        int i = 0;
        for (Problem p : topicProbs) {
            if (p!=null && p.getId() == probId)
                return i;
            i++;
        }
        return -1;
    }


    /**
     * Gets the last problem given.  If it's part of the current topic, return the problem;
     * otherwise return null;
     *
     * @param state
     * @param probList the list of problems in the topic
     * @return return the last problem given in the given topic; null if none
     */
    protected Problem getLastProbGivenInTopic(StudentState state, List<Problem> probList) throws SQLException {

        // BUG:   This is assuming state.getCurProblem() (the last problem given) is the last problem given in a given topic.
        // The topic may be a different one than the one we are currently running (e.g. when in MPP and user clicks "continue"
        // this gets called but user will probably be in a diff topic than the one he is trying to continue.
        int last = state.getCurProblem();
        logger.debug("last problem given: " + last);
        int ix;
        if ((ix = inTopic(last, probList)) != -1)
            return probList.get(ix);
        else return null;

    }












    private String convertToIdList(List<Problem> probsInGroup) {
        StringBuffer sb = new StringBuffer();

        for (Problem p : probsInGroup)
            if ( p!= null )
                sb.append(p.getId() + ", ");
        return sb.toString();
    }



    /**
     * Checks if the given topic has content that is still available to play.   Available content must be
     * problems that have not been solved, or given as examples.   We do not consider problems in the class's ommitted problem
     * set.
     * @param state
     * @param theTopicId
     * @return
     * @throws Exception
     */
    public boolean hasReadyContent(StudentState state, int theTopicId) throws Exception {
        // studentID and classID were set in setServletInfo method.
        List<Problem> topicProbs = getProblemsInTopic(conn, theTopicId);  // operates on a clone so destruction is ok
        Problem p = getLastProbGivenInTopic(state, topicProbs);
        int classID = determineClass();  // get either the default class (with default lesson plan) or the actual class (with a custom plan)
        List<String> omittedProbIds = getClassOmittedProblems(conn, classID, theTopicId); // problems omitted for this class
        Iterator itr = topicProbs.iterator();
        while (itr.hasNext()) {
            Problem prob= (Problem) itr.next();
            if ( prob != null ) {
                if ( Lists.inList(prob.getId(),omittedProbIds))
                    itr.remove();
            }
        }
        probsInTopic = removeRecentExamplesAndCorrectlySolvedProblems(smgr,(p != null ? p.getId() : -1),topicProbs, theTopicId);
        return topicProbs.size() > 1;
    }


    /**
     * Goes through the list of problems in the topic and eliminates some based on the situation.
     *
     *
     * @param smgr
     * @param resetCounters is passed as true only when really preparing for prob selection NOT when doing a check from an
     *                      intervention selector to see if we are about to switch topics.
     * @throws SQLException
     */
    public List<Problem> prepareForSelection(SessionManager smgr, boolean resetCounters) throws SQLException {
        // studentID and classID were set in setServletInfo method.
        StudentState state = smgr.getStudentState();
        classID = determineClass();  // get either the default class (with default lesson plan) or the actual class (with a custom plan)
        this.topicID = getCurTopic(conn, state, classID);
        this.topicName = ProblemMgr.getTopic(this.topicID).getName();
        probsInTopic = getProblemsInTopic(conn, topicID);   // get all problems in the topic

        excludeProblemsOmittedForClass(conn, classID, probsInTopic, this.topicID); // throw out problems teacher wants to omit
//        if (!includeExternalActivities)
//            excludeExternalURLProblems(probsInTopic,0);
        // set some instance variables based on last problem shown
        lastProb = getLastProbGivenInTopic(state, probsInTopic);
        lastProbMode = state.getLastProblemMode();
        // if the last prob is null then we just entered this topic
        if (lastProb == null && resetCounters) {
            state.setTopicNumProbsSeen(0);
            state.setTimeInTopic(0);
        }

        // N.B. this leaves the last problem in the list so the median selector can use it as a stopping point
        // By leaving problems in the list that were not solved correctly we keep student in this topic until all problems
        // are solved correctly.  The only exceptions to this are when the two hardest are solved well or the two easiest are failed.

        if (lastProb != null && lastProb.getId() != -1) {
            lastProbIndex = probsInTopic.indexOf(lastProb);

            if ( lastProbIndex == -1 )  ///IF FOR some reason, the last problem is removed, put it back in the list.
                //  Last problem should be in the list.
                probsInTopic.add(lastProb) ;

            logger.debug("last problem: id " + lastProb.getId() + "index in problems in topic list: " + lastProbIndex);
        }
        return probsInTopic;
    }

    public List<Problem> prepareForSelection(SessionManager smgr, String ccss) throws SQLException {
        // studentID and classID were set in setServletInfo method.
        StudentState state = smgr.getStudentState();
        probsInTopic = getProblemsInStandard(conn, ccss);   // get all problems in the topic
        // set some instance variables based on last problem shown
        lastProb = getLastProbGivenInTopic(state, probsInTopic);
        lastProbMode = state.getLastProblemMode();
        // if the last prob is null then we just entered this topic
        if (lastProb == null) {
            state.setTopicNumProbsSeen(0);
            state.setTimeInTopic(0);
        }

        // N.B. this leaves the last problem in the list so the median selector can use it as a stopping point
        // By leaving problems in the list that were not solved correctly we keep student in this topic until all problems
        // are solved correctly.  The only exceptions to this are when the two hardest are solved well or the two easiest are failed.

        if (lastProb != null && lastProb.getId() != -1) {
            lastProbIndex = probsInTopic.indexOf(lastProb);

            if ( lastProbIndex == -1 )  ///IF FOR some reason, the last problem is removed, put it back in the list.
                //  Last problem should be in the list.
                probsInTopic.add(lastProb) ;

            logger.debug("last problem: id " + lastProb.getId() + "index in problems in topic list: " + lastProbIndex);
        }
        return probsInTopic;
    }



    /**
     * Return the ID of the nth topic for a given class.   Will return -1
     * if there is no
     *
     * @param conn
     * @param classID
     * @param n
     * @return ID of topic that is nth in the sequence for the class
     * @throws java.sql.SQLException
     */
    private int getClassTopic(Connection conn, int classID, int n) throws SQLException {
        String q = "select probGroupId from ClassLessonPlan where classID=? and seqPos>0 order by seqPos";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classID);
        ResultSet rs = ps.executeQuery();
        for (int i = 1; rs.next(); i++) {
            if (i == n)
                return rs.getInt(1);
        }
        return -1;
    }

    /**
     * Get the class id used in the default class lesson plan
     *
     * @param conn
     * @return
     * @throws java.sql.SQLException
     */
    private int getDefaultClassId(Connection conn) throws SQLException {
        String q = "select classId from ClassLessonPlan where isDefault=1";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else return -1;
    }


    private List<String> getClassOmittedProblems(Connection conn, int classID, int topicId) throws SQLException {
        String q = "select probId from ClassOmittedProblems where classId=? and topicId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classID);
        ps.setInt(2, topicId);
       
        List<String> results = new ArrayList<String>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int pid = rs.getInt(1);
            results.add(Integer.toString(pid));
        }
        return results;
    }

    /**
     * @param conn
     * @param classID
     * @param probGroupID
     * @return seqNum of given problemGroup of given class
     */
    private int getClassLessonSeqNum(Connection conn, int classID, int probGroupID) throws SQLException {
        String q = "select seqPos from ClassLessonPlan where classId=? and probGroupId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classID);
        ps.setInt(2, probGroupID);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else return -1;
    }

    /**
     * Gets the id of the .
     *
     * @param conn
     * @param classID
     * @param seqPos
     * @return probGroupID for given seqNum of given class
     */
    private int getClassLessonFromSeqNum(Connection conn, int classID, int seqPos) throws SQLException {
        String q = "select probGroupId from ClassLessonPlan where classId=? and seqPos=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classID);
        ps.setInt(2, seqPos);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    /**
     *
     * @param conn
     * @param classID
     * @return  THe first active topic in the class lesson plan.   If no topics are active, then -1
     * @throws SQLException
     */
    private int getClassLessonFirstTopic(Connection conn, int classID) throws SQLException {
        String q = "select probGroupId from ClassLessonPlan where classId=? and seqPos>=0 order by seqPos";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classID);
        ResultSet rs = ps.executeQuery();
        // returns a sorted ordering of rows.  We return the first which will be the lowest seqPos
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public List<Topic> getClassLessonPlan(Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            int classID = determineClass(); // either the default class or the actual class
            String q = "select probGroupId from ClassLessonPlan where classId=? and seqPos>=0 order by seqPos";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classID);
            rs = stmt.executeQuery();
            List<Topic> plan = new ArrayList<Topic>();
            while (rs.next()) {
                int topicID= rs.getInt(1);
                String descr = "";
                plan.add(new Topic(topicID,descr));
            }
            return plan;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public boolean isTopicMaxTimeExceeded(StudentState state, long probElapsedTime, long maxTimeInTopic) throws SQLException {
        long timeInTopic = state.getTimeInTopic() + probElapsedTime;
        return (timeInTopic > maxTimeInTopic) ;
    }

    public boolean isTopicMinTimeMet(StudentState state, long probElapsedTime, long minTimeInTopic) throws SQLException {
        long timeInTopic = state.getTimeInTopic() + probElapsedTime;
        return (timeInTopic >= minTimeInTopic) ;

    }

    public boolean seenMaxNumberProblems(StudentState state, int maxNumberProbs) {
        return state.getTopicNumProbsSeen() >= maxNumberProbs;
    }

    public boolean isTopicInClassLessonPlan(int topicId) throws SQLException {
        List<Topic> activeTopics = getClassLessonPlan(conn);
        for (Topic t : activeTopics)
            if (t.getId() == topicId)
                return true;
        return false;
    }

    public boolean shouldSwitchTopics( StudentState state, long probElapsedTime, StudentModel sm,
                                      int maxNumberProbs, long maxTimeInTopic, double topicMastery,
                                      int minNumberProbs, long minTimeInTopic) throws Exception {
        topicID = state.getCurTopic();
        this.topicName = ProblemMgr.getTopic(this.topicID).getName();
        boolean b=  shouldSwitchTopics(topicID,state,probElapsedTime, sm, maxNumberProbs, maxTimeInTopic, topicMastery, minNumberProbs, minTimeInTopic);
        // this is here just to report to the logger that we are switching topics.   When the intervention selector
        // thinks there will be an upcoming topic switch it turns on the isTopicSwitch flag in the state.
        // This means that here (when we are actually selecting a problem) we should be returning true about switching topics
        // If we don't we report an error about inconsistency.
        if (b) {
            if (state.isTopicSwitch()) {
//                logger.debug("consistent Topic Switch results: " + getReasonsForNullProblem());
                state.setTopicSwitch(false);
            }
            // intervention selector failed to report an upcoming topic switch.
            else {
                logger.debug("INCONSISTENT Topic Switch results: " + getReasonsForNullProblem());
                state.setTopicSwitch(false);
            }
        }
        return b;
    }


    /**
     * Performs a sequence of cheap tests to see if the topic has been completed.
     * @param state
     * @param probElapsedTime
     * @param sm
     * @param maxNumberProbs
     * @param maxTimeInTopic
     * @param topicMastery
     * @param minNumberProbs
     *@param minTimeInTopic @return
     * @throws SQLException
     */
    public boolean shouldSwitchTopics(int tid, StudentState state, long probElapsedTime, StudentModel sm,
                                      int maxNumberProbs, long maxTimeInTopic, double topicMastery,
                                      int minNumberProbs, long minTimeInTopic) throws Exception {
        boolean  maxProbsMet = false, maxTimeMet = false, topicMastered = false, topicInLesson=false;
        reasonsForNullProblem = new ArrayList<String>();

        // if all the problems in the topic have been solved, we have to switch
        if (!hasReadyContent(state, tid)) {
            reasonsForNullProblem.add(TOPIC_SWITCH_ALL_SOLVED);
            logger.debug("Changing topics because all problems have been solved or were given as examples");
            return true;
        }
        double topicMasteryEstimate = sm.getTopicMastery(tid);
        boolean shouldSwitch =
                (maxProbsMet = seenMaxNumberProblems(state, maxNumberProbs)) ||
                (maxTimeMet = isTopicMaxTimeExceeded(state, probElapsedTime, maxTimeInTopic)) ||
                (topicMastered = topicMasteryEstimate >= topicMastery) ||
                (topicInLesson = ! isTopicInClassLessonPlan(tid) );
        // we don't want to switch topics if the min number problems hasn't been shown or the min time in topic hasn't been reached.
        shouldSwitch = shouldSwitch && state.getTopicNumProbsSeen() >= minNumberProbs;
        shouldSwitch = shouldSwitch && isTopicMinTimeMet(state,probElapsedTime,minTimeInTopic);
        // Creating a concatenated string of the reasons why we would like to switch the topic
        if (shouldSwitch) {
            String msg = null ;
            if (maxProbsMet) {
                reasonsForNullProblem.add(msg = TOPIC_SWITCH_MAX_PROBLEMS);
            }
            else if (maxTimeMet) {
                reasonsForNullProblem.add(msg=TOPIC_SWITCH_MAX_TIME);
            }
            else if (topicMastered) {
                reasonsForNullProblem.add(msg=TOPIC_SWITCH_MASTERED);
            }
            logger.debug("Changing topics because [" + msg + "]");
        }
        return shouldSwitch;
    }


    // Cycle through the topics looking for one that is available (i.e. has problems that haven't been solved,omitted, or given
    // as examples).   If it goes through the whole list of topics and returns to the current topic,  then it returns -1
    public int getNextTopicWithAvailableProblems(Connection conn, int topicID,
                                                 StudentState state) throws Exception {
        int curPos;
        int nextTopicId;

        logger.debug("Finding a next topic: getNextTopicWithAvailableProblems");
        //  A sidelined topic may exist if the student left a topic early to go pursue some other topic using the MPP.
        //  When the student completes the topic they pursued,  the system tries to return them to the sidelined topic.
        // Exceptions to this:  A student may leave topic X to go pursue topic Y.  He may then leave topic Y to pursue topic X (through
        // an MPP action).   When topic X is complete,  the sidelined topic is X but it doesn't make sense to now resume work in topic
        // X.  In a case where the last topic a student was in is the same as the sidelined topic, we remove the sidelined topic and
        // let this select the next topic
        //
        nextTopicId = state.getSidelinedTopic();
        state.setSidelinedTopic(-1);
        if (nextTopicId != -1 && nextTopicId != topicID)   {
            logger.debug("getNextTopicWithAvailableProblems  next topic is : " + nextTopicId);
            return nextTopicId;
        }

        curPos = getClassLessonSeqNum(conn, classID, topicID); // get seqNum of cur topic
        nextTopicId = getClassLessonFromSeqNum(conn, classID, curPos + 1);
        int nextId;
        // will return -1 if we are at the end of the lesson plan
        if (nextTopicId != -1)
            nextId = nextTopicId;
        else {// if all prob groups have been used, start at beginning.
            //  Get the topic with the lowest seqNum (NB inactive topics have seqNum=-1 so we must omit those)
            nextId = getClassLessonFirstTopic(conn,classID);
        }
        while (!hasReadyContent(state,nextId) && nextId != topicID) {
            curPos = getClassLessonSeqNum(conn, classID, nextId); // get seqNum topic
            nextTopicId = getClassLessonFromSeqNum(conn, classID, curPos + 1);
            if (nextTopicId != -1)
                nextId = nextTopicId;
            else // if all prob groups have been used, start at beginning.
                //  Get the topic with the lowest seqNum (NB inactive topics have seqNum=-1 so we must omit those)
                nextId = getClassLessonFirstTopic(conn,classID);
        }
        // if it did a full cycle through all topics,  there are no more problems to show
        if (nextId == topicID)   {
            logger.debug("getNextTopicWithAvailableProblems  next topic is : " + -1);
            return -1;
        }
        else {
            logger.debug("getNextTopicWithAvailableProblems  next topic is : " + nextId);
            return nextId;
        }
    }


    public int setNextTopic(Connection conn, int classID, int probGroupID, StudentState state) throws SQLException {
        int curPos;
        int nextProbGroupID;

        curPos = getClassLessonSeqNum(conn, classID, probGroupID); // get seqNum of cur pgroup
        nextProbGroupID = getClassLessonFromSeqNum(conn, classID, curPos + 1);
        int nextId;
        // will return -1 if we are at the end of the lesson plan
        if (nextProbGroupID != -1)
            nextId = nextProbGroupID;
        else {// if all prob groups have been used, start at beginning.
            //  Get the topic with the lowest seqNum (NB inactive topics have seqNum=-1 so we must omit those)
            nextId = getClassLessonFirstTopic(conn,classID);
        }
        state.setCurTopic(nextId);
        return nextId;
    }
//
//    public void setTopic (int topicId) {
//        this.topicID = topicId;
//    }


    // test to see if the problem ended within SOLVE_RECENCY milliseconds
    private boolean withinExampleRecency(long problemEndTime) {
        return (System.currentTimeMillis() - problemEndTime) <= EXAMPLE_RECENCY_MS;
    }

    // test to see if the problem ended within EXAMPLE_RECENCY milliseconds
    private boolean withinSolveRecency(long problemEndTime) {
        return (System.currentTimeMillis() - problemEndTime) <= SOLVE_RECENCY_MS;
    }

/**
      * Remove problems from the probsInGroup that satisfy these conditions:
      * 1. student has solved within SOLVE_RECENCY_DAYS
     *  2. shown as example withing EXAMPLE_RECENCY_DAYS
     *
      *
      * @param lastProbID
      * @param probsInTopic
      * @return a list of ProblemImpl objects that are
      * @throws java.sql.SQLException
      */
      List<Problem> removeRecentExamplesAndCorrectlySolvedProblems(SessionManager smgr,
                                                                   int lastProbID, List<Problem> probsInTopic, int tid) throws Exception {
         int maxShowForParametrizedProblem = 2; // TODO: get this out of the pedagogies.xml
         StudentProblemHistory studentProblemHistory = smgr.getStudentModel().getStudentProblemHistory();
         // get the problems in the topic ordered by most recent encounter  first.
         List<StudentProblemData> probEncountersInTopic = studentProblemHistory.getTopicHistoryMostRecentEncounters(tid);
         // if the last prob is null then we just entered this topic

         // There is a slight glitch here.   If a problem was recently solved or given as an example as part of another topic,  it will not be eliminated from this topic
         // because we are only looking at the history of this topic.

         logger.debug("topic is: " + tid);
         logger.debug("problems in topic: " + probsInTopic);
         List<Problem> probsRemoved = new ArrayList<Problem>(probsInTopic.size());

         // Check the student's most recent encounter with a problem.
         // Remove it if it was solved and the encounter was within SOLVE_RECENCY_DAYS or if it was an example given
         // within EXAMPLE_RECENCY_DAYS

         // it DOES NOT REMOVE the last problem.  That needs to stay in the list because later we use its location to offset
          // to the next problem when splitting differences based on indices.


         List<String> checked = new ArrayList<String>();
         for (StudentProblemData d : probEncountersInTopic) {
             if (lastProbID == d.getProbId())
                 continue;
             if (!inList(checked,d.getProbId())) {
                 checked.add(Integer.toString(d.getProbId()));
                 Problem p = getProblem(d.getProbId());

                 // If the student solved a parametrized problem, we only want to show it x number of times, and not if there are no more parametrizations
                 // If student got it wrong, show it again.
                 if (p.isParametrized()) {
                     int timesProblemShown = 0;
                     if (d.isSolved() && d.isPracticeProblem() && withinSolveRecency(d.getProblemEndTime())) {
                         timesProblemShown = studentProblemHistory.getTimesEncountered(d.getProbId());
                         if (timesProblemShown >= maxShowForParametrizedProblem) {
                             probsInTopic.remove(p);
                         }
                         if (!p.getParams().hasUnusedParametrization(timesProblemShown)) {
                             probsInTopic.remove(p);
                         }
                     }
                 }

                 else if (d.isSolved() && d.isPracticeProblem() && withinSolveRecency(d.getProblemEndTime())) {
                    probsInTopic.remove(p);
                 }
                 else if (Problem.isExampleOrDemo(d.getMode()) && withinExampleRecency(d.getProblemEndTime())){
                    p = getProblem(d.getProbId());
                    probsInTopic.remove(p);
                 }
             }
         }

         // finally sort the list into ascending order of difficulty
         Collections.sort(probsInTopic, new Comparator<Problem>() {
             public int compare(Problem o1, Problem o2) {
                 return (o1.getDiff_level() < o2.getDiff_level()) ? -1 : (o1.getDiff_level() == o2.getDiff_level() ? 0 : 1);
             }
         });
         logger.debug("probs in topic to select from: " + probsInTopic);
         return probsInTopic;
     }

    public int getTopicId () {
        return this.topicID;
    }

    public String getTopicName () {
        return this.topicName;
    }

    public List<String> getReasonsForNullProblem() {
        return reasonsForNullProblem;
    }

    public void setReasonsForNullProblem(List<String> reasonsForNullProblem) {
        this.reasonsForNullProblem = reasonsForNullProblem;
    }
}