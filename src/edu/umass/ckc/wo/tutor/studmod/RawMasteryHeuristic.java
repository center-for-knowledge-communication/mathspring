package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.db.DbProblem;

import java.sql.SQLException;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 28, 2011
 * Time: 2:55:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class RawMasteryHeuristic implements MasteryHeuristic {

    protected Connection conn;

    public RawMasteryHeuristic() {

    }

    public RawMasteryHeuristic(Connection conn) throws SQLException {
        this.conn = conn;
    }

    /** Computes the mastery level for the given topic without causing any changes to the db.
     *
     * This is called by the BaseStudentModelOld and CelebrateTopicMasteryIS (which needs to predict what the topic mastery will be when
     * the student makes an attempt).   Its also used by the report tool to get the history of topic masteries for a line
     * graph.
     *
     * Consider making an interface for this so that we can have multiple heuristics that can plug into student models
     * so that it is easy to try out different ideas without junking the old heuristic.   Could even have the heurstic
     * specified in the admin tool so that different classes use different ones.
     *
     * @param probID
     * @param topicID
     * @param numHints
     * @param isCorrect
     * @param numMistakes
     * @param numProbsSeen
     * @return
     * @throws java.sql.SQLException
     */
    public double computeTopicMastery(long timeToFirstAttempt, double topicMastery, int probID, int topicID,
                                      int numHints, boolean isCorrect, int numMistakes, int numProbsSeen, String mode) throws SQLException {
        double dd = DbProblem.getDiffLevel(conn,probID);

        double incr=0.0;
        if ( numMistakes == 0 && isCorrect  ) {
            incr=1.0;     // 1
        }
        else if (numMistakes == 1 && isCorrect) {
            incr=.8;   // .8
        }
        else if (numMistakes == 2 && isCorrect) {
            incr=.3;   // .3
        }
        else if (numMistakes > 2)
            incr=0; // 0
        topicMastery = (topicMastery * (numProbsSeen-1) + incr) / (numProbsSeen);
        return topicMastery;
    }

    @Override
    /**
     * Compute a mastery level for a given standard.  numProbsSeen is the number of problems that the student has seen that contains this standard
     */
    public double computeStandardMastery(long timeToFirstAttempt, double stdMastery, int probID,
                                        int numHelpAids, boolean isCorrect, int numMistakes, int numProbsSeen, String problemMode) throws SQLException {
        double dd = DbProblem.getDiffLevel(conn,probID);

        double incr=0.0;
        if ( numMistakes == 0 && isCorrect  ) {
            incr=1.0;     // 1
        }
        else if (numMistakes == 1 && isCorrect) {
            incr=.8;   // .8
        }
        else if (numMistakes == 2 && isCorrect) {
            incr=.3;   // .3
        }
        else if (numMistakes > 2)
            incr=0; // 0
        stdMastery = (stdMastery * (numProbsSeen-1) + incr) / (numProbsSeen);
        return stdMastery;
    }

    // The 4 methods below are the heuristic for updating topic mastery.   They are protected methods because there is a subclass of this
    // TopicMasterySimulator that is used in reports to generate a student's history of topic masteries by simulating events from the
    // event log.    By making it a subclass of this, we insure that it uses the same heuristic.

    protected double updateCorrect(double oldVal, double difflevel) {
        double pSlip = 0.4 ; //0.5 - difflevel / 10;
        double pGuess = 0.4 ; //0.5 - difflevel / 10 ;

        double newVal= ((1 - pSlip) * oldVal) / (((1 - pSlip) * oldVal) + (pGuess * (1 - oldVal)));
        return newVal;
    }

    protected double updateCorrectWithHelp(double oldVal, double difflevel) {
        double pSlip = 0.45 ; //0.5 - 2 * difflevel / 10;      //.4 for diff level 0.5
        double pGuess = 0.45 ; //0.5 - 2 * difflevel / 10;

        double newVal= ((1 - pSlip) * oldVal) / (((1 - pSlip) * oldVal) + (pGuess * (1 - oldVal)));
        return newVal;
    }

    protected double updateIncorrect(long timeToFirstAttempt, double oldVal, double difflevel) {
        double pSlip = 0.4 ; //0.5 - difflevel / 10;
        double pGuess = 0.4 ; //0.5 - difflevel / 10 ;

        if ( timeToFirstAttempt > 6000 ) {
            double newVal= ((pSlip * oldVal) / ((pSlip * oldVal) + (1 - pGuess)*(1 - oldVal)));

            if ( newVal < 0.05 )
                newVal = 0.05 ;

            return newVal;
        }
        return oldVal ;
    }

    protected double updateIncorrectWithHelp(long timeToFirstAttempt, double oldVal, double difflevel) {
        double pSlip = 0.45 ; //0.5 - 2 * difflevel / 10;      //.4 for diff level 0.5
        double pGuess = 0.45 ; //0.5 - 2 * difflevel / 10;

        if ( timeToFirstAttempt > 6000 ) {
            double newVal= ((pSlip * oldVal) / ((pSlip * oldVal) + (1 - pGuess)*(1 - oldVal)));

            if ( newVal < 0.05 )
                newVal = 0.05 ;

            return newVal;
        }
        return oldVal ;
    }
}