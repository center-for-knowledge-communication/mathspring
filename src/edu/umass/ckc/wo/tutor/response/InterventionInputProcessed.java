package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterventionInputProcessed extends InternalEvent {


    public InterventionInputProcessed(SessionEvent sessionEvent) {
        super(sessionEvent,"InterventionComplete");
    }
}
