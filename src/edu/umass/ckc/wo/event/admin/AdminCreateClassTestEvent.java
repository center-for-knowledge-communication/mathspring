package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * The first event in a series of two for creating a class.
 */
public class AdminCreateClassTestEvent extends AdminCreateClassEvent {
    private int classId;

  public AdminCreateClassTestEvent(ServletParams p) throws Exception {
        super(p);
      classId = p.getInt("classId");
  }

    public int getClassId() {
        return classId;
    }
}