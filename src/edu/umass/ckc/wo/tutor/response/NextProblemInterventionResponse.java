package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.tutormeta.Intervention;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/4/14
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class NextProblemInterventionResponse extends InterventionResponse {
    private boolean buildProblem;

    public NextProblemInterventionResponse(Intervention intervention) {
        super(intervention);
    }

    public boolean isBuildProblem() {
        return buildProblem;
    }

    public void setBuildProblem(boolean buildProblem) {
        this.buildProblem = buildProblem;
    }
}
