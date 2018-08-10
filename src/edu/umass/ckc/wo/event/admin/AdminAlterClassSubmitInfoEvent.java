package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 11, 2008
 * Time: 11:15:05 AM
 */
public class AdminAlterClassSubmitInfoEvent extends AdminClassEvent {
    int propGroupId;
    private String school;
    private String town;
    private String schoolYear;
    private String className;
    private String section;
    private String grade;
    public static final String PROP_GROUP_ID="propGroupId";
    public static final String GRADE="grade";
    private static final String CLASS_NAME = "className";
    private static final String SCHOOL = "school";
    private static final String TOWN = "town";
    private static final String SCHOOL_YEAR = "schoolYear";
    private static final String SECTION = "section";


    public AdminAlterClassSubmitInfoEvent(ServletParams p) throws Exception {
        super(p);
        this.className = (p.getString(CLASS_NAME));
        this.school = (p.getString(SCHOOL));
        this.schoolYear = (p.getString(SCHOOL_YEAR));
        this.town = (p.getString(TOWN));
        this.section = p.getString(SECTION);
        grade = p.getString(GRADE);
    }

    public int getPropGroupId() {
        return propGroupId;
    }



    public String getSchool() {
        return school;
    }

    public String getTown() {
        return town;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public String getClassName() {
        return className;
    }

    public String getSection() {
        return section;
    }

    public String getGrade() {
        return grade;
    }
}
