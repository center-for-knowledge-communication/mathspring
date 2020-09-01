package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.smgr.User;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 * Frank	09-01-20	Issue #230	Add params for fname & lname to login intervemtion
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
        boolean firstLogin = DbUser.isFirstLogin(smgr.getConnection(),smgr.getStudentId(),smgr.getSessionNum());
        // Only return an intervention when this is the very first login
        if ( shownTime > 0)
            return null;
        else {
            super.selectIntervention(e);
        	String studentNameUrl = Settings.useNewGUI() ? JSP_NEW : JSP;
            User student = DbUser.getStudent(smgr.getConnection(),smgr.getStudentId());
            if (student.getFname().length() > 0) { 
            	studentNameUrl = studentNameUrl + "?fname=" + student.getFname();
            }
            else {
            	studentNameUrl = studentNameUrl + "?fname=none";            	
            }
            if (student.getLname().length() > 0) { 
            	studentNameUrl = studentNameUrl + "&lname=" + student.getLname().substring(0, 1);
            }
            else {
            	studentNameUrl = studentNameUrl + "&lname=none";            	            	
            }
        	return new LoginIntervention(studentNameUrl);                    
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {

        String fname = params.getString(LoginParams.FNAME);
        String lini = params.getString(LoginParams.LINI);
        if ((fname.length() > 0) && (lini.length() > 0)) {
        	lini = lini.substring(0, 1);
        	DbUser.setUserNames(servletInfo.getConn(), smgr.getStudentId(), fname, lini);
        }
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
