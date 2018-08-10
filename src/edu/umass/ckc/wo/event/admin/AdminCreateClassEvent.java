package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 2, 2006
 * Time: 10:48:28 AM
 */
public abstract class AdminCreateClassEvent extends ActionEvent {

    private int teacherId;
    public static final String TEACHERID = "teacherId";

    public AdminCreateClassEvent (ServletParams p) throws Exception {
        super(p);
        teacherId = p.getInt(TEACHERID);
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
