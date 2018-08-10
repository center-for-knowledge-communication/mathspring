package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 10:03:27 AM
 * Event action: attempt
 * Event params: sessId, elapsedTime, probElapsedTime, userInput, isCorrect
 */
public class AttemptEvent extends IntraProblemEvent {
    private String userInput;
    private boolean isCorrect;
    private boolean intervene;


    public static final String USER_INPUT = "userInput";
    public static final String IS_CORRECT = "isCorrect";
    public static final String FORCE_INTERVENTION = "forceIntervention";

    public AttemptEvent (ServletParams p) throws Exception {
        super(p);
        setProbElapsedTime(p.getLong(PROB_ELAPSED_TIME));
        setUserInput(p.getString(USER_INPUT));
        setCorrect(Boolean.parseBoolean(p.getString(IS_CORRECT,"false")));
        String interv = p.getString(FORCE_INTERVENTION,"false");
        setIntervene(Boolean.parseBoolean(interv));
    }

    // for unit testing only
    public AttemptEvent (String userInput, boolean isCorrect,  long probElapsed, long elapsed, int sessId) {
        this.userInput=userInput;
        this.isCorrect=isCorrect;
        this.probElapsedTime=probElapsed;
        this.elapsedTime=elapsed;
        this.sessionId=sessId;
    }


    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isIntervene() {
        return intervene;
    }

    public void setIntervene(boolean intervene) {
        this.intervene = intervene;
    }    
}
