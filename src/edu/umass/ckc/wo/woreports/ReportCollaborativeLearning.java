package edu.umass.ckc.wo.woreports;

import java.sql.* ;
import java.util.*;


//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.xml.JDOMUtils;
import edu.umass.ckc.wo.woreports.js.CSSFile;
import edu.umass.ckc.wo.woreports.js.JSFile;
import edu.umass.ckc.wo.woreports.js.JSFunction;
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
public class ReportCollaborativeLearning extends Report {

    boolean isV2Events = true ;
    Hashtable simultaneousStudents = new Hashtable();
    Hashtable simultaneousProblems = new Hashtable() ;

    int classID = 0;                     
    int teacherId;
    Connection conn ;
    int lastStudID = 0;
    int minStudId = 9753 ;
    int maxStudId = 9775 ;
    int dimension = maxStudId - minStudId + 1;

    int collaborations[][] = new int[dimension][dimension]  ;
    int disengagement[][] = new int[dimension][dimension]  ;
    int productiveBehaviors[][] = new int[dimension][dimension]  ;

    class StudentProblemInteraction extends java.lang.Object  {
        int probId;
        int studId;
        int sessNum ;
        int pedId ;
        boolean correct ;
        boolean hints ;
        String studentState = new String("") ;

        int solved = 0;
        int lastSessNum = 0;
        int lastTopicId = 0 ;
        int lastProbId = 0 ;
        int numHints = 0;
        int numHelpAids = 0;
        int timeToChoose = 0;
        int timeToAnswer = 0;
        int timeToFirstAnswer = 0;
        int timeToHint = 0;
        int incAttempts = 0;
        Time beginProblemTime ;
        Time endProblemTime ;
        Time studTimeWithinSession ;
        long studTimeInSystem = 0;
        long correctionOffsetInSession ;
        String hintname = null ;
        int topicId = -1 ;
        String username ;
        String emotion=null;
        String level ="-1";
        String mode = "" ;
        String nextProblemMode = "" ;
        int numPracticeProbsInTopic = 0 ;
        boolean isFirstProbOfSess = true;
        boolean endProblem=false;
        int practiceProbId=-1 ;
        TopicMasterySimulator topicMasteryTracker;

        Time beginEmotionRequestTime ;
        int elapsedSinceEmotionRequest = 0;
        boolean lastWasBeginOfProblem = false;
        int positionForSOF = 0 ;
        boolean newStudent = false;
        boolean lastWasEndOfEmotion = false;
        boolean PreviousToLastWasEndOfEmotion = false;
        boolean bottomOutHint = false ;


        public StudentProblemInteraction(int probid, int studid) {
            this.probId = probid ;
            this.studId = studid ;

            try {
                topicMasteryTracker = new TopicMasterySimulator(conn,new StudentModelMasteryHeuristic(conn));
            }
            catch (Exception e) {} ;


        }

    }


    class StudentsOnAProblem extends java.lang.Object {         ///One of these for each problemId
        Hashtable sp_Interactions = new Hashtable() ;  //Many different students here, for the same PID
        int probId ;

        StudentsOnAProblem(int probId, StudentProblemInteraction sp)    {
            this.probId = probId ;
            sp_Interactions.put(sp.studId, sp) ;
        }

        StudentsOnAProblem(int probId) {
           this.probId = probId ;
        }
        int HowManyStudentsOnThisProblem() {
            return sp_Interactions.size() ;
        }

        void add(StudentProblemInteraction sp) {
//            StudentProblemInteraction s = (StudentProblemInteraction) sp_Interactions.get(sp) ;
//            if (s == null)
                sp_Interactions.put(sp.studId, sp) ;
        }

        void remove(StudentProblemInteraction sp) {
            StudentProblemInteraction s = (StudentProblemInteraction) sp_Interactions.get(sp.studId) ;
            if (s != null)
                sp_Interactions.remove(sp.studId) ;

            Collection hashVal = sp_Interactions.values();
            Iterator   iter    = hashVal.iterator();

            while (iter.hasNext())
            {
                StudentProblemInteraction otherStudent = (StudentProblemInteraction) iter.next() ;
                collaborations[sp.studId-minStudId][otherStudent.studId-minStudId] ++ ;

                if (sp.studentState.equals("NOTR") || sp.studentState.equals("GIVEUP") || sp.studentState.equals("GUESS"))
                    disengagement[sp.studId-minStudId][otherStudent.studId-minStudId] ++ ;
                else if (sp.studentState.equals("ATT") || sp.studentState.equals("SHINT") || sp.studentState.equals("SOF"))
                    productiveBehaviors[sp.studId-minStudId][otherStudent.studId-minStudId] ++ ;

            }
        }
    }

