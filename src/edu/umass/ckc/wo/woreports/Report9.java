package edu.umass.ckc.wo.woreports;

import java.sql.*;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: mbaldwin
 */
public class Report9 extends Report {

        private String[][] rowData; // array to store table values
        private final int SECTIONS_PER_PROBLEM = 6; // number of data fields per problem
        private boolean[] probsToReport;  //should change this as it has a max of 200 problems

    public Report9() {
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

      probsToReport = new boolean[200];

      // determine the number of usable problems in Problem
      String SQL = "SELECT count(*) as totalProbs, max(name) as maxProbName FROM Problem WHERE name LIKE '%prob%' ORDER BY name";
      Statement maxProbNumSt = conn.createStatement();
      ResultSet maxProbNumRS = maxProbNumSt.executeQuery(SQL);
      maxProbNumRS.next();
      String maxProbName = maxProbNumRS.getString("maxProbName");
      int numProblems = maxProbNumRS.getInt("totalProbs");
      int maxProb = Integer.parseInt(maxProbName.substring(maxProbName.length() - 3));

      this.src.append("StudId,Name,");
      // write column headers containing problem names
      // retrieve list of problem names
      SQL =
          "SELECT id, name, nickName FROM Problem WHERE name LIKE '%prob%' ORDER BY name";
      Statement probNamesSt = conn.createStatement();
      ResultSet probNamesRS = probNamesSt.executeQuery(SQL);

      while (probNamesRS.next()) {
        String probName = probNamesRS.getString("name");
        String lastThreeDigits = probName.substring(probName.length() - 3);
        int thisProbNum = Integer.parseInt(lastThreeDigits);
        String shortName = new String("p" + lastThreeDigits);
        probsToReport[thisProbNum] = true;
        // write sub-column headers with names of per-problem data

        this.src.append(shortName + "Natt," +
                        shortName + "Hnts," +
                        shortName + "Tatt," +
                        shortName + "Tslv," +
                        shortName + "Thnt," +
                        shortName + "NthP");

        if (probName.endsWith(maxProbName))
          this.src.append("\n");
        else
          this.src.append(",");
      } // End while probnames. End the headers writing

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
        String studentName = (studRS.getString("fname")).trim() + " " +
            (studRS.getString("lname")).trim();

        // append the username if it is not null
        if (studRS.getString("userName") != null)
          studentName += (" [" + studRS.getString("userName") + "]");

        rowData = new String[maxProb][SECTIONS_PER_PROBLEM];

        // write row headers with student names
        this.src.append(studId + ", " + studentName + ", ");

        // retrieve student data per problem
        SQL = "SELECT EpisodicData2.problemId, Problem.name, Problem.nickname, EpisodicData2.action, " +
            "EpisodicData2.userInput, EpisodicData2.isCorrect, EpisodicData2.probElapsed " +
            "FROM EpisodicData2 INNER JOIN Student ON EpisodicData2.studId = Student.id " +
            "INNER JOIN Problem ON EpisodicData2.problemId = Problem.id " +
            "WHERE Student.id = " + Integer.toString(studId) +
            " and Problem.name like 'problem_%' " +
            " and activityname <> 'pretestProblem' and activityname <> 'posttestProblem' " +
            "ORDER by sessnum, elapsedtime";
        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        boolean solvedNoHelp = true;
        int numHints = 0;
        int timeToChoose = 0;
        int timeToAnswer = 0;
        int numAttempts = 0;
        int timeToHint = 0;
        int probSeq = 0;
        boolean repeatedProblem = false ;

        while (rs.next()) { // for each problem

          String gifName = rs.getString("name");
          String action = rs.getString("action");

          // parse the last 3 characters of the problem name
          int probNum = Integer.parseInt(gifName.substring(gifName.length() - 3));

          // calculate all presentable data
          if (action.equalsIgnoreCase("beginProblem")) {
            probSeq++;
            timeToChoose = 0;
            timeToAnswer = 0;
            numAttempts = 0 ;
            timeToHint = 0;
            numHints = 0;
            solvedNoHelp = true;
            repeatedProblem = false ;

            if ( rowData[probNum-1][0] != null || rowData[probNum-1][1] != null || rowData[probNum-1][2] != null ||
                 rowData[probNum-1][3] != null || rowData[probNum-1][4] != null || rowData[probNum-1][5] != null )
              repeatedProblem = true ;

            if (! repeatedProblem) {
              String order = (new Integer(probSeq)).toString();
              rowData[probNum - 1][5] = order; // "Problem sequence"
            }
          }

          if (action.equalsIgnoreCase("attempt")) {
            numAttempts ++ ;
            if ( ( (Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                (timeToAnswer == 0))
              timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));
            if (timeToChoose == 0)
              timeToChoose = Integer.parseInt(rs.getString("probElapsed"));
          }

          if (action.startsWith("hint")) {
            if (timeToHint == 0)
              timeToHint = Integer.parseInt(rs.getString("probElapsed"));
            if ( ( (Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                (timeToHint == 0))
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


            String time_attempt = doubleToString( (double) (timeToChoose /
                (double) 1000.0));
            String time_solve = doubleToString( (double) (timeToAnswer /
                (double) 1000.0));
            String time_hint = doubleToString( (double) (timeToHint /
                (double) 1000.0));

            // store data into temporary array list
            if (! repeatedProblem ) { //if the array doesn't have anything
              if (numHints >= 0 && Double.parseDouble(time_attempt) >= 0 &&
                  Double.parseDouble(time_solve) >= 0 &&
                  Double.parseDouble(time_hint) >= 0) {
                rowData[probNum - 1][0] = (new Integer (numAttempts)).toString() ; // NUMATTEMPTS
                rowData[probNum - 1][1] = Integer.toString(numHints); // "# Hints"
                rowData[probNum - 1][2] = time_attempt; // "T-attempt"
                rowData[probNum - 1][3] = time_solve; // "T-solve"
                rowData[probNum - 1][4] = time_hint; // "T-hint"
              }
            }
          }

        } // per problem while

        // process the row data
        for (int i = 0; i < maxProb; i++) {

          if (probsToReport[i + 1]) { //if the problem exists, then print a column of data for it
            // change null values to empty strings
            for (int j = 0; j < SECTIONS_PER_PROBLEM; j++) {
              if (rowData[i][j] == null) {
                rowData[i][j] = "";
              }
            }
            // write the data row to a row in the table
            this.src.append(rowData[i][0] + ", " +
                            rowData[i][1] + ", " +
                            rowData[i][2] + ", " +
                            rowData[i][3] + ", " +
                            rowData[i][4] + ", " +
                            rowData[i][5]);
            if (i == maxProb - 1)
              this.src.append("\n");
            else
              this.src.append(", ");
          }
        } // per student while
      }
        return this;
    }
}
