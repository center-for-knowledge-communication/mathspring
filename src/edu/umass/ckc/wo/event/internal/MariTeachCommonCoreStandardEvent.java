package edu.umass.ckc.wo.event.internal;

import edu.umass.ckc.wo.event.SessionEvent;

/**
 * This is an internal event that is created to support a call from an outside system like MARi or Assistments.
 * It is used to start the PedagogicalModel off by calling its processInternalEvent method which leads to showing some problems.
 * User: david
 * Date: 5/5/16
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class MariTeachCommonCoreStandardEvent extends InternalEvent {


    public MariTeachCommonCoreStandardEvent () {}

    public MariTeachCommonCoreStandardEvent(SessionEvent sessionEvent, String onEventName) {
        super(sessionEvent, onEventName);
    }
}
