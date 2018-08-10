package edu.umass.ckc.wo.strat;

import edu.umass.ckc.wo.lc.LCRuleset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marshall on 6/30/17.
 */
public class LC {
    private String name;
    private int id;
    private String className;
    private String character;
    private List<LCRuleset> rulesets;

    public LC () {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<LCRuleset> getRulesets() {
        return rulesets;
    }

    public void addRuleset (LCRuleset s) {
        if (this.rulesets == null)
            this.rulesets = new ArrayList<LCRuleset>();
        this.rulesets.add(s);
    }

    public void setRulesets(List<LCRuleset> rulesets) {
        this.rulesets = rulesets;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
