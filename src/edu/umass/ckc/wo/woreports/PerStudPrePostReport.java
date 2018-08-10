package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.*;
import java.text.DecimalFormat;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.handler.ReportHandler;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.chartData.DataSeries;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.*;


/**
 * The WITHIN TUTOR pretest scores, and pretest and possttest quizzes of each student before and after getting tutoring.
 * User: Ivon
 * Date: Mar 30, 2012
 * Time: 4 PM
 */


/*  ASSUMES THE FOLLOWING TYPICAL SEQUENCE OF ACTIONS, and ONLY considers this actions, ignores everything else
        BeginProblem
        Attempt
        Hint
        InputResponse WITH  userinput=<selection>yes-hint</selection>
        ShowSolveProblem
        NextProblem
        EndProblem
 */

public class PerStudPrePostReport extends Report {
    int minTopicSize = 6;
    int numAsessmentProblems = 3 ;

    String[] xAxisLabels = {"< 30 min", "30-60 min", "60-90 min", ">90 min", "All Students"};
    enum timeCategories {c0_30,c30_60,c60_90,c_90, all};
    int[] N_cases = {0,0,0,0,0} ;
    boolean isNewLog;
    int studId;
    int studTopicsWithinPretest = 0 ;
    int studTopicsWithinPostest = 0 ;
    double decPrecision=10.0;
    String prepostlogTableName;
    Connection conn;


    class PerformancePoint {
        int probId;     // not strictly necessary but putting in for verification that things are in synch as the data is gathered
        boolean correct ;
        boolean hints ;
        boolean hintsBeforeCorr;
        double diff_level;
        int attempts;
        boolean skipped=false;

        PerformancePoint(int probId, boolean corr, int attempts, boolean hinted, boolean hintsbefcorr, double diff_level) {
            this.probId = probId ;
            this.correct = corr ;
            this.hints = hinted ;
            this.diff_level = diff_level;
            this.attempts = attempts ;
            this.hintsBeforeCorr = hintsbefcorr ;

            if (attempts==0 && !hints)
                skipped = true ;
        }

        double getProblemScore() {
            //SOLVED ON FIRST
            //SOLVED WITH HINTS = INCORRECT
            if ( attempts==1 && correct && ! hintsBeforeCorr )
                return 1 ;

            else if ( correct && hintsBeforeCorr )
                return 0.5 ;

            return 0 ;
        }

        double getDiffLevel(){
            //returns the diff_level of each problem
            return diff_level;
        }
    }

    class TopicData {
        int topicId;
        List<PerformancePoint> history;
        int rowIndex;

        TopicData (int tid) {
            topicId = tid;
            history = new ArrayList<PerformancePoint>();
        }
    }

