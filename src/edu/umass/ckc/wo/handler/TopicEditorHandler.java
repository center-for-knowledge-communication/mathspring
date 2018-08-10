package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.content.TopicMgr;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.event.admin.AdminEditTopicsEvent;
import edu.umass.ckc.wo.event.admin.AdminReorderTopicsEvent;
import edu.umass.ckc.wo.event.admin.AdminSetTopicModelParametersEvent;
import edu.umass.ckc.wo.event.admin.AdminTopicControlEvent;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.tutor.probSel.ClassTutorConfigParams;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2008
 * Time: 9:58:20 AM
 */
public class TopicEditorHandler {

    private String teacherId;
    private HttpSession sess;

    public static final String JSP = "/teacherTools/orderTopics.jsp";
    public static final String SELECT_PEDAGOGIES_JSP = "/teacherTools/selectPedagogies.jsp";
    public static final String CLASS_INFO_JSP = "/teacherTools/classInfo.jsp";

    public TopicEditorHandler () {}






    public View handleEvent(ServletContext sc, Connection conn, AdminEditTopicsEvent e, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        List<Topic> topics = null;
        ClassTutorConfigParams classParams= (ClassTutorConfigParams) DbClass.getClassConfigTutorParameters(conn, e.getClassId());
        if (e instanceof AdminReorderTopicsEvent) {
            TopicMgr topicMgr = new TopicMgr();
            AdminReorderTopicsEvent ee = (AdminReorderTopicsEvent) e;
            if (ee.getReorderType().equals("move"))
                topics = topicMgr.moveTopic(conn, ee);
            else if (ee.getReorderType().equals("omit")) {
                int classId = e.getClassId();
                int topicId = ee.getTopicId();
                topics = topicMgr.omitTopic(conn,classId,topicId);
            }
            else if (ee.getReorderType().equals("reactivate")) {
                topicMgr.reactivateTopic(conn, ee);
                topics = DbTopics.getClassActiveTopics(conn,ee.getClassId());
            }
            DbClass.setClassTutorConfigParameters(conn,e.getClassId(), classParams);
        }
        else if (e instanceof AdminTopicControlEvent) {
            AdminTopicControlEvent ee = (AdminTopicControlEvent) e;
            topics = DbTopics.getClassActiveTopics(conn,e.getClassId());
            // send to constructor but time in topic is in incorrect units
            classParams = new ClassTutorConfigParams(ee.getMaxTimeInTopic(), ee.getContentFailureThreshold(),
                    ee.getTopicMastery(), ee.getMinNumProbsPerTopic(), ee.getMinTimeInTopic(), ee.getDifficultyRate(),
                     ee.getMaxNumProbsPerTopic());
        }
        // 3/13/17 Added this event because reorder portion of the orderTopics page may be sending the AdminTopicControlEvent (above) and
        // we only want to set the paramters if the submit button in that form is clicked.
        else if (e instanceof AdminSetTopicModelParametersEvent) {
            AdminSetTopicModelParametersEvent ee = (AdminSetTopicModelParametersEvent) e;
            topics = DbTopics.getClassActiveTopics(conn,e.getClassId());
            // send to constructor but time in topic is in incorrect units
            classParams = new ClassTutorConfigParams(ee.getMaxTimeInTopic(), ee.getContentFailureThreshold(),
                    ee.getTopicMastery(), ee.getMinNumProbsPerTopic(), ee.getMinTimeInTopic(), ee.getDifficultyRate(),
                    ee.getMaxNumProbsPerTopic());

            DbClass.setClassTutorConfigParameters(conn,e.getClassId(), classParams);
        }
        else {
            // fetch a list of topics for the class sorted in the order they will be presented
            topics = DbTopics.getClassActiveTopics(conn,e.getClassId());
        }
        ClassInfo[] classes = DbClass.getClasses(conn,e.getTeacherId());
        Classes bean = new Classes(classes);
        ClassInfo classInfo = DbClass.getClass(conn,e.getClassId());
        req.setAttribute("bean",bean);
        req.setAttribute("classInfo",classInfo);
        req.setAttribute("classGradeColumn", DbProblem.getGradeNum(classInfo.getGrade()));
        DbProblem.setTopicNumProbsForClass(conn, e.getClassId(), topics);
        req.setAttribute("gradeColumnMask", DbProblem.getGradeColumnMask(topics));
        List<Topic> inactiveTopics = DbTopics.getClassInactiveTopics(conn, topics);
        DbProblem.setTopicNumProbsForClass(conn, e.getClassId(), inactiveTopics);
        req.setAttribute("topics",topics);
        req.setAttribute("numTopics",topics.size());
        req.setAttribute("inactiveTopics",inactiveTopics);
        Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
        req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
        req.setAttribute("isAdmin",adminId != null);
        // forward to the JSP page that allows reordering the list and omitting topics.
        req.setAttribute("action","AdminEditTopics");
        req.setAttribute("teacherId",e.getTeacherId());
        req.setAttribute("classId",e.getClassId());
        CreateClassHandler.setTeacherName(conn,req, e.getTeacherId());
        req.setAttribute("tutorConfigParams",classParams);
        req.getRequestDispatcher(JSP).forward(req,resp);

        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
