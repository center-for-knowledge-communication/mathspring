package edu.umass.ckc.wo.tutor.model;

import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/11/15
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterventionSpec implements Comparable<InterventionSpec> {
    private String onEvent;
    private String className;
    private Element config;
    private int weight;


    /* Takes XML like
     <interventionSelector onEvent="EndOfTopic" weight="1" class="TopicSwitchAskIS">
                    <config>
                        <ask val="false"></ask>
                    </config>
                </interventionSelector>
     */
    public InterventionSpec(Element intervSel) {
        this.onEvent = intervSel.getAttributeValue("onEvent");
        this.className = intervSel.getAttributeValue("class");
        String w = intervSel.getAttributeValue("weight");
        if (w != null)
            this.weight = Integer.parseInt(w);
        else this.weight = 1;
        this.config = intervSel.getChild("config");
    }

    public String getOnEvent() {
        return onEvent;
    }

    public String getClassName() {
        return className;
    }

    public Element getConfig() {
        return config;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(InterventionSpec interventionSpec) {
        if (this.getWeight() < interventionSpec.getWeight())
            return -1;
        else if (this.getWeight() > interventionSpec.getWeight())
            return 1;
        else return 0;
    }
}
