package edu.umass.ckc.wo.ttmain.ttmodel;

/**
 * Created by Neeraj on 3/26/2017.
 */

public class CreateClassForm {

    /**
     *   FIELDS TO SET UP CLASS FOR TEACHER
     * */
    private String className;
    private String classGrade;
    private String town;
    private String schoolName;
    private String schoolYear;
    private String gradeSection;
    private String lowEndDiff;
    private String highEndDiff;
    private String classId;
    private String reportType;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    /**
     *   DEAFULT SET OF PEDAGOGY PRESELECTED FOR TEACHERS ONLY
     * */
    private String simpleLC = "both";
    private String simpleCollab = "none";
    private String probRate = "normal";

    /**
     *   GENERATE STUDENT ROSTER FIELDS
     * */
    private String userPrefix;
    private String passwordToken;
    private int noOfStudentAccountsForClass;


    /**
     *   HIDDEN TEACHER ID FIELD
     * */
    private String teacherId;


    private boolean showPostSurvey;
    private boolean showPreSurvey;

    public boolean isShowPreSurvey() {
        return showPreSurvey;
    }

    public void setShowPreSurvey(boolean showPreSurvey) {
        this.showPreSurvey = showPreSurvey;
    }

    public boolean isShowPostSurvey() {
        return showPostSurvey;
    }

    public void setShowPostSurvey(boolean showPostSurvey) {
        this.showPostSurvey = showPostSurvey;
    }


    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getGradeSection() {
        return gradeSection;
    }

    public void setGradeSection(String gradeSection) {
        this.gradeSection = gradeSection;
    }

    public String getUserPrefix() {
        return userPrefix;
    }

    public void setUserPrefix(String userPrefix) {
        this.userPrefix = userPrefix;
    }

    public String getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(String passwordToken) {
        this.passwordToken = passwordToken;
    }

    public int getNoOfStudentAccountsForClass() {
        return noOfStudentAccountsForClass;
    }

    public void setNoOfStudentAccountsForClass(int noOfStudentAccountsForClass) {
        this.noOfStudentAccountsForClass = noOfStudentAccountsForClass;
    }


    public String getSimpleLC() {
        return simpleLC;
    }

    public String getSimpleCollab() {
        return simpleCollab;
    }

    public String getLowEndDiff() {
        return lowEndDiff;
    }

    public String getHighEndDiff() {
        return highEndDiff;
    }

    public void setLowEndDiff(String lowEndDiff) {this.lowEndDiff = lowEndDiff;}

    public void setHighEndDiff(String highEndDiff) {
        this.highEndDiff = highEndDiff;
    }

    public String getProbRate() {
        return probRate;
    }
}
