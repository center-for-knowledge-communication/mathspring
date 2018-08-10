package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 11/17/15
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminGetQuickAuthSkeletonEvent extends AdminEvent {

    int probId;
    String mode;

    public AdminGetQuickAuthSkeletonEvent(ServletParams p) throws Exception {
        super(p);
        probId = p.getInt("probId");
    }

    public int getProbId() {
        return probId;
    }
}