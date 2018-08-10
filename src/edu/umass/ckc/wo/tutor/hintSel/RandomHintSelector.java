package edu.umass.ckc.wo.tutor.hintSel;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.HintSelector;
import edu.umass.ckc.wo.util.SqlQuery;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;


public class RandomHintSelector extends BaseHintSelector implements HintSelector {
    protected Connection conn_;
    protected Random generator_ = new Random();


    public void init(SessionManager smgr) {
        conn_ = smgr.getConnection();
    }




    public Hint selectHint(SessionManager smgr_, StudentActionEvent e) throws Exception {
        int lastHintId = e.getLastHintId();
        return doSelectHint(lastHintId, e.getProbId());
    }

    public Hint selectHint(SessionManager smgr) throws Exception {
        init(smgr); // sets the cur problem 
        int lastHintId = smgr.getStudentState().getCurHintId();
        return doSelectHint(lastHintId, smgr.getStudentState().getCurProblem());
    }

    /**
     * given selection criteria (probably chosen by a student in an intervention that asks him something which
     * would guide the hint selection process).
     * @param smgr
     * @param selectionCriteria
     * @return
     */
    public Hint selectHint (SessionManager smgr, SelectHintSpecs selectionCriteria) throws Exception {
        // this hint selector does not know how to work with criteria
        return selectHint(smgr);
    }

    /** When faced with a branch, this randomly picks one hint out of a set.
     * This means that no strategy is adhered to as it goes down the tree of hints.   */
    protected Hint pickBranchHint(List<Hint> possibleHints, int studId, int studGroupId, double percVisual) throws Exception {
        int n = possibleHints.size();
        int ix = generator_.nextInt(n);
        return possibleHints.get(ix);
    }

      // This isn't being used.
    private Hint doSelectHint(int lastHintId, int probId) throws Exception {
       return null;
    }

    // get the ID of a hint from the list of hints for this problem
    private int getHintId(List hints, String label) {
        for (int i = 0; i < hints.size(); i++) {
            if (((Hint) hints.get(i)).getLabel().equals(label))
                return ((Hint) hints.get(i)).getId();
        }
        return -1;
    }


}