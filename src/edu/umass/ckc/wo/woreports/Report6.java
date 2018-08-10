package edu.umass.ckc.wo.woreports;

import java.sql.*;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.util.ProbPlayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: mbaldwin
 */
public class Report6 extends Report {

	private String[][] rowData; // array to store table values
	private final int SECTIONS_PER_PROBLEM = 6; // number of data fields per problem
        private boolean[] probsToReport;  //should change this as it has a max of 200 problems

    public Report6() {
    }

    /**
     * Build up the text of the report by appending to the src StringBuffer
     *
     *
     *
     *
     * @param conn
     * @param classId
     * @param req
     * @param response
     * @throws Exception
     */
    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        probsToReport = new boolean[200] ;
        // html page header
        this.src.append("<html>\n" +
                "<head>\n" +
                "<title>Report 6 - Problem Data by Student and Problem" + "</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n");

        // text header
        this.src.append("<h3>Problem Data by Student and Problem</h3>\n");

        // links to other reports or classes
        this.src.append("<h5>" +
    				"<a href='?action=AdminViewReport&state=chooseReport'>Choose another report</a> | " +
    				"<a href='?action=AdminViewReport&state=chooseClass&reportId=" + e.getReportId() + "'>Choose another class</a>" +
    				"</h5>");

        // begin the table
        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n");

        // retrieve list of problem names
        String SQL = "SELECT id, name, nickName FROM Problem WHERE name LIKE '%prob%' ORDER BY name";
        Statement probNamesSt = conn.createStatement();
        ResultSet probNamesRS = probNamesSt.executeQuery(SQL);

        // write column headers containing problem names
        this.src.append("<tr>\n" +
                "  <td></td><td></td>\n");

        // number of unique problems with "problem_###" names in Problem
        int numProblems = 0;

        while (probNamesRS.next()) {
       	    String link = getLinkURL(probNamesRS.getString("name"), req);
            int thisProbNum = Integer.parseInt(probNamesRS.getString("name").substring(probNamesRS.getString("name").length()-3));
            probsToReport[thisProbNum] = true ;
            this.src.append("    <th colspan='" + SECTIONS_PER_PROBLEM + "'>" + "<a href=" + link + ">" + probNamesRS.getString("name") + "</a>" + "</th>\n");
            //numProblems++;
        }

        // determine the number of usable problems in Problem
        SQL = "SELECT count(*) as totalProbs, max(name) as maxProbName FROM Problem WHERE name LIKE '%prob%' ORDER BY name" ;
        Statement maxProbNumSt = conn.createStatement();
        ResultSet maxProbNumRS = maxProbNumSt.executeQuery(SQL);
        maxProbNumRS.next();
        String maxProbName = maxProbNumRS.getString("maxProbName") ;
        numProblems = maxProbNumRS.getInt("totalProbs") ;
        int maxProb = Integer.parseInt(maxProbName.substring(maxProbName.length()-3));

        this.src.append("</tr>\n\n");

        // write sub-column headers with names of per-problem data
        this.src.append("<tr>\n" +
                // student id/name column headers
                "  <td><b>ID</b></td><td><b>Name</b></td>\n");
        for (int i=1; i<=numProblems; i++) {
            this.src.append("  <td><b>#Attempts</b></td>\n" +
                "  <td nowrap='nowrap'><b># Hints</b></td>\n" +
                "  <td nowrap='nowrap'><b>T-attempt</b></td>\n" +
                "  <td nowrap='nowrap'><b>T-solve</b></td>\n" +
                "  <td nowrap='nowrap'><b>T-hint</b></td>\n" +
                "  <td nowrap='nowrap'><b>NthProbSeen</b></td>\n");
        }
        this.src.append("</tr>\n\n" +
        				"<tbody id='data'>\n");

        // retrieve list of students
        SQL = " SELECT distinct Student.id AS studId, Student.fname, Student.lname, Student.userName " +
              " FROM Class, Student, EpisodicData2 " +
              " WHERE Class.id = Student.classId " +
              " AND EpisodicData2.studid = Student.Id " +
              " AND EpisodicData2.activityname = 'satProblem' " +
              " AND Class.id = " + Integer.toString(classId) +
              " ORDER BY studId";
        Statement studSt = conn.createStatement();
        ResultSet studRS = studSt.executeQuery(SQL);

