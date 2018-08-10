package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;

//import edu.umass.ckc.wo.event.admin.AdminActions;
//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class summary per problem.  Which problems are challenging for your students.
 * User: marshall. Date: Mar 2, 2004
 * Modifications: Ivon. August 17, 2006.
 * This report provides summaries PER PROBLEM, for a whole class. It highlights problems that are hard for students in a class.
 * If it takes a semiabsskillid, then it shows the problems that involve a particular skill
 */
public class PerProbClassSummaryReport2 extends Report {

    private int semiabsskillId =-1;
    private enum ProbStats {THINK_TIME, NUM_HINTS, NUM_INCORRECT, NUM_CORRECT_ATTEMPT1};

    public PerProbClassSummaryReport2() {
    }

    public View createReport(Connection conn, int classid, int semiabsskillid, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        semiabsskillId = semiabsskillid ;
        return createReport(conn, classid, e, req, resp) ;
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        Hashtable probHash = new Hashtable();

        int solved = 0;

        int timeToChoose = 0;
        int timeToAnswer = 0;
        int timeToHint = 0;
        int incAttempts = 0;
        String neck = "<table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n   <tr>\n" +
                "  <th class=\"table-sortable:numeric\">Prob ID</th>\n" +
                "  <th class=\"table-sortable:default\">Problem</th>\n" +
                "  <th class=\"table-sortable:numeric\">Number of problems seen</th>\n" +
                "  <th class=\"table-sortable:numeric\">% correct on Attempt 1</th>\n" +
                "  <th class=\"table-sortable:numeric\">Avg. # of Hints</th>\n" +
                "  <th class=\"table-sortable:numeric\">Avg. thinking time</th>\n" +
                "  <th class=\"table-sortable:numeric\">Avg. # incorrect attempts</th>\n" +
                " </tr></thead>\n"+
                "<tbody id='data'>\n";
        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        String table = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();
        String q;
        if (semiabsskillId == -1)
            q= "select * from problem where status='ready'";
//        else q= "select * from problem p, problemskill ps, skill s where p.status='ready' " +
//                "and  ps.problemid= p.id and ps.skillid=s.id and s.semiabsskillid=?";
        else q= "select * from problem where id in (select distinct problemid\n" +
                "from hint h, skill s\n" +
                "where problemid>0 and h.skillid=s.id\n" +
                "and s.semiabsskillid=?\n" +
                "order by problemid)";
        PreparedStatement ps = conn.prepareStatement(q);
        if (semiabsskillId != -1)
            ps.setInt(1,semiabsskillId);
        ResultSet rs = ps.executeQuery();

        this.src.append(generateHeader2("Report 4 - " + className));

        this.src.append("<h3>Detail Problem Info for " + className + "</h3>\n" ) ;
        this.src.append("<table width=\"557\"> \n" ) ;
        this.src.append("<tr> \n <td bgcolor=#FF0000 width=\"45\">&nbsp;</td>\n") ;
        this.src.append("     <td width=\"496\">Problems that your students found really hard </td> \n </tr> ") ;
        this.src.append("<tr> \n <td bgcolor=#FFFF00 width=\"45\">&nbsp;</td>\n") ;
        this.src.append("     <td width=\"496\">Problems that your students found challenging </td> \n </tr> ") ;
        this.src.append("</table>") ;

        this.src.append(neck);

        this.src.append("<h5>" +
    				"<a href='?action=" + AdminActions.SELECT_REPORT + classId +"'>Choose another report</a> | " +
    				"<a href='?action=" + AdminActions.CHOOSE_ANOTHER_CLASS+ "'>Choose another class</a>" +
    				"</h5>");

        while (rs.next()) {
            int pid = rs.getInt("id");
            String probName = rs.getString("nickname");
            String flashProbName = rs.getString("name");

            // get all the endProblem events for the problem where the student spent less that 10 min on it
            String q2 = "select "+ table + ".* from " + table + ", student where problemId=? and student.trialUser=0 and studId=student.id and " +
                    "student.classId=? and probElapsed<600000";
            PreparedStatement ps2 = conn.prepareStatement(q2);
            ps2.setInt(1,pid);
            ps2.setInt(2,classId);
            ResultSet rs2 = ps2.executeQuery();
            int numProbs=0;
            int[] attemptData = gatherProblemData(conn, pid,classId);
            int correctAttempt1= attemptData[ProbStats.NUM_CORRECT_ATTEMPT1.ordinal()];
            int incorrectAttempts= attemptData[ProbStats.NUM_INCORRECT.ordinal()];
            int numHints= attemptData[ProbStats.NUM_HINTS.ordinal()];
            int thinkTime= attemptData[ProbStats.THINK_TIME.ordinal()];
            while (rs2.next()) {
                String action = rs2.getString("action");
                if (action.equalsIgnoreCase("EndProblem"))
                    numProbs++;
            }
            if (numProbs == 0)
                continue;
            String probnumber="null";
            if (probName.length() > 8)
                probnumber= flashProbName.substring(8) ;
            else
                System.out.println("Warning: Problem name " + probName + " is badly formed");
            String link = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + probnumber ;
            //Change the color of the row depending on performance
            ReportProblem rp = new ReportProblem(1);
            String bgcolor = new String("#FFFFFF") ;
            double avgTimeToFirstAction = thinkTime/1000.0/numProbs;
            double avgCorrectAttempt1 = ((double) correctAttempt1)/numProbs;
            double avgNumHints = ((double) numHints) / numProbs;
            if ( avgCorrectAttempt1 < 0.2 && avgTimeToFirstAction>30 && avgNumHints >1)
                bgcolor=new String("#FF0000") ;
            else if ( avgCorrectAttempt1 < 0.25 && avgTimeToFirstAction>20 && avgNumHints >1)
                bgcolor=new String("#FFFF00") ;
            String percCorrectOnAttempt1 = String.format("%5.2f",avgCorrectAttempt1*100.0);
            String avgHints = String.format("%3.1f",avgNumHints);
            String avgIncorrect = String.format("%3.1f",((double) incorrectAttempts) / numProbs);
            String ttime = String.format("%4.1f",avgTimeToFirstAction);
            src.append(
                    "  <td " + "bgcolor=" + bgcolor + " align=center>" + pid + "</td>\n" +
                    "  <td " + "bgcolor=" + bgcolor + "><a href=\"" + link + "\"><b>" + probName + "</b></a></td>\n" +
                    "  <td " + "bgcolor=" + bgcolor + " align=center>" + numProbs + "</td>\n") ;

            src.append("<td " + "bgcolor=" + bgcolor + " align=center>" + percCorrectOnAttempt1 + "</td>\n" ) ;

            src.append("<td " + "bgcolor=" + bgcolor + " align=center>" + avgHints + "</td>\n" +
                    "   <td " + "bgcolor=" + bgcolor + " align=center>" + ttime + "</td>\n" +
                    "   <td " + "bgcolor=" + bgcolor + " align=center>" + avgIncorrect +  "</td>\n" +
                    " </tr>\n");
        }//for

        this.src.append(foot);
        return this;
    }//report 4



