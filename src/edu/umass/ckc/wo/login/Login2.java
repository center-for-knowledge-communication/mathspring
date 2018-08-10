package edu.umass.ckc.wo.login;

import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.servlet.servbase.ServletAction;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 6:49:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Login2 implements LoginServletAction {
    public static String login1_jsp;
    protected String login_existingSess_jsp;



    public LoginResult process(ServletInfo servletInfo) throws Exception {

        ServletParams params = servletInfo.getParams();
        Connection conn= servletInfo.getConn();
        HttpServletRequest req = servletInfo.getRequest();
        String uName = params.getString(LoginParams.USER_NAME,"");
        String pw = params.getString(LoginParams.PASSWORD,"");
        boolean logoutExistingSession = params.getBoolean(LoginParams.LOGOUT_EXISTING_SESSION,false);
        SessionManager smgr = new SessionManager(conn);
        LoginResult lr = smgr.login(uName,pw,System.currentTimeMillis(), logoutExistingSession);
        // login succeeds.  Now we move into selecting login interventions
        if (!lr.isFailed())  {
            lr.setForwardedToJSP(false);
            return lr;
        }
        // This user already logged in.   Generate a page asking them if they want to logout all existing sessions by logging in.
        if (lr.getStatus()== LoginResult.ALREADY_LOGGED_IN) {
                req.setAttribute(LoginParams.MESSAGE,lr.getMessage());
                req.setAttribute(LoginParams.USER_NAME,uName);
                req.setAttribute(LoginParams.PASSWORD,pw);
                req.getRequestDispatcher(login_existingSess_jsp).forward(req, servletInfo.getResponse());
                lr.setForwardedToJSP(true);
                return lr;
        }
        // Some other error happened during login, so reprompt and show a message about what went wrong
        else {
            req.setAttribute(LoginParams.MESSAGE,lr.getMessage());
            lr.setForwardedToJSP(true);
            req.getRequestDispatcher(login1_jsp).forward(req, servletInfo.getResponse());
            return lr;
        }
    }

}
