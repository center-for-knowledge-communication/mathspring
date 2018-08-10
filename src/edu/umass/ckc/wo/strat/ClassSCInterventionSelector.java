package edu.umass.ckc.wo.strat;

import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorParam;

import java.util.List;

/**
 * Created by marshall on 6/16/17.
 */
public class ClassSCInterventionSelector {
    private int id;
    private String config;
    private String name;
    private String className;
    private String onEvent;

    private List<InterventionSelectorParam> params;

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setOnEvent(String onEvent) {
        this.onEvent = onEvent;
    }

    public List<InterventionSelectorParam> getParams() {
        return params;
    }

    public void setParams(List<InterventionSelectorParam> params) {
        this.params = params;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString () {
        StringBuilder sb = new StringBuilder("InterventionSelector " + id + " " + name + "\n");
        for (InterventionSelectorParam p : this.params) {
            sb.append("\t\t\t" + p.toString() + "\n");
        }
        return sb.toString();
    }
}
