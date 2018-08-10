package edu.umass.ckc.wo.lc;

import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/24/16
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentRuleHistory {

    private Stack<LCRuleInstantiation> hist;

    public StudentRuleHistory() {
        this.hist = new Stack<LCRuleInstantiation>();
    }

    public void addRule (LCRule rule) {
        LCRuleInstantiation inst = new LCRuleInstantiation(rule,System.currentTimeMillis());
        hist.push(inst);

    }

    public Stack<LCRuleInstantiation> getHistory () {
        return hist;
    }

    class LCRuleInstantiation {
        LCRule r;
        long time;

        LCRuleInstantiation(LCRule r, long time) {
            this.r = r;
            this.time = time;
        }

        public LCRule getR() {
            return r;
        }

        public long getTime() {
            return time;
        }
    }
}
