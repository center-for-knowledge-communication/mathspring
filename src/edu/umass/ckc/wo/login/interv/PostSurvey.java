package edu.umass.ckc.wo.login.interv;

import ckc.servlet.servbase.ServletParams;
import ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.beans.ClassConfig;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.state.WorkspaceState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import org.jdom.Element;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/1/15
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostSurvey extends LoginInterventionSelector {
//    public static final String urli = "<iframe src=\"https://docs.google.com/forms/d/1ailDyQ9tChd9Abh6TEUsCoyALYJSLi8mWoIiHzMZcpA/viewform?embedded=true\" width=\"760\" height=\"500\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\">Loading...</iframe>";
//    public static final String url = "https://docs.google.com/forms/d/1ailDyQ9tChd9Abh6TEUsCoyALYJSLi8mWoIiHzMZcpA/viewform?usp=send_form";
    public static final String JSP = "presurvey.jsp";
    public static final String JSPI = "presurveyIframe.jsp";

    private String url;
    private boolean embed=true;
    private String uidVar;
    private String unameVar;
    private int minutesToWait;  // show the post survey if the user has been in the system this many minutes or more

    public PostSurvey(SessionManager smgr) throws SQLException, UserException {
        super(smgr);

    }

    public void init (SessionManager smgr, PedagogicalModel pm) throws Exception {
        if (configXML == null)
            throw new UserException("PostSurvey expects config xml");
        Element e =this.configXML.getChild("url");
        ClassConfig ci = DbClass.getClassConfig(smgr.getConnection(),smgr.getClassID());
        // first choice to get the URL is the classconfig
        if (ci.getPostSurveyURL() != null)
            this.url=ci.getPostSurveyURL();
            // see if provided in the XML
        else if (e != null)
            this.url= e.getTextTrim();
            // get from db globalsettings.
        else
            this.url = Settings.postSurvey;

        e = configXML.getChild("studId");

        if (e != null) {
            uidVar = e.getTextTrim();
        }
        e = configXML.getChild("userName");
        if (e != null) {
            unameVar = e.getTextTrim();
        }

        e =this.configXML.getChild("embed");
        if (e != null)
            this.embed = Boolean.parseBoolean(e.getTextTrim());
        e =this.configXML.getChild("minutesToWait");
        if (e != null)
            this.minutesToWait = Integer.parseInt(e.getTextTrim());
        else this.minutesToWait =0;
    }


    // The post survey is available when the classconfig table for this student has its showPostSurvey field set to 1
    // or if the minutesToWait value has been exceeded by the number of minutes the user has spent in the system
    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();

        int classId = smgr.getClassID();
        ClassConfig cc = DbClass.getClassConfig(smgr.getConnection(),classId);
        boolean showSurvey = cc.isShowPostSurvey();
        int postSurveyWaitTimeMin = cc.getPostSurveyWaitTimeMin();
        // If a post survey wait time is defined in the classconfig, use it instead of the value in the XML config parameters.
        if (postSurveyWaitTimeMin > 0)
            this.minutesToWait = postSurveyWaitTimeMin;
        // We show the survey when the ClassConfig.showPostSurvey is set to 1 and student hasn't done it yet.
        boolean surveyDone =  smgr.getStudentState().getWorkspaceState().isPostSurveyDone();

        if (this.minutesToWait > 0 && DbUser.getLoggedInTimeInMinutes(smgr.getConnection(), smgr.getStudentId()) >= this.minutesToWait)
            return showSurvey(e);
        else if (!showSurvey || shownTime > 0 || surveyDone)
            return null;
        else
            return showSurvey(e);

    }

    private Intervention showSurvey(SessionEvent e) throws Exception {
        super.selectIntervention(e);
        StudentState state = smgr.getStudentState();
        WorkspaceState wstate = state.getWorkspaceState();
        // set the student state so we know its been done
        wstate.setPostSurveyDone(true);

        // Shows the survey in an embedded iframe
        if (this.embed) {
            servletInfo.getRequest().setAttribute("iframeURL",url);
            return new LoginIntervention(JSPI);
        }
        else {
            // URL has two variables (e.g. <uid>) that need to be replaced with userName and studId
            url = url.replaceFirst("<"+uidVar+">",Integer.toString(smgr.getStudentId()));
            url = url.replaceFirst("<"+unameVar+">",smgr.getUserName());
        // A JSP will show nothing more than a "continue" button.
        // The URL comes up in a separate browser window.  When the user is done in the separate window
        // they close it and click the "continue" button.
            servletInfo.getRequest().setAttribute("URL",url);
            return new LoginIntervention(JSP);
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        return null;
    }


}
