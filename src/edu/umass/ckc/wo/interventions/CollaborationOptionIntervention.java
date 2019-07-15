package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 4/7/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationOptionIntervention extends InputResponseIntervention implements NextProblemIntervention{

    public static final String OPTION = "option";
    public static final String YES = "Yes_collaborate";
    public static final String NO_ALONE = "No_alone";
    public static final String NO_DECLINE = "No_decline";

    private Locale locale;

    public  CollaborationOptionIntervention (Locale loc) {
        super();
        this.locale = loc;

    }
    public String getType () {
        return "CollaborationOptionIntervention";
    }


    public String getDialogHTML () {
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);

        		str = "<div><p>" + rb.getString("would_you_like_to_work_with_person_near_you") + "  <br/<br/>" + getFormOpen();
        		str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+YES+"\" checked=\"checked\">" + rb.getString("yes_someone_next_to_me") + "</input><br>";
        		str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+NO_ALONE+"\">" + rb.getString("no_no_one_next_to_me") + "</input><br>";
        		str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+NO_DECLINE+"\">" + rb.getString("no_do_not_want_to") + "</input><br>";
        		str += "<input type=\"hidden\" name=\"destination\" value=\"edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS\">";
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
