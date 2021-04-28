package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.interventions.AttemptIntervention;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 12:24:50 PM
 */
public class AttemptResponse extends Response {
    private boolean isCorrect;
    private Intervention intervention;
    private String logEventName;


    public AttemptResponse(boolean isCorrect, List<TopicMastery> topicMasteryLevels, int curTopic) {
        this.isCorrect=isCorrect;
        logEventName = "attempt";
        this.topicMasteryLevels = topicMasteryLevels;
        this.curTopic = curTopic;
        buildJSON();
    }

    public AttemptResponse(boolean correct, String logEventName) {
        isCorrect = correct;
        this.logEventName = logEventName;
        buildJSON();
    }

    public AttemptResponse(boolean isCorrect, Intervention i, List<TopicMastery> topicMasteryLevels, int curTopic) {
        this.isCorrect=isCorrect;
        this.intervention=i;
        this.topicMasteryLevels = topicMasteryLevels;
        this.curTopic = curTopic;
        buildJSON();


    }


    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        jsonObject.element("isCorrect", isCorrect);
        if (intervention != null) {
            // The AttemptIntervention must return whether the problem should be graded or not.
            // note that Flash problems grade themselves, so this will only work for HTML problems.
            jsonObject.element("showGrade", ((AttemptIntervention) intervention).isShowGrade());
            jsonObject.element("intervention",intervention.buildJSON(new JSONObject()));
        }

        return jsonObject;
    }



//    public String getFlashOut () {
//
//        return buildJSON();
////        StringBuffer result = new StringBuffer(64);
////
////        if (intervention != null)
////            result.append(intervention.getFlashOut());
////        result.append(getEmotionCharacterControl());
////        if (!extraOutputParams.equals(""))
////            result.append("&").append(extraOutputParams).append("\n");
////        if (Settings.useHybridTutor)
////            result.append("&grade=<grade isCorrect=\"" + isCorrect + "\"/>");
////        else result.append("&grade=" + doGrade + "\n");
////        result.append("&isCorrect=" + isCorrect + "\n");
////        result.append("&studentStatus=" + getStudentStatus() + "\n");
////        return result.toString();
//    }

     public String logEventName() {
         return logEventName;
     }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
