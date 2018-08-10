package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AttemptInterventionSelector extends InterventionSelector  implements PedagogicalMoveListener {

    protected List<AttemptInterventionSelector> subSelectors;

    public AttemptInterventionSelector(SessionManager smgr) {
        super(smgr);
    }


    /**
     * Subclasses that select intervention at the time of attempt event must override this.
     *
     * @param e
     * @return
     * @throws Exception
     */
    public abstract Intervention selectIntervention(AttemptEvent e) throws Exception;

    /**
     * When an intervention is shown it may have a simple continue button that the user must click to end the intervention.
     * This even needs to be processed by the InterventionSelector.   It may choose another Intervention or it may be done
     * in which case it will return null.
     *
     * @param e
     * @return
     */
    public abstract Response processContinueAttemptInterventionEvent(ContinueAttemptInterventionEvent e) throws Exception;
    public abstract Response processInputResponseAttemptInterventionEvent(InputResponseAttemptInterventionEvent e) throws Exception;

    @Override
    public void problemGiven(Problem p) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void exampleGiven(Problem ex) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void lessonIntroGiven(TopicIntro intro) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attemptGraded(boolean isCorrect) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hintGiven( Hint hint) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    public void interventionGiven(Intervention intervention) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newTopic(Topic t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newSession(int sessId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getUserInputXML() throws Exception {
        if (subSelectors == null)
            return this.userInputXML;
        else {
            for (AttemptInterventionSelector sel: this.subSelectors) {
                String x = sel.getUserInputXML();
                if (x != null)
                    return x;

            }
            return null;
        }

    }
}
