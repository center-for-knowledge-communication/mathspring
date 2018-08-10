package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Problem Report Across All valid students
 * User: Ivon Date: May 3, 2010
 * This report provides frequency table of incorrect attempts, hints and time spent, for one problem iD
 * that are not skipped with no action. For the time being, this is only done on the EPISODICDATA table.
 */

public class PerProblemDifficultyReport extends Report {

    int[] incattempts ;
    int[] time ;
    int[] hints ;
    private int incMax = 5 ;
    private int timeMax = 1000 ;
    private int hintsMax = 15 ;

    public PerProblemDifficultyReport() {
        time = new int[timeMax] ;
        for (int i=0; i<timeMax; i++) {
            time[i] = 0;
        }

        hints = new int[hintsMax] ;
        for (int i=0; i<hintsMax; i++) {
            hints[i] = 0 ;
        }

        incattempts = new int[incMax] ;
        for (int i=0; i<incMax; i++) {
            incattempts[i] = 0 ;
        }        
    }

    
    public View createReport(Connection conn, int classid, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {
 
        String probNumber_XXX = e.getExtraParam() ;
        String q;
        q = "select * " +
            " from episodicData2, problem, student " +
            " where problem.id=problemid and student.trialUser=0 and student.id=studid and " +
            " animationResource='problem_" + probNumber_XXX + "'" +
            " and activityName not like '%testproblem%' and validStudent=1 and problemid>0" +
            " order by studid,sessnum, elapsedTime";

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        int numObs = 0;
        int skipped = 0 ;

        int currAttempts = 0 ;
        int currHints = 0 ;
        long currTime = 0 ;
        int currIncorrect = 0 ;
        int bottomOut = 0 ;
        int previousRowStudId = -1 ;

        while (rs.next()) {
            int studId = rs.getInt("studid");
            String action = rs.getString("action") ;
            int isCorrect = rs.getInt("isCorrect") ;
            String hintName = rs.getString("hintStep") ;
            long probElapsed = rs.getLong("probElapsed") ; 
            
            if ( action.equals("beginProblem") || previousRowStudId != studId ) {           //initialize everything
                currAttempts = 0 ;
                currHints = 0 ;
                currTime = 0 ;
                currIncorrect = 0 ;
                bottomOut = 0 ;
            }

            if ( action.equals("attempt") ) {
                currAttempts++ ;
                if ( isCorrect == 0)
                    currIncorrect ++ ;
            }

            if ( hintName != null && ! hintName.startsWith("choose")) {
                currHints ++ ;
            }

            if ( hintName != null && hintName.startsWith("choose")) {
                bottomOut = 1 ;      
            }

            if ( action.equals("endProblem")) {           //initialize everything

                currTime = probElapsed ;
                numObs ++ ;
                //if things make sense, problem was not skipped, then count it as a case in the array
                if ( previousRowStudId == studId   //Meaning the studId is still the same
                        && currAttempts > 0 ) {    //Problem wasn't just skipped
                    countInstance(currTime, currHints+bottomOut, currIncorrect) ;
                }
                else
                    skipped ++ ;
            }
            previousRowStudId = studId ;

        }
        emitTables(probNumber_XXX, numObs, skipped);
        return this;
    }

    private void countInstance(long t, int h, int i) {
        int seconds = (int) (t/10000) ;        //5 second intervals
        int oldTimeFreq = time[timeMax-1];
        int oldHintFreq = hints[hintsMax-1];
        int oldIncFreq = incattempts[incMax-1];

        if ( seconds < timeMax )  //won't generate an IndexOutOfBoundsException
            oldTimeFreq = time[seconds] ;
        else
            seconds = timeMax-1 ;

        time[seconds] = oldTimeFreq+1 ;   //update the element at that location

        if ( h < hintsMax )
            oldHintFreq = hints[h] ;
        else
            h = hintsMax-1 ;

        hints[h] = oldHintFreq+1 ;

        if ( i < incMax )
            oldIncFreq = incattempts[i] ;
        else
            i = incMax-1 ;
        
        incattempts[i] = oldIncFreq+1 ;
    }
    

    private void emitTables(String p, int numObs, int skipped) {
        this.src.append("<T1> Details on Student Behavior on problem " + p + "</T1>") ;
        this.src.append("<T2> " + numObs + " observations, " + skipped + " skipped problems </T2>") ;


        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Incorrect Attempts</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsIncorrectAttempts() ;
        this.src.append("</table>");


        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Hints Seen</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsHints() ;
        this.src.append("</table>");


        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Time Spent on The Problem</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsTime() ;
        this.src.append("</table>");
    }





    private void emitRowsIncorrectAttempts() {
        for ( int i=0; i<incMax; i++) {
            int currIncAtt = (incattempts[i] <= 0)? 0 : incattempts[i] ;
            
            this.src.append("<tr> <td>" + i + "</td>" +
                            "     <td>" + currIncAtt + "</td> </tr>" ) ;
        }
    }

    private void emitRowsHints() {
        for ( int i=0; i<hintsMax; i++) {
            int currhints = (hints[i] <= 0)? 0 : hints[i] ;

            this.src.append("<tr> <td>" + i + "</td>" +
                            "     <td>" + currhints + "</td> </tr>" ) ;
        }
    }

    private void emitRowsTime() {
        for ( int i=0; i<timeMax; i++) {
            int currtime = (time[i] <= 0)? 0 : time[i] ;

            this.src.append("<tr> <td>" + i + "</td>" +
                            "     <td>" + currtime + "</td> </tr>" ) ;
        }
    }
}