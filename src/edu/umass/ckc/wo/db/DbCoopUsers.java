package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.assistments.AssistmentSessionData;
import edu.umass.ckc.wo.assistments.CoopUser;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.*;

/**
 * Coop users come from other systems like MARi and Assistments.   We store some information in the db about them.  The db table
 * is called assistmentsUser
 * User: marshall
 * Date: 9/13/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbCoopUsers {

    public static CoopUser getUser(Connection conn, String userId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (userId == null)
            return null;
        try {
            String q = "select token, studId from assistmentsuser where uid=?";
            ps = conn.prepareStatement(q);
            ps.setString(1,userId);
            rs = ps.executeQuery();
            String token = null;
            if (rs.next()) {
                token = rs.getString("token");
                int studId = rs.getInt("studId");
                return new CoopUser(userId,token,studId);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

                        }
    }

    /*
    MARi makes use of the AssistmentUser table.  MARi has two tokens we keep (access_token, session_token).  The
    session_token is what identifies the user and is necessary for all read/write calls to MARi.  We store the
    session_token as the uid and the access_token as the token.   We pretty much never need the access-token after the initial handshake
    with MARi.
     */
    public static CoopUser insertUserInDb(Connection conn, String uid, String token, int studId) throws SQLException {
        PreparedStatement s = null;
        try {
            String q = "insert into assistmentsuser (uid, token, studId) " +
                    "values (?,?,?)";
            s = conn.prepareStatement(q);
            s.setString(1, uid);
            s.setString(2, token);
            s.setInt(3, studId);
            s.execute();
            return new CoopUser(uid,token,studId);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                throw e;
            else throw e;
        } finally {
            if (s != null)
                s.close();
        }
    }


    public static CoopUser getUserFromWayangStudId(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select uid,token from assistmentsuser where studId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            rs = ps.executeQuery();
            String token = null;
            if (rs.next()) {
                String uid = rs.getString("uid");
                token = rs.getString("token");
                return new CoopUser(uid,token,studId);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    // THis will not attempt to insert in the db if any of the required Assistments parameters are absent.
    // TODO at some point we'll need to return an ERROR if they aren't present because logging back to assistments will
    // fail when this db insertion isn't made.
    public static void saveSessionInfo(Connection conn, int sessionNum, String uid, String assignment, String assistment, String problem, String aClass, String logBackURL) throws SQLException {
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            if (uid != null && assignment != null && assistment != null && problem != null && aClass != null) {
                String q = "insert into assistmentsessiondata (uid, sessionId, assignment, assistment, problem, class, logbackURL) " +
                        "values (?,?,?,?,?,?,?)";
                s = conn.prepareStatement(q);
                s.setString(1, uid);
                s.setInt(2, sessionNum);
                s.setString(3, assignment);
                s.setString(4, assistment);
                s.setString(5, problem);
                s.setString(6, aClass);
                if (logBackURL != null)
                    s.setString(7, logBackURL);
                else s.setNull(7,Types.VARCHAR);
                s.execute();
            }

        }  finally {

            if (s != null)
                s.close();
        }
    }

    public static AssistmentSessionData getSessionInfo (Connection conn, int sessionNum) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select uid, assignment, assistment, problem, class,logbackURL from assistmentsessiondata where sessionId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,sessionNum);
            rs = ps.executeQuery();
            if (rs.next()) {
                String assign=rs.getString(2);
                String assist=rs.getString(3);
                String prob = rs.getString(4);
                String uid=rs.getString(1);
                String aclass = rs.getString(5);
                String logbackURL=rs.getString(6);
                boolean b= rs.wasNull();
                return new AssistmentSessionData(assign,assist,prob,uid,aclass,(!b?logbackURL:null));
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    public static int[] getSessionInfo (Connection conn, String user, String problem, String assistment, String assignment) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select sessionId from assistmentsessiondata where uid=? and assignment=? and assistment=? and problem=?";
            ps = conn.prepareStatement(q);
            ps.setString(1,user);
            ps.setString(2,assignment);
            ps.setString(3,assistment);
            ps.setString(4, problem);
            rs = ps.executeQuery();
            if (rs.next()) {
                int sessId= rs.getInt(1);
                int studId = DbUser.getStudentFromSession(conn,sessId);
                return new int[] {sessId, studId};
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }
}
