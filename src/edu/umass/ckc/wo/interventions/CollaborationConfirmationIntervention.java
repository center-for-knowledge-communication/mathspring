package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/12/15
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationConfirmationIntervention extends InputResponseIntervention implements NextProblemIntervention{

    private final String partnerName;
    private Locale locale;

    public  CollaborationConfirmationIntervention (String partnerName, Locale loc) {
        super();
        this.partnerName = partnerName;
        this.locale = loc;
    }
    
    public String getType () {
        return "CollaborationConfirmationIntervention";
    }


    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		 rb = ResourceBundle.getBundle("MathSpring",this.locale);

                 str = "<div><p>" + rb.getString("next_activity_is_special") + "<br/> " +
                 rb.getString("you_will_be_working_with") + "<b><font color='red'>" + partnerName + "</font></b>" + rb.getString("on_one_problem") +
                 "<b><font color='red'>" + partnerName + "</font></b>," + rb.getString("collaborate_instructions_1") + 
                 rb.getString("collaborate_instructions_2") + "<br/>" +
                 rb.getString("collaborate_instructions_3");
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
