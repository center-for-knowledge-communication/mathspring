package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import org.jdom.Element;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChildAssent extends LoginInterventionSelector {

    private static final String JSP = "childAssent.jsp";
    private String file = null;

    public ChildAssent(SessionManager smgr) throws SQLException {
        super(smgr);
    }

    public void init (SessionManager smgr, PedagogicalModel pm) throws Exception {
        file = getConfigParameter2("file");
        if (file == null)
            throw new UserException("ChildAssent expects config xml and a file parameter");
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
            return new LoginIntervention(file);
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        String assent = params.getString(LoginParams.ASSENT);
        if (assent != null && assent.equalsIgnoreCase("no"))
            return new LoginPage1Intervention("login/loginK12.jsp");
        else
            return null;
//        DbUser.setUserNames(servletInfo.getConn(), smgr.getStudentId(), fname, lini);

    }



    public String f (SessionManager smgr) {
//        if (isFirstLogin) {
        if (true ) {
            return JSP;
        }
        return null;
    }
}
