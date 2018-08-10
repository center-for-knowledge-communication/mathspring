package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/11/16
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterleavedProblemSetProblemSelector extends BaseProblemSelector {

    private InterleavedProblemSetParams interleavedProblemSetParams;

    public InterleavedProblemSetProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        super(smgr, lessonModel, params);
        interleavedProblemSetParams = ((TopicModelParameters) lessonModelParameters).getInterleaveParams();
    }

    @Override
    public void setParameters(PedagogicalModelParameters params) {
    }

    // Assumption is that we only call this when we are in an interleaved problem set.
    @Override
    public Problem selectProblem(SessionManager smgr, NextProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        // if we are in a interleaved problem set, do the selection here; otherwise, the superclass can handle it.
        if (smgr.getExtendedStudentState().isInInterleavedProblemSet())
            return null;
        else {
            throw new DeveloperException("Called InterleavedProblemSetProblemSelector.selectProblem when not in a interleaved topic.");
        }
    }
}
