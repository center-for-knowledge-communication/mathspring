package edu.umass.ckc.wo.woreports;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Class summary per student.  How many problems and hints each student has seen
 * User: Ivon
 * Date: August, 2006
 * This report shows details of the problems and hints seen by individual students
 * The report might highlight one student
 */
public class PerStudLearningHutActivityReport extends Report {

    int studId = 0;
    String gain_str = "";
    static final int LOW_TIMEINHINT = 7;
    static final int LOW_GUESSTIME = 9;
    static final int HIGH_GUESSTIME = 20;
    static final int HIGH_TIMEINHINT = 15;
    static final int LOW_LEARNING = 5;
    static final int HIGH_LEARNING = 15;
    static final double LOW_HINTSPERPROB = 0.5;
    static final double HIGH_HINTSPERPROB = 1.5;
    private int intgain = 0;
    String logTable;
    boolean isNewLog;

    public PerStudLearningHutActivityReport() {
    }

    public View createReport(Connection conn, int classId, int _studId, String _gain, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        studId = _studId;

        if (_gain != null) {
            gain_str = _gain;
            intgain = (new Double(gain_str.substring(0, gain_str.length() - 1))).intValue();
        }
        return createReport(conn, classId, e, req, resp);
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String neck = "<table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                " <tr>\n" +
                "<th class=\"table-sortable:numeric\">ID</th>\n" +
                "  <th class=\"table-sortable:default\">Student Name</th>\n" +
                "  <th class=\"table-sortable:default\">User Name</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num Sessions</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num Days</th>\n" +
                "  <th class=\"table-sortable:default\">First Day</th>\n" +
                "  <th class=\"table-sortable:default\">Last Day</th>\n" +
                "  <th class=\"table-sortable:numeric\">Total Time in hut(min)</th>\n" +
                "  <th class=\"table-sortable:numeric\">Problems seen</th>\n" +
                "  <th class=\"table-sortable:numeric\">Hints Seen</th>\n" +
                "  <th class=\"table-sortable:numeric\">Tot time in hinted problems (min)</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num probs help abused</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num probs quick guess</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num probs skipped</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num times character clicked</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num times hide character</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num times mute character</th>\n" +
                "  <th class=\"table-sortable:numeric\">FirstAtt_CorrAfterNoHint</th>\n" +
                "  <th class=\"table-sortable:numeric\">FirstAtt_CorrAfterHint</th>\n" +
                "  <th class=\"table-sortable:numeric\">FirstAtt_IncAfterNoHint</th>\n" +
                "  <th class=\"table-sortable:numeric\">FirstAtt_IncAfterHint</th>\n" ;
        neck += " </tr>\n";
        neck += "</thead>\n";
        neck += "<tbody id='data'>\n";

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        logTable = getEventLogTable(cl);
        isNewLog = cl.isNewLog();
        String SQL;
        Statement statement;
        ResultSet rs;

        this.src.append(generateHeader2(">Learning Hut detailed Student Info for - " + className));

        SQL = "SELECT Student.id, Student.fname, Student.lname, username  " +
                "FROM Student " +
                "WHERE student.trialUser=0 and Student.classId = " + Integer.toString(classId) + " " +
                "ORDER BY Student.id;";

        statement = conn.createStatement();
        rs = statement.executeQuery(SQL);

        this.src.append("<h3>Learning Hut detailed Student Info for " + className + "</h3>\n" + neck);
        addNavLinks(classId,cl.getTeachid());


        if (studId > 0)
            this.src.append("<h5> Click on the 'Back' button to go back to your previous report<h5>");

        while (rs.next()) {
            int studId = rs.getInt(1);
            String fname = rs.getString(2);
            String lname = rs.getString(3);
            String uname = rs.getString(4);
            writeStudentData(conn, studId, fname, lname, uname);
        } //each student
        this.src.append("</table>");
        this.src.append(foot);
        return this;
    }

     private void writeStudentData(Connection conn, int studId, String fname, String lname, String uname) throws SQLException {
         this.src.append("\n<tr>");
         this.src.append("<td>"+studId+"</td><td>" + fname + " " + lname + "</td><td>" + uname + "</td>");
         reportSessionInfo(conn, studId); // puts in columns about lstogins/sessions
         reportTimeInHintProbs(conn, studId);
         reportMotivStats(conn,studId);
         reportCharacterStats(conn,studId);
         reportCorrectNoHintProblems(conn,studId) ;
         this.src.append("</tr>");
    }


