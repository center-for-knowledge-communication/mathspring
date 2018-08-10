package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;
import ckc.servlet.servbase.UserException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 14, 2005
 * Time: 3:05:47 PM
 */
public class SessionEvent extends ActionEvent {
    protected int sessionId;
    
    protected SessionEvent () {}

    public SessionEvent (ServletParams p) throws Exception {
        super(p);
        try {
            setSessionId(p.getInt("sessionId"));
        } catch (Exception e) {
            throw new UserException("SessionEvent expects sessionId parameter");
        }

    }

    public SessionEvent (ServletParams p, int sessId) throws Exception {
        super(p);
        setSessionId(sessId);
    }

    public SessionEvent (ServletParams p, boolean noGripe) throws Exception {
        super(p);
        try {
            sessionId = p.getInt("sessionId");
        } catch (Exception e) {
            sessionId=-1;
        }

    }


    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId () {
        return sessionId;
    }
}
