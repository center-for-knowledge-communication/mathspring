package edu.umass.ckc.wo.tutor.model;



import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;

import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.internal.BeginningOfTopicEvent;
import edu.umass.ckc.wo.event.internal.EndOfTopicEvent;
import edu.umass.ckc.wo.event.internal.InTopicEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.DemoProblemIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;

import edu.umass.ckc.wo.state.TopicState;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutor.intervSel2.NextProblemInterventionSelector;
import edu.umass.ckc.wo.tutor.pedModel.*;
import edu.umass.ckc.wo.tutor.probSel.BaseProblemSelector;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutormeta.HintSelector;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;
import edu.umass.ckc.wo.tutormeta.TopicSelector;
import org.apache.log4j.Logger;


import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 2/27/15
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicModel extends LessonModel {
    private static Logger logger = Logger.getLogger(TopicModel.class);

    protected TopicSelector topicSelector;
    protected difficulty nextDiff;
    protected EndOfTopicInfo reasonsForEndOfTopic;
    protected HintSelector hintSelector;
    protected EndOfTopicInfo eotInfo;
    protected TopicModelParameters tmParams;


    /** This constructor is called initially just to get a pointer to the object.   Later we call a build method
     * to send it objects it needs to do its work.
     * @param smgr
     */
    public TopicModel (SessionManager smgr) {
        super(smgr);
    }


    public void init (SessionManager smgr, Pedagogy pedagogy,
                      PedagogicalModel pedagogicalModel, PedagogicalMoveListener pedagogicalMoveListener) throws Exception {

        // The superclass constructor calls readLessonControl which takes the control parameters out of the pedagogy and
        // builds the intervention handling for the model
        super.init(smgr, pedagogy, pedagogicalModel, pedagogicalMoveListener);
        this.tmParams = (TopicModelParameters) lmParams;
        hintSelector = pedagogicalModel.getHintSelector();
        topicSelector = new TopicSelectorImpl(smgr,tmParams);
        eotInfo = null;
    }




    private EndOfTopicInfo checkForEOT(long probElapsedTime) throws Exception {
        int curTopic = smgr.getStudentState().getCurTopic();
        // If in interleaved Topic, need to see if the list of problems is all shown.
        if (curTopic == Settings.interleavedTopicID) {
            boolean b =BaseProblemSelector.hasInterleavedProblem(smgr.getConnection(),smgr.getStudentId());
            if (!b)
                eotInfo = new EndOfTopicInfo(true,false,false,false,false,false);
            else
                eotInfo = new EndOfTopicInfo(false,false,false,false,false,false);
            return eotInfo;
        }

        // No interventions, so grade the last problem and if EOT, send an internal event for that
        ProblemGrader grader = pedagogicalModel.getProblemGrader();
        Problem lastProb = ProblemMgr.getProblem(smgr.getStudentState().getCurProblem());

        ProblemScore score;
        difficulty nextDiff;
        if (lastProb != null) {
            score = grader.gradePerformance(lastProb);
            nextDiff = getNextProblemDifficulty(score);
        }
        else nextDiff = difficulty.SAME;
        smgr.getStudentState().setNextProblemDesiredDifficulty(nextDiff.name());

        // now we need to use the score to find the desired difficulty of the next problem

        //we save this info so that interventions (TopicSwitchAskIS) can get it to determine if they are applicable
        eotInfo = isEndOfTopic(probElapsedTime,nextDiff);
        return eotInfo;
    }

    public EndOfTopicInfo getEndOfTopicInfo () {
        return eotInfo;
    }

    // Ugly.  The TopicSwitchAskIS needs a way to stop being applicable after its played.  Its processContinue method
    // calls this model back and has it remove the endOfTopic info that it relies on to be applicable.
    public void clearEndOfTopicInfo () {
        eotInfo = null;
    }


    /**
     * If IN_TOPIC:  If there is an intervention return it, else see if the topic is done and switch to END_TOPIC.  If topic
     * is not done, return null.
     * If BEGINNING_OF_TOPIC:  If there is an intervention, return it, else switch to IN-TOPIC
     * If END_OF_TOPIC:  If there is an intervention, return it, else pick the next topic and switch to BEGIN_TOPIC
     * @param e
     * @return
     * @throws Exception
     */
    public Response processInternalEvent (InternalEvent e) throws Exception {


        // This switches to the next topic
        if (e instanceof BeginningOfTopicEvent)
            return processBeginTopic((BeginningOfTopicEvent) e);

        else if (e instanceof EndOfTopicEvent) {
            return processEndOfTopic((EndOfTopicEvent) e);
        }
        else if (e instanceof InTopicEvent) {
            return processInTopic((InTopicEvent) e);
        }
        return null;
    }

    /**
     *
     * The event will contain the next topic (selected while processing EndOfTopic and then passed to this method through the event).
     * So if there is a change of topics happening, make the state changes so that we are in the next topic
     * Then see if any interventions apply, and return one if there is
     * If no interventions, then switch to IN_TOPIC and process it.
     * @param e
     * @return
     * @throws Exception
     */
    private Response processBeginTopic (BeginningOfTopicEvent e) throws Exception {
        studentState.setTopicInternalState(TopicState.BEGINNING_OF_TOPIC);
        int curTopic = studentState.getCurTopic();
        int nextTopic = e.getTopicId();

        if (curTopic == -1)  {
            // If the BeginningOfTopicEvent has a next topic, then we want to go to it because MPP selection of a topic at the beginning of a session
            // will send in a topic here where the curTopic is -1; otherwise we will select the next one
            if (nextTopic == -1)
                curTopic =  topicSelector.getNextTopicWithAvailableProblems(smgr.getConnection(), curTopic, smgr.getStudentState(), smgr.getStudentModel());
            else curTopic = nextTopic;
            curTopic = switchTopics(curTopic);

        }
        // curTopic and nextTopic will be different if we're coming from EndOfTopic processing where we've just passed in the next topic
        // to work on
        else if (nextTopic != -1 && curTopic != nextTopic) {
            curTopic = switchTopics(nextTopic);
        }
        else {
            // Prevent starting a topic that has no problems.  This can happen if a student logs out after solving all problems in a topic because
            // we attempt to resume the last topic a student was in.
            // We won't go into a topic that has 1 or less problems available because 1 problem would probably get used for a demo and then there would be none.
            List<Integer> probs = getUnsolvedProblems();
            if (probs == null || probs.size() <= 1)  {
                curTopic =  topicSelector.getNextTopicWithAvailableProblems(smgr.getConnection(), curTopic, smgr.getStudentState(), smgr.getStudentModel());
                if (curTopic != -1)
                    curTopic = switchTopics(curTopic);
            }
        }

        // if there is no current topic we must be at the beginning of the session so get a new topic and switch to it.
        Response r;
        //// See if there are interventions applicable for BeginningOfTopic
        r = super.processInternalEvent(e); // gets intervention that is highest ranked for this InternalEvent

        if (r != null) {
            return r;
        }
        // If we couldn't switch to a new topic and no interventions were applicable, return no-more-problems
        if (curTopic == -1)
            return ProblemResponse.NO_MORE_PROBLEMS;
        return processInternalEvent(new InTopicEvent(e.getSessionEvent(),studentState.getCurTopic()));

    }


    /**
     * If IN_TOPIC:  see if there are interventions and return one if so,
     * o/w see if we have reached the end of the current topic and if so, switch to END_OF_TOPIC
     * and process that event
     * If none of the above, return null to indicate that this model has nothing it wants to do.
     * @param e
     * @return
     * @throws Exception
     */
    private Response processInTopic (InTopicEvent e) throws Exception {
        Response r;
        studentState.setTopicInternalState(TopicState.IN_TOPIC);

        //// See if there are interventions applicable for InTopic
        r = super.processInternalEvent(e); // gets intervention that is highest ranked for this InternalEvent
        if (r != null) {
            return r;
        }
        IntraProblemEvent ipe = (IntraProblemEvent) e.getSessionEvent();
        long probElapsed = ipe.getProbElapsedTime();
        // If in an interleaved topic that is all out of problems, end the topic.
//        if (isInInterleavedTopic()) {
//            // TODO does this generate errors when this "topic" ends and it tries to show interventions like "moving to another topic because...."
//            List<Integer> pids = DbTopics.getNonShownInterleavedProblemSetProbs(smgr.getConnection(), smgr.getStudentId()) ;
//            if (pids.size() == 0)
//                return this.processInternalEvent(new EndOfTopicEvent(e.getSessionEvent(),studentState.getCurTopic()));
//        }
        EndOfTopicInfo eot = checkForEOT(probElapsed);

        if (eot.isTopicDone())   {
            // So we need send ourselves an EndOfTopicEvent
            return this.processInternalEvent(new EndOfTopicEvent(e.getSessionEvent(), studentState.getCurTopic()));
        }
        else return null;
    }


    /**
     * See if we have any interventions that apply to EndOfTopic.   Return it if so
     * If no interventions, select the next topic and then switch to BeginningOFTopic and process that by
     * passing the next topic that was selected.
     * @param e
     * @return
     * @throws Exception
     */
    private Response processEndOfTopic(EndOfTopicEvent e) throws Exception {
        studentState.setTopicInternalState(TopicState.END_OF_TOPIC);
        // Find an intervention that applies to EndOfTopic
        Response r = super.processInternalEvent(e);
        // r == null means we have no interventions about end of topic and we move on to BeginTopic
        if (r == null) {
            // cleans up any remaining state in the topicState (will also be initialized at BeginTopic time)
            studentState.topicDone();
            // At this point we are done doing anything for EndOfTopic and will now move to BeginningOfTopic.
            // We get the next Topic ID and send it to the processBeginTopic method so that it can see that it was passed a different
            // topic than what is still the current topic.
            int curTopic=studentState.getCurTopic();
            int nextTopic =  topicSelector.getNextTopicWithAvailableProblems(smgr.getConnection(), curTopic, studentState, studentModel);
            if (nextTopic == -1)
                return ProblemResponse.NO_MORE_PROBLEMS;
//            studentState.setCurTopic(nextTopic);
            return processInternalEvent(new BeginningOfTopicEvent(e.getSessionEvent(),nextTopic));
        }
        return r;

    }

    /**
     * Checks to see what internal state the topic is in and processes according to that.
     * If IN_TOPIC:  see if there are interventions and return one if so, o/w return null
     * If BEGINNING_OF_TOPIC:  see if there are interventions and return it if so, o/w switch to IN_TOPIC and process it
     * If END_OF_TOPIC: find an intervention, if not get the next topic, switch to BEGINNING_OF_TOPIC and process it
     * @param e
     * @return
     * @throws Exception
     */
    private Response processNextProblemEvent (NextProblemEvent e) throws Exception {
        Response r;
        // a students first session will not have a topic in the student state so grab the first one
        // TODO If a student has been in a topic in a previous session and it had no more problems,  this will
        // resume in that topic and then fail to get beyond the initial Topic intro.   Need to switch topics.
        if (studentState.getCurTopic() == -1) {
            int nextTopic =  topicSelector.getNextTopicWithAvailableProblems(smgr.getConnection(), -1, studentState, studentModel);
            switchTopics(nextTopic);
        }
        // if this is a new session with a topic left from the last session
        else if (studentState.getCurTopic() != -1 && studentState.getNumProblemsThisTutorSession() < 1)
            System.out.println("Going to resume a topic from last session");
        // If the current state is BeginningOfTopic, then send an BOT internal event to get interventions for that if any
        if (studentState.getTopicInternalState().equals(TopicState.BEGINNING_OF_TOPIC))  {
            return processInternalEvent(new BeginningOfTopicEvent(e,studentState.getCurTopic()));
        }
        // If the current state is EndOfTopic, send EOT internal event to get interventions for that if any
        else if (studentState.getTopicInternalState().equals(TopicState.END_OF_TOPIC))  {
            return processInternalEvent(new EndOfTopicEvent(e, studentState.getCurTopic()));
        }
        else if (studentState.getTopicInternalState().equals(TopicState.IN_TOPIC)) {
            return  processInternalEvent(new InTopicEvent(e,studentState.getCurTopic()));
        }
        return null;

    }

    @Override
    public Response processUserEvent(TutorHutEvent e) throws Exception {
        // Called at the beginning of processing a NextProblem Event.  If the topic is done for any reason,
        // this returns the EndOfTopic event.
        if (e instanceof NextProblemEvent) {
            return processNextProblemEvent( (NextProblemEvent) e);

        }
        // If this model didn't generate the intervention, return null so the pedagogical model can process it.
        // If this model did generate it, process it and if that results in an internal event, process the internal event
        // If it results in nothing, then let this model process this event as if it were a nextProblemEvent
        else if (e instanceof InputResponseNextProblemInterventionEvent) {
            String lastInterventionClass = smgr.getStudentState().getLastIntervention();
            InterventionSelectorSpec spec = interventionGroup.getInterventionSelectorSpec(lastInterventionClass);
            // If the last intervention is not found in this model's list of interventions, it isn't relevant to this model
            // so we return null
            if (spec == null)
                return null;
            NextProblemInterventionSelector intSel = (NextProblemInterventionSelector) spec.buildIS(smgr);
//            NextProblemInterventionSelector intSel = (NextProblemInterventionSelector) new TutorModelUtils().getLastInterventionSelector(smgr);
            // The IS builds an XML string that represents the user's input to the intervention.  This is saved in the IS when it gets the input.
            // Now we transfer that XML to the event so that the logger can get it from the event and put it into the userInput field of eventlog
            ((InputResponseNextProblemInterventionEvent) e).setUserInput(intSel.getUserInputXML());

            intSel.init(smgr,pedagogicalModel);

            Response r = intSel.processInputResponseNextProblemInterventionEvent((InputResponseNextProblemInterventionEvent) e);
            // No state change, so process this just like a NextProblem event now
            if (r == null)  {
                // N.B. This can return null if no interventions and internal state = IN_TOPIC
                r = processNextProblemEvent(new NextProblemEvent(e.getServletParams()));
                // If this model processed the event, but there is nothing it wants to return, we need to return an indicator
                // that the intervention was processed here so the pedagogical model won't try to .
                if (r == null)
                    return new InterventionInputProcessed(e);
                else return r;
            }
            // The intervention selector returned an internal event so process that.
            else if (r instanceof InternalEvent)
                return this.processInternalEvent((InternalEvent) r);
        }


        return null;

    }





    /**
     * Once we are truly ready to begin the nextTopic switchTopics initializes the state variables associated with the current topic.
     * @param nextTopic
     * @return
     * @throws Exception
     */
    protected int switchTopics (int nextTopic) throws Exception {
        smgr.getStudentState().newTopic();
        if (nextTopic != -1)
            pedagogicalMoveListener.newTopic(ProblemMgr.getTopic(nextTopic)); // inform pedagogical move listeners of topic switch
        //If the topic we are switching to is an interleaved problem set, we need to select the problems now
        topicSelector.initializeTopic(nextTopic, smgr.getStudentState(), smgr.getStudentModel());
        smgr.getStudentState().setCurTopic(nextTopic);

        return nextTopic;
    }





    public EndOfTopicInfo isEndOfTopic(long probElapsedTime, LessonModel.difficulty difficulty) throws Exception {
        return topicSelector.isEndOfTopic(probElapsedTime,difficulty);
    }







    public boolean isInInterleavedTopic () throws SQLException {
        return studentState.getCurTopic() == Settings.interleavedTopicID;
    }


    public boolean hasReadyContent(int lessonId) throws Exception {
        return topicSelector.hasReadyContent(lessonId); // lessonId is a topic ID
    }

    public List<Integer> getUnsolvedProblems() throws Exception {
        int classId = smgr.getClassID();
        int topicId = smgr.getStudentState().getCurTopic();
        boolean includeTestProbs =  DbUser.isShowTestControls(smgr.getConnection(), smgr.getStudentId());
        return topicSelector.getUnsolvedProblems(topicId, classId, includeTestProbs);
    }

    public List<Integer> getClassTopicProblems(int topicId, int classId, boolean includeTestProblems) throws Exception {
        return topicSelector.getClassTopicProblems(topicId, classId, includeTestProblems);
    }


    public TopicSelector getTopicSelector() {
        return topicSelector;
    }

    @Override
    protected Response buildInterventionResponse (Intervention interv) throws Exception {
        if (interv == null)
            return null;

        else if (interv instanceof DemoProblemIntervention)   {
            DemoResponse r= new DemoResponse(((DemoProblemIntervention) interv).getProblem());
//                    r.setProblemBindings(smgr);
            pedagogicalMoveListener.exampleGiven(r.getProblem());
            return r;
        }
        else return new InterventionResponse(interv);
    }

    public TopicModelParameters getTmParams() {
        return tmParams;
    }

    public HintSelector getHintSelector() {
        return hintSelector;
    }

    public Class getLessonModelParametersClass () {
        return TopicModelParameters.class;
    }

}