    /**
     * returns an array of #of times problem is solved on first attempt,  # of incorrect responses to problem
     * , the number of hints, the total time probElapsed time before either a hint or an attempt
     * @param conn
     * @param pid
     * @param classId
     * @return
     * @throws SQLException
     */
    private int[] gatherProblemData(Connection conn, int pid, int classId) throws SQLException {

        String q = "select * from eventlog where problemId=? and (action='Attempt' or action='Hint') and " +
                "studId in (select id from student where classId=?) order by sessNum, studId, elapsedTime";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,pid);
        ps.setInt(2,classId);
        ResultSet rs = ps.executeQuery();
        int lastSess=0,lastStudId=0,attempt1Correct=0,nIncorrect=0,nHints=0;
        int thinkTime=0;
        while (rs.next()) {
            boolean isCorrect= rs.getBoolean("isCorrect");
            int sess = rs.getInt("sessNum");
            int stud = rs.getInt("studId");

            if (sess!=lastSess && stud!=lastStudId) {
                System.out.println("probElapsed: "+rs.getInt("probElapsed"));
                thinkTime += rs.getInt("probElapsed");
            }
            if (rs.getString("action").equalsIgnoreCase("Hint")) {
                nHints++;
                lastSess=sess;
                lastStudId=stud;
                continue;
            }
            if (isCorrect && sess!=lastSess && stud!=lastStudId)
                attempt1Correct++;
            else if (!isCorrect)
                nIncorrect++;
            lastSess=sess;
            lastStudId=stud;
        }
        System.out.println("totalprobEalspd:" + thinkTime);
        int[] result = new int[4];
        result[ProbStats.NUM_CORRECT_ATTEMPT1.ordinal()]=attempt1Correct;
        result[ProbStats.NUM_HINTS.ordinal()]=nHints;
        result[ProbStats.NUM_INCORRECT.ordinal()]=nIncorrect;
        result[ProbStats.THINK_TIME.ordinal()]=thinkTime;
        return result;
     }

    public static void main(String[] args) {

        PerProbClassSummaryReport2 r = new PerProbClassSummaryReport2();
        DbUtil.loadDbDriver ();
        try {
            Connection conn = DbUtil.getAConnection("cadmium.cs.umass.edu");
            int[] res = r.gatherProblemData(conn,14,214);
            System.out.println("Number hints: " + res[ProbStats.NUM_HINTS.ordinal()]);
            System.out.println("Num correct on attempt 1: " + res[ProbStats.NUM_CORRECT_ATTEMPT1.ordinal()]);
            System.out.println("Num incorrect: " + res[ProbStats.NUM_INCORRECT.ordinal()]);
            System.out.println("Think time: " + res[ProbStats.THINK_TIME.ordinal()]);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}