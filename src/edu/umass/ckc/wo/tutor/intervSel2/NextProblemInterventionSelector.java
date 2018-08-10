package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class NextProblemInterventionSelector extends InterventionSelector  {

    private boolean buildProblem=false;

    public NextProblemInterventionSelector(SessionManager smgr) {
        super(smgr);
    }



    /**
     * Subclasses that select interventions at the time of nextProblem event must override this
     *
     * @param e
     * @return
     * @throws Exception
     *
     */
    public abstract NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception;

    public abstract Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception;
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception{
        return null;
    }


    public boolean isBuildProblem() {
        return buildProblem;
    }

    public void setBuildProblem(boolean buildProblem) {
        this.buildProblem = buildProblem;
    }

    public String getUserInputXML() throws Exception {
       return this.userInputXML;
    }
}
