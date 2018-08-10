package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.tutor.response.Response;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 2:25:15 PM
 */
public class Video extends Response {
    private String url;

    public Video (String url) {
        this.setUrl(url);
        buildJSON();
    }



    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        jsonObject.element("video", this.getUrl());
        return jsonObject;

    }





    public String logEventName() {
        return "video " + (this.getUrl() != null ? this.getUrl() : "NoVideoAvailable");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
