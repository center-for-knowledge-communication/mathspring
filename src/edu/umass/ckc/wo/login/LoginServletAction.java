package edu.umass.ckc.wo.login;

import edu.umass.ckc.servlet.servbase.ServletAction;
import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/27/15
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LoginServletAction  {

    public LoginResult process(ServletInfo servletInfo) throws Exception;
}
