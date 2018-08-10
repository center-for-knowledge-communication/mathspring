package edu.umass.ckc.wo.woreports;

import java.sql.* ;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.xml.JDOMUtils;
import edu.umass.ckc.wo.woreports.js.DataTable;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMasteryHeuristic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jdom.Attribute;

/**
 * Detailed activity of each student.  What students worked on at each time step.
 * User: Ivon. Date: Aug 1, 2008
 * This report provides summaries PER PROBLEM, for individual students. It highlights problems that are hard for students in a class.
 * If it takes a semiabsskillid, then it shows the problems that involve a particular skill
 */
public class ReportProblemAndEmotions extends Report {

    private static int TIME_TO_SOLVE_HARD = 4 * 60; // >= 4 minutes
    private static int NUM_HINTS_HARD = 3; // >= 3 hints
    private static int NUM_INCORRECT_HARD = 3; // >= 3 incorrect
    private static int TIME_TO_SOLVE_MODERATE = 3 * 60; // >=3 minutes
    private static int NUM_HINTS_MODERATE = 2; // = 2 hints
    private static int NUM_INCORRECT_MODERATE = 2; // = 2 incorrect


    boolean lastWasBeginOfProblem = false;
    int positionForSOF = 0 ;
    boolean newStudent = false;
    boolean lastWasEndOfEmotion = false;
    boolean PreviousToLastWasEndOfEmotion = false;
    boolean bottomOutHint = false ;

    int solved = 0;
    int solvedOnFirst = 0 ;
    int classID = 0;
    int lastStudID = 0;
    int lastSessNum = 0;
    int numHints = 0;
    int numHelpAids = 0;
    int timeToChoose = 0;
    int timeToAnswer = 0;
    int timeToFirstAnswer = 0;
    int timeToHint = 0;
    int incAttempts = 0;
    String studentState = new String() ;
    long beginProblemTime = 0;
    long studTimeWithinSession = 0;
    long studTimeInSystem = 0;
    long correctionOffsetInSession = 0;
    String hintname = null ;
    String emotion=null;
    String level ="-1";
    String mode = "Topic Intro" ;
    String modeNextProblem = "Topic Intro" ;
    int numPracticeProbsInTopic = 0 ;

    long beginEmotionRequestTime = 0;
    int elapsedSinceEmotionRequest = 0;

    boolean isV2Events = false;
    boolean isFirstProbOfSess = true;
    boolean endProblem=false;
    int teacherId;
    int practiceProbId=-1 ;
    int lastSess = -1 ;

    long eventLogLastCall =0 ;
    int studId_Last_Call  ;
    int sessId_Last_Call  ;
    long timeInSystem_Last_Call  ;
    long timeInSession_Last_Call  ;
    double topicMastery_Last_Call = 0 ;

    long eventLogNextCall = 0;
    int studId_Next_Call  ;
    int sessId_Next_Call  ;
    long timeInSystem_Next_Call  ;
    long timeInSession_Next_Call  ;
    double topicMastery_Next_Call ;

    TopicMasterySimulator topicMasteryTracker;


    public ReportProblemAndEmotions(String pointLeftOffInDB) {
        if ( pointLeftOffInDB == null ) {
            eventLogLastCall = 0 ;
            studId_Last_Call = 0 ;
            sessId_Last_Call = 0 ;
            timeInSystem_Last_Call = 0 ;
            timeInSession_Last_Call = 0 ;
            topicMastery_Last_Call = 0 ;
        }
        else {
           String remainingString = pointLeftOffInDB ;

           int posSeparator = remainingString.indexOf("-") ;
           lastStudID = studId_Last_Call = new Integer(remainingString.substring(0,posSeparator)) ;
           remainingString = remainingString.substring(posSeparator+1) ;
           posSeparator = remainingString.indexOf("-") ;
           lastSess = sessId_Last_Call = new Integer(remainingString.substring(0,posSeparator)) ;
           remainingString = remainingString.substring(posSeparator+1) ;
           posSeparator = remainingString.indexOf("-") ;
           eventLogLastCall = new Long(remainingString.substring(0,posSeparator)) ;
           remainingString = remainingString.substring(posSeparator+1) ;
           posSeparator = remainingString.indexOf("-") ;
           timeInSession_Last_Call = new Long(remainingString.substring(0,posSeparator)) ;
           remainingString = remainingString.substring(posSeparator+1) ;
           posSeparator = remainingString.indexOf("-") ;
           timeInSystem_Last_Call = new Long(remainingString.substring(0,posSeparator)) ;
           remainingString = remainingString.substring(posSeparator+1) ;
           topicMastery_Last_Call = new Double(remainingString) ;
        }
    }



    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        ClassInfo cl = DbClass.getClass(conn, classId);
        teacherId = cl.getTeachid();
        String className = getClassName(cl);
        String table = "EventLog" ; //getEventLogTable(cl);   // either EpisodicData2 or EventLog
        isV2Events = false ; //cl.isNewLog();

