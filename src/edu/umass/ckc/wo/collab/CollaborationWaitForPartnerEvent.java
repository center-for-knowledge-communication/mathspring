package edu.umass.ckc.wo.collab;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/21/15
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationWaitForPartnerEvent extends InternalEvent {

    public CollaborationWaitForPartnerEvent(SessionEvent sessionEvent) {
        super(sessionEvent, "CollabWaitForPartner");
    }
}
