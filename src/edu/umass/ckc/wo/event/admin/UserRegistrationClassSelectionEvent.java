package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;


/** The third in a series of events for creating a new Student.
 * Receives the fields about what class/teacher a student has.
 * Frank	07-17-20	Issue #122 modified UserRegistration events for classId parameter
 */
public class UserRegistrationClassSelectionEvent extends UserRegistrationEvent {
    public static final String STUDID = "studId";
    private int studId;

    public UserRegistrationClassSelectionEvent(ServletParams p) throws Exception {
        super(p);
        setStudId(p.getInt(STUDID));
    }
    public int getStudId() {
        return studId;
    }
    public void setStudId(int studId) {
        this.studId = studId;
    }
}