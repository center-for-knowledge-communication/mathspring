package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.Video;
import edu.umass.ckc.wo.smgr.SessionManager;

import java.sql.Connection;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 12:49:04 PM
 */
public interface VideoSelector {

    public void init(SessionManager smgr) throws Exception ;

    //  A video can be returned as an intervention
    public String selectVideo(Connection conn, int probId) throws Exception;
}