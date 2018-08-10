package edu.umass.ckc.wo.event.tutorhut;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.exc.AssistmentsBadInputException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent when user clicks the "next problem" button.
 * Event action: nextProblem
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class GetProblemDataEvent extends ActionEvent {


    public static final String ASSIGNMENT = "assignment";
    public static final String ASSIGNMENTP = "p3";
    public static final String ASSISTMENT = "assistment";
    public static final String ASSISTMENTP = "p4";
    public static final String PROBLEM = "problem";
    public static final String PROBLEMP = "p5";
    public static final String USER = "user";
    public static final String USERP = "p1";
    public static final String CLASS = "class";
    public static final String CLASSP = "p2";



    private String user;
    private String assignment;  // an Assistments input that must be passed back
    private String assistment;    // an Assistments input that must be passed back
    private String problem;     // an Assistments input that must be passed back
    private String assistmentsClass;

    public GetProblemDataEvent(ServletParams p) throws AssistmentsBadInputException, Exception {
        super(p);
        try {

            user = p.getString(USERP, null);
            if (user == null)
                user = p.getString(USER);
            assistmentsClass = p.getString(CLASSP, null);
            if (assistmentsClass == null)
                assistmentsClass = p.getString(CLASS);
            assignment = p.getString(ASSIGNMENTP, null);
            if (assignment == null)
                assignment = p.getString(ASSIGNMENT);
            assistment = p.getString(ASSISTMENTP, null);
            if (assistment == null)
                assistment = p.getString(ASSISTMENT);
            problem = p.getString(PROBLEMP, null);
            if (problem == null)
                problem = p.getString(PROBLEM);
        } catch (Throwable thr) {
            if (thr instanceof AssistmentsBadInputException)
                throw (AssistmentsBadInputException) thr;
            else throw new AssistmentsBadInputException(thr.getMessage());

        }

    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getAssistmentsClass() {
        return assistmentsClass;
    }


    
}
