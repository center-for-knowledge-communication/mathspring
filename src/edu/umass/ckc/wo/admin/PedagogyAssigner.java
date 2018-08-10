package edu.umass.ckc.wo.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 22, 2008
 * Time: 12:00:01 PM      Will this save
 */
public class PedagogyAssigner {

    /**
     * insert a row into the pedagogyGroup table which assigns the given student to a pedagogy.
     * The pedagogyGroup is a triple <classid, studentid, pedagogyid> .   So, for each class, it
     * can return the list of students that are receiving a given pedagogy.  The job of the PedagogyAssigner
     * is to determine which pedagogy a student should be assigned to.   This is based on a load balancing
     * concept.  It wants to evenly distribute the students into each of the pedagogies the class is using.
     * @param conn
     * @param studId
     * @param classId
     */
    public static int assignPedagogy (Connection conn, int studId, int classId) throws SQLException {
        int pedId = getPedagogyAssignment(conn,classId,studId);
        if (pedId != -1)
            return pedId;
        else {
            pedId = getLeastSubscribed(conn,studId,classId);
            if (pedId != -1)  {
                assignToPedagogy(conn,classId,studId,pedId);
                return pedId;
            }
            return -1;
        }

    }

    /**
     * Finds the pedagogy that has the least number of students in it for the given class.
     * @param conn
     * @param studId
     * @param classId
     * @throws SQLException
     */
    private static int getLeastSubscribed (Connection conn, int studId, int classId) throws SQLException {
        String q =
                "SELECT cp.pedagogyId, (select count(studId) from pedagogygroup pg where pg.classId=cp.classId and pg.pedagogyId=cp.pedagogyId)" +
                        " as nstuds from classpedagogies cp where cp.classId=? order by nstuds asc";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int pedId = rs.getInt(1);
            int nstuds = rs.getInt(2);
            return pedId;
        }
        return -1;
    }

    private static int assignToPedagogy(Connection conn, int classId, int studId, int pedId) throws SQLException {
        String q = "insert into pedagogygroup (classId, studId,pedagogyId) values (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ps.setInt(2,studId);
        ps.setInt(3,pedId);
        return ps.executeUpdate();

    }

    private static int getPedagogyAssignment (Connection conn, int classId, int studId) throws SQLException {
       String q = "SELECT pedagogyId FROM pedagogygroup p where classId=? and studId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ps.setInt(2,studId);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else return -1;
    }
}