    public ReportCollaborativeLearning() {
    }


    private void determineMinMaxStudentID() throws SQLException {
        String SQL = "SELECT min(id) as minstudid, max(id) as maxstudid " +
                "FROM Student " +
                "WHERE student.trialUser=0 and classid=" + this.classID ;  //"WHERE studid IN (select id from student where classid="+classId+") " +


        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

        if ( rs.next() ) {
            minStudId = rs.getInt("minstudid") ;
            maxStudId = rs.getInt("maxstudid") ;
            dimension = maxStudId - minStudId + 1 ;
            collaborations= new int[dimension][dimension]  ;
            disengagement = new int[dimension][dimension]  ;
            productiveBehaviors = new int[dimension][dimension]  ;


            for ( int i=0; i<dimension; i++)
                for ( int j=0; j<dimension; j++ )  {
                    disengagement[i][j] = 0 ;
                    collaborations[i][j] = 0 ;
                    productiveBehaviors[i][j]= 0 ;
                }
            
        }


    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        this.conn = conn ;
        ClassInfo cl = DbClass.getClass(conn, classId);
        teacherId = cl.getTeachid();
        String className = getClassName(cl);
        String table = getEventLogTable(cl);   // either EpisodicData2 or EventLog
        isV2Events = cl.isNewLog();

        classID = classId;

        determineMinMaxStudentID() ;

        // this query is going through the eventlog for each student in the class (in order by student)

        String SQL = "SELECT " + table + ".*, pedagogyId, username " +
                "FROM " + table + " LEFT JOIN Problem " +
                "ON " + table + ".problemid = problem.id " +
                "LEFT JOIN Student " +
                "ON studid = Student.id " ;

        SQL = SQL.concat("WHERE student.trialUser=0 and classid=" + classId + //"WHERE studid IN (select id from student where classid="+classId+") " +
                (isV2Events ? "" : " AND activityname NOT like 'pretest%' AND activityname NOT like 'posttest%' ") +
                " ORDER BY time");


        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);

//        this.src.append(generateHeader("Report Problem Activity for " + className));
        this.src.append(generateHeaderWithJS("Report Problem Activity for " + className,
                new JSFile[] { JSFile.JQUERY, DataTable.DATATABLE_JS},
                new JSFunction[] {DataTable.DATATABLE_JQUERY_DOCREADY_FUNCTION},
                new CSSFile[] {DataTable.DATATABLE_PAGE_CSS, DataTable.DATATABLE_TABLE_JUI_CSS,DataTable.DATATABLE_THEME_CSS} ));
        this.src.append(DataTable.BODY_TAG);
        this.src.append("<h3>Detail Problem Info for " + className + "</h3>\n");
        addLegend();
        addNavLinks(classId,cl.getTeachid());
        this.src.append(DataTable.DIV1);
        this.src.append(DataTable.DIV2);
        this.src.append(getTableBegin());



        createV2Report(SQL,req);
        
        this.src.append("</table></div></div></body></html>");


        System.out.println("\n\n--------------------ALL PROBLEMS") ;
        System.out.print(",") ;
        for ( int i=0; i<dimension; i++)
            System.out.print((minStudId + i) + ",");

        for ( int i=0; i<dimension; i++) {
            System.out.println() ;
            for ( int j=0; j<dimension; j++ ) {
                if (j==0) System.out.print((minStudId + i) + "," );
                System.out.print(collaborations[i][j] + ",") ;
            }
        }

        System.out.println("\n\n--------------------GOOD BEHAVIORS") ;
        System.out.print(",") ;
        for ( int i=0; i<dimension; i++)
            System.out.print((minStudId + i) + ",");

        for ( int i=0; i<dimension; i++) {
            System.out.println() ;
            for ( int j=0; j<dimension; j++ ) {
                if (j==0) System.out.print((minStudId + i) + "," );
                System.out.print(productiveBehaviors[i][j] + ",") ;
            }
        }

        System.out.println("\n\n--------------------BAD BEHAVIORS") ;
        System.out.print(",") ;
        for ( int i=0; i<dimension; i++)
            System.out.print((minStudId + i) + ",");

