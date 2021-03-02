package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.db.DbStudentProblemHistory;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutormeta.StudentEffort;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 6/13/12
 * Time: 11:36 AM
 * This is an in-memory representation of a students problem history.   It works with the DbStudentProblemHistory class
 * Note: A problem shown to the student more than once has multiple entries.   It is stored with the most recent entry on the end of the list.
 * 
 * Kartik 03-01-21	issue #286 Mastery Update is Incorrect, goes down to 0.1 due to incorrect topicId. Fixed topicId retrieval. 
 */
public class StudentProblemHistory {

    private static Logger logger =   Logger.getLogger(StudentProblemHistory.class);

    private List<StudentProblemData> history;
    private StudentProblemData curProb;

    public StudentProblemHistory() {
        history = new ArrayList<StudentProblemData>();
    }

    public StudentProblemHistory (Connection conn, int studId) throws SQLException {
        history = new ArrayList<StudentProblemData>();
        DbStudentProblemHistory.loadHistory(conn,studId,history);
        if (history.size() > 0)
            curProb = history.get(history.size()-1);
    }

    // maybe for now we don't need the in-memory model since nothing is reasoning about it.   Its just record keeping for the time being, so we'll
    // go direct to the db.

    public StudentProblemData beginProblem (SessionManager smgr, BeginProblemEvent e) throws SQLException {
        StudentState state = smgr.getStudentState();
        long now = System.currentTimeMillis();
        // if student forced a problem + topic we log that topic, o/w we use the topic that the student is actually in
        int topicId = (state.getStudentSelectedTopic() != -1) ? state.getStudentSelectedTopic() : state.getCurTopic();
        // in some situations (e.g. student comes into tutor without being in a topic) we won't have a legal topic.  Thus
        // we need to get a dummy topic to insert into the history so that foreign keys work.    In other situations
        // (MPP) the topic id gets lost and then it sends illegal values which also cause failures.   This will repair the topicId but
        // logs with a warning because this should be fixed.
        if (topicId < 0) {
            logger.error("StudentProblemHistory.beginProblem:  ILLEGAL TOPIC ID " + topicId + " passed from client.   Replacing with dummy value.");
            topicId = DbTopics.getDummyTopic(smgr.getConnection());
            state.setCurTopic(topicId); // puts the student in this dummy topic so future transactions don't have failures.
        }
        String params = "";
        Problem p = ProblemMgr.getProblem(state.getCurProblem());
        if (p != null && p.isParametrized()) {
            params = state.getProblemBinding();
        }
        // The time since this session began
        long timeInSessionMS = now - DbSession.getSessionBeginTime(smgr.getConnection(),smgr.getSessionId());
        // The time since the first time this student ever logged into mathspring
        long timeInTutorMS = timeInSessionMS + DbSession.getTimeInAllSessions(smgr.getConnection(),smgr.getStudentId());
        long timeInSessionSec = timeInSessionMS / 1000;
        long timeInTutorMin = timeInTutorMS / 60000;

        DbStudentProblemHistory.beginProblem(smgr.getConnection(), e.getSessionId(), smgr.getStudentId(), state.getCurProblem(), topicId,
            now, timeInSessionSec, timeInTutorMin, state.getCurProblemMode(), params, smgr.getCollaboratingWith(),
                p.getDifficulty());

        curProb = new StudentProblemData(state.getCurProblem(),state.getCurTopic(),e.getSessionId(),
                now,timeInSessionSec, timeInTutorMin,state.getCurProblemMode());
        addProblem(curProb);
        return curProb;

    }

    // return the effort on the last 3 practice problems
    public StudentEffort getEffort (int thisSessionId) {
        List<StudentProblemData> hist = getReverseHistory();

        String[] effs = new String[] {"?","?","?"} ;
        int i=0;
        for (StudentProblemData d : hist) {
            if (d.isPracticeProblem() && d.getSessId() == thisSessionId)
                effs[i++] = d.getEffort();
            else if (d.isTopicIntro()&& d.getSessId() == thisSessionId)
                effs[i++] = "INTRO";
            else if (d.isExample()&& d.getSessId() == thisSessionId)
                effs[i++] = "EXAMPLE";
            else if (d.isDemo()&& d.getSessId() == thisSessionId)
                effs[i++]= "DEMO";
            if (i== 3) break;

        }
        return new StudentEffort(effs[2],effs[1],effs[0]) ;
    }

