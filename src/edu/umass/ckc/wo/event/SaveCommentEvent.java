package edu.umass.ckc.wo.event;

import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/12/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveCommentEvent extends SessionEvent {
   private int topicId;
    private String comment;
    private String studentAction;

    public SaveCommentEvent(ServletParams p) throws Exception {
        super(p);
        setTopicId(p.getInt("topicId"));
        setComment(p.getString("comment"));
        setStudentAction(p.getString("studentAction"));

    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStudentAction() {
        return studentAction;
    }

    public void setStudentAction(String studentAction) {
        this.studentAction = studentAction;
    }
}
