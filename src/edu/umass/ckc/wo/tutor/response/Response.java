package edu.umass.ckc.wo.tutor.response;


import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.tutormeta.StudentEffort;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.servlet.servbase.View;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 *
 * @author
 * @version 1.0
 */

public class Response  {

    protected String extraOutputParams = "";
    protected List<TopicMastery> topicMasteryLevels;
    protected int curTopic;
    protected JSONObject jsonObject;
    protected String characterControl ;
    protected StudentEffort effort;




    public Response() {
        jsonObject = new JSONObject();
    }


    public Response(String s) {
        extraOutputParams =s;
    }

    public JSONObject getJSON () {
        if (jsonObject == null)
            return new JSONObject();
        else return this.jsonObject;
    }


    public void addExtra (String extra) {
        if (!extraOutputParams.equals(""))
            this.extraOutputParams += "&"+extra;
        else  this.extraOutputParams=extra;

    }



    /**
     *
     * @return XML that contains topic mastery levels for a student (only given on attempt and nextProblem)
     */
    public String getStudentStatus () {
        StringBuilder sb = new StringBuilder("<studentStatus>\n");
        if ( topicMasteryLevels != null)
            for (TopicMastery ml: topicMasteryLevels) {
                sb.append(ml.toXML(ml.getTopic().getId() == curTopic));
            }
        sb.append("</studentStatus>");
        return sb.toString();
    }


    public String logEventName() {
        return "";
    }

    public void setCharacterControl (String clip) {
        this.characterControl = clip;

    }

    public String getCharacterControl () {
        return this.characterControl;
    }



    public JSONObject buildJSON() {
       return jsonObject;
    }


    public View getView () {

        return new View() {
            public String getView() {
                return buildJSON().toString();
            }
        };
    }


    public void setEffort(StudentEffort effort) {
        this.effort = effort;
        if (jsonObject != null) {
            jsonObject.element("effort",this.effort.getEffortDescription());
        }
    }
}