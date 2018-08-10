package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class summary per problem.  Which problems are challenging for your students.
 * User: marshall. Date: Mar 2, 2004
 * Modifications: Ivon. August 17, 2006.
 * This report provides summaries PER PROBLEM, for a whole class. It highlights problems that are hard for students in a class.
 * If it takes a semiabsskillid, then it shows the problems that involve a particular skill
 */
public class PerProbClassSummaryReport extends Report {

    private int semiabsskillId ;

    public PerProbClassSummaryReport() {
    }

    public View createReport(Connection conn, int classid, int semiabsskillid, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        semiabsskillId = semiabsskillid ;
        return createReport(conn, classid, e, req, resp) ;
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        Hashtable probHash = new Hashtable();

        int solved = 0;
        int numHints = 0;
        int timeToChoose = 0;
        int timeToAnswer = 0;
        int timeToHint = 0;
        int incAttempts = 0;

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        String table = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        String SQL = "SELECT "+table+".* " +
            "FROM "+table+", Problem, Hint, Skill, Class, Student " +
            "WHERE "+table+".problemid = problem.id AND Student.trialUser=0 AND Hint.problemid = Problem.id " +
                "and Skill.id = Hint.skillid and class.id=classid AND Student.id = "+table+".studid " ;

        if ( this.semiabsskillId > 0 )
            SQL = SQL.concat("AND semiabsskillid=" + semiabsskillId + " ") ;

        SQL = SQL.concat("AND classid="+classId +  " " +
            "AND "+table+".problemid>0 and "+table+".problemid<999 " +
            (isNewLog ? "" : "AND activityname <> 'pretestProblem' AND activityname <> 'posttestProblem' ") +
            "ORDER BY studid, sessnum, "+table+".elapsedTime") ;

        String neck = "<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>  </th>\n" +
                "  <th>Number of problems seen <script>insertButtons(1);</script></th>\n" +
                "  <th>Percent correct in first attempt <script>insertButtons(2);</script></th>\n" +
                "  <th>Avg. # of Hints<script>insertButtons(3);</script></th>\n" +
                "  <th>Avg. thinking time<script>insertButtons(4);</script></th>\n" +
                "  <th>Avg. # incorrect attempts<script>insertButtons(5);</script></th>\n" +
                " </tr>\n"+
                "<tbody id='data'>\n";

        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        this.src.append(generateHeader("Report 4 - " + className));

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
          if ( rs.getString("problemId") != null ) {
            int probId = Integer.parseInt(rs.getString("problemId"));
            //      String probName = rs.getString(rs.getString("nickName"));
            String action = rs.getString("action");

            if (action.equalsIgnoreCase("beginProblem")) {
              timeToChoose = 0;
              timeToAnswer = 0;
              timeToHint = 0;
              numHints = 0;
              solved = 0;
              incAttempts = 0;
            }

            if (action.equalsIgnoreCase("attempt")) {

              if ( ( (Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                  (timeToAnswer == 0)) {
                timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));

                if ( timeToChoose == 0 )  //this is the first attempt, and it is correct
                  solved = 1 ;
              }
              if (timeToChoose == 0)
                timeToChoose = Integer.parseInt(rs.getString("probElapsed"));

              if ( (Integer.parseInt(rs.getString("isCorrect"))) == 0
                   && incAttempts < 4)  // A hack because somehow students could get more than 4 incorrect answers
                incAttempts++;
            }

            else if (action.toLowerCase().startsWith("hint") ) {
              if (timeToHint == 0)
                timeToHint = Integer.parseInt(rs.getString("probElapsed"));

                if ( numHints < 7 )   // A threshold just in case students make hectick choose_a clicks
                numHints++;
            }

            else if (action.equalsIgnoreCase("endProblem")) {
              int minutesInProb = (Integer.parseInt(rs.getString("probElapsed"))) /
                  60000;

              if (minutesInProb < 10) {
                if (probHash.containsKey(new Integer(probId))) { //existing
                  ( (ReportProblem) probHash.get(new Integer(probId))).seen(
                      timeToChoose,
                      timeToAnswer, timeToHint, numHints, incAttempts+solved);
                }
                else { // new problem
                  ReportProblem rp = new ReportProblem(probId);
                  rp.seen(timeToChoose, timeToAnswer, timeToHint, numHints,incAttempts+solved);
                  probHash.put(new Integer(probId), rp);
                } //else
              }

            }  // end endProblem
          }
        } //while

        Collection hashVal = probHash.values();
        Iterator iter = hashVal.iterator();

        while (iter.hasNext()) {
            ReportProblem rp = (ReportProblem) iter.next();
            Statement classSt2 = conn.createStatement();
            ResultSet rs2 = classSt1.executeQuery("SELECT Problem.name, Problem.nickname FROM Problem WHERE status='ready' and Problem.id = " + rp.getId());
            String gifName = "null";
            String link = "null";
            String probName = "Unknown Problem";
            int timeToFirstAction = 0 ;

            if (rs2.next()) {
                gifName = rs2.getString("name");
                // DAM 11/19/08 patch repair because problems with bad names are included in the result list.
                String probnumber="null";
                if (gifName.length() > 8)
                     probnumber= gifName.substring(8) ;
                else
                    System.out.println("Warning: Problem name " + gifName + " is badly formed");
                link = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + probnumber ;
                probName = rs2.getString("nickname");
            }


            //Change the color of the row depending on performance
            String bgcolor = new String("#FFFFFF") ;
            if ( rp.getAvgSolvedFirstAtt() < 0.2 && rp.avg_timeToFirstAction>30 && rp.avg_numHints >1)
                bgcolor=new String("#FF0000") ;
            else if ( rp.getAvgSolvedFirstAtt() < 0.25 && rp.avg_timeToFirstAction>20 && rp.avg_numHints >1)
                bgcolor=new String("#FFFF00") ;

            this.src.append(
                    "  <td " + "bgcolor=" + bgcolor + "><a href=\"" + link + "\"><b>" + probName + "</b></a></td>\n" +
                    "  <td " + "bgcolor=" + bgcolor + " align=center>" + doubleToString(rp.getTimesSeen()) + "</td>\n" ) ;

            src.append("<td " + "bgcolor=" + bgcolor + " align=center>" + (new Double(rp.getAvgSolvedFirstAtt()*100)).intValue() + "</td>\n" ) ;

            src.append("<td " + "bgcolor=" + bgcolor + " align=center>" + doubleToString(rp.getAvgNumHints()) + "</td>\n" +
                    "   <td " + "bgcolor=" + bgcolor + " align=center>" + doubleToString(rp.getAvgTimeToAction()/1000.0) + "</td>\n" +
                    "   <td " + "bgcolor=" + bgcolor + " align=center>" + doubleToString(rp.getAvgNumIncorrect()) +  "</td>\n" +
                    " </tr>\n");
        }//for

        this.src.append(foot);
        return this;
    }//report 4
}
