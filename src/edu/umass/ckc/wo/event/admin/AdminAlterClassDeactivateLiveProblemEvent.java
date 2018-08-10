package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 *   This event is used to alter a class's set of active problems while the tutor is running
 */
public class AdminAlterClassDeactivateLiveProblemEvent extends AdminClassEvent {


  public AdminAlterClassDeactivateLiveProblemEvent(ServletParams p) throws Exception {
    super(p);

  }

}