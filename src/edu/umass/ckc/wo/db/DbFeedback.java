package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import edu.umass.ckc.wo.util.SqlQuery;
import java.sql.SQLException;

import edu.umass.ckc.wo.beans.Feedback;;


/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/12/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbFeedback {

    public static void addFeedback(Connection conn, Feedback f) throws SQLException {

        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into teacher_feedback  (teacherId, messageType, priority, message, objectId, timestamp) values (?,?,?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,f.getTeacherId());
            stmt.setString(2,f.getMessageType());
            stmt.setString(3,f.getPriority());
            stmt.setString(4,f.getMessage());
            stmt.setString(5,f.getObjectId());
            stmt.setTimestamp(6,f.getTimestamp());

            stmt.executeUpdate();


        }
        catch (SQLException e) {
            e.printStackTrace();
           }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }


}



