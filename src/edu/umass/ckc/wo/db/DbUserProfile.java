package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.smgr.UserProfile;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 10/3/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUserProfile {
    public static void setValues(Connection conn, int studId, int confidence, int excitement, int interest, int frustration) throws SQLException {
        PreparedStatement s = null;
        try {
            String q = "insert into userprofile (userId, mathConfidence, mathExcitement,  mathInterest, mathFrustration) " +
                    "values (?,?,?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, studId);
            s.setInt(2, confidence);
            s.setInt(3, excitement);
            s.setInt(4, interest);
            s.setInt(5, frustration);
            s.execute();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            throw e;
        } finally {
            s.close();
        }
    }

    public static int deleteProfile (Connection conn, int studId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "delete from userprofile where userid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static UserProfile getProfile(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select confidence,excitement from userprofile where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int confidence=rs.getInt(1);
                int excitement=rs.getInt(2);
                return new UserProfile(studId, confidence, excitement);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }

    }
}
