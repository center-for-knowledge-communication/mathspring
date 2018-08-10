package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: March 2017
 * Time: blee
 * */
public class AdminSetTopicModelParametersEvent extends AdminEditTopicsEvent {

    private int maxNumProbsPerTopic;
    private int maxTimeInTopic;
    private int contentFailureThreshold;
    private double topicMastery;
    private int minNumProbsPerTopic;
    private int minTimeInTopic;
    private int difficultyRate;



    public AdminSetTopicModelParametersEvent(ServletParams p) throws Exception {
        super(p);
        maxNumProbsPerTopic = p.getInt("maxNumberProbsPerTopic");
        minNumProbsPerTopic = p.getInt("minNumberProbsPerTopic");
        maxTimeInTopic = p.getInt("maxTimeInTopic");
        minTimeInTopic = p.getInt("minTimeInTopic");
        contentFailureThreshold = p.getInt("contentFailureThreshold");
        topicMastery = p.getDouble("mastery");
        difficultyRate =p.getInt("difficultyRate");
    }

   

    public int getMaxNumProbsPerTopic() {
        return maxNumProbsPerTopic;
    }

    public int getMaxTimeInTopic() {
        return maxTimeInTopic;
    }

    public int getContentFailureThreshold() {
        return contentFailureThreshold;
    }

    public double getTopicMastery() {
        return topicMastery;
    }

    public int getMinNumProbsPerTopic() {
        return minNumProbsPerTopic;
    }

    public int getMinTimeInTopic() {
        return minTimeInTopic;
    }

    public int getDifficultyRate() {
        return difficultyRate;
    }

}