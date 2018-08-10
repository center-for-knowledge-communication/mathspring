package edu.umass.ckc.wo.tutor.hintSel;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 21, 2009
 * Time: 10:35:58 AM
 */
public class StrategicHintSelector extends BaseHintSelector {

    // This selector is only to be used for selecting strategic hints so it does nothing on regular hint selection 
    public Hint selectHint(SessionManager smgr, StudentActionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Hint selectHint(SessionManager smgr) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Hint selectHint(SessionManager smgr, SelectHintSpecs selectionCriteria) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns a constant Hint object which is a keyword that the client interprets as a strategic hint.
     * If the problem does not have a strategic hint, returns a hint designated as noStrategicHint.
     * @param smgr_
     * @param probId
     * @return
     * @throws java.sql.SQLException
     */
    public Hint selectStrategicHint(SessionManager smgr_, int probId) throws SQLException {
        // only show a strategic hint if the problem has one and if no regular (tactical) hints
        // have been shown
        // The reason for this second condition is because once flash starts showing tactical hints
        // a strategic hint (if played) clears the stage of the previous hint graphics.
        if (hasStrategicHint(smgr_,probId) && !isTacticalHintShown(smgr_,probId)) {
            Hint stratHint= Hint.STRATEGIC_HINT;
            smgr_.getStudentState().setStrategicHintShown(true); // so it won't get shown again for this problem
            stratHint.setProblemId(probId);
            return stratHint;
        }
        else {

            Hint h =  Hint.NO_STRATEGIC_HINT;
            h.setProblemId(probId);
            return h;
        }
    }

    private boolean isTacticalHintShown(SessionManager smgr, int probId) {
        return smgr.getStudentState().getNumHintsGivenOnCurProblem() > 0;
    }

    public boolean hasStrategicHint(SessionManager smgr, int probId) throws SQLException {
        PreparedStatement ps = conn_.prepareStatement("select strategicHintExists from problem where id=?");
        ps.setInt(1,probId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            boolean hasStratHint = rs.getBoolean(1);
            return hasStratHint;
        }
        return false;
    }
}
