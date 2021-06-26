package edu.umass.ckc.wo.tutor.response;
import edu.umass.ckc.wo.tutormeta.Intervention;

import java.util.Random;
import java.sql.Connection;

import edu.umass.ckc.wo.db.DbGaze;
import edu.umass.ckc.wo.interventions.GazeWanderingGUIIntervention;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * User: Frank
 * Date: Jun 26, 2021
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
    
    public GazeWanderingResponse(Connection connection, int studentId, int classId) throws Exception {
        super();

        Object valueObj = null;
    	// Get student gaze params
        String gazeParams = DbGaze.getStudentParams(connection, studentId, classId);
    	JSONObject gazeParamsJsonObject = (JSONObject)JSONSerializer.toJSON(gazeParams);    	

    	valueObj = gazeParamsJsonObject.get("gazinterv_flashScreen");
    	flashScreen = (int)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_flashBox");
    	flashBox = (int)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_textBox");
    	textBox = (int)valueObj;
    	
    	valueObj = gazeParamsJsonObject.get("gazinterv_playSound");    	
    	playSound = (int)valueObj;

    	valueObj = gazeParamsJsonObject.get("gazinterv_LCompanion");
    	LCompanion = (int)valueObj;
    	
    	valueObj = gazeParamsJsonObject.get("gazinterv_LCMessageIDs");

    	String jsonAsString = valueObj.toString();
    	//we need to remove the leading and the ending quotes and square brackets
    	jsonAsString = jsonAsString.substring(2, jsonAsString.length() -2);
    	//split wherever the String contains ","
    	String[] arr = jsonAsString.split(",");    	
    	  	
    	Random rand = new Random();
    	int index = rand.nextInt(arr.length);

    	String str = arr[index];
    	int key = Integer.valueOf(str);
    	
   		String LCFilename = DbGaze.getLCFileName(connection,key);

    	valueObj = gazeParamsJsonObject.get("gazinterv_textBoxChoices");

    	jsonAsString = valueObj.toString();
    	//we need to remove the leading and the ending quotes and square brackets
    	jsonAsString = jsonAsString.substring(2, jsonAsString.length() -2);
    	//split wherever the String contains ","
    	arr = jsonAsString.split(",");    	
    	  	
    	index = rand.nextInt(arr.length);

    	String textBoxChoice = arr[index];
    	
    	
   		
   		
   		
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
        GazeParams = new GazeWanderingGUIIntervention(flashScreen, flashBox, textBox, playSound, LCompanion, LCFilename, textBoxChoice);                
               
        buildJSON();
    }




    public String logEventName() {

        return null ;
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
}