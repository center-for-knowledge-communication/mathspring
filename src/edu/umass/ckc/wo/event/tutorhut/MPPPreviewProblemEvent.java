package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/4/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MPPPreviewProblemEvent extends MPPTopicEvent {

    int probId;
    private String location = ""; // will be either MPP or Dashboard (this is because Huy used MPP events in the dashboard...)
    public MPPPreviewProblemEvent(ServletParams p) throws Exception {
        super(p);
        setProbId(p.getInt("problemId"));
        this.location = p.getString("location","MPP");

    }

    public String getLocation () {
        return this.location;
    }

    public int getProbId() {
        return probId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }
}
