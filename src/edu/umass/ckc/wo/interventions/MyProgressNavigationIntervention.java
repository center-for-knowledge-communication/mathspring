package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/3/14
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyProgressNavigationIntervention extends ChangeGUIIntervention implements NextProblemIntervention {
    private boolean buildProb;

    public MyProgressNavigationIntervention(boolean isNotify, String when, String notifyHTML, boolean buildProblem, String component, String action) {
        super(when,notifyHTML,isNotify,component,action);
    }

    public String getType () {
        return "MyProgressNavigation";
    }


    @Override
    public boolean isBuildProblem() {
        return this.buildProb;
    }
}


