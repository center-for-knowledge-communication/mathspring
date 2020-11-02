package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.woserver.ServletInfo;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/7/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicSwitchIntervention extends InputResponseIntervention implements NextProblemIntervention {
    protected String reasons;
    private int solved;
    private int seen;
    private Locale locale;

    public TopicSwitchIntervention(String reasons, int seen, int solved, Locale loc) {
        this.reasons = reasons;
        this.solved = solved;
        this.seen = seen;
        this.locale = loc;
    }



    public String getType () {
        return "TopicSwitch";
    }


    public String getDialogHTML () {
    	
    	System.out.println("TopicSwitch locale = " + this.locale.toString());
    	
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
                str = "<div><p>" + rb.getString("you_are_about_to_switch_topics_because") + ":<br>";
                str += reasons;
                str += "<br><br>" + rb.getString("you_have_solved") + " " + solved + " / " + seen + " " + rb.getString("of_the_problems_you_saw") + ".";
                str+="</p></div>";
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
