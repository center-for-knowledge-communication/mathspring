package edu.umass.ckc.wo.servletController;

import ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.wo.exc.AssistmentsBadInputException;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/19/16
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Handler {

    public abstract boolean init(ServletInfo info);

    public abstract boolean handleEvent(ServletInfo info) throws Exception;

    public abstract boolean cleanup ();
}
