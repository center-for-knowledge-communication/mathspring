package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.event.tutorhut.ContinueEvent;
import edu.umass.ckc.wo.event.tutorhut.InputResponseEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;

import java.sql.Connection;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 29, 2005
 * Time: 5:09:04 PM
 */
public abstract class InterventionSelector {

    protected SessionManager smgr;
    protected Connection conn;
    protected StudentState studentState;
    protected StudentModel studentModel;

    public InterventionSelector () {
    }

    public void init (SessionManager smgr) {
        this.smgr = smgr;
        this.conn = smgr.getConnection();
        this.studentState = smgr.getStudentState();
        this.studentModel = smgr.getStudentModel();
    }

    // subclasses that implement this are defunct
//    public abstract Intervention selectIntervention(NextProblemEvent e) throws Exception;


    /**
     * Subclasses that select interventions at the time of nextProblem event must override this
     *
     *
     * @param pedagogicalModel
     * @param e
     * @return
     * @throws Exception
     */
     public abstract Intervention selectIntervention(PedagogicalModel pedagogicalModel, NextProblemEvent e) throws Exception;
    /**
     * Subclasses that select intervention at the time of attempt event must override this.
     * @param e
     * @return
     * @throws Exception
     */
     public abstract Intervention selectIntervention(AttemptEvent e) throws Exception ;



    
    /**
     * When an intervention is shown to the student it may request input (a yes/no question or a survey about feelings, etc).
     * The response from the student is sent as an InputResponseEvent to the InterventionSelector.   It interprets the student's
     * inputs and may return more interventions or returns null if it is done.
     * @param e
     * @return
     */
    public abstract Intervention processInputResponse(InputResponseEvent e) throws Exception;


    /**
     * When an intervention is shown it may have a simple continue button that the user must click to end the intervention.
     * This even needs to be processed by the InterventionSelector.   It may choose another Intervention or it may be done
     * in which case it will return null.
     * @param e
     * @return
     */
    public abstract Intervention processContinue (ContinueEvent e) throws Exception;


    /**
     * Each intervention selector must have a unique name (a string) that the tutoring engine can use
     * as a way of keeping state.  It must know which intervention selector produced the last intervention
     * so that when a response comes back from the student it can then forward this response to the
     * correct intervention selector.   The name of the class is sufficient to uniquely identify it.
     * @return
     */
    public String getUniqueName () {
        return this.getClass().getName();
    }


}
