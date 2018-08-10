package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/19/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUserPedagogyParams {

    public static void clearUserPedagogyParams (Connection conn, int studId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "delete from userpedagogyparameters where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static void saveUserPedagogyParams(Connection conn, int studId, String mode, boolean showIntro, int maxtime, int maxprobs, boolean singleTopicMode,
                                              String[] ccss, int topicId, float mastery, int cuId, int lessonId, int overridePedagogyId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement s = null;
        StringBuilder sb = new StringBuilder();
        // put the standards in CSV format to stuff in one db field
        for (int i=0;i<ccss.length;i++)
            sb.append(ccss[i] + ",");
        sb.append(ccss[ccss.length-1]);
        String standards = sb.toString();
        try {
            String q = "insert into userpedagogyparameters (studid,showintro,maxtime,maxprobs,mode,singleTopicMode,ccss,topicId,mastery,cuId,lessonId,overridePedagogy) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, studId);
            s.setBoolean(2, showIntro);
            s.setInt(3, maxtime);
            s.setInt(4, maxprobs);
            s.setString(5, mode);
            s.setBoolean(6, singleTopicMode);
            s.setString(7, standards);
            s.setInt(8, topicId);
            s.setFloat(9, mastery);
            s.setInt(10, cuId);
            s.setInt(11, lessonId);
            s.setInt(12, overridePedagogyId);

            s.execute();

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                ;
            else throw e;
        } finally {

            if (s != null)
                s.close();
        }
    }

    public static void saveUserPedagogyParams(Connection conn, int studId, int maxtime, int maxprobs,
                                              String[] ccss, float mastery, int overridePedagogyId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement s = null;
        StringBuilder sb = new StringBuilder();
        // put the standards in CSV format to stuff in one db field
        for (int i=0;i<ccss.length-1;i++)
            sb.append(ccss[i] + ",");
        sb.append(ccss[ccss.length-1]);
        String standards = sb.toString();
        try {
            String q = "insert into userpedagogyparameters (studid,maxtime,maxprobs,ccss,mastery,overridePedagogy) " +
                    "values (?,?,?,?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, studId);
            s.setInt(2, maxtime);
            s.setInt(3, maxprobs);
            s.setString(4, standards);
            s.setFloat(5, mastery);
            s.setInt(6, overridePedagogyId);

            s.execute();

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                ;
            else throw e;
        } finally {

            if (s != null)
                s.close();
        }
    }


    public static PedagogyParams getPedagogyParams(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select showIntro,maxTime,maxProbs,mode,singleTopicMode, ccss,topicId,mastery,cuId from userpedagogyparameters where studid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boolean showIntro = rs.getBoolean(1);
                int maxTime = rs.getInt(2);   // given in minutes
                int maxProbs = rs.getInt(3);
                String mode = rs.getString(4);
                boolean singTopic = rs.getBoolean(5);
                String ccss = rs.getString(6);
                int topicId = rs.getInt(7);
                float topicMastery = rs.getFloat(8);
                int cuId = rs.getInt(9);


                return new PedagogyParams(studId,showIntro,maxTime,maxProbs,mode,singTopic, ccss, topicId, topicMastery, cuId);
            }
            else return null;

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }
}
