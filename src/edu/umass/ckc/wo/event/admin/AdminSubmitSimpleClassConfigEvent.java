package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 7/27/15
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubmitSimpleClassConfigEvent extends AdminClassEvent {

    private String diffRate;
    private String lowDiff;
    private String highDiff;
    private String lc;
    private String collab;
    private boolean createClassSeq;

    public static final String DIFF_RATE = "diffRate";
    public static final String LOW_DIFF = "lowEndDiff";
    public static final String HIGH_DIFF = "highEndDiff";
    public static final String COLLAB = "collab";
    public static final String LC = "learningCompanion";

    public AdminSubmitSimpleClassConfigEvent(ServletParams p) throws Exception {
        super(p);
        this.diffRate = p.getString(DIFF_RATE);
        this.lowDiff = p.getString(LOW_DIFF);
        this.highDiff = p.getString(HIGH_DIFF);
        this.lc = p.getString(LC);
        this.collab = p.getString(COLLAB);
        this.createClassSeq = p.getBoolean("createClassSeq",false); // an indicator of whether this submission is part of
                                                    // a class creation sequence
    }

    public String getDiffRate() {
        return diffRate;
    }

    public String getLowDiff() {
        return lowDiff;
    }

    public String getHighDiff() {
        return highDiff;
    }

    public String getLc() {
        return lc;
    }

    public String getCollab() {
        return collab;
    }

    public boolean isCreateClassSeq() {
        return createClassSeq;
    }
}
