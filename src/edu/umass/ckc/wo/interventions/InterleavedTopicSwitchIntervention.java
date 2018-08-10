package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.tutor.pedModel.InterleavedTopic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/7/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterleavedTopicSwitchIntervention extends InputResponseIntervention implements NextProblemIntervention {
    protected List<InterleavedTopic.TopicPerformance> topicPerformanceList;



    public InterleavedTopicSwitchIntervention(List<InterleavedTopic.TopicPerformance> topicPerformanceList) {
        this.topicPerformanceList = topicPerformanceList;

    }



    public String getType () {
        return "TopicSwitch";
    }


    public String getDialogHTML () {
        String str = "<div><p>You just completed a review of some recent topics.  Here is how you did:<br>";
        str += "<table><tr><th>Topic</th><th>Problems Solved</th><th>Problems Given</th></tr>";
        for (InterleavedTopic.TopicPerformance perf: topicPerformanceList) {
            str += "<tr><td>" + perf.getTopicName() + "</td><td>" + perf.getNumCorrect() + "</td><td>" + perf.getNumShown() + "</td></tr>";
        }
        str += "</table>";
        str+="</p>You might want to use the Progress Page to return to a topic you didn't do well in.</div>";
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
