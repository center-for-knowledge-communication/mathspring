package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/4/14
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalActivityAskIntervention extends InformationIntervention implements NextProblemIntervention {

    private Locale locale;

    public ExternalActivityAskIntervention(Locale loc) {
        this.locale = loc;
    }

	public String getType () {
        return "ExternalActivityAskIntervention";
    }


    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		str = "<div><p>" + rb.getString("lets_try_different_activity") + "<br></p></div>";

        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
