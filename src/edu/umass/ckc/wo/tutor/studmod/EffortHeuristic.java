package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.state.StudentState;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 7/17/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class EffortHeuristic {

    public static final String NOT_READING = "NOTR";
    public static final String SOLVED_ON_FIRST = "SOF";
    public static final String SKIP = "SKIP";
    public static final String GIVEUP = "GIVEUP";
    public static final String SAW_HINT_AND_SOLVED = "SHINT";
    public static final String SAW_HELP_AND_SOLVED = "SHELP";
    public static final String CORRECT_AFTER_ONE = "ATT";
    public static final String CORRECT_AFTER_TWO = "GUESS";

    public static final int READING_THRESHOLD = 4000; // we expect an answer after 4 seconds o/w considered to be not reading


    /**
     * Based on specification sent to us by Ivon on  March 24,2014
     *
     *
     * Priorities, order of checking:

     SKIP  --no action at all, no answer, no hint, just skipped
     else
     NOTR  --some action, but too fast first action (cut off 4 seconds),
     doesn't matter if solved or not
     else
     GIVEUP --some action, not too fast first action, but did not solve
     correctly at all
     else
     SOF --no hints nor example nor videos, and correct in 1 attempt
     else
     BOTTOMOUT --SAW hints/example/videos and eventually correct, but saw
     LAST "chooseXXX" hint  (where did the Solve it button go? This is
     changing the consistency of behaviors we record compared to our older
     data sets).
     else
     SHINT --saw hints/example/videos and eventually got it correct, saw at
     least 1 hint
     else
     ATT --not seen hints/example/videos but correct after 1 incorrect
     else
     GUESS --not seen hints/example/videos, correct after > 1 incorrect

     *
     * @param state
     * @return
     */

    public String computeEffort(StudentState state) {

        if (state.getCurProblemMode().equals(Problem.DEMO) || state.getCurProblemMode().equals(Problem.EXAMPLE)
        || state.getCurProblemMode().equals(Problem.TOPIC_INTRO))
            return "";

        if (state.getNumAttemptsOnCurProblem() == 0 && state.getNumHintsGivenOnCurProblem() == 0 && state.getNumHelpAidsGivenOnCurProblem() == 0)
            return SKIP;

        else if (state.getTimeToFirstEvent() <= READING_THRESHOLD)
            return NOT_READING;
        else if (state.getTimeToFirstEvent() > 0 && state.getTimeToSolve() <= 0)
            return GIVEUP;
        else if (state.getTimeToSolve() > 0 && state.getNumMistakesOnCurProblem() == 0 && state.getNumHelpAidsGivenOnCurProblem() == 0 &&
                state.getNumHintsGivenOnCurProblem() == 0)
            return SOLVED_ON_FIRST;
        else if (state.getNumHintsBeforeCorrect() > 0 && state.getTimeToSolve() > 0)
            return SAW_HINT_AND_SOLVED;
        else if (state.getNumHelpAidsBeforeCorrect() > 0 && state.getTimeToSolve() > 0)
            return SAW_HELP_AND_SOLVED;
        else if (state.getNumHelpAidsGivenOnCurProblem() == 0 && state.getNumHintsBeforeCorrect() == 0 && state.getNumMistakesOnCurProblem() == 1 &&
                state.getTimeToSolve() > 0)
            return CORRECT_AFTER_ONE;
        else if (state.getNumHelpAidsGivenOnCurProblem() == 0 && state.getNumHintsBeforeCorrect() == 0 && state.getNumMistakesOnCurProblem()  > 1 &&
                state.getTimeToSolve() > 0)
            return CORRECT_AFTER_TWO;


        return "unknown";

    }


    public String computeEffortDovans(StudentState state, int numAttemptsToSolve) {

        if (state.getTimeToFirstAttempt() <= 4000 || (state.getNumHintsGivenOnCurProblem() > 0 && state.getTimeToFirstHint() <= 4000))  // Not reading, whatever they do after
            return "NOTR";
        else if (numAttemptsToSolve == 1 && state.getNumHintsBeforeCorrect() == 0)   //Reading and solved on First Attempt, without help
            return "SOF";
        else if (state.isSolutionHintGiven()) //Reading and solved with at most 2 incorrect attempts, But saw Help (might have accepted when offered)
            return "BOTTOMOUT";
        else if (state.getTimeToSolve() > 0 && state.getNumHelpAidsGivenOnCurProblem() > 0 && state.getNumMistakesOnCurProblem() <= 2) //Reading and solved with at most 2 incorrect attempts, But saw Help (might have accepted when offered)
            return "SHINT";
        else if (state.getTimeToSolve() > 0 && state.getNumHelpAidsGivenOnCurProblem() == 0 && state.getNumMistakesOnCurProblem() <= 2) //Reading and solved with no hints in at most 2 Incorrect Attempts
            return "ATT";
        else if (state.getTimeToSolve() > 0 && state.getNumMistakesOnCurProblem() > 3) //Solved Eventually, in 4-5 attempts, most likely guessing
            return "GUESS";
        else if (state.isProblemSolved()==false) //Reading and gave up. Did not answer.
            return "GIVEUP";

        return "unknown";

    }
}
