package edu.umass.ckc.wo.login;

import ckc.servlet.servbase.ServletAction;
import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 3:47:51 PM
 * The first login page for a k12 user.    Presents user/password inputs plus other buttons
 */
public class LoginK12_1 implements LoginServletAction {

    /**
     * Process the login action for k12.   This returns a JSP for k12 login.
     * @param  servletInfo
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public LoginResult process(ServletInfo servletInfo) throws ServletException, IOException {
        HttpServletRequest req = servletInfo.getRequest() ;
        req.setAttribute(LoginParams.USER_NAME,"" );
        req.setAttribute(LoginParams.PASSWORD,"" );
        req.setAttribute(LoginParams.LOGOUT_EXISTING_SESSION,"false");
        req.setAttribute("preSurvey", Settings.preSurvey);
        req.setAttribute("postSurvey", Settings.postSurvey);
        req.setAttribute("clientType",LoginParams.K12);
        req.setAttribute("startPage",this.getClass().getSimpleName());
        String name = getClass().getName();
        if (name.lastIndexOf('.') > 0)
            name = name.substring(name.lastIndexOf('.')+1);
        req.setAttribute(LoginParams.START_PAGE,name);  // must be name of this class
        req.setAttribute(LoginParams.MESSAGE,null);
        String jsp = "login/loginK12.jsp";
        req.getRequestDispatcher(jsp).forward(req, servletInfo.getResponse());
        LoginResult lr = new LoginResult(-1,null,LoginResult.PRE_LOGIN,LoginResult.FORWARDED_TO_JSP);
        return lr;
    }
}
