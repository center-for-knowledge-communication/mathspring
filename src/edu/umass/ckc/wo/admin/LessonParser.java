package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.config.LessonXML;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/3/15
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonParser {
    private Element root;

    public LessonParser(InputStream str) throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, JDOMException {
        Document d = JDOMUtils.makeDocument(str);
        root = d.getRootElement();

    }

    public List<LessonXML> parse () {
        List<LessonXML> result = new ArrayList<LessonXML>();
        List<Element> lessonElts = root.getChildren("lesson");
        for (Element l: lessonElts) {
            Element control = l.getChild("controlParameters");
            Element interventions = l.getChild("interventions");
            String n = l.getAttributeValue("name");
            String style = l.getAttributeValue("style");
            String lessonModelClassName = l.getAttributeValue("className");
            result.add(new LessonXML(interventions,control,n,lessonModelClassName));
        }
        return result;
    }
}
