package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Sep 17, 2009
 * Time: 3:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndInterventionEvent extends TutorHutEvent {

    private int probElapsedTime;

   public EndInterventionEvent(ServletParams p) throws Exception {
       super(p);
       probElapsedTime = p.getInt("probElapsedTime",0);
   }

    public int getProbElapsedTime() {
        return probElapsedTime;
    }
}