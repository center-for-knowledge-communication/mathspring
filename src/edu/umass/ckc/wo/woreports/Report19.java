package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

//import edu.umass.ckc.wo.event.admin.AdminActions;
//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.db.DbPrePost;
import edu.umass.ckc.wo.content.PrePostProblemDefn;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.tutor.Settings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 12:57:04 PM
 */
public class Report19 extends Report {

    public Report19() {
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

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

        this.src.append(generateHeader2("Report 15 " + className));

        this.src.append("<h3>Detail Report of Pretest/Postest Item for " + className + "</h3>\n");
        this.src.append("<h5>" +
                "<a href='?action=" + AdminActions.SELECT_REPORT + classId + "'>Choose another report</a> | " +
                "<a href='?action=" + AdminActions.CHOOSE_ANOTHER_CLASS + "'>Choose another class</a>" +
                "</h5>");

        int probId = e.getProbId();
        String testType = e.getExtraParam();
        PrePostProblemDefn p = DbPrePost.getPrePostProblem(conn,probId);
        int ansType = showProblem(conn,classId,probId, testType,p);

        if (ansType == PrePostProblemDefn.MULTIPLE_CHOICE)
            showMultipleChoiceStats(conn,classId,probId, testType,p);
        else showOpenResponseStats(conn,classId,probId, testType, p);
        this.src.append("</body></html>");
        return this;
    }

    private int getNumCorrect (Connection conn, int classId, String testType, int probId) throws SQLException {
        String q = "select count(*) from episodicdata2 where isCorrect=1 and activityName=? and problemId=? and studId in (select id from student where classId=?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1,testType);
        ps.setInt(2,probId);
        ps.setInt(3,classId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    private void showOpenResponseStats(Connection conn, int classId, int probId, String testType, PrePostProblemDefn p) throws SQLException {
        this.src.append("<table class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n");
        this.src.append("<thead>\n");
        this.src.append("<tr><th class=\"table-sortable:default\">Answer</th><th class=\"table-sortable:numeric\">Number of Students</th></tr>\n");
        this.src.append("<tr><td>Correct answer: " + p.getAnswer() + "</td><td>"
                + getNumCorrect(conn,classId,testType,probId) + "</td></tr>\n");
        this.src.append("</thead>\n");
        this.src.append("<tbody>\n");


        String q = "select userInput, count(isCorrect) from episodicdata2 where isCorrect=0 and activityName=? and problemId=? and studId in (select id from student where classId=?) group by userInput";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1,testType);
        ps.setInt(2,probId);
        ps.setInt(3,classId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String ans = rs.getString(1);
            int num = rs.getInt(2);
            this.src.append("<tr><td>" + ans + "</td><td>"
                + num + "</td></tr>\n");
        }
        this.src.append("</tbody>\n");
        this.src.append("</table>\n");
    }

    private char getChoiceLetter (Connection conn, int probId, String answer) throws DeveloperException, SQLException {
        if (isChoiceLetter(conn,probId,answer,'a')) return 'a';
        else if (isChoiceLetter(conn,probId,answer,'b')) return 'b';
        else if (isChoiceLetter(conn,probId,answer,'c')) return 'c';
        else if (isChoiceLetter(conn,probId,answer,'d')) return 'd';
        else if (isChoiceLetter(conn,probId,answer,'e')) return 'e';
        throw new DeveloperException("Pretest Problem " + probId + " must have a multi-choice a-e");
    }

    private boolean isChoiceLetter(Connection conn, int probId, String answer, char c) throws SQLException {
        String q = String.format("select %cChoice, %cURL from prepostproblem where id=?",c,c);
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,probId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String choice = rs.getString(1);
            String url = rs.getString(2);
            return answer.equals(choice) || answer.equals(url);
        }
        return false;
    }


    private void showMultipleChoiceStats(Connection conn, int classId, int probId, String testType, PrePostProblemDefn p) throws SQLException, DeveloperException {
        this.src.append("<table border=1><tr><th>Answer<script>insertButtons(1);</script></th><th>Number of Students<script>insertButtons(2);</script></th></tr>\n");
        this.src.append("<tr><td>Correct answer: " + getChoiceLetter(conn,probId, p.getAnswer()) + "</td><td>"
                + getNumCorrect(conn,classId,testType,probId) + "</td></tr>\n");
        String q = "select userInput, count(isCorrect) from episodicdata2 where isCorrect=0 and activityName=? and problemId=? and studId in (select id from student where classId=?) group by userInput";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1,testType);
        ps.setInt(2,probId);
        ps.setInt(3,classId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String ans = rs.getString(1);
            int num = rs.getInt(2);
            this.src.append("<tr><td>" + getChoiceLetter(conn,probId, ans) + "</td><td>"
                + num + "</td></tr>\n");
        }
        this.src.append("</table>");
    }

    private int showProblem(Connection conn, int classId, int probId, String testType,PrePostProblemDefn p) throws SQLException {
        // I think we can assume that there is a web server on the same machine as the web app?
        String host= Settings.prePostProblemURI;
        String url = host + p.getUrl();
        if (url != null)
            this.src.append("<img src=\"" + url +"\" /><p/>\n");
        String descr = p.getDescr();
        this.src.append(descr + "<p/>\n");
        int ansType = p.getAnsType();
        if (ansType == PrePostProblemDefn.MULTIPLE_CHOICE) {
            String ansA,ansB,ansC,ansD,ansE;
            String ansAURL,ansBURL,ansCURL,ansDURL,ansEURL;
            ansA= p.getaAns();
            if (ansA == null) {
                ansAURL =  p.getaURL();
                if (ansAURL != null)
                    this.src.append("a: <img src=\"" + host +ansAURL + "\"><br/>\n");
            }
            else this.src.append("a: " + ansA + "<br/>\n");
            ansB= p.getbAns();
            if (ansB == null) {
                ansBURL =  p.getbURL();
                if (ansBURL != null)
                    this.src.append("b: <img src=\"" + host +ansBURL + "\"><br/>\n");
            }
            else this.src.append("b: " + ansB + "<br/>\n");
            ansC= p.getcAns();
            if (ansC == null) {
                ansCURL =  p.getcURL();
                if (ansCURL != null)
                    this.src.append("c: <img src=\"" + host +ansCURL + "\"><br/>\n");
            }
            else this.src.append("c: " + ansC + "<br/>\n");
            ansD= p.getdAns();
            if (ansD == null) {
                ansDURL =  p.getdURL();
                if (ansDURL != null)
                    this.src.append("d: <img src=\"" + host + ansDURL + "\"><br/>\n");
            }
            else if (ansD != null) this.src.append("d: " + ansD + "<br/>\n");
            ansE= p.geteAns();
            if (ansE == null) {
                ansEURL =  p.geteURL();
                if (ansEURL != null)
                    this.src.append("e: <img src=\"" + host +ansEURL + "\"><br/>\n");
            }
            else if (ansE != null) this.src.append("e: " + ansE + "<br/>\n");

        }
        return ansType;
    }


}