package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 12, 2008
 * Time: 11:18:13 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalModelInterface {

    Response processEvent (SessionEvent e) throws Exception;
}
