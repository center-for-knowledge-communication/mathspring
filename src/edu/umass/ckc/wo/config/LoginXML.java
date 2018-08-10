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
public class LoginXML extends ConfigXML{


    public LoginXML(Element interventions, Element control, String name) {
        super(interventions, control, name);
    }


}
