package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/21/13
 * Time: 1:53 PM
 * Shows a dialog accusing user of guessing and asking if he is bored or frustrated.

 */
public class RapidAttemptIntervention extends InputResponseIntervention implements AttemptIntervention {

    public static final String AFFECT = "affect";
    public static final String FRUSTRATED = "frustrated";
    public static final String BORED = "bored";

    private Locale locale;

    public RapidAttemptIntervention(Locale loc) {
        this.locale = loc;
    }


    @Override
    public String logEventName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShowGrade() {
        return true;     // note that Flash problems grade themselves, so this will only work for HTML problems.
    }

    @Override
    public String getType() {
        return "RapidAttemptIntervention";
    }

    public String getDialogHTML() {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		str = "<div>  " +getFormOpen()+ " <p>" + rb.getString("you_seem_to_be_guessing") + "<br><br>";
        		str+= "<input type=\"radio\" name=\""+AFFECT+"\" value=\"" +FRUSTRATED+ "\">" + rb.getString("frustrated") + "<br>";
        		str+= "<input type=\"radio\" name=\""+AFFECT+"\" value=\"" +BORED+ "\">" + rb.getString("bored") + "<br>";
        		str+="</p></form></div>";
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }

        return str;
    }
}
