package edu.umass.ckc.wo.ttmain.ttmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import edu.umass.ckc.wo.util.StringUtils;
/**
 *  Frank 	03-02-2020	Issue #45 teacher selection list & fix - use sql timestamp data type
 * Frank 	06-17-20	Issue #149
 * Frank	10-27-20	Issue #149R2
 */
public class TeacherLogEntry {

    private String teacherId;
    private String teacherName;
    private String userName;
    private String action;
    private String classId;
    private String activityName;
    private java.sql.Timestamp timestamp;
	private static final String tsFormat_us = "MM/dd/yyyy HH:mm:ss.SSS";
	private static final String tsFormat_es = "dd/MM/yyyy HH:mm:ss.SSS";

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    
    public String getActivityName() {
        return activityName;
    }

    public String getActivityName(String reportType) {
    	String result = "";
    	switch (this.action) {
    		case "createMoreStudentIds":  ;
    			if (reportType.equals("perTeacherReport")) {
    				result = this.activityName;
    			}
    			break;
    		default:
    			result = this.activityName;
    			break;
    		
    	}
        return result;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTimestampString(String lang) {
    	
    	String ts_format = "";
    	if (lang.equals("es")) {
    		ts_format = tsFormat_es;
    	}
    	else {
    		ts_format = tsFormat_us;
    	}
    	
        Date dd = StringUtils.timestampToDate(getTimestamp());
        SimpleDateFormat formatter =  new SimpleDateFormat (ts_format) ;
        return formatter.format(dd);

    	//DateFormat dateFormat = new SimpleDateFormat("EEEEE dd MMMMM yyyy HH:mm:ss.SSSZ");
    	//String pattern = "dd MMMMM yyyy HH:mm:ss";
    	//SimpleDateFormat simpleDateFormat =
    	//        new SimpleDateFormat(pattern, new Locale("en", "US"));
    	//String date = simpleDateFormat.format(timestamp);
        //return date;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
