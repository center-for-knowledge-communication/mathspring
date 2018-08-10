package edu.umass.ckc.wo.woreports;

import java.sql.*;

import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a report of the Interventions. What happened before and after the intervention
 */
public class Report11 extends DirectReport {

        private int[] rowData; // array to store table values
        private final int ITEMS_TO_REPORT = 15; // number of data fields per problem

        private final static int NO_DATA = 0 ;
        private final static int PROB_BEFORE = 1 ;
        private final static int INTERV = 2 ;
        private final static int PROB_AFTER = 3 ;


        private String validStudents = "(3182,3189,3203,3207,3213,3220,3221,3226,3232,3235,3240,3241,3242,3245,3253,3256,3264,3274,3278,3280,3285,3288,3289,3291,3294,3295,3296,3298,3307,3308,3309,3310,3314,3315,3317,3327,3329,3330,3333,3335,3349,3351)" ;

/*  Students from Fall 2005
        private String validStudents = "(2277,2286,2289,2296,2299,2300,2301,2308,2316,2318,2320,2321,2322,2327,2330,2332,2335,2405,2407,2411,2413,2421,2425,2429,2430,2431,2435,2447,2457,2458,2459,2465,2466)" ; ;
*/
        private int n_validStudents=33 ;
        private int currState = 0 ;

        static final int B_NUMHINTS = 0 ;
        static final int B_NUMHINTSACC = 1 ;
        static final int B_TMINHINTS = 2 ;
        static final int B_TMTOTAL = 3 ;
        static final int B_NUMATTEMPTS = 4 ;
        static final int B_TMBETWATT = 5 ;
        static final int B_TMFIRSTATT = 6 ;
        static final int INTERVENTION = 7 ;
        static final int A_NUMHINTS = 8 ;
        static final int A_NUMHINTSACC = 9 ;
        static final int A_TMINHINTS = 10 ;
        static final int A_TMTOTAL = 11 ;
        static final int A_NUMATTEMPTS = 12 ;
        static final int A_TMBETWATT = 13 ;
        static final int A_TMFIRSTATT = 14 ;