        while (studRS.next()) { // for each student in the class

            int studId = studRS.getInt("studId");
            String studentName = (studRS.getString("fname")).trim() + " " + (studRS.getString("lname")).trim();

            // append the username if it is not null
            if (studRS.getString("userName") != null)
            	studentName += (" [" + studRS.getString("userName") + "]");

            rowData = new String[maxProb+1][SECTIONS_PER_PROBLEM];

            // Write row headers with student names
            this.src.append("<tr>\n " +
                     "<td>" + studId + "</td>\n" +
//                   "<td nowrap='nowrap'>" + studentName + "</td>\n");
                     "<td nowrap='nowrap'>" + "<a href='?action=AdminViewReport&state=showReport&reportId=1&classId="+
                     classId+"&studId=" + studId + "'>" + studentName + "</a>"
                     + "</td>\n");

            // retrieve student data per problem
            SQL = "SELECT EpisodicData2.problemId, Problem.name, Problem.nickname, EpisodicData2.action, " +
                " EpisodicData2.userInput, EpisodicData2.isCorrect, EpisodicData2.probElapsed " +
                " FROM EpisodicData2 INNER JOIN Student ON EpisodicData2.studId = Student.id " +
                " INNER JOIN Problem ON EpisodicData2.problemId = Problem.id " +
                " WHERE Student.id = " + Integer.toString(studId) +
                " and Problem.name like 'problem_%' " +
                " and activityname <> 'pretestProblem' and activityname <> 'posttestProblem' " +
                " ORDER by sessnum, elapsedtime";

            Statement classSt1 = conn.createStatement();
            ResultSet rs = classSt1.executeQuery(SQL);

            boolean solvedNoHelp = true;
            int numHints = 0;
            int timeToChoose = 0;
            int timeToAnswer = 0;
            int timeToHint = 0;
            int probSeq = 0;
            int numAttempts = 0 ;
            boolean repeatedProblem = false ;

            while (rs.next()) { // for each problem
                //int probId = Integer.parseInt(rs.getString("problemId"));
                //String probName = rs.getString("nickName");

                String gifName = rs.getString("name");
                String action = rs.getString("action");

                // parse the last 3 characters of the problem name
                int probNum = Integer.parseInt(gifName.substring(gifName.length()-3));


                // calculate all presentable data
                if (action.equalsIgnoreCase("beginProblem")) {
                    probSeq ++ ;
                    timeToChoose = 0;
                    timeToAnswer = 0;
                    timeToHint = 0;
                    numHints = 0;
                    numAttempts = 0 ;
                    solvedNoHelp = true;
                    repeatedProblem = false ;

                    if ( rowData[probNum][0] != null || rowData[probNum][1] != null || rowData[probNum][2] != null ||
                         rowData[probNum][3] != null || rowData[probNum][4] != null || rowData[probNum][5] != null )
                      repeatedProblem = true ;

                    if ( ! repeatedProblem ) {
                      String order = (new Integer(probSeq)).toString();
                      rowData[probNum][5] = order; // "Problem sequence"
                    }
                }

                if (action.equalsIgnoreCase("attempt")) {
                    numAttempts ++ ;
                    if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer == 0))
                        timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));
                    if (timeToChoose == 0)
                        timeToChoose = Integer.parseInt(rs.getString("probElapsed"));
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

                    String probnumber = gifName.substring(8) ;
                    String link = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + probnumber ;


                    String time_attempt = doubleToString((double) (timeToChoose / (double) 1000.0));
                    String time_solve = doubleToString((double) (timeToAnswer / (double) 1000.0));
                    String time_hint = doubleToString((double) (timeToHint / (double) 1000.0));
                    String probSeqRecorded = rowData[probNum][5];

                    if ( probSeqRecorded == null )
                      System.out.println(probNum) ;

                        // store data into temporary array list
                    if ( ! repeatedProblem ) {
                        //if the array doesn't have anything, and if there are nulls if they don't correspond to another problem seen before
                      if (numHints >= 0 && Double.parseDouble(time_attempt) >= 0 &&
                          Double.parseDouble(time_solve) >= 0 && Double.parseDouble(time_hint) >= 0) {
                        rowData[probNum][0] = (new Integer(numAttempts)).toString(); // Num Attempts
                        rowData[probNum][1] = Integer.toString(numHints); // "# Hints"
                        rowData[probNum][2] = time_attempt; // "T-attempt"
                        rowData[probNum][3] = time_solve; // "T-solve"
                        rowData[probNum][4] = time_hint; // "T-hint"
                      }
                    }
                }

            } // per problem while

            // process the row data
            for (int i=1; i<=maxProb; i++) {

              if (probsToReport[i]) {  //if the problem exists, then print a column of data for it
                // change null values to empty strings
                for (int j = 0; j < SECTIONS_PER_PROBLEM; j++) {
                  if (rowData[i][j] == null) {
                    rowData[i][j] = "";
                  }
                }
                // write the data row to a row in the table
                this.src.append("   <td>" + rowData[i][0] + "</td>\n" +
                                "   <td>" + rowData[i][1] + "</td>\n" +
                                "   <td>" + rowData[i][2] + "</td>\n" +
                                "   <td>" + rowData[i][3] + "</td>\n" +
                                "   <td>" + rowData[i][4] + "</td>\n" +
                                "   <td>" + rowData[i][5] + "</td>\n\n");
              }
            }
            this.src.append("</tr>\n\n");
        } // per student while

        this.src.append(foot);
        return this;
    }

}
