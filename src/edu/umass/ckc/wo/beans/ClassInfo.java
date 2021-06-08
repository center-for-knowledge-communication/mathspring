package edu.umass.ckc.wo.beans;

import edu.umass.ckc.wo.woreports.Report;

/**
 * Frank	07-08-20	issue #156 added isActive flag
 * Frank	10-31-20	issue #293 added advanced settings
 */

public class ClassInfo {
    private String school;
    private int schoolYear;
    private String name;
    private String town;
    private String section;
    private int classid;
    private int teachid;
    private String teacherName;
    private int propGroupId;
    private int pretestPoolId;
    private String pretestPoolDescr;
    private int logType; // 1 means version 1 events in EpisodicData2, 2 means version 2 events in EventLog
    private int emailInterval;
    private int statusReportPeriodDays;
    private int studentEmailPeriodDays;
    private int studentEmailIntervalDays;
    private String flashClient;
    private String grade;
    private String simpleLC;
    private String simpleDiffRate;
    private String simpleCollab;
    private String simpleLowDiff;
    private String simpleHighDiff;
    private boolean showPostSurvey;
    private boolean showPreSurvey;
    private String classLanguageCode;
    private String maxProb;
    private String minProb;
    private String maxTime;
    private String minTime;
    private boolean isDefaultClass;
    private int isActive;
    private int gazeDetectionOn; // <= 0 indicates gaze detection off in client.
    
    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int pretestPoolId, String pretestPoolDescr,
                     int logType, int emailStatusInterval, int statusReportPeriodDays, int studentEmailIntervalDays,
                     int studentReportPeriodDays, String grade, int isActive, int gazeDetectionOn) {
        this.school = school;
        this.schoolYear = schoolYear;
        this.name = name;
        this.town = town;
        this.section = section;
        this.classid = classid;
        this.teachid = teachid;
        this.teacherName = teacherName;
        this.propGroupId = propGroupId;
        this.pretestPoolId = pretestPoolId;
        this.pretestPoolDescr =pretestPoolDescr;
        this.logType = logType;
        this.emailInterval=emailStatusInterval;
        this.statusReportPeriodDays=statusReportPeriodDays;
        this.studentEmailIntervalDays=studentEmailIntervalDays;
        this.studentEmailPeriodDays =studentReportPeriodDays;
        this.grade= grade;
        this.isActive= isActive;
        this.gazeDetectionOn = gazeDetectionOn;

    }
    
    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int logType, int pretestPoolId,
                     int emailStatusReportIntervalDays, int statusReportPeriodDays, int studentReportIntervalDays, int studentReportPeriodDays, int isActive, int gazeDetectionOn) {
        this(school,schoolYear,name,town,section,classid,teachid,teacherName,propGroupId, pretestPoolId, null,logType,
                emailStatusReportIntervalDays, statusReportPeriodDays, studentReportIntervalDays, studentReportPeriodDays, "5", isActive, gazeDetectionOn);
    }

    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int logType, int pretestPoolId,
                     int emailStatusReportIntervalDays, int statusReportPeriodDays, int studentReportIntervalDays, int studentReportPeriodDays, String flashClient, String grade, int isActive, int gazeDetectionOn) {
        this(school,schoolYear,name,town,section,classid,teachid,teacherName,propGroupId, pretestPoolId, null,logType,
                emailStatusReportIntervalDays, statusReportPeriodDays, studentReportIntervalDays, studentReportPeriodDays, grade, isActive, gazeDetectionOn);
        this.flashClient = flashClient;
    }

    public String getSchool() {
        return school;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public String getSection() {
        return section;
    }

    public int getClassid() {
        return classid;
    }

    public int getTeachid() {
        return teachid;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getPropGroupId() {
        return propGroupId;
    }

    public void setPropGroupId(int propGroupId) {
        this.propGroupId = propGroupId;
    }

    public String getPretestPoolDescr() {
        return pretestPoolDescr;
    }

    public int getPretestPoolId() {
        return pretestPoolId;
    }

    public int getLogType() {
        return logType;
    }

    public boolean isNewLog () {
        return logType == Report.EVENT_LOG;
    }

    public int getEmailInterval() {
        return emailInterval;
    }

    public void setEmailInterval(int emailInterval) {
        this.emailInterval = emailInterval;
    }

    public int getStatusReportPeriodDays() {
        return statusReportPeriodDays;
    }

    public void setStatusReportPeriodDays(int statusReportPeriodDays) {
        this.statusReportPeriodDays = statusReportPeriodDays;
    }

    public int getStudentEmailPeriodDays() {
        return studentEmailPeriodDays;
    }

    public void setStudentEmailPeriodDays(int studentEmailPeriodDays) {
        this.studentEmailPeriodDays = studentEmailPeriodDays;
    }

    public int getStudentEmailIntervalDays() {
        return studentEmailIntervalDays;
    }

    public void setStudentEmailIntervalDays(int studentEmailIntervalDays) {
        this.studentEmailIntervalDays = studentEmailIntervalDays;
    }

    public String getFlashClient() {
        return flashClient;
    }

    public void setFlashClient(String flashClient) {
        this.flashClient = flashClient;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSimpleLC() {
        return simpleLC;
    }

    public void setSimpleLC(String simpleLC) {
        this.simpleLC = simpleLC;
    }

    public String getSimpleDiffRate() {
        return simpleDiffRate;
    }

    public void setSimpleDiffRate(String simpleDiffRate) {
        this.simpleDiffRate = simpleDiffRate;
    }

    public String getSimpleCollab() {
        return simpleCollab;
    }

    public void setSimpleCollab(String simpleCollab) {
        this.simpleCollab = simpleCollab;
    }

    public String getSimpleLowDiff() {
        return simpleLowDiff;
    }

    public void setSimpleLowDiff(String simpleLowDiff) {
        this.simpleLowDiff = simpleLowDiff;
    }

    public String getSimpleHighDiff() {
        return simpleHighDiff;
    }

    public void setSimpleHighDiff(String simpleHighDiff) {
        this.simpleHighDiff = simpleHighDiff;
    }

    public void setSimpleConfigDefaults() {
        this.simpleCollab="none";
        this.simpleDiffRate="normal";
        this.simpleLC = "both";
        this.simpleLowDiff="below2";
        this.simpleHighDiff="above1";
    }

    public boolean isShowPostSurvey() {
        return showPostSurvey;
    }

    public void setShowPostSurvey(boolean showPostSurvey) {
        this.showPostSurvey = showPostSurvey;
    }

    public boolean isShowPreSurvey() {
        return showPreSurvey;
    }

    public void setShowPreSurvey(boolean showPreSurvey) {
        this.showPreSurvey = showPreSurvey;
    }

	public String getClassLanguageCode() {
		return classLanguageCode;
	}

	public void setClassLanguageCode(String classLanguageCode) {
		this.classLanguageCode = classLanguageCode;
	}

	public boolean isDefaultClass() {
		return isDefaultClass;
	}

	public void setDefaultClass(boolean isDefaultClass) {
		this.isDefaultClass = isDefaultClass;
	}
	
	public void setIsActive(int flag) {
		isActive = flag;
	}
	public int getIsActive() {
		return isActive;
	}

    public String getMaxProb() {
        return maxProb;
    }

    public void setMaxProb(String maxProb) {
        this.maxProb = maxProb;
    }

    public String getMinProb() {
        return minProb;
    }

    public void setMinProb(String minProb) {
        this.minProb = minProb;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public int getGazeDetectionOn() {
        return gazeDetectionOn;
    }

    public void setGazeDetectionOn(int preSurveyURL) {
        this.gazeDetectionOn = gazeDetectionOn;
    }
    
}