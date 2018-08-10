package edu.umass.ckc.wo.woreports;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.handler.ReportHandler;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.util.ProbPlayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import edu.umass.ckc.wo.woreports.util.EventLogEntry;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Nov 11, 2011
 * Time: 12:58:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentTopicMasteryTrajectoryReport extends TopicTrajectoryReport {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StudentTopicMasteryTrajectoryReport.class);



    // this is where data is collected as we walk the event log.   We store each problem seen and the mastery level at the end of that problem.
//    List<Integer> probIds;
    List<Double> masteryHistory;


    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.classId = classId;
        this.studId = e.getStudId();
        masteryHistory = new ArrayList<Double>();
        ClassInfo cl = DbClass.getClass(conn, classId);
        int studId = e.getStudId();
        String topicX = e.getExtraParam();
        this.topicId = Integer.parseInt(topicX);
        String topicName = DbTopics.getTopicName(conn, topicId);
        User u = DbUser.getStudent(conn, studId);
        String className = getClassName(cl);
        List<EventLogEntry> userEvents = collectStudentEventHistory(conn,this.studId,this.topicId);
        StudentMasteryHistory smh = new StudentMasteryHistory();
        processEventHistory(conn,userEvents,smh);
        List<Problem> problems = new ArrayList<Problem>(smh.probIds.size());
        for (int pid: smh.probIds) {
            Problem prob = new DbProblem().getProblem(conn,pid);
            if (prob != null)
                problems.add(prob);
        }

        req.setAttribute("xLabels",getLabelSequence(smh.probIds));
        req.setAttribute("rawMasterySequence",getJQPlotLine(smh.rawMasteryHistory));   // Tom's mastery estimate
        req.setAttribute("masterySequence",getJQPlotLine(smh.masteryHistory));          // Ivon's mastery estimate
        req.setAttribute("teacherId",cl.getTeachid());
        req.setAttribute("classId",classId);
        req.setAttribute("topicName",topicName);
        req.setAttribute("studentName",u.toString());
        req.setAttribute("className",className);
        req.setAttribute("problems",problems);
        req.setAttribute("flashProblemPreviewer", ProbPlayer.getURLToProbPreviewer());
        req.setAttribute("message", userEvents.isEmpty() ? "No events can be found for this user and topic." : "" );
        req.setAttribute("width",smh.probIds.size()*50); // about 50pixels per data point will be a good width for the chart
        req.getRequestDispatcher(ReportHandler.STUDENT_TOPIC_MASTERY_TRAJECTORY_JSP).forward(req,resp);
        return null;
    }







}