        for ( int i=0; i<dimension; i++) {
            System.out.println() ;
            for ( int j=0; j<dimension; j++ ) {
                if (j==0) System.out.print((minStudId + i) + "," );
                System.out.print(disengagement[i][j] + ",") ;
            }
        }

        System.out.println("\n\n--------------------GOOD BEHAVIOR RATIO") ;
        System.out.print(",") ;
        for ( int i=0; i<dimension; i++)
            System.out.print((minStudId + i) + ",");

        for ( int i=0; i<dimension; i++) {
            System.out.println() ;
            for ( int j=0; j<dimension; j++ ) {
                if (j==0) System.out.print((minStudId + i) + "," );

                if ( collaborations[i][j] > 0 )
                    System.out.print(productiveBehaviors[i][j]/new Double(collaborations[i][j]) + ",") ;
                else
                    System.out.print(",") ;
            }
        }

        System.out.println("\n\n--------------------GOOD BEHAVIOR RATIO") ;
        System.out.print(",") ;
        for ( int i=0; i<dimension; i++)
            System.out.print((minStudId + i) + ",");

        for ( int i=0; i<dimension; i++) {
            System.out.println() ;
            for ( int j=0; j<dimension; j++ ) {
                if (j==0) System.out.print((minStudId + i) + "," );

                if ( collaborations[i][j] > 0 )
                    System.out.print(productiveBehaviors[i][j]/new Double(collaborations[i][j]) + ",") ;
                else
                    System.out.print(",") ;
            }
        }

        return this;
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
    private void createV2Report (String SQL, HttpServletRequest req) throws Exception {
        Statement classSt1 = conn.createStatement();
        ResultSet rs = classSt1.executeQuery(SQL);
        int probId=-1, studId=-1;
        StudentProblemInteraction sp = null ;
        while (rs.next()) {
            studId = rs.getInt("studid");
            probId = rs.getInt("problemId") ;

            sp = (StudentProblemInteraction) simultaneousStudents.get(new Integer(studId)) ;
            Integer thisStudId = new Integer(studId) ;

            if ( probId > 0 && probId != 999 ) {
                if ( sp == null ) {
                    sp = new StudentProblemInteraction(probId, studId) ;
                }

                if ( simultaneousStudents != null && simultaneousStudents.get( thisStudId ) == null )
                    simultaneousStudents.put(studId, sp);

                if ( simultaneousProblems!=null && simultaneousProblems.get(probId) == null )
                    simultaneousProblems.put(probId, new StudentsOnAProblem(probId, sp)) ;

                else {
                    StudentsOnAProblem studsOnP = (StudentsOnAProblem) simultaneousProblems.get(probId) ;
                    studsOnP.add(sp);
                }


                sp.username = rs.getString("username");
                sp.pedId = rs.getInt("pedagogyId");
                sp.sessNum = rs.getInt("sessnum");
                sp.topicId = rs.getInt("curtopicId");  // for older events this will be null
                String action = rs.getString("action");
                String userInput = rs.getString("userInput");
                String activityName = rs.getString("activityName") ;

                sp.hintname = rs.getString("hintstep") ;

                if (sp.sessNum != sp.lastSessNum)   {
                    sp.isFirstProbOfSess = true;
                    sp.lastSessNum = sp.sessNum;
                }
                else sp.isFirstProbOfSess = false;


                if (rs.wasNull())
                    sp.topicId = -1;

                // When topic changes let the topic updater know
                if (sp.topicId != -1 && sp.topicId != sp.lastTopicId) {
                    sp.topicMasteryTracker.newTopic(sp.topicId);
                    sp.lastTopicId = sp.topicId;
                    sp.numPracticeProbsInTopic = 0 ;
                }

                sp.lastProbId = probId;
                sp.probId = Integer.parseInt(rs.getString("problemId"));

                if (sp.probId == 999 ) {
                    sp.mode = "Topic Intro" ;
                }else {
                    sp.mode = sp.nextProblemMode ;
                }

                boolean isEmotionEvent = isEmotionEvent(probId, action, userInput);
                if (!isEmotionEvent) 
                    processProblem2(sp, action, rs, req, activityName);
                else
                    processEmotion2(sp,  sp.sessNum,  rs, userInput);

            }
        } //while
    }

