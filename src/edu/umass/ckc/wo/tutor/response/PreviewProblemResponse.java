package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class PreviewProblemResponse extends Response {
    protected String previewProblemResults;


    public PreviewProblemResponse(String previewProblemResults) {
        this.previewProblemResults = previewProblemResults;
        buildJSON();
    }

    public String logEventName() {
        
        return "PreviewProblem";
    }

    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        if (previewProblemResults == "No results") {
            String label =  "noMoreProblems";
            jsonObject.element("activityType", label);
            jsonObject.element("previewProblemParams", label);
            return jsonObject;
        }
        else {        	
            String label =  "previewProblem";
            jsonObject.element("activityType", label);
            jsonObject.element("previewProblemResults", previewProblemResults);
            return jsonObject;
        }

    }

}