package edu.umass.ckc.wo.log;

import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.AttemptResponse;
import edu.umass.ckc.wo.tutor.response.ExampleResponse;
import edu.umass.ckc.wo.tutor.response.HintSequenceResponse;
import edu.umass.ckc.wo.content.Hint;

import java.sql.*;
import java.util.List;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jan 29, 2009
 * Time: 1:56:20 PM
 * 
 * Frank	09-23-20	issue #237 added logStudentAccess() 
 * Frank	09-29-20	issue #237R3 added logStudentAccess() 
 * Frank	07-03-21	added logGazeWanderingEvent
 */
public class TutorLogger {
    private SessionManager smgr;
    Connection conn;
    public static final String EventLog = "EventLog";
    public static final String OldEventLog = "EpisodicData2";

    public static final String[] TUTOR_HUT_EVENTS = {  "BeginProblem", "NextProblem", "EndProblem", "BeginIntervention",
            "EndIntervention", "InputResponse", "Attempt",


    };





    public TutorLogger (SessionManager smgr) {
        this.smgr = smgr;
        this.conn = smgr.getConnection();

    }

    private int getTopic () {
        if (smgr.getStudentState().getStudentSelectedTopic() != -1)
            return  smgr.getStudentState().getStudentSelectedTopic();
        else return smgr.getStudentState().getCurTopic();
    }


    public int clearSessionLog (int sessNum, String eventTable) throws SQLException {
        return clearSessionLog(this.conn,sessNum,eventTable);
    }

    public static int clearSessionLog (Connection conn, int sessNum, String eventTable) throws SQLException {
        String q = "delete from "+eventTable+" where sessNum=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1,sessNum);
            int n = ps.executeUpdate();
            return n;
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    public int clearSessionLog (int sessNum) throws SQLException {
        return clearSessionLog(sessNum,EventLog) +
            clearSessionLog(sessNum,OldEventLog);
    }

    public int insertLogEntry(String action, String userInput, boolean isCorrect, long elapsedTime,
                              long probElapsed, String hintStep, int hintId, String emotion,
                              String activityName, int auxId, String auxTable, int curTopicId, long clickTimeMs) throws Exception {

        return insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
                smgr.getStudentState().getCurProblem(), hintStep, hintId, emotion, activityName, auxId, auxTable, curTopicId, clickTimeMs);
    }

