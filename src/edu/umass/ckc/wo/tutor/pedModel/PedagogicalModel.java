package edu.umass.ckc.wo.tutor.pedModel;

import ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.email.Emailer;
import edu.umass.ckc.wo.config.LessonXML;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUserPedagogyParams;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.SelectHintSpecs;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.strat.ClassStrategyComponent;
import edu.umass.ckc.wo.strat.SCParam;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.intervSel2.*;
import edu.umass.ckc.wo.tutor.model.*;
import edu.umass.ckc.wo.tutor.probSel.ChallengeModeProblemSelector;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.tutor.probSel.ReviewModeProblemSelector;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Abstract class that defines the basic event processing of a pedagogical model used in the TutorHut
 *
 * User: marshall
 * Date: Dec 3, 2008
 * Time: 9:56:34 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PedagogicalModel implements TutorEventProcessor { // extends PedagogicalModelOld {

    public static final String CHALLENGE_MODE = "challenge";
    public static final String REVIEW_MODE = "review";
    private static Logger logger = Logger.getLogger(PedagogicalModel.class);
    protected Pedagogy pedagogy;
    protected LessonModel lessonModel;
    protected PedagogicalModelParameters params;
//    protected LessonModelParameters lessonModelParameters;

    protected StudentModel studentModel;
    protected ProblemSelector problemSelector ;// problem selection is a pluggable strategy
    protected HintSelector hintSelector;  // hint selection is a pluggable strategy
    protected SessionManager smgr;  // object that contains state and session info and db connection
    protected LearningCompanion learningCompanion=null; // an optional LearningCompanion (null if none)
    protected AttemptInterventionSelector attemptInterventionSelector=null; // an optional intervention selector (can be null)
    protected NextProblemInterventionSelector nextProblemInterventionSelector=null; // an optional intervention selector (can be null)
    protected ExampleSelector exampleSelector=null; // an optional example selector (can be null)
    protected VideoSelector videoSelector=null; // an optional video selector (can be null)
    protected ChallengeModeProblemSelector challengeModeSelector;
    protected ReviewModeProblemSelector reviewModeSelector;
    protected ProblemGrader problemGrader;
    protected ProblemScore lastProblemScore;
    protected InterventionGroup interventionGroup;
    private TutorModel tutorModel; // temporarily here until we build the correct set of models

//    public PedagogicalModelParameters setParams(PedagogicalModelParameters classParams, PedagogicalModelParameters defaultParams) {
//        defaultParams.overload(classParams);
//        return defaultParams;
//    }

    public LessonModel getLessonModel () {
        return this.lessonModel;
    }

    public PedagogicalModelParameters getParams () {
        return this.params;
    }

    public void setParams(PedagogicalModelParameters params) {
        this.params = params;
    }

    public HintSelector getHintSelector() {
        return hintSelector;
    }

    public void setHintSelector(HintSelector hintSelector) {
        this.hintSelector = hintSelector;
    }

    public void setSmgr(SessionManager smgr) {
        this.smgr = smgr;
    }

    public void setAttemptInterventionSelector(AttemptInterventionSelector attemptInterventionSelector) {
        this.attemptInterventionSelector = attemptInterventionSelector;
    }

    public void setNextProblemInterventionSelector(NextProblemInterventionSelector nextProblemInterventionSelector) {
        this.nextProblemInterventionSelector = nextProblemInterventionSelector;
    }

    public void setExampleSelector(ExampleSelector exampleSelector) {
        this.exampleSelector = exampleSelector;
    }

    public void setVideoSelector(VideoSelector videoSelector) {
        this.videoSelector = videoSelector;
    }

    public StudentModel getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(StudentModel studentModel) {
        this.studentModel = studentModel;
    }

    public SessionManager getSessionMgr () {
        return smgr;
    }

    public boolean isCollaborative () {
        return false;
    }

    @Override
    public Response processInternalEvent(InternalEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Handle a TutorHutEvent and dispatch to the abstract method which handles it.  Each PedagogicalModel will
     * will have these processing methods plus potentially two others when an intervention selector is part of the pedagogy.
     * When an intervention selector is part of the pedagogy, the InterventionPedagogicalModel overrides this to handle
     * the intervention events and also calls this method to handle the basic processing of the events below.
     * @param e
     * @return
     * @throws Exception
     *
     */
    @Override
    public Response processUserEvent(TutorHutEvent e) throws Exception {
        Response r = null;
        StudentState state = smgr.getStudentState();
        // make sure probElapseTime is saved on each event containing one
        if (e instanceof IntraProblemEvent)
            smgr.getStudentState().setProbElapsedTime(((IntraProblemEvent) e).getProbElapsedTime());
        // Formality problem attempts handled separately from standard attempts


        if (e instanceof AttemptEvent) {
            r = processAttempt((AttemptEvent) e);
            studentModel.save();
            return r;
        }


        else if (e instanceof NextProblemEvent)  {
            NextProblemEvent ee = (NextProblemEvent)  e;
            long t = System.currentTimeMillis();
            //  I think the only way we arrive at this with forceProblem=true is from the tool for test-users that allows problem selection from dialog
            if (ee.isForceProblem())
                r = processStudentSelectsProblemRequest(ee);
            else if (ee.getMode().equalsIgnoreCase(CHALLENGE_MODE) || state.isInChallengeMode())
                r = processChallengeModeNextProblemRequest(ee);
            else if (ee.getMode().equalsIgnoreCase(REVIEW_MODE) || state.isInReviewMode())
                r = processReviewModeNextProblemRequest(ee);
            else r = processNextProblemRequest((NextProblemEvent) e);
            studentModel.save();
            System.out.println("Time to process NextProblem event " + (System.currentTimeMillis() - t));
            return r;
        }



        else if (e instanceof HintEvent) {
            r =  processHintRequest((HintEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowExampleEvent) {
            r = processShowExampleRequest((ShowExampleEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowVideoEvent) {
            r = processShowVideoRequest((ShowVideoEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowInstructionsEvent) {
            new TutorLogger(smgr).logIntraProblemEvent((IntraProblemEvent) e,"ShowInstructions", r);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowGlossaryEvent) {
            new TutorLogger(smgr).logIntraProblemEvent((IntraProblemEvent) e,"ShowGlossary", r);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowFormulasEvent) {
            new TutorLogger(smgr).logIntraProblemEvent((IntraProblemEvent) e,"ShowFormulas", r);
            studentModel.save();
            return r;
        }


        else if (e instanceof BeginProblemEvent) {
            long t = System.currentTimeMillis();
            r = processBeginProblemEvent((BeginProblemEvent) e);
            studentModel.save();
            System.out.println("Time to process BeginProblem event " + (System.currentTimeMillis() - t));

            return r;
        }
        else if (e instanceof ResumeProblemEvent) {
            r = processResumeProblemEvent((ResumeProblemEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof BeginExternalActivityEvent) {
            r = processBeginExternalActivityEvent((BeginExternalActivityEvent) e);
            studentModel.save();
            return r;
        }

        else if (e instanceof EndProblemEvent) {
            r = processEndProblemEvent((EndProblemEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof EndExternalActivityEvent) {
            r = processEndExternalActivityEvent((EndExternalActivityEvent) e);
            studentModel.save();
            return r;
        }

        else if (e instanceof ClickCharacterEvent) {
            r = processClickCharacterEvent((ClickCharacterEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof MuteCharacterEvent) {
            r = processMuteCharacterEvent((MuteCharacterEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof UnMuteCharacterEvent) {
            r = processUnMuteCharacterEvent((UnMuteCharacterEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof EliminateCharacterEvent) {
            r = processEliminateCharacterEvent((EliminateCharacterEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof ShowCharacterEvent) {
            r = processShowCharacterEvent((ShowCharacterEvent) e);
            studentModel.save();
            return r;
        }

        else if (e instanceof ReadProblemEvent) {
            r = processReadProblemEvent((ReadProblemEvent) e);
            studentModel.save();
            return r;
        }

        else if (e instanceof ReportErrorEvent) {
            ReportErrorEvent ee = (ReportErrorEvent) e;
            // an error is reported by the user.
            r = disableProblemForStudent(ee,state);
            String errorMsg = "User reported error on problem:  " + state.getCurProblem() + "  problem-broken:  "
                    + ((ReportErrorEvent) e).isProbBroken() + ".  User message: " +  ((ReportErrorEvent) e).getMessage();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Emailer.sendEmail("contact_mathspring@cs.umass.edu",
                                "no-reply@wayangoutpost.net", Settings.mailServer,"Mathspring User Reported Error",errorMsg);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }).start();
//            Emailer.sendErrorEmail(BaseServlet.adminEmail, BaseServlet.emailServer, "wayang error for session: " + sessId, ((ReportErrorEvent) e).getMessage(), null);
            studentModel.save();
            return r;
        }
        else if (e instanceof BeginInterventionEvent) {
            r = processBeginInterventionEvent((BeginInterventionEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof EndInterventionEvent) {
            r = processEndInterventionEvent((EndInterventionEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof BeginExampleEvent) {
            r = processBeginExampleEvent((BeginExampleEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof EndExampleEvent) {
            r = processEndExampleEvent((EndExampleEvent) e);
            studentModel.save();
            return r;
        }                                 /*
        else if (e instanceof ContinueNextProblemInterventionEvent) {
            r = processContinueNextProblemInterventionEvent((ContinueNextProblemInterventionEvent) e);
            studentModel.save();
            return r;
        }                               */
        else if (e instanceof InterventionTimeoutEvent){
            r = processInterventionTimeoutEvent((InterventionTimeoutEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof ContinueAttemptInterventionEvent) {
            r = processContinueAttemptInterventionEvent((ContinueAttemptInterventionEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof InputResponseNextProblemInterventionEvent) {
            r = processInputResponseNextProblemInterventionEvent((InputResponseNextProblemInterventionEvent) e);
            studentModel.save();
            return r;
        }
        else if (e instanceof InputResponseAttemptInterventionEvent) {
            r = processInputResponseAttemptInterventionEvent((InputResponseAttemptInterventionEvent) e);
            studentModel.save();
            return r;
        }

        else if (e instanceof ClearSessionLogEntriesEvent) {
            new TutorLogger(smgr).clearSessionLog(smgr.getSessionNum());
            studentModel.save();
            return new Response();
        }
        else if (e instanceof ClearUserPropertiesEvent) {
            smgr.clearUserProperties();
            studentModel.save();
            return new Response();
        }


        else return new Response("Unknown Event");

    }

    // On request from Ivon 2/5/18 if a student report comes in about a problem we need to put it on a list of problems
    // that are broken for that user.   This gets stored in the student state that persists for all their sessions.
    //  The problem selectors will not select problems in this list.   Logs (event and problemhistory will have a special
    // status for this broken problem)
    private Response disableProblemForStudent(ReportErrorEvent ee, StudentState state) throws Exception {
        Response r = new Response();
        new TutorLogger(smgr).logReportedError(ee);
        int sessId = ee.getSessionId();
        // If the prob is broken, save its id in the student state so that it will not be given again
        // Also set that the curProb is broken so that next problem event processing will grade it and log it correctly
        if (ee.isProbBroken()) {
            state.addBrokenProblemId(Integer.toString(state.getCurProblem()));
            state.setCurProbBroken(true);
        }
        // If the problem ID is passed and set as broken=true
        // try to process this as a NextProb event.
//        if (ee.isProbBroken()) {
//            NextProblemEvent npe = new NextProblemEvent(ee.getElapsedTime(), ee.getProbElapsedTime(), null, ee.getMode());
//            if (ee.getMode().equalsIgnoreCase(CHALLENGE_MODE) || state.isInChallengeMode())
//                r = processChallengeModeNextProblemRequest(npe);
//            else if (ee.getMode().equalsIgnoreCase(REVIEW_MODE) || state.isInReviewMode())
//                r = processReviewModeNextProblemRequest(npe);
//            else r = processNextProblemRequest(npe);
//        }
        return r;

    }

    // results:  AttemptResponse | InterventionResponse
    public abstract Response processAttempt (AttemptEvent e) throws Exception;

    public abstract Response processBeginProblemEvent (BeginProblemEvent e) throws Exception;

    public abstract Response processEndProblemEvent (EndProblemEvent e) throws Exception;

    public abstract Response processResumeProblemEvent (ResumeProblemEvent e) throws Exception;

    // results: HintResponse | InterventionResponse
    public abstract Response processHintRequest (HintEvent e) throws Exception;

    public abstract Response processShowExampleRequest (ShowExampleEvent e) throws Exception;

    public abstract Response processShowVideoRequest (ShowVideoEvent e) throws Exception;

    // results: ProblemResponse | InterventionResponse
    public abstract Response processNextProblemRequest (NextProblemEvent e) throws Exception;

    public abstract Response processStudentSelectsProblemRequest (NextProblemEvent e) throws Exception;

    public abstract Response processMPPSelectProblemRequest (NextProblemEvent e) throws Exception;

    public abstract Response processChallengeModeNextProblemRequest (NextProblemEvent e) throws Exception;

    public abstract Response processReviewModeNextProblemRequest (NextProblemEvent e) throws Exception;

    public abstract ProblemResponse getNextProblem(NextProblemEvent e) throws Exception;

    public abstract Response processBeginInterventionEvent (BeginInterventionEvent e)  throws Exception;

    public abstract Response processEndInterventionEvent (EndInterventionEvent e)  throws Exception;

    public abstract Response processBeginExampleEvent (BeginExampleEvent e)  throws Exception;

//    protected abstract Response startTutor(EnterTutorEvent e) throws Exception ;


    // The reason we have these two is because a continue or inputResponse may require
    // the pedagogical model to call its wrapped PMs to select a problem
//    public abstract Response processContinueRequestInternal(ContinueEvent e) throws Exception;
//    public abstract Response processInputResponseInternal(InputResponseEvent e) throws Exception;

    public abstract Response processEndExampleEvent (EndExampleEvent e)  throws Exception;

    public abstract Response processBeginExternalActivityEvent (BeginExternalActivityEvent e) throws Exception;

    public abstract Response processEndExternalActivityEvent (EndExternalActivityEvent e) throws Exception;

    public abstract Response processClickCharacterEvent (ClickCharacterEvent e)  throws Exception;

    public abstract Response processMuteCharacterEvent (MuteCharacterEvent e)  throws Exception;

    public abstract Response processUnMuteCharacterEvent (UnMuteCharacterEvent e)  throws Exception;

    public abstract Response processEliminateCharacterEvent (EliminateCharacterEvent e)  throws Exception;

    public abstract Response processShowCharacterEvent (ShowCharacterEvent e)  throws Exception;

    public abstract Response processReadProblemEvent(ReadProblemEvent e) throws Exception;

   // public abstract Response processContinueNextProblemInterventionEvent(ContinueNextProblemInterventionEvent e) throws Exception;
    public abstract Response processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception;

    public abstract Response processContinueAttemptInterventionEvent(ContinueAttemptInterventionEvent e) throws Exception;

    public abstract Response processInputResponseAttemptInterventionEvent(InputResponseAttemptInterventionEvent e) throws Exception;

    public abstract Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception;

    /** These two methods are called each time a pedagogical model makes a problem/hint selection as a result
     * of an intervention selector requesting that a problem/hint be given in response to an intervention.
     *  If the pedagogical model that owns the intervention selector does not have a problem selector/hint selector
     * it must pass the request to the decorated pedagogical model through one of these two methods.
     *
     * Note:  By default, the BasePedagogicalModel (subclass of this) provides the default behavior of calling the
     * decorated PedModel.
     *
     * @param selectionCriteria
     * @return
     * @throws Exception
     */
    public abstract HintResponse doSelectHint (SelectHintSpecs selectionCriteria) throws Exception;

    public LearningCompanion getLearningCompanion() {
        return learningCompanion;
    }

    public void setLearningCompanion(LearningCompanion learningCompanion) {
        this.learningCompanion = learningCompanion;
    }

    protected PedagogicalModelParameters getPedagogicalModelParametersFromStrategy (TutorStrategy strategy) {
        ClassStrategyComponent sc = strategy.getTutor_sc();
        List<SCParam> params = sc.getParams();
        PedagogicalModelParameters theParams = new PedagogicalModelParameters(params);
        return theParams;
    }

    protected PedagogicalModelParameters getPedagogicalModelParametersForUser(Connection connection, Pedagogy ped, int classId, int studId) throws SQLException {

        // first we get the parameters out of the Pedagogy as defined in the XML pedagogies.xml
        PedagogicalModelParameters defaultParams = ped.getParams();
        // If this is a configurable pedagogy (meaning that it can be given some parameters to guide its behavior),  then
        // see if this user has a set of parameters and if so use them to configure the pedagogy.
        // these params come from settings in the WoAdmin tool for the class.
        PedagogicalModelParameters classParams = DbClass.getPedagogicalModelParameters(connection, classId);
        // overload the defaults with stuff defined for the class.
        defaultParams.overload(classParams);
//       if (this.pedagogicalModel instanceof ConfigurablePedagogy) {
        // these params are the ones that were passed in by Assistments and saved for the user
        PedagogyParams userParams = DbUserPedagogyParams.getPedagogyParams(connection, studId);
        // overload the params with anything provided for the user.
        defaultParams.overload(userParams);
        return defaultParams;
    }

    public void setChallengeModeProblemSelector (ChallengeModeProblemSelector challengeModeProblemSelector)  {
        this.challengeModeSelector = challengeModeProblemSelector;
    }

    public void setReviewModeProblemSelector (ReviewModeProblemSelector reviewModeProblemSelector)  {
        this.reviewModeSelector = reviewModeProblemSelector;
    }

    public ChallengeModeProblemSelector getChallengeModeSelector () {
        return this.challengeModeSelector;
    }

    public void setChallengeModeSelector(ChallengeModeProblemSelector challengeModeSelector) {
        this.challengeModeSelector = challengeModeSelector;
    }

    public ReviewModeProblemSelector getReviewModeSelector () {
        return this.reviewModeSelector;
    }

    public void setReviewModeSelector(ReviewModeProblemSelector reviewModeSelector) {
        this.reviewModeSelector = reviewModeSelector;
    }

    public ProblemSelector getProblemSelector () {
        return this.problemSelector;
    }

    public void setProblemSelector(ProblemSelector problemSelector) {
        this.problemSelector = problemSelector;
    }

    public abstract void newSession (int sessionId) throws SQLException;


    /**
     * The ped model should not show the MPP if there is a nextprob intervention selector that determines when MPP shows.
     * If no intervention selector of this sort, then show MPP is determined by a flag in the pedagogy.
     * @return
     */
    public boolean isShowMPP() {
        // note: this used to return false if there was an intervention in the pedagogical model that turned the MPP on and off.
        // That behavior is no longer requested, so we simply return true so that MPP always shows.

        return true;
    }

    public TutorModel getTutorModel() {
        return tutorModel;
    }

    public void setTutorModel(TutorModel tutorModel) {
        this.tutorModel = tutorModel;
    }

    public ProblemGrader getProblemGrader() {
        return problemGrader;
    }

    public ProblemScore getLastProblemScore() {
        return lastProblemScore;
    }

    public Pedagogy getPedagogy () {
        return this.pedagogy;
    }


    public abstract void addPedagogicalMoveListener(PedagogicalMoveListener pml);
}
