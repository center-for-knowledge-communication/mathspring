package edu.umass.ckc.wo.tutor.emot;

import edu.umass.ckc.wo.state.StudentState;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Oct 27, 2009
 * Time: 4:47:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmotionModel {

    public void updateProblemData (StudentState state, int studId, long probElapsedTime, long elapsedTime);



    public boolean[] getAffect(int studId) ;
}
