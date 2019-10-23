package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/7/13
 * Time: 4:00 PM
 * This intervention informs the user that they are about to switch topics.   It no longer is set to ask them if they
 * want to because it is difficult to show problems in a topic that has just been found to be exhausted.
 */
public class TopicSwitchAskIntervention extends InputResponseIntervention implements NextProblemIntervention {

    public static final String WANT_TO_SWITCH = "wantSwitch";
    public static final String SWITCH = "switch";
    public static final String STAY = "stay";
    private int sessionId;
    protected String reasons;
    private Locale locale;


    public TopicSwitchAskIntervention(String reasons, int sessionId, Locale loc) {
        this.reasons = reasons;
        this.sessionId= sessionId;
        this.locale = loc;
    }

    @Override
    public String logEventName() {
        return "TopicSwitchAskIntervention";
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDialogHTML() {
    	
    	
    	System.out.println("TopicSwitch locale = " + this.locale.toString());
    	
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
                str = "<div>  " +getFormOpen()+ " <p>" + rb.getString("you_are_about_to_switch_topics_because")+ ":<br>";
                str += reasons;

                str += "<br><br>";
                str+= "<input type=\"radio\" name=\""+WANT_TO_SWITCH+"\" value=\"" +SWITCH+ "\">" + rb.getString("go_forward_to_next_target") + "<br>";
                str+= "<input type=\"radio\" name=\""+WANT_TO_SWITCH+"\" value=\"" +STAY+ "\">" + rb.getString("stay_in_current_topic") + "<br>";
                str+="</p></form></div>";
        
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }
        return str;
    }

    public String getType () {
        return "TopicSwitch";
    }




}
