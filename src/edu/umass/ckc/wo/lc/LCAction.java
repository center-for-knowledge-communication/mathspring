package edu.umass.ckc.wo.lc;

/**
 * Action part of a rule.
 * User: marshall
 * Date: 3/10/16
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class LCAction extends LCRuleComponent {
    private int id;
    private String msgText;
    private int msgId; // the id of the lcmessage row
    private String name;
    private String actionType; // like PlayLearningCompanion, ShowUserMessage, etc


    public LCAction(int id, String msgText, String name, String actionType, int msgId) {
        this.id = id;
        this.msgText = msgText;
        this.name = name;
        if (actionType == null)
            actionType = "playLearningCompanion";
        this.actionType = actionType;
        this.msgId = msgId;
    }

    public int getId() {
        return id;
    }

    public String getMsgText() {
        return msgText;
    }

    public String getName() {
        return name;
    }

    public String getActionType() {
        return this.actionType;
    }

    public int getMsgId() {
        return msgId;
    }
}
