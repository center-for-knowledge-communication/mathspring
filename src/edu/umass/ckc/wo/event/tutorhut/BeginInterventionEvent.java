package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Sep 17, 2009
 * Time: 3:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeginInterventionEvent extends TutorHutEvent {

   private String interventionType;
   private int probElapsedTime;

   public BeginInterventionEvent (ServletParams p) throws Exception {
       super(p);
       interventionType = p.getString("interventionType");
       probElapsedTime = p.getInt("probElapsedTime",0);
   }

    public int getProbElapsedTime() {
        return probElapsedTime;
    }

    public String getInterventionType () {
        return this.interventionType;
    }
}
