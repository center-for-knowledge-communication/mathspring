package edu.umass.ckc.wo.event.internal;

import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeginningOfTopicEvent extends InternalEvent {

    private int topicId;

    public BeginningOfTopicEvent(SessionEvent sessionEvent, int topicId) {
        super(sessionEvent,"BeginningOfTopic");
        this.topicId=topicId;  // last topic we were in
    }

    public int getTopicId() {
        return topicId;
    }
}
