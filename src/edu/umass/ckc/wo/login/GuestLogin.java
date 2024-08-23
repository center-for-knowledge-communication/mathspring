package edu.umass.ckc.wo.login;

import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.servlet.servbase.ServletAction;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Locale;
/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 10:40:55 PM
 * To change this template use File | Settings | File Templates.
 *  
 * Frank    08-22-24    Issue #781R7 - setup Spanish user profile
 */
public class GuestLogin implements LoginServletAction {

    public LoginResult process(ServletInfo servletInfo) throws Exception {
        Connection conn = servletInfo.getConn();
        ServletParams params = servletInfo.getParams();
        HttpServletRequest req = servletInfo.getRequest();

        String lang = params.getString("lang");
        String className = edu.umass.ckc.wo.db.DbClass.GUEST_USER_CLASS_NAME;
        if (!lang.equals("en")) {
        	className = className + "-" + lang;
        }
        int studId = UserRegistrationHandler.registerTemporaryUser(conn, className, User.UserType.guest);

        
        java.util.Locale myloc = new java.util.Locale(lang,"US");
        SessionManager smgr = new SessionManager(conn).guestLoginSession(studId, lang);
        smgr.setLocale(myloc);
        DbSession.setClientType(conn, smgr.getSessionNum(), params.getString(LoginParams.CLIENT_TYPE));
        LearningCompanion lc = smgr.getLearningCompanion();
        req.setAttribute("sessionId", smgr.getSessionNum());
        req.setAttribute("learningCompanion", (lc != null) ? lc.getCharactersName() : "");

        new LandingPage(servletInfo,smgr).handleRequest();
        LoginResult lr = new LoginResult(smgr.getSessionNum(),null, LoginResult.NEW_SESSION, LoginResult.FORWARDED_TO_JSP);
        return lr;
    }
}
