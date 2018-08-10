package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Sep 23, 2011
 * Time: 1:39:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAlterStudentInClassEvent extends AdminClassEvent {
    private int studId;
    public static final String STUDENT_ID = "studId";

    public AdminAlterStudentInClassEvent(ServletParams p) throws Exception {
        super(p);
        setStudId(p.getMandatoryInt(STUDENT_ID));
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }
}