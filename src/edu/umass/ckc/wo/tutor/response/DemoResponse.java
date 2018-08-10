package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.tutor.Settings;
import net.sf.json.JSONObject;

/**
 * <p> Created by IntelliJ IDEA.
 * User: Ivon Arroyo
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class DemoResponse extends ProblemResponse implements NextProblemIntervention  {

    public DemoResponse(Problem p) throws Exception {
        super(p);
        prob = p ;

        if ( prob!=null)
            prob.setMode(Problem.DEMO);
        buildJSON();
    }


    @Override
    public JSONObject buildJSON(JSONObject jo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String logEventName() {
        if ( prob != null )
           return "demo " + this.prob.getId();

        return null ;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
}