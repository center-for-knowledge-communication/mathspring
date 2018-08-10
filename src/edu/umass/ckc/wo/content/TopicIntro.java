package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.interventions.InputResponseIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 21, 2009
 * Time: 10:59:21 AM
 */
public class TopicIntro extends InputResponseIntervention implements NextProblemIntervention {

    public static String TOPIC_INTRO_TYPE = "TopicIntro";
    private String resource;   // something like problem_935 (.swf is given by type )
    private String topicName;
    private int topicId;
    private String type;    // either swf or html

    private String interventionSelector;  // Here because the topic intro has no HTML form to put the destination in .  So we pass it to javascript so it can pass it back



    public TopicIntro(String resource, String type, String topic, int topicId) {
        this.resource = resource;
        this.type = type;
        this.topicName = topic;
        this.topicId=topicId;

    }

    @Override
    public String getType() {
        return type;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDialogHTML() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getId() {
        return 999;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return this.resource;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getInterventionSelector() {
        return interventionSelector;
    }

    public void setInterventionSelector(String interventionSelector) {
        this.interventionSelector = interventionSelector;
    }

    public JSONObject buildJSON(JSONObject jo) {
        super.buildJSON(jo);

        jo.element("activityType", Intervention.INTERVENTION);
        jo.element("isInputIntervention",this.isInputIntervention);
        jo.element("interventionType",TOPIC_INTRO_TYPE);
        jo.element("resource",this.getResource());
        jo.element("resourceType",this.type);
        jo.element("topicId",this.topicId);
        jo.element("topicName",this.topicName);
        jo.element("destinationInterventionSelector",this.interventionSelector);
        return jo;
    }

    @Override
    public String logEventName() {
        return "TopicIntro";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
