package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Reinforcement;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class ReinforcementResponse extends Response {
    private Reinforcement r;

    public ReinforcementResponse(Reinforcement r) {
        this.r = r;
        buildJSON();
    }

    public String logEventName() {
           return "attempt";
    }

    private String reinforcementResponse() {
        StringBuffer result = new StringBuffer(64);
        result.append("&sound=\n");
        result.append("&balloon_text=Great job!\n");
        result.append("&action_type=reinforcement\n");
//       result.append("<sound/>");
//       result.append("<balloonText>Great job!</balloonText>");
//       result.append("<action type=\"reinforcement\"/>");
        return result.toString();
    }


    public Reinforcement getReinforcement() {
        return r;
    }


}