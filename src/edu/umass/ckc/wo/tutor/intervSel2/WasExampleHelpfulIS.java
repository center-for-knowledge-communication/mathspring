package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.event.tutorhut.ContinueNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 6/17/15
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class WasExampleHelpfulIS extends NextProblemInterventionSelector {

    public WasExampleHelpfulIS(SessionManager smgr) {
        super(smgr);
    }

    @Override
    /**
     * Should only return an intervention right after an example has been shown.   This would not include the demo
     * problem shown at the beginning of topics.   It is for the example that is within a problem.
     */
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
