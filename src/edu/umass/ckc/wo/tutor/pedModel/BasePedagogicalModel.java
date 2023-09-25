package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.LCChange;
import edu.umass.ckc.wo.content.LCList;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.content.Video;

import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.AskGoalsIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.agent.RuleDrivenLearningCompanion;
import edu.umass.ckc.wo.tutor.intervSel2.AttemptInterventionSelector;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelector;
import edu.umass.ckc.wo.tutor.intervSel2.NextProblemInterventionSelector;
import edu.umass.ckc.wo.tutor.model.*;
import edu.umass.ckc.wo.tutor.probSel.*;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import edu.umass.ckc.wo.tutormeta.*;
import edu.umass.ckc.wo.db.DbGaze;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUser;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * The core of the tutor.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 * Frank	06-26-2021	added support for gaze detection
 * Frank	07-03-21	added logging to evemtLog and updating gazewanderingevents with reciprocal links
 * Frank	05-13-23	temporary fix for multi-lingual study
 */
public class BasePedagogicalModel extends PedagogicalModel implements PedagogicalMoveListener {

    private static Logger logger = Logger.getLogger(BasePedagogicalModel.class);
//    protected TopicSelector topicSelector;
//    TopicModel.difficulty nextDiff;
    List<PedagogicalMoveListener> pedagogicalMoveListeners;


    public BasePedagogicalModel() {
    	logger.setLevel(Level.DEBUG);
    }



    public BasePedagogicalModel (SessionManager smgr, Pedagogy pedagogy) throws SQLException {
        this.pedagogy = pedagogy;
        setSmgr(smgr);
        smgr.setPedagogicalModel(this); // do this so that sub-components can get the pedagogical model thru smgr rather than passing this
        setTutorModel(new TutorModel(smgr));
        pedagogicalMoveListeners = new ArrayList<PedagogicalMoveListener>();
        if (pedagogy instanceof TutorStrategy)
            params = getPedagogicalModelParametersFromStrategy((TutorStrategy) pedagogy);
        else
            params = getPedagogicalModelParametersForUser(smgr.getConnection(),pedagogy,smgr.getClassID(),smgr.getStudentId());
//        lessonModelParameters = getLessonModelParametersForUser(smgr.getConnection(),pedagogy,smgr.getClassID(),smgr.getStudentId());
        buildComponents(smgr,pedagogy);
    }

