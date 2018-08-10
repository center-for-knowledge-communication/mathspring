package edu.umass.ckc.servlet.servbase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 3:21:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ServletAction {

    public abstract String process(Connection conn, ServletContext servletContext, ServletParams params, HttpServletRequest req,
                                   HttpServletResponse resp, StringBuffer servletOutput) throws Exception;
}
