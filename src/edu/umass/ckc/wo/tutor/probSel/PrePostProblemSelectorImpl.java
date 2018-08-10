package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.content.PrePostProblem;
import edu.umass.ckc.wo.content.PrePostProblemDefn;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.PrePostProblemSelector;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.wo.db.DbPrePost;
import edu.umass.ckc.wo.beans.PretestPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 16, 2005
 * Time: 6:12:34 PM
 */
public class PrePostProblemSelectorImpl implements PrePostProblemSelector {
    protected Connection conn;
    Random ran = new Random();
    SessionManager smgr;

    public PrePostProblemSelectorImpl() {
    }

   /**
     * @param conn
     * @param testId
     * @return a Vector of PrePostTestProblem objects
     * @throws SQLException
     */
    public static Vector getPrePostProblems(Connection conn, int testId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector result = new Vector();
        try {
            // DM 3/09 Get problems from the map joined with the prePostProblem.   This is because
            // problems now can be in more than one test.
            String q = "select id,name,url,description,answer,ansType,aChoice,bChoice,cChoice,dChoice,eChoice," +
                    "aURL,bURL,cURL,dURL,eURL,waitTimeSecs,imageFilename from PrePostProblem p, PrepostProblemTestMap m where m.testId=? and p.id=m.probId";
            ps = conn.prepareStatement(q);
            ps.setInt(1, testId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String url = rs.getString(3);
                if (rs.wasNull())
                    url = null;
                String description = rs.getString(4);
                if (rs.wasNull())
                    description = null;
                String answer = rs.getString(5);
                int ansType = rs.getInt(6);
                String aURL = null, bURL = null, cURL = null, dURL = null, eURL = null;
                String aChoice = null, bChoice = null, cChoice = null, dChoice = null, eChoice = null;

                PrePostProblemDefn p;
                int waitTimeSecs = 0;
                waitTimeSecs = rs.getInt(17);
                if (rs.wasNull())
                    waitTimeSecs = 0;
                String imgFilename = rs.getString(18);
                if (ansType == PrePostProblemDefn.SHORT_ANSWER) {
                    ;
                } else {
                    aChoice = rs.getString(7);
                    if (rs.wasNull())
                        aChoice = null;
                    bChoice = rs.getString(8);
                    if (rs.wasNull())
                        bChoice = null;
                    cChoice = rs.getString(9);
                    if (rs.wasNull())
                        cChoice = null;
                    dChoice = rs.getString(10);
                    if (rs.wasNull())
                        dChoice = null;
                    eChoice = rs.getString(11);
                    if (rs.wasNull())
                        eChoice = null;
                    aURL = rs.getString(12);
                    if (rs.wasNull())
                        aURL = null;
                    bURL = rs.getString(13);
                    if (rs.wasNull())
                        bURL = null;
                    cURL = rs.getString(14);
                    if (rs.wasNull())
                        cURL = null;
                    dURL = rs.getString(15);
                    if (rs.wasNull())
                        dURL = null;
                    eURL = rs.getString(16);
                    if (rs.wasNull())
                        eURL = null;

                }
                result.add(new PrePostProblemDefn(id, name, description, url, ansType, answer, testId, aChoice, bChoice, cChoice,
                        dChoice, eChoice, aURL, bURL, cURL, dURL, eURL, waitTimeSecs, imgFilename));
            }
            return result;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
            ps.close();
        }
    }

    public boolean isTestingComplete(SessionManager smgr, StudentModel studentModel) throws SQLException {
        if (smgr.getStudentState().getPretestCompleted() &&
                smgr.getStudentState().getPosttestCompleted())
            return true;
        else if (!smgr.getStudentState().getPosttestCompleted()) {
            int problemSet = smgr.getStudentState().getPosttestProblemSet();
            if (problemSet == -1) {
                // get the pre test problem set
                problemSet = smgr.getStudentState().getPretestProblemSet();
                // select a post test that isn't what was used for the pretest
                problemSet = selectProblemSet(problemSet);
                // error situation: we've given a pre-test but there are no other tests.  Therefore we say TESTING_COMPLETE
                if (problemSet == -1)
                    return true;
            }
            return false;
        } else
            return false;
    }
//    public Problem selectProblem(ActionEvent e, SessionManager smgr, StudentModel studentModel) throws Exception {
//        return getRandomProblem();
//    }

    public Problem selectProblem(ActionEvent e, SessionManager smgr, StudentModel studentModel) throws Exception {
        return null;
    }

