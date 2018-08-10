package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/26/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminEvent extends ActionEvent {

    public static final String TEACHER_ID = "teacherId";
    private int teacherId;

    public AdminEvent(ServletParams p) throws Exception {
        super(p);
        this.teacherId = p.getMandatoryInt(TEACHER_ID);
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
