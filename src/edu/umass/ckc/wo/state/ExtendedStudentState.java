package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.util.Dates;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/3/16
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedStudentState {
    private SessionManager smgr;

    public ExtendedStudentState(SessionManager smgr) {
        this.smgr = smgr;
    }

    public boolean isInInterleavedProblemSet() throws SQLException {
        return smgr.getStudentState().getCurTopic() == Settings.interleavedTopicID;
    }

    public List<Integer> getPracticeProblemsSeenInTopic (int topicId) throws Exception {
        StudentProblemHistory studentProblemHistory = smgr.getStudentModel().getStudentProblemHistory();
        List<StudentProblemData> probEncountersInTopic = studentProblemHistory.getTopicHistoryMostRecentEncounters(topicId);
//                practiceProbsSeen = studentProblemHistory.getPracticeProblemsSeen(probEncountersInTopic);
        return getPracticeProblemsSeen(probEncountersInTopic);
    }

    /**
     * Returns ids of problems that have been given to the student.  Problems considered "seen" must be within
     * the problem reuse interval specified for the pedagogy and class.
     * @param probEncountersInTopic
     * @return
     * @throws Exception
     */
    private List<Integer> getPracticeProblemsSeen (List<StudentProblemData> probEncountersInTopic) throws Exception {

        // get the ones that are within the problemReuseInterval
        List<Integer> probs = new ArrayList<Integer>();
        int nSessionReuseInterval = smgr.getPedagogicalModel().getParams().getProblemReuseIntervalSessions();
        int nDayReuseInterval = smgr.getPedagogicalModel().getParams().getProblemReuseIntervalDays();
        int sess = smgr.getSessionNum();
        Date now = new Date(System.currentTimeMillis());
        int numSessions=0;
        for (StudentProblemData d: probEncountersInTopic) {
            Date probBeginTime = new Date(d.getProblemBeginTime());
            if (d.getSessId() != sess) {
                numSessions++;
                sess = d.getSessId();
            }
            int dayDiff = Dates.computeDayDiff(now, probBeginTime);
            if (numSessions == nSessionReuseInterval || dayDiff >= nDayReuseInterval)
                break;
            if (d.isPracticeProblem())
                probs.add(d.getProbId());

        }
        return probs;

    }

    /**
     * Get a list of problems the student has solved or seen as an example.   Works using the problemReuseInterval which is a number of
     * sessions or days.  We only select problems within the interval.  This is a way to control recency.   We want solved problems and examples to be eligible to
     * show again after a certain number of sessions or days (ideally this number might be determined on a per student basis but for now it lives in the pedagogy
     * definition)
     * @return
     * @throws Exception
     */
    public List<Integer> getRecentExamplesAndCorrectlySolvedProblems (List<StudentProblemData> probEncountersInTopic) throws Exception {
        // get the ones that are within the problemReuseInterval
        List<Integer> probs = new ArrayList<Integer>();
        int nSessionReuseInterval = smgr.getPedagogicalModel().getParams().getProblemReuseIntervalSessions();
        int nDayReuseInterval = smgr.getPedagogicalModel().getParams().getProblemReuseIntervalDays();
        int sess = smgr.getSessionNum();
        Date now = new Date(System.currentTimeMillis());
        int numSessions=0;
        for (StudentProblemData d: probEncountersInTopic) {
            Date probBeginTime = new Date(d.getProblemBeginTime());
            if (d.getSessId() != sess) {
                numSessions++;
                sess = d.getSessId();
            }
            int dayDiff = Dates.computeDayDiff(now,probBeginTime);
            // We stop when one of the intervals is reached
            if (numSessions == nSessionReuseInterval || dayDiff >= nDayReuseInterval)
                break;
            if (d.isSolved())
                probs.add(d.getProbId());
            else if (d.getMode().equals(Problem.DEMO))
                probs.add(d.getProbId());
        }
        return probs;
    }


}
