package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/24/14
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminDeleteClassesSubmitEvent extends AdminEvent {
    private int[] classIds = new int[0];

    public AdminDeleteClassesSubmitEvent(ServletParams p) throws Exception {
        super(p);

        String[] ids = p.getStrings("classToDelete");
        if (ids != null && ids.length > 0)   {
            classIds = new int[ids.length];
            for (int i=0;i< ids.length;i++)
                classIds[i] = Integer.parseInt(ids[i]);
        }
    }

    public int[] getClassIds () {
        return this.classIds;
    }


}
