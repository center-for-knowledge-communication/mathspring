package edu.umass.ckc.wo.ttmain.ttmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * Frank 	01-14-20	Issue #45 & #21 
 */
public class TeacherLogEntry {

    private String teacherId;
    private String teacherName;
    private String userName;
    private String action;
    private String activityName;
    private java.util.Date timestamp;

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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public java.util.Date getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() {
    	
    	//DateFormat dateFormat = new SimpleDateFormat("EEEEE dd MMMMM yyyy HH:mm:ss.SSSZ");
    	String pattern = "dd MMMMM yyyy HH:mm:ss";
    	SimpleDateFormat simpleDateFormat =
    	        new SimpleDateFormat(pattern, new Locale("en", "US"));
    	String date = simpleDateFormat.format(timestamp);
        return date;
    }

    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }
}
