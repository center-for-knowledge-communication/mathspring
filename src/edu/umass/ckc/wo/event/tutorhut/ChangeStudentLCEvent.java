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
 * Frank	07-03-21	handle confidence and action fields
 */
public class ChangeStudentLCEvent extends IntraProblemEvent {
	
	int LCid;
	int sessionId;

    public ChangeStudentLCEvent(ServletParams p) throws Exception {
        super(p);
        
        LCid = p.getInt("LCid");
        sessionId = p.getInt("sessionId");
        
    }

    public int getLCid() {
        return LCid;
    }

    public int getSessionId() {
        return sessionId;
    }
    
}