package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutormeta.ProblemSelector;
import edu.umass.ckc.wo.util.Lists;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/26/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReviewModeProblemSelector  implements ProblemSelector {

    public static final String END_PAGE = "reviewDone.html";
    private static Logger logger = Logger.getLogger(ReviewModeProblemSelector.class);

    PedagogicalModelParameters parameters;
    private TopicModel topicModel;

    private SessionManager smgr;

    public ReviewModeProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        this.smgr = smgr;
        this.topicModel = (TopicModel) lessonModel;
        this.parameters=params;
    }


    @Override
    public void setParameters(PedagogicalModelParameters params) {
        this.parameters = params;
    }

    @Override
    public Problem selectProblem(SessionManager smgr, NextProblemEvent eIgnored, ProblemScore lastProblemScoreIgnored) throws Exception {
        StudentState state = smgr.getStudentState();
        List<Integer> topicProbIds = topicModel.getClassTopicProblems(state.getCurTopic(), smgr.getClassID(), DbUser.isShowTestControls(smgr.getConnection(), smgr.getStudentId()));
//        List<Problem> topicProblems = xx;


        int nextIx = state.getCurProblemIndexInTopic();
        nextIx++;
        if (nextIx >= topicProbIds.size()) {
            state.setInReviewMode(false);
            return null;
        }
        state.setCurProblemIndexInTopic(nextIx);
        int nextProbId = topicProbIds.get(nextIx);
        // 2/18 Ugly shit to deal with broken problems in the topic.
        // now make sure the problem isn't among the broken ones and move to the next one if so.
        while (Lists.inList(nextProbId,state.getBrokenProblemIds())) {
            nextIx++;
            if (nextIx >= topicProbIds.size()) {
                state.setInReviewMode(false);
                return null;
            }
            nextProbId = topicProbIds.get(nextIx);
        }
        Problem p = ProblemMgr.getProblem(nextProbId);
        p.setMode(Problem.PRACTICE);
        return p;
    }




}
