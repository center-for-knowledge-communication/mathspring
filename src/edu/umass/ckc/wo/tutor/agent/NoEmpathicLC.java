package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.response.AttemptResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/13/14
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoEmpathicLC extends EmotionalLC {

    public Response processNextProblemRequest(SessionManager smgr, NextProblemEvent e, Response r) throws Exception {
        AffectStudentModel sm;
        try {
            sm = (AffectStudentModel) smgr.getStudentModel();
        } catch (Exception ex) {
            throw new DeveloperException("You must use an AffectStudentModel when learning companions are part of the pedagogy");
        }
        List<String> l = selectEmotions(sm,r, smgr);
        getBestClip(clips);
        addCharacterControl(r);
        return r;


    }

    public Response processAttempt (SessionManager smgr, AttemptEvent attemptEvent, AttemptResponse r) throws Exception {
        String userInput = attemptEvent.getUserInput() ;
        StudentState state = smgr.getStudentState();
        // N.B. The Pedagogical Model that calls this must grade the user input and set the isCorrect value
        // within the AttemptEvent - we no longer rely on what Flash passes for isCorrect
        //If it is an irncorrect response.
        if ( ! attemptEvent.isCorrect())   {
            if ( smgr.getStudentState().getNumAttemptsOnCurProblem() >= 2 ) { //Only the first time
                r= getIncorrectResponse(r, smgr) ;
                getBestClip(clips);
                addLearningCompanionToResponse(r);
                return r;
            }

            addEmotion("idle") ;
            getBestClip(clips);
            addLearningCompanionToResponse(r);
            return r ;
        }

        r= getCorrectResponse(r, smgr) ;
        getBestClip(clips);
        addLearningCompanionToResponse(r);
        return r;
    }

}
