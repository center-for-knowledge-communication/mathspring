package edu.umass.ckc.wo.assistments;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/4/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssistmentSessionData {
    private String assignment;
    private String assistment;
    private String problem;
    private String uid;
    private String aClass;
    private String logbackURL;

    public AssistmentSessionData(String assignment, String assistment, String problem, String uid, String aClass, String logbackURL) {
        this.assignment = assignment;
        this.assistment = assistment;
        this.problem = problem;
        this.uid = uid;
        this.aClass = aClass;
        this.logbackURL = logbackURL;
    }

    public String getAssignment() {
        return assignment;
    }

    public String getAssistment() {
        return assistment;
    }

    public String getProblem() {
        return problem;
    }

    public String getUid() {
        return uid;
    }

    public String getaClass() {
        return aClass;
    }

    public String getLogbackURL() {
        return logbackURL;
    }
}
