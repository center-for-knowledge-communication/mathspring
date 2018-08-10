package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by rezecib on 4/10/2017.
 */
public class AdminEditProblemFormatEvent extends ActionEvent {
    private int probId;

    public AdminEditProblemFormatEvent (ServletParams p) throws Exception {
        super(p);
        this.probId = p.getInt("problemId",-1);
    }

    public int getProbId() {
        return probId;
    }
}
