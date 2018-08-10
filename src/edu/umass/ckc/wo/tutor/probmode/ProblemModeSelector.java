package edu.umass.ckc.wo.tutor.probmode;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.state.StudentState;

/**
 * Based on the idea that a pedagogy might process a student request for the next problem by returning the next problem
 * as either a "practice" problem or as an "example".   
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2009
 * Time: 5:05:09 AM
 */
public interface ProblemModeSelector {


    public String selectMode(Problem p, StudentState state) throws Exception;
}
