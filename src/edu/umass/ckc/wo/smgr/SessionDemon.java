package edu.umass.ckc.wo.smgr;

import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Sep 8, 2005
 * Time: 3:39:46 PM
 */
public class SessionDemon extends Thread {
    private Connection conn;

    public SessionDemon(Connection conn) {
        this.conn = conn;
    }

    public void run () {

            while (true ) {
                try {
                    DbSession.cleanupStaleSessions(this.conn);
                    Thread.sleep(Settings.sessionDemonCleanupInterval);
                } catch (Throwable e) {
                    throw new RuntimeException(e.getMessage());
                }

            }

    }

}