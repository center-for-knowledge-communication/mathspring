package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.HighlightHintButtonIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemGrader;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.

Frank	12/02/2021	Issue #556 send highlight intervention for nextProb button
 */


public class HighlightHintButtonIS extends AttemptInterventionSelector {

    public HighlightHintButtonIS(SessionManager smgr) {
        super(smgr);
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
        this.pedagogicalModel=pedagogicalModel;
    }

    @Override
    public Intervention selectIntervention(AttemptEvent e) throws Exception {
        StudentState st = smgr.getStudentState();
        boolean isSolved= new ProblemGrader(smgr).isAttemptCorrect(st.getCurProblem(),e.getUserInput());
        if (isSolved) {
        	return new HighlightHintButtonIntervention(null,null,false,"nextProb","highlight");
        }
        else {
        	String threshold= getConfigParameter2("threshold");
        	int numMistakesAllowedBeforeHighlight=2;
        	if (threshold!=null)
        		numMistakesAllowedBeforeHighlight= Integer.parseInt(threshold);
        	if (st.getNumMistakesOnCurProblem()==numMistakesAllowedBeforeHighlight && st.getNumHintsGivenOnCurProblem()==0)
        		return new HighlightHintButtonIntervention(null,null,false,"Hint","highlight");
        	else return null;
        }
    }

    @Override
    public Response processContinueAttemptInterventionEvent(ContinueAttemptInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseAttemptInterventionEvent(InputResponseAttemptInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
