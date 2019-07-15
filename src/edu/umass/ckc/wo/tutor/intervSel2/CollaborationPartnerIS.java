package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.collab.CollaborationState;
import edu.umass.ckc.wo.db.DbCollaborationLogging;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/13/15
 * Time: 2:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationPartnerIS extends NextProblemInterventionSelector {

    //Stores state data, such as point in collaboration, time since collaboration
    private CollaborationState state;

    private static Logger logger = Logger.getLogger(CollaborationIS.class);

    private String partnerName = null;

    public CollaborationPartnerIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = CollaborationManager.getCollaborationState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel){
        this.pedagogicalModel=pedagogicalModel;
    }

    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception{
        // The helper is given an intervention that tells
        // them to help the person who is next to them (partnerName).
        //  This intervention plays in the client along with a loop that runs and sends an event every second.
        //   This event is handled below in  processContinueNextProblemInterventionEvent
        CollaborationPartnerIntervention interv = new CollaborationPartnerIntervention(smgr.getLocale());
        interv.setPartner(partnerName);
        return interv;
    }

    //Tell the helper that they are about to work with a partner to give them some help.
    public NextProblemIntervention selectInterventionWithId(int id) throws Exception{
        // the person who is waiting for help is the given id.   This is the partner.
        partnerName = CollaborationManager.getPartnerName(smgr.getConnection(), id); // get the partner's name
        // update DB with collab event indicating the two students are starting to collaborate
        DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), id, null, "CollaborationInstructions_Partner");
        CollaborationPartnerIntervention interv = new CollaborationPartnerIntervention(smgr.getLocale());
        interv.setPartner(partnerName);
        return interv;
    }

    //This handles the input from the last intervention saying they are done and then clicking OK
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception{
        state.triggerCooldown();
        DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationFinishedClickedOK_Partner");
        return null;
    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        // while the helper is working with the originator this is true and this returns the intervention that tells the helper
        // he must work with the originator.  Happens every second.    This partnership ends when they click the nextproblem button
        // and is cleaned out by the CollaborationOriginatorIS
        if(CollaborationManager.isPartner(smgr.getStudentId())){
            SameIntervention interv = new SameIntervention();
            //    CollaborationPartnerIntervention interv = new CollaborationPartnerIntervention();
            //    Integer partnerId = CollaborationManager.getRequestingPartner(smgr.getStudentId());
            // this breaks the originator out of their wait loop
            //  interv.setPartner(CollaborationManager.getPartnerName(conn, partnerId));
            return interv;
        }
        // this happens when the collaboration is done.  THe reason they are not partners anymore (above condition of if)
        // is because the originator clicks NextProblem button which then sends an event to CollaborationIS which removes the partnership.
        //  So we tell the helper he is done.
        DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationFinishedAlert_Partner");
        Intervention interv= new FinishCollaborationIntervention("Partner");
        return interv;
    }

}