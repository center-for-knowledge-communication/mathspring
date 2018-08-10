package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.event.SessionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:59:59 AM
 * Sent when user clicks the "next problem" button.
 * Event action: continue
 * Event params:  sessId , elapsedTime (both handled by inheritance)
 */
public class GetTutorHutQuestionAnswerEvent extends SessionEvent {
    private String problemName;

    public GetTutorHutQuestionAnswerEvent(ServletParams p) throws Exception {
        super(p);
        problemName = p.getString("problemName");
    }

    public String getProblemName() {
        return problemName;
    }
}