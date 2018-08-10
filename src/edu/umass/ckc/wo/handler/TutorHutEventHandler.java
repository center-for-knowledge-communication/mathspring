package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import org.apache.log4j.Logger;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 16, 2005
 * Time: 6:21:44 PM
 */
public class TutorHutEventHandler {
    private static Logger logger = Logger.getLogger(StudentActionHandler.class);
    private SessionManager smgr;

    public TutorHutEventHandler(SessionManager smgr) {
        this.smgr = smgr;
    }


    /**
     * All StudentActionEvents taking place in the tutor hut come here
     * processing
     *
     * @param e
     * @return
     * @throws Exception
     */
    public View handleRequest(TutorHutEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        String curLoc = smgr.getStudentState().getCurLocation();  // find out where the student is in the GUI
        PedagogicalModel pm = smgr.getPedagogicalModel();

        final Response r = pm.processUserEvent(e);
        // If the pedagogy forwards to a JSP the Activity will be null and we just return null
        if (r != null)
            return new View() {
                public String getView() throws Exception {
                    if (r != null) {
                        String jsonstr = r.getJSON().toString();
                        logger.debug(jsonstr);
                        return jsonstr;
                    }
                    else throw new Exception("Null Response received by TutorHutEventHandler.handleRequest");
                }
            };
        else return null;
    }


}