package edu.umass.ckc.wo.tutor.intervSel2;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;
import edu.umass.ckc.wo.event.tutorhut.ContinueAttemptInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InputResponseAttemptInterventionEvent;
import edu.umass.ckc.wo.interventions.RapidAttemptIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;

import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.StudentModel;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuessingAskAffectIS extends AttemptInterventionSelector {

    public static final int GUESS_THRESHOLD_TIME = 10 * 1000;  // 10 seconds
    public static final int NUM_ATTEMPTS = 3;  // 3 attempts within threshold period is considered guessing

    public GuessingAskAffectIS (SessionManager smgr) {
        super(smgr);
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
        this.pedagogicalModel=pedagogicalModel;
    }

    @Override
    public Intervention selectIntervention(AttemptEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        int numAttempts = state.getNumAttemptsOnCurProblem();
        long inProbTime = e.getProbElapsedTime();
        System.out.println("Is a Guess: " + numAttempts + " " + inProbTime);
        if (inProbTime <= GUESS_THRESHOLD_TIME && numAttempts >= NUM_ATTEMPTS) {
            return new RapidAttemptIntervention();

        }
        return null;
    }

    @Override
    public Response processContinueAttemptInterventionEvent(ContinueAttemptInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseAttemptInterventionEvent(InputResponseAttemptInterventionEvent e) throws Exception {
        ServletParams params = e.getServletParams();
        String affect = params.getString(RapidAttemptIntervention.AFFECT);
        System.out.println("Student is " + affect);
        int levelInt = 5;

        StudentModel sm = smgr.getStudentModel();
        if (affect == null) return null;
        if (affect.equals(RapidAttemptIntervention.BORED)) {
            levelInt = 2;
            affect = AffectStudentModel.INTERESTED;
        }
        else if (affect.equals(RapidAttemptIntervention.FRUSTRATED))
            affect = AffectStudentModel.FRUSTRATED;
        String xml =  "<userInput emotion=\"" + affect + "\" level=\"" + levelInt + "\"/>";
        this.setUserInput(this,xml,e);
        // The AffectStudentModel is the place where this emotion data is being kept (by legacy system).
        // Since this whole thing is bogus,   I'm not going to try to find a better place for it.
        // This means that any pedagogy that involves hassling the user with these questions about how they feel
        // must use the AffectStudentModel
        if (sm instanceof AffectStudentModel)
            ((AffectStudentModel) sm).setLastReportedEmotion(affect,levelInt,e.getElapsedTime());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
