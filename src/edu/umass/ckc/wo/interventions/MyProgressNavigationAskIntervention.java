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
public class MyProgressNavigationAskIntervention extends InformationIntervention implements NextProblemIntervention {

   private Locale locale;

    public MyProgressNavigationAskIntervention(Locale loc) {
        this.locale = loc;
    }
	    
	public String getType () {
        return "MyProgressNavigationAsk";
    }


    public String getDialogHTML () {

   	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		str = "<div><p>" + rb.getString("lets_see_progress_we_are_making") + "<br></p></div>";

        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }

        str+="</div>";
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
