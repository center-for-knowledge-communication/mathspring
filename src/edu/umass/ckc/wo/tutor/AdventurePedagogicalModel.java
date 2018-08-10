package edu.umass.ckc.wo.tutor;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.probSel.AdventureProblemSelector;
import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.tutormeta.OldPedagogicalModel;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 16, 2005
 * Time: 6:50:38 PM
 */
public class AdventurePedagogicalModel extends OldPedagogicalModel {
    private SessionManager smgr;
    private AdventureProblemSelector problemSelector;

    public AdventurePedagogicalModel(SessionManager smgr, AdventureProblemSelector adventureProblemSelector) throws Exception {
        this.problemSelector =    adventureProblemSelector;
        this.smgr = smgr;
        problemSelector.init(smgr);
    }

    public String endActivity(EndActivityEvent e) throws Exception {
       return Problem.ADV_PROBLEM;

    }

    public Activity getActivity(StudentActionEvent e) throws Exception {
        smgr.getStudentState().setProbElapsedTime(e.getProbElapsed()); // set the probElapsedTime for the logger
        Problem p = this.problemSelector.selectProblem(e, this.smgr, null);
//          if (p != null)
//            return getAdventureProblemResponse(p, e.getElapsedTime());
//          else // null problem will be returned when adventure is all done.
//            return new Response("&action=returnToAdventure");
        return null;
    }

    private Response getAdventureProblemResponse(Problem p, long elapsedTime) {
        return new ProblemResponse(p);
    }
}
