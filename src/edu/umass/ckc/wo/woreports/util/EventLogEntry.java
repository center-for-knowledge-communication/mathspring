package edu.umass.ckc.wo.woreports.util;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 11/15/12
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventLogEntry {
    public int id;
    public int sessNum;
    public String action;
    public boolean isCorrect;
    public long elapsedTime;
    public long probElapsed;
    public int problemId;
    public int hintId;
    public String activityName;
    public int curTopicId;

    public EventLogEntry(int id, int sessNum, String action, boolean correct, long elapsedTime, long probElapsed, int problemId, int hintId, String activityName, int curTopicId) {
        this.id = id;
        this.sessNum = sessNum;
        this.action = action;
        isCorrect = correct;
        this.elapsedTime = elapsedTime;
        this.probElapsed = probElapsed;
        this.problemId = problemId;
        this.hintId = hintId;
        this.activityName = activityName;
        this.curTopicId = curTopicId;
    }

    public String toString () {
        return String.format("%d: %s %s, %d",id,action,activityName, sessNum);
    }
}
