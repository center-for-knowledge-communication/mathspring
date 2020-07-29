package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/** The first in a series of events for registering a new student.
 *
 * Frank	06-02-2020	Issue #122 Allow student to enter class code on sign-up page
 * Frank	07-17-20	Issue #122 modified UserRegistration events for classId parameter
 */
public class UserRegistrationValidateUsernameEvent extends UserRegistrationEvent {
    private String userName;

    public UserRegistrationValidateUsernameEvent(ServletParams p)  throws Exception {
        super(p);
        this.userName = p.getString("userName");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}