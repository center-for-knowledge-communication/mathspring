package edu.umass.ckc.wo.woreports;

import java.sql.*;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class summary per student.  How many problems and hints each student has seen
 * User: Ivon
 * Date: August, 2006
 * This report shows details of the problems and hints seen by individual students
 * The report might highlight one student
 */
public class PerStudClassSummaryReport extends Report {

    int studId = 0 ;
    String gain_str = "" ;
    static final int LOW_TIMEINHINT = 7 ;
    static final int LOW_GUESSTIME = 9 ;
    static final int HIGH_GUESSTIME = 20 ;
    static final int HIGH_TIMEINHINT = 15 ;
    static final int LOW_LEARNING = 5 ;
    static final int HIGH_LEARNING = 15 ;
    static final double LOW_HINTSPERPROB = 0.5 ;
    static final double HIGH_HINTSPERPROB = 1.5 ;
    private int intgain = 0 ;

    public PerStudClassSummaryReport() {
    }

    public View createReport(Connection conn, int classId, int _studId, String _gain, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp)
                            throws Exception {
        studId = _studId ;

        if ( _gain != null ) {
            gain_str = _gain ;
            intgain = (new Double(gain_str.substring(0,gain_str.length()-1))).intValue();
        }
        return createReport(conn, classId, e, req, resp) ;
    }

    /*
    <table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                " <tr>\n" +
                "<th class=\"table-sortable:numeric\">ID</th>\n" +
                "  <th class=\"table-sortable:default\">Student Name</th>\n" +
                "  <th class=\"table-sortable:default\">User Name</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num Sessions</th>\n" +
                "  <th class=\"table-sortable:numeric\">Num Days</th>\n" +
                "  <th class=\"table-sortable:default\">First Day</th>\n" +
     */

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String neck = "<table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                " <tr>\n" +
                "  <th class=\"table-sortable:default\">Student Name</th>\n" +
                "  <th class=\"table-sortable:default\">User Name</th>\n" +
                "  <th class=\"table-sortable:numeric\">Problems seen</th>\n" +
                "  <th class=\"table-sortable:numeric\"># Hints Seen</th>\n" +
                "  <th class=\"table-sortable:numeric\">Seconds spent reading a hint</th>\n" +
 //               "  <th># Skipped<script>insertButtons(4);</script></th>\n" +
                "  <th class=\"table-sortable:numeric\">Seconds to make a guess</th>\n" ;

