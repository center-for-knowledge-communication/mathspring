package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/1/15
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginInterventionInputEvent extends SessionEvent {

    public LoginInterventionInputEvent(ServletParams p) throws Exception {
        super(p);
    }
}
