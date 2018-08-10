package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/4/14
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyProgressNavigationAskIntervention extends InformationIntervention implements NextProblemIntervention {
    public String getType () {
        return "MyProgressNavigationAsk";
    }


    public String getDialogHTML () {
        String str = "<div><p>Let's see how much progress we are making!<br>";

        str+="</div>";
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
