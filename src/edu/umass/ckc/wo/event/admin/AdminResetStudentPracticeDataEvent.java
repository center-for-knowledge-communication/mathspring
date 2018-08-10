package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/26/11
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminResetStudentPracticeDataEvent extends AdminAlterStudentInClassEvent {

    public AdminResetStudentPracticeDataEvent(ServletParams p) throws Exception {
        super(p);
    }
}
