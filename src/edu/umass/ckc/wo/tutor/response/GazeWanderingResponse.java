package edu.umass.ckc.wo.tutor.response;
import edu.umass.ckc.wo.tutormeta.Intervention;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.Connection;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.db.DbGaze;
import edu.umass.ckc.wo.interventions.GazeWanderingGUIIntervention;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * User: Frank
 * Date: Jun 26, 2021
 * Frank	07-03-21	Added capture of intervention params for logging in eventLog, improved algorithm for parsing textBoxChoices, handle new param eventLogId
 */

public class GazeWanderingResponse extends Response {

    private Intervention intervention;
    private JSONObject params;
    private GazeWanderingGUIIntervention GazeParams;
    private int flashScreen = 0;
    private int flashBox = 0;
    private int textBox = 0;
    private int playSound = 0;
    private int LCompanion = 0;
    private String LCFilename = "";
    private String textBoxChoice = "";
    private String interventions = "";
    private int eventLogId = 0;
    private JSONObject gazeJSONData;
    
    public GazeWanderingResponse(Connection connection, int studentId, int classId, JSONObject gazeJSONData, int newGazeEventId) throws Exception {
        super();

        this.gazeJSONData = gazeJSONData;
        
        Object valueObj = null;
    	// Get student gaze params
        String gazeParams = DbGaze.getStudentParams(connection, studentId, classId,1);
    	JSONObject gazeParamsJsonObject = (JSONObject)JSONSerializer.toJSON(gazeParams);    	

		JSONObject gazintervJsonObject = new JSONObject();    	
    	
    	if (newGazeEventId > 0) {
    		gazintervJsonObject.putIfAbsent("newGazeEventId", newGazeEventId);
    	}
    	
    	if (gazeParamsJsonObject.get("gazinterv_flashScreen") == null) {
       		gazintervJsonObject.putIfAbsent("flashScreen", 0);
    	}
    	else {
    		gazintervJsonObject.putIfAbsent("flashScreen", gazeParamsJsonObject.get("gazinterv_flashScreen"));
    	}
    	
    	if (gazeParamsJsonObject.get("gazinterv_flashBox") == null) {
    		gazintervJsonObject.putIfAbsent("flashBox", 0);
    	}
    	else {
    		gazintervJsonObject.putIfAbsent("flashBox", gazeParamsJsonObject.get("gazinterv_flashBox"));
    	}
    	
    	if (gazeParamsJsonObject.get("gazinterv_playSound") == null) {    	
    		gazintervJsonObject.putIfAbsent("playSound", 0);
    	}
    	else {
        	gazintervJsonObject.putIfAbsent("playSound", gazeParamsJsonObject.get("gazinterv_playSound"));    		
    	}

    	Random rand = new Random();
    	
    	if (gazeParamsJsonObject.get("gazinterv_LCompanion") == null) {   	
    		LCompanion = 0;
    		gazintervJsonObject.putIfAbsent("LCompanion", 0);
    	}
    	else {
    		LCompanion = 1;
    		gazintervJsonObject.putIfAbsent("LCompanion", gazeParamsJsonObject.get("gazinterv_LCompanion"));
    	}
    	if (LCompanion > 0) {
    		valueObj = gazeParamsJsonObject.get("gazinterv_LCMessageIDs");
	
	    	String jsonAsString = valueObj.toString();
	    	//we need to remove the leading and the ending quotes and square brackets
	    	jsonAsString = jsonAsString.replace("[","");
	    	jsonAsString = jsonAsString.replace("]","");
	    	//split wherever the String contains ","
	    	String[] arr = jsonAsString.split(",");    	
	    	  	
	    	int index = rand.nextInt(arr.length);
	
	    	String str = arr[index];
	    	int key = Integer.valueOf(str);
	    	
	    	LCFilename = DbGaze.getLCFileName(connection,key);

    		gazintervJsonObject.putIfAbsent("LCFilename", LCFilename);
    	}
    	else {
    		LCFilename = "";
    	}

    	if (gazeParamsJsonObject.get("gazinterv_textBox") == null) {
    		textBox = 0;
    		gazintervJsonObject.putIfAbsent("textBox", gazeParamsJsonObject.get("gazinterv_textBox"));
    	}
    	else {
    		textBox = 1;
    		gazintervJsonObject.putIfAbsent("textBox", gazeParamsJsonObject.get("gazinterv_textBox"));
    	}
    	if (textBox > 0) {
    		JSONArray textArr = (JSONArray) gazeParamsJsonObject.get("gazinterv_textBoxChoices");
    		int textIndex = rand.nextInt(textArr.size());
    		textBoxChoice = textArr.getString(textIndex);
    		String shortText = textBoxChoice.substring(0, 12) + "...";
    		gazintervJsonObject.putIfAbsent("textBoxChoice", shortText);
    	}
    	else {
    		textBoxChoice = "";
    	}
    	interventions = gazintervJsonObject.toString();    	   		

        GazeParams = new GazeWanderingGUIIntervention(flashScreen, flashBox, textBox, playSound, LCompanion, LCFilename, textBoxChoice);                
               
        buildJSON();
    }




    public String logEventName() {

        return interventions ;
    }

    public String getCharacterControl() {
    	
    	return LCFilename;
    }

    public void putEventLogId(int eventLogId) {
    	this.eventLogId = eventLogId;
    }

    public int getEventLogId() {
    	return this.eventLogId;
    }
    
    public JSONObject paramsBuildJSON(GazeWanderingGUIIntervention GazeParams) {
    	JSONObject pj = new JSONObject();
    	
    	pj.element("flashScreen", GazeParams.getFlashScreen());
    	pj.element("flashBox", GazeParams.getFlashBox());
    	pj.element("textBox", GazeParams.getTextBox());
    	pj.element("playSound", GazeParams.getPlaySound());
    	pj.element("LCompanion", GazeParams.getLCompanion());
    	pj.element("LCFilename", GazeParams.getLCFilename());
    	pj.element("textBoxChoice", GazeParams.getTextBoxChoice());
        return pj;
    }

    public JSONObject buildJSON() {
//        this.jsonObject = new JSONObject();       
        params = paramsBuildJSON(GazeParams);
        this.jsonObject.element("params",params);
        this.jsonObject.element("interventionType","gazeWandering");
        return this.jsonObject;
    }
/*    
    public View getView () {

    	String action = this.gazeJSONData.getString("action");
    	if (action.equals("Intervention")) {
	    	return new View() {
	            public String getView() {
	                return buildJSON().toString();
	            }
	        };
    	}
    	else return null;
    }
*/
}