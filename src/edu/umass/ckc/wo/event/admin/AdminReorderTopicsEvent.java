package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2008
 * Time: 9:53:51 AM
 */
public class AdminReorderTopicsEvent extends AdminEditTopicsEvent {

    private int topicId;
    private String reorderType;
    private int source, destination;



    public AdminReorderTopicsEvent (ServletParams p) throws Exception {
        super(p);
        topicId = p.getInt("topicId");
        reorderType = p.getString("reorderType");
        if(reorderType.equals("move")) {
            source = p.getInt("topicFrom");
            destination = p.getInt("topicTo");
        }
    }

    public int getTopicId() {
        return topicId;
    }

    public String getReorderType() {
        return reorderType;
    }

    public int getSource() { return source; }

    public int getDestination() { return destination; }
}
