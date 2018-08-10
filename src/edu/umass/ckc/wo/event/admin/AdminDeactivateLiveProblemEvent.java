package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;


/**
 *   This event will deactivate a problem (for all classes) while the tutor is running.
 */
public class AdminDeactivateLiveProblemEvent extends ActionEvent {

    private int probId;
    private String probName;
    public static final String PROB_ID = "probId";
    public static final String PROB_NAME = "probName";

    public AdminDeactivateLiveProblemEvent(ServletParams p) throws Exception {
        super(p);
        probId = p.getInt(PROB_ID,-1);
        probName = p.getString(PROB_NAME,null);
    }

    public int getProbId() {
        return probId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }

    public String getProbName() {
        return probName;
    }

    public void setProbName(String probName) {
        this.probName = probName;
    }
}