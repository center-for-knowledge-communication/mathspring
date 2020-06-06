package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/** The first in a series of events for registering a new student.
 *
 * Frank	06-02-2020	Issue #122 Allow student to enter class code on sign-up page
 */
public class UserRegistrationValidateUsernameEvent extends UserRegistrationEvent {
    private String userName;
    private String classId;

    public UserRegistrationValidateUsernameEvent(ServletParams p)  throws Exception {
        super(p);
        userName = p.getString("userName");
        classId = p.getString("classId");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}