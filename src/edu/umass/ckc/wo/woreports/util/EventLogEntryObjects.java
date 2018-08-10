package edu.umass.ckc.wo.woreports.util;

import java.sql.Timestamp;

/**
 * Created by Neeraj on 2/4/2017.
 */
public class EventLogEntryObjects {

    /**
     *
     *  All parameters for eventlog table
     */
    private int id;
    private int studId;
    private int sessNum;
    private String action;
    private String userInput;
    private int  isCorrect;
    private int elapsedTime;
    private int probElapsed;
    private int problemId;
    private String hintStep;
    private int hintId;
    private String emotion;
    private String activityName;
    private int auxId;
    private String auxTable;
    private Timestamp time;
    private int curTopicId;
    private String testerNote;
    private Timestamp clickTime;

    public EventLogEntryObjects(int id, int studId, int sessNum, String action, String userInput, int isCorrect, int elapsedTime, int probElapsed, int problemId, String hintStep, int hintId, String emotion, String activityName, int auxId, String auxTable, Timestamp time, int curTopicId, String testerNote, Timestamp clickTime) {
        this.id = id;
        this.studId = studId;
        this.sessNum = sessNum;
        this.action = action;
        this.userInput = userInput;
        this.isCorrect = isCorrect;
        this.elapsedTime = elapsedTime;
        this.probElapsed = probElapsed;
        this.problemId = problemId;
        this.hintStep = hintStep;
        this.hintId = hintId;
        this.emotion = emotion;
        this.activityName = activityName;
        this.auxId = auxId;
        this.auxTable = auxTable;
        this.time = time;
        this.curTopicId = curTopicId;
        this.testerNote = testerNote;
        this.clickTime = clickTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public int getSessNum() {
        return sessNum;
    }

    public void setSessNum(int sessNum) {
        this.sessNum = sessNum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getProbElapsed() {
        return probElapsed;
    }

    public void setProbElapsed(int probElapsed) {
        this.probElapsed = probElapsed;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getHintStep() {
        return hintStep;
    }

    public void setHintStep(String hintStep) {
        this.hintStep = hintStep;
    }

    public int getHintId() {
        return hintId;
    }

    public void setHintId(int hintId) {
        this.hintId = hintId;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getAuxId() {
        return auxId;
    }

    public void setAuxId(int auxId) {
        this.auxId = auxId;
    }

    public String getAuxTable() {
        return auxTable;
    }

    public void setAuxTable(String auxTable) {
        this.auxTable = auxTable;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getCurTopicId() {
        return curTopicId;
    }

    public void setCurTopicId(int curTopicId) {
        this.curTopicId = curTopicId;
    }

    public String getTesterNote() {
        return testerNote;
    }

    public void setTesterNote(String testerNote) {
        this.testerNote = testerNote;
    }

    public Timestamp getClickTime() {
        return clickTime;
    }

    public void setClickTime(Timestamp clickTime) {
        this.clickTime = clickTime;
    }
}
