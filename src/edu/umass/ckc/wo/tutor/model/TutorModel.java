package edu.umass.ckc.wo.tutor.model;

import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.response.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/16/15
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TutorModel implements TutorEventProcessor {
    private LessonModel lessonModel;
    private SessionManager smgr;

    public TutorModel(SessionManager smgr) {
        this.smgr = smgr;
    }

    @Override
    public Response processUserEvent(TutorHutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInternalEvent(InternalEvent e) throws Exception {
        Response r;
        r = smgr.getPedagogicalModel().processInternalEvent(e);
        smgr.getStudentModel().save();
        return r;

    }




    public LessonModel getLessonModel() {
        return lessonModel;
    }

    public void setLessonModel(LessonModel lessonModel) {
        this.lessonModel = lessonModel;
    }
}
