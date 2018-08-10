package edu.umass.ckc.wo.lc;

import java.util.ArrayList;
import java.util.List;

/**
 *   A single rule that is composed of several conditions that can be tested.  The rule is evaluated by evaluating all its conditions.
 *   If they are all true, the rule's action is executed.
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/25/16
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCRule extends LCRuleComponent implements Comparable<LCRule>{

    List<LCCondition> conditions;
    LCAction action;
    private int id;
    private String name;
    private String descr;
    private String onEvent;
    private double priority;
    private String notes;
    private String interventionPointName;
    private List<LCMetaRule> overrideMetaRules;

    public LCRule() {
        conditions = new ArrayList<LCCondition>();
    }

    public LCRule(int id, String name, String descr, String onEvent, double priority) {
        this();
        this.id=id;
        this.name=name;
        this.descr=descr;
        this.onEvent=onEvent;
        this.priority=priority;
    }

    public List<LCCondition> getConditions() {
        return conditions;
    }

    public void addCondition(LCCondition c) {
        conditions.add(c);
    }

    public LCMetaRule getMetaRuleOverride (String metaRuleName) {
        if (overrideMetaRules != null) {
            for (LCMetaRule mr : overrideMetaRules) {
                if (mr.getName().equals(metaRuleName))
                    return mr;
            }
        }
        return null;
    }

    public List<LCMetaRule> getMetaRuleOverrides () {
        if (overrideMetaRules != null) {
            return this.overrideMetaRules;
        }
        return new ArrayList<LCMetaRule>();
    }

    /**
     * Test to see if a rule has all conditions true
     * @return true if all the rule conditions are true
     * Writes information about the condition and the data it matches into the ruleDataInfo String
     * @throws Exception
     */
    public boolean test (StringBuilder ruleDataInfo) throws Exception {
        try {
            for (LCCondition cond : conditions) {
                cond.setup(smgr, event, cache);
                boolean res = cond.eval();
                if (!res)
                    return false;
                // If the condition succeeds, add info about the test and data to the info string (for logging)
                ruleDataInfo.append(cond.getConditionInfo() + ",");
            }
            action.setup(smgr, event, cache);
            // if there's data in the string delete the trailing comma
            if (ruleDataInfo.length() > 0) {
                ruleDataInfo.deleteCharAt(ruleDataInfo.length()-1);
            }
            return true;    // if all conditions are true, return true
        } catch (Exception ee) {
            System.out.println("Failed to evaluate rule: " + this.getName());
            throw ee;
        }
    }

    /**
     *
     */
    public LCAction getAction () {
       return this.action;
    }

    public String getOnEvent() {
        return onEvent;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public double getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public void setAction(LCAction action) {
        this.action = action;
    }

    public boolean eval () throws Exception {
        boolean fail = false;
        for (LCCondition c : conditions) {
            c.setup(smgr,event, cache);
            if (!c.eval()) {
                fail = true;
                break;
            }
        }
        return !fail;

    }

    @Override
    public int compareTo(LCRule lcRule) {
        if (this.priority < lcRule.getPriority())
            return -1;
        else if (this.priority > lcRule.getPriority())
            return 1;
        else return 0;
    }

    public String getNotes() {
        return notes;
    }


    public String getInterventionPointName() {
        return interventionPointName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String toString () {
        StringBuilder sb = new StringBuilder(this.getName() + "(" +this.id+ "): IF ");
        sb.append("userEvent= " + this.getOnEvent()).append(" AND ");
        for (LCCondition c : this.getConditions())
            sb.append(c.toString()).append(" AND ");
        for (LCMetaRule mr : this.getMetaRuleOverrides())
                sb.append(mr.toString()).append(" AND ");
        if (sb.indexOf("AND") != -1)
            sb.delete(sb.lastIndexOf("AND"),sb.length());
        sb.append(" THEN " );
        sb.append(this.getAction().getName() + " ("+this.getAction().getMsgText()+")");
        return sb.toString();
    }

    public void addMetaRuleOverride(LCMetaRule mr) {
        if (this.overrideMetaRules == null) {
            this.overrideMetaRules = new ArrayList<LCMetaRule>();
        }
        this.overrideMetaRules.add(mr);
    }
}
