package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/8/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbTestEvent extends SessionEvent {
    boolean quick=true;

    public DbTestEvent(ServletParams p) throws Exception {
        super(p);
        this.quick = p.getBoolean("isQuick",true);
    }

    public boolean isQuick() {
        return quick;
    }
}
