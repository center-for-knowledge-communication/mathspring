package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 4:18:03 PM
 * 
 */
public class SetPrePostResultEvent extends SessionEvent {

    public static final String PROB_NAME = "probName";
    public static final String TEST_TYPE = "testType";
    public static final String STUD_ANSWER = "studAnswer";
    public static final String IS_CORRECT = "isCorrect";
    public static final String SKIPPED = "skipped";
    public static final String TIMESTAMP = "timestamp";
    public static final String PROB_ELAPSED = "probElapsed";
    public static final String SEQNUM = "seqNum";

    private String probName;
    private String testType;
    private String studAnswer;
    private String isCorrect;
    private String skipped;
    private String timestamp;
    private String probElapsed;
    private String seqNum;

    public SetPrePostResultEvent(ServletParams p) throws Exception {
        super(p);
        this.setProbName(p.getString(PROB_NAME));
        this.setTestType(p.getString(TEST_TYPE));
        this.setStudAnswer(p.getString(STUD_ANSWER));
        this.setCorrect(p.getString(IS_CORRECT));
        this.setSkipped(p.getString(SKIPPED));
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getStudAnswer() {
        return studAnswer;
    }

    public void setStudAnswer(String studAnswer) {
        this.studAnswer = studAnswer;
    }

    public String getCorrect() {
        return isCorrect;
    }

    public void setCorrect(String correct) {
        isCorrect = correct;
    }

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
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
