package edu.umass.ckc.wo.event;

import edu.umass.ckc.wo.enumx.StudentInputEnum;
import ckc.servlet.servbase.ServletParams;


public class StudentActionEvent extends SessionEvent implements StudentActivityEvent {
    public static final String USER_INPUT = "userInput";
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String PROB_ELAPSED_TIME = "probElapsedTime";

    public static final String REQUESTED_PROBLEM = "requestedproblem";
    public static final String ADVENTURE_NAME = "adventureName"; // only passed when in an adventure
    public static final String PROBLEM_NUMBER = "problemNumber"; // only passed when in an adventure
    public static final String PROBLEM_ID = "problemId"; // only passed when in SAT hut  - required on every call
    public static final String LAST_HINT_ID = "lastHintId"; // only passed when in SAT hut - may be omitted
    public static final String IS_PROBLEM_SOLVED = "isSolved"; //  sat: means problem was answered correctly pre/post: no meaning
    public static final String IS_ATTEMPT_CORRECT = "isCorrect"; //  pre/post: means problem was correctly answered sat: means attempt is correct


    protected String userInputString_;
    protected StudentInputEnum studentInput_;
    protected long elapsedTime_;
    protected long probElapsedTime;  // time since beginning of prob
    protected boolean isCorrect;  // sent on an attempt to let us know if its correct - the rest of the time it has no meaning
    protected boolean isSolved;   // sent on each call letting us know if the student has attempted the problem
    protected int adventureProblemNumber_;
    protected String adventureName;
    protected String requestedProblem_;
    protected int probId; // sent for SAT hut problems
    protected int lastHintId; // send (when applicable) for SAT hut
    private boolean isAttempt=false;



    public StudentActionEvent(ServletParams p) throws Exception {
        super(p);
        userInputString_ = p.getString(USER_INPUT,"");
        studentInput_ = StudentInputEnum.getInstance(userInputString_);
        if (studentInput_==null || studentInput_ == StudentInputEnum.A || studentInput_ == StudentInputEnum.B ||
                studentInput_ == StudentInputEnum.C || studentInput_ == StudentInputEnum.D || studentInput_ == StudentInputEnum.E)
            isAttempt=true;
        elapsedTime_ = p.getInt(ELAPSED_TIME, 0);
        isCorrect = p.getBoolean(IS_ATTEMPT_CORRECT, false);
        this.adventureName = p.getString(ADVENTURE_NAME, null);
        // since the call is not coming from an adventure it may be coming from SAT or prepost hut
        if (this.adventureName == null) {
            String pid = p.getString(PROBLEM_ID,null);
            if (pid == null || pid.equals("undefined"))
                pid = "-1";
            this.probId = Integer.parseInt(pid);
            this.probElapsedTime = p.getMandatoryInt(PROB_ELAPSED_TIME);
            this.isSolved = p.getMandatoryBoolean(IS_PROBLEM_SOLVED);
            String lhiStr =  p.getString(LAST_HINT_ID,"-1");
            try {
                this.lastHintId = Integer.parseInt(lhiStr);
            } catch (NumberFormatException e) {
                this.lastHintId = -1;
            }
        }
        try {
            this.adventureProblemNumber_ = p.getInt(PROBLEM_NUMBER, 0);
            // todo there is a bug with flash sending incorrect problem numbers and adventure names.  So
            // we hard-code things here just so that an exception doesn't get returned.
        } catch (Exception ee) {
            this.adventureProblemNumber_ =-1;
        }

        try {
            requestedProblem_ = p.getString(REQUESTED_PROBLEM);
        } catch (Exception e) {
            requestedProblem_ = null;
        }
    }

    // only for debugging
    public StudentActionEvent (int sessionId, String userInput, long elapsedTime, boolean isCorrect, int probId,
                               long probElapsedTime, boolean isSolved, int lastHintId, String action) {
        this.sessionId = sessionId;
        this.action_ = action;
        if (action.equals("attempt"))
            this.isAttempt = true;
        this.userInputString_ = userInput;
        studentInput_ = StudentInputEnum.getInstance(userInput);
        this.elapsedTime_ = elapsedTime;
        this.isCorrect = isCorrect;
        this.probId = probId;
        this.probElapsedTime = probElapsedTime;
        this.isSolved = isSolved;
        this.lastHintId = lastHintId;

    }

    public String getUserInput()  {
        return userInputString_;
    }

    public void setUserInput(String s) {
        userInputString_ = s;
    }

    public StudentInputEnum getStudentInput() throws Exception {
        return studentInput_;
    }

    public long getElapsedTime()  {
        return elapsedTime_;
    }

    public boolean isCorrect()  {
        return isCorrect;
    }

    public void setadventureProblemNumber(int adventureProblemNumber_) {
        this.adventureProblemNumber_ = adventureProblemNumber_;
    }

    public int getAdventureProblemNumber() {
        return adventureProblemNumber_;
    }

    public String getRequestedProblem() {
        return requestedProblem_;
    }

    public void setAdventureName(String adventureName) {
        this.adventureName = adventureName;
    }

    public String getAdventureName() {
        return adventureName;
    }

    public long getProbElapsed() {
        return probElapsedTime;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public int getProbId() {
        return probId;
    }

    public int getLastHintId() {
        return lastHintId;
    }

    public boolean isAttempt() {
        return isAttempt;
    }

}