    public PerStudPrePostReport() {
    }



    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {
        this.conn =conn;
        double count_casesWithin =0 ;
        double count_casesQuiz =0 ;
        int acum_gainWithin =0 ;
        int acum_gainQuiz =0 ;
        ArrayList learningGainsWithin  = new ArrayList();
        ArrayList learningGainsQuiz  = new ArrayList();
        double[] avgGain_per_category = new double[xAxisLabels.length];
        int[] num_per_category = new int[xAxisLabels.length];

        // the first time the report is run, no sliderValue is passed, so use a very high default
        // of 100 min so that no problems will be omitted.
        String sliderTimeMin = req.getParameter("sliderValue");
        long testTime = (sliderTimeMin != null) ? Long.parseLong(sliderTimeMin) : 100;
        // test time is given in minutes;
        long testTimeMs = testTime*1000*60;
        ClassInfo cl = new DbClass().getClass(conn,classId);
        String className = getClassName(cl);
        prepostlogTableName = getPrePostEventLogTable(cl); // either EpisodicData2 or PrePostEventLog
        String logTableName = getEventLogTable(cl);  // either EpisodicData2 or EventLog
        isNewLog = cl.isNewLog();

        //this.src.append(generateHeader("Report 10 " + className));
        this.src.append(generateHeader2("Per Student Pre/Post Performance" + className));

        this.src.append("<h3>Pretest/Postest Info for " + className + "</h3>\n");
        addNavLinks(classId,cl.getTeachid());


        // add in the slider control
        this.src.append("Upper Time Threshold for the Quizzes (e.g. 10 would give student performance in the quiz during the first 10 minutes) <br>\n" +
                "<form action=\"\" method=\"get\" name=\"demoForm\">\n" +
                "<input type=\"hidden\" name=\"action\" value=\"AdminViewReport\">\n"+
                "<input type=\"hidden\" name=\"classId\" value=\""+cl.getClassid()+"\">\n"+
                "<input type=\"hidden\" name=\"teacherId\" value=\""+cl.getTeachid()+"\">\n"+
                "<input type=\"hidden\" name=\"reportId\" value=\""+ ReportHandler.PER_STUD_PREPOST_HTML+"\">\n"+
                "<input type=\"hidden\" name=\"state\" value=\"showReport\">\n"+
                "<input name=\"sliderValue\" id=\"sliderValue\" type=\"Text\" size=\"3\">\n" +
                "<script language=\"JavaScript\">\n" +
                "\tvar A_TPL = {\n" +
                "\t\t'b_vertical' : false,\n" +
                "\t\t'b_watch': true,\n" +
                "\t\t'n_controlWidth': 120,\n" +
                "\t\t'n_controlHeight': 16,\n" +
                "\t\t'n_sliderWidth': 16,\n" +
                "\t\t'n_sliderHeight': 15,\n" +
                "\t\t'n_pathLeft' : 1,\n" +
                "\t\t'n_pathTop' : 1,\n" +
                "\t\t'n_pathLength' : 103,\n" +
                "\t\t's_imgControl': 'img/blueh_bg.gif',\n" +
                "\t\t's_imgSlider': 'img/blueh_sl.gif',\n" +
                "\t\t'n_zIndex': 1\n" +
                "\t}\n" +
                "\tvar A_INIT = {\n" +
                "\t\t's_form' : 0,\n" +
                "\t\t's_name': 'sliderValue',\n" +
                "\t\t'n_minValue' : 0,\n" +
                "\t\t'n_maxValue' : 100,\n" +
                "\t\t'n_value' : " + ((sliderTimeMin != null) ? sliderTimeMin : "100") + ",\n" +
                "\t\t'n_step' : 1\n" +
                "\t}\n" +
                "\n" +
                "\tnew slider(A_INIT, A_TPL);\n" +
                "</script>\n" +
                "\n" +
                "<input name=\"Submit\" type=\"Submit\" value=\"Submit\">\n" +
                "\n" +
                "\n" +
                "</form>");

        String neck2 = "<table class=\"example sort02 table-autosort:2\">\n" +
                "<thead> <tr>\n" +
                "<th class=\"table-sortable:default\" width=140>Student</th>\n" +
                "  <th class=\"table-sortable:numeric\"># Topics Seen </th>\n" +
                "  <th class=\"table-sortable:numeric\">Within-Tutor Pretest % Estimation </th>\n" +
                "  <th class=\"table-sortable:numeric\">Within-Tutor Posttest % Estimation </th>\n" +
                "  <th class=\"table-sortable:numeric\">SAT-Hut problems seen</th>\n" +
                "  <th class=\"table-sortable:numeric\">SAT-Hut time (minutes)</th>\n" +
                "  <th class=\"table-sortable:numeric\">Learning %Gain Estimation</th>\n" +
                "  <th class=\"table-sortable:numeric\">Group</th>\n" +
                " </tr></thead>\n" +
                "<tbody id='data'>\n";
        StringBuffer rows = new StringBuffer();




        String SQL = "SELECT Student.id, Student.fname, Student.lname, Student.username, pedagogyId  " +
                "FROM Student " +
                "WHERE student.trialUser=0 and Student.classId = " + Integer.toString(classId) + " " +
                "ORDER BY Student.lname;";
        Statement studSt = conn.createStatement();
        ResultSet rs2 = studSt.executeQuery(SQL);


        while (rs2.next()) {

            studId = rs2.getInt("Student.id");
            String fname = rs2.getString("Student.fname");
            String lname = rs2.getString("Student.lname");
            String uname = rs2.getString("Student.username");
            int mygroupnumber = rs2.getInt("pedagogyId") ;
            SQL = "SELECT count(distinct problemid) as satSeen, sum(problemEndTime - problemBeginTime) as satTime from studentproblemhistory where studid=" + studId ;

            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            Long _satTime = new Long(0);
            Integer _satSeen = new Integer(0) ;

            while (rs.next()) {
                _satSeen = rs.getInt("satSeen") ;
                _satTime = rs.getLong("satTime") /60 ;
            }
/*
            String objid=null;

            Hashtable t = new Hashtable();
            while (rs.next()) {
                objid = rs.getString("objid");
                String property = rs.getString("property");
                String value = rs.getString("value");
                String numentries = rs.getString("numentries") ;
                Object o = t.get(objid);
                if (o == null) {
                    ArrayList l = new ArrayList();

                    /// st.satHutProblemsGiven has the value we need in maxpos, not in value like the rest
                    if ( ! property.startsWith(StudentState.SAT_HUT_PROBLEMS_GIVEN) &&
                            ! property.startsWith(StudentState.PRE_TEST_PROBLEMS_GIVEN) &&
                            ! property.startsWith(StudentState.POST_TEST_PROBLEMS_GIVEN))
                        l.add(new TwoTuple(property, value));
                    else
                        l.add(new TwoTuple(property, numentries));

                    t.put(objid, l);

                } else {
                    ArrayList l = (ArrayList) o;
                    if ( ! property.startsWith(StudentState.SAT_HUT_PROBLEMS_GIVEN) &&
                            ! property.startsWith(StudentState.PRE_TEST_PROBLEMS_GIVEN) &&
                            ! property.startsWith(StudentState.POST_TEST_PROBLEMS_GIVEN))
                        l.add(new TwoTuple(property, value));
                    else
                        l.add(new TwoTuple(property, numentries));
                }
            }
            // get the total time a student spent in SAT hut problems
            SQL = "SELECT sum(probElapsed)/60000 as pelapsed, count(probelapsed) as nprobs  from "+logTableName+
                    " where studId=? and  action='EndProblem' and probElapsed>0 and problemid<999";

            ps = conn.prepareStatement(SQL);
            ps.setInt(1, studId);
            rs = ps.executeQuery();


            if (rs.next() && objid != null) {
                _satTime= rs.getLong("pelapsed");     //These Are Minutes


                List tupleList = (List) t.get(objid);
                tupleList.add(new TwoTuple(BaseStudentModel.TOTAL_SOLVE_TIME,Long.toString(_satTime)));

                _satSeen= rs.getInt("nprobs");

            }



            Iterator itr = t.keySet().iterator();
            // iterate through each student in the table (objids)
            while (itr.hasNext()) {
                String k = (String) itr.next();
                ArrayList l = (ArrayList) t.get(k);
//                String satSeen = getProp(StudentState.SAT_HUT_PROBLEMS_GIVEN, l);
                //Integer _satSeen = (satSeen.startsWith("N/A")) ?  new Integer(0) :  new Integer(satSeen) ;

                //String satTotSolveTime = getProp(BaseStudentModel.TOTAL_SOLVE_TIME, l);
                //Long _satTime =  (satTotSolveTime.startsWith("N/A")) ?  new Long(0) :  new Long(satTotSolveTime) ;
                //_satTime /= 60*1000; // convert ms to min
                long preTestStartTime = getTestStartTime(conn,studId,prepostlogTableName,"pretestProblem");

                int[] counts = getProblemCounts(preTestStartTime,preTestStartTime+testTimeMs,"pretestProblem");
                double preCorrect=counts[0];
                double preProblemsSeen=counts[1];
                double pretotal_d = counts[2];
                double _prePerc = (pretotal_d < 1) ? 0.0 : (preCorrect / pretotal_d)*100;
//Get the true Posttest Score from the appropriate log table
                long postTestStartTime = getTestStartTime(conn,studId,prepostlogTableName,"posttestProblem");

                counts = getProblemCounts(postTestStartTime,postTestStartTime+testTimeMs,"posttestProblem");
                double poscorr_d=counts[0];
                double posProblemsSeen=counts[1];
                double postotal_d=counts[2];
                double _postPerc = (postotal_d < 1)? 0.0 :  (poscorr_d / postotal_d)*100 ;

                */
                double withinPretest = calculateWithinTutorPretest(studId)  ;
                double withinPostest = calculateWithinTutorPostest(studId)  ;
                double gainWithin = withinPostest-withinPretest;


// Get the true learning Gain
 //               double gainQuizzes = _postPerc - _prePerc ;
                //gain = java.lang.Math.round(gain) ;
                //learningGainsWithin.add(new Double(gain)) ;
                //Determine the color of the row

                rows.append(" <tr>\n") ;

                String bgcolor= new String("#FFFFFF") ;
                // checks if didn't do enough pre or post test probs
//                if ( (pretotal_d > 0 && postotal_d > 0)  && (pretotal_d < 11 || postotal_d < 11) )    {
//                    bgcolor=new String("#999999") ;
//                }
                // checks if didn't do enough sat probs
                if ( _satSeen.intValue() < 25 )
                    bgcolor=new String("#CCCCCC") ;

                if (withinPostest>= 0 && withinPretest >= 0) {
                    count_casesWithin ++ ;
                    acum_gainWithin+=gainWithin ;
                    learningGainsWithin.add(new Double(gainWithin)) ;
                    storeGainInCategory(avgGain_per_category,num_per_category,_satTime,gainWithin);
                }


                rows.append("<td" + " bgcolor=" + bgcolor +
                        "> <a href=\"WoAdmin?action=AdminViewReport&classId=" + e.getClassId() +
                        "&reportId=5&studId="+ studId + "&teacherId=" + cl.getTeachid() + "&extraParam=" + gainWithin + "&state=showReport\">" +
                        lname + ", " + fname + "[" + uname + "]" + "</A></td>\n" +
                        "  <td" + " bgcolor=" + bgcolor + ">" + studTopicsWithinPretest + "</td>\n") ;

                rows.append("  <td" + " bgcolor=" + bgcolor + ">" + ((withinPretest < 0) ? "NA" : String.format("%4.1f", withinPretest)) + "</td>\n" +
                        "  <td" + " bgcolor=" + bgcolor + ">" + ((withinPostest < 0) ? "NA" : String.format("%4.1f", withinPostest)) + "</td>\n" ) ;
           //             "  <td" + " bgcolor=" + bgcolor + ">" + pretotal_d + "</td>\n" +
           //             "  <td" + " bgcolor=" + bgcolor + ">" + preProblemsSeen + "</td>\n" ) ;

           //     rows.append("  <td" + " bgcolor=" + bgcolor + ">" + String.format("%4.1f", new Double(_prePerc)) + "</td>\n" +
           //             "  <td" + " bgcolor=" + bgcolor + ">" + postotal_d + "</td>\n" ) ;

                rows.append(
           //             "  <td" + " bgcolor=" + bgcolor + ">" + posProblemsSeen + "</td>\n" +
           //                     "  <td" + " bgcolor=" + bgcolor + ">" + String.format("%4.1f",_postPerc) + "</td>\n" +
                                "  <td" + " bgcolor=" + bgcolor + ">" + _satSeen + "</td>\n" +
                                "  <td" + " bgcolor=" + bgcolor + ">" + _satTime + "</td>\n" ) ;

                //Gain From Quizzes --if ( withinPostest>0 && withinPretest > 0 )  {
                DecimalFormat dnf = new DecimalFormat("+00.0;-00.0") ;
                rows.append("<td" + " bgcolor=" + bgcolor + ">" + dnf.format(gainWithin) + "</td>\n") ;

            //    rows.append("<td" + " bgcolor=" + bgcolor + ">" + dnf.format(gainQuizzes) + "</td>\n") ;

                if ( mygroupnumber > 0 ) {
                    rows.append("<td>" + mygroupnumber + "</td>") ;
                }
                else {
                    rows.append("<td></td>") ;
                }
                rows.append("</tr>\n");
        }
        this.src.append(neck2);
        this.src.append(rows);

        //Print legend at the bottom
        this.src.append("<table> \n" ) ;
//        this.src.append("<tr> \n <td bgcolor=#999999 width=\"45\">&nbsp;</td>\n") ;
//        this.src.append("     <td>These students answered very few questions in the pretest or posttest, so the learning gain is not reliable. </td> \n </tr> ") ;
        this.src.append("<tr> \n <td bgcolor=#CCCCCC width=\"45\">&nbsp;</td>\n") ;
        this.src.append("     <td>These students saw very few problems in the SAT-Hut, so we shouldn't expect much improvement. </td> \n </tr> ") ;

        if ( count_casesWithin > 0 ) {
            this.src.append("<tr> \n <td></td><td bgcolor=#00FFFF> The MEAN LEARNING GAIN (computed from within-tutor performance) was " +
                    java.lang.Math.round((acum_gainWithin/count_casesWithin)*decPrecision)/decPrecision + "%, for " + count_casesWithin + " valid students. \n </td> </tr> ") ;

            this.src.append("<tr> \n <td></td><td bgcolor=#00FFFF> The MEDIAN LEARNING GAIN (computed from within-tutor performance) was " +
                    //java.lang.Math.round(getMedian(learningGainsWithin))
                    (java.lang.Math.round(getMedian(learningGainsWithin)*decPrecision))/decPrecision + "%, for " + count_casesWithin + " valid students. \n </td> </tr> ") ;
        }

        if ( count_casesQuiz > 0 ) {
            this.src.append("<tr> \n <td></td><td bgcolor=#00FFFF> The MEAN LEARNING GAIN (computed from quizzes) was " +
                    java.lang.Math.round((acum_gainQuiz/count_casesQuiz)*decPrecision)/decPrecision + "%, for " + count_casesQuiz + " valid students. \n </td> </tr> ") ;

            this.src.append("<tr> \n <td></td><td bgcolor=#00FFFF> The MEDIAN LEARNING GAIN (computed from quizzes) was " +
                    //java.lang.Math.round(getMedian(learningGainsWithin))
                    (java.lang.Math.round(getMedian(learningGainsQuiz)*decPrecision))/decPrecision + "%, for " + count_casesQuiz + " valid students. \n </td> </tr> ") ;
        }

        this.src.append("</table>") ;
        buildBarChartData(req, avgGain_per_category); // puts some data into the HttpSession
        this.src.append("<p><img src=\"BarChartServlet\">"); // inserts an image that calls BarChartServlet which gens a jpeg from data in HttpSession
        return this;
    }

