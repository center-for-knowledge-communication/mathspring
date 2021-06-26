package edu.umass.ckc.wo.event.tutorhut;
/**
 * Copyright (c) University of Massachusetts
 * Written by: Frank Sylvia
 * Date: Jun 26, 2021
 * Frank	06-26-21	Support for gaze detection
 */
import edu.umass.ckc.servlet.servbase.ServletParams;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import java.lang.Math;

/**
 * User: Frank
 * Date: May 25, 2021
 */
public class GazeWanderingEvent extends IntraProblemEvent {

    private String gazeJSONData;

    public GazeWanderingEvent(ServletParams p) throws Exception {
        super(p);
        this.gazeJSONData = p.getString("gazeJSONData", null);
    }

    public JSONObject getGazeJSONData() {
    	    	
    	JSONObject dbGazeEventJsonObject = new JSONObject();
    	try {
	    	JSONObject inGazeEventJsonObject = (JSONObject)JSONSerializer.toJSON(gazeJSONData);
	    	
	    	String strYaw = inGazeEventJsonObject.getString("yaw");
	    	if (strYaw.indexOf(".") > 0) {
	    		strYaw = strYaw.substring(0,strYaw.indexOf("."));
	    	}
	    	int yaw = Integer.parseInt(strYaw);
	    	if (yaw > 1) {
	    		dbGazeEventJsonObject.putIfAbsent("RightDegrees", String.valueOf(java.lang.Math.abs(yaw)));
	    		dbGazeEventJsonObject.putIfAbsent("LeftDegrees", "0");    		
	    	}
	    	else {
	    		dbGazeEventJsonObject.putIfAbsent("RightDegrees", "0");
	    		dbGazeEventJsonObject.putIfAbsent("LeftDegrees", String.valueOf(java.lang.Math.abs(yaw)));    		    		
	    	}
	    	
	    	String strPitch = inGazeEventJsonObject.getString("pitch");
	    	if (strPitch.indexOf(".") > 0) {
	    		strPitch = strPitch.substring(0,strPitch.indexOf("."));
	    	}
			int pitch = Integer.parseInt(strPitch);
	    	if (pitch > 0) {
	    		dbGazeEventJsonObject.putIfAbsent("UpDegrees", String.valueOf(java.lang.Math.abs(pitch)));
	    		dbGazeEventJsonObject.putIfAbsent("DownDegrees", "0");    		
	    	}
	    	else {
	    		dbGazeEventJsonObject.putIfAbsent("UpDegrees", "0");
	    		dbGazeEventJsonObject.putIfAbsent("DownDegrees", String.valueOf(java.lang.Math.abs(pitch)));    		    		
	    	}
	    	
	    	String strRoll = inGazeEventJsonObject.getString("roll");
	    	if (strRoll.indexOf(".") > 0) {
	    		strRoll = strRoll.substring(0,strRoll.indexOf("."));
	    	}
	    	int roll = Integer.parseInt(strRoll);
	    	dbGazeEventJsonObject.putIfAbsent("tiltDegrees",  String.valueOf(roll));
	    	
			dbGazeEventJsonObject.putIfAbsent("modelConfidence", "0");
	//		dbGazeEventJsonObject.putIfAbsent("modelConfidence", inGazeEventJsonObject.getString("modelConfidence"));
	
	    	dbGazeEventJsonObject.putIfAbsent("modelversionID", "1.0.0");
	//    	dbGazeEventJsonObject.putIfAbsent("modelversionID", inGazeEventJsonObject.getString("model_version"));
	
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
        return dbGazeEventJsonObject;
	}

    	
}