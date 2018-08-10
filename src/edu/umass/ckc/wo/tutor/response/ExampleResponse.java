package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.tutor.Settings;

/**
 * <p> Created by IntelliJ IDEA.
 * User: Ivon Arroyo
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class ExampleResponse extends ProblemResponse {

    public ExampleResponse(Problem p) throws Exception {
        super(p);
        prob = p ;

        if ( prob!=null)
            prob.setMode(Problem.EXAMPLE);
        buildJSON();
    }




    public String logEventName() {
        if ( prob != null )
           return "example " + this.prob.getId();

        return null ;
    }


}