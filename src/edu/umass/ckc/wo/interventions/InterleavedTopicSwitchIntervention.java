package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.tutor.pedModel.InterleavedTopic;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/7/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterleavedTopicSwitchIntervention extends InputResponseIntervention implements NextProblemIntervention {
    protected List<InterleavedTopic.TopicPerformance> topicPerformanceList;

    private Locale locale;


    public InterleavedTopicSwitchIntervention(List<InterleavedTopic.TopicPerformance> topicPerformanceList, Locale loc) {
        this.topicPerformanceList = topicPerformanceList;
        this.locale = loc;
        
    }



    public String getType () {
        return "TopicSwitch";
    }


    public String getDialogHTML () {
    	
    	System.out.println("InterleavedTopicSwitch locale = " + this.locale.toString());
    	
    	ResourceBundle rb = null;
    	
    	String str = "";
        try {           	
        		// Multi=lingual enhancement
       		rb = ResourceBundle.getBundle("MathSpring",this.locale);

       		str = "<div><p>" + rb.getString("just_completed_review_of_topics") + "  " + rb.getString("here_is_how_you_did") + ":<br>";
       		str += "<table><tr><th>" + rb.getString("topic") + "</th><th>" + rb.getString("problems_solved") + "</th><th>" + rb.getString("problems_given") + "/th></tr>";
       		for (InterleavedTopic.TopicPerformance perf: topicPerformanceList) {
       			str += "<tr><td>" + perf.getTopicName() + "</td><td>" + perf.getNumCorrect() + "</td><td>" + perf.getNumShown() + "</td></tr>";
       		}
       		str += "</table>";
       		str+="</p>" + rb.getString("use_progress_page_to_return_to_topic") + "</div>";
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
