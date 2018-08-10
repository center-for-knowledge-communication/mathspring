package edu.umass.ckc.wo.tutor.pedModel;

import ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This keeps track of moving through Curriculum Units in a given lesson for a given student.
 * It is responsible for
 * User: marshall
 * Date: 8/14/14
 * Time: 10:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class StudentLessonMgr {

    private static final Logger logger = Logger.getLogger(StudentLessonMgr.class);
    private SessionManager smgr;
    private int lessonId;
    private Lesson curLesson;
    private int curCUId;
    private CurricUnit curCU;
    private int curClusterId;
    private CCCluster curCluster;
    private String curStdId;
    private CCStandard curStd;
    private int curProbId;  // this variable represents the current problem that is on display
    private Problem curProblem;
    private int nextProbId;  // when making a selection, this is the variable to set
    private Problem nextProblem;
    private List<CurricUnit> curricUnitList;
    private StudentState state;
    private int studId;
    private int classId;
    private Connection conn;
    private List<Lesson> classLessons;


    /**
     * Maintains the state of the lesson for the student.
     * @param smgr
     * @throws SQLException
     */
    public StudentLessonMgr(SessionManager smgr) throws Exception {
        init(smgr);
        setLessonLocationState();
        logger.debug("StudentLessonMgr instantiated");
    }


    // This is used to initialize the mgr when the call is from Assistments asking to show a particular lesson.
    // In this situation we need to set the start location to the first CU in the lesson and then initialize the state
    // further within the CU to be the first problem
    public void init (SessionManager smgr, int lessonId) throws Exception {
        init(smgr);
        setLessonStartLocation(lessonId);
    }

    public void init (SessionManager smgr) throws SQLException {
        this.smgr = smgr;
        this.state = smgr.getStudentState();
        this.conn = smgr.getConnection();

        // get the IDs of the various components of the lesson state for the user.   These all come from the workspace state
	// so that they persist after session ends.
        this.lessonId = state.getCurLesson();
        this.curCUId = state.getCurCU();
        this.curClusterId = state.getCurCluster();
        this.curStdId = state.getCurStd();
        // Note that the probId is stored in both Lesson and Workspace state.   Lesson state is wiped clean after a session is logged out
        // so we fetch probId from the workspace state (which isn't).
        this.curProbId = state.getCurProblem();   // get from the lesson state
        if (this.curProbId == -1)
            // not there so get from the workspace state
            this.curProbId = state.getWorkspaceState().getCurProb();
        this.studId = smgr.getStudentId();
        this.classId = smgr.getClassID();
        // from the IDs, get objects that represent the content of the lesson.

    }

    // This is for an initial call from Assistments to a set a fixed lesson for a student.
    public void setLessonStartLocation (int lessonId) throws Exception {
        this.lessonId = lessonId;
        this.classLessons = LessonMgr.getInstance().getClassLessons(conn, classId);
        this.curLesson = getCurLesson(); // get the Lesson object from the lessonId
        state.setCurLesson(curLesson.getId());
        this.curricUnitList = curLesson.getCurricUnits();
        CurricUnit cu = curLesson.getCUByPosition(1);   // get the first CU
        setLessonLocationState(cu); // set the internal state so that this can begin selecting problems
    }

    /**
     * Set the state of the student as his location within the lesson.
     * This means setting objects for the current Lesson, CU, Cluster, Standard, Problem
     * @throws SQLException
     */
    public void setLessonLocationState() throws Exception {
        // this will load Lesson structure as it applies to the student

        // get all the lessons for the class the student is in
        this.classLessons = LessonMgr.getInstance().getClassLessons(conn,classId);
        // get the current lesson (if not yet in one, set one)
        this.curLesson = getCurLesson();
        // no lesson is set yet,  set one up along with the initial lesson location state
        if (curLesson == null)
            setFirstLessonState();
        else {
            this.curricUnitList = curLesson.getCurricUnits();
            // If the student is in a cluster (within a CU), set the Cluster object and the Standard object.
            if (this.curClusterId != -1)  {
                this.curCluster = CCContentMgr.getInstance().getCluster(this.curClusterId);
                if (this.curStdId != null)
                    this.curStd = curCluster.getStandard(this.curStdId);
            }
            // If the student is in a standard (within a CU), set the Standard object.
            else if (this.curStdId != null)
                this.curStd = CCContentMgr.getInstance().getStandard(this.curStdId);
            else   // student must be in a CU that is just a problem list
                ;
            this.curCU = getCurCU(this.curLesson);
            // The first time the student enters, the probId is -1.  Thereafter (even in subsequent sessions) the probId will be found in the
            // lesson state or in the workspace state (if this is new session and the old lesson state was wiped)
            if (this.curProbId == -1)
                this.curProbId = state.getCurProblem();
            this.curProblem = ProblemMgr.getProblem(curProbId);
        }
    }

    // When student is not in a lesson, pick the first one for the class (later we'll figure out something smarter)
    // Then set up the lesson location state based on the first CU in the lesson.
    private void setFirstLessonState() throws SQLException {
        // for now we just take the first lesson out of the list.  Later we'll have to figure out which one using dates.
        if (classLessons.size() <= 0)
            return; // a failure - class must have some lessons
        // the first lesson selected will be the first one with some CUs
        int i=0;
        do {
            curLesson = classLessons.get(i++);
        } while (curLesson.getCurricUnits().size()<=0);

        if (curLesson == null)
            return;  // a failure - a class must have some lesson
        state.setCurLesson(curLesson.getId());
        this.lessonId = curLesson.getId();
        this.curricUnitList = curLesson.getCurricUnits();
        CurricUnit cu = curLesson.getCUByPosition(1);   // get the first CU
        setLessonLocationState(cu);
    }

    public Lesson getCurLesson () throws SQLException {
        Lesson curLesson=null;
        if (lessonId == -1) {
            return null;
        }
        else
            for (Lesson l: classLessons)
                if (l.getId() == this.lessonId)
                    curLesson = l;

        return curLesson;
    }

    public Lesson getNextLesson () {
        return null;
    }

    public boolean isCurLessonDone () throws SQLException {
        return isLessonDone(this.getCurLesson());
    }

    public boolean isLessonDone (Lesson l) throws SQLException {
        if (l == null)
            return true;
        CurricUnit cu = getCurCU(l);
        // the lesson is done if the current CU is done and there is no next CU
        if (isCUDone(cu)) {
            cu = getNextCU(l,cu);
            return (cu ==null);

        }
        else return false;
    }

    public boolean isCUDone (CurricUnit cu) {
        // The CU is done if the standard, cluster, or problem list is done
        CCCluster clust = cu.getCluster();
        if (clust != null)
            return isClusterDone(clust);
        CCStandard std = cu.getStandard();
        if (std != null)
            return isStandardDone(std);
        Problem prob = cu.getProblem();
        if (prob != null)
            return isProbDone(prob);
        return true;
    }

    // A cluster is done if the current standard (within it) is done and no next standard exists in the cluster
    private boolean isClusterDone(CCCluster clust) {
        if (isStandardDone(this.curStd))   {
            // N.B. standards are in no particular order within a cluster - probably not good and will have to add some default ordering in db.
            CCStandard next = clust.getNextStandard(this.curStd);
            return next == null;
        }
        return true;

    }

    // a standard is done if the student has completed the problems within it (using mastery, time, # problems, criteria)
    private boolean isStandardDone (CCStandard std) {

        // TODO for now we are just going through ALL the problems in a standard and not using exit criteria.
        // TODO We'll proceed through the problems within a standard in consecutive order and later will switch to binary searching with exit criteria.
        List<Problem> probs = std.getProblems();
        int i = probs.indexOf(this.curProblem);
        // so we just see if we are at the end of the list.
        return i == probs.size()-1;
    }

    private boolean isProbDone (Problem prob) {
        return (this.curProblem == prob) ;

    }

    public CCStandard getCurStandard (CCCluster clust){
        for (CCStandard std : clust.getStandards() ) {
            if (std.getId().equals(this.curStdId))
                return std;
        }
        return null;
    }

    public CurricUnit getCurCU (Lesson l) throws SQLException {
        if (l != null )  {
            List<CurricUnit> cus = l.getCurricUnits();
            for (CurricUnit u : cus)
                if (u.getId() == this.curCUId)
                    return u;
        }
        return null;
    }

    public CurricUnit getNextCU (Lesson l, CurricUnit curCU) {
        int i = curricUnitList.indexOf(curCU);
        if (i < curricUnitList.size() - 1)
            return curricUnitList.get(i+1);
        else return null;
    }

    // Given a new CU that we are moving the student to,  set the lesson location state.
    // The problem is set to null because problem selection takes care of setting the state for that .
    private void setLessonLocationState(CurricUnit cu) throws SQLException {
        curCU=cu;
        curCUId=cu.getId();
        state.setCurCU(curCUId);
        // set the cluster, standard, problem
        if ((curCluster = cu.getCluster()) != null) {
            curClusterId = curCluster.getId();
            state.setCurCluster(curClusterId);
            curStd = curCluster.getFirstStandard();
            curStdId = curStd.getId();
            state.setCurStd(curStdId);
            curProblem = nextProblem = null;
            curProbId =  nextProbId = -1;
            state.setNextProblem(-1);
        }
        else if ((curStd = cu.getStandard()) != null) {
            curStdId = curStd.getId();
            state.setCurCluster(-1);
            state.setCurStd(curStdId);
            curProblem = nextProblem = null;
            curProbId =  nextProbId = -1;
            state.setNextProblem(-1);
        }
        else {
            state.setCurCluster(-1);
            state.setCurStd(null);
            curProblem = nextProblem = null;
            curProbId =  nextProbId = -1;
            state.setNextProblem(-1);

        }
    }

    private void clearLessonLocationState() throws SQLException {
        curCU=null;
        curCUId=-1;
        curCluster=null;
        curClusterId=-1;
        curStd=null;
        curStdId=null;
        curProblem=null;
        curProbId=-1;
        nextProblem=null;
        nextProbId=-1;
        state.setCurCU(-1);
        state.setCurCluster(-1);
        state.setCurStd(null);
        state.setCurProblem(-1);
        state.setNextProblem(-1);
    }

        /**
         * This is called only when the state has been established.  If we are in a standard or problem list with remaining problems to show,
         * we get one.  Otherwise, we move forward in the lesson and set the student state accordingly.
         * @return
         */
    // for now we forego the idea 'intelligent' problem selection and just get the next problem from the lesson based on the structure of objects in the lesson
    public Problem getNextProblem() throws Exception {
        if (this.curStd != null) {

            // if all problems in the standard have been shown, move forward and return a problem
            if (isStandardDone(this.curStd)) {
                updateLocationInLesson();
                return getNextProblem();
            }
            else return getNextProblem(this.curStd);
        }
        else  // deal with a CU that is just a problem
	    {
            if (curCU.getProbId() <= 0) throw new UserException("Curriculum Unit " + curCU.getId()+ " does not have content of probId, stdId,or clusterId");
            Problem prob = ProblemMgr.getProblem(this.curCU.getProbId());
            if (isProbDone(prob)) {
                // move forward to next CU in the Lesson
                boolean more = updateLocationInLesson();
                if (more)
                    return getNextProblem();
                else return null;
            }
            else
                return prob;
        }
    }







    // move to the next piece of content structure (either a CU or a standard) and set the lessson location state.
    private boolean updateLocationInLesson() throws SQLException {
        // need to move ahead to the next location in the lesson (perhaps next standard in the cluster)
        // or perhaps to the next CU
        if (this.curCluster !=null) {
            return clusterMoveAhead(); // may move ahead one standard in this cluster or may move to next CU
        }
        else {
            return cuMoveAhead();
        }

    }



    // move ahead to the next standard in the cluster or move to the next cluster if at the end of the standards
    private boolean clusterMoveAhead() throws SQLException {
        // if the cluster has more standards move to one, else do cuMoveAhead
        this.curStd = this.curCluster.getNextStandard(this.curStd);
        if (curStd != null) {
            this.curStdId = this.curStd.getId();
            state.setCurStd(curStdId);
            return true;
        }
        else return cuMoveAhead();
    }

    // move ahead to the next CU
    private boolean cuMoveAhead() throws SQLException {
        curCU = getNextCU(curLesson,this.getCurCU(curLesson));
        if (curCU != null) {
            curCUId = curCU.getId();
            setLessonLocationState(curCU);
            return true;
        }
        else {
            clearLessonLocationState();
            return false;
        }
    }

    private Problem getNextProblem (CCStandard std) {
        List<Problem> probs = std.getProblems();
        int i = probs.indexOf(this.curProblem);
        Problem p= probs.get(i+1);
        return p;
    }

    /**
     * This supports a temp hack which allows the createion of a temp Lesson that contains a CU.   So
     * we want to prime the LessonMgr with this single Lesson as if it were for a class.
     * @param lessons
     */
    public void setClassLessons (List<Lesson> lessons) {
        this.classLessons = lessons;
    }
}
