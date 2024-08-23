package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.db.DbStudentComment;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.SaveCommentEvent;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.ShowProgressEvent;
import edu.umass.ckc.wo.event.tutorhut.SplashPageEvent;
import edu.umass.ckc.wo.event.tutorhut.TopicDetailEvent;
import edu.umass.ckc.wo.myprogress.TopicSummary;
import edu.umass.ckc.wo.myprogress.TopicDetails;
import edu.umass.ckc.wo.myprogress.problemDetails;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.pedModel.TopicSelectorImpl;
import edu.umass.ckc.wo.tutormeta.TopicSelector;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 8/16/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 * Frank 05-18-2020 issue #125 cast elapsedtime from (double) to (int) - fixes url parameter of 'Logou' selection.
 * Frank 08-12-2020 issue #194 Send along the first unsolved problem encountered for 'Practice Area' link
 * Frank 08-20-20	Issue #194 correction fetch current problem from smgr for Practice Area link 
 * Kartik 04-22-21 Issue #390 Added session clock functionality
 * Frank  08-22-24 Issue #781R7 set pageLang, pageLangIndex request params
 */
public class MyProgressHandler {

    private static Logger logger = Logger.getLogger(MyProgressHandler.class);
    private ServletContext sc;
    private Connection conn;
    private SessionManager smgr;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public MyProgressHandler (ServletContext sc, SessionManager smgr, Connection conn,
                              HttpServletRequest req, HttpServletResponse resp) {
        this.sc = sc;
        this.conn = conn;
        this.smgr = smgr;
        this.request = req;
        this.response = resp;
    }

    public View handleRequest(SessionEvent e) throws Exception {
        if (e instanceof NavigationEvent) {
        	if (((NavigationEvent) e).getFrom().equals("my_progress")) {
        		smgr.togglePageLangIndex();
        		DbSession.updateSessionPageLangIndex(smgr.getConnection(),smgr.getSessionId(),smgr.getPageLangIndex());
        	}
            showProgressPage(e);
            return null;
        }
        else if (e instanceof SplashPageEvent) {
//            showSplashPage((SplashPageEvent) e);
            return null;
        }
        else if (e instanceof ShowProgressEvent) {
            showProgressPage(e);
            return null;
        }
        else if (e instanceof TopicDetailEvent) {
            TopicDetailEvent ee = (TopicDetailEvent) e;
            int topicId = ee.getTopicId();
            double elapsedTime = ee.getElapsedTime();  // DAM causes a compile error
            int sessionId = e.getSessionId();
            double mastery = ee.getMastery();
            
            String topicName = ee.getTopicName();
            int problemsDone= ee.getProblemsDone();
            int totalProblems= ee.getTotalProblems();

            String learningCompanion = smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName(): "none";
            request.setAttribute("pageLangIndex",smgr.getPageLangIndex());
            request.setAttribute("pageLang",smgr.getLocale().getLanguage());
            request.setAttribute("clientPath", null);
            request.setAttribute("learningCompanion", learningCompanion);
            request.setAttribute("backToVillageURL", null);


            TopicDetails td= new TopicDetails(smgr, topicId);
            List<problemDetails>  problemDetailsList= td.getProblemDetailsList();


            request.setAttribute("topicDetails", td);
            request.setAttribute("mastery", mastery);
            request.setAttribute("elapsedTime", (int)elapsedTime);    // DAM causes compile error
            request.setAttribute("topicId", topicId);
            
            // issue #194
           	request.setAttribute("probId", smgr.getStudentState().getCurProblem());

            request.setAttribute("topicName", topicName);

            request.setAttribute("problemsDone", td.getProblemsSolved());
            request.setAttribute("totalProblems", td.getTotalProblems());
            request.setAttribute("sessionId", sessionId);

            request.setAttribute("td", td);
            request.setAttribute("problemDetailsList", problemDetailsList);
            request.setAttribute("webContentpath", Settings.webContentPath);
            request.setAttribute("useHybridTutor", Settings.useHybridTutor);
            request.setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
            request.setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
            request.setAttribute("mouseSaveInterval", smgr.getMouseSaveInterval());
            request.setAttribute("gritServletContext","gritms");
            request.setAttribute("timeInSession", smgr.getTimeInSession());
            String servContext= request.getContextPath();
            if (servContext != null && servContext.length()>1)
                servContext=servContext.substring(1);    // strip off the leading /
            request.setAttribute("wayangServletContext",servContext);
            request.setAttribute("gritServletName","GritMouseServlet");

            request.getRequestDispatcher(Settings.useNewGUI()
                    ? "TopicDetails_new.jsp"
                    : "TopicDetails.jsp").forward(request,response);
            logger.info("<< JSP: Topic_Details.jsp");
            return null;
        }

        else if (e instanceof SaveCommentEvent) {
            DbStudentComment.saveComment(conn, smgr.getSessionNum(), smgr.getStudentId(),
                    ((SaveCommentEvent) e).getTopicId(), ((SaveCommentEvent) e).getStudentAction(),((SaveCommentEvent) e).getComment());
        }


        return null;
    }

