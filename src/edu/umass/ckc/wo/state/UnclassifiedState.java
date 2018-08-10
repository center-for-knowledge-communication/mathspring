package edu.umass.ckc.wo.state;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnclassifiedState {

    // IRT stuff that is used in MLStudentModel
    public static final String SAT_HUT_MOTIV_OBSERVATIONS = "sm.satHutMotivObservations";
    public static final String SAT_HUT_RESPONSES = "sm.satHutResponses";
    public static final String SAT_HUT_PROBLEM_ORDER = "sm.satHutProblemOrder";

    private static final String TEMP_PROBLEM_INDEX = "st.tempProblemIndex";

    // IRT stuff that is used in MLStudentModel
    private List<String> satHutMotivObservations;
    private List<String> satHutResponses;
    private List<String> satHutProblemOrder; // full sequence of problems given to the user.
}
