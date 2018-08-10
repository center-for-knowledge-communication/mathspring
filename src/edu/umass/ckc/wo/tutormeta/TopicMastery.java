package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.beans.Topic;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 16, 2011
 * Time: 3:30:16 PM
 * To change this template use File | Settings | File Templates.
 *
 * A piece of student model that measures a student's competency in a particular topic (as stored in the db table
 * problemgroup)
 */
public class TopicMastery {
    Topic topic;
    double mastery;
    boolean entered;
    int numProbs=0;
    int numPracticeProbs=0;

    public static final String FULLY_MASTERED = "Fully Mastered";
    public static final String PARTIALLY_MASTERED = "Partially Mastered";
    public static final String NOT_MASTERED = "Not Mastered.  Needs Review";

    public String toString () {
        return this.topic.getId() + ":" + this.mastery;
    }

    public TopicMastery(Topic t, double mastery, boolean entered) {
        this.topic = t;
        this.mastery = mastery;
        this.entered = entered;
    }

    public Topic getTopic() {
        return topic;
    }

    public double getMastery() {
        return mastery;
    }

    public String getMasteryInterpretation () {
        if (mastery > .85)
            return FULLY_MASTERED;
        else if (mastery > .75)
            return PARTIALLY_MASTERED;
        else return NOT_MASTERED;
    }

    public String toXML(boolean isCurrentTopic) {
        return "<topicMastery name=\"" + topic.getName() + "\" masteryLevel=\"" + mastery + "\" isCurTopic=\"" + isCurrentTopic + "\"/>";
    }

    public void setMastery(double m) {
        mastery = m;
    }

    public boolean isEntered() {
        return entered;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public void incrementNumProbs () {
        this.numProbs++;
    }

    public int getNumProbs () {
        return this.numProbs;
    }

    public void incrementNumPracticeProbs () {
        this.numPracticeProbs++;
    }

    public int getNumPracticeProbs () {
        return this.numPracticeProbs;
    }
}
