package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: edo
 * Date: 12/7/12
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminNoClassSubmitClassFormEvent extends AdminCreateClassEvent{
    private static final String CLASS_NAME = "className";
    private static final String SCHOOL = "school";
    private static final String TOWN = "town";
    private static final String SCHOOL_YEAR = "year";
    private static final String SECTION = "section";
    private static final String GRADE = "grade";
    private String school;
    private String town;
    private String schoolYear;
    private String className;
    private String section;
    private String grade;



    public AdminNoClassSubmitClassFormEvent(ServletParams p) throws Exception {
        super(p);

        this.setClassName(p.getString(CLASS_NAME));
        this.setSchool(p.getString(SCHOOL));
        this.setSchoolYear(p.getString(SCHOOL_YEAR));
        this.setTown(p.getString(TOWN));
        this.section = p.getString(SECTION);
        this.grade = p.getString(GRADE);
    }



    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }




    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getSection() {
        return this.section;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
