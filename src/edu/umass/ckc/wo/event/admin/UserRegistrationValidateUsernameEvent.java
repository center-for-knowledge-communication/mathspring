package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/** The first in a series of events for registering a new student.
 *
 */
public class UserRegistrationValidateUsernameEvent extends UserRegistrationEvent {
    private String userName;

    public UserRegistrationValidateUsernameEvent(ServletParams p)  throws Exception {
        super(p);
        userName = p.getString("userName");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}