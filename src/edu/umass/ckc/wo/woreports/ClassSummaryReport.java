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

/**
 * Class summary: What your students have done overall
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 12:57:04 PM
 *
 */
public class ClassSummaryReport extends Report {

    public ClassSummaryReport() {
    }

    //  N.B.   2/2/10 DM Report converted so that it supports both event logs.   The event names and sequencing
    // are all compatible between the two logs for the purposes of this report.
    // Tested on Steven Blinder class (v1 system).
    // Tested on Henderson/Henderson classes (v2 system)
    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        boolean solvedNoHelp = true;
        int numStud = 0;
        int totProbs = 0;
        int probsNoHelp = 0;
        int probsWithHelp = 0;
        int currStud = 0;
        boolean newProblem = false;
        int numSolvedFirstAtt = 0;

        String neck = "<table cellspacing=1 cellpadding=1 align=center>\n" +
                " <tr>\n" +
                "  <th width=300>Each student saw </b></th>\n" +
                "  <th width=300> Students saw hints on </th>\n" +
                "  <th width=300>Students knew how to solve</th>\n" +
                " </tr>\n" ;

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        String logTableName = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        String SQL = "SELECT "+logTableName+".* " +
                "FROM (Student RIGHT JOIN "+logTableName+"  ON Student.id = "+logTableName+".studId) " +
                "LEFT JOIN Class ON Student.classId = Class.id " +
                "WHERE Student.trialUser=0 AND Class.id = " + Integer.toString(classId) + " " +
                "AND problemid> 0 " +
                (isNewLog ? "" : "AND activityname NOT like '%testProblem%' ") +   
                "ORDER BY Student.id, "+logTableName+".id;";

        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        this.src.append(generateHeader("Class Summary Report " + className));

        this.src.append("<h3>Detail Class Info for " + className + "</h3>\n");
        addNavLinks(classId,cl.getTeachid());



        while (rs.next()) {
            if (currStud != Integer.parseInt(rs.getString("studId"))) { //Create new student
                numStud++;
                currStud = Integer.parseInt(rs.getString("studId"));
            } else { // not new student

                String action = rs.getString("action");

                if (action.equalsIgnoreCase("beginProblem")) {
                    newProblem = true;
                    solvedNoHelp = true;
                }

                else if (action.equalsIgnoreCase("attempt")) {
                	if (newProblem && ((Integer.parseInt(rs.getString("isCorrect"))) == 1)) {
                		numSolvedFirstAtt++;
                	}
                	newProblem = false;
                }

                else if (action.toLowerCase().startsWith("hint")) {
                	newProblem = false;
                    if ((Integer.parseInt(rs.getString("isCorrect"))) == 0)
                        solvedNoHelp = false;
                }

                else if (action.equalsIgnoreCase("endProblem")) {
                    newProblem = false;
                    String str;
                    if ((Integer.parseInt(rs.getString("isCorrect"))) == 1) {
                        if (solvedNoHelp)
                            probsNoHelp++;
                        else
                            probsWithHelp++;
                    }//if
                    totProbs++;
                }
            } //else
        } //while

       if ( numStud == 0 ) {
           this.src.append(" <b> No students from this class saw problems. </b>") ;
           this.src.append(foot);
           return this;
       }

       int probs =  totProbs /  numStud ;
       int probHelp =  probsWithHelp / numStud ;
       int probNoEffort =  numSolvedFirstAtt / numStud  ;
       double percNoEffort = ( probNoEffort / (double) probs ) * 100.0 ;
       double percHelp = ( probHelp / (double) probs ) * 100.0 ;

        this.src.append(neck +
                " <tr>\n" +
                "  <td align=center>" + probs + "</td>\n" +
                "   <td align=center>" + probHelp   + " ( " + ((int) percHelp) + "%) </td>\n" +
                "   <td align=center>" +  probNoEffort  + " ( " + ((int) percNoEffort) + "%) </td>\n" +
                " </tr>\n");

        this.src.append("<tr> <td align=center> problems, on average </td>\n") ;
        this.src.append("     <td align=center> problems, on average </td>\n") ;
        this.src.append("     <td align=center> problems without need for help </td> \n") ;
        this.src.append("</tr> </table>") ;

        int percEffortProbs = (100-((int) percNoEffort)) ;
        int help_underuse = percEffortProbs - ((int) percHelp) ;

        this.src.append("<P align=center> Your students seemed to have trouble in " +
                        percEffortProbs + "% of the problems they saw.");
        this.src.append(" They requested help in " + ((int) percHelp) + "% of the problems.") ;

        if ( help_underuse > 25 )// More than 15% of problems where the students are not using help as they should
            this.src.append(" <b> We think that your students are under-using the help provided. " +
                            " Please make sure they are aware they can click on the help button " +
                            " as many times as they need.</b>") ;
        else
            this.src.append(" <b> We think that your students are using the help properly. </b>") ;

        this.src.append(foot);
        return this;
    }


}
