package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.tutor.response.Response;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 2:25:15 PM
 */
public class LCList extends Response {
    private String lcList;

    public LCList(String lcList) {
        this.setList(lcList);
        buildJSON();
    }



    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        jsonObject.element("LCList", this.getList());
        return jsonObject;

    }

    public String logEventName() {
        return "LCShowList " + (this.getList() != null ? this.getList() : "NoListAvailable");
    }

    public String getList() {
        return lcList;
    }

    public void setList(String lcList) {
        this.lcList = lcList;
    }
}
