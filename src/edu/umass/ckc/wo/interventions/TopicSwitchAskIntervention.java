package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public TopicSwitchAskIntervention(String reasons, int sessionId) {
        this.reasons = reasons;
        this.sessionId= sessionId;
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
        String str = "<div>  " +getFormOpen()+ " <p>You are about to switch topics because:<br>";
        str += reasons;

        str += "<br><br>";
        // increment the elapsedTime and probElapsedTime and then put these and sessionId into hidden inputs to the form
//        str+="<script type=\"text/javascript\">incrementTimers(globals); document.getElementById('hiddenparams').innerHTML=";
//        str+="'<input type=\"hidden\" name=\"action\" value=\"InputResponseNextProblemIntervention\">";
//        str+="<input type=\"hidden\" name=\"sessionId\" value=\""+sessionId+"\">";
//        str+="<input type=\"hidden\" name=\"elapsedTime\" value=\"'+globals.elapsedTime+'\">";
//        str+="<input type=\"hidden\" name=\"probElapsedTime\" value=\"'+globals.probElapsedTime+'\">';</script>";

//        str+="<div id=\"hiddenparams\">";
//        str+="</div>";
        str+= "<input type=\"radio\" name=\""+WANT_TO_SWITCH+"\" value=\"" +SWITCH+ "\">Go forward to next topic<br>";
        str+= "<input type=\"radio\" name=\""+WANT_TO_SWITCH+"\" value=\"" +STAY+ "\">Stay in the current topic<br>";
        str+="</p></form></div>";
        return str;
    }

    public String getType () {
        return "TopicSwitch";
    }




}
