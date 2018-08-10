package edu.umass.ckc.wo.event.admin;


import ckc.servlet.servbase.ServletParams;
import ckc.servlet.servbase.ActionEvent;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 31, 2006
 * Time: 10:31:46 AM
 */
public class AdminTeacherLoginEvent extends ActionEvent {
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";

    private String uname;
    private String pw;
    private boolean isReg = false;
    private boolean isLogin = false;

    public AdminTeacherLoginEvent(ServletParams p) throws Exception {
        super(p);
        String reg = p.getString(REGISTER, null);
        String log = p.getString(LOGIN, null);
        if (reg != null) {
            isReg = true;
        } else if (log != null) {
            isLogin = true;
            uname = p.getString(USER_NAME,null);
            pw = p.getString(PASSWORD,null);
        }
    }

    public String getPw() {
        return pw;
    }

    public String getUname() {
        return uname;
    }

    public boolean isReg() {
        return isReg;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
