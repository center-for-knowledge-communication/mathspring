package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.db.DbCollaborationLogging;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.FinishCollaborationIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutormeta.Intervention;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/29/15
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollabPedagogicalModel extends BasePedagogicalModel {

    public CollabPedagogicalModel(){
        super();
    }

    public CollabPedagogicalModel(SessionManager smgr, Pedagogy pedagogy) throws SQLException {
        super(smgr, pedagogy);
    }

    public boolean isCollaborating(int studId) {
        return CollaborationManager.requestExists(studId) && CollaborationManager.getRequestedPartner(studId) != null;
    }

    public Response processNextProblemRequest(NextProblemEvent e) throws Exception {
        //TODO: determine if student has escaped the problem that was intended and terminate collaboration (ex: switching topics)
        int studId = smgr.getStudentId();
        if(isCollaborating(studId)){
            CollaborationManager.removeRequest(studId);
            DbCollaborationLogging.saveEvent(smgr.getConnection(), smgr.getStudentId(), 0, null, "CollaborationFinishedAlert_Originator");
            Intervention interv = new FinishCollaborationIntervention("Originator");
            smgr.getStudentState().setLastIntervention("edu.umass.ckc.wo.tutor.intervSel2.CollaborationIS");
            return new InterventionResponse(interv);
        }
        return super.processNextProblemRequest(e);
    }

    @Override
    protected boolean shouldForceNextProblem() {
        return !isCollaborating(smgr.getStudentId());
    }

    @Override
    public boolean isCollaborative () {
        return true;
    }
}