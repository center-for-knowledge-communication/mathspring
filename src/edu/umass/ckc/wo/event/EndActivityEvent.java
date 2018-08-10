package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 22, 2005
 * Time: 1:42:20 PM
 */
public class EndActivityEvent extends SessionEvent implements StudentActivityEvent {
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String PROB_ELAPSED_TIME = "probElapsedTime";
    public static final String PROBLEM_ID = "problemId"; // only passed when in SAT hut  - required on every call
    public static final String IS_SOLVED = "isSolved"; // pre/post/sat: attempted the problem
    public static final String USER_INPUT = "userInput"; // contains user typed inputs or selections in pre/post test/ other info for interventions 
    public static final String IS_CORRECT = "isCorrect"; // pre/post: prob answered correctly  sat: no meaning
    public static final String QUIT = "quit"; // only passed when in preposttest hut - optional


    private long elapsedTime;
    private long probElapsed;
    private int probId;
    private boolean isSolved;  // was the problem attempted?
    private String userInput;
    private boolean isCorrect;   // was the problem solved correctly?
    private boolean isQuit;



    // only for debugging
    public EndActivityEvent (int sessId, long time, long petime, int probId, boolean isAttempted, String userInput, boolean isCorrect) {
        this.elapsedTime = time;
        this.probElapsed = petime;
        this.probId = probId;
        this.isSolved = isAttempted;
        this.userInput = userInput;
        this.isCorrect = isCorrect;
        this.sessionId = sessId;
        this.isQuit = false;
    }

    public EndActivityEvent(ServletParams p) throws Exception {
        super(p);
        elapsedTime = p.getMandatoryInt(ELAPSED_TIME);
        probElapsed = p.getMandatoryInt(PROB_ELAPSED_TIME);
        probId = p.getMandatoryInt(PROBLEM_ID);
        isSolved = p.getMandatoryBoolean(IS_SOLVED); // sat: was problem solved correctly pre/post: meaningless
        userInput = p.getString(USER_INPUT,null);  // the actual answer typed in or selected from multi choice
        isCorrect = p.getBoolean(IS_CORRECT,false);  // sat: means nothing pre/post: is the problem correctly answered
        isQuit = p.getBoolean(QUIT,false); // optional - sent as true when user forces exit from pre/post
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public long getProbElapsed() {
        return probElapsed;
    }

    public int getProbId() {
        return probId;
    }

    public boolean isQuit() {
        return isQuit;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput (String s) {
        userInput = s;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public boolean isSolved() {
        return isSolved;
    }

}
