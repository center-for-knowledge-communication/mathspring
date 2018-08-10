package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutormeta.ProblemSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/30/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseProblemSelector implements ProblemSelector {

    protected PedagogicalModelParameters parameters;
    protected TopicModel topicModel;
    protected LessonModelParameters lessonModelParameters;
    protected SessionManager smgr;

    public BaseProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        this.smgr = smgr;
        this.topicModel = (TopicModel) lessonModel;
        this.parameters=params;
        // When using this problem selector, there is the assumption that topics are used.
        // I don't think it's necessary to use TopicModelParameters though except that Pedagogies instantiate
        // by setting the topicModel.tmParams and its not so easy to have them set the topicModel.lmParams instead
        // (partially because of interleaved problem sets attributes in the topicModelParameters).
        // Tutoring strategies instantiate with the topicModelParameters.lmParameters set (and the tmParameters null)

        // Tutoring strategies have the lesson params on the lessonModel.lmParams.   Pedagogies have
        // the lesson params on lessonModel.tmParams, so we get the right one.
        LessonModelParameters lmParams = lessonModel.getLmParams();
        TopicModelParameters tmParams = ((TopicModel) lessonModel).getTmParams();
        if (lmParams != null)
            this.lessonModelParameters = lmParams; // tutoring strategies will have them here.
        else this.lessonModelParameters = tmParams; // pedagogies will have them here.
    }

    public static boolean hasInterleavedProblem (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
         PreparedStatement stmt=null;
        try {
            String q = "select count(*) from interleavedProblems where studid=? and shown=0";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c > 0;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Problem selectInterleavedProblem(Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select studid, probId,shown from interleavedProblems where studid=? and shown=0 order by position";
            stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int probid= rs.getInt(2);
                rs.updateInt(3,1);
                rs.updateRow();
                return ProblemMgr.getProblem(probid);
            }
            else return null;
        }  finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    @Override
    /**
     * precondition:  This method is only called if we know the topic has no upcoming content failure and all other conditions for continuing in a topic
     * are met.    In theory,  there should be no fencepost errors based on this.
     */
    public Problem selectProblem(SessionManager smgr, NextProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        long t = System.currentTimeMillis();
        if (topicModel.isInInterleavedTopic()) {
            return selectInterleavedProblem(smgr.getConnection(),smgr.getStudentId());
        }
        // DM 2/18 - note that this will take into account if curProb is broken and return SAME difficulty
        TopicModel.difficulty nextDiff = topicModel.getNextProblemDifficulty(lastProblemScore);
        StudentState state = smgr.getStudentState();
        // Gets problems with testable problems included if the user is marked to receive testable stuff.
        List<Integer> topicProbIds = topicModel.getUnsolvedProblems();
        int lastProbId = smgr.getStudentState().getCurProblem();
        List<String> brokenProblemsForThisStudent = smgr.getStudentState().getBrokenProblemIds(); // DM 2/5/18 added for Ivon request
        // if the last problem given wasn't solved it'll still be in the list.  Toss it out.  We don't want to show the same problem 2X in a row
        int loc = topicProbIds.indexOf(lastProbId);
        if (loc != -1)
            topicProbIds.remove(loc);
        // now remove broken problems from the list
        for (String pidstr: brokenProblemsForThisStudent) {
            loc = topicProbIds.indexOf(Integer.parseInt(pidstr));
            if (loc != -1)
                topicProbIds.remove(loc);
        }

//        List<Problem> topicProblems = xx;
        int lastIx = state.getCurProblemIndexInTopic();
        int nextIx=-1;

        // Error in setting of difficulty Rate is possible if configuration of the strategy was not done well.
        // Must correct it to a default because will lead to malfunction; set to the default difficulty rate
        if (parameters.getDifficultyRate() == 0.0)
            parameters.setDifficultyRate(PedagogicalModelParameters.DIFFICULTY_RATE);
        // lastIx is -1 when the topic is new.
        if (lastIx == -1)
            nextIx = (int) Math.round((topicProbIds.size()-1) / parameters.getDifficultyRate());

        if (nextIx == -1 && nextDiff == LessonModel.difficulty.EASIER) {
            if (lastIx <= 0)
                throw new DeveloperException("Last problem index=0 and want easier problem.   Content failure NOT PREDICTED by TopicSelector");
            nextIx =(int) Math.round(lastIx / parameters.getDifficultyRate());
        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.HARDER) {
            if (lastIx >= topicProbIds.size())
                throw new DeveloperException("Last problem >= number of problems in topic.   Content failure NOT PREDICTED by TopicSelector");
            nextIx = lastIx + ((int) Math.round((topicProbIds.size()-1 - lastIx) / parameters.getDifficultyRate()));

        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.SAME) {
            nextIx = Math.min(lastIx, topicProbIds.size()-1);
        }
        nextIx = Math.min(nextIx, topicProbIds.size()-1);
        int nextProbId = topicProbIds.get( nextIx);
        state.setCurProblemIndexInTopic( nextIx);
        state.setCurTopicHasEasierProblem(nextIx > 0);
        if (nextIx < topicProbIds.size() - 1)
            state.setCurTopicHasHarderProblem(true);
        else state.setCurTopicHasHarderProblem(false);
        // it takes 125 ms to get to here

        Problem p = ProblemMgr.getProblem(nextProbId);

        return p;
    }

    @Override
    public void setParameters(PedagogicalModelParameters params) {
        this.parameters = params;
    }
}
