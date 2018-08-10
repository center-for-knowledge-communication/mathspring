package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/17/13
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TutorHomeEvent extends SessionEvent {
    public TutorHomeEvent (ServletParams p) throws Exception {
        super(p);
    }

    public TutorHomeEvent (int sessionId) {
        this.sessionId=sessionId;
    }
}
