package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/**
 * The first event in a series of two for creating a class.
 */
public class AdminAlterClass1Event extends AdminClassEvent {

  public AdminAlterClass1Event(ServletParams p) throws Exception {
    super(p,false, true);
  }
}