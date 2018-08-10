package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent by the client every N seconds
 * Event action: pollTutor
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class ClearUserPropertiesEvent extends TutorHutEvent {

    public ClearUserPropertiesEvent(ServletParams p) throws Exception {
        super(p);
    }

}