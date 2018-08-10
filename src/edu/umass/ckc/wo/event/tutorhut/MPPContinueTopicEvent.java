package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/4/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MPPContinueTopicEvent extends MPPTopicEvent {
    private String location = ""; // will be either MPP or Dashboard (this is because Huy used MPP events in the dashboard...)
    public MPPContinueTopicEvent(ServletParams p) throws Exception {
        super(p);
        this.location = p.getString("location","MPP");

    }

    public String getLocation () {
        return this.location;
    }

}
