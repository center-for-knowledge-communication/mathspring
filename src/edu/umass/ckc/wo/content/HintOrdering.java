package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.util.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/16/14
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class HintOrdering {
    Connection conn;
    private static final int VISUAL = 1 ;
    private static final int ALGEBRAIC = 0 ;

    // Get a hint from the List of hints for this problem from the ID in the database
    protected Hint getHint(List<Hint> hints, int id) {
        for (Hint h: hints) {
            if (h.getId() == id)
                return h;
        }
        return null;
    }

    /**
     * Given the last hint (which may be -1) return the possible next hints.   If there are multiple
     * hint paths that fork after the last hint, this returns a list of more than 1 hint.  Usually, it just
     * returns 1 hint because there is usually no fork.
     *
     * The includeAnswerGivingHint is so that we can turn off the final hint in some cases which merely
     * says to "choose answer C" or the like.
     *
     * @param probId
     * @param lastHintId
     * @param includeAnswerGivingHint
     * @return
     * @throws Exception
     */
    protected List<Hint> getPossibleSuccessorHints(int probId, int lastHintId, List<Hint> hints, boolean includeAnswerGivingHint) throws Exception {
        List<Hint> possibleHints = new ArrayList<Hint>();
        // if no last hint, then add only the root hint to list of possible.
        if (lastHintId == -1) {
            for (int i = 0; i < hints.size(); i++) {
                if (((Hint) (hints.get(i))).isRoot()) {
                    possibleHints.add(hints.get(i));
                }
            }
            return possibleHints;
        } else {
            // if a previous hint was shown and there is only one next hint
            // (i.e. no branch in tree from the last hint) then select this hint.  If
            // there is a branch, then add all children of last hint to possible.
            //SqlQuery q = new SqlQuery();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String q = "select targetHint from SolutionPath " +
                        " where sourceHint = " + lastHintId;
                ps = conn.prepareStatement(q);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int newhint_id = rs.getInt("targetHint");
                    Hint h = getHint(hints, newhint_id);
                    if (h == null)
                        System.out.println("Cannot find hint " + newhint_id + " for problem " + probId);
                    // DM 2/24/11  By request.   We no longer want to give out hints that say
                    // "Choose C" or the like.  There may be some hints that give some information
                    // about how to solve the problem AND tell which choice should be selected (these
                    // are typically hints that are root hints and marked as givesAnswer=true - these
                    // must still be given out or the system will have no way of helping students on these problems
                    // Because the example system would like to show the full hint path (including which answer
                    // to select) this function is called with a flag (by default it will be true).
                    // we don't want hints that give the answer (unless they are root hints)
                    if (includeAnswerGivingHint || !h.getGivesAnswer() || h.isRoot()) {
                        possibleHints.add(h);
                    }
                }
                return possibleHints;
            }
            finally {
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            }
        }
    }

    // the old (more complicated version of this routine is commented out at the bottom
    // of this file.
    int chooseStrategy()  throws Exception
    {

//        if (studGroupId == 5)
//            return VISUAL;
//        else  return ALGEBRAIC;
        return ALGEBRAIC;

    }

    /* Picks one hint out of a set of more than 1 */
    protected Hint pickBranchHint(List<Hint> possibleHints
                                  ) throws Exception
    {
        //select if we should pick visual or not
        int selectStrategy = chooseStrategy() ;

        if ( selectStrategy == VISUAL ) {
            //return a hint that is visual
            for ( int i=0; i<possibleHints.size(); i++ ) {
                Hint h =    possibleHints.get(i);
                if (h != null && h.isVisual() )
                    return ( possibleHints.get(i)) ;
            }
        }
        else { // selectStrategy is ALGEBRAIC
            for ( int i=0; i<possibleHints.size(); i++ ) {
                Hint h =     possibleHints.get(i);
                if ( h != null && ! h.isVisual() )
                    return ( possibleHints.get(i)) ;
            }
        }
        return null ;
    }


    private Hint doSelectHint (int lastHintId, int probId) throws Exception {
        List<Hint> hints = getHintsForProblem(probId) ;
        // 2/23/11 DM do not include hints that give the answer unless they are ROOT hints
        List<Hint> possibleSuccessorHints = getPossibleSuccessorHints(probId,lastHintId,hints, false);

        // select from among those hints identified as possible.


        if ( possibleSuccessorHints.size() == 0 )
            // There are no more "next" hints, repeat the last one
            return null;

        if ( possibleSuccessorHints.size() == 1 )
            // There is only 1 hint, so just show that one
            return possibleSuccessorHints.get(0) ;

        // return one of the possible.   Currently this always returns a hint that is ALGEBRAIC.
        return pickBranchHint(possibleSuccessorHints) ;

    }




    protected List<Hint> getHintsForProblem (int probId) throws Exception {
        //SqlQuery q = new SqlQuery();
        List result = new Vector();
        String s = "select id,problemid,name,givesAnswer,is_root from hint where problemId=" + probId;

        ResultSet rs = SqlQuery.read(conn, s);

        while (rs.next()) {
            int id = rs.getInt(Hint.ID);
            int problemId = rs.getInt(Hint.PROBLEM_ID);
            String label = rs.getString(Hint.NAME);
            boolean givesAnswer = rs.getBoolean(Hint.GIVES_ANSWER);
            boolean isroot = rs.getBoolean("is_root");

            String att_query = "select " + Hint.ATT_VALUE +
                    " from HintAttributes " +
                    " where hintId" + "=" + id +
                    " and attribute = 'visual' " ;

            ResultSet attres = SqlQuery.read(conn,att_query) ;
            int isVisual = 0 ;
            if ( attres.next() ) {
                if ( attres.getInt(Hint.ATT_VALUE) == 1 )
                    isVisual = 1 ;
            }
            Hint nh = new Hint(id,
                    label,
                    problemId,
                    givesAnswer);
            nh.setIs_root(isroot);
            result.add(nh);
        }
        SqlQuery.closeRS(rs);

        return result;
    }

    private int setHintOrder(Hint h, int ix) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update hint h set h.order=? where h.id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, ix);
            stmt.setInt(2, h.getId());
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private void setProblemHintsOrder() throws Exception {
        List<Problem> probs = ProblemMgr.getAllProblems() ;
        for (Problem p : probs) {
            int lastHintId=-1;
            int ix = 1;
            Hint h = doSelectHint(lastHintId,p.getId());
            while (h != null) {
                setHintOrder(h,ix);
                ix++;
                lastHintId= h.getId();
                h = doSelectHint(lastHintId,p.getId());
            }
        }
    }



    public static void main(String[] args) {
        try {
            HintOrdering ho = new HintOrdering();
            ho.conn = DbUtil.getAConnection("rose.cs.umass.edu");
            ProblemMgr.loadProbs(ho.conn);
            ho.setProblemHintsOrder();
            System.out.println("Succeeded");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
