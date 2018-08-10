package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/4/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MPPReturnToHutEvent extends MPPTopicEvent {
    protected long probElapsedTime;
    protected static String PROB_ELAPSED_TIME = "probElapsedTime";
    protected String probId;

    public MPPReturnToHutEvent(ServletParams p) throws Exception {
        super(p);
        String et = p.getString(PROB_ELAPSED_TIME,"0");
        long etl=0;
        try {
            etl = Long.parseLong(et);
        } catch (Exception e) {
            etl=0;
        }
        probElapsedTime = etl;
        probId = p.getString("probId");
    }

    public long getProbElapsedTime() {
        return probElapsedTime;
    }

    public String getProbId() {
        return probId;
    }
}
