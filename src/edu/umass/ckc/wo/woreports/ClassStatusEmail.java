package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.email.Emailer;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.smgr.Session;
import edu.umass.ckc.wo.smgr.User;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/6/11
 * Time: 12:11 PM
 * Sends an email to a teacher every N days (configured by teacher in admin tool).
 * Gives them information about how their class is doing and some links to reports for more info
 */

public class ClassStatusEmail {
    private int classId;
    private int nDays;
    private int reportSpanDays=7;
    private List<User> students;
    private List<Integer> sessionMinutes;

    public ClassStatusEmail(int classId, int timeFrameInDays) {
        this.classId = classId;
        this.reportSpanDays = timeFrameInDays;
    }


    public void sendEmail (Connection conn, String mailServer) throws SQLException, IOException {
        updateLastStatusSent(conn,classId);
        StringBuilder sb = new StringBuilder();
        sb.append("This is a quick report on how much your students have used Wayang Outpost in the last " + reportSpanDays + " days.\n\n");
        sb.append("If you want more information about how your students are doing, please visit http://cadmium.cs.umass.edu/woj2/WoAdmin");
        sb.append("\nIf you wish to change the frequency of these reports or stop them altogether, please go to the web site above and" +
                " alter your class settings.\n\n -   The Wayang Outpost Team\n\n");
        getReport(conn,sb);
        User teacher = DbUser.getTeacherEmail(conn,classId);
        Emailer em = new Emailer();
        em.sendEmail(teacher.getEmail(),"noreply@wayangoutpost.com", mailServer,  "Wayang Outpost Class Status Update for teacher "+teacher.getUname(),sb.toString());
        
    }

    private int updateLastStatusSent(Connection conn, int classId) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "update class set lastStatusReport=? where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2,classId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public void getReport (Connection conn, StringBuilder sb) throws SQLException {
        students = DbClass.getClassStudents(conn,classId);
        sessionMinutes = new ArrayList<Integer>(students.size());
        for (User stud: students) {
            List<Session> studSessions = DbSession.getStudentSessions(conn,stud.getId(),this.reportSpanDays);
            Timestamp lastLogin = DbSession.getLastLogin(conn,stud.getId());
            int studMinutes = 0;
            if (studSessions.isEmpty())
                sessionMinutes.add(studMinutes);
            else {
                for (Session sess: studSessions) {
                    Timestamp beg = sess.getBeginTime();
                    Timestamp last = sess.getLastAccessTime();
                    long msDiff = last.getTime() - beg.getTime();
                    long minDiff = msDiff / (1000 * 60);
                    studMinutes += minDiff;
                }
                sessionMinutes.add(studMinutes);
            }
            sb.append("Student id:" + stud.getId() + " username:" + stud.getUname() + " lastname:" + stud.getLname() + " logged in for " + studMinutes + " min.");
            if (lastLogin != null)
                sb.append(" Last logged in: " + lastLogin.toString() + "\n");
        }
    }


    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        Connection conn = null;
        int classId = Integer.parseInt(args[0]);
        int ndays = Integer.parseInt(args[1]);
        StringBuilder sb = new StringBuilder();
        try {
            conn = DbUtil.getAConnection("cadmium.cs.umass.edu");
            ClassStatusEmail r = new ClassStatusEmail(classId, ndays);
            r.getReport(conn,sb);
            System.out.println(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
