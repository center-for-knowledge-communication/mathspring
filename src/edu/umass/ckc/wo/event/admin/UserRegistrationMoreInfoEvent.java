package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/** The fourth in a series of events for creating a new Student.
 * Receives the property fields that are further info about the student.
 */
public class UserRegistrationMoreInfoEvent extends UserRegistrationEvent {

  public static final String STUDID = "studId";
  public static final String CLASSID = "classId";
  private int studId;
  private int classId;

  public UserRegistrationMoreInfoEvent(ServletParams p) throws Exception {
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