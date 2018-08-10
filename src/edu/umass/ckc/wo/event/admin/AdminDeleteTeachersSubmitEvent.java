package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/24/14
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminDeleteTeachersSubmitEvent extends AdminEvent {
    private int[] teacherIds = new int[0];

    public AdminDeleteTeachersSubmitEvent(ServletParams p) throws Exception {
        super(p);

        String[] ids = p.getStrings("teacherToDelete");
        if (ids != null && ids.length > 0)   {
            teacherIds = new int[ids.length];
            for (int i=0;i< ids.length;i++)
                teacherIds[i] = Integer.parseInt(ids[i]);
        }
    }

    public int[] getTeacherIds () {
        return this.teacherIds;
    }


}
