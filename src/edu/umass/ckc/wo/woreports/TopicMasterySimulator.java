package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.wo.tutor.studmod.BaseStudentModel;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.tutor.studmod.MasteryHeuristic;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.db.DbTopics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * This is used by various reports to simulate using the eventlog and recreate the sequence of topic masteries for a particular student in a given topic.
 * <p/>
 * The problem with this is that it contains a clone of code that is in the BaseStudentModelOld,   so if that gets messed with, this needs to be changed so that it
 * is similar.
 * User: david
 * Date: Nov 11, 2011
 * Time: 5:49:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicMasterySimulator  {

    Connection conn;
    List<TopicMastery> masteries;
    double mastery = BaseStudentModel.INITIAL_TOPIC_MASTERY_ESTIMATE_FL;
    int topicId;
    TopicMastery curTopicMastery;
    MasteryHeuristic heuristic;


    public TopicMasterySimulator(Connection conn,  MasteryHeuristic heuristic) throws SQLException {
        this.conn = conn;
        this.heuristic= heuristic;
        masteries = new ArrayList<TopicMastery>();
        // set up the initial mastery levels for the student.
        for (Topic t : DbTopics.getAllTopics(conn)) {
            masteries.add(new TopicMastery(t, mastery, false));
        }
    }

    public TopicMasterySimulator(Connection conn,  MasteryHeuristic heuristic, double initialMastery) throws SQLException {
        this(conn,heuristic);
        mastery = initialMastery;
    }

    TopicMasterySimulator(Connection conn, int topicId) {
        this.conn = conn;
        this.topicId = topicId;
    }

    public void updateMastery(int probID, int numHints, int isCorrect, int mistakes, long timeToFirstAttempt, int numPracticeProbsThisTopic, String problemmode) throws SQLException {
        if (topicId == -1 )
            return;
//        double dd = DbProblem.getDiffLevel(conn, probID);
//        double topicMastery = mastery;
//        if (mistakes == 0 && isCorrect == 1) {
//            if (numHints > 0)
//                topicMastery = updateCorrectWithHelp(topicMastery, dd);
//            else
//                topicMastery = updateCorrect(topicMastery, dd);
//        } else {
//            if (numHints > 0)
//                topicMastery = updateIncorrectWithHelp(timeToFirstAttempt, topicMastery, dd);
//            else
//                topicMastery = updateIncorrect(timeToFirstAttempt, topicMastery, dd);
//        }
        this.mastery = this.heuristic.computeTopicMastery(timeToFirstAttempt,mastery,
                probID,topicId,numHints,isCorrect==1,mistakes,numPracticeProbsThisTopic , problemmode);
        this.saveMastery(topicId, mastery);
    }

    double getMastery() {
        return this.mastery;
    }

    private void saveMastery(int topicId, double mastery) {
        for (TopicMastery m : this.masteries)
            if (m.getTopic().getId() == topicId)
                m.setMastery(mastery);
    }


    public void newTopic(int topicId) {
        this.topicId = topicId;
        // when the topic switches, set the mastery instance variable to be the mastery value for the topic.
        for (TopicMastery topicMastery : this.masteries) {
            if (topicMastery.getTopic().getId() == topicId) {
                this.curTopicMastery = topicMastery;
                this.mastery = topicMastery.getMastery();
            }
        }
    }

    public void newTopic(int topicId, double intialMastery) {
        this.topicId = topicId;

        // when the topic switches, set the mastery instance variable to be the mastery value for the topic.
        for (TopicMastery topicMastery : this.masteries) {
            if (topicMastery.getTopic().getId() == topicId) {
                topicMastery.setMastery(intialMastery) ;
                this.curTopicMastery = topicMastery;
                this.mastery = topicMastery.getMastery();
            }
        }
    }

    public int getTopicNumProbs () {
        return this.curTopicMastery.getNumProbs();
    }

    public void incrementNumProblems() {
        this.curTopicMastery.incrementNumProbs();
    }


    public int getTopicNumPracticeProbs () {
        return this.curTopicMastery.getNumPracticeProbs();
    }

    public void incrementNumPracticeProblems() {
        this.curTopicMastery.incrementNumPracticeProbs();
    }
}