package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;

import java.sql.Connection;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 12:49:04 PM
 */
public interface ExampleSelector {


    // An example selector merely selects a problem to play in example mode.
    public int selectProblem(Connection conn, int curProbId) throws Exception;
}
