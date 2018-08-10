package edu.umass.ckc.wo.tutor.intervSel2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/5/14
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterventionState {

    // When stale sessions are cleaned up this is called to get rid of state that intervention selectors have saved into woproperty table
    public static int clearState(Connection conn, int studId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            // The assumption is that all intervention state starts with a string (the class name of the InterventionSelector) preceding the ".".
            // StudentState uses "st." and StudentModels (which should use their own table but may still have some vestiges in woproperty) start with "sm.".
            String q = "delete from woproperty where objid=? and property not like 'st.%' and property not like 'sm.%' and property not like 'wkspcst.%'";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static boolean setRun (Connection conn, int studId, String interventionName) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "insert into RunOnceInterventionLog (studid, name,value) values (?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            stmt.setString(2, interventionName);
            stmt.setString(3, "Shown");
            stmt.execute();
        }
        catch (Exception e) {
            // In case it tries to run a run-once intervention a second time the primary key will be violated but we don't really care
            e.printStackTrace();
            return false;
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
            return true;
        }
    }

    public static boolean hasRun (Connection conn, int studId, String interventionName) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select value from RunOnceInterventionLog where studId=? and name=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            stmt.setString(2, interventionName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }
}
