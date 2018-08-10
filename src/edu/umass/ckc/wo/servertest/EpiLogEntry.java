package edu.umass.ckc.wo.servertest;

/**     Represents a row from the EpisodicData table
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Mar 22, 2006
 * Time: 3:44:56 PM
 */
public class EpiLogEntry {

    private String action;
    private boolean isCorrect;
    private String userInput;
    private long elapsedTime;
    private long probElapsedTime;
    private String activityName;

    // not sure if I should use these three for sending events to the server or if I should have the driver
    // use the server outputs as inputs for subsequent calls.  The latter would be a more realistic simulation and
    // might catch some error.
    private int problemId;
    private int hintId;
    private String hintStep;

    public String toString () {
        return action + " " + userInput + " " + activityName + " " +  isCorrect + " " +  elapsedTime + " " + probElapsedTime + " " + problemId;

    }

    public EpiLogEntry(String action, boolean correct, String userInput, long elapsedTime, long probElapsedTime, String activityName, int problemId, int hintId, String hintStep) {
        this.action = action;
        isCorrect = correct;
        this.userInput = userInput;
        if (action.equals("beginProblem") || action.equals("endProblem"))
            this.userInput = "nextProblem";
        this.elapsedTime = elapsedTime;
        this.probElapsedTime = probElapsedTime;
        this.activityName = activityName;
        this.problemId = problemId;
        this.hintId = hintId;
        this.hintStep = hintStep;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getProbElapsedTime() {
        return probElapsedTime;
    }

    public void setProbElapsedTime(long probElapsedTime) {
        this.probElapsedTime = probElapsedTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getHintId() {
        return hintId;
    }

    public void setHintId(int hintId) {
        this.hintId = hintId;
    }

    public String getHintStep() {
        return hintStep;
    }

    public void setHintStep(String hintStep) {
        this.hintStep = hintStep;
    }

    public boolean isStudentActionEvent () {
        return !this.getAction().equals("endProblem");
    }


    public boolean isEndActivityEvent () {
        return this.getAction().equals("endProblem");
    }
}
