package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.Settings;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 10/17/14
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbGoalResponses {


    public static int saveResponse(Connection conn, String goals, int sessionId, int studId) throws SQLException {
        long now = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            String q = "insert into goalInterventionResponse (goals, sessionId, studId,timestamp) " +
                    "values (?,?,?,?)";
            s = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, goals);
            s.setInt(2, sessionId);
            s.setInt(3, studId);
            s.setTimestamp(4,new Timestamp(now));

            s.execute();
            rs = s.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            return id;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                ;
            else throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (s != null)
                s.close();
        }
        return -1;
    }
}