    protected void buildComponents (SessionManager smgr, Pedagogy pedagogy) {
        try {

            problemGrader = new ProblemGrader(smgr);
            setExampleSelector(new BaseExampleSelector());
            setVideoSelector(new BaseVideoSelector());
            // its simpler to build the lesson model if we are using a TutorStrategy
            if (pedagogy instanceof TutorStrategy)
                lessonModel =  LessonModel.buildModel(smgr, (TutorStrategy) pedagogy);
            else
                // Use the params from the pedagogy and then overwrite any values with things that are set up for the class
                // need a pointer to the lessonModel object to pass into the various components.
                lessonModel =  LessonModel.buildModel(smgr, pedagogy,smgr.getClassID(),smgr.getStudentId());
            this.getTutorModel().setLessonModel(lessonModel);
            // no longer build the student model here.   Its constructed in the Smgr prior to calling the PedModel constructor
//            setStudentModel((StudentModel) Class.forName(pedagogy.getStudentModelClass()).getConstructor(SessionManager.class).newInstance(smgr));
            setStudentModel(smgr.getStudentModel());
            setProblemSelector((ProblemSelector) Class.forName(pedagogy.getProblemSelectorClass()).getConstructor(SessionManager.class, LessonModel.class, PedagogicalModelParameters.class).newInstance(smgr, lessonModel, params));
            if (pedagogy.getReviewModeProblemSelectorClass() != null)
                setReviewModeProblemSelector((ReviewModeProblemSelector) Class.forName(pedagogy.getReviewModeProblemSelectorClass()).getConstructor(SessionManager.class, LessonModel.class, PedagogicalModelParameters.class).newInstance(smgr, lessonModel, params));
            if (pedagogy.getChallengeModeProblemSelectorClass() != null)
                setChallengeModeProblemSelector((ChallengeModeProblemSelector) Class.forName(pedagogy.getChallengeModeProblemSelectorClass()).getConstructor(SessionManager.class, LessonModel.class, PedagogicalModelParameters.class).newInstance(smgr, lessonModel, params));
            setHintSelector((HintSelector) Class.forName( pedagogy.getHintSelectorClass()).getConstructor().newInstance());

            if (pedagogy.hasRuleset())   {
                setLearningCompanion((LearningCompanion) Class.forName( pedagogy.getLearningCompanionClass()).getConstructor(SessionManager.class).newInstance(smgr));
                if (pedagogy.hasRuleset()) {
                    String characterName = pedagogy.getLearningCompanionCharacter();
                    ((RuleDrivenLearningCompanion) learningCompanion).setCharactersName(characterName);
                    ((RuleDrivenLearningCompanion) learningCompanion).setRuleSets(pedagogy.getLearningCompanionRuleSets());
                }
            }
            else if (pedagogy.getLearningCompanionClass() != null){
                setLearningCompanion((LearningCompanion) Class.forName( pedagogy.getLearningCompanionClass()).getConstructor(SessionManager.class).newInstance(smgr));

            }


//            if (pedagogy.getNextProblemInterventionSelector() != null)
//                setNextProblemInterventionSelector(buildNextProblemIS(smgr, pedagogy));
//            if (pedagogy.getAttemptInterventionSelector() != null)
//                setAttemptInterventionSelector(buildAttemptIS(smgr, pedagogy));
            if (pedagogy instanceof TutorStrategy) {
                // build the interventions for the tutor-strategyComponent
                interventionGroup = new InterventionGroup(((TutorStrategy) pedagogy).getTutor_sc().getInterventionSelectors());
                this.interventionGroup.buildInterventions(smgr,this);
            }
            else {
                this.interventionGroup = new InterventionGroup(pedagogy.getInterventionsElement());
                this.interventionGroup.buildInterventions(smgr,this);

            }
            lessonModel.init(smgr,pedagogy,this,this);

        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Temp lates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }




    public void addPedagogicalMoveListener (PedagogicalMoveListener listener) {
        this.pedagogicalMoveListeners.add(listener);
    }






    /**
     * Internal events are first referred to the lesson model.  If it has an intervention, then it is returned.
     * If not, then we see if there is an intervention for that event at this level.   Failing that we return a problem.
     * @param ev
     * @return
     */
    public Response processInternalEvent (InternalEvent ev) throws Exception {
        Response r=null;
        StudentState state = smgr.getStudentState();
        // lesson model process internal event
        r = lessonModel.processInternalEvent(ev);
        // try to process the internval event at this level and get an intervention
        if (r == null) {
            Intervention intervention = interventionGroup.selectIntervention(smgr,ev.getSessionEvent(),ev.getOnEventName());
            if (intervention != null) {
                interventionGiven(intervention); // tell pedagogical move listeners that an intervention is given
                InterventionResponse ir;
                if (intervention instanceof NextProblemIntervention)
                {
                    ir=  new NextProblemInterventionResponse(intervention);
                    boolean isbuildProb = ((NextProblemIntervention) intervention).isBuildProblem();
                    ((NextProblemInterventionResponse) ir).setBuildProblem(isbuildProb);
                    smgr.getStudentModel().interventionGiven(state,intervention);
                }
                else {
                    ir = new InterventionResponse(intervention);
                    smgr.getStudentModel().interventionGiven(state,intervention);
                }
                // A hack because we need to determine if the Intervention returned should also build a Problem.   So we have to see if
                // the intervention is a NextProblemIntervention and then ask if it wants a problem built.   The only case of this currently
                // is the intervention which turns on/off MPP which at same time should show a new problem

                return ir;
            }
        }
        // process at this level and return a problem
        if (r == null) {

            int lastProbId =  state.getCurProblem();  // must do this before processing the event because it might clear curProb
            // have to regrade last problem so that we can select a problem
            gradeLastProblem();

            r = getNextProblem(null);
            ProblemResponse pr = (ProblemResponse) r;
            Problem p = pr.getProblem();
            studentModel.newProblem(state,p);  // this does not set curProb = new prob id,
            state.setCurProblem(lastProbId);  // must make curProb be lastProb id so EndProblem event that comes in next has the id of last problem

        }
        return r;
    }

    @Override
    /**
     * If there is an attempt intervention selected by the attempt intervention selector, run that intervention.
     * Otherwise, simply grade the problem, update the student model and return.
     */
    public Response processAttempt(AttemptEvent e) throws Exception {
        boolean isCorrect = new ProblemGrader(smgr).isAttemptCorrect(smgr.getStudentState().getCurProblem(), e.getUserInput());
        e.setCorrect(isCorrect);
        // first update the student model so that intervention selectors have access to latest stats based on this attempt
        studentModel.studentAttempt(smgr.getStudentState(), e.getUserInput(), isCorrect, e.getProbElapsedTime());

        AttemptResponse r = (AttemptResponse) lessonModel.processUserEvent(e); // this should always return null

        Intervention intervention=null;
        if (r == null )  {
            intervention = interventionGroup.selectIntervention(smgr,e,"Attempt");
            if (intervention != null)
                interventionGiven(intervention); // inform pedagogical move listeners that an intervention is given

        }
        // No interventions
        if (intervention == null) {
//            studentModel.studentAttempt(smgr.getStudentState(), e.getUserInput(), isCorrect, e.getProbElapsedTime());
            r = new AttemptResponse(isCorrect, studentModel.getTopicMasteries(),smgr.getStudentState().getCurTopic());
        }
        else if (r == null) {
            // record this attempt.  We will need to send back information about its correctness
            // once the interventions are done.
            attemptGraded(isCorrect); // inform pedagogical move listeners that an intervention is given
            r = new AttemptResponse(isCorrect,intervention, studentModel.getTopicMasteries(),
                    smgr.getStudentState().getCurTopic()) ;
        }

        if (learningCompanion != null )
            learningCompanion.processAttempt(smgr,e, r);
        new TutorLogger(smgr).logAttempt(e, r);
        return r;
    }

    @Override
    public Response processBeginProblemEvent(BeginProblemEvent e) throws Exception {
        long t = System.currentTimeMillis();
        Response r = new Response();
        this.studentModel.beginProblem(smgr, e); // this sets state.curProb to the new probid
//        System.out.println("After StudentModel.beginProblem " + (System.currentTimeMillis() - t));
//        System.out.println("After TutorLogger.beginProblem " + (System.currentTimeMillis() - t));

        if (learningCompanion != null )   {
            learningCompanion.processBeginProblem(smgr, (BeginProblemEvent) e,r);
//            System.out.println("After learningCompanion.processUncategorizedEvent " + (System.currentTimeMillis() - t));

        }
        new TutorLogger(smgr).logBeginProblem(e, r);  // this relies on the above being done first
        return r;
    }

    @Override
    public Response processResumeProblemEvent(ResumeProblemEvent e) throws Exception {
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logResumeProblem(e,r);  // this relies on the above being done first
        return r;
    }

    @Override
    public Response processEndProblemEvent(EndProblemEvent e) throws Exception {
        Response r = new Response();

        // at the end of a problem the emotional state of the student model is updated
//        this.studentModel.updateEmotionalState(this.smgr,e.getProbElapsedTime(),e.getElapsedTime());
        this.studentModel.endProblem(smgr, smgr.getStudentId(),e.getProbElapsedTime(),e.getElapsedTime());
        r.setEffort(this.studentModel.getEffort());

        if (learningCompanion != null )
            learningCompanion.processEndProblem(smgr,(EndProblemEvent) e,r);
        new TutorLogger(smgr).logEndProblem(e, r);
        return r;
    }

    public HintResponse doSelectHint (SelectHintSpecs selectionCriteria) throws Exception {
        Hint h;
        if (selectionCriteria == null)
            h = hintSelector.selectHint(smgr);
        else
            h = hintSelector.selectHint(smgr,selectionCriteria);
        this.studentModel.hintGiven(smgr.getStudentState(), h);
        // write the student model back to the database
        this.studentModel.save();
        return new HintResponse(h);
    }


    public HintResponse processHintRequest (HintEvent e) throws Exception {
        Hint h= hintSelector.selectHint(smgr);
        this.studentModel.hintGiven(smgr.getStudentState(), h);
        hintGiven(h); // inform pedagogical move listeners that an intervention is given
        HintResponse r = new HintResponse(h);
        if (learningCompanion != null )
            learningCompanion.processHintRequest(smgr,e,r);
        new TutorLogger(smgr).logHintRequest(e, r);
        return r;
    }

    @Override
    public Response processShowExampleRequest(ShowExampleEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Problem p = ProblemMgr.getProblem(smgr.getStudentState().getCurProblem());
        int exId = p.getExample();
        Response r;
        if (exId >= 0)
        {
            Problem ex = ProblemMgr.getProblem(exId);
            List<Hint> solution=null;
            if (ex != null)  {
                hintSelector.init(smgr);
                solution = hintSelector.selectFullHintPath(smgr,ex.getId());
                // No updating of the student model/state is happening with regard to how many hints are seen
                // even though the example is comprised of a problem and all its hints.
                ex.setSolution(solution);
                r= new ExampleResponse(ex);
            }
            else r= new ExampleResponse(null);
            studentModel.exampleGiven(smgr.getStudentState(), exId);
        }
        else r= new ExampleResponse(null);


        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logShowExample((ShowExampleEvent) e, (ExampleResponse) r);
        return r;
    }

    @Override
    public Response processShowVideoRequest(ShowVideoEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Problem p = ProblemMgr.getProblem(smgr.getStudentState().getCurProblem());
        String vid = p.getVideo();

        Response r = new Video(vid);

        if (vid != null && !vid.equals(""))
            studentModel.videoGiven(smgr.getStudentState());
        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logShowVideoTransaction((ShowVideoEvent) e, r);

        return r;
    }

    
    @Override
    public Response processLCListRequest(ShowLCListEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
		
		int currentStudentPedagogyId = DbUser.getStudentPedagogy(smgr.getConnection(), smgr.getStudentId());
		String lcprofiles = DbPedagogy.getLCprofilesJSON(smgr.getConnection(), smgr.getClassID(), currentStudentPedagogyId);

	

        Response r = new LCList(lcprofiles);

        return r;
    }

    @Override
    public Response processChangeStudentLCRequest(ChangeStudentLCEvent e) throws Exception {


        int LCid = e.getLCid();       

        DbUser.setStudentPedagogy(smgr.getConnection(), smgr.getStudentId(), LCid);
        String shortname = DbPedagogy.getLShortname(smgr.getConnection(), LCid);
        
        Response r = new LCChange(shortname);
        return r;
    }
    

    
    @Override
    public Response processGazeWanderingRequest(GazeWanderingEvent e) throws Exception {
    	logger.setLevel(Level.DEBUG);
    	logger.debug("processGazeWanderingRequest");

        Response r = null;

    	int newGazeEventId = 0;
    	smgr.getStudentState().setProblemIdleTime(0);
    	       
        JSONObject gazeJSONData = e.getGazeJSONData();
        if (gazeJSONData == null) {
        	logger.debug("No gaze data");
        }
        else {
        	
            // insert gazeWanderingEvent
        	newGazeEventId = DbGaze.insertGazeWanderingEvent(smgr.getConnection(),smgr.getStudentId(), smgr.getSessionId(), smgr.getStudentState().getCurProblem(),  gazeJSONData);        	
           	r = new GazeWanderingResponse(smgr.getConnection(),smgr.getStudentId(),smgr.getClassID(),gazeJSONData, newGazeEventId);     
           	
           	String action = gazeJSONData.getString("action");
        	if (action.equals("Intervention")) {
           	
	           	// insert eventLog event (includes gazeWandering event Id)
	           	int newId = new TutorLogger(smgr).logGazeWanderingEvent((GazeWanderingEvent) e, r);
	
	            // update GazeWanderingEvent with eventLog eventId
	           	DbGaze.updateGazeWanderingEvent (smgr.getConnection(), newId, newGazeEventId);

           	}
        	else {
        		r = null;
        	}
        	
        }
        return r;
    }
        
    protected InterventionResponse getNextProblemIntervention (NextProblemEvent e) throws Exception {
        NextProblemIntervention intervention = (NextProblemIntervention) interventionGroup.selectIntervention(smgr,e,"NextProblem");
        if (intervention != null) {
            interventionGiven(intervention); // tell pedagogical move listeners that an intervention is given
            NextProblemInterventionResponse ir = new NextProblemInterventionResponse(intervention);
            // A hack because we need to determine if the Intervention returned should also build a Problem.   So we have to see if
            // the intervention is a NextProblemIntervention and then ask if it wants a problem built.   The only case of this currently
            // is the intervention which turns on/off MPP which at same time should show a new problem
            ir.setBuildProblem(intervention.isBuildProblem());
            return ir;
        }
        else return null;
    }



 /*

    protected InterventionResponse getNextProblemIntervention (NextProblemEvent e) throws Exception {
       NextProblemIntervention intervention = null;
        NextProblemInterventionResponse r=null;
        // If student is in challenge or review mode, we do not want interventions
        if (nextProblemInterventionSelector != null && !smgr.getStudentState().isInChallengeMode() && !smgr.getStudentState().isInReviewMode() &&
                e.isTutorMode())
        {
            nextProblemInterventionSelector.setServletInfo(smgr, this);
            intervention= nextProblemInterventionSelector.selectIntervention(e);
        }
        if (intervention != null) {


            interventionGiven(intervention); // tell pedagogical move listeners that an intervention is given
//            smgr.getStudentState().setInBtwProbIntervention(true);
            r = new NextProblemInterventionResponse(intervention);
            // A hack because we need to determine if the Intervention returned should also build a Problem.   So we have to see if
            // the intervention is a NextProblemIntervention and then ask if it wants a problem built.   The only case of this currently
            // is the intervention which turns on/off MPP which at same time should show a new problem
            r.setBuildProblem(intervention.isBuildProblem());
        }
        return r;
    }
    */


    /**
     * Return the problem selected by the student (in the requested mode if that is given)
     * TODO this is screwed up because it assumes the lesson is topic based and does stuff using topics.
     * This function is only on behalf of Assistments and to support test-users that want to select individual
     * problems.
     * @param e
     * @return
     */
    public Response processStudentSelectsProblemRequest (NextProblemEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        state.setProblemAnswer(null);
        if (! e.isForceProblem())
           return null;

        ProblemResponse r = null;
        // If student has selected a particular problem, then they must have left challenge/review mode and are back in practice mode.
        smgr.getStudentState().setInReviewMode(false);
        smgr.getStudentState().setInChallengeMode(false);
        smgr.getStudentState().setInPracticeMode(true);

        // N.B.  We always pass this a problemID.  It is not used to just force a topic
        Problem p = ProblemMgr.getProblem(Integer.parseInt(e.getProbId()));
        p.setMode(Problem.PRACTICE);
        // The student may have selected a problem using the MPP.   This means they had to open up a topic in order to make
        // the selection.   This topic is passed through MPPTryProblemEvent and we stick it in the NextProblemEvent passed
        // to this as the topicToForce EVEN THOUGH WE ARE NOT REALLY FORCING THE TOPIC.   It is merely placed there so that
        // we can then add the topic name to the activity JSON to refresh the GUI so that it shows the topic of this problem

        // This stuff with setting demo mode is all for assistments and is now no longer compatable with how demos are given (from an intervention
        // selector that has rules about how often).   So this code now omitted unles we go back to assistments some day and then it will
        // have to be reworked.
        if (Settings.usingAssistments) {
            int topicId = e.getTopicToForce();
            // TODO We've got topic stuff built in here that is difficult to extract and move to the LessonModel.   It's figuring out
            // what mode the problem should be returned in (practice or demo) based on topicModel example frequency for the tutoring strategy
            setProblemTopic(p, topicId);
            boolean showAsDemo = false;
            if (e.getProbMode() == null) {
                if (!smgr.getStudentState().isExampleShown() && params.getTopicExampleFrequency() != TopicModelParameters.frequency.never) {
                    showAsDemo = true;
                }
            }
            else if (e.getProbMode().equals(Problem.DEMO)) {
                showAsDemo = true;
            }

            // If the user asks for the problem to be given as a DEMO/EXAMPLE (this would only come from a TeachTopicEvent through Assistments) OR
            // the student is at a point in a new topic where an example has not been shown yet, then set the mode to DEMO
            if (e.isForceProblem() )     {
                if (showAsDemo) {
                    new TutorModelUtils().setupDemoProblem(p,smgr,hintSelector);
                }
                //elsesmgr.getStudentState().setTopicNumPracticeProbsSeen(smgr.getStudentState().getTopicNumPracticeProbsSeen() + 1);
            }
        }
        if (p != null)
            studentModel.newProblem(state,p);
            problemGiven(p);

        r = new ProblemResponse(p);

//        if (p != null && p.getType().equals(Problem.HTML_PROB_TYPE)) {
//            r.shuffleAnswers(smgr.getStudentState());
//        }
//        r.setProblemBindings(smgr);
        return r;

    }

    /**
     * process a simple MPP request to see a problem.
     * @param e
     * @return
     * @throws Exception
     */
    public Response processMPPSelectProblemRequest (NextProblemEvent e) throws Exception {
       StudentState state = smgr.getStudentState();
        state.setProblemAnswer(null);

        ProblemResponse r = null;
        // If student has selected a particular problem, then they must have left challenge/review mode and are back in practice mode.
        smgr.getStudentState().setInReviewMode(false);
        smgr.getStudentState().setInChallengeMode(false);
        smgr.getStudentState().setInPracticeMode(true);

        // N.B.  We always pass this a problemID.  It is not used to just force a topic
        Problem p = ProblemMgr.getProblem(Integer.parseInt(e.getProbId()));
        p.setMode(Problem.PRACTICE);
        // The student may have selected a problem using the MPP.   This means they had to open up a topic in order to make
        // the selection.   This topic is passed through MPPTryProblemEvent and we stick it in the NextProblemEvent passed
        // to this as the topicToForce EVEN THOUGH WE ARE NOT REALLY FORCING THE TOPIC.   It is merely placed there so that
        // we can then add the topic name to the activity JSON to refresh the GUI so that it shows the topic of this problem
        int topicId = e.getTopicToForce();
        // TODO We've got topic stuff built in here that is difficult to extract and move to the LessonModel.   It's figuring out
        // what mode the problem should be returned in (practice or demo) based on topicModel example frequency for the tutoring strategy
        setProblemTopic(p, topicId);
      //  smgr.getStudentState().setTopicNumPracticeProbsSeen(smgr.getStudentState().getTopicNumPracticeProbsSeen() + 1);
        if (p != null)
            studentModel.newProblem(state,p);
        int altProbId = 0;
        if (smgr.getExperiment().equals("multi-lingual")) {
       		altProbId = ProblemMgr.getProblemPair(Integer.valueOf(e.getProbId()));
    	}

        
        problemGiven(p);

        r = new ProblemResponse(p);
        r.setAltProbId(altProbId);

        //        r.setProblemBindings(smgr);
        return r;
    }


    private void setProblemTopic(Problem p, int topicId) {
        if (topicId > 0) {
            p.setInTopicId(topicId);
            Topic t = ProblemMgr.getTopic(topicId);
            if (t != null)
                p.setInTopicName(ProblemMgr.getTopic(topicId).getMlName());
            else p.setInTopicName("");
        }
    }

    @Override
    public Response processChallengeModeNextProblemRequest (NextProblemEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        Problem p = null;
        ProblemResponse r=null;
        if (e.getMode() !=null && e.getMode().equalsIgnoreCase(CHALLENGE_MODE) && !state.isInChallengeMode()) {
            smgr.getStudentState().setInChallengeMode(true);
            smgr.getStudentState().setCurProblemIndexInTopic(-1);
        }
        if (smgr.getStudentState().isInChallengeMode()) {
            // when entering the mode, sideline the current topic
            if (e.isForceTopic() && e.getTopicToForce() != smgr.getStudentState().getCurTopic()) {
                smgr.getStudentState().setSidelinedTopic(smgr.getStudentState().getCurTopic());
            }
            r = doSelectChallengeProblem(e);
            if (r == ProblemResponse.NO_MORE_CHALLENGE_PROBLEMS) {
                ((ProblemResponse) r).setEndPage(ChallengeModeProblemSelector.END_PAGE);
                // We clear the current topic from the workspace state so that later logins don't start at this topic
                state.setCurTopic(-1);
                return r;
            }
        }
        if (r ==null && (smgr.getStudentState().isInChallengeMode())) {
            smgr.getStudentState().setInReviewMode(false);
            smgr.getStudentState().setInChallengeMode(false);
            e.clearTopicToForce(); // A topic is on the NextProb event because it is for review/challenge mode.  We clear it so the regular
        }
        if (r != null)  {
            problemGiven(r.getProblem()); // tell all the pedagogical move listeners that a problem is being given.
            smgr.getStudentModel().newProblem(state,r.getProblem());
        }
        return r;
    }

    @Override
    public Response processReviewModeNextProblemRequest (NextProblemEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        Problem p = null;
        ProblemResponse r = null;
        if (e.getMode() !=null && e.getMode().equalsIgnoreCase(REVIEW_MODE) && !state.isInReviewMode()) {
            smgr.getStudentState().setInReviewMode(true);
            smgr.getStudentState().setCurProblemIndexInTopic(-1);
        }
        if (smgr.getStudentState().isInReviewMode()) {
            // when entering the mode, sideline the current topic
            if (e.isForceTopic() && e.getTopicToForce() != smgr.getStudentState().getCurTopic()) {
                smgr.getStudentState().setSidelinedTopic(smgr.getStudentState().getCurTopic());
            }
            r = doSelectReviewProblem(e);
            if (r == ProblemResponse.NO_MORE_REVIEW_PROBLEMS)  {
                ((ProblemResponse) r).setEndPage(ReviewModeProblemSelector.END_PAGE);
                // We clear the current topic from the workspace state so that later logins don't start at this topic
                state.setCurTopic(-1);
                return r;
            }
        }
        if (r ==null &&  smgr.getStudentState().isInReviewMode()) {
            smgr.getStudentState().setInReviewMode(false);
            smgr.getStudentState().setInChallengeMode(false);
            e.clearTopicToForce(); // A topic is on the NextProb event because it is for review/challenge mode.  We clear it so the regular
        }
        if (r != null)  {
            problemGiven(r.getProblem()); // tell all the pedagogical move listeners that a problem is being given.
            smgr.getStudentModel().newProblem(state,r.getProblem());
        }
        return r;

    }

    public ProblemResponse doSelectChallengeProblem (NextProblemEvent e) throws Exception {
        problemSelector = challengeModeSelector;
        if (e.getTopicToForce() > 0)
            smgr.getStudentState().setCurTopic(e.getTopicToForce());
        ProblemResponse r = getNextProblem(e);
        if (r == ProblemResponse.NO_MORE_PROBLEMS)
            return ProblemResponse.NO_MORE_CHALLENGE_PROBLEMS;
        else return r;

    }

    public ProblemResponse doSelectReviewProblem (NextProblemEvent e) throws Exception {
        problemSelector = reviewModeSelector;
        if (e.getTopicToForce() > 0)
            smgr.getStudentState().setCurTopic(e.getTopicToForce());
        ProblemResponse r = getNextProblem(e);
        if (r == ProblemResponse.NO_MORE_PROBLEMS)
            return ProblemResponse.NO_MORE_REVIEW_PROBLEMS;
        else return r;

    }


    /**
     * Note that this always returns a non-null problem even if to just indicate no more problems
     *
     * @param e
     * @return
     * @throws Exception
     */
     public ProblemResponse getNextProblem(NextProblemEvent e) throws Exception {
        Problem curProb = problemSelector.selectProblem(smgr, e, lastProblemScore);
        int altProbId = 0;
        // typically it takes 125 ms to finish the above call
        StudentState state = smgr.getStudentState();
        
        ProblemResponse r=null;
        if (curProb != null) {
            if (smgr.getExperiment().equals("multi-lingual")) {
//	        	if (state.getLangIndex() > 0) {
	        		altProbId = ProblemMgr.getProblemPair(curProb.getId());
//        		}
        	}
            curProb.setMode(Problem.PRACTICE);

            problemGiven(curProb); // inform pedagogical move listeners that a problem is given.
            // this call takes about 175 ms
            r = new ProblemResponse(curProb);
            r.setAltProbId(altProbId);
            r.setIsTranslation(0);
//            r.setProblemBindings(smgr);
        }
        else {
            r = ProblemResponse.NO_MORE_PROBLEMS;
            // We clear the current topic from the workspace state so that later logins don't start at this topic
            smgr.getStudentState().setCurTopic(-1);
        }
        return r;
    }


     /**
      * Note that this always returns a non-null problem even if to just indicate no more problems
      *
      * @param e
      * @return
      * @throws Exception
      */
      public ProblemResponse translateProblem(TranslateProblemEvent e) throws Exception {
         
    	 int altProbId = 0;
 		 Problem curProb  = null;
         ProblemResponse r=null;
    	 try {
    		  altProbId = ProblemMgr.getProblemPair(Integer.valueOf(e.getTranslateProbId()));
    		  if (altProbId == 0 ) {
    			  return ProblemResponse.NO_PROBLEM_TRANSLATION;    			      			  
    		  } 
    		  else { 
   	 		  	curProb  = ProblemMgr.getProblem(altProbId);
    		  }
    	 }
    	 catch (Exception ex) {
             return ProblemResponse.NO_PROBLEM_TRANSLATION;   		  
    	 }
 		 
         StudentState state = smgr.getStudentState();
         smgr.setProbLangIndex(1);
         if (curProb != null) {
             curProb.setMode(Problem.PRACTICE);

             problemGiven(curProb); // inform pedagogical move listeners that a problem is given.
             // this call takes about 175 ms
             r = new ProblemResponse(curProb);
             r.setIsTranslation(1);
             //             r.setProblemBindings(smgr);
         }
         else {
             r = ProblemResponse.NO_MORE_PROBLEMS;
             // We clear the current topic from the workspace state so that later logins don't start at this topic
             smgr.getStudentState().setCurTopic(-1);
         }
         return r;
     }



    /** Sets the lastProblemScore property
     *
     * @return
     * @throws Exception
     */
    protected ProblemScore gradeLastProblem () throws Exception {
        StudentState state = smgr.getStudentState();
        Problem lastProb = ProblemMgr.getProblem(state.getCurProblem());
        String lastProbMode = state.getCurProblemMode();

        if (lastProb != null  && lastProbMode.equals(Problem.PRACTICE))     {
            this.lastProblemScore =  problemGrader.gradePerformance(lastProb);
        }

        return this.lastProblemScore;
    }



    /**
     * Process a request for a next problem
     * @param  e  NextProblemEvent asks for the next problem
     * @return
     * @throws Exception
     */
    public Response processNextProblemRequest(NextProblemEvent e) throws Exception {

        // We have a fixed sequence which prefers forced problems, followed by topic intros, examples, interventions, regular problems.
        // If we ever want something more customized (e.g. from a XML pedagogy defn),  this would have to operate based on that defn
        long t = System.currentTimeMillis();
        Response r=null;
        StudentState state = smgr.getStudentState();
        smgr.setProbLangIndex(0);
        
        Problem curProb=null;
        // First grade the last practice problem which sets the lastProblemScore property of this class (used by subsequent code)
        this.lastProblemScore = gradeLastProblem();

        // NextProblem is an event that the lesson/topic models want to watch.   They may change their internal state or return EndOfLesson/Topic
        // Many interventions are generated in this call because the Lesson Model wants to notify the student about beginnings of new
        // lessons, endings of lessons, etc.
        r = lessonModel.processUserEvent(e);  // If the lesson/topic is done we get a response (an internal event) and exit
        // If the lessonModel didn't generate something, now we see if the pedagogical model wants to generate an intervention
        if (r == null)  {
            r = getNextProblemIntervention(e);
        }
        // less than 100 ms at this point

        // Some interventions are designed to be shown while a problem is being shown (perhaps some GUI element is changed)
        // For cases like this, the intervention's isBuildProblem is true.
        // If the intervention requires a problem, get the next problem and return a Problem with the intervention as a property of a problem
        if (r != null && r instanceof NextProblemInterventionResponse ) {
            if (((NextProblemInterventionResponse) r).isBuildProblem()) {
                ProblemResponse pr = getNextProblem(e);
                pr.setIntervention(((NextProblemInterventionResponse) r).getIntervention());
                r = pr;
            }
        }
        if (r == null) {
            r = getNextProblem(e); // takes about 300 ms
            // 400 ms by this point

        }

        if (r != null && r instanceof ProblemResponse) {
            curProb = ((ProblemResponse) r).getProblem();
        }
        if (learningCompanion != null )
            learningCompanion.processNextProblemRequest(smgr,e,r);
        if (curProb != null)  {
            smgr.getStudentModel().newProblem(state, curProb);  // 120 ms
            // 520 ms by this point
        }
        if (r instanceof InterventionResponse)
            new TutorLogger(smgr).logNextProblemIntervention(e,(InterventionResponse) r);
        else new TutorLogger(smgr).logNextProblem(e, r.getCharacterControl(), "PracticeProblem");
        // about 30 ms to do the logging
        StudentEffort eff = studentModel.getEffort();
        r.setEffort(eff);
        return r;
    }


    /**
     * Process a request for a next problem
     * @param  e  NextProblemEvent asks for the next problem
     * @return
     * @throws Exception
     */
    public Response processTranslateProblemRequest(TranslateProblemEvent e) throws Exception {

        // We have a fixed sequence which prefers forced problems, followed by topic intros, examples, interventions, regular problems.
        // If we ever want something more customized (e.g. from a XML pedagogy defn),  this would have to operate based on that defn
        long t = System.currentTimeMillis();
        Response r=null;
        StudentState state = smgr.getStudentState();
        
        Problem curProb=null;
        // First grade the last practice problem which sets the lastProblemScore property of this class (used by subsequent code)
        this.lastProblemScore = gradeLastProblem();

        // NextProblem is an event that the lesson/topic models want to watch.   They may change their internal state or return EndOfLesson/Topic
        // Many interventions are generated in this call because the Lesson Model wants to notify the student about beginnings of new
        // lessons, endings of lessons, etc.
        r = lessonModel.processUserEvent(e);  // If the lesson/topic is done we get a response (an internal event) and exit
        // If the lessonModel didn't generate something, now we see if the pedagogical model wants to generate an intervention
 /*       
        if (r == null)  {
            r = getNextProblemIntervention(e);
        }
        // less than 100 ms at this point

        // Some interventions are designed to be shown while a problem is being shown (perhaps some GUI element is changed)
        // For cases like this, the intervention's isBuildProblem is true.
        // If the intervention requires a problem, get the next problem and return a Problem with the intervention as a property of a problem
        if (r != null && r instanceof NextProblemInterventionResponse ) {
            if (((NextProblemInterventionResponse) r).isBuildProblem()) {
                ProblemResponse pr = getNextProblem(e);
                pr.setIntervention(((NextProblemInterventionResponse) r).getIntervention());
                r = pr;
            }
        }
        if (r == null) {
            r = getNextProblem(e); // takes about 300 ms
            // 400 ms by this point

        }
*/
        r = translateProblem(e); // takes about 300 ms
/*        
        if (r == null) {
            View er = new ErrorResponse("Translation error", "Problem has no translation available");
            
            r = new ProblemResponse.NO_PROBLEM_TRANSLATION;
           
            return r;
        }
*/
        if (r != null && r instanceof ProblemResponse) {
            curProb = ((ProblemResponse) r).getProblem();
        }
        if (learningCompanion != null )
            learningCompanion.processTranslateProblemRequest(smgr,e,r);
        if (curProb != null)  {
            smgr.getStudentModel().newProblem(state, curProb);  // 120 ms
            // 520 ms by this point
        }
        
//        new TutorLogger(smgr).logTranslateProblem(e, r.getCharacterControl(), "PracticeProblem");
        // about 30 ms to do the logging
        StudentEffort eff = studentModel.getEffort();
        r.setEffort(eff);
        return r;
    }



    
    
//    private ProblemResponse getTopicIntroDemoOrProblem(NextProblemEvent e, StudentState state, int curTopic, boolean topicDone) throws Exception {
//        ProblemResponse r=null;
//        // If nothing is being forced, first see if topic switch is necessary.   If so,   maybe we need to show a topicIntro
//        if ( topicDone ) {
//            curTopic = switchTopics(curTopic);  // sets student state curTopic
//            if (curTopic == -1)
//                return ProblemResponse.NO_MORE_PROBLEMS;
//            TopicIntro ti = getTopicIntro(curTopic);
//            if (ti != null) {
//                r = new TopicIntroResponse(ti);
//
//            }
//        }
//
//        // new code. We might want to return a topic intro if this is the first problem we have shown in this session.
//        if (r == null && state.getCurProblem() == -1) {
//            TopicIntro ti = getTopicIntro(curTopic);
//            if (ti != null) {
//                r = new TopicIntroResponse(ti);
//            }
//        }
//        // maybe we need to show an example
//        if (r == null) {
//            Problem ex = getTopicDemo(curTopic);
//            if (ex != null) {
//                r=  new DemoResponse(ex);
//                exampleGiven(ex);  // inform pedagogical move listeners of example being given
//            }
//        }
//
//        // we have to return a problem
//        if (r == null)   {
//            r = getProblem(e, nextDiff);
//        }
//        return r;
//    }




    @Override
    public Response processBeginInterventionEvent(BeginInterventionEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();
        String intervType = e.getInterventionType();
        String lastInterv =  smgr.getStudentState().getLastIntervention();
        // Prefer an intervType passed by the client to one coming from memory state.
        String interv;
        interv = (intervType != null && !intervType.equals("")) ? intervType : lastInterv;

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logShowIntervention(e, r, interv);
        return r;
    }

    @Override
    public Response processEndInterventionEvent(EndInterventionEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logEndIntervention(e, r);
        return r;
    }

    @Override
    public Response processBeginExampleEvent(BeginExampleEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logBeginExample(e, r);
        return r;
    }

    @Override
    public Response processEndExampleEvent(EndExampleEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processEndExample(smgr,(EndExampleEvent) e,r);
        new TutorLogger(smgr).logEndExample( e, r);
        return r;
    }

    @Override
    public Response processBeginExternalActivityEvent(BeginExternalActivityEvent e) throws Exception {
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logBeginExternalActivity(e, r);
        return r;
    }

    @Override
    public Response processEndExternalActivityEvent(EndExternalActivityEvent e) throws Exception {
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logEndExternalActivity(e, r);
        return r;
    }

    @Override
    public Response processClickCharacterEvent(ClickCharacterEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        r = learningCompanion.processClickCharacterEvent(smgr,e);
        new TutorLogger(smgr).logClickCharacter(e, r);
        return r;
    }

    @Override
    public Response processMuteCharacterEvent(MuteCharacterEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null)
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logMuteCharacter(e, r);
        return r;
    }

