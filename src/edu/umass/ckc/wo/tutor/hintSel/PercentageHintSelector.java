package edu.umass.ckc.wo.tutor.hintSel;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.HintSelector;
import edu.umass.ckc.wo.util.SqlQuery;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;

import java.sql.ResultSet;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class PercentageHintSelector  extends BaseHintSelector implements HintSelector {

    protected Random generator_ = new Random();
    private static final int VISUAL = 1 ;
    private static final int ALGEBRAIC = 0 ;
    private double perc_visual ;

    public PercentageHintSelector() {
    }

    public PercentageHintSelector(SessionManager smgr) throws Exception {
        this.init(smgr);
    }

    //    public void setServletInfo(SessionManager smgr, int probId) throws Exception {
//
//        super.setServletInfo(smgr, probId);
//        conn_ = smgr.getConnection();
//        state = new SelectorState();
//        state.load(conn_,smgr.getStudentId());  // super loads the state
//
//    }



    // no longer doing complicated tree searches for the next hint.    Now it just gets the next one in the sequence
    // which is stored in order in the Problem object.
    private Hint doSelectHint (int lastHintId, int probId, int studId) throws Exception {
        List<Hint> hints = getHintsForProblem(probId) ;
        // 2/23/11 DM do not include hints that give the answer unless they are ROOT hints
        if (lastHintId == -1 && hints != null && hints.size() > 0)
            return hints.get(0);
        else if (lastHintId != -1)
        {
            Hint last=null;
            for (Hint h : hints)
                if (h.getId()==lastHintId) {
                    last = h;
                    break;
                }
            if (last !=null)
            {
                int ix = hints.indexOf(last);
                if (hints.size() > ix+1) {
                    Hint h= hints.get(ix+1);
                    // We do not want to give hints that give the answer.
                    if (h.getGivesAnswer())
                        return last;
                    else return h;
                }
                return last;
            }
            return null;
        }
        else return null;


    }

//
//    private Hint doSelectHintOld (int lastHintId, int probId, int studId) throws Exception {
//        List<Hint> hints = getHintsForProblem(probId) ;
//        // 2/23/11 DM do not include hints that give the answer unless they are ROOT hints
//        List<Hint> possibleSuccessorHints = getPossibleSuccessorHints(probId,lastHintId,hints, false);
//
//        // select from among those hints identified as possible.
//
//
//        if ( possibleSuccessorHints.size() == 0 )
//            // There are no more "next" hints, repeat the last one
//            return getHint(hints, lastHintId);
//
//        if ( possibleSuccessorHints.size() == 1 )
//            // There is only 1 hint, so just show that one
//            return possibleSuccessorHints.get(0) ;
//
//        // return one of the possible.   Currently this always returns a hint that is ALGEBRAIC.
//        return pickBranchHint(possibleSuccessorHints, studId) ;
//
//    }

//    public Hint selectHintOld (SessionManager smgr, HintEvent e) throws Exception {
//        if (problem == null)
//            throw new DeveloperException("A hint is being requested when no problem has been shown");
//        boolean hasStrategicHint = problem.hasStrategicHint();
//        int lastHintId = smgr.getProblemState().getCurHintId();
//        // If the problem hasn't had a hint shown and there is a strategic hint return a strategic hint
//        if ( lastHintId == -1 && hasStrategicHint )
//            return selectStrategicHint(smgr, problem.getId());
//        return doSelectHint(lastHintId,problem.getId(),smgr.getStudentId());
//
//    }


    public Hint selectHint(SessionManager smgr) throws Exception {
        init(smgr); // this sets the cur problem which is extracted from smgr.studentState
        if (problem == null)
            throw new DeveloperException("A hint is being requested when no problem has been shown");
        int lastHintId = this.smgr.getStudentState().getCurHintId();
        return doSelectHint(lastHintId,problem.getId(), this.smgr.getStudentId());

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
    
    /** Selects the next hint to show from the problem and the last hint seen */
    public Hint selectHint (SessionManager smgr_,
                            StudentActionEvent e) throws Exception {
        int lastHintId = e.getLastHintId();

        return doSelectHint(lastHintId,e.getProbId(),smgr_.getStudentId());
    }



    /* Picks one hint out of a set of more than 1 */
    protected Hint pickBranchHint(List<Hint> possibleHints,
                                  int studId) throws Exception
    {
        //select if we should pick visual or not
        int selectStrategy = chooseStrategy(studId) ;

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

    // the old (more complicated version of this routine is commented out at the bottom
    // of this file.
    int chooseStrategy(int studId)  throws Exception
    {

//        if (studGroupId == 5)
//            return VISUAL;
//        else  return ALGEBRAIC;
            return ALGEBRAIC;

    }

    // get the ID of a hint from the list of hints for this problem
    private int getHintId(List hints, String label)
    {
        for ( int i=0; i<hints.size(); i++ ) {
            if ( ((Hint) hints.get(i)).getLabel().equals(label) )
                return ((Hint) hints.get(i)).getId() ;
        }
        return -1 ;
    }


/*
    int chooseStrategy (Problem p,
                        int studId,
                        int studGroupId,
                        double percVisual)  throws Exception {
        // If the person has already seen a hint type for this problem, choose
        // the same strategy seen before

        String s = "select " + Hint.ATT_VALUE +
            " from Hint, HintAttributes, EpisodicData " +
            " where Hint." + Hint.PROBLEM_ID + "=" + p.getId() +
            " and EpisodicData.studId = " + studId +
            " and Hint.id = HintAttributes.hintid " +
            " and Hint.id = EpisodicData.hintid " +
            " and attribute = 'visual' " ;

        SqlQuery q = new SqlQuery();
        ResultSet rs = q.read(conn_,s);

        if ( rs.next() )  { //if there has been one type of hint assigned already for this problem, choose that same one
            int isVisual = rs.getInt(Hint.ATT_VALUE) ;
            if ( isVisual == 1 )
                return VISUAL ;

            return ALGEBRAIC ;
        }

        // Otherwise, count the amount of problems that were given visual and algebraic hints up to now
        s = "select EpisodicData." + Hint.PROBLEM_ID + "," + Hint.ATT_VALUE +
            " from Hint, HintAttributes, EpisodicData2 " +
            " where Hint.id = HintAttributes.hintid " +
            " and Hint.id = EpisodicData2.hintid " +
            " and studId = " + studId +
            " and attribute = 'visual' " +
            " and hintstep <> 'none' " +
            " group by EpisodicData." + Hint.PROBLEM_ID + "," + Hint.ATT_VALUE ;

        rs = q.read(conn_,s);

        int countVisual = 0 ;
        int countAlgeb  = 0 ;

        while ( rs.next() )  { // count all the visual and algebraic problems
            int isVisual = rs.getInt(Hint.ATT_VALUE) ;
            if ( isVisual == 1 )
                countVisual++ ;
            if ( isVisual == 0 )
                countAlgeb++ ;
        }

        double idealVisual = percVisual * (countVisual + countAlgeb) ;
        double idealAlgebraic = (1-percVisual) * (countVisual + countAlgeb) ;


        // If the proportion of algebraic hints given is higher than the desired, give a visual hint
        if ( countAlgeb > idealAlgebraic ) {
            return VISUAL ;
        }

        // If the proportion of visual hints given is higher than the desired, give an algebraic hint
        if ( countVisual > idealVisual ) {
            return ALGEBRAIC ;
        }

        if ( countAlgeb == countVisual) {
            //if there is a tie, or no selection has been made before, choose VISUAL with probability p
            if ( java.lang.Math.random() < percVisual )
                return VISUAL ;

            return ALGEBRAIC ;
        }

        if ( percVisual == 1 ) return VISUAL ;
        if ( percVisual == 0 ) return ALGEBRAIC ;

        return VISUAL ;  // Should never reach this point.
    }

    */
}