    public Problem selectProblemX(ActionEvent e, SessionManager smgr, StudentModel studentModel) throws Exception {
        return null;
//        int pretestNumCorrect = ((BaseStudentModelOld) studentModel).getPretestNumCorrect();
//        int posttestNumCorrect = ((BaseStudentModelOld) studentModel).getPosttestNumCorrect();
//        List preTestProblemsGiven = smgr.getStudentState().getPretestProblemsGiven();
//        // give a pretest problem
//        if (!smgr.getStudentState().getPretestCompleted()) {
//            int problemSet = smgr.getStudentState().getPretestProblemSet();
//            if (problemSet == -1) {
//                // get the problem set for the pretest (note: this selector may be overriden)
////                problemSet = selectProblemSet();
////                problemSet = selectRandomProblemSetFromPool();
//                // dm 2/16/11 - pretests used to be selected randomly from pool (as in line above)
//                // We've switched so that balance is maintained between the two choices.
//                // The table pretestassignment is where <class,student,pretest>  is maintained.
//                problemSet = selectPretestFromPool(); // get a pretest maintaining balance
//                smgr.getStudentState().setPretestProblemSet(problemSet);
//            }
//
//            //if user has selected quit, send end of test response
//            if (e instanceof StudentActionEvent){
//                String input = ((StudentActionEvent)e).getUserInput();
//                if (input != null && input.equals(StudentInputEnum.quitTestHut)){
//                    //todo: Make sure last question isn't counted against student (it is now and hence the -1)
//                    return new PrePostProblem(PrePostProblem.END_OF_TEST, pretestNumCorrect, preTestProblemsGiven.size()-1);
//                }
//            }
//
//            PrePostProblem p = (PrePostProblem) getNextProblem(problemSet, preTestProblemsGiven, pretestNumCorrect);
//            p.setLogEventName(edu.umass.ckc.wo.content.PrePostProblem.PRE);
//            if ( p.isEndOfTest())
//                this.pretestComplete();
//            // BUG FIX.   No longer want to save the problem in the list of problems Given at this time.
//            // Instead we wait until the user responds to it and then add it.   Bug was that Problems were getting
//            // generated and user logged out, navigated out of the test hut, or walked away from the app and
//            // the problem was listed as given but we never got a response to it.  So we have to return it to
//            // the list of available problems
////            else
////                this.pretestProblemSelected(p);
//            // instead we just do this
//            smgr.getStudentState().setCurPreProblem(p.getId());
//
//            return p;
//        // give a posttest problem
//        } else if (!smgr.getStudentState().getPosttestCompleted()) {
//             List postTestProblemsGiven = smgr.getStudentState().getPosttestProblemsGiven();
//            int problemSet = smgr.getStudentState().getPosttestProblemSet();
//
//            if (problemSet == -1) {
//                // first get the pretest problem set that the student completed
//                problemSet = smgr.getStudentState().getPretestProblemSet();
//                // select a posttest from the class's pool that is different from the pretest given (note: this may be overridden)
//                problemSet = selectRandomProblemSetFromPool(problemSet);
//                // error situation: we've given a pre-test but there are no other tests.  Therefore we say TESTING_COMPLETE
//                if (problemSet == -1)
//                    return new PrePostProblem(PrePostProblem.TESTING_COMPLETE,0,0);
//                smgr.getStudentState().setPosttestProblemSet(problemSet);
//            }
//
//            //if user has selected quit, send end of test response
//            if (e instanceof StudentActionEvent){
//                String input = ((StudentActionEvent)e).getUserInput();
//                if (input != null && input.equals(StudentInputEnum.quitTestHut)){
//                    //todo: Make sure last question isn't counted against student (it is now and hence the -1)
//                    return new PrePostProblem(PrePostProblem.END_OF_TEST, posttestNumCorrect, postTestProblemsGiven.size()-1);
//                }
//            }
//
//            PrePostProblem p = (PrePostProblem) getNextProblem(problemSet, postTestProblemsGiven,  posttestNumCorrect);
//            p.setLogEventName(edu.umass.ckc.wo.content.PrePostProblem.POST);
//            if (p.isEndOfTest())
//                this.posttestComplete();
////            else
////                this.posttestProblemSelected(p);
//            // same bug fix as applied above in pretest
//            smgr.getStudentState().setCurPostProblem(p.getId());
//            return p;
//        }
//        // when both pre and post tests are complete return probXML=TESTING_COMPLETE and score for posttest
//        else  {
//            int problemSet = smgr.getStudentState().getPosttestProblemSet();
//            return new PrePostProblem(PrePostProblem.TESTING_COMPLETE, posttestNumCorrect,getTestSize(problemSet));
//        }

    }

