package edu.umass.ckc.wo.event;


import edu.umass.ckc.wo.event.admin.AdminTeacherLoginEvent;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.ActionEvent;

import java.lang.reflect.Constructor;



/** Build events from the args to the servlet.
 */
public class WoAdminEventFactory {


    public WoAdminEventFactory() {
    }


    public ServletEvent buildEvent(ServletParams p) throws Exception {
        String action = p.getString("action",null);
        // no action means we just go to the opening login page for a teacher
        if (action == null) {
            return new AdminTeacherLoginEvent(p);
        }
        String name ;
        name = "edu.umass.ckc.wo.event.admin."+ action + "Event";
        Class c = Class.forName(name);
        Constructor constr = c.getConstructor(ServletParams.class);
        ServletEvent ev = (ActionEvent) constr.newInstance(p);
        return ev;
    }
}
