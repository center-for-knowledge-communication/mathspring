package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;


/** The third in a series of events for creating a new Student.
 * Receives the fields about what class/teacher a student has.
 */
public class UserRegistrationClassSelectionEvent extends UserRegistrationEvent {
    public static final String STUDID = "studId";
    public static final String CLASSID = "classId";
    private int studId;
    private int classId;

    public UserRegistrationClassSelectionEvent(ServletParams p) throws Exception {
        super(p);
        setStudId(p.getInt(STUDID));
        setClassId(p.getInt(CLASSID));
    }
    public int getStudId() {
        return studId;
    }
    public void setStudId(int studId) {
        this.studId = studId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public int getClassId() {
        return classId;
    }
}