    /**
     *   select the post test problem set given the pretest problem set.  The problem set is chosen randomly
     * from the tests that were not given as the pretest.  Override this method in a subclass to achieve
     * other than random results.
     * @param problemSet
     * @return
     * @throws SQLException
     */
    protected int selectProblemSet(int problemSet) throws SQLException {
        List<Integer> ids = getPrePostProblemSets();
        Iterator itr = ids.iterator();
        while (itr.hasNext()) {
            Integer ps = (Integer) itr.next();
            if (ps == problemSet) {
                itr.remove();
                break;
            }
        }
        if (ids.size() == 0)
            return -1;
        int ix = new Random().nextInt(ids.size());
        return ((Integer) ids.get(ix)).intValue();
    }

    // This seems to be based on the concept that a pool can contain more than two tests.  In
       // practice I don't think they ever do so that this is unecessary.   We strictly need
       // to select the ungiven test.
       protected int getOtherTestInPool (int problemSet) throws SQLException {
           List<Integer> ids = getClassPoolTestIds();
           Iterator itr = ids.iterator();
           while (itr.hasNext()) {
               Integer ps = (Integer) itr.next();
               if (ps == problemSet) {
                   itr.remove();
                   break;
               }
           }
           if (ids.size() == 0)
               return -1;
           return ((Integer) ids.get(0)).intValue();
       }

    // This seems to be based on the concept that a pool can contain more than two tests.  In
    // practice I don't think they ever do so that this is unecessary.   We strictly need
    // to select the ungiven test.
    protected int selectRandomProblemSetFromPool (int problemSet) throws SQLException {
        List<Integer> ids = getClassPoolTestIds();
        Iterator itr = ids.iterator();
        while (itr.hasNext()) {
            Integer ps = (Integer) itr.next();
            if (ps == problemSet) {
                itr.remove();
                break;
            }
        }
        if (ids.size() == 0)
            return -1;
        int ix = new Random().nextInt(ids.size());
        return ((Integer) ids.get(ix)).intValue();
    }

    private int getTestSize (int problemSet) throws SQLException {
        // DM 3/09 since problems can now exist in multiple tests we get it from the map
//        String q = "select count(*) from PrePostProblem where problemSet=?";
        String q = "select count(*) from PrePostProblemTestMap where testId=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, problemSet);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else return 0;
    }