    public int insertLogEntry(String action, String userInput, boolean isCorrect, long elapsedTime,
                              long probElapsed, String hintStep, int hintId, String emotion, String activityName, int curTopicId, long clickTimeMs) throws Exception {

        return insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
                smgr.getStudentState().getCurProblem(), hintStep, hintId, emotion, activityName, -1, null, curTopicId, clickTimeMs);
    }

    public int insertLogEntryAttempt(String action, String userInput, boolean isCorrect, long elapsedTime,
            long probElapsed, String hintStep, int hintId, String emotion, String activityName, int curTopicId, long clickTimeMs, int probLangIndex) throws Exception {

    		return insertLogEntryWorkerLang(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
    				smgr.getStudentState().getCurProblem(), hintStep, hintId, emotion, activityName, -1, null, curTopicId, clickTimeMs, probLangIndex);
}    
    public int insertLogEntry(String action, int probId, String userInput, boolean isCorrect, long elapsedTime,
                              long probElapsed, String hintStep, int hintId, String emotion, String activityName, int curTopicId, long clickTimeMs) throws Exception {

        return insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
                probId, hintStep, hintId, emotion, activityName, -1, null, curTopicId, clickTimeMs);
    }

    public int insertLogEntryNextProb(String action, int probId, String userInput, boolean isCorrect, long elapsedTime,
            long probElapsed, String hintStep, int hintId, String emotion, String activityName, int curTopicId, long clickTimeMs, int probLangIndex) throws Exception {

    	return insertLogEntryWorkerLang(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
    			probId, hintStep, hintId, emotion, activityName, -1, null, curTopicId, clickTimeMs, probLangIndex);
    }


    
    public int insertLogEntry(String action, int probId, String userInput, boolean isCorrect, long elapsedTime,
                              long probElapsed, String hintStep, int hintId, String emotion, String activityName,
                              int auxId, String auxTable, int curTopicId, long clickTimeMs) throws Exception {

        return insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionNum(), action, userInput, isCorrect, elapsedTime, probElapsed,
                probId, hintStep, hintId, emotion, activityName, auxId, auxTable, curTopicId, clickTimeMs);
    }

    private int getDummyProbId() throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id from problem where form='dummy'";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            throw new SQLException("The dummy problem is missing from the database.   There must be a problem with form=dummy to support events that do not reference a legal problem.");
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public void newSession (int studId, int sessNum, long elapsedTime) throws SQLException {
        this.insertLogEntryWorker(studId, sessNum, "NewSession", "", false, elapsedTime, 0, -1, null, -1, null, null, -1, null, -1, -1);
    }

    private int getStudentProblemHistoryEntry (int sessNum) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select max(id) from studentproblemhistory where sessionid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, sessNum);
            rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            else
                return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }


    public int insertLogEntryWorker(int studId, int sessNum, String action, String userInput, boolean isCorrect, long elapsedTime,
                                    long probElapsed, int probId, String hintStep, int hintId, String emotion, String activityName,
                                    int auxId, String auxTable, int curTopicId, long clickTimeMs) throws SQLException {
        int studProbHistId = getStudentProblemHistoryEntry(sessNum);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (probId < 1)
                probId = getDummyProbId();
            String q = "insert into " + EventLog + " (studId, sessNum, action, userInput, isCorrect, elapsedTime, probElapsed, problemId, hintStep, " +
                    "hintid, emotion, activityName, auxId, auxTable,time,curTopicId, clickTime, probhistoryid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, studId);
            ps.setInt(2, sessNum);
            ps.setString(3, action);

            if (userInput == null)
                ps.setNull(4,Types.VARCHAR);
            else ps.setString(4,userInput.substring(0,Math.min(userInput.length(),1500))); // column can only store 1500 chars
            ps.setBoolean(5,isCorrect);
            ps.setLong(6,elapsedTime);
            ps.setLong(7,probElapsed);
            ps.setInt(8,probId);
            if (hintStep == null)
                ps.setNull(9,Types.VARCHAR);
            else ps.setString(9,hintStep);
            if (hintId == -1)
                ps.setNull(10,Types.INTEGER);
            else ps.setInt(10,hintId);
            if (emotion == null)
                ps.setNull(11, Types.VARCHAR);
            else ps.setString(11,emotion);
            if (activityName == null)
                ps.setNull(12,Types.VARCHAR);
            else ps.setString(12,activityName);
            if (auxId == -1)
                ps.setNull(13, Types.INTEGER);
            else ps.setInt(13,auxId);
            if (auxTable == null)
                ps.setNull(14,Types.VARCHAR);
            else ps.setString(14,auxTable);
            ps.setTimestamp(15,new Timestamp(System.currentTimeMillis()));
            if (curTopicId > 0)
                ps.setInt(16,curTopicId);
            else ps.setNull(16,Types.INTEGER);
            ps.setTimestamp(17, clickTimeMs > 0 ? new Timestamp(clickTimeMs) : new Timestamp(System.currentTimeMillis()));
            ps.setInt(18,studProbHistId);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);
            return newId;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }



    public int insertLogEntryWorkerLang(int studId, int sessNum, String action, String userInput, boolean isCorrect, long elapsedTime,
                                    long probElapsed, int probId, String hintStep, int hintId, String emotion, String activityName,
                                    int auxId, String auxTable, int curTopicId, long clickTimeMs, int probLangIndex) throws SQLException {
        int studProbHistId = getStudentProblemHistoryEntry(sessNum);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (probId < 1)
                probId = getDummyProbId();
            String q = "insert into " + EventLog + " (studId, sessNum, action, userInput, isCorrect, elapsedTime, probElapsed, problemId, hintStep, " +
                    "hintid, emotion, activityName, auxId, auxTable,time,curTopicId, clickTime, probhistoryid,langIndex) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, studId);
            ps.setInt(2, sessNum);
            ps.setString(3, action);

            if (userInput == null)
                ps.setNull(4,Types.VARCHAR);
            else ps.setString(4,userInput.substring(0,Math.min(userInput.length(),1500))); // column can only store 1500 chars
            ps.setBoolean(5,isCorrect);
            ps.setLong(6,elapsedTime);
            ps.setLong(7,probElapsed);
            ps.setInt(8,probId);
            if (hintStep == null)
                ps.setNull(9,Types.VARCHAR);
            else ps.setString(9,hintStep);
            if (hintId == -1)
                ps.setNull(10,Types.INTEGER);
            else ps.setInt(10,hintId);
            if (emotion == null)
                ps.setNull(11, Types.VARCHAR);
            else ps.setString(11,emotion);
            if (activityName == null)
                ps.setNull(12,Types.VARCHAR);
            else ps.setString(12,activityName);
            if (auxId == -1)
                ps.setNull(13, Types.INTEGER);
            else ps.setInt(13,auxId);
            if (auxTable == null)
                ps.setNull(14,Types.VARCHAR);
            else ps.setString(14,auxTable);
            ps.setTimestamp(15,new Timestamp(System.currentTimeMillis()));
            if (curTopicId > 0)
                ps.setInt(16,curTopicId);
            else ps.setNull(16,Types.INTEGER);
            ps.setTimestamp(17, clickTimeMs > 0 ? new Timestamp(clickTimeMs) : new Timestamp(System.currentTimeMillis()));
            ps.setInt(18,studProbHistId);
            if (probLangIndex == -1)
                ps.setNull(19, Types.INTEGER);
            else ps.setInt(19,probLangIndex);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);
            return newId;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    
    
    
    // Log My Progress Page entries
    public void logMPP (NavigationEvent e) throws Exception {
        insertLogEntry(RequestActions.MPP,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,null,"mpp", getTopic(), e.getClickTime());
    }
    
    // Log Student Access attempts - login, paused, ...
    public void logStudentAccess (int userId, String activityName) throws Exception {
    	try {
    		int sessNum = smgr.getSessionNum();
            insertLogEntryWorker(userId, sessNum, "Student Access", null, false, 0, 0, 0, null, 0, null, activityName, -1, null, 0, 0);
    	}
    	catch(Exception e) {
    		System.out.println("No Session Number");
    	}
    }
    
    public void logHintRequest(IntraProblemEvent e, HintResponse hr) throws Exception {
        insertLogEntry(RequestActions.HINT,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                hr.getHint()!=null ? hr.getHint().getLabel() :  null,hr.getHint()!=null ? hr.getHint().getId() : -1,hr.getCharacterControl(),null,getTopic(), e.getClickTime());
    }



    public void logHintRequestIntervention (IntraProblemEvent e, InterventionResponse r) throws Exception {
        insertLogEntry(RequestActions.HINT,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(),getTopic(), e.getClickTime());
    }


    private void logHelpRequest(IntraProblemEvent e, HintResponse hr, String requestType) throws Exception {
        insertLogEntry(requestType,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                hr.getHint().getLabel(),hr.getHint().getId(),hr.getCharacterControl(),hr.logEventName(), getTopic(), e.getClickTime());
    }


    public void logShowExample(ShowExampleEvent e, ExampleResponse r) throws Exception {
        insertLogEntry(RequestActions.SHOW_EXAMPLE,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),
                e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(),getTopic(), e.getClickTime());
    }

    public void logShowVideoTransaction(ShowVideoEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.SHOW_VIDEO,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),
                e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(),getTopic(), e.getClickTime());
    }

    public int logGazeWanderingEvent(GazeWanderingEvent e, Response r) throws Exception {
    	int newId = 0;
    	newId = insertLogEntry("GazeWandering",null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),
                e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(),getTopic(), e.getClickTime());
        return newId;
    }

    public void logShowSolveProblem(ShowSolveProblemEvent e, HintSequenceResponse hr) throws Exception {
        String hintNamesCSV = hr.getHintNamesCSV();
        List<Hint> hints = hr.getHintSequence();
        int hintId = -1;
        if (hints.size() > 0)
                hintId = hints.get(0).getId();
        // TODO EntryLog hintId column only permits a single integer.  Here we have multiple ids for each hint, so we just store first.
        insertLogEntry(RequestActions.SHOW_SOLVE_PROBLEM,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                hintNamesCSV,hintId,hr.getCharacterControl(),hr.logEventName(),getTopic(), e.getClickTime());
    }

    public void logNextProblem(NextProblemEvent e, ProblemResponse r, int topicId) throws Exception {
        int probId;
        // when it's the first problem in the session, we want the nextProblem event to have no prob id
        // so we put in -1.
        if (smgr.getStudentState().getNumProblemsThisTutorSession() > 1)
            insertLogEntry(RequestActions.NEXT_PROBLEM,smgr.getStudentState().getCurProblem(), null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(), topicId, e.getClickTime());
    }

    public void logNextProblem(NextProblemEvent e, String lcClip, String mode) throws Exception {

        insertLogEntryNextProb(RequestActions.NEXT_PROBLEM,smgr.getStudentState().getCurProblem(), null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                null,-1,lcClip,mode, smgr.getStudentState().getCurTopic(), e.getClickTime(),e.getProbLangIndex());
    }

    public void logNextProblemIntervention (NextProblemEvent e, InterventionResponse r) throws Exception {
        insertLogEntry(RequestActions.NEXT_PROBLEM,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logAttempt(AttemptEvent e, AttemptResponse r) throws Exception {
    	int intprobLangIndex = e.getProbLangIndex();
        String curHint = smgr.getStudentState().getCurHint();
        // Bug 327:  Ivon complains that wrong attempts are graded as incorrect after the problem has been solved.  So now attempts
        // are marked as isCorrect=true once the problem is solved.
        // Bug 328:  Ivon complains that attempts should not have a hintStep logged.  Wants NULL.
        boolean isProbSolved = smgr.getStudentState().isProblemSolved() || r.isCorrect();
        insertLogEntryAttempt(RequestActions.ATTEMPT,e.getUserInput(),isProbSolved,e.getElapsedTime(),e.getProbElapsedTime(),
                null ,-1,r.getCharacterControl(),null, getTopic(), e.getClickTime(), e.getProbLangIndex());
    }


    public void logAttemptIntervention(AttemptEvent e, InterventionResponse r) throws Exception {
        insertLogEntry(RequestActions.ATTEMPT,e.getUserInput(),smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),
                smgr.getStudentState().getCurHint(),smgr.getStudentState().getCurHintId(),r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logBeginProblem(BeginProblemEvent e, Response r) throws Exception {
        insertLogEntryNextProb(RequestActions.BEGIN_PROBLEM,e.getProbId(),null,false,e.getElapsedTime(),0,null,-1,r.getCharacterControl(),e.getMode(), getTopic(), e.getClickTime(), e.getProbLangIndex());
    }

    public void logResumeProblem(ResumeProblemEvent e, Response r) throws Exception {
        insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionNum(), RequestActions.RESUME_PROBLEM, null, false, e.getElapsedTime(), e.getProbElapsedTime(),
                smgr.getStudentState().getCurProblem(), null, -1, r.getCharacterControl(), r.logEventName(), -1, null, getTopic(), e.getClickTime());
    }

    public void logEndProblem(EndProblemEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.END_PROBLEM,smgr.getStudentState().getCurProblem(),null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());


        // If the EndProblem event is coming after a NextProblem event that selected an intervention,
        // we want to use the curProb ID (bc a new problem has not yet been selected); o/w we use the 
        // last prob Id since a new problem Id has been selected
//        if (smgr.getStudentState().getInBtwProbIntervention())
//            insertLogEntryWorker(RequestActions.END_PROBLEM,smgr.getStudentState().getCurProblem(),null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName());
//        else
//            insertLogEntryWorker(RequestActions.END_PROBLEM,smgr.getStudentState().getLastProblem(),null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName());
    }

//    public void logContinue(ContinueEvent e, Response r) throws Exception {
//        insertLogEntry(RequestActions.CONTINUE,null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), clickTimeMs);
//    }
//    public void logContinueNextProblemIntervention (ContinueNextProblemInterventionEvent e, Response r) throws Exception {
//        int probId = smgr.getStudentState().getCurProblem();
//        // if the continue is processed and returns a new problem, we want to log the continue with the LAST probID
//        if (r instanceof ProblemResponse)
//            probId = smgr.getStudentState().getLastProblem();
//        insertLogEntry(RequestActions.CONTINUE,probId,null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic());
//    }
    public void logTimedIntervention (InterventionTimeoutEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.CONTINUE,null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }
    public void logContinueAttemptIntervention(ContinueAttemptInterventionEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.CONTINUE,null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logInputResponse(InputResponseEvent e, Response r) throws Exception {
        AuxilaryEventLogger auxLogger = e.getAuxInfo();
        int probId = smgr.getStudentState().getCurProblem();
        // if the input response is processed and returns a new problem, we want to log the inputResponse with the LAST probID
//        if (r instanceof ProblemResponse)
//            probId = smgr.getStudentState().getLastProblem();

        int auxId= -1;
        String auxTable=null;
        if (auxLogger != null) {
            auxId=auxLogger.logEntry(conn);
            auxTable = auxLogger.getAuxTable();
        }
        String userInput= e.getUserInput();
        insertLogEntry(RequestActions.INPUT_RESPONSE,probId,userInput,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,
                r.getCharacterControl(),r.logEventName(),auxId,auxTable, getTopic(), e.getClickTime());
    }

    public void logInputResponseNextProblemIntervention(InputResponseNextProblemInterventionEvent e, Response r) throws Exception {
        logInputResponse(e,r);
    }
    public void logInputResponseAttemptIntervention(InputResponseAttemptInterventionEvent e, Response r) throws Exception {
        logInputResponse(e,r);
    }

    public void logClickCharacter(ClickCharacterEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.CLICK_CHARACTER,null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logEliminateCharacter(EliminateCharacterEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.ELIMINATE_CHARACTER,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }
    public void logShowCharacter(ShowCharacterEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.SHOW_CHARACTER,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(),getTopic(), e.getClickTime());
    }

    public void logMuteCharacter(MuteCharacterEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.MUTE_CHARACTER,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }
    public void logUnMuteCharacter(UnMuteCharacterEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.UN_MUTE_CHARACTER,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logReadProblem(ReadProblemEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.READ_PROBLEM,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logShowIntervention(BeginInterventionEvent e, Response r, String intervention) throws Exception {
        insertLogEntry(RequestActions.SHOW_INTERVENTION,null, smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),intervention, getTopic(), e.getClickTime());
    }

    public void logEndIntervention(EndInterventionEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.END_INTERVENTION,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logBeginExample(BeginExampleEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.BEGIN_EXAMPLE,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }

    public void logEndExample(EndExampleEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.END_EXAMPLE,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,r.getCharacterControl(),r.logEventName(), getTopic(), e.getClickTime());
    }






    public void logBeginExternalActivity(BeginExternalActivityEvent e, Response r) throws Exception {
        if (!hasOpenBegin(conn, e))
            insertLogEntry(RequestActions.BEGIN_XACT, DbProblem.DUMMY_PROBLEM_ID,null,false,e.getElapsedTime(),0,null,-1,null,r.logEventName(),e.getXactId(),"externalactivity", getTopic(), e.getClickTime());
    }

    /**
     * In the external activity page, the user can go to the My Progress Page and then use the "back" button to return to
     * the external activity page.   Unfortunately, this runs the JQuery document.ready function which then sends BeginExternalActivity
     * which is a duplicate of the one that was sent prior to going to MPP.  So this only logs BeginExternalActivity events if there
     * isn't a similar one already in the log with an associated EndExternalActivity after it.
     * @param conn
     * @param e
     * @return
     */
    private boolean hasOpenBegin(Connection conn, BeginExternalActivityEvent e) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select action from eventlog where (action='EndExternalActivity' or action='BeginExternalActivity') " +
                    "and problemId=? and sessNum=? order by id desc";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,e.getXactId());
            stmt.setInt(2,e.getSessionId());
            rs = stmt.executeQuery();
            // if we hit an End before a begin for the current problem, then things are correct ; o/w we already have an open Begin
            while (rs.next()) {
                String act = rs.getString(1);
                if (act.equals("EndExternalActivity"))
                    return false;
                else return true;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public void logEndExternalActivity(EndExternalActivityEvent e, Response r) throws Exception {
        insertLogEntry(RequestActions.END_XACT,e.getXactId(),null,false,e.getElapsedTime(),e.getProbElapsedTime(),null,-1,null,r.logEventName(), getTopic(), e.getClickTime());

    }

    public void logMPPEvent(MPPTopicEvent e, int probId) throws Exception {
        String action = e.getAction();
        insertLogEntryWorker(smgr.getStudentId(),smgr.getSessionNum(),action,e.getUserInput(),false,e.getElapsedTime(),0,
                probId,null,-1,null,null, -1, null, e.getTopicId(), e.getClickTime());

    }
    
    public void logReportedError(ReportErrorEvent e) throws Exception {
        String action = e.getAction();
        insertLogEntry(action,e.getMessage(),false,e.getElapsedTime(),0,null,-1,null,null,getTopic(), 0);
    }

    public void logShowInstructions(ShowInstructionsEvent e, Response r) throws Exception {
        insertLogEntry("ShowInstructions",null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,null,null,smgr.getStudentState().getCurTopic(), e.getClickTime());
    }

    public void logIntraProblemEvent (IntraProblemEvent e, String action, Response r) throws Exception {
        insertLogEntry(action,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),e.getProbElapsedTime(),null,-1,null,null,smgr.getStudentState().getCurTopic(), e.getClickTime());
    }

    public void logHomeEvent(HomeEvent e) throws Exception {
        insertLogEntry(RequestActions.HOME,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),0,null,-1,null,null,
                -1,null,smgr.getStudentState().getCurTopic(), e.getClickTime());
    }

    public void logChangeLanguageEvent(ChangeLanguageEvent e) throws Exception {
        insertLogEntry(RequestActions.CHANGE_LANGUAGE,null,smgr.getStudentState().isProblemSolved(),e.getElapsedTime(),0,null,-1,null,null,
                -1,null,smgr.getStudentState().getCurTopic(), e.getClickTime());
    }
    public void logDynamicChange(TutorHutEvent e, String change) throws Exception {
        insertLogEntry("DynamicChange", null, false, e.getElapsedTime(), 0, null, -1, null, change, smgr.getStudentState().getCurTopic(), e.getClickTime());
    }
    
	public void logChoosePedagogy(String userInput) throws Exception {
		String LCName = smgr.getLearningCompanion().getCharactersName();
		insertLogEntryWorker(smgr.getStudentId(), smgr.getSessionId(), RequestActions.CHOOSE_PEDAGOGY, userInput, false, 0, 0, -1, null, -1,
				null, LCName, -1, null, 0, 0);
	}
}
