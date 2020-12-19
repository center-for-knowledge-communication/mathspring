package edu.umass.ckc.wo.ttmain.ttmodel;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.umass.ckc.wo.util.StringUtils;

/**
 * Created by Frank on 11/09/202020.
 */
public class ClassLandingReportEvents {

    private String studentId;
    private String action;
    private String probElapsed;
    private String noOfSessions;
    private String timeInMS;
    private java.sql.Timestamp clickTime;
	private static final String tsFormat_us = "MM/dd/yyyy";
	private static final String tsFormat_es = "dd/MM/yyyy";

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProbElapsed() {
        return probElapsed;
    }

    public void setProbElapsed(String probElapsed) {
        this.probElapsed = probElapsed;
    }

    public String getNoOfSessions() {
        return noOfSessions;
    }

    public void setNoOfSessions(String noOfSessions) {
        this.noOfSessions = noOfSessions;
    }

    public String getTimeInMS() {
    	String sMinutes = "0";
    	int iMinutes = Integer.parseInt(timeInMS);
    	if (iMinutes > 0) {
    		iMinutes = iMinutes / 60000;
    		sMinutes = String.valueOf(iMinutes);
    	}
        return sMinutes;
    }

    public void setTimeInMS(String timeInMS) {
        this.timeInMS = timeInMS;
    }

    public java.sql.Timestamp getClickTime() {
        return clickTime;
    }

    public String getTimestampString(String lang) {
    	
    	String ts_format = "";
    	if (lang.equals("es")) {
    		ts_format = tsFormat_es;
    	}
    	else {
    		ts_format = tsFormat_us;
    	}
    	
        Date dd = StringUtils.timestampToDate(getClickTime());
        SimpleDateFormat formatter =  new SimpleDateFormat (ts_format) ;
        return formatter.format(dd);

    	//DateFormat dateFormat = new SimpleDateFormat("EEEEE dd MMMMM yyyy HH:mm:ss.SSSZ");
    	//String pattern = "dd MMMMM yyyy HH:mm:ss";
    	//SimpleDateFormat simpleDateFormat =
    	//        new SimpleDateFormat(pattern, new Locale("en", "US"));
    	//String date = simpleDateFormat.format(timestamp);
        //return date;
    }

    public void setClickTime(java.sql.Timestamp timestamp) {
        this.clickTime = timestamp;
    }
    
    
    public String toString() {
    	String s = "";
    	s += "StudentId=" +  getStudentId();
    	s += "  action=" +  getAction();
    	s += "  probElpased=" +  getProbElapsed();  	
    	s += "  clickTime=" +  getTimestampString("en");
    	return s;
    }
}
