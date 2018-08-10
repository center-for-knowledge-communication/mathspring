package edu.umass.ckc.wo.log;

import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Feb 3, 2010
 * Time: 4:49:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePostLogger {


    // DM 3/17/10 no longer in use since conversion to v2.  
    private static int logEvent1(Connection conn, EndActivityEvent e, String act, int studId, int sessId,
                                String activityName) throws SQLException {

        String table = "EpisodicData2";
        return logEvent(conn,e,act,studId,sessId,activityName,table);
    }

     public static int logEvent2(Connection conn, EndActivityEvent e, String act, int studId, int sessId, 
                                String activityName) throws SQLException {

        String table = "PrePostEventLog";
        return logEvent(conn,e,act,studId,sessId,activityName,table);
    }


    public static int logEvent(Connection conn, EndActivityEvent e, String act, int studId, int sessId,
                               String activityName, String table) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into "+table+" (studId,sessNum,userInput,isCorrect" +
                ",elapsedTime,problemId,action,probElapsed, activityName, time) values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            ps.setInt(2, sessId);
            if (e.getUserInput() != null)
                ps.setString(3,e.getUserInput());
            else ps.setNull(3,Types.VARCHAR);
            ps.setBoolean(4,e.isCorrect());
            ps.setLong(5, e.getElapsedTime());
            ps.setInt(6, e.getProbId());
            ps.setString(7, act);
            ps.setLong(8, e.getProbElapsed());
            ps.setString(9, activityName);
            ps.setTimestamp(10,new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
            if (ex.getErrorCode() == Settings.duplicateRowError ||ex.getErrorCode() == Settings.keyConstraintViolation )
                ;
            else throw ex;
        }
        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        return -1;

    }
}
