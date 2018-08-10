package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.beans.ClassConfig;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
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
public class PreSurvey  extends LoginInterventionSelector {
//    public static final String urli = "<iframe src=\"https://docs.google.com/forms/d/1ailDyQ9tChd9Abh6TEUsCoyALYJSLi8mWoIiHzMZcpA/viewform?embedded=true\" width=\"760\" height=\"500\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\">Loading...</iframe>";
//    public static final String url = "https://docs.google.com/forms/d/1ailDyQ9tChd9Abh6TEUsCoyALYJSLi8mWoIiHzMZcpA/viewform?usp=send_form";
    public static final String JSP = "presurvey.jsp";
    public static final String JSPI = "presurveyIframe.jsp";

    private String url;
    private String uidVar=null;
    private String unameVar=null;
    private boolean embed=true;
    public PreSurvey(SessionManager smgr) throws SQLException, UserException {
        super(smgr);

    }

    public void init (SessionManager smgr, PedagogicalModel pm) throws Exception {
        if (configXML == null)
            throw new UserException("PreSurvey expects config xml");
        Element e =this.configXML.getChild("url");
        ClassConfig ci = DbClass.getClassConfig(smgr.getConnection(),smgr.getClassID());
        // first choice to get the URL is the classconfig
        if (ci.getPreSurveyURL() != null)
            this.url=ci.getPreSurveyURL();
        // see if provided in the XML
        else if (e != null)
            this.url= e.getTextTrim();
        // get from db globalsettings.
        else
           this.url = Settings.preSurvey;
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

    }



    // This is declared as a run-once intervention
    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();
//        boolean isFirstLogin = DbUser.isFirstLogin(smgr.getConnection(),smgr.getStudentId(),smgr.getSessionNum());
        if (shownTime > 0)
            return null;
        else {
            super.selectIntervention(e);
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
                servletInfo.getRequest().setAttribute("URL", url);
                return new LoginIntervention(JSP);
            }
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        return null;
    }


}
