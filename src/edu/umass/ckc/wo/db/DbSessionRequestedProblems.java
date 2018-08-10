package edu.umass.ckc.wo.db;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/15/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbSessionRequestedProblems {
    public static void setRequestedProblems(Connection conn, int sessionId, int probId) throws SQLException {
        PreparedStatement s = null;
        try {
            String q = "insert into sessionRequestedProblems (sessionId, probId, position) " +
                    "values (?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, sessionId);
            s.setString(2, Integer.toString(probId));
            s.setString(3, "1");
            s.execute();

        } catch (SQLException e) {

        } finally {

            if (s != null)
                s.close();
        }
    }

    public static String popRequestedProblems (Connection conn, int sessionId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select probId,sessionId,position from sessionRequestedProblems where sessionId=? order by position asc";
            ps = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1,sessionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String probId = rs.getString(1);
                rs.deleteRow();
                return probId;
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
