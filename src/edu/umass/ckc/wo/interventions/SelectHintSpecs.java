package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * When an intervention selector has a response or continue request come back to it, and it wants
 * to select a hint, it must return this object (an intervention) so that the pedagogical model can
 * then act accordingly.   If there are certain guidelines for making the hint selection, the intervention
 * can set them in this object's name.
 *
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2009
 * Time: 7:18:20 AM
 */
public class SelectHintSpecs implements Intervention {

    
    public SelectHintSpecs() {}

    public String getName() {  return null;
    }

    // for logging these activities are getting logged much like a problem.  Therefore they need to have an id that
    // uniquely identifies them
    public int getId() { return 0;
    }

    public String getResource() {
        return null;
    }


    public String logEventName() {
        return null;  
    }

    public JSONObject buildJSON(JSONObject jo) {
        return jo;
    }
}
