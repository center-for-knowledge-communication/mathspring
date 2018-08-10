package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;
import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/14/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AskGoalsIntervention extends InputResponseIntervention implements NextProblemIntervention {

    public static final String GOALS = "goals" ;
    private boolean askWhy;

    public AskGoalsIntervention(String inputType) {

        this.askWhy=askWhy;


    }


    public void configure (Element configXML) {

    }

    @Override
    public String logEventName() {
        return "AskGoalsIntervention";
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDialogHTML() {
        String str = "<div>  " +
                getFormOpen() + " <p>Some people set goals for themselves as they use MathSpring. We wonder... Do you have any goals, or intentions, for this new phase? What are you trying to accomplish?";

        str += "<br><br><textarea name=\"" + GOALS + "\" rows=\"6\" cols=\"45\"/>" ;
//        str += "<br><br>3. What do you want to happen?";
//        str += "<textarea name=\"" + RESULT + "\" rows=\"3\" cols=\"40\"/>" ;
        str+= "</p>";

        str+="</form></div>";

        return str;
    }

    public String getType () {
        return "AskGoalsIntervention";
    }
}
