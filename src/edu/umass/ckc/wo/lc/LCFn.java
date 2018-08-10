package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/25/16
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCFn extends LCRuleComponent {
    private String fnName; // a method in the LCAccessors class
    private Class[] args;

    public LCFn(String fnName, Class[] args) {
        this.fnName = fnName;
        if (args == null)
            this.args = new Class[] {SessionEvent.class};
        else
            this.args = args;
    }

    public String getFnName() {
        return fnName;
    }

    public void setFnName(String fnName) {
        this.fnName = fnName;
    }

    public Class[] getArgs() {
        return args;
    }

    public void setArgs(Class[] args) {
        this.args = args;
    }

    public Object eval () throws Exception {
        LCAccessors accessors = new LCAccessors(smgr,cache);
        return accessors.eval(this);
    }

    public String toString () {
        return this.fnName;
    }
}
