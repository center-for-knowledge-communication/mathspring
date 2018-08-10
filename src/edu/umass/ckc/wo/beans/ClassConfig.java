package edu.umass.ckc.wo.beans;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Apr 6, 2009
 * Time: 6:00:13 PM
 */
public class ClassConfig {
    public static final int OFF=0;
    public static final int ON=1;
    public static final int ON_AFTER_PRETEST=2;
    public static final int ON_AFTER_POSTTEST=3;
    private int pretest;
    private int posttest;
    private int fantasy;
    private int mfr;
    private int spatial;
    private int tutoring;
    private boolean useDefaults;
    private boolean showPostSurvey;
    private String preSurveyURL;
    private String postSurveyURL;
    private int postSurveyWaitTimeMin;
    private boolean soundSync;
    private int mouseSaveInterval=-1; // <= 0 indicates mouse-tracking off in client; >0 is number of seconds it waits before saving to server.

    public ClassConfig(int pretest, int posttest, int fantasy, int mfr, int spatial, int tutoring, boolean useDefaults,
                       boolean showPostSurvey, String presurveyurl, String postsurveyurl, int postSurveyWaitTime, boolean soundSync, int mouseSaveInterval) {
        this.pretest = pretest;
        this.posttest = posttest;
        this.fantasy = fantasy;
        this.mfr = mfr;
        this.spatial = spatial;
        this.tutoring = tutoring;
        this.useDefaults = useDefaults;
        this.showPostSurvey=showPostSurvey;
        this.preSurveyURL=presurveyurl;
        this.postSurveyURL=postsurveyurl;
        this.postSurveyWaitTimeMin = postSurveyWaitTime;
        this.soundSync = soundSync;
        this.mouseSaveInterval = mouseSaveInterval;
    }

    public static ClassConfig getDefaultConfig () {
        return new ClassConfig(ON,ON_AFTER_PRETEST,ON_AFTER_POSTTEST,ON_AFTER_POSTTEST,ON_AFTER_POSTTEST,ON_AFTER_PRETEST,
                true, false, null, null, 0, false, -1);
    }

    public int getPretest() {
        return pretest;
    }

    public int getPosttest() {
        return posttest;
    }

    public int getFantasy() {
        return fantasy;
    }

    public int getMfr() {
        return mfr;
    }

    public int getSpatial() {
        return spatial;
    }

    public int getTutoring() {
        return tutoring;
    }

    public boolean getUseDefaults () {
        return useDefaults;
    }

    public boolean isShowPostSurvey () {
        return this.showPostSurvey;
    }

    public String getPreSurveyURL() {
        return preSurveyURL;
    }

    public void setPreSurveyURL(String preSurveyURL) {
        this.preSurveyURL = preSurveyURL;
    }

    public String getPostSurveyURL() {
        return postSurveyURL;
    }

    public void setPostSurveyURL(String postSurveyURL) {
        this.postSurveyURL = postSurveyURL;
    }

    public int getPostSurveyWaitTimeMin() {
        return postSurveyWaitTimeMin;
    }

    public boolean isSoundSync() {
        return soundSync;
    }

    public int getMouseSaveInterval () { return this.mouseSaveInterval; }


    public String toString () {
        return String.format("&pretest=%d&posttest=%d&fantasy=%d&mfr=%d&spatial=%d&tutoring=%d",
                pretest,posttest,fantasy,mfr,spatial,tutoring);
    }
}
