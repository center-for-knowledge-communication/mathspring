package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class InterventionResponse extends Response {
    private Intervention interv;


    public InterventionResponse () {
        buildJSON();
    }

    public InterventionResponse(Intervention interv, String extra) {
        this.interv = interv;
        this.extraOutputParams = extra;
        buildJSON();
    }

    public InterventionResponse(Intervention interv) {
        this(interv,"");
        buildJSON();
    }


    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        // TODO For now the only intervention this correctly produces output for is ExternalActivity.   Its getFlashOut produces JSON
            return interv.buildJSON(jsonObject);

    }





    public String logEventName() {
        if (this.interv != null)
            return this.interv.logEventName();
        else return "intervention";
    }



    public Intervention getIntervention() {
        return interv;
    }


}