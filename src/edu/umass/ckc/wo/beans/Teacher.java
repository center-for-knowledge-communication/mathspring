package edu.umass.ckc.wo.beans;


import java.io.Serializable;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 5/28/14
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */

public class Teacher implements Serializable {
    private String email;
    private int id;
    private String fname;
    private String lname;
    private String userName;
    private String password;
    private List<ClassInfo> classes;

    public Teacher() {
    }

    public Teacher(String email, int id, String fname, String lname, String userName, String password) {
        this.email = email;
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.userName = userName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClasses (List<ClassInfo> classes) {
        this.classes = classes;
    }

    public List<ClassInfo> getClasses () {
        return this.classes;
    }
}
