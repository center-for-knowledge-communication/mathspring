package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/**
 * The first event in a series of two for creating a class.
 */
public class AdminCreateNewClassEvent extends AdminCreateClassEvent {

  public AdminCreateNewClassEvent(ServletParams p) throws Exception {
    super(p);
  }
}