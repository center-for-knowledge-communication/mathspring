package edu.umass.ckc.wo.woserver;


import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.*;
import edu.umass.ckc.wo.event.tutorhut.TutorHomeEvent;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.handler.TutorHutEventHandler;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MathSpringTutorHandler {
//    private WoModel model_;
    private TutorBrainEventFactory eventFactory;

    private static Logger logger =   Logger.getLogger(MathSpringTutorHandler.class);

    private ServletInfo servletInfo;




    public MathSpringTutorHandler(ServletInfo info) throws Exception {
        this.servletInfo = info;
        eventFactory = new TutorBrainEventFactory();
    }


    public boolean handleRequest() throws Throwable {
        ServletEvent e = eventFactory.buildEvent(servletInfo.params, "tutorhut");
        SessionManager smgr = new SessionManager(servletInfo.conn,((SessionEvent) e).getSessionId(), servletInfo.hostPath, servletInfo.contextPath).buildExistingSession();
        if (e instanceof DbTestEvent) {
            if (testDB(servletInfo.getConn()))
                servletInfo.getOutput().append("Successful db query ran.");
            else servletInfo.getOutput().append("Db query did not return result set.");
        }
        return true;
    }

    private boolean testDB (Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select count(*) from student";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next())
                return true;
            else return false;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


}