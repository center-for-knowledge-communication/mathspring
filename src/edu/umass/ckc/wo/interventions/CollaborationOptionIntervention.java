package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 4/7/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationOptionIntervention extends InputResponseIntervention implements NextProblemIntervention{

    public static final String OPTION = "option";
    public static final String YES = "Yes_collaborate";
    public static final String NO_ALONE = "No_alone";
    public static final String NO_DECLINE = "No_decline";

    public  CollaborationOptionIntervention () {
        super();
    }
    public String getType () {
        return "CollaborationOptionIntervention";
    }


    public String getDialogHTML () {
        String str = "<div><p> Would you like to work with a person near you for the next problem?  <br/<br/>" + getFormOpen();
        str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+YES+"\" checked=\"checked\">Yes; there is someone next to me.</input><br>";
        str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+NO_ALONE+"\">No; there is no one next to me.</input><br>";
        str += "<input name=\""+OPTION+"\" type=\"radio\" value=\""+NO_DECLINE+"\">No; I don't want to.</input><br>";
        str += "<input type=\"hidden\" name=\"destination\" value=\"edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS\">";
        str+="</form></div>";
        return str;
    }


    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
