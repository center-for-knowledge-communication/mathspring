package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 7/13/15
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAdvancedPedagogySelectionEvent extends AdminCreateClassEvent {

    private int classId;

    public AdminAdvancedPedagogySelectionEvent(ServletParams p) throws Exception {
        super(p);
        classId = p.getInt("classId");
    }

    public int getClassId() {
        return classId;
    }

}
