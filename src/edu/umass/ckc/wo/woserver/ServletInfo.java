package edu.umass.ckc.wo.woserver;

import ckc.servlet.servbase.ServletParams;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/17/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServletInfo {

    ServletContext servletContext;
    Connection conn;
    HttpServletRequest request;
    HttpServletResponse response;
    ServletParams params;
    StringBuffer output;
    String hostPath;
    String contextPath;
    String servletName;

    public ServletInfo(ServletContext servletContext, Connection conn, HttpServletRequest request, HttpServletResponse response, ServletParams params, StringBuffer output, String hostPath, String contextPath, String servletName) {
        this.servletContext = servletContext;
        this.conn = conn;
        this.request = request;
        this.response = response;
        this.params = params;
        this.output = output;
        this.hostPath = hostPath;
        this.contextPath = contextPath;
        this.servletName = servletName;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Connection getConn() {
        return conn;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public ServletParams getParams() {
        return params;
    }

    public StringBuffer getOutput() {
        return output;
    }

    public String getHostPath() {
        return hostPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getServletName() {
        return servletName;
    }
}
