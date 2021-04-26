package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.Video;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.ShowVideoIntervention;
import edu.umass.ckc.wo.interventions.ShowVideoIntervention;
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
 */
public class ShowVideoIS extends AttemptInterventionSelector {

    public ShowVideoIS(SessionManager smgr) {
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
        String threshold= getConfigParameter2("threshold");
        int numMistakesAllowedBeforeHighlight=3;
        if (threshold!=null)
            numMistakesAllowedBeforeHighlight= Integer.parseInt(threshold);
        if (!isSolved && st.getNumMistakesOnCurProblem()==numMistakesAllowedBeforeHighlight) {
            Problem p = ProblemMgr.getProblem(smgr.getStudentState().getCurProblem());
            String vid = p.getVideo();

            Response r = new Video(vid);

            if (vid != null && !vid.equals("")) {        	
                   return new ShowVideoIntervention(null,null,false,"Video","Show");
            }
            else {
            	return null;
            }
        }
        else return null;
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
