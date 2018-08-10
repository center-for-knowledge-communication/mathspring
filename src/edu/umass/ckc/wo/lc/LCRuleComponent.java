package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/10/16
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCRuleComponent {

    protected SessionManager smgr;
    protected SessionEvent event;
    protected  StudentDataCache cache;

    public void setup(SessionManager smgr, SessionEvent event, StudentDataCache cache) {
        this.smgr = smgr;
        this.event = event;
        this.cache = cache;
    }
}