    public Report11() {
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

    // write column headers containing problem names
      this.src.append(
          "StudId,b_nhints,n_nhacc,b_tmhints,b_tmprb,b_natt,b_tmamga,b_tmfatt,interv,a_nhints,a_nhacc,a_tmhints,a_tmprb,a_natt,a_tmamga,a_tmfatt\n");

      rowData = new int[ITEMS_TO_REPORT];

      // retrieve list of problem names
      String SQL = "SELECT * FROM EpisodicData2 WHERE studid in " + validStudents +
          " AND problemid> 0 AND activityname NOT like '%test%' order by studid, sessnum, elapsedtime";

      Statement episodicDataSt = conn.createStatement();
      ResultSet episodicDataRS = episodicDataSt.executeQuery(SQL);

      int numAttempts = 0;
      int numHints = 0;
      int numHintsAcc = 0;
      int lastAttTm = 0;
      int firstAttTm = 0;
      int lastHintTm = 0;
      int firsHinttTm = 0;

      while (episodicDataRS.next()) {

        int problemid = episodicDataRS.getInt("problemid");
        String action = episodicDataRS.getString("action");
        int probElapsed = episodicDataRS.getInt("probElapsed")/1000;
        int studId = episodicDataRS.getInt("studid");
        int id = episodicDataRS.getInt("id") ;

        if (problemid >= 0 ) { //Then it is a real problem_xxx

          if (action.equalsIgnoreCase("beginProblem")) {
            numAttempts = 0;
            numHints = 0;
            numHintsAcc = 0;
            lastAttTm = 0;
            firstAttTm = 0;
            lastHintTm = 0;
            firsHinttTm = 0;


            if (currState == PROB_AFTER) {
              currState = NO_DATA;
              this.src.append(studId + "," +
                              rowData[B_NUMHINTS] + "," + rowData[B_NUMHINTSACC] + "," + rowData[2] +
                              "," +
                              rowData[3] + "," + rowData[4] + "," + rowData[5] +
                              "," +
                              rowData[6] + "," + rowData[7] + "," + rowData[8] +
                              "," +
                              rowData[9] + "," + rowData[10] + "," + rowData[11] +
                              "," +
                              rowData[12] + "," + rowData[13] + "," + rowData[14] +"\n");

              rowData = new int[ITEMS_TO_REPORT] ;
            }
          }

          // compute variables of interest, especially if it is an endProblem
          else if (action.equalsIgnoreCase("endProblem")) { //Here, save all the data in the Array

            if (currState == NO_DATA   //First time
                || currState == PROB_AFTER   // There is data already for an after-intervention problem. This is a before-intervention-problem
                || currState == PROB_BEFORE) {  // Another problem was encountered. Consider this problem as a before-intervention problem
              rowData[B_TMFIRSTATT] = firstAttTm;
              rowData[B_TMBETWATT] = lastAttTm - firstAttTm;
              rowData[B_TMINHINTS] = lastHintTm - firsHinttTm ;
              rowData[B_TMTOTAL] = probElapsed;
              rowData[B_NUMHINTS] = numHints;
              rowData[B_NUMHINTSACC] = numHintsAcc;
              if ( numAttempts > 0 )
                rowData[B_NUMATTEMPTS] = numAttempts;
              currState = PROB_BEFORE;
            }
            else if (currState == INTERV) {
              rowData[A_TMFIRSTATT] = firstAttTm;
              rowData[A_TMBETWATT] = lastAttTm - firstAttTm;
              rowData[A_TMINHINTS] = lastHintTm - firsHinttTm ;
              rowData[A_TMTOTAL] = probElapsed;
              rowData[A_NUMHINTS] = numHints;
              rowData[A_NUMHINTSACC] = numHintsAcc;
              if ( numAttempts > 0 )
                rowData[A_NUMATTEMPTS] = numAttempts;
              currState = PROB_AFTER;
            }
          } // end if we found an endProblem

          else if (action.equalsIgnoreCase("attempt")) {
            numAttempts++;

            if (numAttempts == 1)
              firstAttTm = probElapsed;

            lastAttTm = probElapsed ;

          }

          else if (action.startsWith("hint")) {
            numHints++;

            if (numHints + numHintsAcc == 1 )
              firsHinttTm = probElapsed ;

            lastHintTm = probElapsed ;
          }

          else if (action.equalsIgnoreCase("hintAccepted")) {
            numHintsAcc++;

            if (numHints + numHintsAcc == 1 )
              firsHinttTm = probElapsed ;

            lastHintTm = probElapsed ;
          }
        } // End if Element seen is a true problem_xxx

        else { //Intervention Found
          if ( problemid == -100 ) {
            System.out.println() ;
          }
            currState = INTERV;
            rowData[INTERVENTION] = problemid;
        }
      } // end while

      System.out.print(this.src);
        return this;
    } // end createReport



