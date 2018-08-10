package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;

import java.sql.SQLException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 24, 2005
 * Time: 6:20:48 PM
 */
public interface TutorEventHandler {
    /**
     * A tutor calls this method when the user has clicked on the "new problem" button.   
     * @param state
     * @param p
     * @throws SQLException
     */
    public abstract void newProblem (StudentState state, Problem p) throws SQLException;

    public abstract void beginProblem(SessionManager smgr, BeginProblemEvent e) throws SQLException;
//    public abstract void endProblem (StudentState state) throws SQLException;
    public abstract void endProblem (SessionManager smgr, int studId, long probElapsedTime, long elapsedTime) throws Exception;

    public abstract void hintGiven (StudentState state, Hint hint) throws SQLException ;

    public abstract void videoGiven (StudentState state) throws SQLException ;
    public abstract void exampleGiven(StudentState state, int exampleId) throws SQLException ;

    public abstract void studentAttempt (StudentState state, String answer, boolean isCorrect, long probElapsed) throws SQLException ;

    public abstract void interventionGiven (StudentState state, Intervention intervention) throws SQLException ;
}
