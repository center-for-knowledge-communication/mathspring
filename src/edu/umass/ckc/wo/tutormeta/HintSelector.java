package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;

import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface HintSelector {


    public Hint selectHint (SessionManager smgr, StudentActionEvent e) throws Exception ;
    public Hint selectHint (SessionManager smgr) throws Exception ;
    public Hint selectHint (SessionManager smgr, SelectHintSpecs selectionCriteria) throws Exception ;

    public List<Hint> selectRemainingHints(SessionManager smgr) throws Exception ;
    public List<Hint> selectFullHintPath(SessionManager smgr, int probId) throws Exception ;

    public void init(SessionManager smgr) throws Exception;
}