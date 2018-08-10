package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;


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
 */
public class PerSkillClassSummaryReport extends Report {

    private int maxAbsSkillID;
    private Vector[] skillIDs;
    private String[] skillNames;
    private String[] skillIDs_str;
    private int teacherId;

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {


        String neck = "<table class=\"example sort02 table-autosort:2\" border=1 cellspacing=1 cellpadding=1>\n" +
                "<thead> <tr>\n" +
                "  <th class=\"table-sortable:default\"th> Skill name </th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center># problems</b></th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>% solved in the first attempt</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Avg # of hints requested per problem</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Seconds \"thinking\" out the problem --time to first action (hint or attempt)</th>\n" +
                "  <th class=\"table-sortable:numeric\" align=center>Incorrect attempts per Problem</th>\n" +
                " </tr></thead>\n" +
                "<tbody id='data'>\n";

        ClassInfo cl = DbClass.getClass(conn, classId);
        teacherId = cl.getTeachid();
        String className = getClassName(cl);
        String logTableName = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        this.src.append(generateHeader2("Report 4 - " + className));

        this.src.append("<h3>Detail Problem Info for " + className + "</h3>\n");
        this.src.append("<table width=\"557\"> \n");
        this.src.append("<tr> \n <td bgcolor=#FF0000 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Skills that your students found really hard </td> \n </tr> ");
        this.src.append("<tr> \n <td bgcolor=#FFFF00 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Skills that your students found challenging </td> \n </tr> ");
        this.src.append("</table>");

        this.src.append(neck);

        addNavLinks(classId,cl.getTeachid());


        computeSkillStrings(conn, classId);

        int numProbs = 0;
        int numHints = 0;
        int incAttempts = 0;
        String outputLine = "";
        int local_numHints = 0;
        boolean hintSeen = false;
        boolean solvedFirstAttempt = false;
        int timeToFirstAction = 0;
        int numSolvedFirstAttempt = 0;
        int acumTimeToFirstAction = 0;
        int acumIncAttempts = 0;

        //For each of the semiabstract skills, compute the values
        for (int i = 0; i < skillIDs_str.length; i++) {
            if (skillIDs_str[i] != null) {
                String SQL = "SELECT distinct studid,sessnum,"+logTableName+".problemId, Problem.name, Problem.nickname, " +
                        " "+logTableName+".action, userInput, isCorrect, probElapsed, elapsedTime " +
                        " FROM "+logTableName+", Problem, Hint, Skill " +
                        " WHERE "+logTableName+".problemId = Problem.id " +   //Join Problem and EpiData2/EventLog
                        " AND Hint.skillid = Skill.id " +  //Join Hint and Skill
                        " AND Hint.problemId = Problem.id " +  //Join Hint and Problem
                        " AND studid IN (select id from student where student.trialUser=0 and classid=" + classId + ") " +
                        " AND "+logTableName+".problemid> 0 AND "+logTableName+".problemid<999 " +
                        (isNewLog ? "" : " AND activityname <> 'pretestProblem' and activityname<>'posttestProblem' ") +
                        " AND skillid IN " + this.skillIDs_str[i] +
                        " ORDER by studid, sessnum, elapsedtime  ";

                Statement classSt2 = conn.createStatement();
                ResultSet rs2 = classSt2.executeQuery(SQL);

                numProbs = 0;
                numHints = 0;
                incAttempts = 0;
                numSolvedFirstAttempt = 0;
                acumTimeToFirstAction = 0;
                acumIncAttempts = 0;

                // % solved first attempt<script>insertButtons(2)
                // Avg # of hints requested per problem
                // Seconds \"thinking\" out the problem --time to first attempted action
                //Avg # inc. attempts
                while (rs2.next()) {
                    boolean isCorrect = (rs2.getInt("isCorrect") == 1);
                    int probId = Integer.parseInt(rs2.getString("problemId"));
                    String action = rs2.getString("action");

                    if (action.equalsIgnoreCase("beginProblem")) {
                        local_numHints = 0;
                        hintSeen = false;
                        incAttempts = 0;
                        solvedFirstAttempt = false;
                        timeToFirstAction = 0;
                    } else if (timeToFirstAction == 0) {
                        timeToFirstAction = rs2.getInt("probElapsed");
                    }

                    if (action.equalsIgnoreCase("attempt")) {
                        if ((isCorrect) && (hintSeen == false) && incAttempts == 0)
                            solvedFirstAttempt = true;
                        if (!isCorrect)
                            incAttempts++;
                    }
                    //Counts not only hint requests but also hints accepted when offered
                    if (action.toLowerCase().startsWith("hint")) {
                        hintSeen = true;
                        local_numHints++;
                    }

                    if (action.equalsIgnoreCase("endProblem")) {
                        numProbs++;

                        if (solvedFirstAttempt)
                            numSolvedFirstAttempt++;

                        numHints += local_numHints;
                        acumTimeToFirstAction += timeToFirstAction / 1000.0;
                        acumIncAttempts += incAttempts;
                    }
                }

                double percentFirstAtt = numSolvedFirstAttempt / (double) numProbs;
                double avgNumHints = numHints / (double) numProbs;
                double avgThinkTime = acumTimeToFirstAction / (double) numProbs;
                double avgIncAtt = acumIncAttempts / (double) numProbs;

                // % solved first attempt
                // Avg # of hints requested per problem
                // Seconds \"thinking\" out the problem --time to first attempted action
                //Avg # inc. attempts
                String bgcolor = new String();
                outputLine = "<tr>";

                if (numProbs > 0) {
                    if (percentFirstAtt < 0.20 && avgNumHints >= 1 && avgThinkTime > 20) {
                        bgcolor = new String("#FF0000");
                    } else if (percentFirstAtt < 0.25 && avgNumHints > 0.5 ) {
                        bgcolor = new String("#FFFF00");
                    }

                    outputLine = outputLine.concat(
                            "<td" + " bgcolor=" + bgcolor + ">" + this.skillNames[i] + "</td>" +
                                    "<td" + " bgcolor=" + bgcolor + ">" + numProbs + "</td>" +
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

        this.src.append(foot);
        return this;

    } //report 2

    /**
     * computeSkillStrings
     */
    private void computeSkillStrings(Connection conn, int classId) throws Exception {
        String SQL = "SELECT max(id) as max from semiabstractskill";

        Statement skillSt1 = conn.createStatement();
        ResultSet rs = skillSt1.executeQuery(SQL);

        if (rs.next()) maxAbsSkillID = rs.getInt("max");

        skillIDs = new Vector[maxAbsSkillID + 1];
        skillNames = new String[maxAbsSkillID + 1];
        skillIDs_str = new String[maxAbsSkillID + 1];

        SQL = "SELECT semiabstractskill.id as semiabsID, semiabstractskill.name as name, semiabstractskill.explanation, skill.id as skillid " +
                "from semiabstractskill, Skill where Skill.semiabsskillid=semiabstractskill.id";

        skillSt1 = conn.createStatement();
        rs = skillSt1.executeQuery(SQL);

        while (rs.next()) {
            int semiAbsID = rs.getInt("semiabsID");
            String skillLink = "<a href=\"WoAdmin?action=AdminViewReport&teacherId="+ teacherId + "&classId=" + classId +
                    "&reportId=4&extraParam=" + semiAbsID + "&state=showReport\">" +
                    rs.getString("name") + "</a>";

            this.skillNames[semiAbsID] = skillLink + ": " + rs.getString("explanation");

            if (skillIDs[semiAbsID] == null) {
                skillIDs[semiAbsID] = new Vector();
            }

            skillIDs[semiAbsID].add(new Integer(rs.getInt("skillid")));
        }

        for (int i = 0; i < skillIDs.length; i++) {
            if (skillIDs[i] != null) {
                skillIDs_str[i] = new String("(");

                for (int j = 0; j < skillIDs[i].size(); j++) {
                    //Add a comma to the string before each new element
                    if (j > 0) skillIDs_str[i] = skillIDs_str[i].concat(",");

                    String thisskillID = ((Integer) skillIDs[i].elementAt(j)).
                            toString();
                    skillIDs_str[i] = skillIDs_str[i].concat(thisskillID);
                }
                skillIDs_str[i] = skillIDs_str[i].concat(")");
            }
        }
    }


}
