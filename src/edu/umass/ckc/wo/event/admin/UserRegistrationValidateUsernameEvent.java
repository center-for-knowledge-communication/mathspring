package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/** The first in a series of events for registering a new student.
 *
 * Frank	06-02-2020	Issue #122 Allow student to enter class code on sign-up page
 * Frank	07-17-20	Issue #122 modified UserRegistration events for classId parameter
 * Frank	10-29-21	Issue #526 validate user entry fields
 */
public class UserRegistrationValidateUsernameEvent extends UserRegistrationEvent {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String age;


    public UserRegistrationValidateUsernameEvent(ServletParams p)  throws Exception {
        super(p);
        this.userName = p.getString("userName");
        this.firstName = p.getString("firstName");
        this.lastName = p.getString("lastName");
        this.password = p.getString("password");
        this.age = p.getString("age");

    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    
 }