package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.event.StudentActivityEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.AdventurePedagogicalModel;
import edu.umass.ckc.wo.tutor.PrePostPedagogicalModel;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.TransactionLogger;
import edu.umass.ckc.wo.tutor.probSel.PrePostProblemSelectorImpl;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.tutormeta.OldPedagogicalModel;
import edu.umass.ckc.wo.tutormeta.PrePostProblemSelector;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.log.PrePostLogger;
import edu.umass.ckc.wo.enumx.Actions;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 16, 2005
 * Time: 6:21:44 PM
 */
public class StudentActionHandler {
    private static Logger logger =   Logger.getLogger(StudentActionHandler.class);
    private SessionManager smgr;
    private Connection conn;
    public Activity selectedActivity=null; // for debugging

    public StudentActionHandler(SessionManager smgr, Connection conn) {
        this.smgr = smgr;
        this.conn = conn;
    }

    public Activity getSelectedActivity() {
        return selectedActivity;
    }



    private OldPedagogicalModel getPedagogicalModel(String curLoc, StudentActivityEvent e) throws Exception {
        smgr.getStudentState().setProbElapsedTime(e.getProbElapsed()); // record the probElapsedTime in the student curLoc

        OldPedagogicalModel pm = null;

        if (curLoc != null && curLoc.equals(NavigationEvent.SAT_HUT)) {
            throw new DeveloperException("StudentActionHandler.getPedagogicalModel is being called on navigation" +
                    "to the tutor hut.  The WoPedagogicalModel would normally process this event, but we are " +
                    "trying to eliminate it, so this nav event has to be handled differently.");

        }
        // SAT activity requests are handled by the WoPedagogicalModel
//        if (curLoc != null && curLoc.equals(NavigationEvent.SAT_HUT)) {
//            Pedagogy p = new PedagogyBuilder().buildPedagogySelectors(conn,smgr.getStudentId());
//            pm = new WoPedagogicalModel(smgr, p.getProblemSelector(),
//                    p.getHintSelector(), p.getInterventionSelector(),p.getLearningCompanion(), e);
//        }
        if (curLoc != null && curLoc.equals(NavigationEvent.PREPOST_HUT))   {
            PrePostProblemSelector prePostSelector = new PrePostProblemSelectorImpl();
            pm = new PrePostPedagogicalModel(smgr, prePostSelector);
            // when student skips a prepost problem we want the userinput to be NULL not empty string
            if (e.getUserInput().trim().equals(""))
                e.setUserInput(null);
        }
        // Adventure activity requests are handled by the AdventurePedagogicalModel
        else if (curLoc != null && (curLoc.equals(NavigationEvent.A1) || curLoc.equals(NavigationEvent.A2) ||
                curLoc.equals(NavigationEvent.A3)))
            pm = new AdventurePedagogicalModel(smgr, Settings.adventureProblemSelector_);
        return pm;
    }


    /**
     *   All EndActivityEvents are sent to this handler for processing.
     * @param e
     * @return
     * @throws Exception
     */
    public View handleRequest(EndActivityEvent e) throws Exception {
        String curLoc = smgr.getStudentState().getCurLocation();  // find out where the student is in the GUI
        OldPedagogicalModel pm = getPedagogicalModel(curLoc,e);

        // Pedagogical Model handles the endActivity event and logs it.
        if (pm != null) {
            // action=endProblem signals the end of pre/post problem and we log it
            if (pm instanceof PrePostPedagogicalModel) {
                String activityName = pm.endActivity(e); // determines if pre or post problem
                // Reports involving pretest log data have all been updated to
                // work off the preposteventlog.   No need for log to EpisodicData2 DM 3/17/10
                //PrePostLogger.logEvent1(conn,e, Actions.endProblem,smgr.getStudentId(),smgr.getSessionNum(), activityName); // TODO this is what we should do when we commit to logging prepost to eventLog
                PrePostLogger.logEvent2(conn,e, Actions.endProblem,smgr.getStudentId(),smgr.getSessionNum(), activityName); // TODO this is what we should do when we commit to logging prepost to eventLog
                // TransactionLogger.logEvent( e, smgr, a);
            }
            else {
                String probType = pm.endActivity(e);
                TransactionLogger.logEvent((EndActivityEvent) e, smgr, probType);
            }
        }
        // for some reason we can't find a pm, lets log the curLoc so we can see where this end event is coming from
        else {
            logger.info("Got endEvent for session " + e.getSessionId() + " and curLoc was " + curLoc);
            TransactionLogger.logEvent((EndActivityEvent) e, smgr, curLoc);
        }
        return new Response().getView();
    }

    /**
     * All StudentActionEvents (which take place within a quiz such as a pretest are sent to this handler for
     * processing
     * @param e
     * @return
     * @throws Exception
     */
    public View handleRequest(StudentActionEvent e) throws Exception {

        String curLoc = smgr.getStudentState().getCurLocation();  // find out where the student is in the GUI
        OldPedagogicalModel pm = getPedagogicalModel(curLoc,e);  // will be using PrePostPedagogicalModel or AdventurePedagogicalModel

        // if curProb is null, then the getActivity will set a new Problem in the smgr
        final Activity r = pm.getActivity(e);
//        this.selectedActivity = r; // for debugging we need this

        TransactionLogger.logEvent( e, smgr, r);

        return new View() {
            public String getView() {
                return r.buildJSON(new JSONObject()).toString();
            }
        };
    }







    private PrePostProblemSelector determinePrePostProblemSelector () {
//        StudentProfile p = smgr.getStudentProfile();
        // group 100 is for system testing.  They get a fixed sequence of pre/post problems
//        if (p != null && (p.getGroup() == 100 || p.getGroup() == 200 || p.getGroup() == 300 || p.getGroup() == 400))
//            return Settings.prepostProblemSelectorTester;
        // all others get the regular selector
         return Settings.prepostProblemSelector;
    }
}
