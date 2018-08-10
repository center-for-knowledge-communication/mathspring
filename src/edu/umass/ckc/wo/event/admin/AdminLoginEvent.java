package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by marshall on 9/25/17.
 */
public class AdminLoginEvent extends AdminTeacherLoginEvent {

    public AdminLoginEvent (ServletParams params) throws Exception {
        super(params);
    }
}
