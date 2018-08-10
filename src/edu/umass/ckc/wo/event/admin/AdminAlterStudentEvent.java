package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Sep 23, 2011
 * Time: 1:39:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAlterStudentEvent extends AdminAlterStudentInClassEvent {
    private String uname;
    private String fname;
    private String password;
    private int pedagogyId;

    public static final String UNAME = "uname";
    public static final String FNAME = "fname";
    public static final String PASSWORD = "password";
    public static final String PEDAGOGY_ID = "pedagogyId";


    public AdminAlterStudentEvent(ServletParams p) throws Exception {
        super(p);
        this.uname=p.getMandatoryString(UNAME);
        this.fname=p.getMandatoryString(FNAME);
        this.password=p.getMandatoryString(PASSWORD);
        this.pedagogyId=p.getMandatoryInt(PEDAGOGY_ID);
    }


    public String getUname() {
        return uname;
    }

    public String getFname() {
        return fname;
    }

    public String getPassword() {
        return password;
    }

    public int getPedagogyId() {
        return pedagogyId;
    }
}