package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.SessionEvent;


public class LogoutEvent extends SessionEvent {
    private long elapsedTime_;
    public static final String ELAPSED_TIME = "elapsedTime";

    public LogoutEvent(ServletParams p) throws Exception {
        super(p);
        elapsedTime_ = p.getInt(ELAPSED_TIME,0);
    }

    public long getElapsedTime () {
      return elapsedTime_;
    }

}