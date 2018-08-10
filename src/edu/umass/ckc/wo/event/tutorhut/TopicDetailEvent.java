package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 8/16/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicDetailEvent extends TutorHutEvent {
    double mastery;
    int topicId;

    String topicName;
    int problemsDone;
    int totalProblems;

    public TopicDetailEvent(ServletParams p) throws Exception {
        super(p);
        String masteryStr = p.getString("mastery");
        mastery= Double.parseDouble(masteryStr);
        topicId = p.getInt("topicId");
        sessionId = p.getInt("sessionId");

        topicName= p.getString("topicName");
        problemsDone = p.getInt("problemsDone");
        totalProblems = p.getInt("totalProblems");
        String et = p.getString(ELAPSED_TIME,"0");
        long etl=0;
        try {
            etl = Long.parseLong(et);
        } catch (Exception e) {
            etl=0;
        }
        elapsedTime = etl;
    }

    public double getMastery() {
        return mastery;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }
    public int getProblemsDone() {
        return problemsDone;
    }

    public int getTotalProblems() {
        return totalProblems;
    }

}
