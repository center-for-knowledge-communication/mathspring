package edu.umass.ckc.wo.content;

import net.sf.json.JSONObject;

/**
 * The only point of this class is to provide an object that holds the stuff necessary to construct JSON to send into the javascript
 * that wants a quickAuth problem
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 11/16/15
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 * Frank 01-26-21 Added short answer problems answer 
 */
public class QuickAuthProb {
    private Problem prob;
    private String probContentPath;
    private ProblemBinding binding;

    public QuickAuthProb(Problem prob, String probContentPath, ProblemBinding binding) {
        this.prob = prob;
        this.probContentPath = probContentPath;
        this.binding = binding;
    }

    public JSONObject buildJSON (JSONObject jo) {
        JSONObject probJSON = new JSONObject();
        prob.setAnswersViewList();
        prob.buildJSON(probJSON);
        jo.element("problem",probJSON);
        jo.element("binding",binding.getJSON())  ;
        jo.element("probContentPath", probContentPath);
        return jo;
    }
}
