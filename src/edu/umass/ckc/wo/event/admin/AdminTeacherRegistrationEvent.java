package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 31, 2006
 * Time: 4:00:56 PM
 */
public class AdminTeacherRegistrationEvent extends ActionEvent {
    private static final String FNAME = "fname";
    private static final String LNAME = "lname";
    private static final String PW1 = "pw1";
    private static final String PW2 = "pw2";
    private static final String UN = "userName";
    private static final String EMAIL = "email";

    private String fname;
    private String lname;
    private String pw1;
    private String pw2;
    private String un;
    private String email;

    public AdminTeacherRegistrationEvent (ServletParams p) {
        fname = p.getString(FNAME,null);
        lname = p.getString(LNAME,null);
        pw1 = p.getString(PW1,null);
        pw2 = p.getString(PW2,null);
        un = p.getString(UN,null);
        email = p.getString(EMAIL,null);
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

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getEmail() {
        return this.email;
    }
}
