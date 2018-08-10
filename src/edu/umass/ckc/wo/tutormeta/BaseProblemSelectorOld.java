package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Sep 19, 2005
 * Time: 10:29:31 AM
 */
public abstract class BaseProblemSelectorOld implements ProblemSelector {

    protected static Random indexGenerator_ = new Random();
    protected DbProblem dbProblemMgr= new DbProblem();  // an object which handles db queries about Problems
    protected int classID;
    protected Problem lastProb;
    protected String lastProbMode;
    protected Connection conn;
    protected int lastProbIndex;
    protected SessionManager smgr;

    protected int topicID;
    protected List<Problem> probsInTopic;

    public void setParameters(PedagogicalModelParameters params) {

    }



    public List<Problem> getProblemsInTopic (Connection conn, int probGroupID) throws SQLException {
//       return dbProblemMgr.getProblemsInTopic(conn,probGroupID);
        return ProblemMgr.getTopicProblems(probGroupID);
    }




    protected boolean inList(List<String> ids, int id) {
        for (String s : ids) {
            if (s.equals(Integer.toString(id)))
                return true;
        }
        return false;
    }







    /**
     * Load the problem from the database.  Forward the request to the databse problem manager.
     * @param conn
     * @param id
     * @return Problem object or null if nothing found
     * @throws SQLException
     */
    protected Problem buildProblem(Connection conn, int id) throws SQLException {
        return dbProblemMgr.getProblem(conn,id);
    }



    /** Return all the ready problems in the database . Orders them by id.
     * Forwards the request to the database problem magager.
     * @param smgr
     * @return  a List of Problem objects
     * @throws Exception
     */
    protected List<Problem> getAllProblems (SessionManager smgr) throws Exception {
        return ProblemMgr.getAllProblems();
//        return dbProblemMgr.getAllProblems(smgr.getConnection());
    }


    /**
     * An abstract implementation which forces subclasses to implement this interface method
     * @param smgr
//     * @param studentModel
     * @param e
     * @return
     * @throws Exception
     */
//    public abstract Problem selectProblem(SessionManager smgr, StudentModel studentModel, NextProblemEvent e) throws Exception;






//    /**
//     * An abstract implementation which forces subclasses to implement this interface method
//     * @param smgr
//     * @throws Exception
//     */
//    public abstract void setServletInfo(SessionManager smgr) throws Exception;

    // TODO These should be abstract methods to force subclasses to implement these three methods.  Instead
    // we have implementations here so that subclasses will compile.   They will break at runtime if they don't
    // have overrides for these methods.
//    public Problem selectProblemLessDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        throw new DeveloperException("This problem selector does not know how to select a less difficult problem");
//    }
//    public Problem selectProblemSameDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        throw new DeveloperException("This problem selector does not know how to select a same difficulty problem");
//    }
//    public Problem selectProblemMoreDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        throw new DeveloperException("This problem selector does not know how to select a more difficult problem");
//    }




}
