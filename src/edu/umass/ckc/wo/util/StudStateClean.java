package edu.umass.ckc.wo.util;

import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.state.*;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMotivational;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/16/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudStateClean {

    public static void printNumRows (Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select count(*) from woproperty";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Num rows in woproperty is: " + rs.getInt(1));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    // staging edit 2

    public static void clearAllStudentStates(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            printNumRows(conn);
            String q = "select distinct objid from woproperty order by objid";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            List<Integer> objs = new ArrayList<Integer>();
            while (rs.next()) {
                System.out.println("id:"+rs.getInt(1));
                int t = rs.getInt(1);
                objs.add(t);
            }
            for (int id: objs) {
                System.out.println("Cleaning student: " + id);
                cleanStudentState(conn, id);
                cleanStudentModel(conn,id);
//                BaseStudentModelOld.clearState(conn,id);
            }

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    private static void cleanStudentState(Connection conn, int id) throws SQLException {
        ProblemState.clearState(conn, id);
        TopicState.clearState(conn, id);
        SessionState.clearState(conn, id);
        PrePostState.clearState(conn, id);
        WorkspaceState.clearState(conn, id);
        // new staging code
    }

    private static void cleanStudentModel (Connection conn, int id) throws SQLException {
        StudentModelMotivational sm = new StudentModelMotivational(conn);
        sm.setObjid(id);
        sm.clearTutorHutState();
        AffectStudentModel am = new AffectStudentModel(conn);
        am.clearTutorHutStateOld();

    }

    public static void main(String[] args) {
        try {
            // Dovan adds some code
            int i = 0;
            Connection c= DbUtil.getAConnection("localhost");
            StudStateClean.clearAllStudentStates(c);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
