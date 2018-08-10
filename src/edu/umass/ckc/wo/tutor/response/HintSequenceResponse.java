package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Hint;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Iterator;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class HintSequenceResponse extends Response {
    private List<Hint> hints;

    public HintSequenceResponse(List<Hint> hints) {
        this.hints = hints;
        buildJSON();
    }

    public String logEventName() {
           return "hintSequence";
    }

    public String getHintNamesCSV () {
        StringBuffer sb = new StringBuffer();
        Iterator<Hint> itr = hints.iterator();
        itr = hints.iterator();
        if (itr.hasNext()) {
            sb.append(itr.next().getLabel());
        }
        while (itr.hasNext())
            sb.append(","+itr.next().getLabel());
        return sb.toString();
    }

    public String getHintIdsCSV () {
        StringBuffer sb = new StringBuffer();
        Iterator<Hint> itr = hints.iterator();
        itr = hints.iterator();
        if (itr.hasNext()) {
            sb.append(itr.next().getId());
        }
        while (itr.hasNext())
            sb.append(","+itr.next().getId());
        return sb.toString();
    }

    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        for (Hint h : this.getHintSequence()) {
            jsonObject.accumulate("solution",h.getJSON(new JSONObject()));
        }
        return jsonObject;

    }

    private String getSolutionXML () {
        StringBuffer sb = new StringBuffer();
        if (this.hints != null) {
            for (Hint h: this.hints) {
                sb.append("<hint>" +h.getLabel()+ "</hint>");
            }
            return sb.toString();
        }
        return "";
    }

    public List<Hint> getHintSequence() {
        return hints;
    }

    protected String getActivityXML () {
        return String.format("<activity><solution>%s</solution></activity>",
                getSolutionXML());
    }


}