            if ( studId > 0 ) {
                neck = neck.concat("  <th class=\"table-sortable:numeric\">Learning Gain</th>\n") ;
            }
            neck = neck.concat(" </tr>\n"+
                               "<tbody id='data'>\n");

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);
        String table = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        this.src.append(generateHeader2("Report 5 - " + className));

        String SQL = "SELECT Student.id, Student.fname, Student.lname, username  " +
                "FROM Student " +
                "WHERE student.trialUser=0 and Student.classId = " + Integer.toString(classId) + " " +
                "ORDER BY Student.id;";

        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        this.src.append("<h3>Detail Student Info for " + className + "</h3>\n" + neck);

        addNavLinks(classId,cl.getTeachid());


        if ( studId > 0 )
            this.src.append("<h5> Click on the 'Back' button to go back to your previous report<h5>") ;

        while (rs.next()) {
            int thisstudent = Integer.parseInt(rs.getString("id"));
            int a_numNoHints = 0;
            int a_numSkipped = 0;
            int a_numProbs = 0;
            int a_TimeToChoose = 0 ;
            int a_TimePerHint = 0 ;

            String SQL2 = "SELECT "+table+".problemId, Problem.name, Problem.nickname, "+table+".action, "
                    +table+".userInput, "+table+".isCorrect, "+table+".probElapsed " +
                    "FROM "+table+" INNER JOIN Student ON "+table+".studId = Student.id " +
                    "INNER JOIN Problem ON "+table+".problemId = Problem.id " +
                    "WHERE Student.id = " + Integer.toString(thisstudent) + " " +
                    "AND problemid> 0 " +
                    (isNewLog ? "" : "AND activityname NOT like '%testProblem%' ") +
                    "ORDER by sessnum,elapsedTime;";

            Statement classSt2 = conn.createStatement();
            ResultSet rs2 = classSt2.executeQuery(SQL2);

//            boolean solvedNoHelp = true;
            int numHints = 0;
            int totalNumHints = 0 ;
            int timeToChoose = 0;
            int timeToAnswer = 0;
            int timeToHint = 0;
            boolean seenEnd=false;
            while (rs2.next()) {

//                int probId = Integer.parseInt(rs2.getString("problemId"));
//                String probName = rs2.getString("nickName");
//                String gifName = rs2.getString("name");
                String action = rs2.getString("action");

                if (action.equalsIgnoreCase("beginProblem")) {
                    timeToChoose = 0;
                    timeToAnswer = 0;
                    timeToHint = 0;
                    numHints = 0;
                    seenEnd = false;
 //                   solvedNoHelp = true;
                }

                else if (action.equalsIgnoreCase("attempt")) {
                    if (((Integer.parseInt(rs2.getString("isCorrect"))) == 1) && (timeToAnswer == 0))
                        timeToAnswer = Integer.parseInt(rs2.getString("probElapsed"));
                    if (timeToChoose == 0)
                        timeToChoose = Integer.parseInt(rs2.getString("probElapsed"));
                }
                //Counts not only hint requests but also hints accepted when offered
                else if (action.toLowerCase().startsWith("hint")) {
                    if (timeToHint == 0)
                        timeToHint = Integer.parseInt(rs2.getString("probElapsed"));
//                    if (((Integer.parseInt(rs2.getString("isCorrect"))) == 1) && (timeToHint == 0))
//                        solvedNoHelp = true;
//                    else
//                        solvedNoHelp = false;
                    numHints++;
                    totalNumHints++ ;
                }

                else if (action.equalsIgnoreCase("endProblem")) {

                    a_numProbs++;
                    if ((numHints == 0) && (timeToAnswer != 0))
                        a_numNoHints++;
                    if (timeToAnswer == 0)
                        a_numSkipped++;

                    a_TimeToChoose += timeToChoose ;
                    // the seenEnd flag is here to prevent a bug that is caused by two endProblem events
                    // in a row with no beginProblem between them.   This will prevent the timePerHint from
                    // getting messed up by these flaws in the EpisodicData log.
                    if ( timeToHint > 0 && !seenEnd)
                        a_TimePerHint += (Integer.parseInt(rs2.getString("probElapsed"))-timeToHint) ;
                    seenEnd = true;
                }

            } //while calculate

            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            String uname = rs.getString("username");

//            double perNoHelp = ((double) a_numNoHints) / ((double) a_numProbs);
//            double perSkipped = ((double) a_numSkipped) / ((double) a_numProbs);

//            if ( studId > 0 && thisstudent == studId ) {//If we have to highlight a student
//                this.src.append("<tr bgcolor=#00FFFF>\n");
//            }

            int timePerHint = 0 ;
            if (totalNumHints > 0 )  timePerHint = (int)((a_TimePerHint / totalNumHints)/1000.0) ;
            int guessingTime = 0 ;
            if ( a_numProbs > 0 )  guessingTime = (int)((a_TimeToChoose / a_numProbs)/1000.0) ;
            if ( studId > 0 && thisstudent == studId )
                getPersonalizedMsgForStudent(fname, lname, a_numProbs, totalNumHints,
                                           timePerHint, a_numSkipped, guessingTime, gain_str) ;
            else this.src.append("   <td>" + fname + " " + lname + "</td>\n");
            this.src.append("   <td>" + uname + "</td>\n");
            this.src.append("   <td>" + a_numProbs + "</td>\n");
            this.src.append("   <td>" + totalNumHints + "</td>\n");

            if ( timePerHint < LOW_TIMEINHINT && totalNumHints > 0)
                this.src.append("   <td bgcolor=#FFFFCC>" + timePerHint + "</td>\n") ;
            else
                this.src.append("   <td>" + timePerHint + "</td>\n") ;

  //          this.src.append("   <td>" + a_numSkipped + "</td>\n");

            if ( guessingTime < this.LOW_GUESSTIME &&  a_numProbs>0 )
                this.src.append("   <td bgcolor=#CCFFFF>" + guessingTime + "</td>\n") ;
            else
                this.src.append("   <td>" + guessingTime + "</td>\n") ;

            if ( studId > 0 ) {//If we have to highlight a student
                if ( thisstudent == studId )
                    this.src.append("   <td>" + gain_str + "</td>\n") ;
                else
                    this.src.append("   <td> </td>\n") ;

            }
            this.src.append("</tr>\n") ;



        } //each student

        printLegend()  ;
        this.src.append(foot);
        return this;
    }  //report 5

    /**
     * getLegend
     *
     * @return Object
     */
    private void printLegend() {
        //Print legend at the bottom
        this.src.append("<table> \n");
        this.src.append(
            "<tr> \n <td bgcolor=#FFFFCC width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td>These students were clicking through hints. </td> \n </tr> ");
        this.src.append(
            "<tr> \n <td bgcolor=#CCFFFF width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td>These students were clicking through answers very fast. </td> \n </tr> ");
        this.src.append("</table>");
    }

    /**
     * getPersonalizedMsgForStudent
     *
     * @param fname String
     * @param lname String
     * @param numProbs int
     * @param totalNumHints int
     * @param timePerHint int
     * @param a_numSkipped int
     * @param guessingTime int
     * @param gain_str String
     */
    private void getPersonalizedMsgForStudent(String fname, String lname,
                                            int numProbs, int totalNumHints,
                                            int timePerHint, int a_numSkipped,
                                            int guessingTime, String gain_str) {
      if (numProbs > 0) {
          double hintsPerProblem = totalNumHints / (double) numProbs;

          String proportionHints = new String(doubleToString(hintsPerProblem) + " hints per problem, which is ");

          //Judge the number of hints per problem that she has got
          if (hintsPerProblem > this.HIGH_HINTSPERPROB)
              proportionHints = proportionHints.concat(" many ");
          else if (hintsPerProblem < this.LOW_HINTSPERPROB) {
              if ( hintsPerProblem < 1 )
                  proportionHints = proportionHints.concat(" very ") ;
              proportionHints = proportionHints.concat(" few ");
          }
          else
              proportionHints = proportionHints.concat(
                  " a reasonable amount of ");

          this.src.append("<td bgcolor=#FFFF00 color=#FF0000> <b>" + fname + " " + lname + " has seen " +
                          proportionHints + " hints per problem.");

          //Judge whether they took advantage of the hints she got.
          String timeInHints_output = new String() ;
          if (timePerHint < this.LOW_TIMEINHINT)  {//Less than seven seconds
              if ( hintsPerProblem > LOW_HINTSPERPROB )
                  timeInHints_output = timeInHints_output.concat(" However, this student " ) ;
              else
                  timeInHints_output = timeInHints_output.concat(" Also, this student ") ;

              timeInHints_output = timeInHints_output.concat(" spent only " + timePerHint +
                      " seconds reading each hint, which is a very short time." );

              if ( intgain > LOW_LEARNING )
                  timeInHints_output = timeInHints_output.concat(" Still, this student did learn, maybe he/she focused on some important hints, while ignoring others that didn't seem useful. " ) ;
              if ( intgain < LOW_LEARNING )
                  timeInHints_output = timeInHints_output.concat(" Students who have this behavior are generally clicking through hints to get to the answer, but not seriously trying to learn. " ) ;
          }

          else if (timePerHint > HIGH_TIMEINHINT)
              timeInHints_output = timeInHints_output.concat(fname + " spent " + timePerHint +
                                               " seconds reading each hint, which means she was very focused when seeing" +
                                               " the explanations given. ") ;
          this.src.append(timeInHints_output) ;

          //Judge whether they clicked through the answers
          String timeToGuess = new String() ;
          if ( guessingTime < this.LOW_GUESSTIME ) {
              timeToGuess = timeToGuess.concat(fname + " spent only " + guessingTime + " seconds reading and thinking the problem before making a guess. ") ;
          }
          else if ( guessingTime > this.HIGH_GUESSTIME ) {
              timeToGuess = timeToGuess.concat(fname + " spent a good " + guessingTime + " seconds thinking the problem out before making a guess.") ;

              if ( timePerHint < this.LOW_TIMEINHINT && intgain<0)
                   timeToGuess = timeToGuess.concat(" Maybe " + fname + " was struggling with the problem but not willing to be helped much. ") ;
          }
          this.src.append(timeToGuess) ;

          if ( guessingTime < LOW_GUESSTIME && timePerHint < this.LOW_TIMEINHINT ) {
              this.src.append(" Students who do this are generally clicking through to get " +
                  " to the right answer, but are not really trying to learn." ) ;
          }
          else if (guessingTime < LOW_GUESSTIME) {
              if ( intgain < LOW_LEARNING )
                  this.src.append(" If the student doesn't pay attention to the problem, he/she will not learn." ) ;
              else
                  this.src.append(" Still, this student did learn, so he/she must have focused on some problems that were more appealing. " ) ;
          }

          //Relate  behavior to learning gain
          if (gain_str!=null && gain_str.endsWith("%")) {
              if (intgain < LOW_LEARNING && (guessingTime < LOW_GUESSTIME ||
                                             timePerHint < this.LOW_TIMEINHINT ||
                                             hintsPerProblem<this.LOW_HINTSPERPROB)) {
                  if ( intgain>0 )
                      this.src.append(" This helps to explain why "+ fname +" only improved " + gain_str );
                  else
                      this.src.append(" This helps to explain why " + fname + " did not learn at all.") ;
              }
              if (intgain > this.HIGH_LEARNING)
                  this.src.append(" This helps to explain why "+ fname + " learned as much as " + gain_str + " Very good indeed!");
          }
          this.src.append("</b></td>") ;
      }
  }
}



















