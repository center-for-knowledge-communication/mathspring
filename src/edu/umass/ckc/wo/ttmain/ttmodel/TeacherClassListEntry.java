package edu.umass.ckc.wo.ttmain.ttmodel;


/**
 * Frank 	02-26-20	Issue #45 & #21 
 */
public class TeacherClassListEntry {

    private String teacherId;
    private String teacherName;
    private String classId;
    private String className;
    
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
}
