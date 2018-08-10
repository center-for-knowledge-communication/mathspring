package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutormeta.Intervention;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetNeighbors extends LoginInterventionSelector {
    private static final String JSP = "neighbors.jsp";


    public GetNeighbors(SessionManager smgr) throws SQLException {
        super(smgr);
    }




    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();
        if (shownTime > 0)
            return null;
        // At end of LoginSequence we need to clear this time so that a new login will show this again.
        else {
            super.selectIntervention(e);
            List<User> students = DbClass.getClassStudents(conn,smgr.getStudentClass(smgr.getStudentId()));
            HttpServletRequest req = this.servletInfo.getRequest();
            req.setAttribute(LoginParams.STUDENTS,students);
            req.setAttribute("nstudents",students.size());
            return new LoginIntervention(JSP);
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        int left = params.getInt(LoginParams.LEFT);
        int right = params.getInt(LoginParams.RIGHT);
        DbUser.setFlankingUsers(servletInfo.getConn(), smgr.getStudentId(), left, right);
        return null;
    }


}
