package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/2/14
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowMPPIntervention extends SimpleBaseIntervention implements NextProblemIntervention {
    protected boolean buildProblem;

    public ShowMPPIntervention(boolean buildProblem) {
        this.buildProblem = buildProblem;
    }

    @Override
    public boolean isBuildProblem() {
        return buildProblem;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public JSONObject buildJSON(JSONObject jo) {
        jo.element("interventionType","ShowMPPButton");
        return jo;
    }

    public String getType () {
        return "ShowMPPIntervention";
    }

}
