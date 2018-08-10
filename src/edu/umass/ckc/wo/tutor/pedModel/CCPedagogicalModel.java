package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Lesson;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutormeta.StudentEffort;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/13/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CCPedagogicalModel extends BasePedagogicalModel {
    private static final Logger logger = Logger.getLogger(CCPedagogicalModel.class);

    private StudentLessonMgr studentLessonMgr;

    /**
     * When this pedagogical model is instantiated, it has to regain the state of the lesson for the student.
     * @param smgr
     * @param ped
     * @throws SQLException
     */
    public CCPedagogicalModel (SessionManager smgr, Pedagogy ped) throws Exception {
        super(smgr,ped);
        studentLessonMgr = new StudentLessonMgr(smgr);
        logger.debug("Instantiated CCPedagogicalModel");
    }


    public Response processStudentSelectsProblemRequest (NextProblemEvent e) throws SQLException {
        if (! e.isForceProblem())
            return null;

        ProblemResponse r = null;
        // If student has selected a particular problem, then they must have left challenge/review mode and are back in practice mode.
        smgr.getStudentState().setInReviewMode(false);
        smgr.getStudentState().setInChallengeMode(false);
        smgr.getStudentState().setInPracticeMode(true);

        Problem p = ProblemMgr.getProblem(Integer.parseInt(e.getProbId()));
        p.setMode(Problem.PRACTICE);
        return new ProblemResponse(p);
    }

    protected boolean gradeProblem (long probElapsedTime) throws Exception {
        StudentState state = smgr.getStudentState();

        Problem lastProb = ProblemMgr.getProblem(state.getCurProblem());
        String lastProbMode = state.getCurProblemMode();
        Problem curProb=null;
        ProblemScore score=null;

        if (lastProb != null  && lastProbMode.equals(Problem.PRACTICE))     {
            score = problemGrader.gradePerformance(lastProb);
//            nextDiff = problemGrader.getNextProblemDifficulty(score);
        }
//        else nextDiff = TopicModel.difficulty.SAME;
//        this.reasonsForEndOfTopic=  topicSelector.isEndOfTopic(probElapsedTime, nextDiff);
//        boolean topicDone = reasonsForEndOfTopic.isTopicDone();
        return false;
    }

    /**
     * Process a request for a next problem
     * @param  e  NextProblemEvent asks for the next problem
     * @return
     * @throws Exception
     */
    public Response processNextProblemRequest(NextProblemEvent e) throws Exception {

        // We have a fixed sequence which prefers forced problems, followed by topic intros, examples, interventions, regular problems.
        // If we ever want something more customized (e.g. from a XML pedagogy defn),  this would have to operate based on that defn

        Response r=null;
        StudentState state = smgr.getStudentState();
        Problem curProb=null;


        // only grade the problem if we aren't trying to force a topic or problem
        if (r == null)
            gradeProblem(e.getProbElapsedTime());

        boolean lessonDone = studentLessonMgr.isCurLessonDone();
        // TODO: if the lesson is done, we need to return something that tells the student this and then move on to the next lesson somehow
        if (!lessonDone) {
            // TODO assumption for now is that we go through the lesson in order (which amounts to harder problems)
            r = getNextProblem(e);
        }
        else r = ProblemResponse.NO_MORE_PROBLEMS;

        if (r == ProblemResponse.NO_MORE_PROBLEMS)
            ((ProblemResponse) r).setEndPage(TutorPage.END_TUTOR_FRAME_CONTENT);




        if (r != null && r instanceof ProblemResponse)
            curProb = ((ProblemResponse) r).getProblem();
        if (r != ProblemResponse.NO_MORE_PROBLEMS && learningCompanion != null )
            learningCompanion.processNextProblemRequest(smgr,e,r);
        if (curProb != null)
            smgr.getStudentModel().newProblem(state,curProb);
        if (r instanceof InterventionResponse)
            new TutorLogger(smgr).logNextProblemIntervention(e,(InterventionResponse) r);
        else new TutorLogger(smgr).logNextProblem(e, r.getCharacterControl(), "PracticeProblem");
        StudentEffort eff = studentModel.getEffort();
        r.setEffort(eff);
        return r;
    }


//    /**
//     * This is an override that is badly named because this PM doesn't even deal in topics.   But to support Assistments making
//     * calls that request Lessons or CUs   we package up the Lesson inside the NextProblemEvent and then call this to get it to play the lesson.
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    public ProblemResponse getProblemInTopicSelectedByStudent (NextProblemEvent e) throws Exception {
//        StudentState state = smgr.getStudentState();
//        Problem curProb=null;
//        ProblemResponse r;
//
//        boolean lessonDone = studentLessonMgr.isCurLessonDone();
//        // TODO: if the lesson is done, we need to return something that tells the student this and then move on to the next lesson somehow
//        if (!lessonDone) {
//            // TODO assumption for now is that we go through the lesson in order (which amounts to harder problems)
//            r = getProblem(e, ProblemGrader.difficulty.HARDER);
//        }
//        else r = ProblemResponse.NO_MORE_PROBLEMS;
//        if (r != null && r instanceof ProblemResponse)
//            curProb = ((ProblemResponse) r).getProblem();
//        if (learningCompanion != null )
//            learningCompanion.processNextProblemRequest(smgr,e,r);
//        if (curProb != null)
//            smgr.getStudentModel().newProblem(state,curProb);
//        new TutorLogger(smgr).logNextProblem(e, r.getCharacterControl());
//        StudentEffort eff = studentModel.getEffort();
//        r.setEffort(eff);
//        return r;
//    }


    public ProblemResponse getNextProblem(NextProblemEvent e) throws Exception {
        Problem p= studentLessonMgr.getNextProblem();
        ProblemResponse r=null;
        if (p != null) {
            p.setMode(Problem.PRACTICE);
//            smgr.getStudentState().setTopicNumPracticeProbsSeen(smgr.getStudentState().getTopicNumPracticeProbsSeen() + 1);
            problemGiven(p); // inform pedagogical move listeners that a problem is given.
            r = new ProblemResponse(p);
        }
        else r = ProblemResponse.NO_MORE_PROBLEMS;
        return r;
    }

    public StudentLessonMgr getStudentLessonMgr() {
        return studentLessonMgr;
    }

    private boolean isCUDone() {
        return false;

    }

    public void setLesson (Lesson l) {
        List<Lesson> ll = new ArrayList<Lesson>();
        ll.add(l);
        this.studentLessonMgr.setClassLessons(ll);
    }



}
