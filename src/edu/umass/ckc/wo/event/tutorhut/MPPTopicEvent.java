package edu.umass.ckc.wo.event.tutorhut;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/4/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */

import ckc.servlet.servbase.ServletParams;


public class MPPTopicEvent extends TutorHutEvent {
    private int topicId;
    private String userInput; // not passed in to the event from client.  Set by server so that topic or problem selected by student is put in userInput of eventlog
    public MPPTopicEvent(ServletParams p) throws Exception {
        super(p);
        String tid = p.getString("topicId");
        if (tid.length()>0 && !tid.equalsIgnoreCase("undefined"))
            topicId= Integer.parseInt(tid);
        else topicId = -1;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}