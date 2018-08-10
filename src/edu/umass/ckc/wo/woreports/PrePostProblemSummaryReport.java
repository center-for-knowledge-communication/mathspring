package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Pre/Post test problem summaries per class.
 *  How your class is doing on individual problems in the pretest
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 12:57:04 PM
 * DM 3/16/10 alterred to work in v1/v2 mode
 */
public class PrePostProblemSummaryReport extends Report {
    String logTable;

    public PrePostProblemSummaryReport() {
    }

    protected String getHeader(String title) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n" +
		                "<script src=\"" + WORKING_SORT_URL + "\"></script>\n" +
                        "<script language=\"JavaScript\" src=\"slider.js\"></script>" +
                        "<script language=\"JavaScript\">\n" +
                "        function launchWindow(probId)\n" +
                "        {\n" +
                "            var wx = screen.width / 2 - 400;\n" +
                "            var wy = screen.height / 2 - 300;\n" +
                "            attributes = \"scrollbars=no,resizable=no,width=800,height=600,left=\" + wx + \",top=\" + wy;\n" +
                "            newwin = window.open(\"WoAdmin?action=AdminGetPrePostProblemPreview&probId=\"+probId,\"Wayang_Outpost\",attributes);\n" +
                "            newwin.focus();\n" +
                "        }\n" +
                "    </script>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"table.css\" media=\"all\">\n" +
                        "</head>\n" +
		                "\n" +
		                "<body onLoad='readTable(\"data\");'>";
        return header;
    }


    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        logTable = getPrePostEventLogTable(cl);

        this.src.append(getHeader("Summary Report of Pretest/Postest Problems for " + className));

        this.src.append("<h3>Summary Report of Pretest/Postest Problems for " + className + "</h3>\n");
        addNavLinks(classId,cl.getTeachid());


        this.src.append("<table><tr><td valign='top'>");
        createTable(conn,classId,"pretestProblem");
        this.src.append("<br><br>\n");
//        this.src.append("</td><td valign='top'");
        createTable(conn,classId,"posttestProblem");
        this.src.append("</td></tr></table>");
        this.src.append("</body></html>");
        return this;
    }

    private void createTable (Connection conn, int classId, String testType) throws Exception {
        String headers = "\n<table class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                " <tr>\n" +
                "  <th class=\"table-sortable:default\">"+testType+"</th>\n" +
                "  <th class=\"table-sortable:numeric\">%Correct</th>\n" +
                "  <th class=\"table-sortable:numeric\">#Students</th>\n" +
                " </tr>\n" +
                "</thead>\n"+
                "<tbody id='data'>\n";
        StringBuffer rows = new StringBuffer();

        String SQL = "SELECT count(isCorrect), sum(isCorrect),problemId,p.name FROM "+logTable+", prepostproblem p " +
                "where studId in (SELECT id FROM student where trialUser=0 and classId=?)  and p.id=problemId and " +
                "activityName='" + testType + "' group by problemId";
        PreparedStatement ps = conn.prepareStatement(SQL);
        ps.setInt(1,classId);
        ResultSet rs2 = ps.executeQuery();
        while (rs2.next()) {
            int numShown = rs2.getInt(1);
            int numCorrect = rs2.getInt(2);
            int probId = rs2.getInt(3);
            String probName = probId + ": " + rs2.getString(4);
            //Determine the color of the row

            rows.append(" <tr>\n");
            rows.append(String.format("<td> <a onClick=\"javascript:launchWindow(%d);\" href=\"#\">%s</a></td>\n" +
                                "  <td> %4.1f </td>\n" +
                                "  <td>%d</td>\n",probId,probName,numCorrect*100.0/numShown, numShown));

            // href="#" onClick="javascript:launchWindow();
//            rows.append(String.format("<td> <a href=\"WoAdmin?action=AdminViewReport&classId=%d&reportId=%s&probId=%d" +
//                    "&%s=%s&state=showReport\">%s</a></td>\n" +
//                    "  <td> %4.1f </td>\n" +
//                    "  <td>%d</td>\n",classId,ReportHandler.VIEW_PREPOST_PROBLEM_HTML,probId,AdminViewReportEvent.EXTRAPARAM,testType,probName,numCorrect*100.0/numShown, numShown));
            rows.append("</tr>\n");
        }

        this.src.append(headers);
        this.src.append(rows);
        this.src.append("</tbody>\n</table>");
    }


}