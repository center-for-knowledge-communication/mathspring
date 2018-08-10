package edu.umass.ckc.wo.tutor.probmode;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.HintSelector;

import java.util.List;
import java.util.Random;

/**
 * This thing was designed to select the mode of every problem that a problem selector chooses.   It does run this way.  BUT
 * it really only selects the mode of the FIRST problem delivered in the topic AFTER a Topic Intro is shown. In this situation
 * may or may not choose to return a problem in EXAMPLE mode (we call this DEMO mode in the new hybrid system).   It will only do this
 * if this object was constructed with the flag turning this feature on.
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2009
 * Time: 5:06:26 AM
 */
public class RandomProblemModeSelector implements ProblemModeSelector {
    private static Random random = new Random();
    private String[] notPlayableExamples = { } ;
//                                    "problem_075" ,
//                                     "problem_076",
//                                     "problem_077", "problem_078", "problem_079", "problem_063",
//                                     "problem_065", "problem_084", "problem_085", "problem_086",
//                                     "problem_087", "problem_088" } ;

    protected HintSelector hintSelector;
    protected SessionManager smgr;
    protected boolean showDemoProblemOnTopicStart;

    public RandomProblemModeSelector(SessionManager smgr, HintSelector hs, boolean showDemoProblemOnTopicStart) {
        this.hintSelector = hs;
        this.smgr = smgr;
        this.showDemoProblemOnTopicStart = showDemoProblemOnTopicStart;
    }

    private boolean isTopicIntroShown (StudentState state) {
        return state.isTopicIntroShown() && state.getTopicNumProbsSeen() == 1;
    }

    /**
     * For every problem but the first one in a topic,  it will be 'practice'.   The classic system also needs the mode
     * to be 4mality or external if the problem is one of those.   When the problem is the first one in the topic, it
     * may be returned in EXAMPLE (classic system) / DEMO (hybrid system) mode.
     * @param p
     * @param state
     * @return
     * @throws Exception
     */
    public String selectMode(Problem p, StudentState state) throws Exception {
        String mode = "practice";
//        if (random.nextInt(7) == 0 && isProblemPlayableAsExample(p))
//            mode = "example";

        if ( p!= null && p.getForm() != null && p.isExternalActivity())
            mode = Problem.EXTERNAL_MODE ;

        // TODO This is dependent on TopicIntro being seen first.   Some pedagogies might like no TopicIntro but examples first.
        // Need to make this condition know about that config.
        else if (this.showDemoProblemOnTopicStart && isTopicIntroShown(state))  {
            mode = Settings.useHybridTutor ? Problem.DEMO : Problem.EXAMPLE;
            hintSelector.init(smgr);
            List<Hint> soln = hintSelector.selectFullHintPath(smgr,p.getId());
            p.setSolution(soln);
        }
        
        return mode;
    }

    private boolean isProblemPlayableAsExample(Problem p) {

        for (int i=0; i<notPlayableExamples.length; i++)
            if ( notPlayableExamples[i].equalsIgnoreCase(p.getName()) )
                return false ;

        return true ;
    }
}
