package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/12/15
 * Time: 11:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationOriginatorIntervention extends TimeoutIntervention implements NextProblemIntervention{
    private Locale locale;

    public  CollaborationOriginatorIntervention (Locale loc) {
        super();
        this.locale = loc;

    }
    public String getType () {
        this.setWaitTime(1000);
        return "CollaborationOriginatorIntervention";
    }

    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);

        		str = "<div><p>" + rb.getString("please_wait_for_a_partner") + "<br/></p>";
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
    }}
