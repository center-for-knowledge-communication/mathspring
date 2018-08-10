package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent when user clicks the "next problem" button.
 * Event action: continue
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class ContinueEvent extends IntraProblemEvent {

    public ContinueEvent(ServletParams p) throws Exception {
        super(p);
    }
}