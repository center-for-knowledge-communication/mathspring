package edu.umass.ckc.wo.smgr;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Apr 4, 2006
 * Time: 3:08:50 PM
 */
public class User {
    private String fname;
    private String lname;
    private String uname;
    private String momname;
    private String gender;
    private String email;
    private String password;
    private int id;
    private int pedagogyId;
    private int strategyId;



    /*
    Meanings:
       test: a developer user who is testing the system and wants extra controls
       guest:  A user who logs in by clicking the "guest login" button
       student: A student who is registered as a "regular" student (also called a "real" student)
       coopStudent: A student coming from another system (e.g. MARi or Assistments)
       coopStudentTest:  A test-student coming from another system (test implies showing tester controls and not altering statistics about problems)
       external*:  Not sure.   Probably supports the use of calls to the system using TeachTopic
       testStudent:  a tester who sees the same interface a regular student would (no problem stats updates happen)
     */
    public enum UserType {
        test, guest, student, coopStudent, coopStudentTest, externalTest, externalTempTest, externalTempNonTest, testStudent, tester
    }

    public User(String fname, String lname, String uname, String gender, String email, String password, int id) {
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.gender = gender;
        this.email = email;
        this.momname=email; // this is for backward compatibility with ServerTestMgr which we don't even use anymore
        this.id = id;
        this.password= password;
    }

    public static boolean[] getUserTypeFlags(UserType userType) {
        // four fields in order are KEEP_USER, KEEP_DATA, UPDATE_STATS, SHOW_TEST_CONTROLS
        if (userType == UserType.test)
            return new boolean[] {true,true,false,true} ;
        else if (userType == UserType.testStudent)
            return new boolean[] {true,true,false,false};
        else if (userType == UserType.guest)
            return new boolean[] {false,false,false,false} ;
        else if (userType == UserType.student)
            return new boolean[] {true,true,true,false} ;
        else if (userType == UserType.coopStudent)
            return new boolean[] {true,true,true,false} ;
        else if (userType == UserType.coopStudentTest)
            return new boolean[] {true,true,false,true} ;
        else if (userType == UserType.externalTest)
            return new boolean[] {true,true,false,true} ;
        else if (userType == UserType.externalTempTest)
            return new boolean[] {false,false,false,true} ;
        else if (userType == UserType.externalTempNonTest)
            return new boolean[] {false,false,false,false} ;
        else if (userType == UserType.tester)
            return new boolean[] {true,false,false,true} ;
        else return null;
    }


    public static boolean isTrialUser (UserType ut) {
        return (ut==UserType.test || ut==UserType.coopStudentTest || ut==UserType.externalTempTest || ut==UserType.externalTest || ut==UserType.testStudent || ut == UserType.tester);
    }

    public String toString () {
        return fname + " " + lname + " " + uname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMomname() {
        return momname;
    }

    public void setMomname(String momname) {
        this.momname = momname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail () {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedagogyId() {
        return pedagogyId;
    }

    public void setPedagogyId(int pedagogyId) {
        this.pedagogyId = pedagogyId;
    }

    public String getPassword() {
        return password;
    }

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }
}
