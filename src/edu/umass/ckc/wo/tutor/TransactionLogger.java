package edu.umass.ckc.wo.tutor;


import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.enumx.Actions;
import edu.umass.ckc.wo.enumx.StudentInputEnum;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.event.tutorhut.ClickCharacterEvent;
import edu.umass.ckc.wo.event.tutorhut.EliminateCharacterEvent;
import edu.umass.ckc.wo.event.tutorhut.MuteCharacterEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.ClickCharacterResponse;
import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.util.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class TransactionLogger {

    private static String getAction(String userInput, int lastProbId) {
        char choice = userInput.toUpperCase().trim().charAt(0);

//    if (userInput.equalsIgnoreCase(StudentInputEnum.nextProblem) && lastProbId == -1)
//      return Actions.beginProblem;
//    if (userInput.equalsIgnoreCase(StudentInputEnum.nextProblem) && lastProbId != -1)
//      return Actions.endProblem;
        if (userInput.equalsIgnoreCase(StudentInputEnum.nextProblem))
            return Actions.beginProblem;
        else if (userInput.equalsIgnoreCase(StudentInputEnum.help) ||
                userInput.equalsIgnoreCase(StudentInputEnum.forceHelp))
            return Actions.hint;
        else if ( userInput.equalsIgnoreCase(StudentInputEnum.helpAccepted))
            return Actions.hintAccepted ;
        else if ( userInput.equalsIgnoreCase(StudentInputEnum.helpRejected))
            return Actions.hintRejected ;
        else // we have to consider it an attempt because its nothing else
            return Actions.attempt;

    }


    public static void logInterventionEngagement(Connection conn,  int studId, int sessNum, long elapsedTime,
                                                 int probsSinceLastInt, String engagement, double ability,
                                                 double informReward, double studControlReward, String interventionName,
                                                 int informBeforeValue, int informNowValue) throws Exception {
        SqlQuery q = new SqlQuery();
        String s = "insert into interventionengagementdata (studId,sessNum,elapsedTime,probsSinceLastInt," +
                "engagement,ability,informReward,studControlReward,interventionName,informBeforeValue,"+
                " informNowValue) values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setInt(3,(int)elapsedTime);

        if (probsSinceLastInt != -1)
            ps.setInt(4, probsSinceLastInt);
        else
           ps.setNull(4, Types.INTEGER);
        if (engagement != null)
            ps.setString(5, engagement);
        else
           ps.setNull(5, Types.VARCHAR);
        if (ability != -1)
            ps.setDouble(6, ability);
        else
            ps.setNull(6, Types.DOUBLE);
        if (informReward != -1)
            ps.setDouble(7, informReward);
        else
            ps.setNull(7, Types.DOUBLE);
        if (studControlReward != -1)
            ps.setDouble(8, studControlReward);
        else
            ps.setNull(8, Types.DOUBLE);
        if (interventionName != null)
            ps.setString(9, interventionName);
        else
           ps.setNull(9, Types.VARCHAR);
        if (informBeforeValue != -1)
            ps.setInt(10, informBeforeValue);
        else
           ps.setNull(10, Types.INTEGER);
        if (informNowValue != -1)
            ps.setInt(11, informNowValue);
        else
           ps.setNull(11, Types.INTEGER);


        try {
            ps.execute();
        } catch (Exception ee) {
            System.out.println(ee);
            throw ee;
        }


    }

    /**
     * This will log an event in the EpisodicData table each time a user event comes in.
     * Nota Bene: It is important to realize that this function gets called after the current Problem has been
     * changed to the next problem.
     */
    public static void logEvent(StudentActionEvent e, SessionManager smgr, Activity a) throws Exception {
        String userInput = e.getUserInput();
        long elapsedTime = e.getElapsedTime();
        long probElapsedTime = e.getProbElapsed();
        boolean isCorrect = e.isCorrect();   // for sat this only has meaning on an attempt
        boolean isSolved = e.isSolved();   // for sat this tells whether the student has answered this problem correctly
        int studId = smgr.getStudentId();
        int probId;
        StudentState sst = smgr.getStudentState();
        int hintId = sst.getCurHintId();
        String hintStep = null;
        int sessionNum = smgr.getSessionNum();
        String action = getAction(userInput, e.getProbId());
        // suppress hint info if the action is not beginHint
        if (!action.equals(Actions.hint)) {
            hintId = -1;
            hintStep = null;
        } else if ((action.equals(Actions.hint) || action.equals(Actions.hintAccepted)) && hintId != -1) {
            hintStep = getHintStep(smgr.getConnection(), hintId);
        }
        // this is the first problem in sequence for the session
        if (action.equalsIgnoreCase(Actions.beginProblem)) {
            userInput = null;
            probId = smgr.getStudentState().getCurProblem(); // so use the id of the problem that the problem selector just picked
        } else
            probId = e.getProbId();   // otherwise the problemId was passed in as part of the event

        Connection conn = smgr.getConnection();
        // hack if a is null it was a special call to this logger from a hint selector that chooses a bunch of hints
        // and logs each one and passing null as the activity (since Hint is not an activity), so we special case null
        String activityname = new String();



        if ( action.equals(Actions.hint) )
          activityname = "hint" ;
        else if ( action.equals(Actions.hintAccepted) ) {
          activityname = "helpAccepted" ;
            Hint h = ((HintResponse) a).getHint();
            if (h != null) {
                hintId = h.getId();
                hintStep = h.getLabel();
            }
        }
        else if ( action.equals(Actions.hintRejected) )
          activityname = "helpRejected" ;
        else if (action.equals(Actions.attempt))
            activityname = "attempt";
        // if we are returning a new activity which is an intervention then we want to make sure the ED2 has
        //  an activityName which is the name and id of the intervention.  This needs to be after handling of ATTEMPT
        // so that attempts with interventions do not get logged as interventions - This is because the class hierarchy of
        // motivational responses (graphs) masquerading as problems was fucked up but I do not want to untangle it now.
        else if (a instanceof InterventionResponse ) {
            activityname = ((InterventionResponse) a).getIntervention().logEventName();
            probId =   ((InterventionResponse) a).getIntervention().getId();
        }
        else if ( a != null )
          activityname = a.logEventName() ;
        // This is because the intervention that asks student if they want harder/easier problem
        // must log the students response to the question.   It was incorrectly logging it as an attempt.
        if (e.getStudentInput() == StudentInputEnum.EASIER_NEXT_PROBLEM ||
                e.getStudentInput() == StudentInputEnum.HARDER_NEXT_PROBLEM ||
                e.getStudentInput() == StudentInputEnum.SAME_NEXT_PROBLEM) {
            action="studDifficultyChoice";
            activityname="studControlInterventionResponse";
        }

        if (e.getStudentInput() == StudentInputEnum.NEXT_PROB_WITH_INFO){
            action = "studentFeedback";
            activityname = "emotionFeedbackInterventionResponse";
        }
        
        logStudentAction(conn, userInput, elapsedTime, isCorrect, studId, probId,
                hintStep, sessionNum, hintId, action, probElapsedTime, activityname);
//    if (action.equalsIgnoreCase(Actions.endProblem)) {
//      insertEpisodicData(conn, null, elapsedTime, false, studId, probId,
//                       null, sessionNum, -1, Actions.beginProblem, 0);
//    }

    }

    public static void logEvent(EndActivityEvent e, SessionManager smgr, String probType) throws Exception {

        long elapsedTime = e.getElapsedTime();
        long probElapsedTime = e.getProbElapsed();
        int studId = smgr.getStudentId();
        int hintId = smgr.getStudentState().getCurHintId();
        String hintStep = null;
        int sessionNum = smgr.getSessionNum();
        String action = Actions.endProblem;
        String userInput = e.getUserInput();
        boolean isCorrect = e.isCorrect();
        boolean isSolved = e.isSolved();
        Connection conn = smgr.getConnection();

        boolean problemSolvedCorrectly = false;
        // if we are in the sat hut we want to log whether the problem was solved correctly.  This is passed in the
        // isSolved variable.  If we are in pre/post hut the isCorrect contains this information
        // note:  This is assuming adventures behave like pre/post test.

        StudentState sst = smgr.getStudentState();
        String curLoc = sst.getCurLocation();
        // for sat and adventures the isSolved variable says whether the problem was solved correctly (not sure if
        // this is really true of adventures - get toby to verify)
        if (curLoc.equals(NavigationEvent.SAT_HUT) || curLoc.equals(NavigationEvent.A1) || curLoc.equals(NavigationEvent.A2)
            || curLoc.equals(NavigationEvent.A3))
           problemSolvedCorrectly = isSolved;
        // in pre/post isCorrect tells whether student got problem right
        else problemSolvedCorrectly = isCorrect;


        // if this action is from the sat hut then
        logStudentAction(conn,userInput, elapsedTime, problemSolvedCorrectly, studId, e.getProbId(),
                hintStep, sessionNum, hintId, action, probElapsedTime, probType );

    }

    private static String getHintStep(Connection conn, int hintId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select name from Hint where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, hintId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            rs.close();
            ps.close();
        }
        return null;
    }


    private static void logStudentAction(Connection conn, String userInput, long elapsedTime, boolean isCorrect, int studId,
                                         int probId, String hintStep, int sessionNum, int hintId,
                                         String action, long probElapsedTime, String activityLogName) throws Exception {
        String s = "insert into EpisodicData2 (studId,sessNum,userInput,isCorrect" +
                ",elapsedTime,problemId,hintStep,hintId,action,probElapsed, activityName) values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessionNum);

        System.out.println("DEBUG:\t\tIn TrasactionLogger.logStudentAction: userInput-" + userInput);
        if (userInput == null)
            ps.setNull(3, Types.VARCHAR);
        else
            ps.setString(3, userInput);
        ps.setInt(4, isCorrect ? 1 : 0);
        ps.setInt(5, (int) elapsedTime);
        if (probId != -1)
            ps.setInt(6, probId);
        else
            ps.setNull(6, Types.INTEGER);
        if (hintStep == null)
            ps.setNull(7, Types.VARCHAR);
        else
            ps.setString(7, hintStep);
        if (hintId == -1)
            ps.setNull(8, Types.INTEGER);
        else
            ps.setInt(8, hintId);
        ps.setString(9, action);
        ps.setLong(10, probElapsedTime);
        ps.setString(11, activityLogName);
        try {
            ps.execute();
        } catch (Exception ee) {
            System.out.println(ee);
            throw ee;
        }
    }

    public static void logClickCharacter(Connection conn, ClickCharacterEvent event, ClickCharacterResponse r,
                                          int probId, int studId, int sessNum) throws SQLException {

        String s = "insert into EpisodicData2 (studId,sessNum" +
                ",elapsedTime,problemId,hintStep,hintid,action,probElapsed, activityName) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setLong(3, event.getElapsedTime());
        ps.setInt(4,probId);
        ps.setString(5,r.getMovieClip());
        ps.setInt(6,r.getClipId());
        ps.setString(7,"ClickCharacter");
        ps.setLong(8,event.getProbElapsedTime());
        ps.setString(9,"satProblem");
        ps.executeUpdate();


    }

    // for backward compat with old event system
    public static void logClickCharacter(Connection conn, ClickCharacterEvent event, HintResponse hr,
                                          int studId, int sessNum) throws SQLException {

        String s = "insert into EpisodicData2 (studId,sessNum" +
                ",elapsedTime,problemId,hintStep,hintid,action,probElapsed, activityName) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setLong(3, event.getElapsedTime());
        ps.setInt(4,hr.getHint().getProblemId());
        ps.setString(5,hr.getHint().getLabel());
        ps.setInt(6,hr.getHint().getId());
        ps.setString(7,"ClickCharacter");
        ps.setLong(8,event.getProbElapsedTime());
        ps.setString(9,"satProblem");
        ps.executeUpdate();
        

    }

    public static void logSimpleActionEvent(Connection conn, long elapsedTime, long probElapsedTime,
                                             int studId, int sessNum, int probId, String action) throws SQLException {
        String s = "insert into EpisodicData2 (studId,sessNum" +
                ",elapsedTime,problemId,action,probElapsed, activityName) values (?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setLong(3, elapsedTime);
        ps.setInt(4, probId);
        ps.setString(5,action);
        ps.setLong(6,probElapsedTime);
        ps.setString(7,"satProblem");
        ps.executeUpdate();
    }












    public static void logEliminateCharacter(Connection conn, EliminateCharacterEvent event,
                                             int studId, int sessNum, int probId) throws SQLException {
        logSimpleActionEvent(conn,event.getElapsedTime(), event.getProbElapsedTime(),studId,sessNum,
                probId,"EliminateCharacter");
    }

    public static void logMuteCharacter(Connection conn, MuteCharacterEvent event,
                                        int studId, int sessNum, int probId) throws SQLException {
        logSimpleActionEvent(conn,event.getElapsedTime(), event.getProbElapsedTime(),studId,sessNum,
                probId,"MuteCharacter");
    }
}