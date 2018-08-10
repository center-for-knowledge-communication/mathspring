package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * <p> One of the first events upon entering the tutoring hut.  This requests a set of configuration switches
 * that tells Flash how to construct the GUI.  Things such as a cartoon character and certain buttons are
 * made available depending on the pedagogy a user is assigned.  The pedagogy is queried for the UI elements
 * it requires and it returns them.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:47:32 AM
 */
public class GetConfigEvent extends TutorHutEvent {

    public GetConfigEvent (ServletParams p) throws Exception {
        super(p);
    }

}
