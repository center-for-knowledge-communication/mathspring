package edu.umass.ckc.wo.tutormeta;


import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.TopicModel;

import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface ProblemSelectorInterface {


    public List<String> getReasonsForNullProblem (StudentState state);

    public Problem selectProblem(SessionManager smgr, StudentModel studentModel, NextProblemEvent e, TopicModel.difficulty nextProblemDesiredDifficulty) throws Exception;
}