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
 * Time: 3:48:27 PM
 * The first login page for an adult.   Presents the user/password fields plus others.
 */
public class LoginAdult_1 implements LoginServletAction {
    /**
     * Process the login action for adult.   This returns a JSP for k12 login.
     * @param servletInfo
     * @return
     * @throws ServletException
     * @throws java.io.IOException
     */
    public LoginResult process(ServletInfo servletInfo) throws ServletException, IOException {
        HttpServletRequest req = servletInfo.getRequest();
        req.setAttribute(LoginParams.USER_NAME,"" );
        req.setAttribute(LoginParams.PASSWORD,"" );
        req.setAttribute(LoginParams.LOGOUT_EXISTING_SESSION,"false");
        req.setAttribute("clientType",LoginParams.ADULT);
        req.setAttribute("preSurvey", Settings.preSurvey);
        req.setAttribute("postSurvey", Settings.postSurvey);
        req.setAttribute("startPage",this.getClass().getSimpleName());
        String name = getClass().getName();
        if (name.lastIndexOf('.') > 0)
            name = name.substring(name.lastIndexOf('.')+1);
        req.setAttribute(LoginParams.START_PAGE,name);
        req.setAttribute(LoginParams.MESSAGE,null);
        String jsp= "login/loginAdult.jsp";
        req.getRequestDispatcher(jsp).forward(req, servletInfo.getResponse());
        LoginResult lr = new LoginResult(-1,null,LoginResult.PRE_LOGIN,LoginResult.FORWARDED_TO_JSP);
        // results are forwarded to a JSP
        return lr;
    }

}
