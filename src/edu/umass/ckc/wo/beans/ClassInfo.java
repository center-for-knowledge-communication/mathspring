package edu.umass.ckc.wo.beans;

import edu.umass.ckc.wo.woreports.Report;

/**
 * Frank	07-08-20	issue #156 added isActive flag
 * Frank	10-31-20	issue #293 added advanced settings
 * Frank	06-26-21	Added gaze_detection_on handling
 *  Frank 	02-04-23    Issue #723 - handle class clustering
 *  Frank	05-13-23	issue #763 - make LCs selectable by class
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
    private String experiment;
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
    private int hasClusters;
    private int isCluster;
    private String color;
    private String classesInCluster;
    private String altClassLanguageCode;
    
    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int pretestPoolId, String pretestPoolDescr,
                     int logType, int emailStatusInterval, int statusReportPeriodDays, int studentEmailIntervalDays,
                     int studentReportPeriodDays, String grade, int isActive, int gazeDetectionOn, int hasClusters, int isCluster, String color, String experiment) {
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
        this.hasClusters= hasClusters;
        this.isCluster= isCluster;
        this.color=color;
        this.classesInCluster = classesInCluster;
        this.altClassLanguageCode = altClassLanguageCode;
        this.experiment = experiment;
    }

    
     
    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int logType, int pretestPoolId,
                     int emailStatusReportIntervalDays, int statusReportPeriodDays, int studentReportIntervalDays, int studentReportPeriodDays, int isActive, int gazeDetectionOn, int hasClusters, int isCluster, String color, String experiment ) {
        this(school,schoolYear,name,town,section,classid,teachid,teacherName,propGroupId, pretestPoolId, null,logType,
                emailStatusReportIntervalDays, statusReportPeriodDays, studentReportIntervalDays, studentReportPeriodDays, "5", isActive, gazeDetectionOn, hasClusters, isCluster, color, experiment);
    }
    
    public ClassInfo(String school, int schoolYear, String name, String town, String section,
                     int classid, int teachid, String teacherName, int propGroupId, int logType, int pretestPoolId,
                     int emailStatusReportIntervalDays, int statusReportPeriodDays, int studentReportIntervalDays, int studentReportPeriodDays, String experiment, String grade, int isActive, int gazeDetectionOn, int hasClusters, int isCluster, String color) {
    	this(school,schoolYear,name,town,section,classid,teachid,teacherName,propGroupId, pretestPoolId, null,logType,
                emailStatusReportIntervalDays, statusReportPeriodDays, studentReportIntervalDays, studentReportPeriodDays, grade, isActive, gazeDetectionOn, hasClusters, isCluster, color, experiment);
        this.experiment = experiment;
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

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
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

    public void setSimpleConfigDefaults(String lc) {
        this.simpleCollab="none";
        this.simpleDiffRate="normal";
        this.simpleLC = lc;
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

	public void setAltClassLanguageCode(String altLanguageCode) {
		this.altClassLanguageCode = altLanguageCode;
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

    public void setGazeDetectionOn(int gazeDetectionOn) {
        this.gazeDetectionOn = gazeDetectionOn;
    }
    
    public int getHasClusters() {
        return hasClusters;
    }

    public void setHasClusters(int HasClusters) {
       this.hasClusters = HasClusters;
    }
    
    public int getIsCluster() {
        return isCluster;
    }

    public void setIsCluster(int isCluster) {
       this.hasClusters = isCluster;
    }
    public String getColor() {
    	if (color == null) 
    		return "green";
    	else
    		return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public String getClassesInCluster() {
   		return classesInCluster;
    }

    public void setClassesInCluster(String classesInCluster) {
        this.classesInCluster = classesInCluster;
    }


    

}