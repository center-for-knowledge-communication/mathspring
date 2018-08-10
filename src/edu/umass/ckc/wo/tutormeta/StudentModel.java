package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.db.DbStateTableMgr;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import edu.umass.ckc.wo.tutor.studmod.MasteryHeuristic;


import java.sql.SQLException;
import java.util.List;


public abstract class StudentModel extends State implements TutorEventHandler {

    protected DbStateTableMgr dbWorker ;
    protected StudentProblemHistory problemHistory;

    protected MasteryHeuristic heuristic=null;   // object that computes topic mastery based on performance

    public StudentProblemHistory getStudentProblemHistory () {
        return this.problemHistory;
    }

    /**
     * Save the StudentModel to the database
     * @return
     * @throws SQLException
     */
    public abstract boolean save () throws SQLException;

    /**   Load all student model key/value pairs.
     * Called right after StudentModel is constructed.
     * @param props
     * @param studId
     * @param classId
     * @throws SQLException
     */
    public abstract void init(WoProps props, int studId, int classId) throws SQLException ;

//    public abstract void updateEmotionalState(SessionManager smgr, long probElapsedTime, long elapsedTime) ;

    public abstract String getStudentFirstName();
    public abstract String getStudentLastName();

    public abstract StudentEffort getEffort ();

    public abstract double getTopicMastery(int topicId) throws SQLException;

    public MasteryHeuristic getMasteryHeuristic () {
        return heuristic;
    }


    public abstract List<TopicMastery> getTopicMasteries () throws SQLException;
}