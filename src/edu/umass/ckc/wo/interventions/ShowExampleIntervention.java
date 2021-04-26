package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/21/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowExampleIntervention extends ChangeGUIIntervention implements AttemptIntervention {


    public ShowExampleIntervention(String when, String dialogHTML, boolean notify, String component, String action) {
        super(when, dialogHTML, notify, component, action);
    }

    public String getType () {
        return "ShowExampleIntervention";
    }


    @Override
    public boolean isShowGrade() {
        return true;     // note that Flash problems grade themselves, so this will only work for HTML problems.
    }

    public JSONObject buildJSON(JSONObject jo) {

        jo.element("changeGUI",false);
        jo.element("notify",this.isNotify);
        jo.element("when",this.when);
        jo.element("component", this.component);
        jo.element("action", this.action);
        return super.buildJSON(jo);
    }



}
