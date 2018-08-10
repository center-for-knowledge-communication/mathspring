package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.UserException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 20, 2005
 * Time: 12:40:54 PM
 */
public class CleanOutSystemTestDataEvent extends SessionEvent {
    public CleanOutSystemTestDataEvent(ServletParams p) throws Exception {
        super(p);
        String u = p.getMandatoryString("user");
        String pw = p.getMandatoryString("password");
        if (u.equals("SystemTest") && pw.equals("WoAdmin") && this.getSessionId() == 1)
            ;
        else throw new UserException("Deleting session data requires a user/password and the correct test sessionID");
    }


}
