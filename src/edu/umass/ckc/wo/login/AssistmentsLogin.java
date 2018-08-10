package edu.umass.ckc.wo.login;

import ckc.servlet.servbase.ServletAction;
import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 10:40:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssistmentsLogin implements ServletAction {

    // This was for testing only
    public String process(Connection conn, ServletContext servletContext, ServletParams params, HttpServletRequest req,
                          HttpServletResponse resp, StringBuffer servletOutput) throws Exception {
        String flashClientURL = (String) servletContext.getAttribute("flashClientURI");
        String userName = params.getString("userName");
        int studId = UserRegistrationHandler.registerTemporaryUser(conn, DbClass.ASSISTMENTS_CLASS_NAME, User.UserType.coopStudent);
        SessionManager smgr = new SessionManager(conn).assistmentsLoginSession(studId);
        int sessId = smgr.getSessionNum();
        int classId = smgr.getClassID();
        ClassInfo c = DbClass.getClass(conn,classId);
        DbSession.setClientType(conn, smgr.getSessionNum(), c.getFlashClient());
        servletOutput.append("<login success=\"true\" sessionId=\"" + sessId + "\" studId=\"" + studId + "\" />");
        return null;  // tell the caller that it's not JSP
    }
}
