package edu.umass.ckc.wo.tutormeta;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/19/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PedagogyParams {

    private int studId;
    private boolean showIntro;
    private long maxTime;
    private int maxTimeMinutes;
    private int maxProbs;
    private float mastery;   // THe mastery level we'd want the student to reach.
    private String mode;
    private boolean singleTopicMode;
    private String ccss;
    private int topicId;
    private int cuId;


    public PedagogyParams(int studId, boolean showIntro, int maxTimeMinutes, int maxProbs, String mode, boolean singleTopicMode, String ccss, int topicId, float topicMastery, int cuId) {
        this.studId = studId;
        this.showIntro = showIntro;
        this.maxTimeMinutes = maxTimeMinutes;
        this.maxTime = maxTimeMinutes * 60 * 1000;  // max time is given in minutes.   We convert to milliseconds here
        this.maxProbs = maxProbs;
        this.mode = mode;
        this.singleTopicMode = singleTopicMode;
        this.ccss = ccss;
        this.topicId=topicId;
        this.mastery = topicMastery;
        this.cuId= cuId;
    }

    public int getStudId() {
        return studId;
    }

    public boolean isShowIntro() {
        return showIntro;
    }

    // Returns a time in ms  (read in from db as minutes but converted to ms in the constructor of this class)
    public long getMaxTime() {
        return maxTime;
    }

    public int getMaxTimeMinutes () {
        return this.maxTimeMinutes;
    }

    public int getMaxProbs() {
        return maxProbs;
    }

    public String getMode() {
        return mode;
    }

    public boolean isSingleTopicMode() {
        return singleTopicMode;
    }

    public String getCcss(){
        return ccss;
    }

    public int getTopicId() {
        return topicId;
    }

    public float getMastery() {
        return mastery;
    }

    public void setMastery(float mastery) {
        this.mastery = mastery;
    }

    public int getCuId() {
        return cuId;
    }
}