    public void showSplashPage(String jsp) throws Exception {
        //request.setAttribute("problemHistory", smgr.getStudentModel().getProblemHistory());
        //List<TopicSummary> summaries = TopicSummary.getTopicSummaries(smgr.getConnection(),smgr.getStudentId(),smgr.getClassID(),smgr.getClassMasteryThreshold());
        List<TopicSummary> summaries = TopicSummary.getTopicSummaries(smgr);
        String  backToVillageURL= null;
        //String  backToVillageURL= smgr.getClient() + "?sessnum=" + smgr.getSessionNum() ;
        String learningCompanion = smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName(): "none";
        // in the hybrid system problemId and topicId are passed as params with NavigationEvent when to the MPP
        String pid=null;
        String topid=null;
        List<Topic> topics = DbTopics.getClassPlayableTopics(smgr.getConnection(), smgr.getClassID(), smgr.showTestableContent());

        // We want to get the probId that should be passed on the Navigation event (to MPP) when launched from hybrid tutor
        // Similarly for the topicId.   If they aren't passed in, then its the old system and we get them from the student state.
        int probId = (pid != null) ? Integer.parseInt(pid) : smgr.getStudentState().getCurProblem();
        int topicId = (topid != null) ? Integer.parseInt(topid) : smgr.getStudentState().getCurTopic();
        request.setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
        request.setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
        request.setAttribute("teacherName", smgr.getClassTeacher());
        request.setAttribute("probId",probId);
        request.setAttribute("topicId",topicId);
        request.setAttribute("sessionId",smgr.getSessionNum());
        request.setAttribute("topicSummaries", summaries);
        request.setAttribute("backToVillageURL", backToVillageURL);
        request.setAttribute("learningCompanion", learningCompanion);
        request.setAttribute("useHybridTutor", Settings.useHybridTutor);
        request.setAttribute("topics", topics);
        logger.info("<< JSP: " + jsp);
        request.getRequestDispatcher(jsp).forward(request, response);
        smgr.getStudentState().setTutorEntryTime(System.currentTimeMillis());
    }

    private void showProgressPage(SessionEvent e) throws Exception {
        //request.setAttribute("problemHistory", smgr.getStudentModel().getProblemHistory());
        //List<TopicSummary> summaries = TopicSummary.getTopicSummaries(smgr.getConnection(),smgr.getStudentId(),smgr.getClassID(),smgr.getClassMasteryThreshold());
        List<TopicSummary> summaries = TopicSummary.getTopicSummaries(smgr);
        String  backToVillageURL= null ;
        //String  backToVillageURL= smgr.getClient() + "?sessnum=" + smgr.getSessionNum() ;
        String learningCompanion = smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName(): "none";
        // in the hybrid system problemId and topicId are passed as params with NavigationEvent when to the MPP
        String pid=null;
        String topid=null;
        if (e instanceof NavigationEvent) {
            pid=((NavigationEvent) e).getProbId();
            topid= ((NavigationEvent) e).getTopicId();
        }
        // We want to get the probId that should be passed on the Navigation event (to MPP) when launched from hybrid tutor
        // Similarly for the topicId.   If they aren't passed in, then its the old system and we get them from the student state.
        int probId = (pid != null && pid.length() != 0) ? Integer.parseInt(pid) : smgr.getStudentState().getCurProblem();
        int topicId = (topid != null && topid.length() != 0) ? Integer.parseInt(topid) : smgr.getStudentState().getCurTopic();
        request.setAttribute("experiment",smgr.getExperiment());
    	request.setAttribute("pageLangIndex",smgr.getPageLangIndex());
    	request.setAttribute("pageLang",smgr.getLocale().getLanguage());
        request.setAttribute("probId",probId);
        request.setAttribute("topicId",topicId);
        request.setAttribute("sessionId",smgr.getSessionNum());
        request.setAttribute("topicSummaries", summaries);
        request.setAttribute("backToVillageURL", backToVillageURL);
        request.setAttribute("learningCompanion", learningCompanion);
        request.setAttribute("useHybridTutor", Settings.useHybridTutor);
        request.setAttribute("eventCounter", smgr.getEventCounter());
        request.setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
        request.setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
        request.setAttribute("mouseSaveInterval", smgr.getMouseSaveInterval());
        request.setAttribute("gritServletContext","gritms");
        request.setAttribute("timeInSession", smgr.getTimeInSession());
        String servContext= request.getContextPath();
        if (servContext != null && servContext.length()>1)
            servContext=servContext.substring(1);    // strip off the leading /
        request.setAttribute("wayangServletContext",servContext);
        request.setAttribute("gritServletName","GritMouseServlet");
        logger.info("<< JSP: MyProgress.jsp");
        request.getRequestDispatcher(Settings.useNewGUI()
                ? "MyProgress_new.jsp"
                : "MyProgress.jsp").forward(request, response);
    }
}