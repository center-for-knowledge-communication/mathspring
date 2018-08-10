package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.log.AuxilaryEventLogger;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 10:22:38 AM
 * Event action: response
 * Event params: sessId, elapsedTime, userInput
 */
public class InputResponseEvent extends IntraProblemEvent {

    private String userInput;

    // not an parameter passed in the URL String.   This is set as the event is processed so that
    // the logger can get access to some extra info as it logs this event
    private AuxilaryEventLogger auxInfo;  // holds extra logging information that will be logged to tables other than the normal eventlog


    public static final String USER_INPUT = "userInput";

    public InputResponseEvent(ServletParams p) throws Exception {
        super(p);
        setUserInput(p.getString(USER_INPUT));    
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    

    public AuxilaryEventLogger getAuxInfo() {
        return auxInfo;
    }

    public void setAuxInfo(AuxilaryEventLogger auxInfo) {
        this.auxInfo = auxInfo;
    }


}
