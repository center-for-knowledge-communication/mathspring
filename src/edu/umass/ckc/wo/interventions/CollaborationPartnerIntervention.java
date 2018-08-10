package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 3/13/15
 * Time: 3:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationPartnerIntervention extends TimeoutIntervention implements NextProblemIntervention{

    private String name = null;

    public String getType () {
        return "CollaborationPartnerIntervention";
    }

    public String getDialogHTML () {
        String str = "<div><p>In this next problem, you will work with <b><font color='red'>" + name + "</font></b> who should be" +
                " sitting next to you, on <b><font color='red'>" + name + "</font></b>'s screen for <strong>ONE</strong> math problem. <br/> <br/>" +
                "<b><font color='red'>" + name + "</font></b> will use the mouse and keyboard." +
                " Your job is to <strong>READ</strong> the math problem aloud on "+name+"'s screen.<br/><br/>" +
                " Work <strong>together</strong> to solve the problem.<br/>";
        str+="</div>";
        return str;
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPartner(String name){
        this.name = name;
    }
}
