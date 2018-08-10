package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 10:19:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class HintEvent extends IntraProblemEvent  {

    // we have this so that PedModels can call doSelectHint which calls a hint selector with an empty event just so it will process.
    public HintEvent () {}

    public HintEvent (ServletParams p) throws Exception {
        super(p);
    }

    // In a very simple tutor with a character all click character events may just be treated as requests for hints
    // so this convenience constructor is provided to convert the ClickCharacterEvent to a HintEvent
    public HintEvent(ClickCharacterEvent e) {
        setProbElapsedTime(e.getProbElapsedTime());
        setElapsedTime(e.getElapsedTime());
        setSessionId(e.getSessionId());
        setAction(e.getAction());
    }


}
