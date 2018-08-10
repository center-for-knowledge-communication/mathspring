package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ServletParams;


/** A convenience class so that the userInput may be accessed as a String. */

public class ReplayHintEvent extends StudentActionEvent {
  private String hintLabel_;

  public ReplayHintEvent(ServletParams p) throws Exception {
    super(p);
  }


  public String getHintLabel() {
    return userInputString_;
  }


}