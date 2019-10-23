package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;
import org.jdom.Element;

import java.util.Locale;
import java.util.ResourceBundle;

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
    private Locale locale;

    public AskGoalsIntervention(String inputType, Locale loc) {

        this.askWhy=askWhy;
        this.locale = loc;


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

    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
    	
        str = "<div>  " +
                getFormOpen() + " <p>" + rb.getString("ask_goals_question");

        str += " <br><br><textarea name=\"" + GOALS + "\" rows=\"6\" cols=\"45\"/>" ;
//        str += "<br><br>3. What do you want to happen?";
//        str += "<textarea name=\"" + RESULT + "\" rows=\"3\" cols=\"40\"/>" ;
        str+= "</p>";

        str+="</form></div>";
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }

        return str;
    }

    public String getType () {
        return "AskGoalsIntervention";
    }
}
