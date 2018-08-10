package edu.umass.ckc.wo.event.internal;

import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class InTopicEvent extends InternalEvent {
    private int topicId;

    public InTopicEvent(SessionEvent sessionEvent, int topicId) {
        super(sessionEvent,"InTopic");
        this.topicId=topicId;
    }

    public int getTopicId() {
        return topicId;
    }
}
