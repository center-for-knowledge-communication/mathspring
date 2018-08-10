package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.tutorhut.IntraProblemEvent;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyrighdaft (c) 2002</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class AdventurePSolvedEvent extends IntraProblemEvent {
    public static final String USER_INPUT = "userInput";
    public static final String PROBLEM_NUMBER = "problemNumber";
    public static final String ADVENTURE_NAME = "adventureName";
    public static final String IS_SOLVED = "isSolved";
    public static final String PROB_ID = "problemId";
    private int probId;
    private String userInput;
    private int problemNumber;
    private boolean isSolved;
    private String adventureName;

    public AdventurePSolvedEvent(ServletParams p) throws Exception {
        super(p);
        this.userInput = p.getString(USER_INPUT, null);
        this.probId = p.getInt(PROB_ID);
        this.problemNumber = p.getInt(PROBLEM_NUMBER);
        this.isSolved = p.getBoolean(IS_SOLVED,false);
        this.adventureName = p.getString(ADVENTURE_NAME);
    }

    public int getProbId() {
        return (probId);
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber = problemNumber;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public String getAdventureName() {
        return adventureName;
    }

    public void setAdventureName(String adventureName) {
        this.adventureName = adventureName;
    }



}