package edu.umass.ckc.wo.ttmain.ttmodel;

/**
 * Created by nsmenon on 6/14/2017.
 */
public class RequestMapperForm {
    private String classId;
    private String reportType;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    private String teacherId;
}
