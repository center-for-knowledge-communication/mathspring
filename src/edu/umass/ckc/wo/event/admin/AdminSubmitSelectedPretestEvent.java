package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/**
 * The third event in the series for creating a class.  Receives the fields
 * that give its pretest pool.
 */

public class AdminSubmitSelectedPretestEvent extends AdminCreateClassEvent {

    private int classId;
    private int poolId;
    private boolean givePretest;

    public AdminSubmitSelectedPretestEvent(ServletParams p) throws Exception {
        super(p);
        classId = p.getInt("classId");
        String poolIDstr = p.getString("poolId");
        if (poolIDstr.equals("noPretest")) {
            givePretest = false;
            poolId=0;
        }
        else {
            givePretest = true;
            poolId = p.getInt("poolId");
        }


    }

    public int getClassId() {
        return classId;
    }

    public int getPoolId() {
        return poolId;
    }

    public boolean isGivePretest() {
        return givePretest;
    }
}