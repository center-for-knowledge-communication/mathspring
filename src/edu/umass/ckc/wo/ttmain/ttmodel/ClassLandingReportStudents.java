package edu.umass.ckc.wo.ttmain.ttmodel;

/**
 * Created by Frank on 11/09/20.
 */
public class ClassLandingReportStudents {

    private String studentId;
    private String studentName;
    private String userName;
    private String noOfProblems;
//    private String noOfSessions;
//    private String noOfLogins;
    private String timeInMS;
    private String latestLogin;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNoOfProblems() {
        return noOfProblems;
    }

    public void setNoOfProblems(String noOfProblems) {
        this.noOfProblems = noOfProblems;
    }
    public String getTimeInMS() {
        return timeInMS;
    }

    public void setTimeInMS(String timeInMS) {
        this.timeInMS = timeInMS;
    }

    public String getLatestLogin() {
        return latestLogin;
    }

    public void setLatestLogin(String latestLogin) {
        this.latestLogin = latestLogin;
    }

    /*
    public String getNoOfSessions() {
        return noOfSessions;
    }

    public void setNoOfSessions(String noOfSessions) {
        this.noOfSessions = noOfSessions;
    }
*/
/*
    public String getNoOfLogins() {
        return noOfLogins;
    }

    public void setNoOfLogins(String noOfLogins) {
        this.noOfLogins = noOfLogins;
    }
*/

}
