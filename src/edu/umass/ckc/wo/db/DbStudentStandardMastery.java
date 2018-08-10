package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.studmod.StudentStandardMastery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/26/16
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbStudentStandardMastery {

    public static StudentStandardMastery getMastery (Connection conn, int studId, String standard) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select val, numProbs from studentstandardmastery where studid=? and standard=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            stmt.setString(2, standard);
            rs = stmt.executeQuery();
            if (rs.next()) {
                float v= rs.getFloat(1);
                int probCount = rs.getInt(2);
                return new StudentStandardMastery(studId,standard,v,probCount);
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int updateMastery (Connection conn, int studId, String standard, double val, int probCount) throws SQLException {
        PreparedStatement ps = null;
        try {
            if (standard == null)
                return 0;
            String q = "update studentstandardmastery set val=?, numProbs=? where studId=? and standard=?";
            ps = conn.prepareStatement(q);
            ps.setDouble(1, val);
            ps.setInt(2,probCount);
            ps.setInt(3,studId);
            ps.setString(4, standard);
            int n = ps.executeUpdate();
            return n;
        } finally {
            if (ps != null)
                ps.close();
        }

    }

    public static void insertMastery(Connection conn, int studId, String standard, double val, int probCount) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            if (standard == null)
                return;
            String q = "insert into studentstandardmastery (studId,standard,val,numProbs) values (?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            stmt.setString(2, standard);
            stmt.setDouble(3, val);
            stmt.setInt(4,probCount);
            stmt.execute();
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }
}
