package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/30/14
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetProblemListForTesterEvent extends TutorHutEvent {

    public GetProblemListForTesterEvent(ServletParams p) throws Exception {
        super(p);
    }
}
