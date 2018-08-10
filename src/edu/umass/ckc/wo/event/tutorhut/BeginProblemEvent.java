package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent by Flash client when the problem is displayed
 * Event action: beginProblem
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */

public class BeginProblemEvent extends TutorHutEvent {
    int probId;
    String mode;

    public BeginProblemEvent(ServletParams p) throws Exception {
        super(p);
        probId = p.getInt("probId");
        mode = p.getString("mode");
    }

    public int getProbId() {
        return probId;
    }


    public void setProbId(int probId) {
        this.probId = probId;
    }

    public String getMode() {
        return mode;
    }
}