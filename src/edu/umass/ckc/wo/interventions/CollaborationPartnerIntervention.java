package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/13/15
 * Time: 3:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationPartnerIntervention extends TimeoutIntervention implements NextProblemIntervention{

    private String name = null;
    private Locale locale;

    public  CollaborationPartnerIntervention (Locale loc) {
        super();
        this.locale = loc;
    }
 
    public String getType () {
        return "CollaborationPartnerIntervention";
    }

    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		 rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		 
        		/* 
        		 str = "<div><p>In this next problem, you will work with <b><font color='red'>" + name + "</font></b> who should be" +
                " sitting next to you, on <b><font color='red'>" + name + "</font></b>'s screen for <strong>ONE</strong> math problem. <br/> <br/>" +
                "<b><font color='red'>" + name + "</font></b> will use the mouse and keyboard." +
                " Your job is to <strong>READ</strong> the math problem aloud on "+name+"'s screen.<br/><br/>" +
                " Work <strong>together</strong> to solve the problem.<br/>";
				*/
        		 
        		 
        
        		 str = "<div><p>" + rb.getString("partner_instructions_1") + "<b><font color='red'>" + name + "</font></b>" + 
        		 rb.getString("partner_instructions_2") + "<b><font color='red'>" + name + "</font></b>" + rb.getString("partner_instructions_3") + "<br/> <br/>" +
        	     "<b><font color='red'>" + name + "</font></b>" + rb.getString("partner_instructions_4") + rb.getString("partner_instructions_5") +  
        	     name + rb.getString("partner_instructions_6") + "<br/>";
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

    public void setPartner(String name){
        this.name = name;
    }
}
