package edu.umass.ckc.wo.woreports;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.handler.ReportHandler;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutor.studmod.BaseStudentModel;
import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.woreports.js.JSFile;
import edu.umass.ckc.wo.woreports.js.JSFunction;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMasteryHeuristic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import edu.umass.ckc.wo.woreports.util.EventLogEntry;
import edu.umass.ckc.wo.woreports.util.TrajectoryUtil;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.chartData.DataSeries;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 11, 2011
 * Time: 12:58:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentAllTopicsMasteryTrajectoryReport extends TopicTrajectoryReport {

    TopicMasterySimulator topicMasteryTracker;
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
    private boolean endProblem;
    private String curProbMode;
    private List<EventLogEntry> userEvents;
    List<Integer> pidList;
    private List<Problem> problems;


    class MasteryPoint {
        MasteryPoint(int probId, double mastery) {
            this.probId = probId;
            this.mastery = mastery;
        }

        int probId;     // not strictly necessary but putting in for verification that things are in synch as the data is gathered
        double mastery;

        public String toString() {
            return String.format("%d:%4.3f", probId, mastery);
        }
    }

    class TopicData {
        int topicId;
        List<MasteryPoint> history;
        int rowIndex;

        TopicData(int tid) {
            topicId = tid;
            history = new ArrayList<MasteryPoint>();
        }
    }

    List<TopicData> data;
    TopicData curTopicData;
    List<String> topicNames;

    // this is where data is collected as we walk the event log.   We store each problem seen and the mastery level at the end of that problem.
//    List<Integer> probIds;
//    List<Double> masteryHistory;


    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req,
                             HttpServletResponse resp) throws Exception {
        this.classId = classId;
        this.studId = e.getStudId();
        this.pidList = new ArrayList<Integer>();
        this.problems = new ArrayList<Problem>();
        data = new ArrayList<TopicData>();
        userEvents = new ArrayList<EventLogEntry>();
        ClassInfo cl = DbClass.getClass(conn, classId);
        int studId = e.getStudId();
        User u = DbUser.getStudent(conn, studId);
        String className = getClassName(cl);
        this.src.append(generateHeaderWithJS("Topic Mastery history for student - " + u.getUname() + " in class " + className, new JSFile[]{},
                new JSFunction[]{JSFunction.NAVIGATION_PULLDOWN_MENU}));
        this.src.append("<h3>Topic Mastery history for student " + u.getUname() + "</h3>\n");
        addNavLinks(classId, cl.getTeachid());
        // Similar to StudentTopicMasteryTrajectoryReport I've replaced this with a buffered approach.   See comments in that file for
        // details
        // Old call that processes each row
//        collectStudentMasteryHistory(conn, this.studId, req);
        // new calls that fill buffer and then process it
        int nEvents = collectStudentEventHistory(conn, this.studId);
        processEventHistory(conn);
        int nProbs = 0;
        for (TopicData d : this.data)
            nProbs += d.history.size();
        String xLabels = getXLabels();
        int nrows = determineRowIndices(); // marks TopicData records with rowNumber
        this.topicNames = new ArrayList<String>();
        JQPlotLine[] lines = createJQPlotMultipleTopicLines(nrows);
        String outString = getJQPlotLines(lines) ;
//        saveLineChartDataInSession(req);
        this.src.append("<p><img src=\"LineGraphServlet\">"); // inserts an image that calls BarChartServlet which gens a jpeg from data in HttpSession
        insertProbLister(req, conn, pidList);
        req.setAttribute("numTopics", this.topicNames.size());
        req.setAttribute("problems", problems);
        req.setAttribute("xLabels", xLabels);
        req.setAttribute("masterySequence", outString);
        req.setAttribute("topicNames",this.topicNames);
        req.setAttribute("teacherId", cl.getTeachid());
        req.setAttribute("classId", classId);
        req.setAttribute("className", className);
        req.setAttribute("flashProblemPreviewer", ProbPlayer.getURLToProbPreviewer());

        req.setAttribute("studentName", u.toString());
        req.setAttribute("message", nEvents == 0 ? "No events can be found for this user and topic." : "");
        req.setAttribute("width", nProbs * 50); // about 50pixels per data point will be a good width for the chart
        req.getRequestDispatcher(ReportHandler.STUDENT_ALL_TOPICS_MASTERY_TRAJECTORY_JSP).forward(req, resp);
        return null;
    }

    /**
     * Does all the work of getting data out of this.data and putting it into an array.  It also gathers
     * the names of topics and the problems.  this.data is a list of TopicData objects that come in the order of the
     * topics a student saw.  Each of these holds a history of the problems seen within that topic (history is a list of MasteryPoints).
     * The trick is that some topics are seen more than once and the graph needs to show these mastery points with the same color marker
     * So we mark each TopicData with a row number.   When a topic A is first seen we give it the next rowNumber.  If topic A is seen
     * again, we use the previously assigned row number.  This can then be used when we produce JQPlotLines so that one line for topic A
     * can have all these histories.
     * @param nrows  The number of unique topics
     * @return
     * @throws SQLException
     */
    private JQPlotLine[] createJQPlotMultipleTopicLines(int nrows) throws SQLException {
        int i = 0; // an overall index for what problem we are on

        JQPlotLine[] a;   // An array of lines - one for each topic
        a = new JQPlotLine[nrows];
        for (TopicData d : data) {
            int topicIx = d.rowIndex; // a number that identifies a topic uniquely for this chart
            // Add a new line if the topic has not been seen before
            // if we've already seen some problems in this topic, there will be a line, so use it
            if (a[topicIx] == null) {
                a[topicIx] = new JQPlotLine();
                Topic t = ProblemMgr.getTopic(d.topicId);
                if (t != null)
                    this.topicNames.add(t.getName());
            }
            // go through the history adding points to the line
            for (MasteryPoint p : d.history) {
                JQPlotPoint jp = new JQPlotPoint(i,p.mastery); // creates a single point in the chart for a given topic
                a[topicIx].addPoint(jp);
                this.problems.add(ProblemMgr.getProblem(p.probId));
                i++; // move to the next problem number
            }

        }
        return a;
    }



    //
    private String getXLabels() {
        List<Integer> pids = new ArrayList<Integer>();
        for (TopicData d : this.data) {
            for (MasteryPoint p : d.history)
                pids.add(p.probId);
        }
        return getLabelSequence(pids);
    }




    private int collectStudentEventHistory(Connection conn, int studId) throws Exception {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "SELECT l.id, l.sessNum,l.action,l.isCorrect,l.elapsedTime,l.probElapsed,l.problemId,l.hintId,l.activityName,l.curTopicId FROM eventlog l WHERE l.studid=? and l.problemId != 999 ORDER BY l.sessnum, l.elapsedTime ";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                int sessNum = rs.getInt(2);
                String action = rs.getString(3);
                boolean isCorrect = rs.getBoolean(4);
                long elapsedTime = rs.getLong(5);
                long probElapsed = rs.getLong(6);
                int problemId = rs.getInt(7);
                if (rs.wasNull())
                    problemId = -1;
                int hintId = rs.getInt(8);
                String activityName = rs.getString(9);
                if (rs.wasNull())
                    activityName = "";
                int curTopicId = rs.getInt(10);
                if (rs.wasNull())
                    curTopicId = -1;
                EventLogEntry ee = new EventLogEntry(id, sessNum, action, isCorrect, elapsedTime, probElapsed, problemId, hintId, activityName, curTopicId);
                userEvents.add(ee);
            }
            return userEvents.size();
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }


    // Walk over the buffer (userEvents) and use the masteryTrackers to create a set of records.
    private void processEventHistory(Connection conn) throws SQLException {
        topicMasteryTracker = new TopicMasterySimulator(conn, new StudentModelMasteryHeuristic(conn), BaseStudentModel.INITIAL_TOPIC_MASTERY_ESTIMATE_FL);
        // cycle through the event log for the student and the topic
        int lastSess = -1, lastTopicId = -1;
        isFirstProbOfSess = true;
        for (EventLogEntry entry : userEvents) {
            if (entry.problemId == 899)
                continue;
            if (entry.sessNum != lastSess) {
                isFirstProbOfSess = true;
                lastSess = entry.sessNum;
            } else isFirstProbOfSess = false;
            topicId = entry.curTopicId;
            // When topic changes let the topic updater know
            if (topicId != -1 && topicId != lastTopicId) {
                this.topicMasteryTracker.newTopic(topicId);
                // add a new topic data record to the list
                curTopicData = new TopicData(topicId);
                this.data.add(curTopicData);
                lastTopicId = topicId;
            }
            if (entry.problemId != -1)
                processEvent(entry);
        }
    }


    // This is a revised processor based on using events saved in the userEvents list rather than working directly off
    // rows coming from the query.   The reason I changed it is so that it is easy to move back and forth to find
    // a given problem's type.   There is some event prior to the problem's BeginProblem event that has activityName with
    // a value saying what the mode of the next problem will be.   The problem is that the event log (in past times) hasn't consistently
    // placed value in a row with a certain action type.   So we need to search back for it.   Hence the use of the userEvent list
    // which is kept in order that the events came in and allows this search backward from the point that problem begins.

    // The other major change is that this strictly ignores counting anything other than practice problems because only
    // practice problems affect mastery.


    private void processEvent(EventLogEntry entry) throws SQLException {

        // If beginProblem doesn't have a mode, then these are events prior to a format change on the event log (instituted
        // 9/24/15) and we have to try to locate the mode of the problem by searching back through previous events using
        //  TrajectoryUtil.getProblemType(userEvents,entry.id) .  Typically the type is on the preceding NextProblem event.
        // We are only interested in practice problems so this will not do anything if it sees that the current problem
        // is a demo, example, topicIntro
        if (entry.action.equalsIgnoreCase("beginProblem")) {
            curProbMode = entry.activityName;
            if (curProbMode == null || curProbMode.equals("")) {
                String type = TrajectoryUtil.getProblemType(userEvents, entry.id); // searches backward for type
                if (type.equals("PracticeProblem"))
                    curProbMode = Problem.PRACTICE;
            }
        }

        // We've just hit a different problem.  Process the last one (if this isn't the first prob in the session)
        if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("beginProblem")) {
            this.pidList.add(entry.problemId);
            beginProblemTime = entry.elapsedTime;
            endProblem = false;
            timeToChoose = 0;
            timeToAnswer = 0;
            timeToHint = 0;
            numHints = 0;
            solved = 0;
            incAttempts = 0;
            timeToFirstAttempt = 0;
        } else if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("endProblem")) {

            endProblem = true;
            // update the topic Mastery based on whats happened in this problem.
            topicMasteryTracker.updateMastery(entry.problemId, numHints, solved, incAttempts, timeToFirstAttempt, 0, "practice");   //!!!!Check mode and number of practice problems

            if (topicMasteryTracker.getMastery() > 0)
                curTopicData.history.add(new MasteryPoint(entry.problemId, topicMasteryTracker.getMastery()));


        }  // end endProblem
        else if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("attempt")) {
            if (entry.isCorrect && (timeToAnswer == 0)) {
                timeToAnswer = entry.probElapsed;

                if (timeToChoose == 0)  //this is the first attempt, and it is correct
                    solved = 1;

            }
            // if its the first attempt
            if (timeToChoose == 0) {
                timeToChoose = entry.probElapsed;
                timeToFirstAttempt = timeToChoose;
            }
            if (!entry.isCorrect && incAttempts < 4)  // A hack because somehow students could get more than 4 incorrect answers
                incAttempts++;
        } else if (Problem.isPracticeProblem(curProbMode) && entry.action.toLowerCase().startsWith("hint")) {
            if (timeToHint == 0)
                timeToHint = entry.probElapsed;

            if (numHints < 7)   // A threshold just in case students make hectic choose_a clicks
                numHints++;
        }
    }


    // for each TopicData store a rowIndex which is the row in the table that will represent this topic in the output
    // graph that is sent to the LineGraphServlet. Returns the number of rows
    private int determineRowIndices() {
        int count = 0;
        List<Integer[]> alist = new ArrayList<Integer[]>();
        int ix;
        for (TopicData d : this.data) {
            if ((ix = getIxinAlist(d.topicId, alist)) == -1) {
                d.rowIndex = count++;
                alist.add(new Integer[]{d.topicId, count - 1});
            } else d.rowIndex = ix;

        }
        return count;
    }

    // the alist stores pairs of ints <topicId, rowIndex>
    // Return the rowIndex of the given topicId or -1
    private int getIxinAlist(int topicId, List<Integer[]> alist) {
        for (Integer[] pair : alist) {
            if (pair[0] == topicId)
                return pair[1];

        }
        return -1;
    }
}