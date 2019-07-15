package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.collab.CollaborationState;
import edu.umass.ckc.wo.db.DbCollaborationLogging;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.interventions.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/13/15
 * Time: 2:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationOriginatorIS extends NextProblemInterventionSelector {

    //Stores state data, such as point in collaboration, time since collaboration
    private CollaborationState state;

    private static Logger logger = Logger.getLogger(CollaborationIS.class);

    public CollaborationOriginatorIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = CollaborationManager.getCollaborationState(smgr);
    }

    //CollaborationOriginatorIS works a little differently, in that it gets called several times without being setServletInfo'd,
    // via the SameIntervention path? or perhaps because it keeps getting thrown back as an InterventionResponse below
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel){
        this.pedagogicalModel = pedagogicalModel;
    }

    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception{
        // This condition is true right after the originator is given his final intervention before starting the problem.   It tells them
        // how to work with their partner and they click OK.  That response is processed by  processInputResponseNextProblemInterventionEvent
        // below and it returns null so that the pedagogical model then attempts to find an intervention (which results in this being called)
        // None should be available (actually nothing guarantees this - it would be awful if some intervention (like look at the MPP) were
        // to suddenly come up; to prevent this every intervention selector would need to check that a partnership doesn't exist.
        // SOLUTION:  CollabPedagogicalModel will have to behave differently than BasePedagogicalModel when processing InputResponses
        //TODO: Better way to handle this transition?
        if(CollaborationManager.requestExists(smgr.getStudentId()) && CollaborationManager.getRequestedPartner(smgr.getStudentId()) != null){
//            CollaborationManager.removeRequest(smgr.getStudentId());
            return null;
        }
        else{
            // This intervention has inputs so that the student may accept or decline.
            DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationOfferedBySystem_Originator");
            return new CollaborationOptionIntervention(smgr.getLocale());
        }
    }

    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception{
        String option = e.getServletParams().getString(CollaborationTimedoutIntervention.OPTION);
        //CollaborationOptionIntervention: Yes, I want to work with a partner
        if(option != null && option.equals(CollaborationOptionIntervention.YES)) {
            //It's possible for both potential partners to get CollaborationOptionIntervention;
            // if so, they will both be stuck waiting for each other as partners unless we check if one has already
            // listed the other as a potential partner first
            Intervention interv = CollaborationIS.tryGettingPartnerIS(smgr, pedagogicalModel);
            if(interv != null) //There was a student waiting on this one as a partner
                return new InterventionResponse(interv);
            else { //Otherwise, proceed as normal (have them wait while we look for a partner)
                DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, option, "CollaborationAccepted_Originator");
                CollaborationManager.addRequest(smgr.getConnection(), smgr.getStudentId());
                // DM had modify to return an InterventionResponse rather than intervention
                return new InterventionResponse(new CollaborationOriginatorIntervention(smgr.getLocale()));
            }
        }
        //CollaborationTimedoutIntervention: Yes, I want to keep waiting for a partner
        else if(option != null && option.equals(CollaborationTimedoutIntervention.YES)){
            //It's possible for the same deadlock as above, so check for a partner first
            Intervention interv = CollaborationIS.tryGettingPartnerIS(smgr, pedagogicalModel);
            if(interv != null) //There was a student waiting on this one as a partner
                return new InterventionResponse(interv);
            else {
                DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, option, "CollaborationContinuedWaiting_Originator");
                // DM had modify to return an InterventionResponse rather than intervention
                return new InterventionResponse(new CollaborationOriginatorIntervention(smgr.getLocale()));
            }
        }
        //Either CollaborationTimedoutIntervention or CollaborationOptionIntervention, they wanted to exit out
        else if(option != null &&
                (   option.equals(CollaborationTimedoutIntervention.NO)
                        || option.equals(CollaborationOptionIntervention.NO_ALONE)
                        || option.equals(CollaborationOptionIntervention.NO_DECLINE))){
            CollaborationManager.decline(smgr.getStudentId());
            DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, option, "CollaborationDeclined_Originator");
            return null; //so that the next problem is selected
        }
        //Collaboration has been set up, the originator has clicked OK to start
        else if(CollaborationManager.requestExists(smgr.getStudentId())){
            DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationConfirmationToBeginClickedOK_Originator");
            return null; //so that the next problem is selected
        }
        //Collaboration has finished, the originator has clicked OK to return to individual tutoring
        else{
            //We want to make sure they can't just endlessly collaborate by spending the whole cooldown on the problem
            state.triggerCooldown();
            DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationFinishedClickedOK_Originator");
            Response r=  smgr.getPedagogicalModel().processNextProblemRequest(new NextProblemEvent(e.getElapsedTime(),e.getProbElapsedTime()));
            return r;
        }
    }

    // asks the originator if they want to continue to wait for the partner (helper).   This happens every 60 seconds and is triggered
    // by code in intervhandlers.js processCollaborationOriginatorIntervention
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        Integer partner = CollaborationManager.getRequestedPartner(smgr.getStudentId());

        // returns message to originator saying that they are waiting for partner
        if(partner == null){
            if(e.getTimeWaiting() >= state.getMaxPartnerWaitPeriod()){
                DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), 0, null, "CollaborationAskContinueWaiting_Originator");
                return new CollaborationTimedoutIntervention(smgr.getLocale());
            }
            else{
                return new SameIntervention();
            }
        }
        else{
            User u = DbUser.getStudent(smgr.getConnection(),partner);
            String name = (u.getFname() != null && !u.getFname().equals("")) ? u.getFname() : u.getUname();
            DbCollaborationLogging.saveEvent(conn, smgr.getStudentId(), partner, null, "CollaborationConfirmationToBeginAlert_Originator");
            Intervention interv = new CollaborationConfirmationIntervention(name, smgr.getLocale());
            return interv;
        }


//        rememberInterventionSelector(this);
    }
}