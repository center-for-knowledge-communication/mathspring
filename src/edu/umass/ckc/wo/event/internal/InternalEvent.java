package edu.umass.ckc.wo.event.internal;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.tutor.response.Response;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class InternalEvent extends Response {
    private SessionEvent sessionEvent;
    private String onEventName;

    public InternalEvent() {
    }

    public InternalEvent(SessionEvent sessionEvent, String onEventName) {
        this.sessionEvent = sessionEvent;
        this.onEventName = onEventName;
    }

    public SessionEvent getSessionEvent() {
        return sessionEvent;
    }

    public String getOnEventName() {
        return onEventName;
    }
}
