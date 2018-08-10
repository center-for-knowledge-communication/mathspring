package edu.umass.ckc.wo.handler;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.db.DbStudentComment;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.SaveCommentEvent;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.ShowProgressEvent;
import edu.umass.ckc.wo.event.tutorhut.SplashPageEvent;
import edu.umass.ckc.wo.event.tutorhut.TopicDetailEvent;
import edu.umass.ckc.wo.myprogress.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dovan
 * Date: 8/16/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardHandler {
    private ServletContext sc;
    private Connection conn;
    private SessionManager smgr;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public DashboardHandler(ServletContext sc, SessionManager smgr, Connection conn,
                            HttpServletRequest req, HttpServletResponse resp) {
        this.sc = sc;
        this.conn = conn;
        this.smgr = smgr;
        this.request = req;
        this.response = resp;
    }

    public View handleRequest(SessionEvent e) throws Exception {

        return null;
    }


    public void showSplashPage(String jsp, boolean newSession) throws Exception {
        String pid = null;
        String topid = null;
        List<Topic> topics = DbTopics.getClassPlayableTopics(smgr.getConnection(), smgr.getClassID(), smgr.showTestableContent());


        int problemsSolved;
        int topicsDone;
        int daysSinceLastSession;
        int problemsDoneInLastSession;
        int totalMasteredTopics;
        int topicsMasteredInLastSession;
        double masteryThreshold;

        StudentSummary ss = new StudentSummary(smgr);

        List<String> topicsList = ss.getTopicsList();
        List<Float> topicMasteryList = ss.getTopicMasteryList();
        List<String> TopicListMasteredInLastSession = ss.getTopicListMasteredInLastSession();
        List<dayDetails> dayDetailsList = ss.getDayDetailsList();

        problemsSolved = ss.getProblemsSolved();
        topicsDone = ss.getTopicsDone();
        daysSinceLastSession = ss.getDaysSinceLastSession();
        problemsDoneInLastSession = ss.getProblemsDoneInLastSession();
        totalMasteredTopics = ss.getTotalMasteredTopics();
        topicsMasteredInLastSession = ss.getTopicsMasteredInLastSession();
        masteryThreshold = ss.getMasteryThreshold();
        StudentState state= smgr.getStudentState();

        // We want to get the probId that should be passed on the Navigation event (to MPP) when launched from hybrid tutor
        // Similarly for the topicId.   If they aren't passed in, then its the old system and we get them from the student state.
        int probId = (pid != null) ? Integer.parseInt(pid) : state.getCurProblem();
        int topicId = (topid != null) ? Integer.parseInt(topid) :state.getCurTopic();
        request.setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
        request.setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
        request.setAttribute("teacherName", smgr.getClassTeacher());
        request.setAttribute("probId", probId);
        request.setAttribute("topicId", topicId);
        request.setAttribute("sessionId", smgr.getSessionNum());
        request.setAttribute("elapsedTime", smgr.getElapsedTime());
        request.setAttribute("useHybridTutor", Settings.useHybridTutor);
        request.setAttribute("topics", topics);
        request.setAttribute("learningCompanion", smgr.getLearningCompanion() != null ?  smgr.getLearningCompanion().getCharactersName() : "");

        request.setAttribute("problemsSolved", problemsSolved);
        request.setAttribute("topicsDone", topicsDone);
        request.setAttribute("daysSinceLastSession", daysSinceLastSession);
        request.setAttribute("problemsDoneInLastSession", problemsDoneInLastSession);
        request.setAttribute("totalMasteredTopics", totalMasteredTopics);
        request.setAttribute("topicsMasteredInLastSession", topicsMasteredInLastSession);
        request.setAttribute("topicsList", topicsList);
        request.setAttribute("topicMasteryList", topicMasteryList);
        request.setAttribute("TopicListMasteredInLastSession", TopicListMasteredInLastSession);
        request.setAttribute("dayDetailsList", dayDetailsList);
        request.setAttribute("masteryThreshold", masteryThreshold);
        request.setAttribute("newSession", newSession);
        request.setAttribute("eventCounter",smgr.getEventCounter());

        request.setAttribute("mouseSaveInterval", smgr.getMouseSaveInterval());
        request.setAttribute("gritServletContext","gritms");
        String servContext= request.getContextPath();
        if (servContext != null && servContext.length()>1)
            servContext=servContext.substring(1);    // strip off the leading /
        request.setAttribute("wayangServletContext",servContext);
        request.setAttribute("gritServletName","GritMouseServlet");


        request.getRequestDispatcher(jsp).forward(request, response);
        smgr.getStudentState().setTutorEntryTime(System.currentTimeMillis());

    }

    public void showNewSplashPage(String jsp, boolean newSession) throws Exception {
        request.setAttribute("topicSummaries", TopicSummary.getTopicSummaries(smgr));
        showSplashPage(jsp, newSession);
    }

}