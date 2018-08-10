package edu.umass.ckc.wo.config;

import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/3/15
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonXML extends ConfigXML{
    protected String className;


    public LessonXML(Element interventions, Element control, String name, String lessonModelClassname) {
        super(interventions, control, name);
        this.className = lessonModelClassname;
    }

//    public LessonModelParameters getLessonModelParams () throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Class c = Class.forName(className);
//        Constructor constructor = c.getConstructor(new Class[] {Element.class});
//        constructor.newInstance(this.control);
//        if (style.equalsIgnoreCase("topics"))
//            return new TopicModelParameters(this.control);
//        else return new LessonModelParameters(this.control);
//    }

    public String getLessonModelClassname () {
        return this.className;
    }


}