    private void setRunningAvg (int i, double[] avgGain_per_category, int[] num_per_category,  double gain) {
        int n = ++num_per_category[i];
        avgGain_per_category[i] = (gain + (n-1)*avgGain_per_category[i]) / (double) n;
    }

    private void storeGainInCategory(double[] avgGain_per_category, int[] num_per_category, long satTime, double gain) {

        if (satTime <= 30 ) {
            setRunningAvg(timeCategories.c0_30.ordinal(),avgGain_per_category, num_per_category, gain);
            N_cases[0] ++ ;
        }
        else if (satTime > 30 && satTime <= 60) {
            setRunningAvg(timeCategories.c30_60.ordinal(),avgGain_per_category, num_per_category, gain);
            N_cases[1] ++ ;
        }

        else if (satTime > 60 && satTime <= 90)  {
            setRunningAvg(timeCategories.c60_90.ordinal(),avgGain_per_category, num_per_category, gain);
            N_cases[2] ++ ;
        }
        else if (satTime > 90 ) {
            setRunningAvg(timeCategories.c_90.ordinal(),avgGain_per_category, num_per_category, gain);
            N_cases[3] ++ ;
        }
        setRunningAvg(timeCategories.all.ordinal(),avgGain_per_category, num_per_category, gain);
        N_cases[4] ++ ;
    }


