package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.handler.ReportHandler;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.woreports.js.JSFile;
import edu.umass.ckc.wo.woreports.js.JSFunction;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMasteryHeuristic;
import edu.umass.ckc.wo.tutor.studmod.RawMasteryHeuristic;
import edu.umass.ckc.wo.tutor.studmod.BaseStudentModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import edu.umass.ckc.wo.woreports.util.EventLogEntry;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.chartData.DataSeries;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 11, 2011
 * Time: 12:58:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassTopicMasteryTrajectoryReport extends TopicTrajectoryReport {

    TopicMasterySimulator topicMasteryTracker;  // Ivon's heuristic for progress bars that student sees
    TopicMasterySimulator rawMasteryTracker;    // Tom's heuristic for mastery level that teachers see in report
    int classId;
    int studId;
    int topicId;
    String topicName;

    private boolean isFirstProbOfSess;
    private long beginProblemTime;
    private long timeToChoose;
    private long timeToAnswer;
    private long timeToHint;
    private long timeToFirstAttempt;
    private int numHints;
    private int solved;
    private int incAttempts;
    private int numAttempts;
    private boolean endProblem;
    private int solvedOnAttemptN;
    private int numProbsThisTopic;
    private int numPracticeProbsThisTopic;
    private int exampleProbId;
    private int practiceProbId ;

    List<Double> avgProcessMasteryHistory;   // Ivon's estimate of mastery
    List<Double> avgAnswerMasteryHistory;    // Tom's estimate of mastery (no longer shown)



    private List<StudentMasteryHistory> classData;


    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.classId = classId;
        classData = new ArrayList<StudentMasteryHistory>();
//        probIds = new ArrayList<Integer>();
//        masteryHistory = new ArrayList<Double>();
//        rawMasteryHistory = new ArrayList<Double>();

        ClassInfo cl = DbClass.getClass(conn, classId);

        String topicX = e.getExtraParam();
        this.topicId = Integer.parseInt(topicX);
        topicName = DbTopics.getTopicName(conn, topicId, classId);
        User u = DbUser.getStudent(conn, studId);
        String className = getClassName(cl);
//        this.src.append(generateHeader2("Topic Mastery history for student - " + u.getUname() + " in class " + className ));
        this.src.append(generateHeaderWithJS("Topic Mastery history for class " + className,
                new JSFile[]{}, new JSFunction[]{JSFunction.NAVIGATION_PULLDOWN_MENU}));
        this.src.append("<h3>Topic Mastery history for class " + className + "</h3>\n");
        addNavLinks(classId, cl.getTeachid());
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id from student where classId=? and trialUser=0";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            List<EventLogEntry> userEvents;
            while (rs.next()) {
                int studId= rs.getInt(1);
                StudentMasteryHistory smh = new StudentMasteryHistory();
                classData.add(smh);
                userEvents = collectStudentEventHistory(conn,studId,topicId);
                processEventHistory(conn,userEvents,smh);
//                collectStudentMasteryHistory(conn, studId, this.topicId, req, smh);
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

        computeClassMasteries(classData);
        List<Integer> labels = new ArrayList<Integer>();
        for (int i = 0; i < avgProcessMasteryHistory.size(); i++)
            labels.add( new Integer(i));
        String processMasteryline = getJQPlotLine(avgProcessMasteryHistory);
        String answerMasteryline = getJQPlotLine(avgAnswerMasteryHistory);
        req.setAttribute("xLabels",getLabelSequence(labels));
        req.setAttribute("processMastery",processMasteryline);
        req.setAttribute("answerMastery",answerMasteryline);
        req.setAttribute("teacherId",cl.getTeachid());
        req.setAttribute("classId",classId);
        req.setAttribute("className",className);
        req.setAttribute("topicName",topicName);
        req.setAttribute("width",labels.size()*50); // about 50pixels per data point will be a good width for the chart
        req.getRequestDispatcher(ReportHandler.CLASS_TOPICS_MASTERY_TRAJECTORY_JSP).forward(req,resp);
        return null;

    }




    /**
     * Go through all the students mastery histories and compute 2 trajectories that represent the whole class
     * One is answer-oriented-mastery (toms) and the other is the process-oriented-mastery (ivons)
     * @param classData
     */
    private void computeClassMasteries(List<StudentMasteryHistory> classData) {
        avgProcessMasteryHistory = new ArrayList<Double>();  // keeps an avg process mastery for each time slice t
        List<Integer> avgProcessCounters = new ArrayList<Integer>(); // keeps track of how many process-masteries encountered for each time slice t
        avgAnswerMasteryHistory = new ArrayList<Double>();
        List<Integer> avgAnswerCounters = new ArrayList<Integer>();
        int weight = 0;
        // Ivon requested that this report stop putting out trajectory points if there are less than five students included
        // in a time-slice.   The number of students in a time-slice is held in the counters lists.
        for (StudentMasteryHistory h: classData) {
            updateAverages(avgProcessMasteryHistory,h.masteryHistory,weight, avgProcessCounters);
            updateAverages(avgAnswerMasteryHistory,h.rawMasteryHistory,weight, avgAnswerCounters);
            weight++;
        }
        int numStudentsNecessary = 5; // don't add a mastery point if there are less than this many students providing data
        // Now remove points from avgProcessMasteryHistory and avgAnswerMasteryHistory where counters associated with time-slices
        // are less than numStudentsNecessary
        for (int i = 0; i < avgProcessCounters.size(); i++) {
            // once we hit the portion of the list of counters that is below 5 we will start removing masteries
            // from the end of the list.   When done,  we will have removed all the masteries from the end of the list
            // that had counters below 5
            if (avgProcessCounters.get(i) < numStudentsNecessary)
                avgProcessMasteryHistory.remove(avgProcessMasteryHistory.size()-1);

        }
        for (int i = 0; i < avgAnswerCounters.size(); i++) {
            if (avgAnswerCounters.get(i) < numStudentsNecessary)
                avgAnswerMasteryHistory.remove(avgAnswerMasteryHistory.size()-1);

        }
    }

    // Given one students mastery history as studentMasteries.  This is a sequence of probIds and the mastery at that point
    // Update the values in avgMast using the student data.   The avgMast is a weighted average where the value in the avgMast gets weight and
    // the student's value gets weight 1.   The list of counters keeps track of how many masteries at time slice t have been encountered
    private void updateAverages(List<Double> avgMast, List<Double> studentMasteries, int weight, List<Integer> counters) {
        int i=-1;
        for (double studMast: studentMasteries) {
            i++;
            // some students will not have data for this topic.  They should be ignored from the averaging.
            if (!(studMast >= 0 && studMast <= 1))
                continue;
            if (counters.size() <= i)
                counters.add(1);
            else counters.set(i,counters.get(i)+1);
            int w = counters.get(i);
            double prevAvg = (counters.get(i) == 1) ? 0.0 : avgMast.get(i);
            double newAvg = (prevAvg * (w-1) + studMast) / w;
            if (w == 1)
                avgMast.add(newAvg);
            else avgMast.set(i,newAvg);

        }
    }

}