package edu.umass.ckc.wo.event.admin;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 6, 2006
 * Time: 10:45:57 AM
 */
public class AdminActions {
    public static final String LOGIN = "AdminTeacherLogin";

    public static final String REGISTER = "AdminTeacherRegistration";
    public static final String CREATE_CLASS1 = "AdminCreateClass1";
    public static final String CREATE_CLASS2 = "AdminCreateClass2";
    public static final String CREATE_CLASS3 = "AdminCreateClass3";
    public static final String ALTER_CLASS1 = "AdminAlterClass1";
    public static final String VIEW_REPORT1 = "AdminViewReport&state="  + AdminViewReportEvent.CHOOSE_CLASS;
    public static final String CHOOSE_ACTIVITY = "AdminChooseActivity";
    public static final String SELECT_CLASS = "AdminSelectClass";
    public static final String SELECT_REPORT = AdminActions.SELECT_CLASS + "&classId=";
    public static final String CHOOSE_ANOTHER_CLASS = AdminActions.VIEW_REPORT1;

}
