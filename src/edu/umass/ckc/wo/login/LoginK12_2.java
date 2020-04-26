package edu.umass.ckc.wo.login;


import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 5:29:38 PM
 * This processes the user/password that are input fields.
 * There are several possible results:
 * 1a.   An error (e.g. bad user/password).   Regenerate k12 login page
 * 1b    An error, The user is already logged in.    Generate loginExistingsession.jsp (asks if they want to continue by logging out existing session)
 * 2.   Successful login (first login for this user).   Generate login_2.jsp (asks for name)
 * 3.   Successful login (not first login).   Generate login_3.jsp (asks for flanking users)
 * 
 *  Frank 04-24-2020 issue #28
 */
public class LoginK12_2 extends Login2 {

    public static final String LOGIN_JSP = "login/loginK12_new.jsp";
    public static final String LOGIN_JSP_NEW = "login/loginK12_new.jsp";

    public LoginK12_2() {
        this.login1_jsp = LOGIN_JSP;
        this.login_existingSess_jsp = "login/loginExistingSessionK12.jsp";
    }

    @Override
    public LoginResult process(ServletInfo servletInfo) throws Exception {
        if (Settings.useNewGUI()) {
            this.login1_jsp = LOGIN_JSP_NEW;
            this.login_existingSess_jsp = "login/loginExistingSessionK12_new.jsp";
        }
        return super.process(servletInfo);
    }
}
