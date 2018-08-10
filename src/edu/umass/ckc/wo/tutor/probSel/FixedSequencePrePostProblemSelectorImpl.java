package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.content.PrePostProblem;
import edu.umass.ckc.wo.content.PrePostProblemDefn;
import edu.umass.ckc.wo.tutor.probSel.PrePostProblemSelectorImpl;
import edu.umass.ckc.wo.db.DbPrePost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a PreTest/PostTest problem selector that behaves in a non-random way.  The problem set it selects for a pretest
 * is always the one with minimum id.  The problem set it chooses for the post test is always the second most minimum id.
 * Problems are selected by taking the first unseen problem for a list sorted by problem Id.   
 *
 * This problem selector is used by the testing system for verifying the correctness of student models.
 *
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Mar 22, 2006
 * Time: 1:04:20 PM
 */
public class FixedSequencePrePostProblemSelectorImpl extends PrePostProblemSelectorImpl {

    public FixedSequencePrePostProblemSelectorImpl () {
        super();
    }

    /**
     * Overrides the superclasses method for selecting the next pre/post problem from the remaining unseen
     * problems in the test. Takes the first one from the list of unseen problems.
     * @param unseenProbs
     * @param count
     * @param numCorrect
     * @return
     * @throws SQLException
     */
    protected PrePostProblemDefn selectUnseenProblem(ArrayList unseenProbs, int count, int numCorrect) throws SQLException {
        // now select randomly from the unseen problems.
        if (unseenProbs.size() > 0) {
            int ix = 0;
            PrePostProblemDefn ppp = DbPrePost.getPrePostProblem(conn, Integer.parseInt((String) unseenProbs.get(ix)));
            // return a new pp and include the numProbsInTest and numProbsSeen
            return new PrePostProblem(ppp,count,count-unseenProbs.size());
        } else  {
            // special PPProblem marked EOT with numCorrect/numProblemsInTest
            return new PrePostProblem(PrePostProblem.END_OF_TEST,numCorrect,count);
        }
    }


    /**
     * Overrides the superclass.  Takes the first pre/post test from the list of problem sets.
     * this will be used for the pretest.
     * @return
     * @throws SQLException
     */
    protected int selectProblemSet() throws SQLException {
        List<Integer> ids = getPrePostProblemSets();
        int ix = 0;
        return ((Integer) ids.get(ix)).intValue();
    }


    /**
     * Overrides the superclass.  Take the second pre/post test from the list of problem sets.  This
     * will be used for the post test.
     *   select the post test problem set given the pretest problem set.  The problem set is chosen randomly
     * from the tests that were not given as the pretest.  Override this method in a subclass to achieve
     * other than random results.
     * @param problemSet
     * @return
     * @throws SQLException
     */
    protected int selectProblemSet(int problemSet) throws SQLException {
        List<Integer> ids = getPrePostProblemSets();
        int ix = 1;
        return ((Integer) ids.get(ix)).intValue();
    }

}
