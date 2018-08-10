package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.smgr.TestSessionManager;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 4/7/14
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Test() public class TestTopicSelectorImpl {
    Connection conn;
    TopicSelectorImpl topicSelector;
    SessionManager smgr;
    StudentState studState;
    PedagogicalModelParameters params;

    @BeforeClass
    public void setUp () {
        try {
            TestSessionManager tsmgr = new TestSessionManager();
            smgr = tsmgr.setUpTestSession();
            conn = smgr.getConnection();
            params = smgr.getPedagogicalModel().getParams();
            studState = smgr.getStudentState();
            // TODO need to create some TopicModelParameters instead of null
            topicSelector = new TopicSelectorImpl(smgr,null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @AfterClass
    public void tearDown () throws Exception {
        // get rid of anything added to the db
        smgr.removeTestSessionData();
    }

    /**
     * Test if TopicSelectorImpl.isEndOfTopic performs correctly based on time in the topic.
     * @throws Exception
     */
    @Test()
    public void testIsEndOfTopicForTimeThreshold() throws Exception {
        long maxTime = params.getMaxTimeInTopic();
        int maxNumProbs = params.getMaxNumberProbs();
        studState.setTopicNumPracticeProbsSeen(maxNumProbs -1 );  // seen less than max num problems
        studState.setTimeInTopic(maxTime-1000); // been in topic for 1 sec less than max time
        TopicModel.difficulty nextDiff = LessonModel.difficulty.HARDER;

        EndOfTopicInfo info = topicSelector.isEndOfTopic(1000,nextDiff);  // see if its time to switch at probElapsed=1 sec
        Assert.assertTrue(info.isMaxTimeReached(), "End of topic when time in topic == MAXTIME");
        info = topicSelector.isEndOfTopic(0,nextDiff);  // see if its time to switch at probElapsed=0 sec
        Assert.assertFalse(info.isMaxTimeReached(), "Not End of topic when time < MAXTIME");
        info = topicSelector.isEndOfTopic(2000,nextDiff);  // see if its time to switch at probElapsed=2 sec
        Assert.assertTrue(info.isMaxTimeReached(), "End of topic when time > MAXTIME");
    }

    /**
     * Test if TopicSelectorImpl.isEndOfTopic performs correctly based on number of problems seen
     * @throws Exception
     */
    @Test()
    public void testIsEndOfTopicForNumProbsThreshold() throws Exception {
        long maxTime = params.getMaxTimeInTopic();
        int maxNumProbs = params.getMaxNumberProbs();
        studState.setTimeInTopic(maxTime-1000); // been in topic for 1 sec less than max time
        TopicModel.difficulty nextDiff = LessonModel.difficulty.HARDER;

        studState.setTopicNumPracticeProbsSeen(maxNumProbs -1 );  // seen 1 less than max num problems
        EndOfTopicInfo info = topicSelector.isEndOfTopic(0,nextDiff);  // see if its time to switch at probElapsed=1 sec
        Assert.assertFalse(info.isMaxProbsReached(), "Not End of topic when num probs < MAX PROBS");

        studState.setTopicNumPracticeProbsSeen(maxNumProbs );  // seen exactly the max num problems
        info = topicSelector.isEndOfTopic(0,nextDiff);  // see if its time to switch at probElapsed=0 sec
        Assert.assertTrue(info.isMaxProbsReached(),  "End of topic when num probs == MAX PROBS");

        studState.setTopicNumPracticeProbsSeen(maxNumProbs + 1 );  // seen 1 more than max num problems
        info = topicSelector.isEndOfTopic(0,nextDiff);  // see if its time to switch at probElapsed=2 sec
        Assert.assertTrue(info.isMaxProbsReached(), "End of topic when num probs > MAX PROBS");
    }

    /**
     * Test if TopicSelectorImpl.isEndOfTopic performs correctly based content failures
     * @throws Exception
     */
    @Test()
    public void testIsEndOfTopicForContentFailure() throws Exception {
        long maxTime = params.getMaxTimeInTopic();
        int maxNumProbs = params.getMaxNumberProbs();
        studState.setTimeInTopic(maxTime-1000); // been in topic for 1 sec less than max time
        studState.setTopicNumPracticeProbsSeen(maxNumProbs -1 );  // seen 1 less than max num problems

        smgr.getStudentState().setCurTopicHasEasierProblem(true);
        smgr.getStudentState().setCurTopicHasHarderProblem(true);
        EndOfTopicInfo info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.HARDER);  // want harder prob
        Assert.assertFalse(info.isContentFailure(), "test content failure: has easier & harder, want harder");
        Assert.assertFalse(info.isFailToFindHarder(), "test failToFindHarder: has easier & harder, want harder");

        info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.EASIER);  // want easier prob
        Assert.assertFalse(info.isContentFailure(), "test content failure: has easier & harder, want easier");
        Assert.assertFalse(info.isFailToFindEasier(), "test failToFindHarder: has easier & harder, want easier");

        info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.SAME);  // want same diff prob
        Assert.assertFalse(info.isContentFailure(), "test content failure: has easier & harder, want same");
        Assert.assertFalse(info.isFailToFindSame(), "test failToFindHarder: has easier & harder, want same");

        smgr.getStudentState().setCurTopicHasEasierProblem(false);
        smgr.getStudentState().setCurTopicHasHarderProblem(false);

        info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.HARDER);  // want harder prob
        Assert.assertTrue(info.isContentFailure(), "test content failure: no easier & harder, want harder");
        Assert.assertTrue(info.isFailToFindHarder(), "test failToFindHarder: no easier & harder, want harder");

        info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.EASIER);  // want easier prob
        Assert.assertTrue(info.isContentFailure(), "test content failure: no easier & harder, want easier");
        Assert.assertTrue(info.isFailToFindEasier(), "test failToFindHarder: no easier & harder, want easier");

        info = topicSelector.isEndOfTopic(0, LessonModel.difficulty.SAME);  // want same diff prob
        Assert.assertTrue(info.isContentFailure(), "test content failure: no easier & harder, want same");
        Assert.assertTrue(info.isFailToFindSame(), "test failToFindHarder: no easier & harder, want same");


    }


}
