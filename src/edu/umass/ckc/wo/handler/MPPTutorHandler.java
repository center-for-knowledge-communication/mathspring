package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.internal.BeginningOfTopicEvent;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.TutorModel;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.woserver.ServletInfo;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/9/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MPPTutorHandler {
    private ServletInfo info;
    private SessionManager smgr;
    private boolean showMPP;

    public MPPTutorHandler(ServletInfo servletInfo, SessionManager smgr) throws SQLException {
        this.info = servletInfo;
        this.smgr = smgr;
        showMPP = smgr.getPedagogicalModel().isShowMPP();
    }

    public void handleRequest(MPPTopicEvent e) throws Exception {
        StudentState state = smgr.getStudentState();
        // If the MPPReturnToHut event comes in and the student had solved the problem they were on before going to MPP, get a next problem and show it
        if (e instanceof MPPReturnToHutEvent) {
//            Response r = new ProblemResponse(p,smgr.getStudentModel().getTopicMasteries(),smgr.getStudentState().getCurTopic(), e.getElapsedTime());
            String lastProbType = state.getCurProbType();
            // If the last problem is resumable and not a topic intro
            if (lastProbType != null && !((MPPReturnToHutEvent) e).getProbId().equals(Problem.TOPIC_INTRO_ID)) {

                if (lastProbType.equalsIgnoreCase(Problem.FLASH_PROB_TYPE) || lastProbType.equalsIgnoreCase(Problem.HTML_PROB_TYPE) )  {
                    Problem p = ProblemMgr.getProblem(Integer.parseInt(((MPPReturnToHutEvent) e).getProbId()));
                    ProblemResponse r = new ProblemResponse(p);
                    // if the last problem is not a topic intro and it isn't solved, resume it (which means ending it and beginning it again)
                    // TODO:   The problem is that ending it causes the student model to update as if an incorrect problem were submitted.
                    if (!p.isIntro() && !state.isProblemSolved()) {
                        new TutorPage(info,smgr).createTutorPageForResumingPreviousProblem(e.getElapsedTime(), 0, e.getTopicId(), r, "practice", state.getCurProbType(),
                                true, p.getResource(), null, false, p.getId(), this.showMPP,smgr.getPageLocale().getLanguage());
                        new TutorLogger(smgr).logMPPEvent(e,p.getId());
                        return;
                    }
                }
            }
            //  Can't resume the last problem because it was solved or something else non-resumable.  Consider this a regular NextProblem event now.
            // Unfortunately,  this could result in either a problem or an intervention or topic intro which means the client needs to be given
            // the necessary info so it can start its page correctly.
            PedagogicalModel pedMod = smgr.getPedagogicalModel();
            NextProblemEvent npe = new NextProblemEvent(e.getElapsedTime(),0);
            Response r;
            if (state.isInChallengeMode())
                r = pedMod.processChallengeModeNextProblemRequest(npe);
            else if (state.isInReviewMode())
                r = pedMod.processReviewModeNextProblemRequest(npe);
            else
                r= pedMod.processNextProblemRequest(npe);
            smgr.getStudentState().save();
            if (r instanceof ProblemResponse)
                new TutorPage(info,smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), (ProblemResponse) r, "practice",
                        state.getCurProbType(), true, ((ProblemResponse) r).getProblem().getResource(), null, false, state.getCurProblem(), this.showMPP);

            else if (r instanceof InterventionResponse) {

                new TutorPage(info, smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), (InterventionResponse) r, "practice",
                        state.getCurProbType(), true, null, null, false, state.getCurProblem(), this.showMPP);
            }
            new TutorLogger(smgr).logMPPEvent(e,state.getCurProblem());

        }
        else if (e instanceof MPPContinueTopicEvent) {
            // If the user uses MPP to break out of a review or challenge and goes to continue something, we have to turn off the rev
            // or chal in the state. \
            //we need to pass solved=true so that it won't set continueUnsolvedProblem to true
            smgr.getStudentState().setInReviewMode(false);
            smgr.getStudentState().setInChallengeMode(false);
            PedagogicalModel pedMod = smgr.getPedagogicalModel();
            NextProblemEvent npe = new NextProblemEvent(e.getElapsedTime(),0,e.getTopicId());

            String lastLoc = "ContinueTopic";
            lastLoc = ((MPPContinueTopicEvent) e).getLocation() + lastLoc;   // will be either MPP or Dashboard
            npe.setIsEnteringPracticeArea(true);
            npe.setLastLocation(lastLoc);
            e.setUserInput(Integer.toString(e.getTopicId()));
            String typ = smgr.getStudentState().getCurProbType();
            int lastProbId =  smgr.getStudentState().getCurProblem(); // must do before next line because it clears curProb
            // problem:  this wipes out the student problemstate so that state.curProb = -1
            // MPP is used to switch topics, reset the state for a new topic
            if (e.getTopicId() != smgr.getStudentState().getCurTopic())
                smgr.getStudentState().newTopic(); // resets all the variables that track a topic for a student
            InternalEvent beginningOfTopicEvent = new BeginningOfTopicEvent(npe,e.getTopicId());
            TutorModel tutMod = pedMod.getTutorModel();
            Response r =  tutMod.processInternalEvent(beginningOfTopicEvent);
            new TutorPage(info,smgr).createTutorPageForResponse(e.getElapsedTime(), 0, e.getTopicId(), r, "practice", typ, true, null, false, lastProbId, this.showMPP);
            new TutorLogger(smgr).logMPPEvent(e,lastProbId);
//            new TutorPage(info,smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), -1, "practice", Problem.PRACTICE, state.getCurProbType(), true, null, null, false);

        }

        else if (e instanceof MPPReviewTopicEvent) {
            smgr.getStudentState().setInChallengeMode(false);
            PedagogicalModel pedMod = smgr.getPedagogicalModel();
            NextProblemEvent npe = new NextProblemEvent(e.getElapsedTime(),0,e.getTopicId());
            String lastLoc = "ReviewTopic";
            lastLoc = ((MPPReviewTopicEvent) e).getLocation() + lastLoc;   // will be either MPP or Dashboard
            npe.setIsEnteringPracticeArea(true);
            npe.setLastLocation(lastLoc);
            npe.setMode(PedagogicalModel.REVIEW_MODE);
            int lastProbId =  smgr.getStudentState().getCurProblem();
            String typ = smgr.getStudentState().getCurProbType();
            ProblemResponse r = (ProblemResponse) pedMod.processReviewModeNextProblemRequest(npe);
            Problem p = r.getProblem();
            if (e.getTopicId() != smgr.getStudentState().getCurTopic())
                smgr.getStudentState().newTopic();
            e.setUserInput(Integer.toString(e.getTopicId()));
            smgr.getStudentModel().newProblem(state,p);
            smgr.getStudentModel().save();
            new TutorPage(info,smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), r, "practice",  typ, true, p.getResource(), null, false, lastProbId, this.showMPP);
            new TutorLogger(smgr).logMPPEvent(e, lastProbId);
        }

        else if (e instanceof MPPChallengeTopicEvent) {
            smgr.getStudentState().setInReviewMode(false);
            PedagogicalModel pedMod = smgr.getPedagogicalModel();
            NextProblemEvent npe = new NextProblemEvent(e.getElapsedTime(),0,e.getTopicId());
            npe.setMode(PedagogicalModel.CHALLENGE_MODE);
            String lastLoc = "ChallengeTopic";
            lastLoc = ((MPPChallengeTopicEvent) e).getLocation() + lastLoc;   // will be either MPP or Dashboard
            npe.setIsEnteringPracticeArea(true);
            npe.setLastLocation(lastLoc);
            int lastProbId =  smgr.getStudentState().getCurProblem();
            String typ = smgr.getStudentState().getCurProbType();
            Response r =  pedMod.processChallengeModeNextProblemRequest(npe);
            if (r instanceof ProblemResponse) {
                ProblemResponse rr = (ProblemResponse) r ;
                Problem p = rr.getProblem();
                if (e.getTopicId() != smgr.getStudentState().getCurTopic())
                    smgr.getStudentState().newTopic();
                e.setUserInput(Integer.toString(e.getTopicId()));

                smgr.getStudentModel().newProblem(state,p);
                smgr.getStudentModel().save();
                new TutorPage(info,smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), rr,
                        "practice",  typ, true, p != null ? p.getResource() : null, null, false, lastProbId, this.showMPP);
                new TutorLogger(smgr).logMPPEvent(e,lastProbId);
            }

            //we need to pass solved=true so that it won't set continueUnsolvedProblem to true
