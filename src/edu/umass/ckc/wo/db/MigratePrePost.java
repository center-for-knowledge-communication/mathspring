package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Mar 16, 2010
 * Time: 3:52:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MigratePrePost extends DbUtil {

    public static Connection getAConnection() throws SQLException {
        return getAConnection("cadmium.cs.umass.edu");
    }

    public static void doDbWork(Connection conn) throws SQLException {
        String q = "select studId,sessNum,action,userInput,isCorrect,elapsedTime, probElapsed,problemId,hintStep," +
                "hintid,emotion,activityName from episodicdata2 where studId in (select id from student " +
                "where classId in (197,198,199)) and activityName like '%testProblem'";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int studId = rs.getInt(1);
            int sessNum = rs.getInt(2);
            String action = rs.getString(3);
            String userInput = rs.getString(4);
            boolean isC = rs.getBoolean(5);
            long elapsed = rs.getLong(6);
            long pElapsed = rs.getLong(7);
            int pid = rs.getInt(8);
            String step = rs.getString(9);
            int hid = rs.getInt(10);
            String emot = rs.getString(11);
            String actN = rs.getString(12);

            String q2 = "insert into PrePostEventLog (studId,sessNum,action,userInput,isCorrect,elapsedTime, probElapsed,problemId,activityName) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(q2);
            ps2.setInt(1, studId);
            ps2.setInt(2, sessNum);
            ps2.setString(3, action);
            if (userInput != null)
                ps2.setString(4, userInput);
            else ps2.setNull(4, Types.VARCHAR);
            ps2.setBoolean(5, isC);
            ps2.setLong(6, elapsed);
            ps2.setLong(7, pElapsed);
            ps2.setInt(8, pid);
            ps2.setString(9, actN);

            ps2.executeUpdate();
            System.out.println("Inserting sessId: " + sessNum + " studId: " + studId + " action:" + action);
        }
    }

    public static void main(String[] args) {

        loadDbDriver();
        try {
            Connection conn = getAConnection();
            doDbWork(conn);
            //setClassConfigs(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
