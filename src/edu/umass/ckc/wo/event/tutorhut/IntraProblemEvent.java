package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jun 23, 2009
 * Time: 10:08:04 AM
 */
public class IntraProblemEvent extends TutorHutEvent {
    protected long probElapsedTime;
    public static final String PROB_ELAPSED_TIME = "probElapsedTime";

    public IntraProblemEvent () {}


    public IntraProblemEvent (ServletParams p) throws Exception {
        super(p);
        setProbElapsedTime(p.getLong(PROB_ELAPSED_TIME));
    }

    public long getProbElapsedTime() {
        return probElapsedTime;
    }

    public void setProbElapsedTime(long probElapsedTime) {
        this.probElapsedTime = probElapsedTime;
    }
}
