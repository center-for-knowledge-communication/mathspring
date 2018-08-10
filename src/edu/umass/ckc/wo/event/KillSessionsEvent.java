package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Sep 13, 2005
 * Time: 2:50:50 PM
 */
public class KillSessionsEvent extends SessionEvent {

   private boolean killAll=false;

    public KillSessionsEvent(ServletParams p, boolean all) throws Exception {
        super(p);    //To change body of overridden methods use File | Settings | File Templates.
        this.killAll = all;
    }

    public boolean isKillAll() {
        return killAll;
    }
}
