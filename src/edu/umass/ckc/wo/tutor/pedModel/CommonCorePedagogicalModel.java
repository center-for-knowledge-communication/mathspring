package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.assistments.AssistmentsHandler;
import edu.umass.ckc.wo.assistments.CoopUser;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Lesson;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbCoopUsers;
import edu.umass.ckc.wo.event.tutorhut.EndProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.servletController.MariHandler;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.StudentEffort;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * PM that supports MARi tutoring of a common core standard
 * User: marshall
 * Date: 5/3/2016
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonCorePedagogicalModel extends BasePedagogicalModel {
    private static final Logger logger = Logger.getLogger(CommonCorePedagogicalModel.class);

    private StudentLessonMgr studentLessonMgr;

    /**
     * When this pedagogical model is instantiated, it has to regain the state of the lesson for the student.
     * @param smgr
     * @param ped
     * @throws java.sql.SQLException
     */
    public CommonCorePedagogicalModel(SessionManager smgr, Pedagogy ped) throws Exception {
        super(smgr,ped);  // This should get built with a LessonModel instead of a TopicModel
//        studentLessonMgr = new StudentLessonMgr(smgr);
    }

    public ProblemResponse getNextProblem(NextProblemEvent e) throws Exception {
        ProblemResponse r = super.getNextProblem(e);
        if (r.isNoMoreProblems())
            r.setEndPage(MariHandler.END_PAGE);
        return r;
    }

    public Response processNextProblemRequest(NextProblemEvent e) throws Exception {
        Response r = super.processNextProblemRequest(e);
        if (r instanceof ProblemResponse)
            if (((ProblemResponse) r).isNoMoreProblems())
                ((ProblemResponse) r).setEndPage(MariHandler.END_PAGE);
        return r;
    }

    @Override
    public Response processEndProblemEvent(EndProblemEvent e) throws Exception {
        Response r = super.processEndProblemEvent(e);

        // see if there is an entry in the assistmentsUser table which would indicate this user is from MARi
        CoopUser mariU = DbCoopUsers.getUserFromWayangStudId(smgr.getConnection(), smgr.getStudentId());
        if (mariU != null) {
            smgr.setAssistmentsUser(true);
            MariHandler.logToMariProblemEnd(smgr, (EndProblemEvent) e, mariU);
        }
        return r;
    }


}
