package edu.umass.ckc.wo.tutor.intervSel2;

import ckc.servlet.servbase.ServletParams;
import ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.InterleavedTopicSwitchIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.interventions.TopicSwitchAskIntervention;
import edu.umass.ckc.wo.interventions.TopicSwitchIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.EndOfTopicInfo;
import edu.umass.ckc.wo.tutor.pedModel.InterleavedTopic;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/2/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicSwitchAskIS extends NextProblemInterventionSelector {

    private boolean ask=false;

    private static Logger logger = Logger.getLogger(TopicSwitchAskIS.class);

    public TopicSwitchAskIS(SessionManager smgr) {
        super(smgr);
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) throws Exception {
        this.pedagogicalModel=pedagogicalModel;
        configure(this.getConfigXML());
    }

    private void configure (Element configElt) throws Exception {
        if (configElt != null) {
            String b = getConfigParameter2("ask");
            if (b != null)
                this.ask = Boolean.parseBoolean(b);
            if (this.ask)
                throw new UserException("TopicSwitchAsk cannot function with ask=true because we can't stay in an exhausted topic if student does want to switch");
        }
    }

    @Override
    /**
     * We will pop up a dialog asking student if they want to move to new topic of stay in current one only
     * if the reasons for leaving are maxTime or maxProblems.   This is because we can reset counters/timers so that
     * student can stay in topic longer.   Other reasons for leaving topic (e.g. mastery, content failures) we cannot
     * do anything about so student is just given a message that we are moving to next topic.
     */
    /*
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        if (smgr.getStudentState().getCurTopic() < 1) {
            return null;
        }

        // I don't see why we need this test.   Grading the students last answer is sufficient
//        boolean topicContinues = pedagogicalModel.isLessonContentAvailable(smgr.getStudentState().getCurTopic()) ;
        boolean topicContinues = pedagogicalModel.getLessonModel().hasReadyContent(smgr.getStudentState().getCurTopic());
        NextProblemIntervention intervention = null;
        if (!topicContinues) {
            TopicModel tm = (TopicModel) pedagogicalModel.getLessonModel();
            ProblemScore lastProbScore = pedagogicalModel.getLastProblemScore();
            TopicModel.difficulty nextDiff = tm.getNextProblemDifficulty(lastProbScore);
            EndOfTopicInfo reasons = tm.isEndOfTopic(e.getProbElapsedTime(), nextDiff);
            if (reasons.isTopicDone()) {
                String expl = reasons.getExplanation();
                //
                // If configured to ask about staying in the topic and not a content failure, then pop up dialog asking if stay or switch topics.
                // Can only stay in the current topic if we have more content (i.e. maxProbs = true or maxTime has been reached)
                if (this.ask && !reasons.isContentFailure())
                    intervention = new TopicSwitchAskIntervention(expl,smgr.getSessionNum());
                // just inform that we are moving to next topic
                else intervention = new TopicSwitchIntervention(expl);
            }
            rememberInterventionSelector(this);
            return intervention;
        }
        else return null;
    }
    */

    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        if (smgr.getStudentState().getCurTopic() < 1) {
            return null;
        }
        // TODO As long as its END_OF_TOPIC this will be true and will return intervention
        // It has to shut off after playing at the end of a topic and then become possible
        // again in the next topic.

        // The TopicModel already did this test before switching to EndOfTopic, so know its
        // true.
        NextProblemIntervention intervention=null;
        TopicModel tm = (TopicModel) pedagogicalModel.getLessonModel();
        EndOfTopicInfo reasons = tm.getEndOfTopicInfo();
        if (reasons != null && reasons.isTopicDone()) {
            String expl = reasons.getExplanation();
            int seen = studentState.getTopicNumPracticeProbsSeen();
            int solved = studentState.getTopicProblemsSolved();
            // If configured to ask about staying in the topic and not a content failure, then pop up dialog asking if stay or switch topics.
            // Can only stay in the current topic if we have more content (i.e. maxProbs = true or maxTime has been reached)
            if (smgr.getStudentState().getCurTopic() == Settings.interleavedTopicID)  {
                intervention = getInterleavedTopicIntervention();

            }
            else if (this.ask && !reasons.isContentFailure())
                intervention = new TopicSwitchAskIntervention(expl,smgr.getSessionNum());
                // just inform that we are moving to next topic
            else intervention = new TopicSwitchIntervention(expl,seen,solved);
        }
        return intervention;
    }

    private NextProblemIntervention getInterleavedTopicIntervention() throws SQLException {
        InterleavedTopic interleavedTopic = new InterleavedTopic(smgr);
        List<String> reviewTopics = studentState.getReviewTopics();
        List<String> topicNames = new ArrayList<String>(reviewTopics.size());
        List<InterleavedTopic.TopicPerformance> topicPerformanceList = interleavedTopic.getStudentPerformance();
        NextProblemIntervention intervention = new InterleavedTopicSwitchIntervention(topicPerformanceList);
        return intervention;
    }




    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
//        TopicModel tm = (TopicModel) pedagogicalModel.getLessonModel();
//        tm.clearEndOfTopicInfo();
        if (true)
            throw new UserException("Should not be getting a timeout event in topic switch");
        return null;
    }

    // inherits selectIntervention which does a rememberIntervention.   I think this should remember the name of this class
    // and not the super class but maybe not.

    @Override
    /**
     * We've told the user that we want to switch topics and given him the option of staying in the current topic or moving to the
     * next one.   The parameters will be inside the event and will need to be retrieved.  Value of wantSwitch will either be
     *
     */
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        ServletParams params = e.getServletParams();
        // default is to switch
        String wantSwitch = params.getString(TopicSwitchAskIntervention.WANT_TO_SWITCH,TopicSwitchAskIntervention.SWITCH);
        // if they want to stay in the topic for either reason,  then we reset these counters/timers so they
        // can be in until content failure or mastery
        if (wantSwitch != null && wantSwitch.equals(TopicSwitchAskIntervention.STAY)) {
            smgr.getStudentState().setTimeInTopic(0);
            smgr.getStudentState().setTopicNumProbsSeen(1);  // set to one so that it won't play an example
            logger.debug("Topic Switch: Student elects to STAY in topic.  Turning off topicSwitch flag");
            smgr.getStudentState().setTopicSwitch(false);
            setUserInput(this,"<topicSwitch wantSwitch=\"" + wantSwitch + "\"/>",e);
            return null;  // no interventions - TODO we were in EndOfTopic and need to do something to return
        }
        else  {
//            logger.debug("Topic Switch: Student elects to SWITCH to new topic.");
//            return new BeginningOfTopicEvent(e,smgr.getStudentState().getCurTopic());
            TopicModel tm = (TopicModel) pedagogicalModel.getLessonModel();
            tm.clearEndOfTopicInfo();
            // At this point we generate an EndProblemEvent to end the last problem before going into the next topic
            EndProblemEvent epe = new EndProblemEvent(e.getServletParams());
            smgr.getPedagogicalModel().processEndProblemEvent(epe);
            return null;
        }


    }

}
