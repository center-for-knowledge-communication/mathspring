package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/6/13
 * Time: 7:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShowProgressEvent extends TutorHutEvent {

    public ShowProgressEvent(ServletParams p) throws Exception {
        super(p);
    }
}
