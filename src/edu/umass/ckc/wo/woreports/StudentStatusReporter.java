package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/12/11
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentStatusReporter extends TimerTask {

    private Connection conn;
    private String mailServer;

    public StudentStatusReporter(Connection conn, String mailServer) {
        this.conn = conn;
        this.mailServer = mailServer;
    }

    /**
     * Go through all classes that have classconfig.studentEmailIntervalDays > 0 (which means this class
     * is configured so students receive a status report every N days).   Check the class.lastStudentReportSent date
     * and if it's time to send a report, email the students a status report about their activity.
     */
    private void reportClassStatus () throws SQLException, IOException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select c.id, c.lastStudentReportSent, f.studentEmailIntervalDays, f.studentEmailPeriodDays from classconfig f, class c" +
                    " where c.id=f.classId and f.studentEmailIntervalDays > 0";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            while (rs.next()) {
                int classId= rs.getInt(1);
                Timestamp lastReportTime= rs.getTimestamp(2);
                int interval = rs.getInt(3);
                int period = rs.getInt(4);
                // default time is set at 2000-01-01.   If it's that, then we want to send email
                Calendar cal = Calendar.getInstance();
                cal.set(2000,0,2,0,0,0);
                Date d = cal.getTime();
                Timestamp sentinel = new Timestamp(d.getTime());

                if (lastReportTime.before(sentinel))
                    // report on last <period> days of activity
                    new StudentStatusEmail(classId,period).sendEmail(conn,mailServer, false);
                else {
                    long msDiff = now.getTime() - lastReportTime.getTime();
                    long dayDiff = msDiff / (24 * 60 * 60 * 1000);
                    if (dayDiff > interval)
                        // hardwired to report on last 7 days of activity
                        new ClassStatusEmail(classId,period).sendEmail(conn,mailServer);
                }
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    @Override
    public void run() {
        try {
            reportClassStatus();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}