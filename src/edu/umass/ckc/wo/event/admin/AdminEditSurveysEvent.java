package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 10/15/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminEditSurveysEvent extends AdminEvent {
    public static final String MODE = "mode";
    public static final String SAVE = "save";
    public static final String PRE_SURVEY = "preSurvey";
    public static final String POST_SURVEY = "postSurvey";
    private String mode;
    private String preSurvey;
    private String postSurvey;



  public AdminEditSurveysEvent(ServletParams p) throws Exception {
    super(p);
    mode = p.getString(MODE,null);
    preSurvey = p.getString(PRE_SURVEY,null);
    postSurvey = p.getString(POST_SURVEY,null);
  }

    public String getMode() {
        return mode;
    }

    public boolean isSaveMode () {
        return getMode() != null && getMode().equals(SAVE);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPreSurvey() {
        return preSurvey;
    }

    public void setPreSurvey(String preSurvey) {
        this.preSurvey = preSurvey;
    }

    public String getPostSurvey() {
        return postSurvey;
    }

    public void setPostSurvey(String postSurvey) {
        this.postSurvey = postSurvey;
    }
}
