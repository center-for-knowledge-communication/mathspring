package edu.umass.ckc.wo.tutor.response;
import edu.umass.ckc.wo.tutormeta.Intervention;

import java.util.Iterator;

import java.sql.Connection;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbGaze;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.interventions.GazeWanderingGUIIntervention;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * User: Frank
 * Date: May 26, 2021
 */

public class GazeWanderingResponse extends Response {

    private Intervention intervention;
    private JSONObject params;
    private GazeWanderingGUIIntervention GazeParams;
    private String flashScreen = "";
    private String flashBox = "";
    private String playSound = "";
    private String LCompanion = "";
    
    public GazeWanderingResponse(Connection connection, int studentId) throws Exception {
        super();

        Object valueObj = null;
    	// Get student gaze params
        String gazeParams = DbGaze.getStudentParams(connection, studentId);
    	JSONObject gazeParamsJsonObject = (JSONObject)JSONSerializer.toJSON(gazeParams);    	

    	valueObj = gazeParamsJsonObject.get("gazinterv_flashScreen");
    	flashScreen = (String)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_flashBox");
    	flashBox = (String)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_playSound");
    	playSound = (String)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_LCompanion");
    	LCompanion = (String)valueObj;
/*    	
        Iterator<?> valueIterator = gazeParamsJsonObject.keys();
        while (valueIterator.hasNext()) {        	        
            String key = (String)valueIterator.next();
            Object valueObj = gazeParamsJsonObject.get(key);
            if (!key.equals("gazinterv_LCMessageIDs") ) {
            	System.out.println(key + "=" + (String)valueObj);
            }
        }
        */
        GazeParams = new GazeWanderingGUIIntervention(flashScreen, flashBox, playSound, LCompanion);                
               
        buildJSON();
    }




    public String logEventName() {

        return null ;
    }

    public JSONObject paramsBuildJSON(GazeWanderingGUIIntervention GazeParams) {
    	JSONObject pj = new JSONObject();
    	
    	pj.element("flashScreen", GazeParams.getFlashScreen());
    	pj.element("flashBox", GazeParams.getFlashBox());
    	pj.element("playSound", GazeParams.getPlaySound());
    	pj.element("LCompanion", GazeParams.getLCompanion());
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