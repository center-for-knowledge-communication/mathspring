package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/20/14
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeLanguageEvent extends TutorHutEvent {
    int probId;
    String lc;

    public ChangeLanguageEvent(ServletParams p) throws Exception {
        super(p);
        probId = p.getInt("probId");
        lc = p.getString("learningCompanion");
    }

    public int getProbId() {
        return probId;
    }

    public String getLc() {
        return lc;
    }
}