        classID = classId;
        String q = "select s.username,s.id,s.pedagogyId,h.sessionId,h.problemBeginTime,h.timeInSession,h.timeInTutor,p.name,p.id,h.mode,h.topicId, " +
                "t.description, h.mastery,h.isSolved,h.numAttemptsToSolve,h.numMistakes,h.numhints,h.videoSeen,h.exampleSeen,h.solutionHintGiven," +
                "h.timeToFirstAttempt, h.timeToSolve, h.effort, h.emotionLevel from studentproblemhistory h, class c, student s, problem p, problemgroup t " +
                "where t.id=h.topicid and s.classid=c.id and s.id=h.studid and p.id=h.problemId and c.id=? order by s.id, h.problemBeginTime";
        // this query is going through the eventlog for each student in the class (in order by student)



//        this.src.append(generateHeaderWithJS("Report Problem Activity for " + className,
//                new JSFile[] { JSFile.JQUERY, DataTable.DATATABLE_JS},
//                new JSFunction[] {DataTable.DATATABLE_JQUERY_DOCREADY_FUNCTION},
//                new CSSFile[] {DataTable.DATATABLE_PAGE_CSS, DataTable.DATATABLE_TABLE_JUI_CSS,DataTable.DATATABLE_THEME_CSS} ));
//        this.src.append(DataTable.BODY_TAG);
        this.src.append("<h3>Detail Problem Info for " + className + "</h3>\n");
        addLegend();
        addNavLinks(classId,cl.getTeachid());
        this.src.append(DataTable.DIV1);
        this.src.append(DataTable.DIV2);
        this.src.append(getTableBegin());

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            rs = ps.executeQuery();
            while (rs.next()) {
                outputRow(this.src, rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }

        this.src.append("</table></div></div></body></html>");
        return this;
    }

    /*
    s.username,s.id,s.pedagogyId,h.sessionId,h.problemBeginTime,h.timeInSession,h.timeInTutor,p.name,p.id,h.mode,h.topicId, " +
                "t.description, h.mastery,h.isSolved,h.numAttemptsToSolve,h.numMistakes,h.numhints,h.videoSeen,h.exampleSeen,h.solutionHintGiven," +
                "h.timeToFirstAttempt, h.timeToSolve, h.effort, h.emotionLevel
     */
    private void outputRow(StringBuffer src, ResultSet rs) throws SQLException {
        src.append("<tr>");
        int c=1;
        outputStr(rs.getString(c), src); //username
        outputInt(rs.getInt(++c), src); //id
        outputInt(rs.getInt(++c), src);  //pedagogyId
        outputInt(rs.getInt(++c), src);  //sessionId
        Timestamp ts = rs.getTimestamp(++c);   // problemBeginTime
        outputStr(ts.toString(),src);
        Double d =  (((double) rs.getInt(++c)) / 1000.0 / 60.0);
        outputStr(String.format("%.2f", d), src);  //timeInSession
        d =  (((double) rs.getInt(++c)) / 1000.0 / 60.0);
        outputStr(String.format("%.2f", d), src);  ////timeInTutor
        outputStr(rs.getString(++c), src); // p.name
        outputInt(rs.getInt(++c), src);  //p.id
        outputStr(rs.getString(++c), src); // mode
        outputInt(rs.getInt(++c), src);  //topic.id
        outputStr(rs.getString(++c), src); // topic name
        d = rs.getDouble(++c);    //h.mastery
        outputStr(d.toString(),src);
        Boolean b = rs.getBoolean(++c); // isSolved
        outputStr(b.toString(),src);
        outputStr(rs.getInt(++c)==1?"true":"false", src);  //numAttemptsToSolve  -> Solved on 1
        outputInt(rs.getInt(++c), src);  //numMistakes
        outputInt(rs.getInt(++c), src);  //numhints
        outputInt(rs.getInt(++c) + rs.getInt(++c), src);  //num Help aids
        b = rs.getBoolean(++c); //solutionHintGiven
        outputStr(b.toString(),src);
        long x = rs.getLong(++c); // timeToFirstAttempt
        outputInt((int) (x / 1000), src);
        x = rs.getLong(++c); // timeToSolve
        outputInt((int) (x / 1000), src);
        outputStr(rs.getString(++c), src); // effort
        outputInt(rs.getInt(++c), src); //emotion level
        outputStr("",src); // stud state
        src.append("</tr>");





    }

    private void outputStr (String s, StringBuffer src) {
        src.append("<th>"+ s + "</th>");
    }

    private void outputInt (int i, StringBuffer src) {
        src.append("<th>"+ i + "</th>");
    }

    private void addLegend() {
        this.src.append("<table width=\"557\"> \n");
        this.src.append("<tr> \n <td bgcolor=#FF0000 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Problems that your students found really hard </td> \n </tr> ");
        this.src.append("<tr> \n <td bgcolor=#FFFF00 width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Problems that your students found challenging </td> \n </tr> ");
        this.src.append("</table>\n");
    }

