package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbUserProfile;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.Intervention;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathAttitudes extends LoginInterventionSelector {
    private static final String JSP = "mathAttitudes.jsp";
    private static final String JSP_NEW = "mathAttitudes_new.jsp";

    // This intervention is declared as run-once so it gets entered into the RunOnceInterventionLog once it plays for
    // a student.
    public MathAttitudes(SessionManager smgr) throws SQLException {
        super(smgr);
    }

    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();
        // shownTime is checked to make sure we don't show this more than once in that first login
        if ( shownTime > 0)
            return null;
        else {
            super.selectIntervention(e);
            return new LoginIntervention(
                    Settings.useNewGUI() ? JSP_NEW : JSP);
        }
    }



    public LoginIntervention processInput (ServletParams params) throws Exception {


        int confidence = params.getInt(LoginParams.CONFIDENCE,0);
        int excitement = params.getInt(LoginParams.EXCITEMENT,0);
        int frustration = params.getInt(LoginParams.FRUSTRATION,0);
        int interest = params.getInt(LoginParams.INTEREST,0);
        // TODO might want to put in a correct servlet path rather than ""

        DbUserProfile.deleteProfile(servletInfo.getConn(), smgr.getStudentId())  ;
        DbUserProfile.setValues(conn, smgr.getStudentId(), confidence, excitement, interest, frustration);
        return null;
    }



    public String f (SessionManager smgr) {
        return Settings.useNewGUI() ? JSP_NEW : JSP;
    }
}
