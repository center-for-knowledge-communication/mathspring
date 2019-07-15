package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 4/7/15
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationTimedoutIntervention extends InputResponseIntervention implements NextProblemIntervention{

    public static final String OPTION = "option";
    public static final String YES = "Yes_wait";
    public static final String NO = "No";

    private Locale locale;

    public  CollaborationTimedoutIntervention (Locale loc) {
        super();
        this.locale = loc;

    }

    public String getType () {
        return "CollaborationTimedoutIntervention";
    }


    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);

        		str = "<div><p>" + rb.getString("no_partner_yet") + " <br>" +
        				rb.getString("wait_for_partner") + "<br/<br/>" + getFormOpen();
        		str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+NO+"\" checked=\"checked\">" + rb.getString("no") + "</input><br>";
        		str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+YES+"\">" + rb.getString("yes") + "</input><br>";
        		str+="</form></div>";
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
