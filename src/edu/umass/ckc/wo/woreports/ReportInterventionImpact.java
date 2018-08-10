package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.List;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.util.ProblemActivityRecord;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Impact of interventions
 * User: Ivon. Date: Aug 1, 2008
 * This report provides summaries PER PROBLEM, for individual students. It highlights problems that are hard for students in a class.
 * If it takes a semiabsskillid, then it shows the problems that involve a particular skill
 */


public class ReportInterventionImpact extends Report {

    private boolean headerPrinted = false ;
    private String className = null;

    public ReportInterventionImpact() {
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {


        int nprobsbefore = 0 ;
        int nprobsafter = 0 ;
        boolean firstInterventionInSession = true ;
        List<ProblemActivityRecord> problemActivityBefore = null ;
        List<ProblemActivityRecord> problemActivityAfter = null ;
        ClassInfo cl = DbClass.getClass(conn, classId);
        String className = getClassName(cl);
        String logTable = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();

        // vertBarGraph happens on action=beginProblem
        // studControlInterventionResponse happens on action=studDifficultyChoice
        // emotionFeedbackInterventionResponse happends on action=studentFeedback
        // TextIntervention happens on action =beginProblem
        String q = "select s.pedagogyId, e.studId, e.sessnum, e.action, e.userInput, e.problemId, e.activityname " +
                   "from episodicdata2 e INNER JOIN student s " +
                   "ON e.studId=s.id WHERE s.trialUser=0 and s.classId=? " +
                   "and (activityname = 'satProblem' or activityname = 'emotionFeedbackInterventionResponse' or " +
                   "activityName='studControlInterventionResponse' or activityName='TextIntervention' or activityname = 'vertBarGraph') " +
                   "order by e.studId,e.sessnum,e.elapsedTime";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);

        ResultSet rs = ps.executeQuery();
        int curSessNum=-1;


        while (rs.next()) {
            int pedId = rs.getInt(1);
            int studId = rs.getInt(2);
            int sessNum = rs.getInt(3);
            String action = rs.getString(4);
            String input = rs.getString(5);
            int problemid = rs.getInt(6);
            String activityname = rs.getString(7);
            className = "" ;


            // we just moved to a new student or session so wrap up and report that last Intervention.
            if (sessNum != curSessNum) {
                // if there was a change of student or session, report on him
//                if (curSessNum != -1) {
//                    reportInterventionData(studId, sessNum, pedId, action, input, problemid, activityname,
//                            nprobsbefore, nprobsafter);
//                }
                curSessNum = sessNum;
                 // reset bookeeping
                nprobsbefore = 0 ;
                nprobsafter = 0 ;
                firstInterventionInSession = true ;
            }

            if ( isIntervention(problemid, activityname) ) {   //found an Intervention
                if ( ! firstInterventionInSession ) {
                    reportInterventionData(studId, sessNum, pedId, action, input, problemid, activityname,
                        nprobsbefore, nprobsafter);

                    // reset bookeeping to get ready for a new intervention
                    nprobsbefore = nprobsafter ;
                    problemActivityBefore = problemActivityAfter ;

                    nprobsafter = 0 ;
                    problemActivityAfter = null ;
                }
                else {
                    firstInterventionInSession = false ;
                }
            }

            else if ( isSATProblem(problemid, activityname) ) {  // found a real problem

                if ( isEndOfSATProblem(problemid, activityname, action)) {
                    if ( firstInterventionInSession )   {
                           nprobsbefore++ ;
                           problemActivityBefore.add(new ProblemActivityRecord()) ;
                    }
                }
                else  {
                    if ( isEndOfSATProblem(problemid, activityname, action))    {
                        nprobsafter++ ;
                        problemActivityAfter.add(new ProblemActivityRecord()) ;

                    }
                }
            }
        }

        generateFoot() ;
        return this;
    }


    private  boolean isGaming(int nattempts, int nhints, int probelapsed) {
        return false ;
    }


    private boolean isIntervention(int problemid, String activityName) {
        return ( problemid < 0 &&
        ( activityName.equals("emotionFeedbackInterventionResponse") ||
        activityName.equals("TextIntervention") ||
        activityName.equals("studControlInterventionResponse") ||
        activityName.equals("vertBarGraph")) ) ;  //To change body of created methods use File | Settings | File Templates.
    }

    private boolean isSATProblem(int problemid,  String activityName) {
        return ( problemid > 0 && ! activityName.equals("posttestProblem")
                && ! activityName.equals("pretestProblem") && problemid != 999) ;
    }

    private boolean isEndOfSATProblem(int problemid,  String activityName, String action) {
        return ( action.equals("endProblem") && problemid > 0 && ! activityName.equals("posttestProblem")
                && ! activityName.equals("pretestProblem") && problemid != 999) ;
    }

    private boolean isAttemptToAnswerProblem(int problemid,  String activityName, String action) {
        return ( action.equals("endProblem") && problemid > 0 && ! activityName.equals("posttestProblem")
                && ! activityName.equals("pretestProblem") && problemid != 999) ;
    }

    private void reportInterventionData(int studId, int sessNum, int pedId, String action,
                                        String input, int problemid, String activityName,
                                        int nprobsbefore, int nprobsafter) {

        String intervention = null ;
        String kind = null ;
        String value = null ;

        if (activityName.equals("emotionFeedbackInterventionResponse") ) {
                intervention = "Emotion" ;
                kind = getEmotion(input) ;
                value = getEmotionValue(input) ;
        }
        if (activityName.equals("TextIntervention") ) {
                intervention = "Tip" ;
                kind="" ;
                value = new Integer(problemid).toString() ; 
        }
        if (activityName.equals("studControlInterventionResponse") ) {
                intervention = "Difficulty Choice" ;
                kind="" ;
                value = input ;
            
        }
        if (activityName.equals("vertBarGraph") ) {
                intervention = "Progress Chart" ;
                kind="" ;
                value = new Integer(problemid).toString() ; 
        }
        
        emitRow(studId,pedId,sessNum,nprobsbefore,intervention,kind,value,nprobsafter);
    }


    String getEmotion (String userInput) {                       
        String[] x = userInput.split(",");
        String emotion = x[1].toUpperCase();
        return emotion ;
    }


    String getEmotionValue (String userInput) {
        String[] x = userInput.split(",");
        String val = x[2];
        return val ;
    }

    private void emitRow (int studId, int pedId, int sessNum, int nprobsbefore,
                          String intervention, String kind, String value, int nprobsafter) {

        if ( !headerPrinted ) {
            printHeader() ;
            headerPrinted = true ;
        }

        this.src.append(
                    String.format("<tr>\n" +
                        "<td>%d</td>" +
                        "<td>%d</td>" +
                        "<td>%d</td>" +
                        "<td>%d</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%d</td>\n" +
                        "</tr>\n",
                        studId,pedId,sessNum,nprobsbefore,intervention,kind,value,nprobsafter));
    }

    private void generateFoot() {
        this.src.append("</table>" + foot);
    }

    private void printHeader () {
        this.src.append(generateHeader("Report of Intervention Impact for " + className));

        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Stud ID</th>" +
                "  <th>Pedagogy ID</th>" +
                "  <th>Session Number</th>" +
                "  <th>NumProblemsBefore</th>" +
                "  <th>Intervention</th>" +
                "  <th>Kind</th>" +
                "  <th>Value</th>" +
                "  <th>NumProblemsAfter</th>" +
                " </tr>\n");
    }


}