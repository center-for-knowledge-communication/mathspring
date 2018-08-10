package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/26/11
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminClearStudentPosttestDataEvent extends AdminAlterStudentInClassEvent {

    public AdminClearStudentPosttestDataEvent(ServletParams p) throws Exception {
        super(p);
    }
}
