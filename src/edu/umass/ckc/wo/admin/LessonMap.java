package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.config.LessonXML;
import edu.umass.ckc.wo.tutor.Pedagogy;
import org.apache.log4j.Logger;
import org.jdom.DataConversionException;
import org.jdom.JDOMException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.List;

/**
 * Maps the Pedagogy id to a Pedagogy object.
 * Later students will be assigned to a group number that is one of these pedagogy ids.  This map is used
 * to find the Pedagogy for a student.
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 20, 2007
 * Time: 10:27:19 AM
 */
public class LessonMap extends Hashtable<String, LessonXML> {

    private static final Logger logger = Logger.getLogger(LessonMap.class);


    public LessonMap() {}

    public LessonMap(InputStream str) throws IOException, ClassNotFoundException, JDOMException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        LessonParser parser = new LessonParser(str); // a list of Pedagogy objects
        List<LessonXML> l = parser.parse();
        buildMap(l);
    }

    private void buildMap( List<LessonXML> l) {
        for (LessonXML les: l)  {
            this.put(les.getName(),les);
        }
    }



}
