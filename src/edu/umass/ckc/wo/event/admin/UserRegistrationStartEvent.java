package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/** The first in a series of events for registering a new student.
 *
 */
public class UserRegistrationStartEvent extends UserRegistrationEvent {

    public UserRegistrationStartEvent(ServletParams p)  throws Exception {
        super(p);
    }
}