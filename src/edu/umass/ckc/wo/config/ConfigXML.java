package edu.umass.ckc.wo.config;

import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/3/15
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigXML {
    protected Element interventions;
    protected Element control;
    protected String name;


    public ConfigXML(Element interventions, Element control, String name) {
        this.interventions = interventions;
        this.control = control;
        this.name = name;
    }

    public Element getInterventions() {
        return interventions;
    }

    public void setInterventions(Element interventions) {
        this.interventions = interventions;
    }

    public Element getControl() {
        return control;
    }

    public void setControl(Element control) {
        this.control = control;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
