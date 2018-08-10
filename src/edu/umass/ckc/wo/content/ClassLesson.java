package edu.umass.ckc.wo.content;


import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/4/14
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassLesson {
    private int masterLessonId;
    private int classId;
    private Timestamp assignDate;
    private Timestamp dueDate;
    private Lesson masterLesson;
    private int id;
    private List<LessonOmit> omits;

    public ClassLesson() {
    }

    public ClassLesson(int id, int masterLessonId, Timestamp due, Timestamp assigned) {
        this.masterLessonId = masterLessonId;
        this.dueDate= due;
        this.assignDate=assigned;
        this.id=id;
    }

    public int getClassId() {
        return classId;
    }

    public Timestamp getAssignDate() {
        return assignDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public Lesson getMasterLesson() {
        return masterLesson;
    }

    public void setMasterLesson(Lesson masterLesson) {
        this.masterLesson = masterLesson;
    }

    public int getMasterLessonId() {
        return masterLessonId;
    }

    public List<LessonOmit> getOmits() {
        return omits;
    }

    public void setOmits(List<LessonOmit> omits) {
        this.omits = omits;
    }
}
