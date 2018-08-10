package edu.umass.ckc.wo.login;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 6:53:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginAdult_2 extends Login2 {
    public static final String LOGIN_JSP = "login/loginAdult.jsp";

    public LoginAdult_2() {
        this.login1_jsp = LOGIN_JSP;
        this.login_existingSess_jsp= "login/loginExistingSessionAdult.jsp";
    }
}
