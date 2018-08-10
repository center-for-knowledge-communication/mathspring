package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * <p> Created by IntelliJ IDEA.         ;
 * User: david
 * Date: Jul 23, 2008
 * Time: 6:57:39 PM
 */
public class AdminActivateProblemsEvent extends AdminProblemSelectionEvent {
    private String[] activated;
    private int topicId;


    public AdminActivateProblemsEvent (ServletParams p) throws Exception {
        super(p);
        topicId = p.getInt("topicId");
        activated = p.getStrings("activated");    // only those checkboxes that are checked send in their value (which is the id of the problem) when form is submitted
    }

    public String[] getActivatedIds () {
        return activated;
    }

    public int getTopicId() {
        return topicId;
    }


}
