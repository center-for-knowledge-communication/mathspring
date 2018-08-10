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
public class EndExternalActivityEvent extends TutorHutEvent {
    public static final String XACT_ID = "xactId";
    public static final String PROB_ELAPSED_TIME = "probElapsedTime";
    public int xactId=-1;
    public long probElapsedTime;


    public EndExternalActivityEvent(ServletParams p) throws Exception {
        super(p);
        xactId=p.getInt(XACT_ID,-1);
        probElapsedTime = p.getLong(PROB_ELAPSED_TIME);
    }

    public int getXactId() {
        return xactId;
    }

    public long getProbElapsedTime() {
        return probElapsedTime;
    }
}