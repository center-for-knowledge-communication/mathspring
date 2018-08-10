package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class InformationIntervention extends InputResponseIntervention {

    public JSONObject buildJSON(JSONObject jo) {
        this.isInputIntervention = false;
        return super.buildJSON(jo);
    }




}
