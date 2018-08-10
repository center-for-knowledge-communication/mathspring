package edu.umass.ckc.wo.tutor.model;

import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.tutor.response.Response;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/13/15
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TutorEventProcessor {

    // For now its a TUtorHutEvent.  Later we will make it a SessionEvent so that this interface can be implemented by all the models.
    public Response processUserEvent (TutorHutEvent e) throws Exception  ;

    public Response processInternalEvent (InternalEvent e) throws Exception  ;


}
