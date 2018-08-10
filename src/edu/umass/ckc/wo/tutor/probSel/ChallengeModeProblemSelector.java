package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.util.Lists;
import org.apache.log4j.Logger;

import java.util.*;

/**
 *
 * This problem selector selects problems from a single topic after the user has elected to work on that topic in "challenge"
 * mode.   It uses problems the student has not correctly solved in the past, has skipped, and those that have difficulty rating
 * that is at least 75% of the students topic mastery.
 *
 * When this runs out of problems it returns null.   The pedagogy can then decide what to do next (e.g. go to the next topic,
 * go back to a sidelined topic, or return an intervention asking the user to go to MPP and select another topic)
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 11/19/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChallengeModeProblemSelector extends BaseProblemSelector {
    public static final String END_PAGE = "challengeDone.html";
    private static Logger logger = Logger.getLogger(ReviewModeProblemSelector.class);



    public ChallengeModeProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        super(smgr,lessonModel,params);
    }


    @Override
    public void setParameters(PedagogicalModelParameters params) {
        this.parameters = params;
    }

    @Override
    public Problem selectProblem(SessionManager smgr, NextProblemEvent eIgnored, ProblemScore lastProblemScoreIgnored) throws Exception {
        StudentState state = smgr.getStudentState();
        List<Integer> topicProbIds = topicModel.getUnsolvedProblems();
        if (topicProbIds.size() == 0)
            return null;
        int nextIx = state.getCurProblemIndexInTopic();
        // THIS IS FAILING BECUASE IF They solve then you don't want to increase the index because the solved problem is thrown out
        // if they don't solve we want to increase the index.
        if (nextIx == -1)  {
           nextIx = topicProbIds.size()/2;
           state.setCurProblemIndexInTopic(nextIx);
        }
        if (nextIx >= topicProbIds.size()) {
            state.setInChallengeMode(false);
            return null;
        }
        // If the last problem given is the same as this one,  then the user must have got it wrong because it wasn't removed in the
        // prepare step above.   So we increase the index
        if (state.getCurProblem() == topicProbIds.get(nextIx)) {
            nextIx++;
            if (nextIx >= topicProbIds.size() )
            {
                state.setInChallengeMode(false);
                return null;
            }
        }

        int nextProbId = topicProbIds.get(nextIx);
        // 2/18 Ugly shit to deal with broken problems in the topic.
        // now make sure the problem isn't among the broken ones and move to the next one if so.
        // Jumps over broken problems.
        while (Lists.inList(nextProbId,state.getBrokenProblemIds())) {
            nextIx++;
            if (nextIx >= topicProbIds.size()) {
                state.setInChallengeMode(false);
                return null;
            }
            nextProbId = topicProbIds.get(nextIx);
        }

        state.setCurProblemIndexInTopic(nextIx); // save the index of the place we are in.

        Problem p = ProblemMgr.getProblem(nextProbId);
        p.setMode(Problem.PRACTICE);
        return p;
    }



}
