package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.xml.JDOMUtils;
import net.sf.json.JSONObject;
import org.jdom.Element;

/**
 * An intervention selector gets an InputResponse and may choose to select a problem.   This
 * object (an intervention) must be returned by the intervention selector to alert the pedagogical
 * model that it should select a problem.   This object may be given a name that acts as a guideline
 * in making the problem selection (e.g. easier, harder, same).
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2009
 * Time: 7:18:20 AM
 */
public class SelectProblemSpecs implements Intervention {


    // The intervention selector is passing these names as responses in the dialog spec, because they are what get
    // sent back by the user when he clicks a button.   These are then used to create the SelectProblemSpec
    public static final String HARDER_PROBLEM = "harderProblem";
    public static final String EASIER_PROBLEM = "easierProblem";
    public static final String SAME_DIFFICULTY_PROBLEM = "sameDifficultyProblem";

    private String name;
    public enum specType {TOPIC_SELECTION, DIFFICULTY_SELECTION};
    private specType type ;

    /**
     *  sets the user input (harder/easier/same ) as the name
     * to be returned to a ped model that can then call a problem selector with this arg
     * @param userInput XML of the form <userInput difficulty="easierProblem|sameDifficultyProblem|harderProblem"/>
     * or <userInput topic="id"/>
     */
    public SelectProblemSpecs(String userInput, specType type) throws Exception {
        Element e = JDOMUtils.getRoot(userInput);
        String d;
        this.type = type;
        if (type == specType.DIFFICULTY_SELECTION) {
             d = e.getAttributeValue("difficulty");
        }
        else {
            d = e.getAttributeValue("topic");
        }
        this.name = d;
    }

    public specType getType () {
        return this.type;
    }


    public String getName() {
        return name;
    }

    // for logging these activities are getting logged much like a problem.  Therefore they need to have an id that
    // uniquely identifies them
    public int getId() { return 0;
    }

    public String getResource() {
        return null;
    }


    public TopicModel.difficulty getDesiredDifficulty () {
        if (name.equalsIgnoreCase(HARDER_PROBLEM))
            return LessonModel.difficulty.HARDER;
        else if (name.equalsIgnoreCase(EASIER_PROBLEM))
            return LessonModel.difficulty.EASIER;
        else return LessonModel.difficulty.SAME;
    }


    public String logEventName() {
        return null;  
    }

    public JSONObject buildJSON(JSONObject jo) {
        return jo;
    }
}