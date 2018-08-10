package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 10:13:17 AM
 *
 */
public class Report1 extends Report {

    public Report1() {
    }

    /**
     * Build up the text of the report by appending to the src StringBuffer
     *
     *
     *
     * @param conn
     * @param studId
     * @param req
     * @param response
     * @throws Exception
     */
    public View createReport(Connection conn, int studId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String neck = "<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>  </th>\n" +
                "  <th>Help?<script>insertButtons(1);</script></th>\n" +
                "  <th># Hints<script>insertButtons(2);</script></th>\n" +
                "  <th>Time to attempt<script>insertButtons(3);</script></th>\n" +
                "  <th>Time to solve<script>insertButtons(4);</script></th>\n" +
                "  <th>Time to hint<script>insertButtons(5);</script></th>\n" +
                "  <th># Incorrect attempts<script>insertButtons(6);</script></th>\n" +
                " </tr>\n" +
                "<tbody id='data'>\n"; // designates the part of the table to be sorted


        String SQL = "SELECT * FROM Student " +
                "WHERE Student.id = " + Integer.toString(studId);

        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        String studentName = "";

        if (rs.next()) {
            studentName = rs.getString("fname") + " " + rs.getString("lname");
            if (rs.getString("userName") != null)
            	studentName += (" [" + rs.getString("userName") + "]");
        }
        else {
            studentName = "some kid";
        }

        SQL = "SELECT EpisodicData2.problemId, Problem.name, Problem.nickname, EpisodicData2.action, " +
                "EpisodicData2.userInput, EpisodicData2.isCorrect, EpisodicData2.probElapsed " +
                "FROM EpisodicData2 INNER JOIN Student ON EpisodicData2.studId = Student.id " +
                "INNER JOIN Problem ON EpisodicData2.problemId = Problem.id " +
                "WHERE Student.id = " + Integer.toString(studId) + " " +
                "ORDER by EpisodicData2.id;";

        classSt1 = conn.createStatement();
        rs = classSt1.executeQuery(SQL);

        this.src.append(generateHeader("Problem Detail - " + studentName));

        this.src.append("<h3>Detail Problem Info for " + studentName + "</h3>\n" + neck);

        this.src.append("<h5>" +
        				"<a href='?action=AdminViewReport&state=chooseReport'>Choose another report</a> | " +
        				"<a href='?action=AdminViewReport&state=chooseClass&reportId=" + e.getReportId() + "'>Choose another class</a> | " +
    					"<a href='?action=AdminViewReport&state=chooseStudent&reportId=" + e.getReportId() + "&classId=" + e.getClassId() + "'>Choose another student</a> | " +
        				"<a href='?action=AdminViewReport&state=showReport&reportId=2&classId=" + e.getClassId() + "&studId=" + e.getStudId() + "'>Skills performance for " + studentName + "</a>" +
        				"</h5>");

        boolean solvedNoHelp = true;
        int numHints = 0;
        int timeToChoose = 0;
        int timeToAnswer = 0;
        int timeToHint = 0;
        int incAttempts = 0;

        while (rs.next()) {
            int probId = Integer.parseInt(rs.getString("problemId"));
            String probName = rs.getString("nickName");
            String gifName = rs.getString("name");
            String action = rs.getString("action");

            if (action.equalsIgnoreCase("beginProblem")) {
                timeToChoose = 0;
                timeToAnswer = 0;
                timeToHint = 0;
                numHints = 0;
                solvedNoHelp = true;
                incAttempts = 0;
            }

            if (action.equalsIgnoreCase("attempt")) {
                if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer == 0))
                    timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));
                if (timeToChoose == 0)
                    timeToChoose = Integer.parseInt(rs.getString("probElapsed"));
                // tally incorrect attempts
                if ((Integer.parseInt(rs.getString("isCorrect"))) == 0)
                    incAttempts++;
            }

            if (action.startsWith("hint")) {
                if (timeToHint == 0)
                    timeToHint = Integer.parseInt(rs.getString("probElapsed"));
                if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToHint == 0))
                    solvedNoHelp = true;
                else
                    solvedNoHelp = false;
                numHints++;
            }

            if (action.equalsIgnoreCase("endProblem")) {
                String str;
                if (solvedNoHelp)
                    str = "No";
                else
                    str = "Yes";

                String link = getLinkURL(gifName, req);

                String time_attempt = doubleToString((double) (timeToChoose / (double) 1000.0));
                String time_solve = doubleToString((double) (timeToAnswer / (double) 1000.0));
                String time_hint = doubleToString((double) (timeToHint / (double) 1000.0));

                this.src.append(" <tr>\n" +
                        "  <td><a href=" + link + "><b>" + probName + "</b></a></td>\n" +
                        "   <td>" + str + "</td>\n" +
                        "   <td>" + Integer.toString(numHints) + "</td>\n" +
                        "   <td>" + time_attempt + "</td>\n" +
                        "   <td>" + time_solve + "</td>\n" +
                        "   <td>" + time_hint + "</td>\n" +
                        "   <td>" + incAttempts + "</td>\n" +
                        " </tr>\n");
            }

        } //while
        this.src.append(foot);
        return this;
    }
}
