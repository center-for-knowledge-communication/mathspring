package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.Settings;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 4/8/15
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbCollaborationLogging {

    public static int saveEvent (Connection conn, int studentId, int partnerId , String response, String collabType) throws SQLException {
        long now = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            String q = "insert into collaborationlog (studId, partnerStudId, response, type, timestamp) " +
                    "values (?,?,?,?,?)";
            s = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            s.setInt(1, studentId);
            s.setInt(2, partnerId);
            s.setString(3, response);
            s.setString(4, collabType);
            s.setTimestamp(5,new Timestamp(now));
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
