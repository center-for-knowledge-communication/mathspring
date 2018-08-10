package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.InterleavedProblemSetParams;
import edu.umass.ckc.wo.tutor.studmod.EffortHeuristic;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.util.Lists;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 2/2/16
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterleavedTopic {
    private SessionManager smgr;

    public InterleavedTopic(SessionManager smgr) {
        this.smgr = smgr;
    }

    void buildInterleavedProblemSet(Connection conn, List<String> topicsToReview, StudentModel studentModel, InterleavedProblemSetParams params) throws SQLException {
        // need to clear the interleavedProblems table for this student and then load it with problems from the topicsToReview

        int numProbsPerTopic = params.getNumProbsPerTopic();
        int studId = smgr.getStudentId();
        int interleaveTopicId = Settings.interleavedTopicID;
        StudentProblemHistory hist = studentModel.getStudentProblemHistory();
        List<StudentProblemData> probs = hist.getReverseHistory();
        // a list of pairs <pid, topicId>  which is review problems and the topics they are in.
        List<ReviewProblem> probsToReview = new ArrayList<ReviewProblem>();
        // Go through the student history and find problems that are good for review.
        for (StudentProblemData p : probs) {
            int topicId = p.getTopicId();
            String topicIdstr = Integer.toString(topicId);
            // If the problem is part of the list of topics to review, add it to the list of problems if it meets certain criteria
            if (topicsToReview.contains(topicIdstr) ) {
                    // if the probsToReview has an element for the problem p, skip it
                    // DM : N.B. this is a Java 8 lambda expression (see https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#approach1)
                    if (Lists.hasAnElement(probsToReview,  x -> x.getProbId() == p.getProbId()))
                        continue;
                    // Add problems where a hint was seen and it was solved.
                    else if (p.getNumHints() > 0 && p.isSolved())
                        probsToReview.add(new ReviewProblem(p.getProbId(),topicId));
                    // Add problems that were solved and are above the student mastery in the topic
                    else if (p.isSolved() && studentModel.getTopicMastery(topicId) <= ProblemMgr.getProblem(p.getProbId()).getDifficulty())
                        probsToReview.add(new ReviewProblem(p.getProbId(),topicId));
                    // Add problems that were attempted and not solved and are below the student mastery in the topic
                    else if (p.getNumAttemptsToSolve() > 0 && !p.isSolved() && studentModel.getTopicMastery(topicId) >= ProblemMgr.getProblem(p.getProbId()).getDifficulty())
                        probsToReview.add(new ReviewProblem(p.getProbId(),topicId));
                    // Add problems that were solved but after more than two attempts
                    else if (p.getNumAttemptsToSolve() > 2)
                        probsToReview.add(new ReviewProblem(p.getProbId(),topicId));
                    // Add problems where it looks like user is guessing
                    else if (p.getEffort().equals(EffortHeuristic.NOT_READING))
                        probsToReview.add(new ReviewProblem(p.getProbId(),topicId));

            }
            // When we go back in the history and hit the last interleaved problem-set, stop adding stuff.
            else if (topicId == interleaveTopicId)
                break;
        }
        List<ReviewProblem> reviewProblems = new ArrayList<ReviewProblem>(probsToReview);
        // A random shuffle isn't that desirable but an intelligent ordering of problems is much more difficult
        Collections.shuffle(reviewProblems);
        DbTopics.deleteStudentInterleavedProblems(conn, studId);
        int i=1;
        for (ReviewProblem p : probsToReview)
            DbTopics.addStudentInterleavedProblem(conn,studId,p.getProbId(),p.getTopicId(),i++);
        // TODO we're going to have issues with the MPP:  What happens if student is in interleaved topic and uses MPP and then
        // clicks go back to problem I was on??  Handling the nextProb event is going to have to be handled correctly
    }


    private int getNumTopicsSinceLastReview (Connection conn, int topicId, StudentModel sm, InterleavedProblemSetParams params) {
        StudentProblemHistory hist = sm.getStudentProblemHistory();
        int interleaveId = Settings.interleavedTopicID;
        int lastTopic = topicId;
        int topicCount = 0;  // counts the number of fully explored topics.
        int probsSolvedInTopic = 0;
        long timeInTopic=0; // number of seconds in the topic
        List<String> topicsToReview = new ArrayList<String>();
        for (StudentProblemData d : hist.getReverseHistory()) {
            int probTopicId = d.getTopicId();
            if (d.isDemo() || d.isTopicIntro())
                continue;
            // if the problem is in the interleaved topic, we've found the last time we were in an interleaved problem set
            if (probTopicId == interleaveId)
                return topicCount;
            // if the problem is in a different topic than the last, then increment the topicCounter IF the problemsSolved count is
            // at a level that indicates the student saw enough problems in the previous topic.   Then
            // reset the problemsSolved counter.
            if (probTopicId != lastTopic)  {
                 topicCount++;
                lastTopic = probTopicId;
            }
        }
        return topicCount;
    }

    /**
     * Given the current topic, go through the students problem-solving history in reverse looking for topics that are reviewable.
     * A reviewable topic is one that a student solved enough problems in.   Gathering these topics stops at the point where
     * we find a topic that is the interleaved-topic ID OR when we've gathered problems from enough topics (the number of topics is
     * specified in the InterleavedProblemSetParams).   The effect is that we are gathering reviewable topics between now and
     * the last time an interleaved problem set was given.  Returns the list of topics to review.
     * @param conn
     * @param topicID
     * @param studentModel
     * @param params
     * @return
     * @throws SQLException
     */
    private List<String> getReviewableTopics(Connection conn, int topicID, StudentModel studentModel, InterleavedProblemSetParams params) throws SQLException {
        StudentProblemHistory hist = studentModel.getStudentProblemHistory();
        int interleaveId = Settings.interleavedTopicID;
        int lastTopic = topicID;
        int topicCount = 0;  // counts the number of fully explored topics.
        int probsSolvedInTopic = 0;
        long timeInTopic=0; // number of seconds in the topic
        List<String> topicsToReview = new ArrayList<String>();
        for (StudentProblemData d : hist.getReverseHistory()) {
            int probTopicId = d.getTopicId();
            if (d.isDemo() || d.isTopicIntro())
                continue;
            // if the problem is in the interleaved topic, we've found the last time we were in an interleaved problem set
            if (probTopicId == interleaveId)
                return topicsToReview;
            // if the problem is in a different topic than the last, then increment the topicCounter IF the problemsSolved count is
            // at a level that indicates the student saw enough problems in the previous topic.   Then
            // reset the problemsSolved counter.
            if (probTopicId != lastTopic)  {
                // Did the user solve enough problems in the topic or spend enough time in it?  If so, we'll count it as an explored topic.
                if (probsSolvedInTopic >= params.getExploredProblemNum() || timeInTopic >= params.getExploredMinTime() * 60) {
                    topicCount++;
                    topicsToReview.add(Integer.toString(lastTopic));  // put the last topic in a list
                }
                probsSolvedInTopic= d.isSolved() ? 1 : 0; // if the current problem is solved start at 1, else 0
                timeInTopic = d.isSolved() ? d.getTimeInProblemSeconds() : 0;
                lastTopic = probTopicId;
            }
            // Each time we see a solved problem that is in the same topic as the last problem, we increment the problemsSolved counter
            else if (probTopicId == lastTopic && d.isSolved())
                probsSolvedInTopic++;
            // count the amount of time spent in every practice problem
            timeInTopic += d.getTimeInProblemSeconds();
            // exit if we've found enough reviewable topics - TODO or spent enough time
            if (topicCount >= params.getNumTopicsToReview() )
                return topicsToReview;
        }
        return topicsToReview;
    }


    /**
     * Given the current topic,  determine if its time to show an interleaved problem set.
     *
     * @param conn
     * @param topicID
     * @param studentModel
     * @param params
     * @return
     * @throws SQLException
     */
    boolean shouldShowInterleavedProblemSet(Connection conn, int topicID, StudentModel studentModel, InterleavedProblemSetParams params) throws SQLException {
        List<String> topicsToReview = getReviewableTopics(conn, topicID, studentModel, params);
        int numTopicsSeenSinceLastReview = getNumTopicsSinceLastReview(conn,topicID,studentModel,params);
        // put results of condition in local var so I can tweak this in debugger to force it to go into interleaved topics if I want.
        boolean cond =  topicsToReview.size() >= params.getNumTopicsToReview() && numTopicsSeenSinceLastReview >= params.getNumTopicsToWait();
        // We only want to show an interleaved topic if we found enough topics to review since the last time we showed
        // an interleaved problem set AND we have seen enough topics to satisfy the numTopicsToWait parameter which controls how frequently these
        // interleaved topics are offered.
        if (cond) {
            smgr.getStudentState().setReviewTopics(topicsToReview);
            return true;
        }
        else return false;
    }

    /**
     * Using the list of topics that are part of the interleaved problem set and the student problem history we gather
     * a list of TopicPerformance objects that have tallies of how the student did in the topics within the interleaved pset.
     * @return
     * @throws SQLException
     */
    public List<TopicPerformance> getStudentPerformance() throws SQLException {
        StudentState studentState = smgr.getStudentState();
        StudentProblemHistory h = smgr.getStudentModel().getStudentProblemHistory();
        List<TopicPerformance> tp = new ArrayList<TopicPerformance>(studentState.getReviewTopics().size());
        // Create a list of empty TopicPerformance objects
        for (String tid : studentState.getReviewTopics()) {
            int topicId =  Integer.parseInt(tid);
            String topicName = ProblemMgr.getTopic(topicId).getName();
            TopicPerformance tp1 = new TopicPerformance(topicName , topicId) ;
            tp.add(tp1);
        }
        gradePerformance(tp,h);
        return tp;
    }

    /**
     * Given a list of TopicPerformance objects and the student history, go back through the history and
     * count the number of incorrect/correct in each topic that is part of the interleaved prob set.
     * @param topicPerformanceList
     * @param h
     * @throws SQLException
     */
    private void gradePerformance(List<TopicPerformance> topicPerformanceList, StudentProblemHistory h) throws SQLException {
        // returns a list of objects that are the problems given as interleaved problem set.  Pairs of [probId, topicId]
        List<int[]> psetProbs = DbTopics.getInterleavedProblems(smgr.getConnection(),smgr.getStudentId());
        List<StudentProblemData> recs = h.getReverseHistory();
        // Go through reverse history and tally how many right/wrong the student got in each topic being reviewed.
        for (StudentProblemData d: recs)  {
            // go back until we hit a problem that is not part of an interleaved problem set
            if (d.getTopicId() != Settings.interleavedTopicID)
                break;

            int tid = getInterleavedProblemTopic(d.getProbId(), psetProbs);
            // find the correct performance record
            TopicPerformance rec=null;
            for (TopicPerformance perf : topicPerformanceList )
                if (perf.topicId ==  tid) {
                    rec = perf;
                    break;
                }
            if (rec == null)
                continue;
            rec.numShown++;
            if (d.isSolved())
                rec.numCorrect++;
            else rec.numIncorrect++;

        }
    }

    //  The pid was an interleaved problem.   Return which topic this problem is in (interleavedProbs gotten from interleavedproblemset table)
    private int getInterleavedProblemTopic (int pid, List<int[]> interleavedProbs) {
        for (int[] interleavedProb: interleavedProbs)
            if (interleavedProb[0] == pid)
                return interleavedProb[1];
        return -1;
    }

    public class TopicPerformance {
        String topicName;
        int topicId;
        int numShown;
        int numIncorrect;
        int numCorrect;

        TopicPerformance(String topicName, int topicId) {
            this.topicName = topicName;
            this.topicId = topicId;
        }

        public String getTopicName() {
            return topicName;
        }

        public int getTopicId() {
            return topicId;
        }

        public int getNumShown() {
            return numShown;
        }

        public int getNumIncorrect() {
            return numIncorrect;
        }

        public int getNumCorrect() {
            return numCorrect;
        }
    }

    static class ReviewProblem   {
        int probId;
        int topicId;

        ReviewProblem(int probId, int topicId) {
            this.probId = probId;
            this.topicId = topicId;
        }

        public int getProbId() {
            return probId;
        }

        public int getTopicId() {
            return topicId;
        }

        public String toString () {
            return probId + "," + topicId;
        }

        @Override
        public boolean equals (Object o) {
            ReviewProblem p = (ReviewProblem) o;
            return (p.getProbId() == this.probId && p.getTopicId() == this.topicId) ;
        }

        @Override
        public int hashCode () {
            return 41 + new Integer(probId).hashCode() + new Integer(topicId).hashCode();
        }
    }

    public static void main (String[] args) {
        ReviewProblem p1 = new ReviewProblem(1,1);
        ReviewProblem p2 = new ReviewProblem(1,2);
        ReviewProblem p3 = new ReviewProblem(1,1);
        Set<ReviewProblem> s = new HashSet<ReviewProblem>();
        s.add(p1);
        s.add(p2);
        s.add(p3);
        for (ReviewProblem p : s) {
            System.out.println(p);
        }
    }

}