    @Override
    public Response processUnMuteCharacterEvent(UnMuteCharacterEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logUnMuteCharacter(e, r);
        return r;
    }

    @Override
    public Response processEliminateCharacterEvent(EliminateCharacterEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logEliminateCharacter(e, r);
        return r;
    }

    @Override
    public Response processShowCharacterEvent(ShowCharacterEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logShowCharacter(e, r);
        return r;
    }

    @Override
    public Response processReadProblemEvent(ReadProblemEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        smgr.getStudentState().setIsTextReaderUsed(true);
        Response r = new Response();

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logReadProblem(e, r);
        return r;
    }

    public InterventionSelector getLastInterventionSelector () throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String lastInterventionClass = smgr.getStudentState().getLastIntervention();
        Class interventionSelectorClass = Class.forName(lastInterventionClass);
        Constructor constructor = interventionSelectorClass.getConstructor(SessionManager.class);
        InterventionSelector isel = (InterventionSelector) constructor.newInstance(smgr);
        return isel;
    }


    @Override
    public Response processContinueAttemptInterventionEvent(ContinueAttemptInterventionEvent e) throws Exception {
        Response r;
        smgr.getStudentState().setProblemIdleTime(0);
        r = attemptInterventionSelector.processContinueAttemptInterventionEvent(e);

        if (r != null && r instanceof Intervention) {
            ;
        }
        else if (r instanceof InternalEvent)
            ;
        else {
            //TODO.  When the attempt first came in the problem was graded and the isCorrect value went back with the intervention.
            // The question is:  Does the client always show the grade or is displaying the grade controlled by another flag?
            if (smgr.getStudentState().isProblemSolved())
                r= new Response("grade=true&isCorrect=true");
            else
                r= new Response("grade=true&isCorrect=false");
        }

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logContinueAttemptIntervention(e, r);
        return r;
    }

    public InterventionSelector getInterventionSelectorThatGeneratedIntervention () throws Exception {
        String classname = smgr.getStudentState().getLastIntervention();
        Class c = Class.forName(classname);
        Constructor constructor = c.getConstructor(SessionManager.class);
        InterventionSelector is =(InterventionSelector) constructor.newInstance(smgr);
        is.init(smgr,this);
        return is;
    }

                                                                                                            /*
    @Override
    public Response processContinueNextProblemInterventionEvent(ContinueNextProblemInterventionEvent e) throws Exception {
        Response r;
        smgr.getStudentState().setProblemIdleTime(0);
        // if the lesson model generated the intervention it will process this event and may return a response or not
        r = lessonModel.processUserEvent(e) ; // give lesson model a chance to weigh in.
        // If the lesson model doesn't have a response to this event, see if the pedagogical model does.
        if ( r == null) {
            String lastInterventionClass = smgr.getStudentState().getLastIntervention();
            InterventionSelectorSpec spec= interventionGroup.getInterventionSelectorSpec(lastInterventionClass);
            if (spec != null) {
                NextProblemInterventionSelector intSel = (NextProblemInterventionSelector) spec.buildIS(smgr);
                intSel.setServletInfo(smgr,this);
                // We may get back null, InternalEvent, InterventionResponse
                r = intSel.processContinueNextProblemInterventionEvent( e);
                // The last intervention selector will either return an InternalEvent or null
                // if an internal state is returned, then process it
                if (r instanceof InternalEvent)
                    r=processInternalEvent((InternalEvent) r);
                // if null comes back, see if the pedagogical model has an intervention
                else if (r == null)
                    r =  getNextProblemIntervention(new NextProblemEvent(e.getServletParams()));
                // otherwise we must have gotten back an InterventionResponse so we don't need to do anything more except log it.
            }
        }
        // lesson model had the intervnetion that was interested in the continue request but didn't have anything else to do
        // so see if this model wants to intervene
        else if (r instanceof InterventionInputProcessed)
            r = getNextProblemIntervention(new NextProblemEvent(e.getServletParams()));
        // If we don't pick an intervention or if the lesson model didn't, grade the last problem and try to give a new problem
        if (r == null) {
            StudentState state = smgr.getStudentState();
            // have to regrade last problem so that we can select a problem
            gradeLastProblem();

            r = getNextProblem(null);
            studentModel.newProblem(state, ((ProblemResponse) r).getProblem());
        }
        new TutorLogger(smgr).logContinueNextProblemIntervention(e, r);
        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        return r;
    }


                                                                                                     */

    @Override
    // this is not really the time-based intervention I wanted.   Instead this is a time-based message being sent
    // to the last intervention to see if it has a response.   It typically returns itself as the response. which has the
    // effect of keeping the intervention on screen .   If it returns null, it means it should behave like a NextProblem event.
    // This is being called by the wait loop in the helper to see if the helper is released from collaborating and can go back to
    // working by themselves.    The wait loop in the originator (waiting for helper to become available) also uses this.  When
    // the helper becomes available, this gets back a different intervention telling the person they are now going to have a partner
    // helping them.
    public Response processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        Response r;
        smgr.getStudentState().setProblemIdleTime(0);
        NextProblemInterventionSelector isel = (NextProblemInterventionSelector) getLastInterventionSelector();
        Intervention intervention = isel.processInterventionTimeoutEvent(e);

        if (intervention != null) {
            r= new InterventionResponse(intervention);

            if (learningCompanion != null )
                learningCompanion.processUncategorizedEvent(e,r);
            new TutorLogger(smgr).logTimedIntervention(e, r);
            return r;
        }
        // this does not want to generate another intervention.  So select a new prob
        else {
            r= this.processNextProblemRequest(new NextProblemEvent(e.getElapsedTime(),e.getProbElapsedTime()));
            StudentState state = smgr.getStudentState();
//            boolean isTopicDone = gradeProblem(e.getProbElapsedTime());
//            r = getTopicIntroDemoOrProblem(new NextProblemEvent(e.getElapsedTime(), e.getProbElapsedTime()), state, state.getCurTopic(), isTopicDone);
            studentModel.newProblem(state, ((ProblemResponse) r).getProblem());
            return r;
        }

    }

    @Override
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r = lessonModel.processUserEvent(e) ; // give lesson model a chance to weigh in.
        // If the lesson model did not handled the event, this will find the last intervention and have it process the event.
        // We then see if there is an intervention that applies after that is done (or process an internal event
        if ( r == null) {
            String lastInterventionClass = smgr.getStudentState().getLastIntervention();
            InterventionSelectorSpec spec= interventionGroup.getInterventionSelectorSpec(lastInterventionClass);
            if (spec != null) {
                NextProblemInterventionSelector intSel = (NextProblemInterventionSelector) spec.buildIS(smgr);
                intSel.init(smgr,this);
                // N.B. Assumption is that we no longer get Interventions back
                r = intSel.processInputResponseNextProblemInterventionEvent( e);
                // XML created to represent user input is retrieved from the IS and put in the event for logger to stick in eventlog.userinput
                e.setUserInput(intSel.getUserInputXML());

                // I don't understand why we are adding stuff to the event after its been sent to the npis
//                e.setUserInput(intSel.getUserInputXML());
                // The last intervention selector will either return an InternalEvent or null
                // if an internal state is returned, then process it
                if (r instanceof InternalEvent)
                    // this should not happen because pedagogical models don't have internal events.
                    r = processInternalEvent((InternalEvent) r);
                // if null comes back, see if the pedagogical model has an intervention
                else if (r==null && shouldForceNextProblem())
                    r =  getNextProblemIntervention(new NextProblemEvent(e.getServletParams()));
                // otherwise its an InterventionResponse which will be logged and returned.
            }
        }
        // the lesson model processed the intervention input but didn't want to select a response after that
        // so see if this model wants to intervene.
        else if (r instanceof InterventionInputProcessed) {
            r = getNextProblemIntervention(new NextProblemEvent(e.getServletParams()));
        }

       // If we don't pick an intervention or if the lesson model didn't, grade the last problem and try to give a new problem
        if (r == null) {
            StudentState state = smgr.getStudentState();
            // have to regrade last problem so that we can select a problem
            gradeLastProblem();

            r = getNextProblem(null);
            studentModel.newProblem(state, ((ProblemResponse) r).getProblem());
        }


        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logInputResponseNextProblemIntervention( e, r);
        return r;
    }

    /**
     * This is intended to be a hook for subclasses to weigh in (especially CollabPedagogicalModel),
     * without having to override the whole processInputResponseNextProblemInterventionEvent() method.
     * @return Whether to move on to the next problem
     */
    protected boolean shouldForceNextProblem() {
        return true;
    }

    @Override
    /**
     * An input response to an intervention on attempt will go back to the originating IS.  This
     *  may return an intervention, internval event, or null.
     */
    public Response processInputResponseAttemptInterventionEvent(InputResponseAttemptInterventionEvent e) throws Exception {
        smgr.getStudentState().setProblemIdleTime(0);
        Response r=null;
        String lastInterventionClass = smgr.getStudentState().getLastIntervention();
        InterventionSelectorSpec spec= interventionGroup.getInterventionSelectorSpec(lastInterventionClass);
        if (spec != null) {
            AttemptInterventionSelector intSel = (AttemptInterventionSelector) spec.buildIS(smgr);
            intSel.init(smgr,this);
            r = intSel.processInputResponseAttemptInterventionEvent(e);
            if (true)
                throw new UserException("This isn't finished yet.  Not sure what to do after sending to the IS");
            e.setUserInput(intSel.getUserInputXML());
            // old outdated idea that an intervention prompts for a hint and the user selects it ?
//            if (r != null && r instanceof SelectHintSpecs) {
//                r= doSelectHint((SelectHintSpecs) intervention);
//            }
            if (r != null) {
                ;
            }
            else if (r instanceof InternalEvent) {

            }
            // TODO failing anything unusual, we need to grade the attempt and return that.
            else {
                // we are done with post-attempt interventions.  Its now time to grade the problem.
                if (smgr.getStudentState().isProblemSolved())
                    r= new Response("&grade=true&isCorrect=true");
                else
                    r= new Response("&grade=true&isCorrect=false");
            }
        }

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        new TutorLogger(smgr).logInputResponseAttemptIntervention( e, r);
        return r;
    }





    @Override
    public void problemGiven(Problem p) throws SQLException {
        StudentState st = smgr.getStudentState();
        st.setNumRealProblemsThisTutorSession(st.getNumRealProblemsThisTutorSession()+1);
        st.setNumProblemsThisTutorSession(st.getNumProblemsThisTutorSession()+1);
        // 6/15/15 DM comment out below.  StudentState.beginProblem is what sets this.  This was making it happen too early
//        st.setCurProblem(p.getId());
            setProblemTopic(p, st.getCurTopic());
        if (p.getMode().equals(Problem.PRACTICE))
            st.setTopicNumPracticeProbsSeen(smgr.getStudentState().getTopicNumPracticeProbsSeen() + 1);
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.problemGiven(p);
    }

    @Override
    public void exampleGiven(Problem ex) throws SQLException {
        StudentState st = smgr.getStudentState();
        st.setNumProblemsThisTutorSession(st.getNumProblemsThisTutorSession()+1);
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.exampleGiven(ex);
    }

    @Override
    public void lessonIntroGiven(TopicIntro intro) throws SQLException {
        StudentState st = smgr.getStudentState();
        st.setNextProblem(intro.getId());
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.lessonIntroGiven(intro);
    }

    @Override
    public void attemptGraded(boolean isCorrect) {
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.attemptGraded(isCorrect);
    }

    @Override
    public void hintGiven( Hint hint) throws SQLException {
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.hintGiven(hint);
    }



    @Override
    public void interventionGiven(Intervention intervention) throws SQLException {
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.interventionGiven(intervention);
    }

    @Override
    public void newTopic(Topic t) {
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.newTopic(t);
    }

    @Override
    public void newSession(int sessId) throws SQLException {
        new TutorLogger(smgr).newSession(smgr.getStudentId(),sessId,0);
        for (PedagogicalMoveListener l : this.pedagogicalMoveListeners)
            l.newSession(sessId);
    }


}
