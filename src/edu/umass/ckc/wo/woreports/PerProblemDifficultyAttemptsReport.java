package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.chartData.DataSeries;

/**
 * Problem Report Across All valid students
 * User: Ivon Date: May 3, 2010
 * This report provides frequency table of incorrect attempts, hints and time spent, for one problem iD
 * that are not skipped with no action. For the time being, this is only done on the EPISODICDATA table.
 */

public class PerProblemDifficultyAttemptsReport extends Report {

    int[] incattempts ;
    int[] time ;
    int[] hints ;
    private int incMax = 5 ;
    private int timeMax = 150 ;
    private int hintsMax = 8 ;
    String[] xAxisLabels ;
    int numObs = 0;
    int skipped = 0 ;
    int convergenceSlice = 10 ;
    int breakpoint = convergenceSlice ;


    //To get the maximumProblemDifficulty
    boolean allProbs=false ;
    int maxID=0 ;

    public PerProblemDifficultyAttemptsReport() {
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

        //Header to the printout
        System.out.println("Slice, MedianIncAttempts, MedianHints, MedianTime, MeanIncAttempts, MeanHints, MeanTime") ;
        
        createReportUsingDB("wayangNov11DB", conn, e, req) ;
        createReportUsingDB("wayangoutpostdb", conn, e, req) ;

        emitTables(e.getExtraParam(), req) ;
        return this;
    }

    private void createReportUsingDB(String DB, Connection conn, AdminViewReportEvent e, HttpServletRequest req) throws Exception {

        String probNumber_XXX = e.getExtraParam() ;
        String q;
        q = " use " + DB ;

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();

        String allprobsCondition = "animationResource='problem_" + probNumber_XXX + "' " ;
        if (allProbs==true)
            allprobsCondition = "" ; //"id<" + maxID + " " ;


        q = " select * " +
            " from episodicData2, problem, student " +
            " where problem.id=problemid and student.trialUser=0 and student.id=studid and " +
            allprobsCondition +
            " and activityName not like '%testproblem%' and validStudent=1 and problemid>0" +
            " order by studid,sessnum, elapsedTime";

        ps = conn.prepareStatement(q);
        rs = ps.executeQuery();

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
    }

    private void countInstance(long t, int h, int i) {
        int seconds = (int) (t/5000) ;        //5 second intervals
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

        if ( numObs-skipped == breakpoint ) {
            reportConvergenceChartCase() ;
        }
    }


    private void reportConvergenceChartCase() {
        int medianCaseNumber = breakpoint / 2 ;
        int medianIncAttempts = -1 ;
        int medianHints=-1 ;
        int medianTime=-1;
        double count=0;
        int acum=0 ;

        //Find the median incorrect attempts
        count=0; acum=0 ;
        for (int i=0; i<this.incMax;  i++) {
            count += incattempts[i] ;
            acum += incattempts[i]*i ;

            if (count>=medianCaseNumber && medianIncAttempts==-1)
                medianIncAttempts = i ;
        }
        double meanIncAttempts = acum / count ;

        //Find the median hints
        count=0; acum=0 ;
        for (int i=0; i<this.hintsMax; i++) {
            count += hints[i] ;
            acum += hints[i]*i ;

            if (count>=medianCaseNumber && medianHints==-1)
                medianHints = i ;
        }
        double meanHints = acum / count ;

        //Find the median incorrect attempts
        count=0; acum=0 ;
        for (int i=0; i<timeMax; i++) {
            count += time[i] ;
            acum += time[i]*i ;

            if (count>=medianCaseNumber && medianTime==-1)
                medianTime = i ;
        }
        double meanTime = acum / count ;
        meanTime *= 5 ;


        System.out.println(breakpoint + "," + medianIncAttempts + "," + medianHints + "," + medianTime + "," +
                meanIncAttempts + "," + meanHints + "," + meanTime) ;

        breakpoint += convergenceSlice ;
    }


    private void emitTables(String p, HttpServletRequest req) {
        this.src.append("<T1> Details on Student Behavior on problem " + p + "</T1>") ;
        this.src.append("<T2> " + numObs + " observations, " + skipped + " skipped problems </T2>") ;


        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Incorrect Attempts</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsIncorrectAttempts() ;
        this.src.append("</table>");
//Create histogram for Inc Attempts
        buildBarChartData("Frequency of Incorrect Attempts","Incorrect attempts","Frequency",req, incattempts) ;
        this.src.append("<p><img src=\"BarChartServletIvon\">"); // inserts an image that calls BarChartServletIvon which gens a jpeg from data in HttpSession


        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Hints Seen</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsHints() ;
        this.src.append("</table>");
//Create histogram for Hints
//        buildBarChartData("Frequency of Hints","Hints seen","Frequency",req, hints) ;
//        this.src.append("<p><img src=\"BarChartServlet\">"); // inserts an image that calls BarChartServlet which gens a jpeg from data in HttpSession



        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Time Spent on The Problem</th>" +
                "  <th>Frequency</th>" +
                " </tr>\n");

        emitRowsTime() ;
        this.src.append("</table>");
//Create histogram for Time
//        buildBarChartData("Each bar corresponds to 5 second intervals","5-Second intervals","Frequency",req, time) ;
//        this.src.append("<p><img src=\"BarChartServlet\">"); // inserts an image that calls BarChartServlet which gens a jpeg from data in HttpSession


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

    private void buildBarChartData(String title, String xAxisTitle, String yAxisTitle, HttpServletRequest req, int[] freq) {

        //String xAxisTitle = "Time spent in tutor";
        //String yAxisTitle = "Learning gain (Posttest - Pretest)%";
        //String title = "Learning Gain by Time Spent in the 'Learning Hut'";

        String[] xAxisLabels = new String[freq.length] ;
        for (int i=0; i<freq.length; i++) {
            xAxisLabels[i] = new Integer(i).toString() ;
        }

        IAxisDataSeries dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle, title );

        double d_freq[] = new double[freq.length] ;
        for ( int i=0; i<freq.length; i++ ) {
            d_freq [i] = freq[i] ;
        }

        double[][] data = new double[][]{d_freq};
        String[] legendLabels = {"Frequency"};
        HttpSession sess = req.getSession();
        sess.setAttribute("dataSeries",dataSeries);
        sess.setAttribute("data",data);
        sess.setAttribute("legendLabels",legendLabels);

    }
    
}