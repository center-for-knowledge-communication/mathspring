package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 5/4/15
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterventionTimeoutEvent extends IntraProblemEvent {

    private long timeWaiting;

    public InterventionTimeoutEvent(ServletParams p) throws Exception {
        super(p);
        this.timeWaiting = p.getLong("timeWaiting",0);
    }

    public long getTimeWaiting() {
        return timeWaiting;
    }
}
