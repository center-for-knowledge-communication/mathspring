package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * TimeoutInterventions are returned to the client and can put up a dialog with a message just like other interventions, but there
 * is no continue or submit button.   Instead the intervention sits on screen with a setWait that runs for the waitTime.   It sends
 * an InterventionTimeoutEvent to the server after its wait period.  The InterventionTimeoutEvent
 * gets sent back to the InterventionSelector that generated this
 * intervention and it can return an indication to the client to keep spinning its wait loop, a new intervention, or a new problem.
 * User: david
 * Date: 5/21/15
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TimeoutIntervention extends InputResponseIntervention{

    public long waitTime;

    public void setWaitTime(int i){
        waitTime = i;
    }


    @Override
    public JSONObject buildJSON(JSONObject jo) {
        super.buildJSON(jo);
        jo.element("timeoutwait", waitTime);
        return jo;
    }
}