    /**
     * Returns the next PrePostProblem in the given problem set.  The next problem is a random problem from the remainder
     * of problems in the problem set that haven't yet been given.  If all have been given, return null.
     *
     * @param problemSet
     * @param problemsGiven
     * @return
     * @throws SQLException
     */
    private PrePostProblemDefn getNextProblem(int problemSet, List problemsGiven,
                                          int numCorrect) throws SQLException {
        // ordering by id is done here so that subclasses can depend on the list of unseen problems to be presented
        // in a predictable order.  This class doesn't care what kind of order things are in since it chooses randomly
        // from them.

        // DM 3/09   A map (PrePostProblemTestMap) now allows a problem to be put in multiple
        // problem sets so we find all problems that are from the selected problem set in the map
        //String q = "select p.id from PrePostProblem p where problemSet=? order by p.id";

        String q = "select probId from PrePostProblemTestMap m where m.testId =? order by probId";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, problemSet);
        ResultSet rs = ps.executeQuery();
        // Cycle through each PrePostProblem and if it hasn't been given, add it to the list of unseen problems
        ArrayList unseenProbs = new ArrayList();
        int count = 0;
        while (rs.next()) {
            int id = rs.getInt(1);
            count++;
            String idstr = Integer.toString(id);
            if (problemsGiven.indexOf(idstr) == -1)
                unseenProbs.add(idstr);
        }
        // call the method (may be overridden) which will take the next unseen problem
        return selectUnseenProblem(unseenProbs, count, numCorrect);
    }

    /**
     * Select the next problem from the given list of unseen problems.  This will choose randomly.
     * Override this method in a subclass if you wish to select from the unseen problems in a way that is
     * not random.
     * @param unseenProbs
     * @param count
     * @param numCorrect
     * @return
     * @throws SQLException
     */
    protected PrePostProblemDefn selectUnseenProblem(ArrayList unseenProbs, int count, int numCorrect) throws SQLException {
        // now select randomly from the unseen problems.
        if (unseenProbs.size() > 0) {
            int randomIx = ran.nextInt(unseenProbs.size());
            PrePostProblemDefn ppp = DbPrePost.getPrePostProblem(conn, Integer.parseInt((String) unseenProbs.get(randomIx)));
            // return a new pp and include the numProbsInTest and numProbsSeen
            return new PrePostProblem(ppp,count,count-unseenProbs.size());
        } else  {
            // special PPProblem marked EOT with numCorrect/numProblemsInTest
            return new PrePostProblem(PrePostProblem.END_OF_TEST,numCorrect,count);
        }
    }

    /**
     * Select the problem set for the pretest. It is selected randomly from all the pre/post tests.
     * Override this method in a subclass if you wish for different behavior than random selection from the
     * set of tests
     * @return
     * @throws SQLException
     */
    protected int selectProblemSet() throws SQLException {
        List<Integer> ids = getPrePostProblemSets();
        int ix = new Random().nextInt(ids.size());
        return  ids.get(ix);
    }

    protected int selectRandomProblemSetFromPool() throws SQLException {
        List<Integer> testIds = getClassPoolTestIds();
        int ix = new Random().nextInt(testIds.size());
        return  testIds.get(ix);
    }

    protected int selectPretestFromPool () throws SQLException {
        int classId = smgr.getStudentClass(smgr.getStudentId());
        int pretestId = getLeastSubscribedPretest(classId);
        insertPretestAssignment(classId,smgr.getStudentId(),pretestId);
        return pretestId;
    }

    private void insertPretestAssignment(int classId, int studentId, int pretestId) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "insert into pretestassignment (classId, studId, pretestId ) values (?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            stmt.setInt(2,studentId);
            stmt.setInt(3,pretestId);
            stmt.execute();
        }

        finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private int getLeastSubscribedPretest (int classId) throws SQLException {
        String q =
                "select pt.id, (select count(studId) from pretestassignment pa where pa.classId=c.id and\n" +
                        "pa.pretestId=pt.id) as nstuds\n" +
                        "from preposttest pt, prepostpool pp, class c where c.id=? and c.pretestPoolId=pp.id and pt.poolId=pp.id order by nstuds asc";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int pretestId = rs.getInt(1);
            int nstuds = rs.getInt(2);
            return pretestId;
        }
        return -1;
    }

    private List<Integer> getClassPoolTestIds() throws SQLException {
        int classId = smgr.getStudentClass(smgr.getStudentId());
        // get the class's pretest pool id
        PretestPool pool = DbPrePost.getPretestPool(smgr.getConnection(),classId);
        int poolId = pool.getId();

        List<Integer> testIds = DbPrePost.getTestsInPool(smgr.getConnection(), poolId);
        return testIds;
    }

    protected List<Integer> getPrePostProblemSets() throws SQLException {
        // note we want the order by id so that they come back in a predictable order.  Subclasses
        // of this class rely on this method to return lists of problem sets in a sorted order even
        // though this class doesn't really care.
        String q = "select id from PrePostTest where isActive=1 order by id";
        ArrayList ids = new ArrayList();
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(1);
            ids.add(new Integer(id));
        }
        return ids;
    }


    private Problem getRandomProblem() throws SQLException {
        Vector probs = getPrePostProblems(this.conn, 1);
        int size = probs.size();
        int ix = ran.nextInt(size);
        PrePostProblemDefn ppp = (PrePostProblemDefn) probs.elementAt(ix);
        return new edu.umass.ckc.wo.content.PrePostProblem(ppp);
    }


    public void init(SessionManager smgr) throws Exception {
        this.smgr = smgr;
        this.conn = smgr.getConnection();
    }

    private void pretestProblemSelected (Problem p) throws SQLException {
        smgr.getStudentState().addPretestProblem(p.getId());
        smgr.getStudentState().setCurPreProblem(p.getId());
    }

    private void posttestProblemSelected (Problem p) throws SQLException {
        smgr.getStudentState().addPosttestProblem(p.getId());
        smgr.getStudentState().setCurPostProblem(p.getId());
    }


    public void pretestComplete() throws SQLException {
        smgr.getStudentState().setPretestCompleted(true);
    }

    public void posttestComplete() throws SQLException{
        smgr.getStudentState().setPosttestCompleted(true);
    }



    // Because the pre/post selector implements ProblemSelector (which it probably shouldn't) these methods
    // need to exist even though they will never get called in a pretest
   public Problem selectProblemLessDifficulty (SessionManager smgr) throws Exception {
        return null;
    }
    public Problem selectProblemSameDifficulty (SessionManager smgr) throws Exception {
        return null;
    }
    public Problem selectProblemMoreDifficulty (SessionManager smgr) throws Exception {
        return null;
    }
}