    private boolean isEmotionEvent(int probId, String action, String userInput) throws Exception {

        // TODO we must add conditions so that we know the intervention is about clips
        // The problem is that we don't really know it until the InputResponse event
        if (action.equalsIgnoreCase("beginIntervention") || action.equalsIgnoreCase("endIntervention"))
            return true;
        else if (action.equalsIgnoreCase("InputResponse") && isEmotionSurveyResponse(userInput))
            return true;
        else return false;
    }

    private void processTimes(StudentProblemInteraction sp, int sessNum, long elapsedTime, Time beginEventTime, int probElapsed) {

        if (sp.lastSessNum == 0 || sp.lastSessNum != sessNum) {
            sp.studTimeWithinSession = new Time (probElapsed);
            sp.correctionOffsetInSession = elapsedTime - probElapsed;
        } else {                                                                                                                          
            sp.studTimeWithinSession = beginEventTime ; // - sp.correctionOffsetInSession);
        }

        sp.lastSessNum = sessNum;

        if (lastStudID == 0 || lastStudID != sp.studId) {
            sp.studTimeInSystem = probElapsed;
        } else {
            sp.studTimeInSystem += probElapsed;
        }

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
                "  <th> Time in Session </th>\n" +
                "  <th> Minutes in Tutor </th>\n" +
                "  <th> Problem Name </th>\n" +
                "  <th> Mode </th>\n" +
                "  <th> Solved First Attempt? </th>\n" +
                "  <th> Incorrect Attempts </th>\n" +
                "  <th> Hints Seen </th>\n" +
                "  <th> Help Aids </th>\n" +
                "  <th> BottomOut Hint </th>\n" +
                "  <th> Seconds To First Attempt </th>\n" +
                "  <th> Seconds To Solve </th>\n" +
                "  <th> Emotion After </th>\n" +
                "  <th> Emotion Level </th>\n" +
                "  <th> Cur TopicId </th>\n" +
                "  <th> TopicName </th>\n" +
                "  <th> Topic Mastery </th>\n" +
                "  <th> Student State </th>\n" +

                " </tr></thead>\n" +
                " <tfoot><tr>\n" +
                "  <th> User Name  </th>\n" +
                "  <th> Student ID  </th>\n" +
                "  <th> Pedagogy </th>\n" +
                "  <th> Session Number </th>\n" +
                "  <th> Begin Problem Time </th>\n" +
                "  <th> End Problem Time </th>\n" +
                "  <th> Time in Tutor </th>\n" +
                "  <th> Problem Name </th>\n" +
                "  <th> Mode </th>\n" +
                "  <th> Solved First Attempt? </th>\n" +
                "  <th> Incorrect Attempts </th>\n" +
                "  <th> Hints Seen </th>\n" +
                "  <th> Help Aids </th>\n" +
                "  <th> Bottom Out Hint </th>\n" +
                "  <th> Seconds To First Attempt </th>\n" +
                "  <th> Seconds To Solve </th>\n" +
                "  <th> Emotion After </th>\n" +
                "  <th> Emotion Level </th>\n" +
                "  <th> Cur TopicId </th>\n" +
                "  <th> TopicName </th>\n" +
                "  <th> Topic Mastery </th>\n" +
                "  <th> Student State </th>\n" +

                " </tr></tfoot>\n" +
                "<tbody id=\"data\">\n");
        return sb.toString();

    }


    private void processProblem2(StudentProblemInteraction sp, String action,
                                 ResultSet rs, HttpServletRequest req, String activityName) throws java.lang.Exception {

        if (action.equalsIgnoreCase("beginProblem")) {

            //Print out everything for the last problem in record
            if ( ! sp.isFirstProbOfSess) {
                processTimes(sp, sp.sessNum, rs.getLong("elapsedTime"), sp.beginProblemTime, rs.getInt("probElapsed"));

                printReport2(sp, req);

                StudentsOnAProblem studsOnThisProb = (StudentsOnAProblem) this.simultaneousProblems.get(sp.probId) ;

                if (studsOnThisProb != null ) {
                       studsOnThisProb.remove(sp);
                       if ( studsOnThisProb.sp_Interactions.size() == 0 )
                            simultaneousProblems.remove(sp.probId) ;
                }

            }

            //Initialize everything again
            sp.beginProblemTime = rs.getTime("time");
            sp.emotion=null;
            sp.level ="-1";
            sp.timeToChoose = 0;
            sp.timeToFirstAnswer = 0;
            sp.timeToAnswer = 0;
            sp.timeToHint = 0;
            sp.numHints = 0;
            sp.numHelpAids = 0;
            sp.solved = 0;
            sp.incAttempts = 0;
            sp.lastWasBeginOfProblem = true;
            sp.PreviousToLastWasEndOfEmotion = sp.lastWasEndOfEmotion;
            sp.lastWasEndOfEmotion = false;
            sp.bottomOutHint = false ;

            sp.mode = sp.nextProblemMode ;
        }

        if (action.equalsIgnoreCase("attempt")) {

            if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) &&
                    (sp.timeToAnswer == 0)) {
                sp.timeToAnswer = Integer.parseInt(rs.getString("probElapsed"));

                if (sp.timeToChoose == 0)  //this is the first attempt, and it is correct
                    sp.solved = 1;
            }
            if (sp.timeToChoose == 0)  {
                sp.timeToChoose = Integer.parseInt(rs.getString("probElapsed"));
                sp.timeToFirstAnswer = sp.timeToChoose;
            }

            if ((Integer.parseInt(rs.getString("isCorrect"))) == 0
                    && sp.incAttempts < 4)  // A hack because somehow students could get more than 4 incorrect answers
                sp.incAttempts++;
        } else if (action.toLowerCase().startsWith("hint")) {
            if (sp.timeToHint == 0)
                sp.timeToHint = Integer.parseInt(rs.getString("probElapsed"));

            if (sp.numHints < 7) {  // A threshold just in case students make hectic choose_a clicks
                sp.numHints++;
                sp.numHelpAids ++ ;
            }

            if (sp.hintname.startsWith("choose")) {
                sp.bottomOutHint = true ;
            }

        } else if (action.equalsIgnoreCase("ShowExample") || action.equalsIgnoreCase("ShowVideo") || action.equalsIgnoreCase("ShowSolveProblem")) {
            sp.numHelpAids ++ ;

            if (action.equalsIgnoreCase("ShowSolveProblem"))
                sp.bottomOutHint = true ;


        } else if (action.equalsIgnoreCase("endProblem")) {
            sp.endProblem=true;
            sp.endProblemTime = rs.getTime("time") ;

            if ( sp.mode.equals("PracticeProblem"))  {
                sp.numPracticeProbsInTopic ++ ;
                //mode = "practice" ;
            }

            sp.topicMasteryTracker.updateMastery(sp.probId, sp.numHints,sp.solved,sp.incAttempts,sp.timeToFirstAnswer,
                    sp.numPracticeProbsInTopic, sp.mode );   // Check problem mode!!!!
            //mode  = null ;
        }  // end endProblem

        else if (action.equalsIgnoreCase("NextProblem"))  {
            if ( ! isIntervention(activityName) ) {
                sp.nextProblemMode =  activityName ;
            }
        }
    }


    private boolean isIntervention(String activityName) {
        return activityName.contains("Intervention") && ! activityName.contains("BarGraph") ;
    }
    // The problem with this method is that it assumes that an intervention following a problem
    // is about clips.  In v2 it may not be.
    private void processEmotion2(StudentProblemInteraction sp, int sessNum, ResultSet rs, String userInput) throws java.lang.Exception {
        // emotion survey (interventions) are done after endProblem=true
        if (sp.endProblem && rs.getString("action").equalsIgnoreCase("beginIntervention")) {
            sp.beginEmotionRequestTime = new Time(rs.getLong("time")) ; // - rs.getLong("probElapsed"));
            sp.elapsedSinceEmotionRequest = rs.getInt("probElapsed");
        }
        // the intervention response is different in v2 vs v1
        else if (sp.endProblem &&
                rs.getString("action").equalsIgnoreCase("InputResponse") &&
                isEmotionSurveyResponse(userInput)) {
            processTimes(sp, sessNum, rs.getLong("elapsedTime"), sp.beginEmotionRequestTime, sp.elapsedSinceEmotionRequest);
            Element e = JDOMUtils.getRoot(userInput);
            if (e.getName().equals("userInput")) {
                sp.emotion= e.getAttributeValue("emotion") ;
                sp.level = e.getAttributeValue("level") ;
            }
        }
    }

    private boolean isEmotionSurveyResponse(String userInput) throws Exception {

        if ( userInput.startsWith("<")) {
            System.out.println(userInput) ;
            Element root = JDOMUtils.getRoot(userInput);
            Attribute affect = root.getAttribute("emotion");
            return affect != null;
        }

        return false ;
    }


    private void printReport2(StudentProblemInteraction sp, HttpServletRequest req) throws java.lang.Exception {

        Statement classSt2 = conn.createStatement();
        ResultSet rs2 = classSt2.executeQuery("SELECT Problem.name, Problem.form, " +
                "Problem.nickname, Problem.animationresource, Problem.id FROM Problem WHERE Problem.id = " + sp.probId);
        String gifName = "null";
        String link = "null";
        String probNumber;

        if (rs2.next()) {
            gifName = rs2.getString("name");
            // DAM 11/19/08 patch repair because problems with bad names are included in the result list.
            if (gifName.startsWith("problem_") ) {
                probNumber = gifName.substring(8);
                link = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + probNumber;
            }
            else if (rs2.getString("form").equalsIgnoreCase("ExternalURL")) {
                probNumber = gifName ;
                link = rs2.getString("animationresource");
            }
            else {
                probNumber = "null";
                System.out.println("Warning: Badly formed problem name " + gifName);
            }

            if (sp.probId == 999 ) {
                gifName = "Introduction Screen";
                link = "Introduction to Topic";
            }
        }


        int collaboration = 0;
        //Change the color of the row depending on collaboration
//        String bgcolor = new String("#FFFFFF");
        // to add color coding to a row use classes in <tr> that have a class from the css as in <tr class="gradeA">
        StudentsOnAProblem studentsOnThisProblem =
                (StudentsOnAProblem) simultaneousProblems.get(new Integer(sp.probId)) ;

        if ( studentsOnThisProblem != null && studentsOnThisProblem.HowManyStudentsOnThisProblem() > 2 )
            collaboration = 2;
        else if ( studentsOnThisProblem != null && studentsOnThisProblem.HowManyStudentsOnThisProblem() > 1 )
            collaboration = 1;

        if (collaboration == 2)
            src.append("<tr class=\"hard2\">\n");
        else if (collaboration == 1)
            src.append("<tr class=\"hard1\">\n");

        else src.append("<tr>\n");
        src.append("<td>" + getUserNameHTML(sp.username) + "</td>\n");
        src.append("<td>" + getStudIdHTML(studentsOnThisProblem) + "</td>\n");
        src.append("<td>" + sp.pedId + "</td>\n");
        src.append("<td>" + sp.sessNum + "</td>\n");
        src.append("<td>" + sp.beginProblemTime + "</td>\n");
        src.append("<td>" + sp.endProblemTime + "</td>\n");
        src.append("<td>" + sp.studTimeInSystem/60000 + "</td>\n");

        //REGARDING THE PROBLEM NAME AND MODE
        if (sp.probId == 999)
            src.append("<td><b> Topic Intro </b></td>\n");

        else {
            src.append("<td><a href=\"" + link + "\"><b>" + gifName + "</b></a></td>\n");

            if ( ! gifName.startsWith("problem"))
                sp.mode = "External WebSite" ;
        }

        src.append("<td>" + sp.mode + "</td>\n");
        src.append("<td>" + sp.solved + "</td>\n");
        src.append("<td>" + sp.incAttempts + "</td>\n");
        src.append("<td>" + sp.numHints + "</td>\n");
        src.append("<td>" + sp.numHelpAids + "</td>\n");
        src.append("<td>" + sp.bottomOutHint + "</td>\n");
        src.append("<td>" + sp.timeToChoose / 1000 + "</td>\n");
        src.append("<td>" + sp.timeToAnswer / 1000 + "</td>\n");
        if (sp.emotion != null) {
            src.append("<td>" + sp.emotion + "</td>\n");
            src.append("<td>" + sp.level + "</td>\n");
        }
        else {
            src.append("<td></td><td></td>");
        }
        if (sp.topicId > 0) {
            String tname = DbTopics.getTopicName(conn,sp.topicId);
            if (tname.length() > 12)
                tname= tname.substring(0,12);
            src.append("<td>" + sp.topicId + "</td>\n");
            src.append("<td>" + tname + "</td>\n");

            src.append("<td>" + String.format("%4.3f",sp.topicMasteryTracker.getMastery()) + "</td>\n");

        }
        else {
            src.append("<td></td><td></td><td></td>");
        }

        long timespan = -1 ;

        if (sp != null && sp.endProblemTime != null && sp.beginProblemTime != null)
            timespan = (sp.endProblemTime.getTime() - sp.beginProblemTime.getTime()) ;

        if ( sp.probId != 999)   {
            if ( ( timespan > -1 && timespan <= 5000 ) || (sp.timeToChoose>0 && sp.timeToChoose<=5000))  // Not reading, whatever they do after
                sp.studentState = "NOTR" ;
            else if ( sp.solved > 0 && sp.numHelpAids == 0 )   //Reading and solved on First Attempt, without help
                sp.studentState = "SOF" ;
            else if ( sp.bottomOutHint ) //Reading and solved with at most 2 incorrect attempts, But saw Help (might have accepted when offered)
                sp.studentState = "BOTTOMOUT" ;
            else if ( sp.timeToAnswer == 0  ) //Reading and gave up. Did not answer.
                sp.studentState = "GIVEUP" ;
            else if ( sp.timeToAnswer > 0 && sp.numHelpAids > 0 && sp.incAttempts <= 2 ) //Reading and solved with at most 2 incorrect attempts, But saw Help (might have accepted when offered)
                sp.studentState = "SHINT" ;
            else if ( sp.timeToAnswer > 0 &&  sp.numHelpAids == 0 && sp.incAttempts <= 2 ) //Reading and solved with no hints in at most 2 Incorrect Attempts
                sp.studentState = "ATT" ;
            else if ( sp.timeToAnswer > 0 && sp.incAttempts > 3  ) //Solved Eventually, in 4-5 attempts, most likely guessing
                sp.studentState = "GUESS" ;
        }
        else
            sp.studentState = "" ;

        if (sp.studentState.equals("NOTR") || sp.studentState.equals("GIVEUP") || sp.studentState.equals("GUESS"))
            disengagement[sp.studId-minStudId][sp.studId-minStudId] ++ ;
        else if (sp.studentState.equals("ATT") || sp.studentState.equals("SHINT") || sp.studentState.equals("SOF"))
            productiveBehaviors[sp.studId-minStudId][sp.studId-minStudId] ++ ;

        collaborations[sp.studId-minStudId][sp.studId-minStudId] ++ ;

        if ( sp.studentState != null )
            src.append("<td>" + sp.studentState + "</td>\n");


        else
            src.append("<td></td>") ;

        src.append("</tr>");
//            src.append(" </tr>\n");


        if ( sp.studentState != null && (sp.studentState.equals("SOF")
                || (sp.studentState.equals("NOTR")) && sp.solved>0 && sp.numHelpAids == 0) ) {
            ResultSet rs=null;
            java.sql.PreparedStatement stmt=null;
            try {
//                String q = "select * from woproperty where objid=" + sp.studId +
//                        " and property='" + StudentState.SAT_HUT_PROBLEMS_SOLVED_ON_FIRST +
//                        "' and value=" + sp.probId ;
//
//                stmt = conn.prepareStatement(q);
                rs = stmt.executeQuery();
                // THIS MAKES NO SENSE.   The report is alterring the data!.   If there is
                // not a problem listed as having been solved on first attempt,  the report
                // cannot just insert it into the list.   In doing this it is also
                // incorrectly setting the values in the woproperty table and violating
                // the uniqueness required of the objid,property,value, posistion fields.
                // 3/25/13 DAM commented out.  This code is wrong.
//                if ( ! rs.next()) {
//                    insertMissingProperty(sp.studId, StudentState.SAT_HUT_PROBLEMS_SOLVED_ON_FIRST, sp.probId);
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





    private String getStudIdHTML(StudentsOnAProblem students) {

        Collection hashVal = students.sp_Interactions.values();
        Iterator   iter    = hashVal.iterator();
        String studIds = new String("") ;

        while (iter.hasNext())
        {
           if ( ! studIds.equals("") )
                studIds = studIds.concat("-") ;

          StudentProblemInteraction thisstudent = (StudentProblemInteraction) iter.next();
          studIds = new String(studIds + new Integer(thisstudent.studId).toString() +
                  "(" + thisstudent.username + ")") ;
        }


        return ("<a href=\"WoAdmin?action=AdminViewReport&teacherId=" + teacherId + "&classId=" + classID +
                "&reportId=5&state=showReport\">" + studIds + "</a>");

    }

    private String getUserNameHTML(String uname) {
        return ("<a href=\"WoAdmin?action=AdminViewReport&teacherId=" + teacherId + "&classId=" + classID +
                "&reportId=5&state=showReport\">" + uname + "</a>");

    }

}