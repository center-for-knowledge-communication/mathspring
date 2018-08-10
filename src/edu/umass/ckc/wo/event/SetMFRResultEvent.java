package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ServletParams;


/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 3:54:03 PM
 * 
 */
public class SetMFRResultEvent extends SessionEvent {

    public static final String PROB_NAME = "probName";
    public static final String STUDENT_ANSWER = "studentAnswer";
    public static final String CORRECT_ANSWER = "correctAnswer";
    public static final String TIMESTAMP = "timestamp";
    public static final String PROB_ELAPSED = "probElapsed";
    public static final String SEQNUM = "seqNum";

    private String probName;
    private String studentAnswer;
    private String correctAnswer;
    private String timestamp;
    private String probElapsed;
    private String seqNum;

    public SetMFRResultEvent (ServletParams p) throws Exception {
        super(p);
        this.setProbName(p.getString(PROB_NAME));
        this.setStudentAnswer(p.getString(STUDENT_ANSWER));
        this.setCorrectAnswer(p.getString(CORRECT_ANSWER));
        this.setTimestamp(p.getString(TIMESTAMP));
        this.setProbElapsed(p.getString(PROB_ELAPSED));
        this.setSeqNum(p.getString(SEQNUM));
    }

    public String getProbName() {
        return probName;
    }

    public void setProbName(String probName) {
        this.probName = probName;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProbElapsed() {
        return probElapsed;
    }

    public void setProbElapsed(String probElapsed) {
        this.probElapsed = probElapsed;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }
}
