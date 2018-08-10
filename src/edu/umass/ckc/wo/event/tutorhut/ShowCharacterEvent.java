package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 10:22:38 AM
 * Event action: response
 * Event params: sessId, elapsedTime, userInput
 */
public class ShowCharacterEvent extends IntraProblemEvent {


    public ShowCharacterEvent(ServletParams p) throws Exception {
        super(p);
    }



}