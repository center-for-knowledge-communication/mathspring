package edu.umass.ckc.wo.lc;

/**
 * Created by david on 11/7/2016.
 */
public class StudentDataCache {
    Double lastTopicMasteryValue=null;
    Double curTopicMasteryValue=null;
    String curEffort;
    String effort3;
    String effort2;
    String effort1;
    Boolean videoExists;
    Boolean exampleExists;
    Boolean hintExists;
    Double curProbDifficulty;
    Double lastProbDifficulty;
    Integer lastProbTopic;
    Boolean lastProblemIsDemoOrExample;
    Boolean isFirstProblemInTopic;


    public StudentDataCache() {
    }
}
