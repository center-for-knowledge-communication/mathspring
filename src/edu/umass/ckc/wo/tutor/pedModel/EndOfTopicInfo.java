package edu.umass.ckc.wo.tutor.pedModel;

import java.util.Locale;
import java.util.ResourceBundle;


public class EndOfTopicInfo {
    boolean maxProbsReached=false, maxTimeReached=false, topicMasteryReached=false, failToFindEasier=false, failToFindHarder=false, failToFindSame=false;

    public EndOfTopicInfo(boolean maxProbsReached, boolean maxTimeReached, boolean failToFindEasier, boolean failToFindHarder, boolean failToFindSame, boolean topicMasteryReached) {
        this.maxProbsReached = maxProbsReached;
        this.maxTimeReached = maxTimeReached;
        this.failToFindEasier = failToFindEasier;
        this.failToFindHarder = failToFindHarder;
        this.failToFindSame = failToFindSame;

    }

    public boolean isTopicDone () {
        return maxProbsReached || maxTimeReached || topicMasteryReached || failToFindSame || failToFindEasier || failToFindHarder;
    }

    public boolean isContentFailure () {
        return failToFindEasier || failToFindHarder || failToFindSame;
    }

    public String getExplanation (Locale loc) {
        if (!isTopicDone())
            return null;
    	ResourceBundle rb = null;

        System.out.println("End of Topic");

        StringBuilder sb = new StringBuilder();
        try {           	
    		rb = ResourceBundle.getBundle("MathSpring",loc);

    		if (maxTimeReached)
    			sb.append(rb.getString("spent_a_lot_of_time_in_topic") + "<br>");
    		if (maxProbsReached)
    			sb.append(rb.getString("seen_a_lot_of_problems_in_topic") + "<br>");
    		if (topicMasteryReached)
    			sb.append(rb.getString("mastered_topic") + "<br>");
    		if (failToFindEasier)
    			sb.append(rb.getString("cannot_find_right_problem_for_you") + "<br>");
    		if (failToFindHarder)
    			sb.append(rb.getString("cannot_find_problem_more_challenging") + "<br>");
    		if (failToFindSame)
    			sb.append(rb.getString("all_out_of_problems_in_topic") + "<br>");
    		sb.append(rb.getString("lets_try_something_else"));
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }

    boolean isMaxProbsReached() {
        return maxProbsReached;
    }

    void setMaxProbsReached(boolean maxProbsReached) {
        this.maxProbsReached = maxProbsReached;
    }

    boolean isMaxTimeReached() {
        return maxTimeReached;
    }

    void setMaxTimeReached(boolean maxTimeReached) {
        this.maxTimeReached = maxTimeReached;
    }

    public boolean isTopicMasteryReached() {
        return topicMasteryReached;
    }

    public void setTopicMasteryReached(boolean topicMasteryReached) {
        this.topicMasteryReached = topicMasteryReached;
    }

    boolean isFailToFindEasier() {
        return failToFindEasier;
    }

    void setFailToFindEasier(boolean failToFindEasier) {
        this.failToFindEasier = failToFindEasier;
    }

    boolean isFailToFindHarder() {
        return failToFindHarder;
    }

    void setFailToFindHarder(boolean failToFindHarder) {
        this.failToFindHarder = failToFindHarder;
    }

    boolean isFailToFindSame() {
        return failToFindSame;
    }

    void setFailToFindSame(boolean failToFindSame) {
        this.failToFindSame = failToFindSame;
    }
}

