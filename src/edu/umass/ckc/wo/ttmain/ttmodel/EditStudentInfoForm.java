package edu.umass.ckc.wo.ttmain.ttmodel;

/**
 * Created by nsmenon on 4/19/2017.
 */
public class EditStudentInfoForm {

    private Integer studentId;
    private String studentFname;
    private String studentLname;
    private String studentUsername;
    private String className;
    private String classPassword;

    public EditStudentInfoForm(Integer studentId, String studentFname, String studentLname, String studentUsername) {
        this.studentId = studentId;
        this.studentFname = studentFname;
        this.studentLname = studentLname;
        this.studentUsername = studentUsername;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentFname() {
        return studentFname;
    }

    public void setStudentFname(String studentFname) {
        this.studentFname = studentFname;
    }

    public String getStudentLname() {
        return studentLname;
    }

    public void setStudentLname(String studentLname) {
        this.studentLname = studentLname;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPassword() {
        return classPassword;
    }

    public void setClassPassword(String classPassword) {
        this.classPassword = classPassword;
    }
}
