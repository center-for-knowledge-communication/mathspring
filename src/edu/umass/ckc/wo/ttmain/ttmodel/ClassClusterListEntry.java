package edu.umass.ckc.wo.ttmain.ttmodel;


/**
 * Frank 	01-15-23	Issue #723 new class 
 */
public class ClassClusterListEntry {

    private String classId;
    private String className;
    private String hasClusters;
    private String isCluster;
    private String color;
    
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
