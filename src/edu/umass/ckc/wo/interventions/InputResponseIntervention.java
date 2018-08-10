package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class InputResponseIntervention extends SimpleBaseIntervention {

    protected boolean isInputIntervention = true;
    protected String destinationIS; // the intervention selector that selected this

    public String getFormOpen () {
        StringBuilder sb = new StringBuilder();
        sb.append("<form id=\"inputResponseForm\">");
        return sb.toString();
    }

    public abstract String getType ();
    public abstract String getDialogHTML ();

    public String getDestinationIS() {
        return destinationIS;
    }

    public void setDestinationIS(String destinationIS) {
        this.destinationIS = destinationIS;
    }

    public JSONObject buildJSON(JSONObject jo) {
        jo.element("activityType", Intervention.INTERVENTION);

        jo.element("isInputIntervention",this.isInputIntervention);
        jo.element("interventionType",getType());
        jo.element("destinationIS",getDestinationIS());
        String html = "<!DOCTYPE html><html>"+getDialogHTML() + "</html>";
//        String html = "<!DOCTYPE html><html></html>";
        jo.element("html", html);
        return jo;
    }



}
