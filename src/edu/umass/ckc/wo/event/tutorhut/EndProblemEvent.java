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

public class EndProblemEvent extends IntraProblemEvent {
    boolean isExample =false;  // NextProblem event is sent example/demo ends.  This indicates it.

    public EndProblemEvent(ServletParams p) throws Exception {
        super(p);
        isExample = p.getBoolean("isExample",false);
    }


    public boolean isExample() {
        return isExample;
    }
}