//            new TutorPage(info,smgr).createTutorPageFromState(e.getElapsedTime(), 0, e.getTopicId(), -1, "challenge", Problem.PRACTICE, state.getCurProbType(), true, null, null, false);
        }

        else if (e instanceof MPPTryProblemEvent) {
            MPPTryProblemEvent ee = (MPPTryProblemEvent) e;
            smgr.getStudentState().setInReviewMode(false);
            smgr.getStudentState().setInChallengeMode(false);
            NextProblemEvent npe = new NextProblemEvent(e.getElapsedTime(),0,Integer.toString(ee.getProbId()),Problem.PRACTICE);
            String lastLoc = "MPPTryProblem";
            npe.setIsEnteringPracticeArea(true);
            npe.setLastLocation(lastLoc);
            npe.setTopicToForce(e.getTopicId());  // N.B. Student is NOT FORCING THIS TOPIC.   It is passed for information only
            e.setUserInput(Integer.toString(e.getTopicId())); // for logging
            PedagogicalModel pedMod = smgr.getPedagogicalModel();
            int lastProbId =  smgr.getStudentState().getCurProblem();
            // When user was in topicIntro prior to going to MPP this isn't correct.
            String typ = smgr.getStudentState().getCurProbType();
            //ProblemResponse r = pedMod.getProblemSelectedByStudent(npe);
            
            // They request that trying a problem have the effect of switching to that topic
            InternalEvent beginningOfTopicEvent = new BeginningOfTopicEvent(npe,e.getTopicId());
            TutorModel tutMod = pedMod.getTutorModel();
            Response xx =  tutMod.processInternalEvent(beginningOfTopicEvent);
            ProblemResponse r = (ProblemResponse) pedMod.processMPPSelectProblemRequest(npe);
            Problem p = r.getProblem();
            smgr.getStudentModel().newProblem(state,r.getProblem());

            smgr.getStudentModel().save();
            e.setUserInput(Integer.toString(r.getProblem().getId()));
            //we need to pass solved=true so that it won't set continueUnsolvedProblem to true
//            new TutorPage(info,smgr).createTutorPageFromState(ee.getElapsedTime(), 0, ee.getTopicId(), ee.getProbId(), "practice", Problem.PRACTICE, state.getCurProbType(), true, null, null, false);
            // This means that the problem or topic intro that was playing prior to the MPP will have an endEvent
            // sent
            new TutorPage(info,smgr).createTutorPageFromState(ee.getElapsedTime(), 0, ee.getTopicId(), r, "practice",  typ, true, p.getResource(), null, false, lastProbId, this.showMPP);
            new TutorLogger(smgr).logMPPEvent(e,lastProbId);
        }
    }
}
