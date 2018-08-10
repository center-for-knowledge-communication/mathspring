package edu.umass.ckc.wo.servertest;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.event.NavigationEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.handler.StudentActionHandler;
import edu.umass.ckc.wo.handler.NavigationHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Walks the EpisodicData table for a given id (a baseline test user) and generates calls to the server from these
 * log entries
 * <p/>
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Mar 22, 2006
 * Time: 3:41:50 PM
 */
public class EpisodicLogDriver {

    private Connection conn;
    private int baselineId;
    private int sessionId;
    private List logEntries;
    private int logIndex=0;
    private String lastActivityName="";



    public EpisodicLogDriver() {
    }

    public void init(Connection conn, int baselineId, int sessionId) throws SQLException {
        this.conn = conn;
        this.baselineId = baselineId;
        this.sessionId = sessionId;
        loadLog(baselineId);
    }

    /** This is an interpreter for the EpisodicData table.  For a given users log entries, it will
     * call the server (in a new session) exactly the same way as the log entries recorded it.
     */
    public void interpretEpisodicLog () {

        EpiLogEntry e =null;
        try {
            while ((e = getNextLogEntry()) != null) {
                interpretLogEntry(e);
            }
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }




    /**
     * If the log entry is a switch from one test to another (in the activityName) then create
     * a navigationEvent; otherwise return null.
     *
     * Note that we do not generate nav events when work is completed in a hut and user goes back to village.  We just
     * generate nav events from the village to each of the subsequent huts.
     * @param e
     * @return
     */
    private NavigationEvent getImplicitNavEvent (EpiLogEntry e) {

        if (e.getActivityName().equals(lastActivityName))
            return null;
        else if (e.getActivityName().equals("pretestProblem") && lastActivityName.equals("")) {
            lastActivityName = "pretestProblem";
            return new NavigationEvent(sessionId,NavigationEvent.VILLAGE,NavigationEvent.PREPOST_HUT, Long.toString(e.getElapsedTime()), null);
        }
        else if (e.getActivityName().equals("satProblem") && lastActivityName.equals("pretestProblem")) {
            lastActivityName = "satProblem";
            return new NavigationEvent(sessionId,NavigationEvent.PREPOST_HUT,NavigationEvent.SAT_HUT, Long.toString(e.getElapsedTime()), null);
        }
        else if (e.getActivityName().equals("posttestProblem") && lastActivityName.equals("satProblem")) {
            lastActivityName = "posttestProblem";
            return new NavigationEvent(sessionId,NavigationEvent.VILLAGE,NavigationEvent.PREPOST_HUT, Long.toString(e.getElapsedTime()), null);
        }

        //We may never see this case.  It means the student just did a pretest and then a posttest with no SAT problems.
        else if (e.getActivityName().equals("posttestProblem") && lastActivityName.equals("pretestProblem")) {
            lastActivityName = "posttestProblem";
            return new NavigationEvent(sessionId,NavigationEvent.VILLAGE,NavigationEvent.PREPOST_HUT, Long.toString(e.getElapsedTime()),null);
        }
        else if (lastActivityName.equals("posttestProblem") && !e.getActivityName().equals("posttestProblem"))
            return new NavigationEvent(sessionId,NavigationEvent.POST,NavigationEvent.VILLAGE, Long.toString(e.getElapsedTime()),null);
        // we will get here when the activity is something like attempt.  But that doesn't represent a navigation.
        return null;
    }

    /**
     * Take apart the log entry and make a call to the server's handler class that simulates the same
     * user event that took place in the log entry.  Do this in a different session.
     * @param e
     * @return View object containing the String the server would return for this input
     */
    public View interpretLogEntry (EpiLogEntry e) throws Exception {
        // TODO passing an empty servlet path - this may cause problems
        SessionManager smgr = new SessionManager(this.conn,this.sessionId).buildExistingSession();
        NavigationEvent ne = getImplicitNavEvent(e);
        System.out.println(e);
        // first we need to tell the server of navigation events that may be implicit in the log entry.
        if (ne != null) {
            View v = new NavigationHandler(null, smgr, conn, null, null).handleRequest(ne);
            // if navigating from pre to SAT then mark pretest as complete
            if (ne.getFrom().equals(NavigationEvent.PREPOST_HUT) && ne.getTo().equals(NavigationEvent.SAT_HUT))
                smgr.getStudentState().setPretestCompleted(true);
            // if navigating from post to anything mark posttest complete.
            else if (ne.getFrom().equals(NavigationEvent.POST))
                smgr.getStudentState().setPosttestCompleted(true);
            System.out.println("Navigation: " + ne.getTo());
        }
        if (e.isStudentActionEvent()) {
            StudentActionEvent ae = new StudentActionEvent(sessionId,e.getUserInput(),e.getElapsedTime(),e.isCorrect(),e.getProblemId(),e.getProbElapsedTime(),
                    false,e.getHintId(), e.getAction()); //have to set isSolved=false (it aint in the EpisodicData)
            System.out.println("StudentAction: " + e.getUserInput());
            return new StudentActionHandler(smgr,conn).handleRequest(ae);
        }
        else if (e.isEndActivityEvent()) {
            EndActivityEvent ae = new EndActivityEvent(sessionId,e.getElapsedTime(),e.getProbElapsedTime(),e.getProblemId(),false,e.getUserInput(),e.isCorrect());
             System.out.println("EndActivity: " +  e.getUserInput());
            return new StudentActionHandler(smgr,conn).handleRequest(ae);
        }
        return null;
    }

    private EpiLogEntry getNextLogEntry () {
        if (logIndex < logEntries.size())
            return (EpiLogEntry) logEntries.get(logIndex++);
        else return null;
    }

    private void loadLog (int studId) throws SQLException {
        logEntries = new ArrayList();
        String q = "select action, isCorrect, userInput,elapsedTime,probElapsed,activityName,problemId,hintId,hintStep from episodicdata2 where studId=? order by sessNum, elapsedTime";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,studId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String action = rs.getString(1);
            int corr = rs.getInt(2);
            String ui = rs.getString(3);
            boolean isCorrect = corr == 1;
            long elapsedTime = rs.getLong(4);
            long probElapsed = rs.getLong(5);
            String actName = rs.getString(6);
            int probId = rs.getInt(7);
            int hintId = rs.getInt(8);
            String hintStep = rs.getString(9);
            EpiLogEntry e = new EpiLogEntry(action,isCorrect,ui,elapsedTime,probElapsed,actName,probId,hintId,hintStep);
            logEntries.add(e);
        }
    }
}
