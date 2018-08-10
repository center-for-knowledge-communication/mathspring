package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 29, 2011
 * Time: 3:39:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminEditStudentEvent extends AdminAlterStudentInClassEvent {



    public AdminEditStudentEvent(ServletParams p) throws Exception {
        super(p);
    }
}
