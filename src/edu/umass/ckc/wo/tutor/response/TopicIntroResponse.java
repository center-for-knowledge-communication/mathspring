package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class TopicIntroResponse extends ProblemResponse implements NextProblemIntervention {


    public TopicIntroResponse(TopicIntro intro, List<TopicMastery> topicMasteryLevels, int curTopic) {
//        super(intro,topicMasteryLevels,curTopic);
    }

    public TopicIntroResponse(TopicIntro intro) {
//        super(intro );
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
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject buildJSON(JSONObject jo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}