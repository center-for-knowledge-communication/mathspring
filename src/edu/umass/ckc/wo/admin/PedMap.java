package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.tutor.Pedagogy;
import org.apache.log4j.Logger;
import org.jdom.DataConversionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
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
public class PedMap extends Hashtable<String, Pedagogy> {

    private static final Logger logger = Logger.getLogger(PedMap.class);

    public PedMap(InputStream str) throws IOException, ClassNotFoundException, DataConversionException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        PedagogyParser config = new PedagogyParser(str); // a list of Pedagogy objects
        buildMap(config);
    }

    public PedMap () {}

    private void buildMap(PedagogyParser config) {
        for (Pedagogy p: config.getPedagogies())  {
            this.put(p.getId(),p);
            logger.debug(p);
        }
    }

    public Pedagogy getDefaultPedagogy () {
        for (Pedagogy p: this.values()) {
            if (p.isDefault())
                return p;
        }
        return null;
    }

    public static void main (String[] args) {
        try {
            PedMap m = new PedMap(new FileInputStream("U:\\dev\\mathspring\\woServer\\resources\\pedagogies.xml"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
