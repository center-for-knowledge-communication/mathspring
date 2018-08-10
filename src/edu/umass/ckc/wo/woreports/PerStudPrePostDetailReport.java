package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shows individual pre/post test problem scoring data per student.
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 12:57:04 PM
 * DM 3/16/10 alterred to work in v1/v2 mode
 */
public class PerStudPrePostDetailReport extends Report {

    class ProbResult {
        boolean isCorrect;
        long time;

        ProbResult(boolean correct, long time) {
            isCorrect = correct;
            this.time = time;
        }
    }

    private Map<Integer,ProbResult> allProbs = new Hashtable<Integer,ProbResult>();
    private List<Integer> pids = new ArrayList<Integer>();
    String logTable;

    public PerStudPrePostDetailReport() {
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        logTable = getPrePostEventLogTable(cl);

        this.src.append(generateHeader2("Pretest/Postest Problems data per problem/per student in " + className));

        this.src.append("<h3>Pretest/Postest Problems data per problem/per student in " + className + "</h3>\n");
        addNavLinks(classId,cl.getTeachid());



        createTable(conn,classId);


        this.src.append("</body></html>");
        return this;
    }

    private void getClassPrePostProblems (Connection conn, int classId, List<Integer> probIds, List<String> probNames) throws SQLException {
        ResultSet rs=null;
        PreparedStatement ps=null;
        try {
            String q = "select p.id, p.name from prepostproblem p, prepostproblemtestmap m, preposttest t, class c where m.probId=p.id and m.testId=t.id and t.poolId =c.pretestPoolId and c.id=?";

            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            rs = ps.executeQuery();

            while (rs.next()) {
                int pid= rs.getInt(1);
                String pname = rs.getString(2);
                allProbs.put(pid,new ProbResult(false,-1));  // create entry in the table for each problem
                probIds.add(pid);
                probNames.add(pname);
            }
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    private String getColHeader (Connection conn, int classId) throws SQLException {

        ArrayList pnames = new ArrayList<String>();
        getClassPrePostProblems(conn,classId,pids,pnames);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr><th colspan=\"" + pnames.size()* 2 + "\">Pre-test Questions</th> <th colspan=\"" + (pnames.size()* 2+2) + "\">Post-test Questions</th></tr>\n");
        sb.append("<tr><th>name</th><th>id</th>");
        // make the headers of all problems for the pretest items
        for (int i = 0; i < pnames.size(); i++) {
            String s = pids.get(i) + ":" + pnames.get(i);
            sb.append("\t<th>" + s + "</th><th>time</th>\n");

        }
        // make the headers of all problems for the posttest items
        for (int i = 0; i < pnames.size(); i++) {
            String s = pids.get(i) + ":" + pnames.get(i);
            sb.append("\t<th>" + s + "</th><th>time</th>\n");

        }
        sb.append("</tr>\n");
        return sb.toString();
    }

    private void resetTable () {
        Set<Integer> keys = allProbs.keySet();
        for (int k: keys) {
            allProbs.put(k,new ProbResult(false,-1));
        }
    }

    private void createTable (Connection conn, int classId) throws Exception {
        String headers = "\n<table class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                getColHeader(conn,classId)  +
                "</thead>\n"+
                "<tbody id='data'>\n";
        this.src.append(headers);

        StringBuffer rows = new StringBuffer();
        String q1 = "SELECT id,fname,lname,userName FROM student where student.trialUser=0 and classId=?";
        PreparedStatement ps1 = conn.prepareStatement(q1);
        ps1.setInt(1,classId);
        ResultSet rs1 = ps1.executeQuery();
        // interate through the students in the class generating one HTML table row for each
        while (rs1.next()) {
            int studId = rs1.getInt(1);
            String userName = rs1.getString(2) + " " + rs1.getString(3) + ":" + rs1.getString(4);
            String q2 = "SELECT isCorrect, probElapsed ,problemId FROM "+logTable+", prepostproblem p" +
                " where studId=? and p.id=problemId and activityName='pretestProblem'";
            PreparedStatement ps2 = conn.prepareStatement(q2);
            ps2.setInt(1,studId);
            ResultSet rs2 = ps2.executeQuery();
            // iterate through the problems a student saw and output first 1/2 of HTML table row (pretest results)
            while (rs2.next()) {
                boolean isCorrect = rs2.getBoolean(1);
                long time = rs2.getLong(2);
                int pid = rs2.getInt(3);
                allProbs.put(pid,new ProbResult(isCorrect,time));  // store the results for the problem in the map
            }
            this.src.append("<tr><td>"+userName+ "</td><td>"+studId+"</td>");
            generateTableRow();  // create a row in the HTML table for this students pretest scores
            ps2.close();
            rs2.close();
            resetTable(); // clear the map of this students data
            q2 = "SELECT isCorrect, probElapsed ,problemId FROM "+logTable+", prepostproblem p" +
                " where studId=? and p.id=problemId and activityName='posttestProblem'";
            ps2 = conn.prepareStatement(q2);
            ps2.setInt(1,studId);
            rs2 = ps2.executeQuery();
            // iterate through the problems a student saw and output second 1/2 of HTML table row (posttest results)
            while (rs2.next()) {
                boolean isCorrect = rs2.getBoolean(1);
                long time = rs2.getLong(2);
                int pid = rs2.getInt(3);
                allProbs.put(pid,new ProbResult(isCorrect,time));  // store the results for the problem in the map
            }
            generateTableRow();  // finish the row in the HTML table with this students posttest scores
            this.src.append("</tr>"); // close out the row
            resetTable(); // clear the map of this students data
        }

        this.src.append("</tbody>\n</table>");
    }

    private void generateTableRow() {
        StringBuffer row= new StringBuffer();
        // go through all pretest/posttest ids that are in the pool of tests for the class
        for (int pid: pids) {
            ProbResult r = allProbs.get(pid);
            if (r.time != -1)
                row.append("<td>"+(r.isCorrect?1:0)+"</td><td>"+r.time+"</td>");
            else
                row.append("<td/><td/>");

            }
        this.src.append(row);
        }

}