    private Double getMedian(ArrayList learningGains) {
        Collections.sort(learningGains);

        Double median = (Double) learningGains.get(learningGains.size()/2) ;

        return median ;
    }


    private void buildBarChartData(HttpServletRequest req, double[] avgGain_per_category) {

        String xAxisTitle = "Time spent in tutor";
        String yAxisTitle = "Learning gain %";
        String title = "Learning Gain by Time Spent in the 'Learning Hut'";

        for (int i=0; i<xAxisLabels.length; i++ )
            xAxisLabels[i] = new String(xAxisLabels[i] + ", N=" + N_cases[i]) ;

        IAxisDataSeries dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle, title );

//        double[][] data = new double[][]{{7, -5, 3.3, 4, 8.5, 10.2, 18, 21, 18, 17.4}};
        double[][] data = new double[][]{avgGain_per_category};
        String[] legendLabels = {"learning gain"};
        HttpSession sess = req.getSession();
        sess.setAttribute("dataSeries",dataSeries);
        sess.setAttribute("data",data);
        sess.setAttribute("legendLabels",legendLabels);
    }


    private double calculateWithinTutorPretest(int studId) throws java.sql.SQLException {
        ArrayList<TopicData> data = new ArrayList<TopicData>();

        data = collectStudentHistory(studId, data);

        Iterator itr = data.iterator() ;
        int numTopics = data.size() ;
        double acumScoreAcrossTopics = 0 ;
        studTopicsWithinPretest = numTopics ;

        while ( itr.hasNext() ) {
            //Process a topic
            TopicData td = (TopicData) itr.next() ;
            Iterator studTopicHist = td.history.iterator() ;
            int numPretestProbsTopic = 0 ;
            double topicAcum = 0 ;

            if (td.history.size() >= minTopicSize){

                while ( studTopicHist.hasNext() &&  numPretestProbsTopic < numAsessmentProblems-1  ) {
                    numPretestProbsTopic ++ ;
                    PerformancePoint studProbInteraction = (PerformancePoint) studTopicHist.next() ;

                    topicAcum += studProbInteraction.getProblemScore() ;
                }

                if (numPretestProbsTopic == numAsessmentProblems-1 )    {   //Then INVENT THE LAST PROBLEM, or look ahead one more
                    numPretestProbsTopic ++ ;

                    if ( topicAcum == 2 )  {  //GOOD STUDENT. Both first pretest problems were answered correct
                        if (studTopicHist.hasNext()) {     //Then look ahead another problem
                            PerformancePoint studProbInteraction = (PerformancePoint) studTopicHist.next() ;
                            topicAcum += studProbInteraction.getProblemScore() ;
                        }
                        else  {   //invent a third problem that is easiest of all, as there are no more problems ahead. Reachable Code??
                            topicAcum +=1 ;   //Where am I putting the difficulty level?
                        }
                    }
                    else {   //Student has answered at least one wrong. Assume a hardest problem would also be wrong.
                        topicAcum += 0 ;   //Where am I putting the difficulty level?
                    }
                }
            }
            if (numPretestProbsTopic > 0)
                acumScoreAcrossTopics += topicAcum / numPretestProbsTopic ;          // numPretestProbsTopic should be three, but might be less.
            else  {
                numTopics -- ;      // If there are no valid problems in the topic, then don't consider that topic at all as seen
                studTopicsWithinPretest -- ;
            }
        }

        return ( numTopics > 0 ? 100 * acumScoreAcrossTopics / numTopics : -1  ) ;
    }



    private double calculateWithinTutorPostest(int studId) throws java.sql.SQLException {
        ArrayList<TopicData> data = new ArrayList<TopicData>();
        double easiestdiff = 1 ;
        double hardestdiff= 0 ;
        data = collectStudentHistory(studId, data);

        Iterator itr = data.iterator() ;
        int numTopics = data.size() ;
        double acumPostScoreAcrossTopics = 0 ;
        studTopicsWithinPostest = numTopics ;


        while ( itr.hasNext() ) {
            //td has data from a  Topic, for a student Id
            TopicData td = (TopicData) itr.next() ;

            //Performance over the first three pretest problems
            PerformancePoint thisTopicPretestDiff[] = new PerformancePoint[numAsessmentProblems] ;
            PerformancePoint matchProblem[] = new PerformancePoint[numAsessmentProblems] ;
            int matchProblemID[] = new int[numAsessmentProblems] ;
            double differenceDifficulty[] = new double[numAsessmentProblems] ;

            Iterator studTopicHist = td.history.iterator() ;
            int numPostestProbsTopic = 0 ;
            double topicPostAcum = 0 ;
            int currentProblemNum = 0;
            double currentScoreSum=0;
            PerformancePoint studProbInteraction = null ;


            if (td.history.size() >= minTopicSize){
                int topicCountCorrect = 0 ;

                while ( studTopicHist.hasNext() && currentProblemNum < numAsessmentProblems) {
                    //studProblemInteraction has the current Problem being considered in the list.
                    studProbInteraction = (PerformancePoint) studTopicHist.next() ;
                    if ( studProbInteraction.diff_level > hardestdiff )   hardestdiff = studProbInteraction.diff_level ;
                    if ( studProbInteraction.diff_level < easiestdiff )   easiestdiff = studProbInteraction.diff_level ;


                    if ( studProbInteraction.getProblemScore()== 1 ) topicCountCorrect ++ ;    //Correct no hints

                    //If it is  a Pretest Problem
                    if ( currentProblemNum < numAsessmentProblems ) {
                        if (currentProblemNum == numAsessmentProblems-1 )    {   ////THIS IS THE THIRD PROBLEM, CONSIDER IT OR INVENT A NEW ONE?
                            if ( topicCountCorrect < 2)  {   //AT LEAST ONE INCORRECT, then invent a third problem that is harder, incorrect
                                if (hardestdiff < 0.75 ) hardestdiff += 0.15 ; else  hardestdiff = 0.85 ;

                                td.history.add(currentProblemNum, new PerformancePoint(0,true,2,true,true, hardestdiff));
                                thisTopicPretestDiff[currentProblemNum] = td.history.get(currentProblemNum) ;
                            }
                            else {
                                thisTopicPretestDiff[currentProblemNum] = td.history.get(currentProblemNum) ;
                            }
                        }
                        else
                            thisTopicPretestDiff[currentProblemNum] = td.history.get(currentProblemNum) ;
                    }
                    currentProblemNum ++;
                } //end while pretest problem


                //Need to restart again, because we created a new dummy problem in third position, index=2
                studTopicHist = td.history.iterator() ;
                currentProblemNum = 0 ;
                while ( studTopicHist.hasNext() ) {
                    studProbInteraction = (PerformancePoint) studTopicHist.next() ;

                    //Ignore Pretest Problems.
                    if ( currentProblemNum < numAsessmentProblems)                                       {
                        //Initialize the Match Problems with something! The following 2-3 problems after pretest.
                        matchProblemID[currentProblemNum] = numAsessmentProblems+currentProblemNum ;
                        matchProblem[currentProblemNum] = td.history.get(numAsessmentProblems+currentProblemNum) ;
                        differenceDifficulty[currentProblemNum] =
                                Math.abs(thisTopicPretestDiff[currentProblemNum].diff_level - matchProblem[currentProblemNum].diff_level) ;


                        currentProblemNum ++ ;
                        continue ;
                    }
                    //As long as not the  Pretest problems, it is a Potential Posttest Problem
                    //Which of all Pretest Problems is this one closest in difficulty?  Find the minimum difference.
                    double mindiff=999; int whichPretestProb=-1; double diff=0 ;
                    for (int i=0; i< numAsessmentProblems; i++) {
                        //Find the absolute Value difference in difficulty, with each of the pretest problems
                        diff = Math.abs(thisTopicPretestDiff[i].diff_level - studProbInteraction.diff_level) ;
                        if ( i==0 || diff < mindiff )  {
                            mindiff = diff ;
                            whichPretestProb = i ;
                        }
                    }

                    //if difference in difficulty between old pair is worse than the difference in difficulty for this new pair
                    if ( mindiff < differenceDifficulty[whichPretestProb]  ) {
                        //swap problems assigned to each.
                        int whereWasCurProbAssigned = IndexOfX(matchProblemID,currentProblemNum) ;

                        if ( whereWasCurProbAssigned >=0 ) {      //Then Swap the two problems assigned.  One will be a better match, and the other one we don't know.
                            //swap match of problem IDs
                            int oldMatch = matchProblemID[whichPretestProb] ;
                            matchProblemID[whichPretestProb] = currentProblemNum ;   //Assign new problem as a match
                            matchProblemID[whereWasCurProbAssigned] = oldMatch ;   //Assign new problem as a match

                            //Swap the old problem with the new problem
                            PerformancePoint oldPerfPoint = matchProblem[whichPretestProb] ;
                            matchProblem[whichPretestProb] = studProbInteraction ;   //Assign new problem as a match
                            matchProblem[whereWasCurProbAssigned] = oldPerfPoint ;   //Assign oldPerfPoint problem to the other problem

                            differenceDifficulty[whichPretestProb] = mindiff ;          //Record new smaller difference
                            differenceDifficulty[whereWasCurProbAssigned] =             //set new difference for these two
                                    Math.abs(oldPerfPoint.diff_level - td.history.get(whereWasCurProbAssigned).diff_level) ;
                        }
                        else { //Just assign the current Problem to that pretest problem that it is ideal for
                            matchProblemID[whichPretestProb] = currentProblemNum ;   //Assign new problem as a match
                            matchProblem[whichPretestProb] = td.history.get(currentProblemNum) ;   //Assign new problem as a match
                            differenceDifficulty[whichPretestProb] =
                                    Math.abs(td.history.get(whichPretestProb).diff_level - td.history.get(currentProblemNum).diff_level) ;
                        }
                    }

                    currentProblemNum ++;
                }

                //For each topic, accumulate the within-posttest score for the topic
                for (int i=0; i< numAsessmentProblems; i++) {
                    topicPostAcum += matchProblem[i].getProblemScore();
                }
                //Accumulate across Topics, for the same student
                acumPostScoreAcrossTopics += topicPostAcum / this.numAsessmentProblems ;          // numPretestProbsTopic should be three, but might be less.
            } //end IF enough Problems in Topic

            else  {
                numTopics -- ;      // If there are no valid problems in the topic, then don't consider that topic at all as seen
                studTopicsWithinPostest -- ;
            }
        } //end While each topic

        return ( numTopics > 0 ? 100 * acumPostScoreAcrossTopics / numTopics : -1  ) ;
    }



    /**
     * Goes through a given students eventlog entries.
     * The data is saved in the servlet session and then an HTML page with an <img href > makes a call to LineGraphServlet to fetch
     * data out of the HttpSession and build a graph which is returned as the image.
     */
    private ArrayList<TopicData> collectStudentHistory(int studId, ArrayList<TopicData> data) throws java.sql.SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        TopicData curTopicData = null ;

        try {

            String q = "SELECT * from studentproblemhistory sph, overallprobdifficulty opd " +
                " where studid=? " +
                " and sph.problemid=opd.problemid " +
                " and mode='practice' and timeToSolve>0 " +
                " order by problembegintime " ;

            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            int lastTopicId=-1;

            while (rs.next() ) {
                    double diff_level = rs.getDouble("diff_level");
                    int topicId = rs.getInt("topicId");

                    // When topic changes let the topic updater know
                    if (topicId != -1 && topicId != lastTopicId) {
                        curTopicData = new TopicData(topicId);
                        data.add(curTopicData);
                        lastTopicId = topicId;
                    }
                    if (rs.getString("sph.problemId") != null) {
                        int probId = rs.getInt("sph.problemId");

                        if ( curTopicData == null || curTopicData.history == null || curTopicData.history.size()<1000) {                  //Why would we check that the history size is less than 1000?
                            if ( curTopicData == null ) {
                                curTopicData = new TopicData(topicId);
                                data.add(curTopicData);
                            }

                            int corr = rs.getInt("isSolved") ;
                            int attempts = rs.getInt("numAttemptsToSolve") ;
                            int hints = rs. getInt("numHints") ;
                            int hintsBeforeCorr = rs.getInt("numHintsBeforeSolve") ;
                            curTopicData.history.add(new  PerformancePoint(probId, corr > 0, attempts, hints>0, hintsBeforeCorr>0, diff_level) ) ;
                        }
                    }
            } //while

        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

        return data ;
    }


    private boolean isActionValid(String action, String activityname, String userinput) {

        if (action.equalsIgnoreCase("BeginProblem"))
            return true ;

        if (action.equalsIgnoreCase("EndProblem"))
            return true ;

        if (action.equalsIgnoreCase("NextProblem"))
            return true ;

        if (action.equalsIgnoreCase("Attempt"))
            return true ;

        if (action.equalsIgnoreCase("ShowSolveProblem"))
            return true ;

        //Considers it a valid action only if the student has been offered a hint and accepted
        if (action.equalsIgnoreCase("InputResponse") && activityname != null && activityname.equals("hint")
                && userinput != null && userinput.equals("<selection>yes-hint</selection>"))
            return true ;

        if (action.equalsIgnoreCase("Hint"))
            return true ;

        return false ;
    }


    //    int probId, topicid ;
    boolean solved, endProblem ;
    boolean corr, hints, hintsBeforeCorr ;
    int attempts ;
    String activityName ;

    ArrayList<Integer> pidList = new ArrayList<Integer>() ;

    // process the event and save the topicMastery updates whenever and endProblem is hit.
    private TopicData processProblem(ResultSet rs, TopicData curTopicData, int probId, double diff_level) throws java.sql.SQLException {

        curTopicData.history.add(new  PerformancePoint(probId, corr, attempts, hints, hintsBeforeCorr, diff_level) ) ;

        /*
        if ( activityname == null) activityname = new String() ;

        // We've just hit a different problem.  Process the last one (if this isn't the first prob in the session)
        if (action.equalsIgnoreCase("BeginProblem") || action.equalsIgnoreCase("Formality_BeginProblem")) {
            pidList.add(probId);
            hints = false ;
            solved = false ;
            corr = false ;
            attempts = 0 ;
            endProblem=false;
            hintsBeforeCorr=false ;
        }
        // The next problem event has the decision of showing the next one as an example.
        if (action.equalsIgnoreCase("NextProblem")) {
            activityName = activityname;
        }
        // don't do anything with events after endProblem and before next beginProblem
        else if (endProblem)
            ;

        else if (action.equalsIgnoreCase("attempt") || action.equalsIgnoreCase("Formality_Attempt")) {
            attempts ++ ;

            if ((Integer.parseInt(rs.getString("isCorrect"))) == 0 )
                corr = false ;
            else
                corr = true ;

        }

        else if (action.toLowerCase().startsWith("hint")  ||
                action.toLowerCase().startsWith("ShowSolveProblem") ||
                activityname.startsWith("hint") ||
                action.equalsIgnoreCase("Formality_Hint")) {

            hints = true ;
            if ( ! corr )  hintsBeforeCorr = true ;
        }

        else if (action.equalsIgnoreCase("EndProblem") || action.equalsIgnoreCase("Formality_EndProblem")) {
            endProblem=true;

            //If it was an example problem, we cannot count it towards any measure of performance
            if ( probId != 999 && (activityName==null || !activityName.equals("ExampleProblem")))  {
                if ( hints  || attempts > 0 )              //Ignore Skipped Problems
                    curTopicData.history.add(new  PerformancePoint(probId, corr,attempts, hints, hintsBeforeCorr, diff_level) ) ;
            }

        }  // end endProblem
*/
        return curTopicData ;
    }

    private int IndexOfX(int[] myArray, int x) {
        for (int i=0; i<myArray.length; i++)
            if ( myArray[i] == x)
                return i ;
        return -1 ;
    }
}