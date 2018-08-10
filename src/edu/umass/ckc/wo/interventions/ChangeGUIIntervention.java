package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 6/25/15
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ChangeGUIIntervention extends InputResponseIntervention {
    public static final String BEFORE="before";
    public static final String AFTER="after";
    public static final String SHOW="show";
    public static final String HIDE="hide";
    public static final String HIGHLIGHT="highlight";

    protected String when;
    protected String dialogHTML;
    protected String component; // the component of the GUI to change.
    protected String action; // show/hide/highlight
    protected boolean isNotify;

    protected ChangeGUIIntervention(String when, String dialogHTML, boolean notify, String component, String action) {
        super();
        this.isInputIntervention = true;
        this.when = when;
        this.dialogHTML = dialogHTML;
        isNotify = notify;
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

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }


    public JSONObject buildJSON(JSONObject jo) {
        jo.element("changeGUI",true);
        jo.element("notify",this.isNotify);
        jo.element("when",this.when);
        jo.element("component", this.component);
        jo.element("action", this.action);
        return super.buildJSON(jo);
    }

}
