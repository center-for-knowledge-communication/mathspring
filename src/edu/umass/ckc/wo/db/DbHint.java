package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.content.Hint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * An object which is in charge of database mgmt of Problem objects.
 * <p/>
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 12:02:49 PM
 */
public class DbHint extends BaseMgr {
    private static final Logger logger = Logger.getLogger(DbHint.class);



    // rewrote the above method to determine isRoot status of a hint based on solutionpath rather than the isRoot column in
    // the hint table which is error prone.
    public static List<Hint> getHintsForProblem(Connection conn, int probId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select h.id, h.name, h.givesAnswer, h.statementHTML, h.audioResource, h.hoverText, h.order, h.is_root, h.imageURL, h.placement from Hint h " +
                    "where givesAnswer = 0 and h.problemid= ? and h.order is not null order by h.order";
            ps = conn.prepareStatement(q);
            ps.setInt(1, probId);
            rs = ps.executeQuery();
            List<Hint> hints = new ArrayList<Hint>();
            while (rs.next()) {
                int id = rs.getInt("h.id");
                String name = rs.getString("h.name");
                boolean givesAnswer = rs.getBoolean("h.givesAnswer");
                String audio = rs.getString("h.audioResource");
                String hoverText = rs.getString("h.hoverText");
                String stmtHTML = rs.getString("h.statementHTML");
                int order = rs.getInt("h.order");
                boolean isroot = rs.getBoolean("h.is_root");
                String imageURL = rs.getString("h.imageURL"); // DM 1/23/18 added
                int placement = rs.getInt("h.placement"); // DM 1/23/18 DB sets default of 2 on column
                Hint h = new Hint(id, name, probId, givesAnswer, stmtHTML, audio, hoverText, order, placement, imageURL);
                h.setIs_root(isroot);
                hints.add(h);
            }
            return hints;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }

    }

    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            addInSolutionHints(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private static void addInSolutionHints(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select h.id, h.problemId, max(h.order) as position from hint h where h.order is not null group by h.problemId order by h.problemid";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
                int hid = rs.getInt("h.id");
                int position = rs.getInt("position");
                int nextHint = getNextHint(conn,hid);
                if (nextHint != -1) {
                    updateAsLast(conn,nextHint,position+1);
                }
            }

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    private static int updateAsLast(Connection conn, int nextHint, int i) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update hint h set h.order=? where h.id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, i);
            stmt.setInt(2, nextHint);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int getNextHint(Connection conn, int parent) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select p.targetHint, h.givesAnswer from solutionpath p, hint h where p.sourceHint=? and p.targethint=h.id";
            ps = conn.prepareStatement(q);
            ps.setInt(1,parent);
            rs = ps.executeQuery();
            while (rs.next()) {
                int childId = rs.getInt("p.targetHint");
                boolean givesAnswer = rs.getBoolean("h.givesAnswer");
                if (givesAnswer)
                    return childId;
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


}