package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * User: Frank
 * Date: May 25, 2021
 */
public class GazeWanderingEvent extends IntraProblemEvent {

    private String gazeJSONData;

    public GazeWanderingEvent(ServletParams p) throws Exception {
        super(p);
        this.gazeJSONData = p.getString("gazeJSONData", null);
    }

    public String getGazeJSONData() {
        return gazeJSONData;
    }
}