    private void reportSessionInfo(Connection conn, int studId) throws SQLException {
        String sql = "select beginTime from session where studId=? order by beginTime";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        int count = 0, ndays=1;
        Timestamp earliest = null;
        Timestamp latest = null;
        GregorianCalendar e = new GregorianCalendar();
        GregorianCalendar l = new GregorianCalendar();
        GregorianCalendar lastLoginDay = new GregorianCalendar() ;

        if ( studId == 10345 )
            System.out.println();

        while (rs.next()) {
            count++;
            Timestamp dt = rs.getTimestamp(1);
            if (earliest == null) {
                earliest = dt;
                e.setTime(earliest);
                latest = earliest;
                l.setTime(latest);
                lastLoginDay.setTime(latest) ;
            } else {
                latest = dt;
                l.setTime(latest);
                // if different from last date, increment counter of number of days used tutor
                if (lastLoginDay.get(Calendar.MONTH) != l.get(Calendar.MONTH) ||
                     lastLoginDay.get(Calendar.DAY_OF_MONTH) != l.get(Calendar.DAY_OF_MONTH)||
                     lastLoginDay.get(Calendar.YEAR) != l.get(Calendar.YEAR)) {

                        ndays++;
                        lastLoginDay.setTime(l.getTime()) ;
                }
            }
        }
        if (earliest == null || latest == null) {
            this.src.append("<td>0</td><td>0</td><td>-</td><td>-</td><td>0</td><td>0</td>");
            return;
        }
        long totTutTime = getTutorTime(conn,studId);    // students tot time in tutor hut (in ms)
        int nprobsSeen = getNumProbsSeen(conn,studId);
        this.src.append(
                String.format("<td>%d</td><td>%d</td><td>%d/%d/%d</td><td>%d/%d/%d</td><td>%5.1f</td><td>%d</td>",
                    count, ndays, e.get(Calendar.MONTH), e.get(Calendar.DAY_OF_MONTH), e.get(Calendar.YEAR),
                    l.get(Calendar.MONTH), l.get(Calendar.DAY_OF_MONTH), l.get(Calendar.YEAR), totTutTime/(1000.0*60), nprobsSeen));


    }

    /**
     *  For a given session, find the time of the first beginProblem and subtract from the time of the last endProblem
     */
    private long getSessTime (Connection conn, int sessId) throws SQLException {
        String q1 = "select elapsedTime from "+logTable+" where id = (select min(id) from "+logTable+" where sessNum=? and action='beginProblem')";
        String q2 = "select elapsedTime from "+logTable+" where id = (select max(id) from "+logTable+" where sessNum=? and action='endProblem')";
        PreparedStatement ps1 = conn.prepareStatement(q1);
        PreparedStatement ps2 = conn.prepareStatement(q2);
        ps1.setInt(1,sessId);
        ps2.setInt(1,sessId);
        ResultSet rs1 = ps1.executeQuery();
        long beginTime=0, endTime=0;
        if (rs1.next())
            beginTime = rs1.getLong(1);
        ResultSet rs2 = ps2.executeQuery();
        if (rs2.next())
            endTime = rs2.getLong(1);
        return endTime - beginTime;
    }

    private long getTutorTime (Connection conn, int studId) throws SQLException {
        String q = "select id from session where studId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,studId);
        ResultSet rs = ps.executeQuery();
        int totalTime=0;
        while (rs.next()) {
            int sessNum = rs.getInt(1);
            totalTime += getSessTime(conn,sessNum);
        }
        return totalTime;
    }

    private int getNumProbsSeen (Connection conn, int studId) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select count(*) from studentProblemHistory where studId=? and mode=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            ps.setString(2, Problem.PRACTICE);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    private void reportTimeInHintProbs(Connection conn, int studId) throws SQLException {

        String SQL = "SELECT problemId, action, " +
                "userInput, isCorrect, probElapsed " +
                "FROM "+logTable+" WHERE studId = " + studId + " " +
                "AND problemId> 0 " +
                (isNewLog ? "" : "AND activityname NOT like '%testProblem%' ") +
                "ORDER by sessnum,elapsedTime;";

        PreparedStatement ps = conn.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        int numHints = 0;
        int timeInHintProbs =0;
        while (rs.next()) {

            String action = rs.getString("action");

            if (action.startsWith("hint")) {
                numHints++;
            }

            else if (action.equalsIgnoreCase("endProblem")) {
                if (numHints > 0)
                    timeInHintProbs += rs.getInt("probElapsed");
            }
        }
        this.src.append(String.format("<td>%d</td><td>%5.1f</td>",numHints,timeInHintProbs/(1000.0*60)));

    }



