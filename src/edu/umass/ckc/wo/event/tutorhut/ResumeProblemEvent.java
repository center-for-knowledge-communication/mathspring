package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent by Flash client when the problem is displayed
 * Event action: beginProblem
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class ResumeProblemEvent extends IntraProblemEvent {

    private int probId;

    public ResumeProblemEvent(ServletParams p) throws Exception {
        super(p);
        setProbId(p.getInt("probId"));
    }

    public int getProbId() {
        return probId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }
}