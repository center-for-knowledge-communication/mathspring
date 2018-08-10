package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 4:17:32 PM
 * 
 */
public class SetMRResultEvent extends SessionEvent {
    public static final String IMAGE_A = "imageA";
    public static final String IMAGE_B = "imageB";
    public static final String STUD_ANSWER = "studAnswer";
    public static final String CORRECT_ANSWER = "correctAnswer";
    public static final String TIMESTAMP = "timestamp";
    public static final String PROB_ELAPSED = "probElapsed";
    public static final String SEQNUM = "seqNum";

    private String imageA;
    private String imageB;
    private String studAnswer;
    private String correctAnswer;
    private String timestamp;
    private String probElapsed;
    private String seqNum;

    public SetMRResultEvent(ServletParams p) throws Exception {
        super(p);
        this.setImageA(p.getString(IMAGE_A));
        this.setImageB(p.getString(IMAGE_B));
        this.setStudAnswer(p.getString(STUD_ANSWER));
        this.setCorrectAnswer(p.getString(CORRECT_ANSWER));
        this.setTimestamp(p.getString(TIMESTAMP));
        this.setProbElapsed(p.getString(PROB_ELAPSED));
        this.setSeqNum(p.getString(SEQNUM));
    }

    public String getImageA() {
        return imageA;
    }

    public void setImageA(String imageA) {
        this.imageA = imageA;
    }

    public String getImageB() {
        return imageB;
    }

    public void setImageB(String imageB) {
        this.imageB = imageB;
    }

    public String getStudAnswer() {
        return studAnswer;
    }

    public void setStudAnswer(String studAnswer) {
        this.studAnswer = studAnswer;
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
