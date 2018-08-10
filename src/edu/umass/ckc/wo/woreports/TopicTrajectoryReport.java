package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.tutor.studmod.BaseStudentModel;
import edu.umass.ckc.wo.tutor.studmod.RawMasteryHeuristic;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMasteryHeuristic;
import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.woreports.util.EventLogEntry;
import edu.umass.ckc.wo.woreports.util.TrajectoryUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 9, 2011
 * Time: 9:25:45 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TopicTrajectoryReport extends Report {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TopicTrajectoryReport.class);


    TopicMasterySimulator topicMasteryTracker;  // Ivon's heuristic for progress bars that student sees
    TopicMasterySimulator rawMasteryTracker;    // Tom's heuristic for mastery level that teachers see in report
    int classId;
    int studId;
    int topicId;
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
    private int solvedOnAttemptN;
    private int numProbsThisTopic;
    private int numPracticeProbsThisTopic;
    private int exampleProbId;
    private String curProbMode ;
    private boolean endProblem;

    // cadmium/wayang2/flash/Problems/probplayer.swf?
    protected void insertProbLister(HttpServletRequest req, Connection conn, List<Integer> probIds) throws SQLException {
        StringBuilder sb = new StringBuilder();

        for (int pid: probIds) {
            Problem prob = new DbProblem().getProblem(conn,pid);
            if (prob != null) {
                String url = ProbPlayer.getURLToProbPreviewer() + "?questionNum=" + prob.getProbNumber();
                sb.append( "<option value=\"" +url+ "\">" + pid + ": " + prob.getName() + ": " + prob.getNickname() + "\n");
            }
        }

        this.src.append("<form name=\"form\">\n" +
                "<select name=\"site\" size=1>\n" +
                sb.toString() +
                "</select>\n" +
                "<input type=button value=\"Preview\" onClick=\"javascript:formHandler(this)\">\n" +
                "</form>");
    }



    protected List<EventLogEntry> collectStudentEventHistory (Connection conn, int studId, int topicId) throws Exception {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<EventLogEntry> userEvents = new ArrayList<EventLogEntry>();
        try {
            // bugs exist in event log so that elapsedTime cannot be used to put events in correct order
            String q = "SELECT l.id, l.sessNum,l.action,l.isCorrect,l.elapsedTime,l.probElapsed,l.problemId,l.hintId,l.activityName,l.curTopicId FROM eventlog l WHERE l.studid=? and curTopicId=? and l.problemId != 999 ORDER BY l.sessnum, l.id ";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setInt(2, topicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                int sessNum = rs.getInt(2); String action=rs.getString(3); boolean isCorrect=rs.getBoolean(4); long elapsedTime=rs.getLong(5);
                long probElapsed= rs.getLong(6);
                int problemId=rs.getInt(7);
                if (rs.wasNull())
                    problemId=-1;
                int hintId=rs.getInt(8);
                String activityName=rs.getString(9);
                if (rs.wasNull())
                    activityName="";
                int curTopicId=rs.getInt(10);
                if (rs.wasNull())
                    curTopicId=-1;
                EventLogEntry ee = new EventLogEntry(id,sessNum,action,isCorrect,elapsedTime,probElapsed,problemId,hintId,activityName,curTopicId);
                userEvents.add(ee);
            }
            return userEvents;  // return the list of events
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    // Walk over the buffer (userEvents) and use the masteryTrackers to create a set of records.
    protected void processEventHistory (Connection conn, List<EventLogEntry> userEvents, StudentMasteryHistory smh) throws SQLException {
        topicMasteryTracker = new TopicMasterySimulator(conn,new StudentModelMasteryHeuristic(conn), BaseStudentModel.INITIAL_TOPIC_MASTERY_ESTIMATE_FL);
        rawMasteryTracker = new TopicMasterySimulator(conn,new RawMasteryHeuristic(conn),0.0);   // initial mastery is 0.0
        // cycle through the event log for the student and the topic
        exampleProbId = -1;
        int lastSess = -1,lastTopicId = -1;
        isFirstProbOfSess = true;
        for (EventLogEntry entry: userEvents) {
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
                numProbsThisTopic= this.topicMasteryTracker.getTopicNumProbs();
                this.rawMasteryTracker.newTopic(topicId);
                lastTopicId = topicId;
            }
            if (entry.problemId != -1)
                processEvent(entry,userEvents,smh);
        }
    }

    // This is given the id of the entry in the eventHistory.   We then go into the userEvents list (which is sorted in order of
    // event occurrence) and find the row just prior to this event which says what type of problem we are working with.


    // This is a revised processor based on using events saved in the userEvents list rather than working directly off
    // rows coming from the query.   The reason I changed it is so that it is easy to move back and forth to find
    // a given problem's type.   There is some event prior to the problem's BeginProblem event that has activityName with
    // a value saying what the mode of the next problem will be.   The problem is that the event log (in past times) hasn't consistently
    // placed value in a row with a certain action type.   So we need to search back for it.   Hence the use of the userEvent list
    // which is kept in order that the events came in and allows this search backward from the point that problem begins.

    // The other major change is that this strictly ignores counting anything other than practice problems because only
    // practice problems affect mastery.

    // Issue:  When a topic starts and shows an example there will not be an event prior to the first beginProblem event
    // which has an activityType of PracticeProblem or ExampleProblem anymore (this is a result of treating example problems
    // as an intervention).  So trying to find the type of the first beginProblem event using this search will not work.

    private void processEvent(EventLogEntry entry, List<EventLogEntry> userEvents, StudentMasteryHistory smh) throws SQLException {

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
        // Beginning a practice problem so reset counters about problem solving.
        if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("beginProblem")) {
            beginProblemTime = entry.elapsedTime;
            endProblem = false;
            timeToChoose = 0;
            timeToAnswer = 0;
            timeToHint = 0;
            numHints = 0;
            solved = 0;
            incAttempts = 0;
            numAttempts = 0;
            timeToFirstAttempt = 0;
            solvedOnAttemptN = 0;
            numProbsThisTopic++;
            this.topicMasteryTracker.incrementNumProblems();
        }
        // ending a practice problem.
        else if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("endProblem")) {
            endProblem = true;
            // update the topic Mastery based on whats happened in this problem.
            topicMasteryTracker.updateMastery(entry.problemId, numHints, solved, incAttempts, timeToFirstAttempt, numProbsThisTopic,"practice");    //check this, not sure it should be practice

            rawMasteryTracker.updateMastery(entry.problemId, numHints, solved, incAttempts, timeToFirstAttempt, numProbsThisTopic, "practice");
            smh.probIds.add(entry.problemId);
            smh.masteryHistory.add(topicMasteryTracker.getMastery());
            smh.rawMasteryHistory.add(rawMasteryTracker.getMastery());
        }
        // attempt on a practice problem
        else if (Problem.isPracticeProblem(curProbMode) && entry.action.equalsIgnoreCase("attempt")) {
            numAttempts++;
            // The first time the problem is solved,   record the attempt #
            if (entry.isCorrect) {
                solved = 1;
                if (solvedOnAttemptN == 0)
                    solvedOnAttemptN = numAttempts;
            }
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
        }
        // hint on a practice problem
        else if (Problem.isPracticeProblem(curProbMode) && entry.action.toLowerCase().startsWith("hint")  ) {
            if (timeToHint == 0)
                timeToHint = entry.probElapsed;

            if (numHints < 7)   // A threshold just in case students make hectic choose_a clicks
                numHints++;
        }
    }


    /**
     * Prepare a list of problem ids as a label sequence of [0,"label0"],[1,"label1"], [2, "label2"]... for jqplot.js reports
     * @param pids
     * @return
     */
    protected String getLabelSequence(List<Integer>pids) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<pids.size();i++) {
            int pid = pids.get(i);
            // 899 is a dummy problem and should be eliminated from the report
            if (pid != 899)
                sb.append("[" + i + ", '" + pid + "'],");
//            sb.append("[" + i+1 + " , " + d.toString() + "],");
        }
        String s = sb.toString();
        if (s.length() > 0)
            return s.substring(0,s.length()-1);
        else return "";
    }

    /**
     * Given some masteries, return a string that represents them as a jqplot line.
     * @param floatSeq
     * @return
     */
    protected String getJQPlotLine(List<Double> floatSeq) {
        int i=0;
        JQPlotLine l = new JQPlotLine();
        for (Double d : floatSeq) {
            JQPlotPoint p = new JQPlotPoint(i++,d);
            l.addPoint(p);
        }
        return l.toString();
    }

    // Given all the lines, put out a String that represents all of them
    // like: [[line1], [line2]]
    protected String getJQPlotLines (JQPlotLine[] lineData) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0;i<lineData.length;i++) {
            sb.append("\n");
            JQPlotLine line = lineData[i];
            sb.append(line.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        return sb.toString();
    }

    protected class JQPlotPoint {

        int ix;
        double mastery;


        JQPlotPoint (int ix, double mastery) {
            this.ix = ix;
            this.mastery = mastery;
        }

        public String toString () {
            return String.format("[%d,%6.4f]",ix,mastery);
        }
    }

    protected class JQPlotLine {
        List<JQPlotPoint> points;

        protected JQPlotLine() {
            this.points = new ArrayList<JQPlotPoint>();
        }

        protected void addPoint (JQPlotPoint p) {
            this.points.add(p);
        }

        // return something like [[0,.4],[1,.5] ... ]
        public String toString () {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (JQPlotPoint p: points) {
                sb.append(p.toString());
                sb.append(",");
            }
            // eliminate trailing ,
            if (points.size() > 0)
                sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("]");
            return sb.toString();
        }
    }

    // this is where data is collected as we walk the event log.   We store each problem seen and the mastery level at the end of that problem.
    protected class StudentMasteryHistory {
        int studId;
        List<Integer> probIds;
        List<Double> masteryHistory;
        List<Double> rawMasteryHistory;

        protected StudentMasteryHistory() {
            probIds = new ArrayList<Integer>();
            masteryHistory = new ArrayList<Double>();
            rawMasteryHistory = new ArrayList<Double>();
        }
    }
}
