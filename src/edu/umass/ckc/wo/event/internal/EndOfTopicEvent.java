package edu.umass.ckc.wo.event.internal;

import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class EndOfTopicEvent extends InternalEvent {
    private int topicId;

    public EndOfTopicEvent(SessionEvent sessionEvent, int topicId) {
        super(sessionEvent,"EndOfTopic");
        this.topicId=topicId;
    }

    public int getTopicId() {
        return topicId;
    }
}
