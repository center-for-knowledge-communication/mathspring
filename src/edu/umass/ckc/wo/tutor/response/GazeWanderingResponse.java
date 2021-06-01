package edu.umass.ckc.wo.tutor.response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.interventions.GazeWanderingGUIIntervention;
import net.sf.json.JSONObject;

/**
 * User: Frank
 * Date: May 26, 2021
 */

public class GazeWanderingResponse extends Response {

    private Intervention intervention;
    private JSONObject params;
    private GazeWanderingGUIIntervention GazeParams;

    public GazeWanderingResponse(Problem p) throws Exception {
        super();

        GazeParams = new GazeWanderingGUIIntervention("", "Your gaze is wandering", "Y", "", "Flash");                
               
        buildJSON();
    }




    public String logEventName() {

        return null ;
    }

    public JSONObject paramsBuildJSON(GazeWanderingGUIIntervention GazeParams) {
    	JSONObject pj = new JSONObject();
    	pj.element("when", GazeParams.getWhen());
    	pj.element("dialogHtml", GazeParams.getDialogHTML());
    	pj.element("notify", GazeParams.getNotify());
    	pj.element("component", GazeParams.getComponent());
    	pj.element("action", GazeParams.getAction());
        return pj;
    }

    public JSONObject buildJSON() {
//        this.jsonObject = new JSONObject();       
        params = paramsBuildJSON(GazeParams);
        this.jsonObject.element("params",params);
        this.jsonObject.element("interventionType","gazeWandering");
        return this.jsonObject;
    }
}