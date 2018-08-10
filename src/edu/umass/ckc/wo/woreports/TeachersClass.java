package edu.umass.ckc.wo.woreports;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 7, 2006
 * Time: 2:22:46 PM
 */
public class TeachersClass {
    private String name;
    private String year;
    private String section;
    private String school;
    private String town;
    private int id;

    public TeachersClass(int id, String name, String year, String section, String school, String town) {
        this.name = name;
        this.year = year;
        this.section = section;
        this.school = school;
        this.town = town;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
