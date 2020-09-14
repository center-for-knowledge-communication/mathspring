package edu.umass.ckc.wo.beans;


import java.io.Serializable;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 5/28/14
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 * Frank	09-14-20	issue #237 added pauseStudentUse
 */

public class Teacher implements Serializable {
    private String email;
    private int id;
    private String fname;
    private String lname;
    private String userName;
    private String password;
    private int pauseStudentUse;
    private List<ClassInfo> classes;

    public Teacher() {
    }

    public Teacher(String email, int id, String fname, String lname, String userName, String password, int pauseStudentUse) {
        this.email = email;
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.userName = userName;
        this.password = password;
        this.pauseStudentUse = pauseStudentUse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getName () {
        if (fname != null && lname != null)
            return fname + " " + lname;
        else return userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPauseStudentUse() {
        return pauseStudentUse;
    }

    public void setPauseStudentUse(int pauseStudentUse) {
        this.pauseStudentUse = pauseStudentUse;
    }

    public void setClasses (List<ClassInfo> classes) {
        this.classes = classes;
    }

    public List<ClassInfo> getClasses () {
        return this.classes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
