package edu.umass.ckc.wo.lc;

import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/21/16
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class LCMetaRule {
    private String name;
    private String units;
    private String value;
    private boolean status;   // true means active
    private int vali;

    // tell how long to wait until a the same rule can fire again
    private static final String AVOID_REPEAT_RULE_MR = "thisRuleNotFiredInPastMinutes";
    // tell how long to wait before giving another message from a rule set.
    private static final String AVOID_TOO_MANY_MESSAGES = "noMessageFiredInPastMinutes";

    public LCMetaRule(String name, String units, String value, boolean status) {
        this.name = name;
        this.units = units;
        this.value = value;
        this.status = status;
        if (units.equalsIgnoreCase("milliseconds"))
            this.vali = Integer.parseInt(value);
        else if (units.equalsIgnoreCase("seconds"))
            this.vali = Integer.parseInt(value) * 1000;
        else if (units.equalsIgnoreCase("minutes"))
            this.vali = Integer.parseInt(value) * 60 * 1000;
    }


    public static LCMetaRule createFromRuleXML (Element mrOverrideElt) {
        String name = mrOverrideElt.getAttributeValue("name");
        String value = mrOverrideElt.getAttributeValue("value");
        String units = mrOverrideElt.getAttributeValue("units");
        String status = mrOverrideElt.getAttributeValue("status");
        return new LCMetaRule(name,units,value,status == null ? true : Boolean.parseBoolean(status));
    }

    public boolean isStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getUnits() {
        return units;
    }

    public String getValue() {
        return value;
    }

    public String toString () {
        return "metaRule-override: " + getName() + " " + getValue() + " " + getUnits();
    }


    public List<LCRule> apply(StudentRuleHistory hist, List<LCRule> candidates, long now) {
        List<LCRule> keep = new ArrayList<LCRule>();
        for (LCRule r : candidates) {
            if (isSatisfied(hist, r, now))
                keep.add(r);
        }
        return keep;
    }

    /**
     * See if this meta-rule is true by testing the given rule against the student history and the current time.
     * If the given rule overrides this meta-rule, then use the value coming from the rule rather than defined in this meta rule.
     * @param hist
     * @param r
     * @param now
     * @return
     */
    public boolean isSatisfied(StudentRuleHistory hist, LCRule r, long now) {
        // the too-many messages meta rule says to not fire messages until a certain wait time.  We simply test the top of the history stack
        // (the last message given) to see when it was given and compare it to now.  If enough time has gone by, then it's ok to fire r.
        if (this.name.equalsIgnoreCase(AVOID_TOO_MANY_MESSAGES)) {
            StudentRuleHistory.LCRuleInstantiation inst = hist.getHistory().peek();
            long diff = now - inst.getTime();
            LCMetaRule ovmr = r.getMetaRuleOverride(this.name);
            int val = ovmr != null ? ovmr.vali : this.vali;
            if (diff > this.vali)
                return true;
            else return false;
        }
        else if (this.name.equalsIgnoreCase(AVOID_REPEAT_RULE_MR) ) {
            for (StudentRuleHistory.LCRuleInstantiation inst : hist.getHistory()) {
                // avoidRepeatRule means that we should return false if this rule fails any the test against
                //  any rule instantiation
                if ( !testInstantiation(inst,r,now))
                     return false;
            }
            return true;
        }
        return true;

    }

    /**
     * Test a given rule instantiation against the candidate rule and the current time.
     * @param inst
     * @param r
     * @param now
     * @return
     */
    private boolean testInstantiation(StudentRuleHistory.LCRuleInstantiation inst, LCRule r, long now) {
        // If the instantiation is for the given rule r, make sure it was fired longer ago than
        // the meta rules value.
        if (this.name.equalsIgnoreCase(AVOID_REPEAT_RULE_MR) && inst.getR()==r)  {
            long diff = now - inst.getTime();
            // if the rule has a meta-rule override, use the override value rather than this meta-rule's
            LCMetaRule mr = r.getMetaRuleOverride(this.name);
            int val = (mr != null) ? mr.vali : this.vali;
            return diff > this.vali;
        }
        return true;

    }
}
