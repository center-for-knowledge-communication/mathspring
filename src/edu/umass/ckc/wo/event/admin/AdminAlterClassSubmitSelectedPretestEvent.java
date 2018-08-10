package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * The third event in the series for creating a class.  Receives the fields
 * that give its pretest pool.
 */

public class AdminAlterClassSubmitSelectedPretestEvent extends AdminClassEvent {

    private int poolId;
    private boolean givePretest;

    public AdminAlterClassSubmitSelectedPretestEvent(ServletParams p) throws Exception {
        super(p);
        String pool = p.getString("poolId");
        if (pool.equalsIgnoreCase("noPretest")) {
            givePretest= false;
            poolId = 0;  // there is no pool
        }
        else {
            givePretest=true;
            poolId = Integer.parseInt(pool);
        }
    }



    public int getPoolId() {
        return poolId;
    }

    public boolean isGivePretest() {
        return givePretest;
    }
}