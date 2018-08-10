package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 7/27/15
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAlterClassSubmitPrePostEvent extends AdminClassEvent {
    private static final String POST = "postSurvey";
    private String postSurvey;

    public AdminAlterClassSubmitPrePostEvent(ServletParams p) throws Exception {
        super(p);
        this.postSurvey = p.getString(POST);
    }

    public boolean showPostSurvey() {
        return this.postSurvey.equals("on");
    }

}
