package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import edu.umass.ckc.wo.util.SqlQuery;
import java.sql.SQLException;

import edu.umass.ckc.wo.smgr.SessionManager;


/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/12/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbStudentComment {

    public static void saveComment(Connection conn, int sessionId, int studentId, int topicId, String studentAction, String comment) throws SQLException {

        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into myProgressComments  (sessionId, studentId, topicId, studentAction, comment) values (?,?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,sessionId);
            stmt.setInt(2,studentId);
            stmt.setInt(3,topicId);
            stmt.setString(4,studentAction);
            stmt.setString(5,comment);

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