    public void saveCurProbEffort (Connection conn, int studId, String effort) throws SQLException {
        int studProbHistId = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(conn,studId);
        DbStudentProblemHistory.updateVar(studProbHistId,conn,DbStudentProblemHistory.EFFORT,effort);
    }



    public void endProblem(SessionManager smgr, StudentProblemData curProbData, int topicId) throws Exception {
        Connection conn = smgr.getConnection();
        StudentState state = smgr.getStudentState();
        int studProbHistId = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(conn,smgr.getStudentId());
        int numAttemptsToSolve = (state.isProblemSolved() ? (state.getNumMistakesOnCurProblem() + 1) : 0);
        long now = System.currentTimeMillis();
        String hint = state.getCurHint();
        EffortHeuristic effortComputer = new EffortHeuristic();
       // TODO do not set effort if the current problem is Topic Intro or Example
        // Special case:  If starting a problem or topic from MPP, it first does an EndProblem on the old problem.
        // But some of the topicState got reset so we can't compute effort.  So just leave the effort as it was
        
        //In the special case, if starting a new topic, the old problem shoud be ended with the correct topicId 
        if (curProbData.getTopicId()!=0) {
        	topicId = curProbData.getTopicId();
        }
        
        String effort = null;
        if (state.getCurProblemMode() != null)
            effort = effortComputer.computeEffort(state);
        else
            effort =  curProbData.getEffort();
        DbStudentProblemHistory.endProblem(smgr.getConnection(),studProbHistId,state.getNumHintsBeforeCorrect(),
                numAttemptsToSolve,state.getTimeToSolve(),
               state.getTimeToFirstHint(),state.getTimeToFirstAttempt(),state.isProblemSolved(), now,
                state.getNumMistakesOnCurProblem(),
                state.getNumHintsGivenOnCurProblem(), state.isSolutionHintGiven(),
                smgr.getStudentModel().getTopicMastery(topicId),
                effort,state.isVideoShown(),state.getProbExamplesShown(),state.isTextReaderUsed(),
                state.getTimeToSecondHint(), state.getTimeToThirdHint(), state.getTimeToSecondAttempt(), state.getTimeToThirdAttempt(), state.isCurProbBroken());

        curProbData.setNumAttemptsToSolve(numAttemptsToSolve);
        curProbData.setNumHintsBeforeCorrect(state.getNumHintsBeforeCorrect());
        curProbData.setTimeToSolve(state.getTimeToSolve());  // should be zero if problem has not been solved.
        curProbData.setTimeToFirstHint(state.getTimeToFirstHint());
        curProbData.setTimeToSecondHint(state.getTimeToSecondHint());
        curProbData.setTimeToThirdHint(state.getTimeToThirdHint());
        curProbData.setTimeToFirstAttempt(state.getTimeToFirstAttempt());
        curProbData.setSolved(state.isProblemSolved());
        curProbData.setProblemEndTime(now);
        curProbData.setNumMistakes(state.getNumMistakesOnCurProblem());
        curProbData.setGivenAnswerHint(state.isSolutionHintGiven());
        curProbData.setMastery( smgr.getStudentModel().getTopicMastery(topicId));
        curProbData.setEffort(effort);
        curProbData.setSeenVideo(state.isVideoShown());
        curProbData.setSeenExample(state.isExampleShown());
        curProbData.setUsedTextReader(state.isTextReaderUsed());
        curProbData.setProblemBroken(state.isCurProbBroken());


    }

