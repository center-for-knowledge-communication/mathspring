package edu.umass.ckc.wo.login.interv;

/* Author: Kartik
 * 
 * Frank	08-03-21	Issue 150 and 487 Added test for gaze, Worksheet location and class Messages
 * Frank	05-13-23	Issue #763 make LCs selectable by class
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.Intervention;

public class StudentPedagogy extends LoginInterventionSelector {
	
    private static final String JSP_NEW = "studentPedagogy_new.jsp";

	public StudentPedagogy(SessionManager smgr) throws SQLException {
		super(smgr);
	}
	
    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();

        if (shownTime > 0) {
        	return null;
        }
        else {

        	Map<Integer, List<String>> lcprofile = null;
        	super.selectIntervention(e);
        	String studentPedagogyUrl = JSP_NEW;
        	int classId = smgr.getClassID();
        	int currentStudentPedagogyId = DbUser.getStudentPedagogy(smgr.getConnection(), smgr.getStudentId());
        	
    		if (smgr.getExperiment().indexOf("sameGenderLC") >= 0) {
    			if (smgr.getGender().equals("") || smgr.getGender().equals("O")) {
       			 	lcprofile = DbPedagogy.getLCprofiles(smgr.getConnection(), classId, currentStudentPedagogyId);
       			 	System.out.println("Missing gender");
    			}
    			else {    					
    				lcprofile = DbPedagogy.getLCprofilesForGender(smgr.getConnection(), classId, currentStudentPedagogyId,smgr.getGender());
    			}
    		}
    		else {
    			 lcprofile = DbPedagogy.getLCprofiles(smgr.getConnection(), classId, currentStudentPedagogyId);
    		}
/*        	
        	int gazeDetectionOn = DbClass.getGazeDetectionOn(smgr.getConnection(),classId);
        	if (gazeDetectionOn > 0) {
            	studentPedagogyUrl = studentPedagogyUrl + "?resource=gazeOn";
        	}
        	else {
            	studentPedagogyUrl = studentPedagogyUrl + "?resource=gazeOff";
        	}
*/

        	studentPedagogyUrl = studentPedagogyUrl + "?resource=gazeOn";
        	
        	String worsheetLocation = DbUser.getStudentWorksheetLocation(smgr.getConnection(),smgr.getStudentId());
        	if ((worsheetLocation == null) || (worsheetLocation.length() == 0)) {
        		worsheetLocation = "Center";
        	}
        	studentPedagogyUrl = studentPedagogyUrl + "&location=opt" + worsheetLocation;
        	
        	List<String> messagesFromTeacher = DbClass.getClassMessages(smgr.getConnection(),classId);
        	
        	if (messagesFromTeacher.size() > 0 ) {
            	studentPedagogyUrl = studentPedagogyUrl + "&messageFromTeacher=";
	        	for(String message:messagesFromTeacher) {
	            	studentPedagogyUrl = studentPedagogyUrl + message + "... ";
	            }
        	}
        	
        	LoginIntervention li = new LoginIntervention(studentPedagogyUrl);
            li.setUrl(Settings.webContentPath + "LearningCompanion");
            li.setUrl2(Settings.webContentPath2 + "LearningCompanion");
            li.setLCprofile(lcprofile);
        	return li;                    
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
    	String learningCompanion = params.getString(LoginParams.LEARNING_COMPANION);
    	if (learningCompanion == null)
    		learningCompanion = "2";
        int lcIntValue = Integer.parseInt(learningCompanion);
        DbUser.setStudentPedagogy(conn, smgr.getStudentId(), lcIntValue);
    	String WorksheetLocation = params.getString(LoginParams.WORKSHEET_LOCATION);
        DbUser.setStudentWorksheetLocation(conn, smgr.getStudentId(), WorksheetLocation);
        if (!(lcIntValue == 19)) {
        	new TutorLogger(smgr).logChoosePedagogy(learningCompanion);
        }
        return null;
    }



    public String f (SessionManager smgr) {
        return JSP_NEW;
    }
}