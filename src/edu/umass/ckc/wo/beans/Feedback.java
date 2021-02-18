package edu.umass.ckc.wo.beans;


import java.io.Serializable;
import java.sql.Timestamp;



/**
 * Frank	02-12-201	Created
 */


public class Feedback implements Serializable {

	public static final String TEACHER_TOOL_FEEDBACK = "teacherToolFeedback";
	public static final String PROBLEM_PREVIEW_FEEDBACK = "problemPreviewFeedback";

	public static final String PRIORITY_SERIOUS = "serious";
	public static final String PRIORITY_MINOR = "minor";
	public static final String PRIORITY_OTHER = "other";

	private int teacherId;
    private String messageType;
    private String priority;
    private String message;
    private	String objectId;
    private Timestamp timestamp;

    public Feedback() {
    }

    public Feedback(int teacherId, String messageType, String priority, String message, String objectId, Timestamp timestamp) {
        this.teacherId = teacherId;
        this.messageType = messageType;
        this.priority = priority;
        this.message = message;
        this.objectId = objectId;
        this.timestamp = timestamp;


    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public int getObjectId_int() {
        return Integer.valueOf(objectId);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp priority) {
        this.timestamp = timestamp;
    }

}
