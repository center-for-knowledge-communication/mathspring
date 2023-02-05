package edu.umass.ckc.wo.ttmain.ttmodel;


/**
 * Frank 	02-26-20	Issue #45 & #21
* Frank 	02-04-23    Issue #723 - handle class clusters 
 */
public class TeacherClassListEntry {

    private String teacherId;
    private String teacherName;
    private String classId;
    private String className;
    private String hasClusters;
    private String isCluster;
    private String color;
    
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String TeacherName) {
        this.teacherName = teacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassHasClusters() {
        return hasClusters;
    }

    public void setClassHasClusters(String hasClusters) {
        this.hasClusters = hasClusters;
    }
    public String getClassIsCluster() {
        return isCluster;
    }

    public void setClassIsCluster(String isCluster) {
        this.isCluster = isCluster;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
