package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/25/16
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCAccessors {
    private SessionManager smgr;
    private Connection conn;
    private StudentState state;
    private StudentModel studentModel;
    private StudentProblemHistory probSolveHist;
    private SessionEvent event ; // the current event sent in by user
    private StudentDataCache cache;

    private static final double LOW_MASTERY_LEVEL = 0.4;
    private static final double HIGH_MASTERY_LEVEL = 0.85;

    public static final int LOW_EMOTION = 2;
    public static final int HIGH_EMOTION = 4;

    /**
     * Creates the Object with a session manager instance and the current user event.   Must be done prior to calling
     * eval method to evaluate rules.
     * @param smgr
     * @param cache
     */
    public LCAccessors(SessionManager smgr, StudentDataCache cache) {
        this.smgr = smgr;
        this.conn = smgr.getConnection();
        this.state = smgr.getStudentState();
        this.studentModel = smgr.getStudentModel();
        this.probSolveHist = studentModel.getStudentProblemHistory();
        this.cache = cache;
    }

    public Object eval (LCFn fn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = this.getClass();
        Method m = c.getDeclaredMethod(fn.getFnName(),fn.getArgs());
        // The assumption is that all methods take an event for an argument and thats it.
        return m.invoke(this, this.event);
    }

    public String lastReportedEmotionLevel (SessionEvent ev) {
        int val = lastReportedEmotionValue(ev);
        if (val <= LOW_EMOTION)
            return "LOW";
        else if (val >= HIGH_EMOTION)
            return "HIGH";
        else return "NEUTRAL";
    }


    public int lastReportedEmotionValue (SessionEvent ev) {
        AffectStudentModel asm = (AffectStudentModel) studentModel;
        int val = asm.getLastReportedEmotionValue();
        return val;
    }

    public String lastReportedEmotion (SessionEvent ev) {
        AffectStudentModel asm = (AffectStudentModel) studentModel;
        return asm.getLastReportedEmotion();
    }

    public double lastTopicMasteryValue (SessionEvent ev) {
        Double d = cache.lastTopicMasteryValue;
        if (d == null) {
            int lt = state.getLastTopic();
            if (lt != -1) {
                try {
                    double v= studentModel.getTopicMastery(lt);
                    cache.lastTopicMasteryValue=v;
                } catch (SQLException e) {
                    e.printStackTrace();
                    cache.lastTopicMasteryValue=0.01; // protection against exceptions and/or divide by zero errors.
                }
            } else cache.lastTopicMasteryValue=0.01;
        }
        return cache.lastTopicMasteryValue;
    }

    /**
     * Gets the mastery in the current topic.
     * @return
     */
    public double topicMasteryValue(SessionEvent ev)  {
        try {
            Double d = cache.curTopicMasteryValue;
            if (d == null) {
                cache.curTopicMasteryValue = studentModel.getTopicMastery(state.getCurTopic());
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            cache.curTopicMasteryValue= 0.01;   // protection against exceptions and/or divide by zero errors.
        }
        return cache.curTopicMasteryValue;
    }

    public boolean isTopicMasteryLow (SessionEvent ev) {
        double d = topicMasteryValue(ev);
        return d < LOW_MASTERY_LEVEL;
    }

    public boolean isTopicMasteryMiddle (SessionEvent ev) {
        double d = topicMasteryValue(ev);
        return d >= LOW_MASTERY_LEVEL && d < HIGH_MASTERY_LEVEL;
    }

    public boolean isTopicMasteryHigh (SessionEvent ev) {
        double d = topicMasteryValue(ev);
        return d >= HIGH_MASTERY_LEVEL;
    }


    /**
     * Return true if the current user event is an attempt
     * @param ev
     * @return
     */
    public boolean isAttemptEvent  (SessionEvent ev) {
        return (ev instanceof AttemptEvent);
    }

    public boolean isHintEvent  (SessionEvent ev) {
        return (ev instanceof HintEvent);
    }

    public boolean isExampleEvent  (SessionEvent ev) {
        return (ev instanceof ShowExampleEvent);
    }

    public boolean isVideoEvent  (SessionEvent ev) {
        return (ev instanceof ShowVideoEvent);
    }

    public boolean isReadProblemEvent (SessionEvent ev) {
        return (ev instanceof ReadProblemEvent);
    }

    /**
     * Get the number of incorrect attempts in the current problem.
     * @return
     */
    public int numIncorrectAttempts (SessionEvent ev) {
        return state.getNumMistakesOnCurProblem();
    }

    /**
     * Get the number of attempts it took to solve the problem.  -1 indicates problem hasn't been solved.
     * @return
     */
    public int numAttemptsToSolve (SessionEvent ev) {
        if (state.getTimeToSolve() > 0)
            return state.getNumMistakesOnCurProblem() + 1;
        return -1;
    }

    public int numHintsGiven (SessionEvent ev) {
        int n = state.getNumHintsGivenOnCurProblem() ;
        return n;
    }

    /**
     * Get how long it takes to solve the current problem. -1 indicates not solved.
     * @return
     */
    public double timeToSolveSecs (SessionEvent ev) {
        return state.getTimeToSolve() / 1000.0;
    }

    public double timeToFirstHintSecs (SessionEvent ev) {
        return state.getTimeToFirstHint() / 1000.0;
    }

    public double timeToFirstAttemptSecs (SessionEvent ev) {
        return state.getTimeToFirstAttempt() / 1000.0;
    }
    public double timeToSecondAttemptSecs (SessionEvent ev) {
        return state.getTimeToSecondAttempt() / 1000.0;
    }
    public double timeToThirdAttemptSecs (SessionEvent ev) {
        return state.getTimeToThirdAttempt() / 1000.0;
    }


    public boolean isSolved (SessionEvent ev) {
        return state.isProblemSolved();
    }



    // Get the effort on the current problem
    public String currentEffort (SessionEvent ev) {
        String eff = cache.curEffort;
        if (eff == null) {
            cache.curEffort= historyEffortN(0);
        }
        return cache.curEffort;
    }

    // get the effort on the problem 3 before the current
    public String effort3 (SessionEvent ev) {
        String eff = cache.effort3;
        if (eff == null) {
            cache.effort3= historyEffortN(3);
        }
        return cache.effort3;
    }

    // get the effort on the problem 2 before the current
    public String effort2 (SessionEvent ev) {
        String eff = cache.effort2;
        if (eff == null) {
            cache.effort2= historyEffortN(2);
        }
        return cache.effort2;
    }
    // Get the effort on the previous
    public String effort1 (SessionEvent ev) {
        String eff = cache.effort1;
        if (eff == null) {
            cache.effort1= historyEffortN(1);
        }
        return cache.effort1;
    }


    /**
     * Get the effort of the nth problem where n is a number indicating how many problems to go back.
     * 0 indicates the last problem the student saw.
     *
     * @param n
     * @return The effort or empty string "" if nothing can be found
     */
    public String historyEffortN (int n) {
        StudentProblemData d = historyN(n);
        if (d != null)
            return (d.getEffort() != null) ?  d.getEffort() :  "";
        else return "";

    }

    // Return the nth history record from the student problem solve history.
    public StudentProblemData historyN (int n) {
        int c = 0;
        for (StudentProblemData d : probSolveHist.getReverseHistory()) {
            if (c == n)
                return d;
            c++;
        }
        return null;
    }

    public boolean videoExists (SessionEvent e) throws SQLException {
        if (cache.videoExists == null) {
            int curProbId = state.getCurProblem();
            Problem p = ProblemMgr.getProblem(curProbId);
            if (p != null) {
                String v = p.getVideo();
                cache.videoExists = v != null && !v.equals("");
            }
            else cache.videoExists= false;
        }
        return cache.videoExists;

    }

    public boolean exampleExists (SessionEvent e) throws SQLException {
        if (cache.exampleExists == null) {
            int curProbId = state.getCurProblem();
            Problem p = ProblemMgr.getProblem(curProbId);
            if (p != null) {
                int ex = p.getExample();
                cache.exampleExists = ex != -1;
            }
            else cache.exampleExists= false;
        }
        return cache.exampleExists;
    }

    public boolean hintExists (SessionEvent e) throws SQLException {
        if (cache.hintExists == null) {
            int curProbId = state.getCurProblem();
            Problem p = ProblemMgr.getProblem(curProbId);
            if (p != null) {
                List<Hint> hs = p.getHints();
                cache.hintExists= hs != null && hs.size() > 0;
            }
            else cache.hintExists =false;

        }
        return cache.hintExists;
    }

    public double curProbDifficulty (SessionEvent e) throws SQLException {
        if (cache.curProbDifficulty == null) {
            int curProbId = state.getCurProblem();
            Problem p = ProblemMgr.getProblem(curProbId);
            cache.curProbDifficulty= p.getDiff_level();
        }
        return cache.curProbDifficulty;
    }

    public double lastProbDifficulty (SessionEvent e) throws SQLException {
        if (cache.lastProbDifficulty == null) {
            int pid = state.getLastProblem();
            Problem p = ProblemMgr.getProblem(pid);
            if (p != null)
                cache.lastProbDifficulty = p.getDiff_level();
            else cache.lastProbDifficulty = 0.0;
        }
        return cache.lastProbDifficulty ;
    }

    public int curProbTopic (SessionEvent e) {
        return state.getCurTopic();

    }

    public String curProbMode (SessionEvent e) {
        return state.getCurProblemMode();
    }

    public int lastProbTopic (SessionEvent e) {
        if (cache.lastProbTopic == null) {
            final StudentProblemData data = historyN(1);
            if (data != null)
                cache.lastProbTopic = data.getTopicId();
            else cache.lastProbTopic = -1;
        }
        return cache.lastProbTopic ;
    }

    public boolean curProbTopicSameAsLastProbTopic (SessionEvent e) {
        return curProbTopic(e) == lastProbTopic(e);
    }

    public boolean curProbHarderThanLastProb (SessionEvent e) throws SQLException {
        double c = curProbDifficulty(e);
        double l = lastProbDifficulty(e);
        return c > l;
    }

    public boolean lastProblemIsExampleOrDemo (SessionEvent e) {
        if (cache.lastProblemIsDemoOrExample == null) {
            final StudentProblemData d = historyN(1);
            if (d != null)
                cache.lastProblemIsDemoOrExample = d.getMode().equals(Problem.DEMO) || d.getMode().equalsIgnoreCase(Problem.EXAMPLE);
            else cache.lastProblemIsDemoOrExample = false;
        }
        return cache.lastProblemIsDemoOrExample;
    }

    // The first problem in a topic is either a demo problem or one that is labelled with a different topic than the previous problem
    public boolean isFirstProblemInTopic (SessionEvent e) {
        if (cache.isFirstProblemInTopic == null) {
            final StudentProblemData l = historyN(1);  // the last prob
            if (l != null) {
                int curTopic = state.getCurTopic();
                cache.isFirstProblemInTopic = (l.getMode().equals(Problem.DEMO) || l.getTopicId() != curTopic);
            } else cache.isFirstProblemInTopic = true;
        }
        return cache.isFirstProblemInTopic ;
    }


    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection();
            Settings.lessonMap = DbPedagogy.buildAllLessons(conn);
            Settings.loginMap = DbPedagogy.buildAllLoginSequences(conn);
            Settings.pedagogyGroups = DbPedagogy.buildAllPedagogies(conn,null);
            SessionManager smgr = new SessionManager(conn);
            smgr.attemptSessionCreation("dm","dm",System.currentTimeMillis(),true);

            System.out.println("Testing LCAccessors with user dm/dm studId="+ smgr.getStudentId());
            LCAccessors fns = new LCAccessors(smgr,null);
            String eff = fns.historyEffortN(1);
            System.out.println("Last effort was " + eff);
            eff = fns.historyEffortN(2);
            System.out.println("Second to last effort was " + eff);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
