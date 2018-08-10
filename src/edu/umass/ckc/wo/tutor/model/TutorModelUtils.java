package edu.umass.ckc.wo.tutor.model;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelector;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.HintSelector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/16/15
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TutorModelUtils {

    // Put the hints into a problem which is being used for a demo
    public void setupDemoProblem(Problem problem, SessionManager smgr, HintSelector hintSelector) throws Exception {
        hintSelector.init(smgr);
        // need to put in the solution since its an example
        List<Hint> soln = hintSelector.selectFullHintPath(smgr, problem.getId());
        problem.setSolution(soln);
        problem.setMode(Problem.DEMO);
        smgr.getStudentState().setNextProblemMode(Problem.DEMO);
        smgr.getStudentState().setNextProblem(problem.getId()); // so that the ensuing BeginProblemEvent will move forward to the correct prob ID and log it
    }

    public InterventionSelector getLastInterventionSelector (SessionManager smgr) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String lastInterventionClass = smgr.getStudentState().getLastIntervention();
        Class interventionSelectorClass = Class.forName(lastInterventionClass);
        Constructor constructor = interventionSelectorClass.getConstructor(SessionManager.class);
        InterventionSelector isel = (InterventionSelector) constructor.newInstance(smgr);
        return isel;
    }
}