    // v2 reports generate a line for each problem when the beginProblem is hit.   This is because
    // interventions can come after the endProblem event.
    private void createV2Report (Connection conn, String SQL, HttpServletRequest req, int classId) throws Exception {
        PreparedStatement ps;
        ps = conn.prepareStatement(SQL);
        ps.setInt(1,classId);
        ResultSet rs = ps.executeQuery(SQL);
        int lastProbId=-1;
        int lastTopicId=-1;
        int probId=-1;
        while (rs.next()) {

            if ( rs.getLong("eventLogID") >= this.eventLogLastCall ) {
                int studId = rs.getInt("studid");
                int sessNum = rs.getInt("sessnum");
                String action = rs.getString("action");
                int probElapsed = rs.getInt("probElapsed") ;


               if (action.equalsIgnoreCase("BeginProblem") || action.equals("Formality_BeginProblem"))  {

                   if (lastSessNum == 0 || lastSessNum != sessNum) {     // Just starting
                       if ( timeInSession_Last_Call > 0  )   {
                           studTimeWithinSession = this.timeInSession_Last_Call ;
                           timeInSession_Last_Call = 0 ;
                       }
                       else
                           //studTimeWithinSession = probElapsed;
                           studTimeWithinSession = 0 ;

                       lastSessNum = sessNum ;
                   }


                   if (lastStudID == 0 || lastStudID != studId || this.timeInSystem_Last_Call > 0 ) {
                       if ( studId == 10371 ) { ;;; ; }
                       if ( timeInSystem_Last_Call > 0 ) {
                           studTimeInSystem = timeInSystem_Last_Call ;//probElapsed ;
                           timeInSystem_Last_Call = 0 ;
                       }
                       else
                           studTimeInSystem = 0 ;//probElapsed;
                   }

               }
               else if (action.equalsIgnoreCase("EndProblem") || action.equals("Formality_EndProblem"))
                    processTimes(studId, sessNum, rs.getLong("elapsedTime"), beginProblemTime, rs.getLong("probElapsed"));

                
                // on each new student, reset the topic mastery tracking.
                if (studId != lastStudID) {
                    lastStudID = studId;
                    positionForSOF = 0 ;
                    this.topicMasteryTracker = new TopicMasterySimulator(conn,new StudentModelMasteryHeuristic(conn));
                }
                String username = rs.getString("username");
                int pedId = rs.getInt("pedagogyId");
                if (sessNum != lastSess)   {
                    isFirstProbOfSess = true;
                    lastSess = sessNum;
                }
                else isFirstProbOfSess = false;
                int topicId = rs.getInt("curtopicId");  // for older events this will be null
                if (rs.wasNull())
                    topicId = -1;
                // When topic changes let the topic updater know
                if (topicId != -1 && topicId != lastTopicId) {
                    if ( topicMasteryTracker == null ) {
                        topicMasteryTracker = new TopicMasterySimulator(conn,new StudentModelMasteryHeuristic(conn));
                        topicMasteryTracker.newTopic(topicId, this.topicMastery_Last_Call);
                    }
                    else
                        topicMasteryTracker.newTopic(topicId);

                    lastTopicId = topicId;
                    numPracticeProbsInTopic = 0 ;
                }
                String userInput = rs.getString("userInput");
                String activityName = rs.getString("activityName") ;
                hintname = rs.getString("hintstep") ;
                String problemName = rs.getString("name") ;

                if (rs.getString("problemId") != null) {
                    lastProbId = probId;
                    probId = Integer.parseInt(rs.getString("problemId"));

                    boolean isEmotionEvent = isEmotionEvent(probId, action, userInput, activityName);
                    if (!isEmotionEvent) {
                        processProblem2(conn, studId, username, pedId, sessNum, lastProbId, topicId, action, userInput,
                                problemName, rs, req, activityName);
                    } else {
                        processEmotion2(conn, studId, username,  sessNum,  rs, userInput);
                    }
                }
            } // end if that truncates so that too many rows are not queried on the DB
        } //while

//        this.src.append("<H1> Parameter to Add in Next round:  &extraParam=" +
//          studId_Next_Call + "-" + sessId_Next_Call + "-" + eventLogNextCall + "-" + timeInSession_Next_Call
//                + "-" + timeInSystem_Next_Call +  "-" +  topicMastery_Next_Call + "</H1>") ;
    }

