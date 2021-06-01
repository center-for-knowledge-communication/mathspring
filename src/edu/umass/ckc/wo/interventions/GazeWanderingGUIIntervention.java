package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 */
public class GazeWanderingGUIIntervention extends InputResponseIntervention {
    public static final String BEFORE="before";
    public static final String AFTER="after";
    public static final String SHOW="show";
    public static final String HIDE="hide";
    public static final String HIGHLIGHT="highlight";

    protected String when;
    protected String dialogHTML;
    protected String component; // the component of the GUI to change.
    protected String action; // show/hide/highlight
    protected String notify;

    public GazeWanderingGUIIntervention(String when, String dialogHTML, String notify, String component, String action) {
        super();
        this.isInputIntervention = true;
        this.when = when;
        this.dialogHTML = dialogHTML;
        notify = notify;
        this.component = component;
        this.action = action;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getDialogHTML () {
        String str = "<div>" + dialogHTML;
        str+="</div>";
        return str;
    }

    public String getType () {
        return "ChangeGUIIntervention";
    }

    public void setDialogHTML(String dialogHTML) {
        this.dialogHTML = dialogHTML;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        notify = notify;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
    	component = component;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
    	action = action;
    }


    
    
    public JSONObject buildJSON(JSONObject jo) {
        jo.element("changeGUI",true);
        jo.element("notify",this.notify);
        jo.element("when",this.when);
        jo.element("component", this.component);
        jo.element("action", this.action);
        return super.buildJSON(jo);
    }

}
