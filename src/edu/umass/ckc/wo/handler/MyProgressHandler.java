package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbClass;
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

import ckc.servlet.servbase.View;
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
        if (e instanceof NavigationEvent  ) {
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

            String flashClientPath = Settings.flashClientPath  + smgr.getClient();

           String  backToVillageURL= Settings.flashClientPath + smgr.getClient() + "?sessnum=" + smgr.getSessionNum() ;


            String learningCompanion = smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName(): "none";
            request.setAttribute("clientPath", flashClientPath);
            request.setAttribute("learningCompanion", learningCompanion);
            request.setAttribute("backToVillageURL", backToVillageURL);


            TopicDetails td= new TopicDetails(smgr, topicId);
            List<problemDetails>  problemDetailsList= td.getProblemDetailsList();


            request.setAttribute("topicDetails", td);
            request.setAttribute("mastery", mastery);
            request.setAttribute("elapsedTime", elapsedTime);    // DAM causes compile error
            request.setAttribute("topicId", topicId);

            request.setAttribute("topicName", topicName);

            request.setAttribute("problemsDone", td.getProblemsSolved());
            request.setAttribute("totalProblems", td.getTotalProblems());
            request.setAttribute("sessionId", sessionId);

            request.setAttribute("td", td);
            request.setAttribute("problemDetailsList", problemDetailsList);

            request.setAttribute("useHybridTutor", Settings.useHybridTutor);
            request.setAttribute("studentFirstName", smgr.getStudentModel().getStudentFirstName());
            request.setAttribute("studentLastName", smgr.getStudentModel().getStudentLastName());
            request.setAttribute("mouseSaveInterval", smgr.getMouseSaveInterval());
            request.setAttribute("gritServletContext","gritms");
            String servContext= request.getContextPath();
            if (servContext != null && servContext.length()>1)
                servContext=servContext.substring(1);    // strip off the leading /
            request.setAttribute("wayangServletContext",servContext);
            request.setAttribute("gritServletName","GritMouseServlet");

            request.getRequestDispatcher(Settings.useNewGUI()
                    ? "TopicDetails_new.jsp"
                    : "TopicDetails.jsp").forward(request,response);
            logger.info("<< JSP: TopicDetails.jsp");
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
        String  backToVillageURL= Settings.flashClientPath + smgr.getClient() + "?sessnum=" + smgr.getSessionNum() ;
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
        String  backToVillageURL= Settings.flashClientPath + smgr.getClient() + "?sessnum=" + smgr.getSessionNum() ;
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