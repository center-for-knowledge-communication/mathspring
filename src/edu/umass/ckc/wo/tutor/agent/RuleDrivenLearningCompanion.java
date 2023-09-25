package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.lc.InferenceEngine;
import edu.umass.ckc.wo.lc.LCAction;
import edu.umass.ckc.wo.lc.LCRule;
import edu.umass.ckc.wo.lc.LCRuleset;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.TransactionLogger;
import edu.umass.ckc.wo.tutor.hintSel.StrategicHintSelector;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import net.sf.json.JSONObject;
import org.dom4j.rule.RuleSet;

import java.util.List;

/**
 * A learning companion that uses rules to define its behavior.   The learning companion is plugged into the pedagogy.
 *
 * User: marshall
 * Date: 5/10/16
 * Time: 8:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuleDrivenLearningCompanion extends LearningCompanion {

    private List<LCRuleset> ruleSets;
    private String charactersName;


    public RuleDrivenLearningCompanion(SessionManager smgr) {

    }

    public void addRuleSet (LCRuleset ruleSet) {
        this.ruleSets.add(ruleSet);
    }

    public void setRuleSets (List<LCRuleset> rulesets) {
        this.ruleSets = rulesets;
    }


    @Override
    public String getGender() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEthnicity() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCharactersName() {
        return this.charactersName;
    }

    public void setCharactersName(String charactersName) {
        this.charactersName = charactersName;
    }

    // Default implementation of methods that must be overidden by learning companions that are subclasses.
    //
    // Learning Companions must contribute to handling requests by adding movie clips to control the on-screen character.
    // The learning companion is passed the session manager, the request event, and the response that has been built so far.
    // The response has already been built by the pedagogical model's helpers (hint selector, problem selector, intervention selector).
    // The learning companion is passed the already built response (r) and it can do whatever processing it needs to do
    // (accessing the student model and student state through the session manager) so that it selects some movie clips to control
    // character.   The movie clip names should be added to the response and the response should be returned.
    // r.addCharacterControl(String[] clipNames)

    public Response processShowSolveProblemRequest (SessionManager smgr, ShowSolveProblemEvent e, HintSequenceResponse r) throws Exception {
        addLearningCompanionToResponse(r);
        return r;
    }


    public Response processNextProblemRequest (SessionManager smgr, NextProblemEvent e, Response r) throws Exception {
        return processEvent(smgr,e,r);
    }


//    public Response processNextProblemRequest (SessionManager smgr, NextProblemEvent e, Response r) throws Exception {
//        StudentState state = smgr.getStudentState();
//        if (r instanceof ProblemResponse) {
//            Problem p = ((ProblemResponse) r).getProblem();
//            // when content runs out,  p will be null
//            if (p == null)
//                return r;
//            if (p instanceof TopicIntro)
//                return r;
//            // if this is the first non-topicIntro add the solve-together
//            if  (state.getTopicNumProbsSeen() == 1)
//                pushEmotion("solve together");
//
//            if (p.isPractice() && p.hasStrategicHint())
//                addEmotion("idea");
//        }
//        addLearningCompanionToResponse(r);
//        return r;
//    }

    public Response processHintRequest (SessionManager smgr, HintEvent e, HintResponse r) throws Exception {
        return processEvent(smgr,e,r);
    }

    /**
     * Given a user event, this will go through all the rulesets in the pedagogy and apply rules that are
     * relevant to that event.
     * @param smgr
     * @param e
     * @param r
     * @return
     * @throws Exception
     */
    private Response processEvent (SessionManager smgr, TutorHutEvent e, Response r) throws Exception {
        //TODO not sure how multiple rulesets should be dealt with.  Is there some precedence among them or can
        //I just go through them in some undefined order and find the first rule that matches?
        // The problem is that if I just go through one and then the other, they aren't all treated with the same
        // importance.
        InferenceEngine ruleInterpreter = new InferenceEngine(smgr);
        LCRule rule = ruleInterpreter.runRulesForEvent(e,ruleSets);
        if (rule != null)    {
            LCAction act = rule.getAction();
            clips.add(act.getName()); // The LCAction has the name of the html lc action and this is all we really use
            addLearningCompanionToResponse(rule,r);

            return r;
        }
        else {
            // If no rule is applied, then set the character to idle by default.
            clips.add("idle");
            addLearningCompanionToResponse(null, r);
            return r;
        }
    }

    public void addLearningCompanionToResponse (LCRule rule, Response r) {
        JSONObject jo = r.getJSON();
        // eliminate the element if there are no clips (clips) to send
        if (clips.size() > 0) {
            for (String e: clips)
                jo.accumulate(LearningCompanion.LEARNING_COMPANION_JSON_ATTRIBUTE, getCharactersName()+"/"+e+".html");
            r.setCharacterControl(clips.get(0));
        }
        if (rule != null) {
            String msg =rule.getAction().getMsgText();
            msg = msg != null ? msg : "";
            jo.element(LEARNING_COMPANION_TEXT_MESSSAGE,msg);
        }


    }

    public Response processAttempt (SessionManager smgr, AttemptEvent e, AttemptResponse r) throws Exception {
        return processEvent(smgr,e,r);
    }

    @Override
    public Response processEndProblem(SessionManager smgr, EndProblemEvent e, Response r) throws Exception {
        return processEvent(smgr,e,r);
    }

    @Override
    public Response processBeginProblem(SessionManager smgr, BeginProblemEvent e, Response r) throws Exception {
        return processEvent(smgr,e,r);
    }

    @Override
    public Response processEndExample(SessionManager smgr, EndExampleEvent e, Response r) throws Exception {
        return processEvent(smgr,e,r);
    }

    public Response processInputResponse (SessionManager smgr, InputResponseEvent e, Response r) throws Exception {
        addLearningCompanionToResponse(r);
        return r;
    }
    public Response processContinueRequest (SessionManager smgr, ContinueEvent e, Response r) throws Exception {
        addLearningCompanionToResponse(r);
        return r;
    }

    // The basic response to a click on the character is to return the strategic hint if it hasn't already been played or
    // some indication that it has played or that there isn't one.  The BasicHintSelector is responsible for getting the hint.
    public Response processClickCharacterEvent(SessionManager smgr, ClickCharacterEvent e) throws Exception {
        // if the strategic hint has already been shown, return  strategicAlreadyPlayed
        if (smgr.getStudentState().isStrategicHintShown()) {
            Hint h = Hint.STRATEGIC_HINT_PLAYED;
            h.setProblemId(smgr.getStudentState().getCurProblem());
            ClickCharacterResponse r = new ClickCharacterResponse(h.getLabel(),h.getId());
            TransactionLogger.logClickCharacter(smgr.getConnection(), e, r, smgr.getStudentState().getCurProblem(), smgr.getStudentId(), smgr.getSessionNum());
            return r;

        }
        else {
            StrategicHintSelector hs= new StrategicHintSelector();
            hs.init(smgr);
            Hint h = hs.selectStrategicHint(smgr,smgr.getStudentState().getCurProblem());
            // possibly alter the label of the strategic hint to be customized based on the learning
            // companion being used (e.g. jakeStrategicHint, janeStrategicHint)
            setStrategicHintLabel(h);
            // return the hint (its either noStrategicHint or strategic_hint)
            ClickCharacterResponse r = new ClickCharacterResponse(h.getLabel(),h.getId());
            TransactionLogger.logClickCharacter(smgr.getConnection(), e, r, smgr.getStudentState().getCurProblem(), smgr.getStudentId(),smgr.getSessionNum());
            return r;
        }
    }

    // All learning companions need to say they are solving the problems with the student and to show interest in the
    // examples.  Otherwise, by default, idle.   Sub-classes will override this.
    protected List<String> selectEmotions(AffectStudentModel m, Response r, SessionManager smgr) throws Exception {
        if (r instanceof ProblemResponse) {
            Problem p = ((ProblemResponse) r).getProblem();
            // By request of Ivon 6/14
            if (smgr.getStudentState().getNumRealProblemsThisTutorSession() == 1)
            {
                clips.add("solveTogether");
                return clips;
            }
            if (p != null && p.isExample())  {
                clips.add("interestHigh");
                return clips;
            }
        }
        clips.add("idle");

        return clips;
    }

    @Override
    public String getMessageSelectionStrategy () {
        return "rules";  // this indicates that the messages are selected by the Java class itself (as opposed to a rule-set)
    }

	@Override
	public Response processTranslateProblemRequest(SessionManager smgr, TranslateProblemEvent e, Response r)
			throws Exception {
		// TODO Auto-generated method stub
		return processEvent(smgr,e,r);
	}

}
