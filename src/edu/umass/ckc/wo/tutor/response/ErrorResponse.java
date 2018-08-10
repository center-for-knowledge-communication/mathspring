package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.servlet.servbase.View;
import net.sf.json.JSONObject;

/**
 * Created by marshall on 6/6/17.
 * Used to create a View that is JSON containing error information that can be sent back to the client
 */
public class ErrorResponse implements View {

    private JSONObject obj;

    public ErrorResponse (String errorType, String message) {
        obj = new JSONObject();
        obj.element("error",errorType);
        obj.element("message",message);
        obj.element("fatal", false);

    }

    public ErrorResponse (Exception e) {
        obj = new JSONObject();
        obj.element("error",e.getClass().getName());
        obj.element("message",e.getMessage());
        obj.element("fatal", false);
    }

    public ErrorResponse (Exception e, boolean fatal) {
        this(e);
        obj.element("fatal", fatal);
    }


    @Override
    public String getView() throws Exception {
        return obj.toString();
    }
}
