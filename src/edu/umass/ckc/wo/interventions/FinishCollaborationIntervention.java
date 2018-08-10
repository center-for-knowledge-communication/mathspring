package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/13/15
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class FinishCollaborationIntervention extends InputResponseIntervention implements NextProblemIntervention{
    private String destination;

    public FinishCollaborationIntervention(String source){
        destination = source;
    }

    public String getType () {
        return "FinishCollaborationIntervention";
    }

    public String getDialogHTML () {
        String str = "<div><p>Finished working together.<br/>";

        str+="</div>";
        return str;
    }

    @Override
    public JSONObject buildJSON(JSONObject jo) {
        super.buildJSON(jo);
        jo.element("source", destination);
        return jo;
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
