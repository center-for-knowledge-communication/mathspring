package edu.umass.ckc.wo.tutor.switcher;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.DynamicPedagogy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutor.model.InterventionGroup;
import edu.umass.ckc.wo.tutormeta.Switcher;

import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 10/2/15
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomSwitcher extends Switcher {

    private String actionTaken = "No change taken since last report";

    public RandomSwitcher(SessionManager smgr, DynamicPedagogy ped, InterventionGroup intervs){
        super(smgr, ped, intervs);
    }

    @Override
    public boolean checkConditions() {
        if(smgr.getTimeInSession() - smgr.getStudentState().getTimeLastChange() > 60000)
            return true;
        return false;
    }

    @Override
    public void doChange() {
        Random rand = new Random();
        String elementSwitched = null;
        String eltChanged = "None";
        while(elementSwitched == null){
            //TODO Compatibility checking: ask for list of dependencies and send them as parameters to the change methods
            switch (rand.nextInt(7)){
                case 1:
                    elementSwitched = ped.changeChallengeModeProblemSelectorClass();
                    eltChanged = "ChallengeModeSelector";
                    break;
                case 2:
                    elementSwitched = ped.changeHintSelectorClass();
                    eltChanged = "HintSelector";
                    break;
                case 3:
                    elementSwitched = ped.changeLearningCompanionClass();
                    eltChanged = "LearningCompanion";
                    break;
                case 4:
                    elementSwitched = ped.changeReviewModeProblemSelectorClass();
                    eltChanged = "ReviewModeProblemSelector";
                    break;
                case 5:
                    elementSwitched = ped.changeProblemSelectorClass();
                    eltChanged = "ProblemSelector";
                    break;
                case 6:
                    elementSwitched = ped.changeStudentModelClass();
                    eltChanged = "StudentModel";
                    break;
                default:
                    if (intervs != null){
                        List<InterventionSelectorSpec> specs = intervs.getInterventionsSpecs();
                        int toSwitch = rand.nextInt(specs.size());
                        InterventionSelectorSpec candidate = specs.get(toSwitch);
                        if(candidate.getTurnedOn()){
                            candidate.setTurnedOn(false);
                        }
                        else{
                            candidate.setTurnedOn(true);
                        }
                        elementSwitched = candidate.getClassName();
                        eltChanged = "Intervention:"+ candidate.getClassName()+" originally, "+ !candidate.getTurnedOn();
                    }
                    else{
                        elementSwitched = null;
                    }
                    break;
            }

        }
        actionTaken = eltChanged + " was changed to: " + elementSwitched;
    }

    @Override
    public void doRelatedTasks() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString(){
        return actionTaken;
    }
}