    // V1 report generates its output for each problem when the endProblem event is hit
    private void createV1Report (Connection conn, String SQL, HttpServletRequest req) throws Exception {
        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);
        while (rs.next()) {
            int studId = rs.getInt("studid");
            String username = rs.getString("username");
            int pedId = rs.getInt("pedagogyId");
            int sessNum = rs.getInt("sessnum");
            String action = rs.getString("action");
            String userInput = rs.getString("userInput");
            hintname = rs.getString("hintstep") ;


            if (action.equalsIgnoreCase("EndProblem") || action.equals("Formality_EndProblem"))
                processTimes(studId, sessNum, rs.getLong("elapsedTime"), beginProblemTime, rs.getLong("probElapsed"));


            if (rs.getString("problemId") != null) {
                int probId = Integer.parseInt(rs.getString("problemId"));

                boolean isEmotionEvent = probId <= 0;
                if (!isEmotionEvent) {
                    processProblem1(conn, studId, username, pedId, sessNum, probId, action, userInput, rs, req);
                } else {
                    processEmotion1(conn, studId, username, pedId, sessNum, probId, rs, userInput);
                }

            }
        } //while
    }

    private boolean isEmotionEvent(int probId, String action, String userInput, String activityName) throws Exception {

        // TODO we must add conditions so that we know the intervention is about clips
        // The problem is that we don't really know it until the InputResponse event
        if (action.equalsIgnoreCase("beginIntervention") || action.equalsIgnoreCase("endIntervention"))
            return true;
        else if (action.equalsIgnoreCase("InputResponse") && isEmotionSurveyResponse(userInput))
            return true;

        else if (activityName.equalsIgnoreCase("emotionFeedbackInterventionResponse") )
            return true ;
        else return false;
    }

    private void processTimes(int studId, int sessNum, long elapsedTime, long beginEventTime, long probElapsed) {

        //correctionOffsetInSession = elapsedTime - beginEventTime ;
        studTimeWithinSession += probElapsed ;//- correctionOffsetInSession;
        studTimeInSystem += probElapsed ; //- correctionOffsetInSession;
    }

    private String getTableBegin() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table border=1 cellspacing=1 cellpadding=1 class=\"display\" id=\"example\">\n" +
                " <thead><tr>\n" +
                "  <th> User Name  </th>\n" +
                "  <th> Student ID  </th>\n" +
                "  <th> Pedagogy </th>\n" +
                "  <th> Session Number </th>\n" +
                "  <th> Begin Problem Time </th>\n" +
                "  <th> Min. in Session </th>\n" +
                "  <th> Min. in Tutor </th>\n" +
                "  <th> Problem Name </th>\n" +
                "  <th> Problem ID </th>\n" +
                "  <th> Mode </th>\n" +
                "  <th> Cur TopicId </th>\n" +
                "  <th> TopicName </th>\n" +
                "  <th> Topic Mastery </th>\n" +
                "  <th> Solved? </th>\n" +
                "  <th> Solved First Attempt? </th>\n" +
                "  <th> Incorrect Attempts </th>\n" +
                "  <th> Hints Seen </th>\n" +
                "  <th> Help Aids </th>\n" +
                "  <th> BottomOut Hint </th>\n" +
                "  <th> Seconds To First Attempt </th>\n" +
                "  <th> Seconds To Solve </th>\n" +
                "  <th> Emotion After </th>\n" +
                "  <th> Emotion Level </th>\n" +
                "  <th> Student State </th>\n" +

                " </tr></thead>\n" +
                " <tfoot><tr>\n" +
                "  <th> User Name  </th>\n" +
                "  <th> Student ID  </th>\n" +
                "  <th> Pedagogy </th>\n" +
                "  <th> Session Number </th>\n" +
                "  <th> Begin Problem Time </th>\n" +
                "  <th> Time in Session </th>\n" +
                "  <th> Time in Tutor </th>\n" +
                "  <th> Problem Name </th>\n" +
                "  <th> Problem ID </th>\n" +
                "  <th> Mode </th>\n" +
                "  <th> Cur TopicId </th>\n" +
                "  <th> TopicName </th>\n" +
                "  <th> Topic Mastery </th>\n" +
                "  <th> Solved? </th>\n" +
                "  <th> Solved First Attempt? </th>\n" +
                "  <th> Incorrect Attempts </th>\n" +
                "  <th> Hints Seen </th>\n" +
                "  <th> Help Aids </th>\n" +
                "  <th> Bottom Out Hint </th>\n" +
                "  <th> Seconds To First Attempt </th>\n" +
                "  <th> Seconds To Solve </th>\n" +
                "  <th> Emotion After </th>\n" +
                "  <th> Emotion Level </th>\n" +
                "  <th> Student State </th>\n" +

                " </tr></tfoot>\n" +
                "<tbody id=\"data\">\n");
        return sb.toString();

    }

    private void processProblem1(Connection conn, int studId, String username, int pedId, int sessNum, int probId, String action,
                                 String userInput, ResultSet rs, HttpServletRequest req) throws java.lang.Exception {

        if (action.equalsIgnoreCase("BeginProblem") || action.equals("Formality_BeginProblem")) {
            timeToChoose = 0;
            timeToAnswer = 0;
            timeToHint = 0;
            numHints = 0;
            numHelpAids = 0;
            solved = 0;
            solvedOnFirst = 0 ;
            incAttempts = 0;
            lastWasBeginOfProblem = true;
            PreviousToLastWasEndOfEmotion = lastWasEndOfEmotion;
            lastWasEndOfEmotion = false;
            bottomOutHint = false ;

            return ;
        }

        if (action.equalsIgnoreCase("attempt") || action.equalsIgnoreCase("Formality_Attempt")) {

            if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                    (timeToAnswer == 0)) {
                timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));
                solved = 1 ;

                if (timeToChoose == 0)  //this is the first attempt, and it is correct
                    solvedOnFirst = 1;
            }
            if (timeToChoose == 0)
                timeToChoose = Integer.parseInt(rs.getString("probElapsed"));

            if ((Integer.parseInt(rs.getString("isCorrect"))) == 0
                    && incAttempts < 4)  // A hack because somehow students could get more than 4 incorrect answers
                incAttempts++;

        }

        if ( (action != null && action.toLowerCase().startsWith("hint"))
                || (userInput != null && userInput.toLowerCase().equals("<selection>yes-hint</selection>") )
                || (action != null &&action.startsWith("Formality_Hint"))) {

            if (timeToHint == 0)
                timeToHint = Integer.parseInt(rs.getString("probElapsed"));

            if (numHints < 7) {  // A threshold just in case students make hectic choose_a clicks
                numHints++;
                numHelpAids ++ ;
            }

            if (hintname!= null && hintname.startsWith("choose")) {
                bottomOutHint = true ;
            }

            return ;
        }

        if (action.equalsIgnoreCase("ShowExample") || action.equalsIgnoreCase("ShowVideo") || action.equalsIgnoreCase("ShowSolveProblem")) {
            numHelpAids ++ ;

            if (action.equalsIgnoreCase("ShowSolveProblem"))
                bottomOutHint = true ;

            return ;

        }

        if (action.equalsIgnoreCase("endProblem")  || action.equals("Formality_EndProblem")) {
            int probElapsed = Integer.parseInt(rs.getString("probElapsed"));
            long elapsedTime = Long.parseLong(rs.getString("elapsedTime"));
            beginProblemTime = elapsedTime - probElapsed ;

            printReport1(conn, probId, studId, username, pedId, sessNum);

            mode = modeNextProblem ;
            beginProblemTime = 0;
            studentState = "" ;
            timeToChoose = 0;
            timeToAnswer = 0;
            timeToHint = 0;
            numHints = 0;
            numHelpAids = 0 ;
            solvedOnFirst = 0;
            solved = 0 ;
            incAttempts = 0;
            lastWasBeginOfProblem = true;
            PreviousToLastWasEndOfEmotion = lastWasEndOfEmotion;
            lastWasEndOfEmotion = false;
            bottomOutHint = false ;

            //Not sure which one will be last, Next Problem or End Problem
            eventLogNextCall = rs.getLong("eventLogID") ;
            studId_Next_Call = studId ;
            sessId_Next_Call = sessNum ;
            timeInSession_Next_Call = studTimeWithinSession ;
            timeInSystem_Next_Call = studTimeInSystem ;
            if ( topicMasteryTracker != null )
                topicMastery_Next_Call = this.topicMasteryTracker.getMastery() ;

            return ;
        }  // end endProblem
    }

    //  it assumes that an intervention following a problem
    // is about clips.
    private void processEmotion1(Connection conn, int studId, String username, int pedId, int sessNum, int probId, ResultSet rs, String userInput) throws java.lang.Exception {
        int probElapsed = Integer.parseInt(rs.getString("probElapsed"));
        long elapsedTime = Long.parseLong(rs.getString("elapsedTime"));

        //This line is here only because the endProblem always comes in with the right probElapsedTime
        // immediately before the  studentFeedback line
        if (rs.getString("action").equalsIgnoreCase("endProblem") || rs.getString("action").equals("Formality_EndProblem")) {
            beginEmotionRequestTime = elapsedTime - probElapsed ;
            elapsedSinceEmotionRequest = probElapsed ;
        }
        // the intervention response is different in v2 vs v1
        else if (rs.getString("action").equals("studentFeedback"))  {
            processTimes(studId, sessNum, elapsedTime, beginEmotionRequestTime, elapsedSinceEmotionRequest);

            if (studId != this.lastStudID) {
                lastStudID = studId;
               //Here is where we print the first row when the emotion has been the first thing in the session
                this.src.append("</tr><tr>" +
                        "<td>" + getUserNameHTML(username) + "</td>" +
                        "<td>" + getStudIdHTML(studId) + "</td>" +
                        "<td>" + pedId + "</td>" +
                        "<td>" + sessNum + "</td>" +
                        "<td>" + beginEmotionRequestTime + "</td>" +
                        "<td>" + studTimeWithinSession + "</td>" +
                        "<td>" + studTimeInSystem + "</td>" +
                        "<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>");
            }
            String[] userinput = rs.getString("userinput").split(",");
            this.src.append("<td>" + userinput[1] + "</td>");
            this.src.append("<td>" + userinput[2] + "</td>");

            PreviousToLastWasEndOfEmotion = lastWasEndOfEmotion;
            lastWasEndOfEmotion = true;

        }

    }

    private void processProblem2(Connection conn, int studId, String username, int pedId, int sessNum, int probId, int topicId, String action,
                                 String userInput, String problemName, ResultSet rs, HttpServletRequest req, String activityName) throws java.lang.Exception {

        int probElapsed = Integer.parseInt(rs.getString("probElapsed"));
        long elapsedTime = Long.parseLong(rs.getString("elapsedTime"));

        if (action.equalsIgnoreCase("BeginProblem") || action.equals("Formality_BeginProblem")) {
            if (!isFirstProbOfSess  ) {
                printReport2(conn, probId, studId, username, topicId, pedId, topicId, sessNum, req);
            }

            mode  = modeNextProblem ;

            if ( action.equals("Formality_BeginProblem") || (activityName!=null && activityName.equals("satProblem")) )
                   mode = "PracticeProblem" ;

            beginProblemTime = elapsedTime ;
            emotion=null;
            level ="-1";
            timeToChoose = 0;
            timeToFirstAnswer = 0;
            timeToAnswer = 0;
            timeToHint = 0;
            numHints = 0;
            numHelpAids = 0;
            solved = 0;
            solvedOnFirst = 0 ;
            incAttempts = 0;
            lastWasBeginOfProblem = true;
            PreviousToLastWasEndOfEmotion = lastWasEndOfEmotion;
            lastWasEndOfEmotion = false;
            bottomOutHint = false ;
        }
        if (action.equalsIgnoreCase("attempt") || action.equals("Formality_Attempt")) {

            if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                    (timeToAnswer == 0)) {
                timeToAnswer = probElapsed;
                solved = 1 ;

                if (timeToChoose == 0 && this.numHelpAids == 0 )  //this is the first attempt, and it is correct
                    solvedOnFirst = 1;
            }
            if (timeToChoose == 0)  {
                timeToChoose = probElapsed;
                timeToFirstAnswer = timeToChoose;
            }

            if ((Integer.parseInt(rs.getString("isCorrect"))) == 0
                    && incAttempts < 4)  // A hack because somehow students could get more than 4 incorrect answers
                incAttempts++;
        } else if (action.toLowerCase().startsWith("hint")
                || (userInput!= null && userInput.toLowerCase().equals("<selection>yes-hint</selection>"))
                || action.startsWith("Formality_Hint")) {
            if (timeToHint == 0)
                timeToHint = probElapsed ;

            if (numHints < 7) {  // A threshold just in case students make hectic choose_a clicks
                numHints++;
                numHelpAids ++ ;
            }

            if (hintname != null && hintname.startsWith("choose")) {
                bottomOutHint = true ;
            }

        } else if (action.equalsIgnoreCase("ShowExample") || action.equalsIgnoreCase("ShowVideo") || action.equalsIgnoreCase("ShowSolveProblem")) {
            numHelpAids ++ ;

            if (action.equalsIgnoreCase("ShowSolveProblem"))
                bottomOutHint = true ;


        } else if ( action.equalsIgnoreCase("endProblem")  || action.equals("Formality_EndProblem") ) {
            endProblem=true;

            if ( (mode!= null && (mode.equals("PracticeProblem") || mode.equals("satProblem"))) ||
                    (problemName != null && problemName.startsWith("formality")))  {
                numPracticeProbsInTopic ++ ;
                //mode = "PracticeProblem" ;
            }

            topicMasteryTracker.updateMastery(probId, numHints,solved,incAttempts,timeToFirstAnswer, numPracticeProbsInTopic, mode );   // Check problem mode!!!!
        }  // end endProblem

        else if (action.equalsIgnoreCase("NextProblem")) {
            if (sessNum != lastSess)   {
                isFirstProbOfSess = true;
                lastSess = sessNum;
            }
            else isFirstProbOfSess = false ;

            if ( ! activityName.contains("Intervention") && ! activityName.contains("BarGraph") && ! activityName.contains("NextProblem"))
                modeNextProblem =  activityName ;

            eventLogNextCall = rs.getLong("eventLogID") ;
            studId_Next_Call = studId ;
            sessId_Next_Call = sessNum ;
            timeInSession_Next_Call = studTimeWithinSession ;
            timeInSystem_Next_Call = studTimeInSystem ;
            beginProblemTime = elapsedTime - probElapsed ;

        }
    }

    // The problem with this method is that it assumes that an intervention following a problem
    // is about clips.  In v2 it may not be.
    private void processEmotion2(Connection conn, int studId, String username,  int sessNum, ResultSet rs, String userInput) throws java.lang.Exception {
        int probElapsed = Integer.parseInt(rs.getString("probElapsed"));
        long elapsedTime = Long.parseLong(rs.getString("elapsedTime"));

        // emotion survey (interventions) are done after endProblem=true
        if (endProblem && rs.getString("action").equalsIgnoreCase("beginIntervention")) {
            beginEmotionRequestTime = elapsedTime - probElapsed ;
            elapsedSinceEmotionRequest = probElapsed;
        }
        // the intervention response is different in v2 vs v1
        else if (endProblem &&
                rs.getString("action").equalsIgnoreCase("InputResponse") &&
                isEmotionSurveyResponse(userInput)) {
            processTimes(studId, sessNum, elapsedTime, beginEmotionRequestTime, elapsedSinceEmotionRequest);
            Element e = JDOMUtils.getRoot(userInput);
            if (e!=null && e.getName().equals("userInput")) {
                emotion= e.getAttributeValue("emotion") ;
                level = e.getAttributeValue("level") ;
            }
        }
    }

    private boolean isEmotionSurveyResponse(String userInput) throws Exception {

        if ( userInput.startsWith("<")) {
            //System.out.println(userInput) ;
            Element root = JDOMUtils.getRoot(userInput);
            Attribute affect = root.getAttribute("emotion");
            return affect != null;
        }

        return false ;
    }

    private void printReport1(Connection conn, int probId, int studId, String username, int pedId, int sessNum) throws java.lang.Exception {


        if (lastWasEndOfEmotion) {
            src.append("</tr>");
            PreviousToLastWasEndOfEmotion = true;
            lastWasEndOfEmotion = false;
        } else if (lastWasBeginOfProblem && !PreviousToLastWasEndOfEmotion) {
            src.append("<td></td><td></td><td></td><td></td><td></td><td></td></tr>");
            lastWasBeginOfProblem = false;
        }

        Statement classSt2 = conn.createStatement();
        ResultSet rs2 = classSt2.executeQuery("SELECT Problem.name, Problem.form, Problem.nickname, " +
                "Problem.animationresource, Problem.id FROM Problem WHERE Problem.id = " + probId);
        String gifName = "null";
        String link = "null";
        String probName = "Unknown Problem";
        int timeToFirstAction = 0;
        String probNumber;

        if (rs2.next()) {
            gifName = rs2.getString("name");
            probName = rs2.getString("nickname");

            // DAM 11/19/08 patch repair because problems with bad names are included in the result list.
            if (gifName.startsWith("problem_") ) {
                probNumber = gifName.substring(8);
            }
            else if (rs2.getString("form") != null && rs2.getString("form").equalsIgnoreCase("ExternalURL")) {
                probNumber = gifName ;
                link = rs2.getString("animationresource");
            }
            else {
                probNumber = "null";
                //System.out.println("Warning: Badly formed problem name " + gifName);
            }

            if (probId == 999) {
                probName = gifName = "Introduction Screen";
                link = "Introduction to Topic";
            }

        }

        //Change the color of the row depending on performance
//        String bgcolor = new String("#FFFFFF");
        // If coloring other than the default is to be applied to the rows then the <tr>  should have a class added to it
        // such as  <tr class="gradeA">   which then gets formatting from the datatable css for the row



        src.append("<td>" + getUserNameHTML(username) + "</td>\n");
        src.append("<td>" + getStudIdHTML(studId) + "</td>\n");
        src.append("<td>" + pedId + "</td>\n");
        src.append("<td>" + sessNum + "</td>\n");
        src.append("<td>" + beginProblemTime + "</td>\n");
        src.append("<td>" + studTimeWithinSession + "</td>\n");
        src.append("<td>" + studTimeInSystem + "</td>\n");

        if (probId == 999)
            src.append("<td><b> Topic Intro </b></td>\n");
        else
            src.append("<td><a href=\"" + link + "\"><b>" + gifName + "</b></a></td>\n");

        src.append("<td>"+ probId +"</td>");
        src.append("<td>" + mode + "</td>\n");
        src.append("<td>"+ probId +"</td>");
        src.append("<td>" + "" + "</td>\n");  //topicId
        src.append("<td>" + "" + "</td>\n");  //Mastery


        src.append("<td>" + solved + "</td>\n");
        src.append("<td>" + solvedOnFirst + "</td>\n");
        src.append("<td>" + incAttempts + "</td>\n");
        src.append("<td>" + numHints + "</td>\n");
        src.append("<td>" + numHints + "</td>\n");
        src.append("<td>" + bottomOutHint + "</td>\n");
        src.append("<td>" + timeToChoose / 1000 + "</td>\n");
        src.append("<td>" + timeToAnswer / 1000 + "</td>\n");
//            src.append(" </tr>\n");

        //PROCESS THE STUDENT STATE
        if ( shouldPrintDetails(probId, mode))   {


            if ( solved == 0 && incAttempts == 0 && numHints == 0 && numHelpAids == 0)
                studentState = "SKIP" ;
            else if ( timeToChoose <= 4000 && timeToChoose > 0)  // Not reading, whatever they do after
                studentState = "NOTR" ;
            else if ( timeToAnswer == 0  && solved == 0 ) //Reading and some action, but gave up. Did not answer.
                studentState = "GIVEUP" ;
            else if ( solvedOnFirst == 1 && numHints == 0 && numHelpAids == 0)   //Reading and solved on First Attempt, without help
                studentState = "SOF" ;
            else if ( bottomOutHint ) //Reading and solved, But saw the last choose_x hint
                studentState = "BOTTOMOUT" ;
            else if ( timeToAnswer > 0 && ( numHints > 0 || numHelpAids > 0) ) //Reading and solved with Help (might have accepted when offered)
                studentState = "SHINT" ;
            else if ( timeToAnswer > 0 && incAttempts <= 1 ) //Reading and solved with no hints in at most 2 Incorrect Attempts
                studentState = "ATT" ;
            else if ( timeToAnswer > 0 && incAttempts >= 1  ) //Solved Eventually, in 2-5 attempts, most likely guessing
                studentState = "GUESS" ;


            src.append("<td>" + studentState + "</td>\n");
        }
        else {
            studentState = null ;
            src.append("<td></td>") ;
        }

    }


    private void printReport2(Connection conn, int probId, int studId, String username, int topicNOTRId,
                              int pedId, int topicId, int sessNum, HttpServletRequest req) throws java.lang.Exception {

        Statement classSt2 = conn.createStatement();
        ResultSet rs2 = classSt2.executeQuery("SELECT Problem.name, Problem.form, " +
                "Problem.nickname, Problem.animationresource, Problem.id FROM Problem WHERE Problem.id = " + probId);
        String gifName = "null";
        String link = "null";
        String probName = "Unknown Problem";
        int timeToFirstAction = 0;
        String probNumber;

        if (rs2.next()) {
            gifName = rs2.getString("name");
            probName = rs2.getString("nickname");
            // DAM 11/19/08 patch repair because problems with bad names are included in the result list.
            if (gifName.startsWith("problem_") ) {
                probNumber = gifName.substring(8);
                link = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + probNumber;
            }
            else if (rs2.getString("form")!= null && rs2.getString("form").equalsIgnoreCase("ExternalURL")) {
                probNumber = gifName ;
                link = rs2.getString("animationresource");
            }
            else {
                probNumber = "null";
                //System.out.println("Warning: Badly formed problem name " + gifName);
            }

            if (probId == 999 ) {
                probName = gifName = "Introduction Screen";
                link = "Introduction to Topic";
            }
        }
        int hardProb = 0;
        //Change the color of the row depending on performance
//        String bgcolor = new String("#FFFFFF");
        // to add color coding to a row use classes in <tr> that have a class from the css as in <tr class="gradeA">
        /*
        if (numHints >= NUM_HINTS_HARD || incAttempts >= NUM_INCORRECT_HARD || (timeToAnswer / 1000) >= TIME_TO_SOLVE_HARD)
            hardProb = 2;
        else if (numHints == NUM_HINTS_MODERATE || incAttempts==NUM_INCORRECT_MODERATE || (timeToAnswer / 1000) >= TIME_TO_SOLVE_MODERATE )
            hardProb = 1;
          */
        if (hardProb == 2)
            src.append("<tr class=\"hard2\">\n");
        else if (hardProb == 1)
            src.append("<tr class=\"hard1\">\n");
        else src.append("<tr>\n");
        src.append("<td>" + getUserNameHTML(username) + "</td>\n");
        src.append("<td>" + getStudIdHTML(studId) + "</td>\n");
        src.append("<td>" + pedId + "</td>\n");
        src.append("<td>" + sessNum + "</td>\n");
        src.append("<td>" + beginProblemTime + "</td>\n");
        src.append("<td>" + studTimeWithinSession + "</td>\n");
        src.append("<td>" + studTimeInSystem + "</td>\n");

        //REGARDING THE PROBLEM NAME AND MODE
        if (probId == 999)
            src.append("<td><b> Topic Intro </b></td>\n");

        else {
            src.append("<td><a href=\"" + link + "\"><b>" + gifName + "</b></a></td>\n");

            if ( ! gifName.startsWith("problem") && ! gifName.startsWith("formality"))
                mode = "External WebSite" ;
        }

        src.append("<td>"+ probId +"</td>");
        src.append("<td>" + mode + "</td>\n");

        if (topicId > 0) {       //Print the topic ID and mastery regardless
            String tname = DbTopics.getTopicName(conn,topicId);
            if (tname.length() > 12)
                tname= tname.substring(0,12);
            src.append("<td>" + topicId + "</td>\n");
            src.append("<td>" + tname + "</td>\n");

            src.append("<td>" + String.format("%4.3f",this.topicMasteryTracker.getMastery()) + "</td>\n");
        }
        else {
            src.append("<td></td><td></td><td></td>");
        }

        if ( ! shouldPrintDetails(probId, mode) )  {
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            src.append("<td></td>\n");
            if (emotion != null) {
                src.append("<td>" + emotion + "</td>\n");
                src.append("<td>" + level + "</td>\n");
            }
            else {
                src.append("<td></td><td></td>");
            }
        }
        else {
            src.append("<td>" + solved + "</td>\n");
            src.append("<td>" + solvedOnFirst + "</td>\n");
            src.append("<td>" + incAttempts + "</td>\n");
            src.append("<td>" + numHints + "</td>\n");
            src.append("<td>" + numHelpAids + "</td>\n");
            src.append("<td>" + bottomOutHint + "</td>\n");
            src.append("<td>" + timeToChoose / 1000 + "</td>\n");
            src.append("<td>" + timeToAnswer / 1000 + "</td>\n");
            if (emotion != null) {
                src.append("<td>" + emotion + "</td>\n");
                src.append("<td>" + level + "</td>\n");
            }
            else {
                src.append("<td></td><td></td>");
            }
        }



        //PROCESS THE STUDENT STATE
        if ( shouldPrintDetails(probId, mode))   {


            if ( solved == 0 && incAttempts == 0 && numHints == 0 && numHelpAids == 0)
                studentState = "SKIP" ;
            else if ( timeToChoose <= 4000 && timeToChoose > 0)  // Not reading, whatever they do after
                studentState = "NOTR" ;
            else if ( timeToAnswer == 0  && solved == 0 ) //Reading and some action, but gave up. Did not answer.
                studentState = "GIVEUP" ;
            else if ( solvedOnFirst == 1 && numHints == 0 && numHelpAids == 0)   //Reading and solved on First Attempt, without help
                studentState = "SOF" ;
            else if ( bottomOutHint ) //Reading and solved, But saw the last choose_x hint
                studentState = "BOTTOMOUT" ;
            else if ( timeToAnswer > 0 && ( numHints > 0 || numHelpAids > 0) ) //Reading and solved with Help (might have accepted when offered)
                studentState = "SHINT" ;
            else if ( timeToAnswer > 0 && incAttempts <= 1 ) //Reading and solved with no hints in at most 2 Incorrect Attempts
                studentState = "ATT" ;
            else if ( timeToAnswer > 0 && incAttempts >= 1  ) //Solved Eventually, in 2-5 attempts, most likely guessing
                studentState = "GUESS" ;


            src.append("<td>" + studentState + "</td>\n");
        }
        else {
            studentState = null ;
            src.append("<td></td>") ;
        }
        
        src.append("</tr>");


         if ( studentState != null && (studentState.equals("SOF") || (studentState.equals("NOTR")) && solvedOnFirst == 1) ) {
            ResultSet rs=null;
            java.sql.PreparedStatement stmt=null;
            try {
//                String q = "select * from woproperty where objid=" + studId +
//                        " and property='" + StudentState.SAT_HUT_PROBLEMS_SOLVED_ON_FIRST +
//                        "' and value=" + probId ;
//
//                stmt = conn.prepareStatement(q);
//                rs = stmt.executeQuery();
                // THIS MAKES NO SENSE.   The report is alterring the data!.   If there is
                // not a problem listed as having been solved on first attempt,  the report
                // cannot just insert it into the list.   In doing this it is also
                // incorrectly setting the values in the woproperty table and violating
                // the uniqueness required of the objid,property,value, posistion fields.
                // 3/25/13 DAM commented out.  This code is wrong.
//                if ( ! rs.next()) {
//                    insertMissingProperty(conn, studId, StudentState.SAT_HUT_PROBLEMS_SOLVED_ON_FIRST, probId);
//                }
            }
            finally {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
            }
        }
    }


    private String getStudIdHTML(int studentID) {
        return ("<a href=\"WoAdmin?action=AdminViewReport&teacherId=" + teacherId + "&classId=" + classID +
                "&reportId=5&state=showReport\">" + studentID + "</a>");

    }

    private String getUserNameHTML(String uname) {
        return ("<a href=\"WoAdmin?action=AdminViewReport&teacherId=" + teacherId + "&classId=" + classID +
                "&reportId=5&state=showReport\">" + uname + "</a>");

    }


    private boolean shouldPrintDetails(int probId, String mode)  {
        return ! ( probId == 999 || mode.equals("ExampleProblem") || mode.equals("External WebSite")) ;
    }


}