    private void  reportCorrectNoHintProblems (Connection conn, int studId) throws SQLException {

        String SQL = "SELECT problemId, action, " +
                "userInput, isCorrect, probElapsed, problemid " +
                "FROM "+logTable+" WHERE studId = " + studId + " " +
                "AND problemId> 0 " +
                (isNewLog ? "" : "AND activityname NOT like '%testProblem%' ") +
                "ORDER by studid,sessnum,elapsedTime;";

        PreparedStatement ps = conn.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        boolean hintSeen = false;
        int fa_correctAfterNoHint =0;
        int fa_correctAfterHint =0;
        int fa_incorrectAfterNoHint =0;
        int fa_incorrectAfterHint =0;
        int newprob = -1 ;
        boolean skipToNextProblem = false ;

        while (rs.next()) {

            String action = rs.getString("action");
            int isCorrect = rs.getInt("isCorrect") ;

            if ( skipToNextProblem ) {   // we don't need to look at the current problem any more, skip to endProblem
                if ( action.equalsIgnoreCase("endProblem") ) {
                    skipToNextProblem = false ;
                }

            }

            else {
                if (action.startsWith("hint")) {
                    hintSeen=true;
                }

                else if (action.equalsIgnoreCase("attempt") && isCorrect==1) { //correct attempt
                    if (hintSeen) {
                        fa_correctAfterHint ++ ;
                        skipToNextProblem = true ;
                        hintSeen=false ;
                    }
                    else { //no hint
                        fa_correctAfterNoHint ++ ;
                        skipToNextProblem = true ;
                    }
                }
                else if (action.equalsIgnoreCase("attempt") && isCorrect==0) { //incorrect attempt
                    if (hintSeen) {
                        fa_incorrectAfterHint ++ ;
                        skipToNextProblem = true ;
                        hintSeen=false ;
                    }
                    else { //no hint
                        fa_incorrectAfterNoHint ++ ;
                        skipToNextProblem = true ;
                    }
                }
            }
        }

        this.src.append(String.format("<td>%d</td><td>%d</td><td>%d</td><td>%d</td>",fa_correctAfterNoHint,fa_correctAfterHint,
                                                                fa_incorrectAfterNoHint,fa_incorrectAfterHint)) ;

    }


     private void reportCharacterStats(Connection conn, int studId) throws SQLException {
         int nClickChar = getCharacterStat(conn,studId,"ClickCharacter");
         int nMuteChar = getCharacterStat(conn,studId,"MuteCharacter");
         int nHideChar = getCharacterStat(conn,studId,"EliminateCharacter");
         this.src.append(String.format("<td>%d</td><td>%d</td><td>%d</td>",nClickChar,nHideChar,nMuteChar));
     }

    private int getCharacterStat(Connection conn, int studId, String stat) throws SQLException {
        ResultSet rs=null;
        PreparedStatement ps=null;
        try {
            String q = "select count(*) from "+logTable+" where studId=? and action=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            ps.setString(2,stat);
            rs = ps.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            return 0;
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    private int getMotivStat (Connection conn, int studId, int motivValue) throws SQLException {
        ResultSet rs=null;
        PreparedStatement ps=null;
        try {
            String q = "select count(*) from woproperty w where property = 'st.satHutMotivObservations' and objid=? and w.value=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            ps.setInt(2,motivValue);
            rs = ps.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            return 0;
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }

    }

    private void reportMotivStats(Connection conn, int studId) throws SQLException {
        int nProbs_hintAbuse = getMotivStat(conn,studId,0); // 0 value is for hint abuse
        int nProbs_quickGuess = getMotivStat(conn,studId,1); // 1 value is for quick guess
        int nProbs_skipped = getMotivStat(conn,studId,3); // 3 value is for prob skipped
        this.src.append(String.format("<td>%d</td><td>%d</td><td>%d</td>",nProbs_hintAbuse,nProbs_quickGuess,nProbs_skipped));
    }


    private String getClassName(Connection conn, int classId) throws SQLException {
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
        return className;
    }




}