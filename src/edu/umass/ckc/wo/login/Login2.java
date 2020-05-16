package edu.umass.ckc.wo.login;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.http.HttpServletRequest;
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
 */
public class Login2 implements LoginServletAction {
    public static String login1_jsp;
    protected String login_existingSess_jsp;



    public LoginResult process(ServletInfo servletInfo) throws Exception {

        ServletParams params = servletInfo.getParams();
        Connection conn= servletInfo.getConn();
        HttpServletRequest req = servletInfo.getRequest();
        
    	Locale rbloc = req.getLocale();
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",rbloc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}

        
        String uName = params.getString(LoginParams.USER_NAME,"");
        String pw = params.getString(LoginParams.PASSWORD,"");
        String forgotPassword = params.getString("forgotPassword","");;
        boolean logoutExistingSession = params.getBoolean(LoginParams.LOGOUT_EXISTING_SESSION,false);
        Locale loc = (Locale) req.getLocales().nextElement();

        SessionManager smgr = new SessionManager(conn);
        LoginResult lr = smgr.login(uName,pw,System.currentTimeMillis(), logoutExistingSession, loc);
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
        	if (forgotPassword.equals("on")) {
        		req.setAttribute(LoginParams.MESSAGE,rb.getString("ask_teacher_for_password"));
        	}
        	else {
        		req.setAttribute(LoginParams.MESSAGE,lr.getMessage());        		
        	}
            lr.setForwardedToJSP(true);
            req.getRequestDispatcher(login1_jsp).forward(req, servletInfo.getResponse());
            return lr;
        }
    }

}
