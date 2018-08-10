package edu.umass.ckc.wo.strat;

import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelector;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marshall on 6/16/17.
 */
public class ClassStrategyComponent {
    private int id;
    private String name;
    private String className;
    private String type;
    private List<SCParam> params;
    private List<InterventionSelectorSpec> interventionSelectors;

    public ClassStrategyComponent(int scId, String name, String className) {
        this.id=scId;
        this.name=name;
        this.className=className;
        this.interventionSelectors = new ArrayList<InterventionSelectorSpec>();
    }

    public void addInterventionSelector(InterventionSelectorSpec isel) {
        this.interventionSelectors.add(isel);
    }

    public void setInterventionSelectors(List<InterventionSelectorSpec> specs) { this.interventionSelectors = specs; }

    public void setParams(List<SCParam> params) {
        this.params = params;
    }

    public List<SCParam> getParams() {
        return params;
    }

    public String getClassName() {
        return className;
    }

    public List<InterventionSelectorSpec> getInterventionSelectors() {
        return interventionSelectors;
    }

    public String toString () {
        StringBuilder sb = new StringBuilder("StrategyComponent: " + id + " " + name + "\n");
        for (SCParam p: params) {
            sb.append("\t\t" + p.toString() + "\n");
        }
        for (InterventionSelectorSpec sel: this.interventionSelectors)
            sb.append("\t\t" + sel.toString() + "\n");
        return sb.toString();

    }

    public String getParameterValue(String name) {
        for (SCParam p: this.params) {
            if (p.getName().equals(name))
                return p.getValue();
        }
        return null;
    }

    public String getParameterValue(String name, String defaultVal) {
        String v = getParameterValue(name);
        if (v == null)
            return defaultVal;
        else return v;
    }
}
