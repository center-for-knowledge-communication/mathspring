package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.PreviewProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.ProblemSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *Will select problems from a standard
 * User: marshall
 * Date: 5/3/16
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CCSSProblemSelector implements ProblemSelector {

    protected PedagogicalModelParameters parameters;
    protected LessonModel lessonModel;
    protected LessonModelParameters lessonModelParameters;
    protected SessionManager smgr;

    public CCSSProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        this.smgr = smgr;
        this.lessonModel =  lessonModel;
        this.parameters=params;
        this.lessonModelParameters =  lessonModel.getLmParams(); // ((TopicModel) lessonModel).getTmParams(); // topics assumed when using this selector
    }


    private List<Integer> getUnsolvedProblems () throws Exception {
        String ccss = lessonModel.getLmParams().getCcss();
        // get all problems that address this CCSS.  They are sorted by difficulty
        List<Problem> probs = ProblemMgr.getStandardProblems(smgr.getConnection(), ccss);
        // Turn this list of Problem objects into a simple list of integer ids.
        List<Integer> probIds = new ArrayList<Integer>();
        for (Problem p : probs) {
            probIds.add(p.getId());
        }
        StudentProblemHistory studentProblemHistory = smgr.getStudentModel().getStudentProblemHistory();
        // get the history records for problems this student has seen on this standard
        List<StudentProblemData> probEncountersInStandard = studentProblemHistory.getCCSSHistoryMostRecentEncounters(ccss);
        // reduce this history to only the problems that are considered "recently correctly solved"
        List<Integer> recentProbs = smgr.getExtendedStudentState().getRecentExamplesAndCorrectlySolvedProblems(probEncountersInStandard);
        // eliminate these recently solved ones from the list of problems
        probIds.removeAll(recentProbs);
        return probIds;
    }


    @Override
    /**
     * Select a problem from the common core standard.  Bases the selection on the student problem history, his score on the last problem
     * and difficulty level of the remaining problems.
     */
    public Problem selectProblem(SessionManager smgr, NextProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        long t = System.currentTimeMillis();

        LessonModel.difficulty nextDiff = lessonModel.getNextProblemDifficulty(lastProblemScore);
        // TODO Handle multiple CCSS.
        // If the request is for multiple CCSS, then we will get a string in CSV format with each of them.  If its just one, we can handle it.
        // If there are multiple standards we probably need to build out the LessonModel that goes in this pedagogy so that it moves through the standards
        // and puts the current standard in the student state which this selector would then access to figure out what problems to pick from.
        String ccss = lessonModel.getLmParams().getCcss();
        StudentState state = smgr.getStudentState();
        //  We ask the lesson model to keep track of problems that are unsolved.  This is mostly because the TopicModel has a helper (TopicSelectorImp)
        // that keeps track of this.   So for CommonCore we'll have a LessonModel subclass that can return the problems remaining in the standard???
        // For ref:  The way the TopicSelector does it is that it knows the topic and creates the list of problems in the topic; it uses the student problem history to remove recently seen/solved
        // problems for the topic.
        // So our LessonModel subclass could do a similar thing with the standard.  The search through the problem history would be a little slower
        // because we'd have to lookup the Problem object to get the standards.


        // gets the list of problems that aren't solved (recently)
        List<Integer> probIds = getUnsolvedProblems();
        if (probIds == null || probIds.size() == 0)
            return null;
        // We want to grab a new problem based on the position of the last problem and whether it should be either easier, harder, or the same.
        // If the last problem was solved, it won't be in the probIds list.  If it was not solved, it will be.
        if (lastProblemScore != null && !lastProblemScore.isCorrect()) {
            int lastProbId = smgr.getStudentState().getCurProblem();
            int loc = probIds.indexOf(lastProbId);
            if (loc != -1)
                probIds.remove(loc);
        }


        int lastIx = state.getCurProblemIndexInLesson();
        int nextIx=-1;
        // lastIx is -1 when the topic is new.
        if (lastIx == -1)
            nextIx = (int) Math.round((probIds.size()-1) / parameters.getDifficultyRate());

        if (nextIx == -1 && nextDiff == LessonModel.difficulty.EASIER) {
            if (lastIx <= 0)
                ;
            nextIx =(int) Math.round(lastIx / parameters.getDifficultyRate());
        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.HARDER) {
            if (lastIx >= probIds.size())
                ;
            nextIx = lastIx + ((int) Math.round((probIds.size()-1 - lastIx) / parameters.getDifficultyRate()));

        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.SAME) {
            nextIx = Math.min(lastIx, probIds.size()-1);
        }
        nextIx = Math.min(nextIx, probIds.size()-1);
        Problem p=null;
        if (nextIx != -1) {
            int nextProbId = probIds.get( nextIx);
            state.setCurProblemIndexInLesson(nextIx);

        // maybe we can do away with these and just make it work correctly when this method can't find a problem within the index range.

//        state.setLessonHasEasierProblem(nextIx > 0);
//        if (nextIx < probIds.size() - 1)
//            state.setLessonHasHarderProblem(true);
//        else state.setLessonHasHarderProblem(false);
        // it takes 125 ms to get to here

             p= ProblemMgr.getProblem(nextProbId);
        }
        return p;
    }

    /**
     * precondition:  This method is only called if we know the topic has no upcoming content failure and all other conditions for continuing in a topic
     * are met.    In theory,  there should be no fencepost errors based on this.
     */
    public TopicModel.difficulty getNextProblemDifficulty(SessionManager smgr, PreviewProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        long t = System.currentTimeMillis();
        // DM 2/18 - note that this will take into account if curProb is broken and return SAME difficulty
        return TopicModel.difficulty.SAME;
    }

    public Problem selectProblemUsingPreviewDifficulty(SessionManager smgr, ProblemScore lastProblemScore, String problemPreviewData) throws Exception, SQLException {
        return null;
    }



    @Override
    public void setParameters(PedagogicalModelParameters params) {
        this.parameters = params;
    }
}
