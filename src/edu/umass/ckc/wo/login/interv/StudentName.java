package edu.umass.ckc.wo.login.interv;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbUser;
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
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentName extends LoginInterventionSelector {

    private static final String JSP = "studentName.jsp";
    private static final String JSP_NEW = "studentName_new.jsp";

    public StudentName(SessionManager smgr) throws SQLException {
        super(smgr);
    }

    // THis is a run-once intervention (declared in the logins.xml that way).   This gets intervention is logged
    // in the RunONceInterventionLog table for a given student once it runs so it won't run again.
    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();
//        boolean firstLogin = DbUser.isFirstLogin(smgr.getConnection(),smgr.getStudentId(),smgr.getSessionNum());
        // Only return an intervention when this is the very first login
        if ( shownTime > 0)
            return null;
        else {
            super.selectIntervention(e);
            return new LoginIntervention(
                    Settings.useNewGUI() ? JSP_NEW : JSP);
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        String fname = params.getString(LoginParams.FNAME);
        String lini = params.getString(LoginParams.LINI);
        DbUser.setUserNames(servletInfo.getConn(), smgr.getStudentId(), fname, lini);
        return null;
    }



    public String f (SessionManager smgr) {
//        if (isFirstLogin) {
        if (true ) {
            return Settings.useNewGUI() ? JSP_NEW : JSP;
        }
        return null;
    }
}
