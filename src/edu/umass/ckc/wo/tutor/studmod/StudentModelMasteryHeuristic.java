package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.db.DbProblem;

import java.sql.SQLException;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 28, 2011
 * Time: 2:55:21 PM
 * Updated Sarah Schultz
 * Date: Sept 26, 2014
 * To change this template use File | Settings | File Templates.
 */
public class StudentModelMasteryHeuristic implements MasteryHeuristic {

    protected Connection conn;
    private int NOTR_TIME_THRESHOLD = 4000 ;       // Too fast time threshold. NOT ENOUGH TIME TO READ THE PROBLEM
   // private double threshold[] = {0.1, 0.25, 0.5, 0.75};

    public StudentModelMasteryHeuristic() {
        
    }

    public StudentModelMasteryHeuristic(Connection conn) throws SQLException {
        this.conn = conn;
    }

    @Override
    public double computeStandardMastery(long timeToFirstAttempt, double stdMastery, int probID,
                                         int numHelpAids,  boolean isCorrect, int numMistakes, int numProbsSeen, String mode) throws SQLException {
        double dd = DbProblem.getDiffLevel(conn,probID);

        double oldThreshold = getThreshold(stdMastery);

        if ( mode != null && ( mode.equals("PracticeProblem") ||
                mode.equals( "review") || mode.equals("practice") )){// && ! problemWasSkipped(timeToFirstAttempt, isCorrect, numMistakes) ) {
            if ( numProbsSeen == 1 ) {   //First problem with this standard
                if ( numMistakes == 0 && isCorrect && numHelpAids==0 )   //correct No Errors
                    stdMastery = 0.2 ;
    /*            else if ( isCorrect && numHelpAids > 0 )
                    topicMastery = 0.5 ;  */
                else
                    stdMastery = 0.1 ;
            }



            else if ( numMistakes == 0 && isCorrect  ) {    //Second problem onwards
                if ( numHelpAids > 0 )
                    stdMastery = updateCorrectWithHelp(stdMastery,dd, oldThreshold) ;
                else
                    stdMastery = updateCorrect(stdMastery,dd, oldThreshold);
            }
            else {
                if ( numHelpAids > 0 )
                    stdMastery = updateIncorrectWithHelp(timeToFirstAttempt,stdMastery,dd, oldThreshold);
                else
                    stdMastery = updateIncorrect(timeToFirstAttempt,stdMastery,dd, oldThreshold);
            }
        }
        else {
            return stdMastery ;   //if not a problem, don't change anything
        }
        //threshold = updateThreshold(topicMastery, threshold);      //update the threshold
        return stdMastery;
    }

    /** Computes the mastery level for the given topic without causing any changes to the db.
     *
     * This is called by the BaseStudentModel.   Its also used by the report tool to get the history of topic masteries for a line
     * graph.
     *
     * Consider making an interface for this so that we can have multiple heuristics that can plug into student models
     * so that it is easy to try out different ideas without junking the old heuristic.   Could even have the heurstic
     * specified in the admin tool so that different classes use different ones.
     *
     * @param timeToFirstAttempt
     * @param probID
     * @param topicID
     * @param numHelpAids
     * @param isCorrect
     * @param numMistakes
     * @param numPracticeProbsInTopic
     * @return
     * @throws java.sql.SQLException
     */

    public double computeTopicMastery(long timeToFirstAttempt, double topicMastery,  int probID, int topicID,
                                      int numHelpAids, boolean isCorrect, int numMistakes, int numPracticeProbsInTopic, String mode) throws SQLException {
        double dd = DbProblem.getDiffLevel(conn,probID);

       double oldThreshold = getThreshold(topicMastery);


        if ( mode != null && ( mode.equals("PracticeProblem") || //mode.equals( "4Mality") ||
                mode.equals( "review") || mode.equals("practice") )){// && ! problemWasSkipped(timeToFirstAttempt, isCorrect, numMistakes) ) {
            if ( numPracticeProbsInTopic == 1 ) {   //First problem
                if ( numMistakes == 0 && isCorrect && numHelpAids==0 )   //correct No Errors
                    topicMastery = 0.2 ;
    /*            else if ( isCorrect && numHelpAids > 0 )
                    topicMastery = 0.5 ;  */
                else
                    topicMastery = 0.1 ;
            }
       /*    else if ( numPracticeProbsInTopic == 2 ) {
                if ( numMistakes == 0 && isCorrect && numHelpAids==0 ) {   //correct No Errors )
                    if ( topicMastery >= 0.2 ){  //Previous problem was solved correctly
                        topicMastery = 0. ;    //First and second correct
                    }
                    else{
                        topicMastery = 0.35 ;    //First inccorrect, second easier one was correct
                    }
                }
                else {   //second Problem Incorrect

                    if ( topicMastery >= 0.5 ){  //First problem correct, second harder incorrect
                        topicMastery = 0.50 ;  //Not clear, first one was lucky guess? Second disengaged?
                    }
                    else
                        topicMastery = 0.15 ; //First incorrect, second incorrect.
                }
            }          */


            else if ( numMistakes == 0 && isCorrect  ) {    //Second problem onwards
                if ( numHelpAids > 0 )
                    topicMastery = updateCorrectWithHelp(topicMastery,dd, oldThreshold) ;
                else
                    topicMastery = updateCorrect(topicMastery,dd, oldThreshold);
            }
            else {
                if ( numHelpAids > 0 )
                    topicMastery = updateIncorrectWithHelp(timeToFirstAttempt,topicMastery,dd, oldThreshold);
                else
                    topicMastery = updateIncorrect(timeToFirstAttempt,topicMastery,dd, oldThreshold);
            }
        }
        else {
            return topicMastery ;   //if not a problem, don't change anything
        }
        //threshold = updateThreshold(topicMastery, threshold);      //update the threshold
        return topicMastery;
    }

