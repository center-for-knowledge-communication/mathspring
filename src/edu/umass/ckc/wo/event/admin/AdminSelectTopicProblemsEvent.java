package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2008
 * Time: 9:53:51 AM
 */
public class AdminSelectTopicProblemsEvent extends AdminProblemSelectionEvent {
    private int topicId;


    public AdminSelectTopicProblemsEvent (ServletParams p) throws Exception {
        super(p);
        topicId = p.getInt("topicId");
    }


    public int getTopicId() {
        return topicId;
    }
}
