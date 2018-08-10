package edu.umass.ckc.wo.tutor.model;

import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;

/**
 * This lesson model is for teaching common core standards to a user coming in from another system (MARi).  It works with the CCSSProblemSelector which
 * can teach one common core standard.  This class makes sure the lesson switches to other standards (if more than one standard were requeted to be taught)
 * and makes sure that we don't show too many problems or go for too much time (both params passed in by MARi which then become lesson model params).
 * User: david
 * Date: 5/13/16
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CCLessonModel extends LessonModel {

    public CCLessonModel (SessionManager smgr) {
        super(smgr);
    }


    public void init (SessionManager smgr, Pedagogy pedagogy,
                      PedagogicalModel pedagogicalModel, PedagogicalMoveListener pedagogicalMoveListener) throws Exception {

        // The superclass constructor calls readLessonControl which takes the control parameters out of the pedagogy and
        // builds the intervention handling for the model
        super.init(smgr, pedagogy, pedagogicalModel, pedagogicalMoveListener);
    }


    // If the lesson is done because we have exceeded the time limit or the number of problems it is detected here.
    // If that happens, we return a NO_MORE_PROBLEMS response.
    private Response processNextProblemEvent (NextProblemEvent e) throws Exception {
        boolean cond = false;
        if (cond)
            return ProblemResponse.NO_MORE_PROBLEMS;
        else return null;
    }

    public Response processUserEvent(TutorHutEvent e) throws Exception {
        if (e instanceof NextProblemEvent) {
            return processNextProblemEvent( (NextProblemEvent) e);

        }
        // Assuming this lesson model is simple in that it doesn't generate interventions that require input (as TopicModel deals with in an analogous method)
        return null;
    }

}
