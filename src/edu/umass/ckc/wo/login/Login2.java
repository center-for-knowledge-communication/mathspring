package edu.umass.ckc.wo.login;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.Connection;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 6:49:18 PM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank 	04-24-20	Issue #28
 * Frank    05-29-2020  issue #28 remove password reset for students
 * Frank	09-14-20	issue #237 added pauseStudentUse
 * Frank	12-08-20	issue #185 student session could be hijacked by altering sessionId on the URL 
 */
public class Login2 implements LoginServletAction {
    public static String login1_jsp;
    protected String login_existingSess_jsp;



    public LoginResult process(ServletInfo servletInfo) throws Exception {

        ServletParams params = servletInfo.getParams();
        Connection conn= servletInfo.getConn();
        HttpServletRequest req = servletInfo.getRequest();
        HttpSession session = req.getSession();
        session.invalidate();
    	session = req.getSession();
        
    	       
        String uName = params.getString(LoginParams.USER_NAME,"");
        String pw = params.getString(LoginParams.PASSWORD,"");
        String forgotPassword = params.getString("forgotPassword","");
        boolean logoutExistingSession = params.getBoolean(LoginParams.LOGOUT_EXISTING_SESSION,false);
        Locale loc = (Locale) req.getLocales().nextElement();
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	

        
        SessionManager smgr = new SessionManager(conn);
        LoginResult lr = smgr.login(uName,pw,System.currentTimeMillis(), logoutExistingSession, loc);
        // login succeeds.  Now we move into selecting login interventions
        if (!lr.isFailed())  {
            lr.setForwardedToJSP(false);
    		session.setAttribute("smgrSessionId", smgr.getSessionId());
            return lr;
        }
        // This user already logged in.   Generate a page asking them if they want to logout all existing sessions by logging in.
        if (lr.getStatus()== LoginResult.ALREADY_LOGGED_IN) {
                req.setAttribute(LoginParams.MESSAGE,lr.getMessage());
                req.setAttribute(LoginParams.USER_NAME,uName);
                req.setAttribute(LoginParams.PASSWORD,pw);
                req.getRequestDispatcher(login_existingSess_jsp).forward(req, servletInfo.getResponse());
                lr.setForwardedToJSP(true);
        		session.setAttribute("smgrSessionId", smgr.getSessionId());
                return lr;
        }
        // Some other error happened during login, so reprompt and show a message about what went wrong
        else {
        	 String msg = lr.getMessage();
     		req.setAttribute(LoginParams.MESSAGE,msg);
            lr.setForwardedToJSP(true);
            req.getRequestDispatcher(login1_jsp).forward(req, servletInfo.getResponse());
            return lr;
        }
    }

}
