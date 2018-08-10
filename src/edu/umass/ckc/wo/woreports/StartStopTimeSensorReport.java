package edu.umass.ckc.wo.woreports;

import java.sql.*;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Start and Stop times of students with associated sensors.
 * User: David G Cooper
 * Date:November 21, 2008
 * This report shows the session for each student in a class
 */
public class StartStopTimeSensorReport extends Report {

    int studId = 0 ;
    String gain_str = "" ;
    static final int LOW_TIMEINHINT = 7 ;
    static final int LOW_GUESSTIME = 9 ;
    static final int HIGH_GUESSTIME = 20 ;
    static final int HIGH_TIMEINHINT = 15 ;
    static final int LOW_LEARNING = 5 ;
    static final int HIGH_LEARNING = 15 ;
    static final double LOW_HINTSPERPROB = 0.5 ;
    static final double HIGH_HINTSPERPROB = 1.5 ;
    private int intgain = 0 ;

    public StartStopTimeSensorReport() {
    }

    public View createReport(Connection conn, int classId, int _studId, String _gain, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp)
                            throws Exception {
        studId = _studId ;

        if ( _gain != null ) {
            gain_str = _gain ;
            intgain = (new Double(gain_str.substring(0,gain_str.length()-1))).intValue();
        }
        return createReport(conn, classId, e, req, resp) ;
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String neck = "<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Session ID</th>\n" +
                "  <th>Student ID<script>insertButtons(1);</script></th>\n" +
                "  <th>Begin Time<script>insertButtons(2);</script></th>\n" +
                "  <th>End Time<script>insertButtons(3);</script></th>\n" +
                "  <th>client Begin Time<script>insertButtons(4);</script></th>\n" +
                "  <th>client Begin Time Millis<script>insertButtons(5);</script></th>\n" +
                "  <th>wrist ID<script>insertButtons(6);</script></th>\n" ;

            
            neck = neck.concat(" </tr>\n"+
                               "<tbody id='data'>\n");

        String SQL = "SELECT * " +
                "FROM Class " +
                "WHERE id = " + Integer.toString(classId);

        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        String className = "";

        if (rs.next())
            className = rs.getString("school") + " - " + rs.getString("name");
        else
            className = "some class";

        this.src.append(generateHeader("Report 13 - " + className));

        SQL = "SELECT Student.id, Student.fname, Student.lname  " +
                "FROM Student " +
                "WHERE Student.classId = " + Integer.toString(classId) + " " +
                "ORDER BY Student.id;";

        classSt1 = conn.createStatement();
        rs = classSt1.executeQuery(SQL);

        this.src.append("<h3>Detail Session Info for " + className + "</h3>\n" + neck);
        ClassInfo cl = DbClass.getClass(conn, classId);
        addNavLinks(classId,cl.getTeachid());


        if ( studId > 0 )
            this.src.append("<h5> Click on the 'Back' button to go back to your previous report<h5>") ;

        while (rs.next()) {
            int thisstudent = Integer.parseInt(rs.getString("id"));
       
            String SQL2 = "SELECT id, studid, beginTime, endTime, " +
                    "clientBeginTime, clientBeginTime_millis, wristID " +
                    "FROM session " +
                    "WHERE studid = " + thisstudent;

            Statement classSt2 = conn.createStatement();
            ResultSet rs2 = classSt2.executeQuery(SQL2);

            this.src.append("<tr>\n") ;

            while (rs2.next()) {

		for (int i = 1; i <= 7; ++i) {
		    String colContents = rs2.getString(i);
		    this.src.append("   <td>" + colContents + "</td>\n");
		}

            } //while calculate


            this.src.append("</tr>\n") ;

        } //each student

        this.src.append(foot);
        return this;
    }  //report 13



}



















