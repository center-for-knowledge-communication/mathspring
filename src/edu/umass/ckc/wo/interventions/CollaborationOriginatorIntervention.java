package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/12/15
 * Time: 11:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationOriginatorIntervention extends TimeoutIntervention implements NextProblemIntervention{

    public String getType () {
        this.setWaitTime(1000);
        return "CollaborationOriginatorIntervention";
    }

    public String getDialogHTML () {
        String str = "<div><p>Please wait while we find a partner for you... <br/>" +
                "You will discuss the problem <b>together</b>. <br/>" +
                "<b>YOUR</b> job is to use the <b>mouse</b> and <b>keyboard</b>.<br/>";

        str+="</div>";
        return str;
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }}
