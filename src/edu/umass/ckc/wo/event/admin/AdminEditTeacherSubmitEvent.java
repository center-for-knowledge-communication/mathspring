package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/24/14
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminEditTeacherSubmitEvent extends AdminEvent {
    private String fname;
    private String lname;
    private String uname;
    private String pw;

    public AdminEditTeacherSubmitEvent(ServletParams p) throws Exception {
        super(p);
        this.fname=p.getString("fname");
        this.lname=p.getString("lname");
        this.uname=p.getString("uname");
        this.pw=p.getString("password");
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUname() {
        return uname;
    }

    public String getPw() {
        return pw;
    }
}
