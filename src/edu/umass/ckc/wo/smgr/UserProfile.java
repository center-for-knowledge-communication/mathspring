package edu.umass.ckc.wo.smgr;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 10/3/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProfile {
    private int studId;
    private int confidence;
    private int excitement;

    public UserProfile(int studId, int confidence, int excitement) {
        this.studId = studId;
        this.confidence = confidence;
        this.excitement = excitement;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getExcitement() {
        return excitement;
    }

    public void setExcitement(int excitement) {
        this.excitement = excitement;
    }
}
