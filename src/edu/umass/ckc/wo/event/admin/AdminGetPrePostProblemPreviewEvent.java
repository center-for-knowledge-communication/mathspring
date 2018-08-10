package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: May 5, 2010
 * Time: 1:38:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminGetPrePostProblemPreviewEvent extends ActionEvent {
    private int probId;

    public AdminGetPrePostProblemPreviewEvent (ServletParams p) {
        probId = p.getInt("probId");
    }

    public int getProbId() {
        return probId;
    }
}
