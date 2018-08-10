package edu.umass.ckc.wo.tutormeta;


import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface ProblemSelector {


    void setParameters(PedagogicalModelParameters params);

    public Problem selectProblem(SessionManager smgr, NextProblemEvent e, ProblemScore lastProblemScore) throws Exception;
}