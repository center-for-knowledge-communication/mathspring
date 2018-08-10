package edu.umass.ckc.wo.tutor.probmode;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.state.StudentState;

import java.util.Random;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2009
 * Time: 5:06:26 AM
 */
public class PracticeProblemModeSelector implements ProblemModeSelector {
    private static Random random = new Random();

    public PracticeProblemModeSelector() {
    }

    public String selectMode(Problem p, StudentState state) {

        return Problem.PRACTICE;
    }

}