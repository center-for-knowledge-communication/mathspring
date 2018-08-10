package edu.umass.ckc.wo.woreports.util;

import java.sql.Timestamp;

/**
 * Created by Neeraj on 2/8/2017.
 */
public class StudentProblemHistoryObjects {

    /**
     * All parameters for StudentProblemHistory table
     */
    private int id;
    private int studId;
    private int sessionId;
    private int problemId;
    private int topicId;
    private Timestamp problemBeginTime;
    private Timestamp problemEndTime;
    private int timeInSession;
    private int timeInTutor;
    private int timeToFirstAttempt;
    private int timeToFirstHint;
    private int timeToSolve;
    private int numMistakes;
    private int numHints;
    private int videoSeen;
    private int numAttemptsToSolve;
    private int solutionHintGiven;
    private String mode;
    private float mastery;
    private String emotionAfter;
    private int emotionLevel;
    private String effort;
    private int exampleSeen;
    private int textReaderUsed;
    private int numHintsBeforeSolve;
    private int isSolved;
    private String adminFlag;
    private String authorFlag;
    private int collaboratedWith;
    private int timeToSecondAttempt;
    private int timeToThirdAttempt;
    private int timeToSecondHint;
    private int timeToThirdHint;
    private double probDiff;

    public StudentProblemHistoryObjects(int id, int studId, int sessionId, int problemId, int topicId, Timestamp problemBeginTime, Timestamp problemEndTime, int timeInSession, int timeInTutor, int timeToFirstAttempt, int timeToFirstHint, int timeToSolve, int numMistakes, int numHints, int videoSeen, int numAttemptsToSolve, int solutionHintGiven, String mode, float mastery, String emotionAfter, int emotionLevel, String effort, int exampleSeen, int textReaderUsed, int numHintsBeforeSolve, int isSolved, String adminFlag, String authorFlag, int collaboratedWith, int timeToSecondAttempt, int timeToThirdAttempt, int timeToSecondHint, int timeToThirdHint, double probDiff) {
        this.id = id;
        this.studId = studId;
        this.sessionId = sessionId;
        this.problemId = problemId;
        this.topicId = topicId;
        this.problemBeginTime = problemBeginTime;
        this.problemEndTime = problemEndTime;
        this.timeInSession = timeInSession;
        this.timeInTutor = timeInTutor;
        this.timeToFirstAttempt = timeToFirstAttempt;
        this.timeToFirstHint = timeToFirstHint;
        this.timeToSolve = timeToSolve;
        this.numMistakes = numMistakes;
        this.numHints = numHints;
        this.videoSeen = videoSeen;
        this.numAttemptsToSolve = numAttemptsToSolve;
        this.solutionHintGiven = solutionHintGiven;
        this.mode = mode;
        this.mastery = mastery;
        this.emotionAfter = emotionAfter;
        this.emotionLevel = emotionLevel;
        this.effort = effort;
        this.exampleSeen = exampleSeen;
        this.textReaderUsed = textReaderUsed;
        this.numHintsBeforeSolve = numHintsBeforeSolve;
        this.isSolved = isSolved;
        this.adminFlag = adminFlag;
        this.authorFlag = authorFlag;
        this.collaboratedWith = collaboratedWith;
        this.timeToSecondAttempt = timeToSecondAttempt;
        this.timeToThirdAttempt = timeToThirdAttempt;
        this.timeToSecondHint = timeToSecondHint;
        this.timeToThirdHint = timeToThirdHint;
        this.probDiff = probDiff;
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

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Timestamp getProblemBeginTime() {
        return problemBeginTime;
    }

    public void setProblemBeginTime(Timestamp problemBeginTime) {
        this.problemBeginTime = problemBeginTime;
    }

    public Timestamp getProblemEndTime() {
        return problemEndTime;
    }

    public void setProblemEndTime(Timestamp problemEndTime) {
        this.problemEndTime = problemEndTime;
    }

    public int getTimeInSession() {
        return timeInSession;
    }

    public void setTimeInSession(int timeInSession) {
        this.timeInSession = timeInSession;
    }

    public int getTimeInTutor() {
        return timeInTutor;
    }

    public void setTimeInTutor(int timeInTutor) {
        this.timeInTutor = timeInTutor;
    }

    public int getTimeToFirstAttempt() {
        return timeToFirstAttempt;
    }

    public void setTimeToFirstAttempt(int timeToFirstAttempt) {
        this.timeToFirstAttempt = timeToFirstAttempt;
    }

    public int getTimeToFirstHint() {
        return timeToFirstHint;
    }

    public void setTimeToFirstHint(int timeToFirstHint) {
        this.timeToFirstHint = timeToFirstHint;
    }

    public int getTimeToSolve() {
        return timeToSolve;
    }

    public void setTimeToSolve(int timeToSolve) {
        this.timeToSolve = timeToSolve;
    }

    public int getNumMistakes() {
        return numMistakes;
    }

    public void setNumMistakes(int numMistakes) {
        this.numMistakes = numMistakes;
    }

    public int getNumHints() {
        return numHints;
    }

    public void setNumHints(int numHints) {
        this.numHints = numHints;
    }

    public int getVideoSeen() {
        return videoSeen;
    }

    public void setVideoSeen(int videoSeen) {
        this.videoSeen = videoSeen;
    }

    public int getNumAttemptsToSolve() {
        return numAttemptsToSolve;
    }

    public void setNumAttemptsToSolve(int numAttemptsToSolve) {
        this.numAttemptsToSolve = numAttemptsToSolve;
    }

    public int getSolutionHintGiven() {
        return solutionHintGiven;
    }

    public void setSolutionHintGiven(int solutionHintGiven) {
        this.solutionHintGiven = solutionHintGiven;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public float getMastery() {
        return mastery;
    }

    public void setMastery(float mastery) {
        this.mastery = mastery;
    }

    public String getEmotionAfter() {
        return emotionAfter;
    }

    public void setEmotionAfter(String emotionAfter) {
        this.emotionAfter = emotionAfter;
    }

    public int getEmotionLevel() {
        return emotionLevel;
    }

    public void setEmotionLevel(int emotionLevel) {
        this.emotionLevel = emotionLevel;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public int getExampleSeen() {
        return exampleSeen;
    }

    public void setExampleSeen(int exampleSeen) {
        this.exampleSeen = exampleSeen;
    }

    public int getTextReaderUsed() {
        return textReaderUsed;
    }

    public void setTextReaderUsed(int textReaderUsed) {
        this.textReaderUsed = textReaderUsed;
    }

    public int getNumHintsBeforeSolve() {
        return numHintsBeforeSolve;
    }

    public void setNumHintsBeforeSolve(int numHintsBeforeSolve) {
        this.numHintsBeforeSolve = numHintsBeforeSolve;
    }

    public int getIsSolved() {
        return isSolved;
    }

    public void setIsSolved(int isSolved) {
        this.isSolved = isSolved;
    }

    public String getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        this.adminFlag = adminFlag;
    }

    public String getAuthorFlag() {
        return authorFlag;
    }

    public void setAuthorFlag(String authorFlag) {
        this.authorFlag = authorFlag;
    }

    public int getCollaboratedWith() {
        return collaboratedWith;
    }

    public void setCollaboratedWith(int collaboratedWith) {
        this.collaboratedWith = collaboratedWith;
    }

    public int getTimeToSecondAttempt() {
        return timeToSecondAttempt;
    }

    public void setTimeToSecondAttempt(int timeToSecondAttempt) {
        this.timeToSecondAttempt = timeToSecondAttempt;
    }

    public int getTimeToThirdAttempt() {
        return timeToThirdAttempt;
    }

    public void setTimeToThirdAttempt(int timeToThirdAttempt) {
        this.timeToThirdAttempt = timeToThirdAttempt;
    }

    public int getTimeToSecondHint() {
        return timeToSecondHint;
    }

    public void setTimeToSecondHint(int timeToSecondHint) {
        this.timeToSecondHint = timeToSecondHint;
    }

    public int getTimeToThirdHint() {
        return timeToThirdHint;
    }

    public void setTimeToThirdHint(int timeToThirdHint) {
        this.timeToThirdHint = timeToThirdHint;
    }

    public double getProbDiff() {
        return probDiff;
    }

    public void setProbDiff(double probDiff) {
        this.probDiff = probDiff;
    }
}
