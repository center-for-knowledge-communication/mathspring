package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/23/15
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DemoProblemIntervention implements NextProblemIntervention {
    private Problem problem;

    public DemoProblemIntervention(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject buildJSON(JSONObject jo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String logEventName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