    public void getBeforeAfterAnyProblem(Connection conn, int classId, AdminViewReportEvent e) throws Exception {
      boolean firstProblem = true ;

      this.src.append(
           "StudId,b_nhints,n_nhacc,b_tmhints,b_tmprb,b_natt,b_tmamga,b_tmfatt,interv,a_nhints,a_nhacc,a_tmhints,a_tmprb,a_natt,a_tmamga,a_tmfatt\n");

         // write column headers containing problem names
           this.src.append(
               "StudId,b_nhints,n_nhacc,b_tmhints,b_tmprb,b_natt,b_tmamga,b_tmfatt,interv,a_nhints,a_nhacc,a_tmhints,a_tmprb,a_natt,a_tmamga,a_tmfatt\n");

           rowData = new int[ITEMS_TO_REPORT];

           // retrieve list of problem names
           String SQL = "SELECT * FROM EpisodicData2 WHERE studid in " + validStudents +
               " AND problemid> 0 AND activityname NOT like '%test%' order by studid, sessnum, elapsedtime";

           Statement episodicDataSt = conn.createStatement();
           ResultSet episodicDataRS = episodicDataSt.executeQuery(SQL);

           int numAttempts = 0;
           int numHints = 0;
           int numHintsAcc = 0;
           int lastAttTm = 0;
           int firstAttTm = 0;
           int lastHintTm = 0;
           int firsHinttTm = 0;

           while (episodicDataRS.next()) {

             int problemid = episodicDataRS.getInt("problemid");
             String action = episodicDataRS.getString("action");
             int probElapsed = episodicDataRS.getInt("probElapsed") / 1000;
             int studId = episodicDataRS.getInt("studid");
             int id = episodicDataRS.getInt("id");

             if (problemid >= 0 ) { //Then it is a real problem_xxx

               if (action.equalsIgnoreCase("beginProblem")) {
                 numAttempts = 0;
                 numHints = 0;
                 numHintsAcc = 0;
                 lastAttTm = 0;
                 firstAttTm = 0;
                 lastHintTm = 0;
                 firsHinttTm = 0;

                 if (currState == PROB_AFTER) {
                   currState = NO_DATA;
                   this.src.append(studId + "," +
                                   rowData[0] + "," + rowData[1] + "," + rowData[2] +
                                   "," +
                                   rowData[3] + "," + rowData[4] + "," + rowData[5] +
                                   "," +
                                   rowData[6] + "," + rowData[7] + "," + rowData[8] +
                                   "," +
                                   rowData[9] + "," + rowData[10] + "," + rowData[11] +
                                   "," +
                                   rowData[12] + "," + rowData[13] + "," + rowData[14] +
                                   "\n");

                   rowData = new int[ITEMS_TO_REPORT];
                 }
               }

               // compute variables of interest, especially if it is an endProblem
               else if (action.equalsIgnoreCase("endProblem")) { //Here, save all the data in the Array

                 if (currState == NO_DATA //First time
                     || currState == PROB_AFTER // There is data already for an after-intervention problem. This is a before-intervention-problem
                     || currState == PROB_BEFORE) { // Another problem was encountered. Consider this problem as a before-intervention problem

                   if ( java.lang.Math.random() < 0.1 ) {  //Only record 10% of the problems, otherwise skip it
                     rowData[B_TMFIRSTATT] = firstAttTm;
                     rowData[B_TMBETWATT] = lastAttTm - firstAttTm;
                     rowData[B_TMINHINTS] = lastHintTm - firsHinttTm;
                     rowData[B_TMTOTAL] = probElapsed;
                     rowData[B_NUMHINTS] = numHints;
                     rowData[B_NUMHINTSACC] = numHintsAcc;
                     if (numAttempts > 0)
                       rowData[B_NUMATTEMPTS] = numAttempts;
                     currState = INTERV;
                   }
                 }
                 else if (currState == INTERV) {
                   rowData[A_TMFIRSTATT] = firstAttTm;
                   rowData[A_TMBETWATT] = lastAttTm - firstAttTm;
                   rowData[A_TMINHINTS] = lastHintTm - firsHinttTm;
                   rowData[A_TMTOTAL] = probElapsed;
                   rowData[A_NUMHINTS] = numHints;
                   rowData[A_NUMHINTSACC] = numHintsAcc;
                   if (numAttempts > 0)
                     rowData[A_NUMATTEMPTS] = numAttempts;
                   currState = PROB_AFTER;

                 }
               } // end if we found an endProblem

               else if (action.equalsIgnoreCase("attempt")) {
                 numAttempts++;

                 if (numAttempts == 1)
                   firstAttTm = probElapsed;

                 lastAttTm = probElapsed;

               }

               else if (action.startsWith("hint")) {
                 numHints++;

                 if (numHints + numHintsAcc == 1)
                   firsHinttTm = probElapsed;

                 lastHintTm = probElapsed;
               }

               else if (action.equalsIgnoreCase("hintAccepted")) {
                 numHintsAcc++;

                 if (numHints + numHintsAcc == 1)
                   firsHinttTm = probElapsed;

                 lastHintTm = probElapsed;
               }
             } // End if Element seen is a true problem_xxx
           } // end while

           System.out.print(this.src);
         } // end createReport

    public static void main(String[] args) {

    Report11 r = new Report11();
    try {
        r.getBeforeAfterAnyProblem (r.getConnection(), 0, null);
    } catch (Exception e) {
        e.printStackTrace();

    }
}

}
