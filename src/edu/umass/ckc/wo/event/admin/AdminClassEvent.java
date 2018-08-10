package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 2, 2006
 * Time: 10:48:28 AM
 */
public abstract class AdminClassEvent extends AdminEvent {

    public static final String CLASS_ID = "classId";
    public static final String TEACHER_ID = "teacherId";
    private int classId;


    public AdminClassEvent(ServletParams p) throws Exception {
        super(p);
        setClassId(p.getMandatoryInt(CLASS_ID));
    }

    public AdminClassEvent(ServletParams p, boolean wantClassId, boolean wantTeacherId) throws Exception {
        super(p);
        if (wantClassId)
            setClassId(p.getMandatoryInt(CLASS_ID));
        if (wantTeacherId)
            setTeacherId(p.getMandatoryInt(TEACHER_ID));   // some places may not be sending this.
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

}