    public void attempt (SessionManager smgr, boolean isCorrect, long probElapsedTime, int numAttempts) throws SQLException {
        int histRecId = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(smgr.getConnection(),smgr.getStudentId());
        if (numAttempts==1) {
            curProb.setTimeToFirstAttempt(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_FIRST_ATTEMPT,probElapsedTime) ;

        }
        else if (numAttempts==2) {
            curProb.setTimeToSecondAttempt(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_SECOND_ATTEMPT,probElapsedTime) ;

        }
        else if (numAttempts==3) {
            curProb.setTimeToThirdAttempt(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_THIRD_ATTEMPT,probElapsedTime) ;

        }

        if (!isCorrect) {
            curProb.setNumMistakes(curProb.getNumMistakes()+1);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.NUM_MISTAKES,curProb.getNumMistakes()) ;
        }
        else {
            curProb.setNumAttemptsToSolve(curProb.getNumAttemptsToSolve()+1);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.NUM_ATTEMPTS_TO_SOLVE,curProb.getNumMistakes()) ;
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.IS_SOLVED,1) ;
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_SOLVE,probElapsedTime) ;

        }
    }

    public void hint (SessionManager smgr, long probElapsedTime, boolean givesSolution) throws SQLException {
        int histRecId = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(smgr.getConnection(),smgr.getStudentId());

        if (curProb.getNumHints() == 0) {
            curProb.setTimeToFirstHint(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_FIRST_HINT,probElapsedTime) ;
        }
        else  if (curProb.getNumHints() == 1) {
            curProb.setTimeToSecondHint(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_SECOND_HINT,probElapsedTime) ;
        }
        else  if (curProb.getNumHints() == 2) {
            curProb.setTimeToThirdHint(probElapsedTime);
            DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.TIME_TO_THIRD_HINT,probElapsedTime) ;
        }
        curProb.setGivenAnswerHint(givesSolution);
        DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.SOLUTION_HINT_GIVEN,curProb.getGivenAnswerHint()) ;

        curProb.setNumHintsBeforeCorrect(curProb.getNumHintsBeforeCorrect() + 1);
        curProb.setNumHints(curProb.getNumHints() + 1);
        DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.NUM_HINTS_BEFORE_SOLVE,curProb.getNumHintsBeforeCorrect()) ;
        DbStudentProblemHistory.updateVar(histRecId, smgr.getConnection(),DbStudentProblemHistory.NUM_HINTS,curProb.getNumHints()) ;


    }

    public void updateEmotions (SessionManager smgr, String emotion, int level) throws SQLException {
        int studProbHistId = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(smgr.getConnection(),smgr.getStudentId());

        DbStudentProblemHistory.updateEmotions(smgr.getConnection(),studProbHistId,emotion,level);
    }

    public void addProblem(StudentProblemData d) {
        history.add(d);
    }

    public StudentProblemData getCurProblem () {
        return curProb;
    }

    public List<StudentProblemData> getHistory() {
        return history;
    }

    public List<StudentProblemData> getReverseHistory() {
        List<StudentProblemData> h= (List<StudentProblemData>) ((ArrayList<StudentProblemData>) history).clone();
        Collections.reverse(h);
        return h;
    }

    public int getTimesEncountered(int probId) {
        int timesEncountered = 0;
        for (StudentProblemData d: history) {
            if (d.getProbId() == probId) {
               timesEncountered++;
            }
        }
        return timesEncountered;
    }

    // get problems in the history that are in the topic and were ended within N days of the given time.
    public List<StudentProblemData> getTopicHistory(int topicID, int withinNDays) {
        List<StudentProblemData> result = new ArrayList<StudentProblemData>();
        long now = System.currentTimeMillis();
        for (StudentProblemData d: history) {
            long probEndTime=d.getProblemEndTime();
            int daysDiff = (int) (now - probEndTime) / (1000 * 60 * 60 * 24);
            if (d.getTopicId() == topicID && daysDiff <= withinNDays)
                result.add(d);
        }
        return result;
    }


    // get problems in the history that are sorted by students most recent encounter (this means a problem seen more than
    // once will have the most recent encounter earlier in the list.
    public List<StudentProblemData> getTopicHistoryMostRecentEncounters (int topicID) {
        List<StudentProblemData> result = getTopicHistory(topicID);
        // sort so that list is in descending order based on endTime
        Collections.sort(result,new Comparator<StudentProblemData>() {
            public int compare(StudentProblemData o1, StudentProblemData o2) {
                return (o1.getProblemEndTime() < o2.getProblemEndTime()) ? 1 : (o1.getProblemEndTime() == o2.getProblemEndTime() ? 0 : -1);
            }
        });
        return result;

    }

    // get problems in the history that are sorted by students most recent encounter (this means a problem seen more than
    // once will have the most recent encounter earlier in the list.
    public List<StudentProblemData> getCCSSHistoryMostRecentEncounters (String ccss) throws SQLException {
        List<StudentProblemData> result = getCCSSHistory(ccss);
        // sort so that list is in descending order based on endTime
        Collections.sort(result,new Comparator<StudentProblemData>() {
            public int compare(StudentProblemData o1, StudentProblemData o2) {
                return (o1.getProblemEndTime() < o2.getProblemEndTime()) ? 1 : (o1.getProblemEndTime() == o2.getProblemEndTime() ? 0 : -1);
            }
        });
        return result;

    }

        // get problems in the history that are in the topic
        public List<StudentProblemData> getTopicHistory(int topicID) {
            List<StudentProblemData> result = new ArrayList<StudentProblemData>();
            long now = System.currentTimeMillis();
            for (StudentProblemData d: history) {
                if (d.getTopicId() == topicID )
                    result.add(d);
            }
            return result;
        }

    // get problems in the history that are in the standard
    public List<StudentProblemData> getCCSSHistory(String ccss) throws SQLException {
        List<StudentProblemData> result = new ArrayList<StudentProblemData>();
        long now = System.currentTimeMillis();
        for (StudentProblemData d: history) {
            int probId = d.getProbId();
            Problem p = ProblemMgr.getProblem(probId);
            if (p.hasStandard(ccss))
                result.add(d);
        }
        return result;
    }

    public List<String> getTopicProblemsSeen(int topicId) {
        List<StudentProblemData> topicHist = getTopicHistory(topicId);
        List<String> ids = new ArrayList<String>();
        for (StudentProblemData d: topicHist) {
            if (d.isPracticeProblem())
                ids.add(Integer.toString(d.getProbId()));
        }
        return ids;
    }

    public List<String> getTopicProblemsSolved(int topicId) {
        List<StudentProblemData> topicHist = getTopicHistory(topicId);
        List<String> ids = new ArrayList<String>();
        for (StudentProblemData d: topicHist) {
            if (d.isPracticeProblem() && d.isSolved())
                ids.add(Integer.toString(d.getProbId()));
        }
        return ids;
    }

    public List<String> getTopicProblemsSolvedOnFirstAttempt(int topicId) {
        List<StudentProblemData> topicHist = getTopicHistory(topicId);
        List<String> ids = new ArrayList<String>();
        for (StudentProblemData d: topicHist) {
            if (d.isPracticeProblem() && d.getNumAttemptsToSolve()==1)
                ids.add(Integer.toString(d.getProbId()));
        }
        return ids;
    }

    public int getNumPracticeProbsSeenInTopicAcrossSessions(int topicID) {
        List<String> probs = getTopicProblemsSeen(topicID);
        if (probs != null)
            return probs.size();
        else return 0;
    }

    // Find the last time the problem was given as practice and return it or null.
    public StudentProblemData getMostRecentPracticeProblemEncounter (int probId) {
        for (int i=history.size()-1; i>=0; i--) {
            StudentProblemData d = history.get(i);
            if (d.getProbId() == probId && d.isPracticeProblem())
                return d;
        }
        return null;

    }

    // if the problem has been given as practice and answered then it had an effect on mastery.  This function is used to determine
    // if an answer to a problem should update mastery, so return true if the problem was answered within the reuse-interval.
    public boolean answeredRecently(int probId, int sessId, int problemReuseIntervalDays, int problemReuseIntervalSessions) {
        long now = System.currentTimeMillis();
        int lastSess=sessId;
        int sessCount=0;
        int n = history.size()-2; // don't want to start with the last record because that is the current problem (== probId)
        if (n >= 0) {
            for (int i = n; i != 0; i--) {
                StudentProblemData d = history.get(i);
                if (d.getSessId() != lastSess) {
                    lastSess = d.getSessId();
                    sessCount++;
                }
                long probEndTime = d.getProblemEndTime();
                int daysDiff = (int) (now - probEndTime) / (1000 * 60 * 60 * 24);
                // if the problem has been seen within the number of days in the reuse interval OR
                // seen in a session within the # of sessions in the reuse interval return true.
                if (d.getProbId() == probId && d.isPracticeProblem() && (d.getTimeToFirstAttempt() > 0) &&
                        (daysDiff <= problemReuseIntervalDays || sessCount <= problemReuseIntervalSessions))
                    return true;
                // if we've gone back far enough to be outside the interval of days and sessions, return false
                if (daysDiff > problemReuseIntervalDays && sessCount > problemReuseIntervalSessions)
                    return false;
            }
        }
        return false;

    }
}
