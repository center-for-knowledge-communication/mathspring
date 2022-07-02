package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.content.Lesson;
import edu.umass.ckc.wo.content.Problem;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent when user clicks the "next problem" button.
 * Event action: nextProblem 
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class PreviewProblemEvent extends IntraProblemEvent {
    // for debugging
    public static final String PROB_ID = "probID";

    private String probId=null;

    public PreviewProblemEvent (ServletParams p) throws Exception {
        super(p);

    }

    // THis is used for requests coming from student to show a particular problem.   From MPP, it will
    // want to show it in Practice mode.   From Assistments it may want practice OR example/demo

        public String getProbId() {
        return probId;
    }

    public void setProbId(String probId) {
        this.probId = probId;
    }

}
