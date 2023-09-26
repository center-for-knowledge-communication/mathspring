package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.tutor.response.Response;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 2:25:15 PM
 */

public class LCChange extends Response {
    private String LCNew;

    public LCChange(String shortname) {
        this.setLCNew(shortname);
        buildJSON();
    }



    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        jsonObject.element("LCNew", this.getLCNew());
        
        return jsonObject;

    }

    public String logEventName() {
        return "LCChange " + this.getLCNew();
    }

    public String getLCNew() {
        return LCNew;
    }

    public void setLCNew(String LCNew) {
        this.LCNew = LCNew;
    }
}