    private boolean problemWasSkipped(long timeToFirstAttempt, boolean isCorrect, int numMistakes) {

        if ( isCorrect )
            return false ;

        //THEY DID NOT GET IT RIGHT, FROM NOW ON

        if ( numMistakes > 0 && timeToFirstAttempt > 0 ) {  /// Definitely Possible.
            if ( timeToFirstAttempt < NOTR_TIME_THRESHOLD )  {
                return true ;  //Problem was skipped after Making a mistake. Student has given Up TOO FAST. Consider it SKIPPED.
            }
            else  {       //timetoFirstAttempt > NOTR_TIME_THRESHOLD
                return false ; //Mistakes were made, but there was enough time to consider that the student was thinking about the problem. DO CONSIDER IT AS NOT-SKIPPED.
            }
        }

        if ( numMistakes > 0 && timeToFirstAttempt == 0 )  /// not Possible. Error in the passed  time To First Attempot
            return false ;  //allow the mastery update to happen, No way to know if the problem was skipped.

        if ( numMistakes == 0 && timeToFirstAttempt > 0)    /// not Possible. This is because isCorrect should be true
            return false ;    // Should not be reachable. No way to know if the problem was skipped.

        if ( numMistakes == 0 && timeToFirstAttempt == 0)    /// not Possible. This is because isCorrect should be true when nummistakes = 0.
            return false ; //Should not be reachable. No way to know if the problem was skipped.

        return false ;
    }

    
    // The 4 methods below are the heuristic for updating topic mastery.   They are protected methods because there is a subclass of this
    // TopicMasterySimulator that is used in reports to generate a student's history of topic masteries by simulating events from the
    // event log.    By making it a subclass of this, we insure that it uses the same heuristic.

    protected double updateCorrect(double oldVal, double difflevel, double threshold) {
        //System.out.println("In StudentModelMasteryHeuristic --UPDATECORRECT: ")  ;


        // A lower number will make a marked shift upwards in mastery
        double pSlip = 0.3 ; //0.4 ; //0.5 - difflevel / 10;
        double pGuess = 0.3 ; //0.4 ; //0.5 - difflevel / 10 ;

        double newVal= ((1 - pSlip) * oldVal) / (((1 - pSlip) * oldVal) + (pGuess * (1 - oldVal)));

        if(newVal <= threshold){
            newVal = threshold;
        }

        return newVal;
    }

    protected double updateCorrectWithHelp(double oldVal, double difflevel, double threshold) {
        // A lower number will make a marked shift upwards in mastery
        double pSlip =  0.45 ; //0.5 - 2 * difflevel / 10;      //.4 for diff level 0.5
        double pGuess = 0.45 ; //0.5 - 2 * difflevel / 10;

        double newVal= ((1 - pSlip) * oldVal) / (((1 - pSlip) * oldVal) + (pGuess * (1 - oldVal)));
        if(newVal <= threshold){
            newVal = threshold;
        }
        return newVal;
    }

    protected double updateIncorrect(long timeToFirstAttempt, double oldVal, double difflevel, double threshold) {
        // A lower number will make a marked shift downwards in mastery
        double pSlip = 0.3 ; //0.5 - difflevel / 10;
        double pGuess = 0.3 ; //0.5 - difflevel / 10 ;

       // if ( timeToFirstAttempt > 6000 ) {
            double newVal= ((pSlip * oldVal) / ((pSlip * oldVal) + (1 - pGuess)*(1 - oldVal)));

     //   System.out.println("updated mastery is " + newVal);

        if(newVal <= threshold){
            newVal = threshold;
        }

     //   System.out.println("after thresholding " + newVal);

          /*  if ( newVal < 0.05 )
                newVal = 0.05 ;   */

            return newVal;
        //}
        //return oldVal ;
    }

    protected double updateIncorrectWithHelp(long timeToFirstAttempt, double oldVal, double difflevel, double threshold) {
        // A lower number will make a marked shift downwards in mastery
        double pSlip = 0.45 ; //0.5 - 2 * difflevel / 10;      //.4 for diff level 0.5
        double pGuess = 0.45 ; //0.5 - 2 * difflevel / 10;

        //if ( timeToFirstAttempt > 6000 ) {
            double newVal= ((pSlip * oldVal) / ((pSlip * oldVal) + (1 - pGuess)*(1 - oldVal)));

           /* if ( newVal < 0.05 )
                newVal = 0.05 ;    */
        if(newVal <= threshold){
            newVal = threshold;
        }

            return newVal;
       // }
       // return oldVal ;
    }

    //update the threshold when the value goes above a cut point
    protected double getThreshold(double value){
        double oldThreshold;
        if (value >= 0.75){
            oldThreshold = 0.75;
        }
        else if(value >= 0.5){
            oldThreshold = 0.5;
        }
        else if (value >= 0.25){
            oldThreshold = 0.25;
        }
        else if(value >=0.1){
            oldThreshold = 0.1;
        }
        else oldThreshold = 0.05; //if we haven't reached a milestone, stay at 0.05
//        System.out.println("threshold updated to " + oldThreshold);
        return oldThreshold;
    }
}

