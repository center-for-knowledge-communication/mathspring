package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


/**
 * Class summary Report Per SKILL -- Ivon: August 17, 2006
 * Summarizes student performance per skill, for a full class of students.
 * This report runs very slowly
 *
 * DM 3/16/10 - Tested on Henderson/Henderson v2
 * and Steven Blinder v1
 *
 * Concerns:  Henderson/Henderson does not show any rows with red/yellow highlite because avgHintsPerProb are
 * always less than 1.    Can't be sure numbers are right.
 * 
 * Frank 06-13-2020 issue #106 replace use of probstdmap
 */
public class PerStandardClassSummaryReport extends Report {

    private int maxAbsSkillID;
    private HashMap<Integer,ClusterInfo> clusterData = new HashMap<Integer,ClusterInfo>();
    private int teacherId;
    private int classId;

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {


        String neck = "<table class=\"example sort02 table-autosort:2\" border=1 cellspacing=1 cellpadding=1>\n" +
                "<thead> <tr>\n" +
                "  <th class=\"table-sortable:default\"th> Cluster name </th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center># problems</b></th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>% solved in the first attempt</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Avg # of hints requested per problem</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Seconds \"thinking\" out the problem --time to first action (hint or attempt)</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Incorrect attempts per Problem</th>\n" +
                " </tr></thead>\n" +
                "<tbody id='data'>\n";
        this.classId = classId;
        ClassInfo cl = DbClass.getClass(conn, classId);
        teacherId = cl.getTeachid();
        String className = getClassName(cl);
        String logTableName = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        this.src.append(generateHeader2("Report 4 - " + className));

        this.src.append("<h3>Detail Problem Info for " + className + "</h3>\n");
        this.src.append("<table width=\"557\"> \n");
        this.src.append("<tr> \n <td bgcolor=#FF0000 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Clusters that your students found really hard </td> \n </tr> ");
        this.src.append("<tr> \n <td bgcolor=#FFFF00 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Clusters that your students found challenging </td> \n </tr> ");
        this.src.append("</table>");

        this.src.append(neck);

        addNavLinks(classId,cl.getTeachid());


        getClusterInfo(conn);

        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select h.problemId, h.numHints,h.numMistakes,h.numAttemptsToSolve,h.timeToFirstHint,h.timeToFirstAttempt,std.clusterId from studentproblemhistory h, standard std, problem p where studid in (select id from student where classId = ?) and mode='practice' and p.id=h.problemId and std.id=p.standardID";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int clusterId = rs.getInt("std.clusterId");
                int numHints = rs.getInt("h.numHints");
                int incAttempts = rs.getInt("h.numMistakes");
                int numAttemptsToSolve = rs.getInt("h.numAttemptsToSolve");
                long timeToFirstHint = rs.getLong("h.timeToFirstHint");
                long timeToFirstAttempt = rs.getLong("h.timeToFirstAttempt");
                long thinkingTime=0;
                if (timeToFirstHint > 0 && timeToFirstAttempt > 0)
                    thinkingTime += Math.min(timeToFirstHint, timeToFirstAttempt);
                else   {
                    long mx = Math.max(timeToFirstHint,timeToFirstAttempt) ;
                    if (mx == -1)
                        mx = 0;
                    thinkingTime += mx;
                }
                ClusterInfo ci = clusterData.get(clusterId);
                if (ci != null) {
                    ci.setNumProbs(ci.getNumProbs() + 1);
                    ci.setNumHints(ci.getNumHints() + numHints);
                    ci.setNumMistakes(ci.getNumMistakes() + incAttempts);
                    if (numAttemptsToSolve == 1)
                        ci.setNumSolvedOnAttempt1(ci.getNumSolvedOnAttempt1() + 1);
                    ci.setThinkTime(ci.getThinkTime() + thinkingTime);
                }
            }
            produceRows();
            return this;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    } //report 2

    private void produceRows() {

        Collection<ClusterInfo> clusters = clusterData.values();
        for (ClusterInfo ci: clusters) {

            // % solved first attempt
            // Avg # of hints requested per problem
            // Seconds \"thinking\" out the problem --time to first attempted action
            //Avg # inc. attempts
            String bgcolor = new String();
            String outputLine = "<tr>";

            if (ci.getNumProbs() > 0) {
                double percentFirstAtt = ((double) ci.getNumSolvedOnAttempt1()) / ci.getNumProbs();
                double avgNumHints = ((double) ci.getNumHints()) / ci.getNumProbs();
                double avgThinkTime = ((double) ci.getThinkTime()/1000.0) / ci.getNumProbs();
                double avgIncAtt = ((double) ci.getNumMistakes()) / ci.getNumProbs();

                if (percentFirstAtt < 0.20 && avgNumHints >= 1 && avgThinkTime > 20) {
                    bgcolor = new String("#FF0000");
                } else if (percentFirstAtt < 0.25 && avgNumHints > 0.5 ) {
                    bgcolor = new String("#FFFF00");
                }
                String subReportURL = "WoAdmin?action=AdminViewReport&teacherId=" +teacherId+ "&classId=" +classId+ "&reportId=4&extraParam=cluster:"+ ci.id+ "&state=showReport&mode=clusters";
                outputLine = outputLine.concat(
                        "<td" + " bgcolor=" + bgcolor + ">" +
                                "<a href=\"" +subReportURL+ "\">" + ci.getName() +  "</a>" +
                                "</td>" +
                                "<td" + " bgcolor=" + bgcolor + ">" + ci.getNumProbs() + "</td>" +
                                "<td" + " bgcolor=" + bgcolor + ">" + doubleToString(percentFirstAtt * 100) +
                                "</td>" +
                                "<td" + " bgcolor=" + bgcolor + ">" + doubleToString(avgNumHints) + "</td>" +
                                "<td" + " bgcolor=" + bgcolor + ">" + doubleToString(avgThinkTime) + "</td>" +
                                "<td" + " bgcolor=" + bgcolor + ">" + doubleToString(avgIncAtt) + "</td>" +
                                "</tr>");

                this.src.append(outputLine);
            }
        }
    }


    /**
     * computeSkillStrings
     */
    private void  getClusterInfo (Connection conn) throws Exception {
       ResultSet rs=null;
       PreparedStatement stmt=null;
        List<ClusterInfo> clusters = new ArrayList<ClusterInfo>();
       try {
           String q = "select id,displayName,clusterCCName from cluster";
           stmt = conn.prepareStatement(q);
           rs = stmt.executeQuery();
           while (rs.next()) {
               int id= rs.getInt(1);
               String name= rs.getString(2);
               String descr= rs.getString(3);
               ClusterInfo ci = new ClusterInfo(id,name,descr);
               clusterData.put(id,ci);
           }
       }
       finally {
           if (stmt != null)
               stmt.close();
           if (rs != null)
               rs.close();
       }
    }

    class ClusterInfo {
        int id;
        String name;
        String descr;
        int numProbs;
        int numHints;
        long thinkTime;
        int numSolvedOnAttempt1;
        int numMistakes;


        private ClusterInfo(int id, String name, String descr) {
            this.id=id;
            this.name=name;
            this.descr = descr;
        }

        public String getName () {
            return name + ".  " +  descr;
        }

        public int getNumProbs() {
            return numProbs;
        }

        public void setNumProbs(int numProbs) {
            this.numProbs = numProbs;
        }

        public int getNumHints() {
            return numHints;
        }

        public void setNumHints(int numHints) {
            this.numHints = numHints;
        }

        public long getThinkTime() {
            return thinkTime;
        }

        public void setThinkTime(long thinkTime) {
            this.thinkTime = thinkTime;
        }

        public int getNumSolvedOnAttempt1() {
            return numSolvedOnAttempt1;
        }

        public void setNumSolvedOnAttempt1(int numSolvedOnAttempt1) {
            this.numSolvedOnAttempt1 = numSolvedOnAttempt1;
        }

        public int getNumMistakes() {
            return numMistakes;
        }

        public void setNumMistakes(int numMistakes) {
            this.numMistakes = numMistakes;
        }
    }


}
