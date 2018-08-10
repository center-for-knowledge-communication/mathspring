package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Hint;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class HintResponse extends Response {
    private Hint hint;
    private String logEventName="hint";

    public HintResponse(Hint hint) {
        this.hint = hint;
        buildJSON();
    }
    public HintResponse (String logEventName) {
        this.logEventName = logEventName;
        buildJSON();
    }

    public String logEventName() {
           return logEventName;
    }

    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        if (hint != null)
            return hint.getJSON(jsonObject);
        else return jsonObject;
    }


    public Hint getHint() {
        return hint;
    }


}
