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
public class DbEmotionResponses {


    public static int saveResponse(Connection conn, String emotion, int level, String explanation,
                                   int sessionId, int studId, String continueMathspring, String reasons,
                                   String goal, String desiredResult, String skipFreq, String skipReason) throws SQLException {
        long now = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            String q = "insert into emotionInterventionResponse (emotion, level, explanation, sessionId, studId,timestamp, " +
                    "continueMathspring,reasons,goal,desiredResult,skipfrequency,skipreason) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            s = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, emotion);
            s.setInt(2, level);
            s.setString(3, explanation);
            s.setInt(4, sessionId);
            s.setInt(5, studId);
            s.setTimestamp(6,new Timestamp(now));
            if (continueMathspring != null)
                s.setString(7,continueMathspring);
            else s.setNull(7,Types.VARCHAR);
            if (reasons != null)
                s.setString(8,reasons);
            else s.setNull(8,Types.VARCHAR);
            if (goal != null)
                s.setString(9,goal);
            else s.setNull(9,Types.VARCHAR);
            if (desiredResult != null)
                s.setString(10,desiredResult);
            else s.setNull(10,Types.VARCHAR);
            if (skipFreq != null)
                s.setString(11,skipFreq);
            else s.setNull(11,Types.VARCHAR);
            if (skipReason != null)
                s.setString(12,skipReason);
            else s.setNull(12,Types.VARCHAR);
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
