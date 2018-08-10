package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/21/13
 * Time: 1:53 PM
 * Shows a dialog accusing user of guessing and asking if he is bored or frustrated.

 */
public class RapidAttemptIntervention extends InputResponseIntervention implements AttemptIntervention {

    public static final String AFFECT = "affect";
    public static final String FRUSTRATED = "frustrated";
    public static final String BORED = "bored";


    @Override
    public String logEventName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShowGrade() {
        return true;     // note that Flash problems grade themselves, so this will only work for HTML problems.
    }

    @Override
    public String getType() {
        return "RapidAttemptIntervention";
    }

    public String getDialogHTML() {
        String str = "<div>  " +getFormOpen()+ " <p>You seem to be guessing!   Can you tell me if you are:<br><br>";
        str+= "<input type=\"radio\" name=\""+AFFECT+"\" value=\"" +FRUSTRATED+ "\">frustrated<br>";
        str+= "<input type=\"radio\" name=\""+AFFECT+"\" value=\"" +BORED+ "\">bored<br>";
        str+="</p></form></div>";
        return str;
    }
}
