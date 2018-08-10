package edu.umass.ckc.wo.interventions;

import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/21/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class HighlightHintButtonIntervention extends ChangeGUIIntervention implements AttemptIntervention {


    public HighlightHintButtonIntervention(String when, String dialogHTML, boolean notify, String component, String action) {
        super(when, dialogHTML, notify, component, action);
    }

    public String getType () {
        return "HighlightHintButton";
    }


    @Override
    public boolean isShowGrade() {
        return true;     // note that Flash problems grade themselves, so this will only work for HTML problems.
    }




}
