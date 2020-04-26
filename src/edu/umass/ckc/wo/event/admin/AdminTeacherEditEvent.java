package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: Frank Sylvia
 * Date: Mar 24, 2020
 */
public class AdminTeacherEditEvent extends ActionEvent {
    private static final String FNAME = "fname";
    private static final String LNAME = "lname";
    private static final String PW1 = "pw1";
    private static final String PW2 = "pw2";
    private static final String EMAIL = "email";
    private static final String ID = "teacherId";

    private String fname;
    private String lname;
    private String pw1;
    private String pw2;
    private String email;
    private String id;

    public AdminTeacherEditEvent (ServletParams p) {
        fname = p.getString(FNAME,null);
        lname = p.getString(LNAME,null);
        pw1 = p.getString(PW1,null);
        pw2 = p.getString(PW2,null);
        email = p.getString(EMAIL,null);
        id = p.getString(ID,null);
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

    public String getPw1() {
        return pw1;
    }

    public void setPw1(String pw1) {
        this.pw1 = pw1;
    }

    public String getPw2() {
        return pw2;
    }

    public void setPw2(String pw2) {
        this.pw2 = pw2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return this.id;
    }

    public int getIntId() {
        return Integer.parseInt(this.id);
    }

    public void setId(String id) {
        this.id = id;
    }


}
