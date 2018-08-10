package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class StudentAnsweredEvent extends ActionEvent {
    public static final String STUDENT_ANSWER = "studentAnswer";
    private String studentAnswer_;

    public StudentAnsweredEvent(ServletParams p) throws Exception {
        super(p);
        studentAnswer_ = p.getString(STUDENT_ANSWER);
    }

    public String getStudentAnswer () throws Exception {
        return studentAnswer_;
    }
}