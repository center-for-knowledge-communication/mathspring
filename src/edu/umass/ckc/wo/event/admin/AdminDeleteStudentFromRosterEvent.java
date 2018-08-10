package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Sep 23, 2011
 * Time: 1:39:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminDeleteStudentFromRosterEvent extends AdminAlterStudentInClassEvent {
     
    public AdminDeleteStudentFromRosterEvent(ServletParams p) throws Exception {
        super(p);
    }
}
