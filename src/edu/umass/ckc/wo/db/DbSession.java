package edu.umass.ckc.wo.db;


import edu.umass.ckc.wo.exc.NoSessionException;
import edu.umass.ckc.wo.state.ProblemState;
import edu.umass.ckc.wo.state.ProblemState2;
import edu.umass.ckc.wo.state.TopicState;
import edu.umass.ckc.wo.smgr.Session;
import edu.umass.ckc.wo.state.SessionState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionState;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/6/11
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbSession {
    private static final Logger logger = Logger.getLogger(DbSession.class);


    public static List<Session> getStudentSessions(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            List<Session> sessions = new ArrayList<Session>();
            String q = "select id,beginTime,lastAccessTime,clientType from session where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int sessId = rs.getInt(1);
                Timestamp beginTime = rs.getTimestamp(2);
                Timestamp lastAccessTime = rs.getTimestamp(3);
                String flashClient = rs.getString(4);
                Session s = new Session(sessId, studId, beginTime, lastAccessTime);
                s.setClientType(flashClient);
                sessions.add(s);
            }
            return sessions;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Timestamp getLastLogin(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select max(beginTime) from session where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp c = rs.getTimestamp(1);
                return c;
            } else return null;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public static List<Session> getStudentSessions(Connection conn, int studId, int withLastNDays) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            List<Session> sessions = new ArrayList<Session>();
            String q = "select id,beginTime,lastAccessTime from session where studid=? and lastAccessTime>?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            Date now = new Date();
            Calendar cal;
            cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DATE, withLastNDays * -1); // get a date n days earlier
            long prevTime = cal.getTimeInMillis();
            stmt.setTimestamp(2, new Timestamp(prevTime));
            rs = stmt.executeQuery();
            while (rs.next()) {
                int sessId = rs.getInt(1);
                Timestamp beginTime = rs.getTimestamp(2);
                Timestamp lastAccessTime = rs.getTimestamp(3);
                Session s = new Session(sessId, studId, beginTime, lastAccessTime);
                sessions.add(s);
            }
            return sessions;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static void updateSessionLastAccessTime(Connection conn, int sessionId) throws SQLException {
        PreparedStatement ps = null;
        try {
            String q = "update session set lastAccessTime=? where id=?";
            ps = conn.prepareStatement(q);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, sessionId);
            int n = ps.executeUpdate();

        } finally {
            if (ps != null)
                ps.close();
        }
    }

    /**
     * At the time the sessionMgr is built we extract some commonly used data from the session table and store it in this object
     *
     * @param conn
     * @param sessionId
     * @return an int array with studId in the first position and classId in the second
     * @throws SQLException
     */
    public static int[] setSessionInfo(Connection conn, int sessionId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select sess.studId, stud.classId from Session sess, Student stud  where sess.id=? and sess.isActive=1 and sess.studId=stud.ID";
            ps = conn.prepareStatement(q);
            ps.setInt(1, sessionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int[] res = new int[2];
                res[0] = rs.getInt(1);    // studId
                res[1] = rs.getInt(2);    // classId
                return res;
//                this.curGUIState= rs.getString(2);
            } else throw new NoSessionException(sessionId);
        } finally {
            rs.close();
            ps.close();
        }
    }


    public static int findMaxActiveSession(Connection conn, int studId) throws SQLException {
        String q = "select max(id) from Session where isActive=1 and studId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else
            return -1;
    }


    /* Return the session id. */
    public static int newSession(Connection conn, int studId, long sessBeginTime, boolean isAssistmentsUser) throws Exception {

        String i = "insert into Session (studId, beginTime,isActive, lastAccessTime, endTime, ipAddr, isAssistmentsUser) " +
                "values (?,?,?,?,?,'',?)";
        PreparedStatement ps = conn.prepareStatement(i, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, studId);
        Timestamp now = new Timestamp(sessBeginTime);
        ps.setTimestamp(2, now);
        ps.setInt(3, 1);
        ps.setTimestamp(4, now);
        ps.setNull(5, Types.TIMESTAMP);
        ps.setBoolean(6,isAssistmentsUser);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        else return -1;
    }

    public static int findActiveSession(Connection conn, int studId) throws SQLException {
        String q = "select id from Session where isActive=1 and studId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else
            return -1;
    }


    public static void inactivateSession(Connection conn, int sessionId) throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String s = "update Session set endTime=?, isActive=0 where id=?";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setTimestamp(1, now);
        ps.setInt(2, sessionId);
        ps.executeUpdate();
    }

    // Checks to see if the session has timed out
    public static boolean isStale(Timestamp lastWrite, Timestamp now) {
        return now.getTime() - lastWrite.getTime() >= Settings.sessionIdleTimeout;
    }

    public static boolean isOld(Timestamp lastWrite, Timestamp now) {
        return now.getTime() - lastWrite.getTime() >= Settings.session4MonthsOldTimeout;
    }

    public static boolean isActive(Connection conn, int sessId) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select isActive from session where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, sessId);
              rs = ps.executeQuery();
              if (rs.next()) {
                  int stat = rs.getInt(1);
                  return stat == 1;
              }
              return false;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    public static boolean hasNoActiveSessions (Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select count(*) from session where studId=? and isActive=1";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int nSessions = rs.getInt(1);
                return nSessions == 0;
            }
            return true;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    private static void deleteStudentAndData (Connection conn, int studId) throws SQLException {
            DbUser.deleteStudent(conn, studId);

    }

    private static void cleanOutTestUserData(Connection conn, int sessId, int studId) throws SQLException {
        if (DbUser.isTestUser(conn, studId))
            DbUser.deleteStudentData(conn, studId);

    }

    public static int deleteSession(Connection conn, int sessId) throws SQLException {
        PreparedStatement stmt = null;
        boolean error = false;
        conn.setAutoCommit(false);
        try {
            DbUser.deleteChildRows(conn, "sessionId", sessId, "mrtestdata");
            DbUser.deleteChildRows(conn, "sessionId", sessId, "preposttestdata");
            DbUser.deleteChildRows(conn, "sessionId", sessId, "studentproblemhistory");
            DbUser.deleteChildRows(conn, "sessId", sessId, "flashsessionstate");
            DbUser.deleteChildRows(conn, "sessNum", sessId, "preposteventlog");
            DbUser.deleteChildRows(conn, "sessNum", sessId, "overallhintsinproblems");
            DbUser.deleteChildRows(conn, "sessionNumber", sessId, "preposttestproblems");
            DbUser.deleteChildRows(conn, "sessNum", sessId, "eventlog");
            DbUser.deleteChildRows(conn, "sessNum", sessId, "episodicadventuredata");
            DbUser.deleteChildRows(conn,"sessionId",sessId,"myprogresscomments");
            String q = "delete from session where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, sessId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failure during DbSession.deleteSession for session: " + sessId + " " + e.getMessage());
            error = true;
            conn.rollback();
            System.out.println("Roll back completed");
            throw e;
        } finally {
            if (!error)   {
                conn.commit();
            }
            conn.setAutoCommit(true);
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * Called by the SessionDemon to kill sessions that have become idle.
     * Note: this could just be an update statement
     *
     * @param conn
     * @throws SQLException
     */
    public static void cleanupStaleSessions(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<Integer>();
        String q = "select lastAccessTime, id,isActive,endTime,studId from Session where isActive=1";
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(q);
        long now = System.currentTimeMillis();
        try {
            while (rs.next()) {
                int sessId = rs.getInt(2);
//                logger.debug("testing session for staleness " + sessId);
                Timestamp lastWrite = rs.getTimestamp(1);

                int active = rs.getInt(3);
                int studId = rs.getInt(5);
                // If a Session is stale (18 hours inactive) perform cleanup on it.
                // If a session has been dead for 4 months, clean out the user data.
                if (isStale(lastWrite, new Timestamp(now))) {
                    logger.debug("Session " + sessId + "is stale.  It has been auto-logged off.");
                    ids.add(sessId);
                    rs.updateInt(3, 0);  // make inactive - JDBC 2.0 stuff may not work
                    rs.updateTimestamp(4, new Timestamp(now));
                    rs.updateRow();
                    if (!DbUser.isKeepUser(conn,studId) )
                        deleteStudentAndData(conn, studId);
                    else if (!DbUser.isKeepData(conn,studId))
                        DbUser.deleteStudentData(conn, studId);

                    // blow away student data if the student associated with this session has no other active sessions.
                    if (hasNoActiveSessions(conn,studId))
                        removeTransientState(conn,studId);
                }
                // if a session is 4 months old and is a test user, clean out the data but leave the user
                else if (isOld(lastWrite,new Timestamp(now))) {
                    if (DbUser.isTestUser(conn,studId))
                        DbUser.deleteStudentData(conn,studId);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private static void removeTransientState(Connection conn, int studId) throws SQLException {
        ProblemState.clearState(conn,studId);
        TopicState.clearState(conn, studId);
        SessionState.clearState(conn, studId);
        InterventionState.clearState(conn, studId);
    }

    public static String getClientType(Connection conn, int sessId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select clientType from session where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, sessId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String c = rs.getString(1);
                return c;
            }
            return null;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int setClientType(Connection conn, int sessId, String clientType) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update session set clientType=? where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1, clientType);
            stmt.setInt(2, sessId);
            int nrows = stmt.executeUpdate();
            return nrows;
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    // Adds up the time spent in all sessions.
    public static long getTimeInAllSessions(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select beginTime, endTime from session where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            long totTime=0;
            while (rs.next()) {
                Timestamp beginTime = rs.getTimestamp(1);
                Timestamp endTime = rs.getTimestamp(2);
                if (!rs.wasNull())
                    totTime += endTime.getTime() - beginTime.getTime();
            }
            return totTime;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();

        }
    }

    public static long getSessionBeginTime (Connection conn, int sessId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select beginTime from session where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, sessId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp beginTime = rs.getTimestamp(1);
                return beginTime.getTime();
            }
            return 0;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();

        }
    }

    public static void main(String[] args) {
//        String sessId = args[0];
//        int id = Integer.parseInt(sessId);
//        System.out.println("Deleting session: " + id);
        DbUtil.loadDbDriver();
        Connection conn = null;
        try {
            conn = DbUtil.getAConnection("rose.cs.umass.edu");
             DbSession.deleteSession(conn,52056);
            System.out.println("Deleted stale sessions");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Update the sessions event counter.  Only does it if the provided counter value (c) is one greater than
     * the value we have stored for this session.
     * @param conn
     * @param sessId
     * @param c
     * @param forceOverwrite if true will overwrite the eventCounter even if c is not greater than the last value by 1.
     * @return   On successful update, it returns c.  Will return -1 if it can't find the session and will return the last counter
     * if the new value isn't one greater than the last.
     * @throws SQLException
     */
    public static int updateEventCounter(Connection conn, int sessId, int c, boolean forceOverwrite) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id, eventCounter from session where id=?";
            stmt = conn.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1,sessId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int lastCount= rs.getInt(2);
                if (lastCount + 1 == c || forceOverwrite) {
                    rs.updateInt("eventCounter",c);
                    rs.updateRow();
                    return c;
                }
                else return lastCount;
            }
            return -1;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }
}