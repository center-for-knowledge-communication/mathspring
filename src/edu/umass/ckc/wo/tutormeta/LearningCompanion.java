package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.response.AttemptResponse;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.tutor.response.HintSequenceResponse;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.content.Hint;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:04:18 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class LearningCompanion {

    //    protected String[] clipNames;
    protected List<String> clips = new ArrayList<String>();

    public static final String LEARNING_COMPANION_JSON_ATTRIBUTE = "learningCompanionFiles";
    public static final String LEARNING_COMPANION_TEXT_MESSSAGE = "lcTextMessage";

    public abstract String getGender() ;
    public abstract String getEthnicity() ;
    public abstract String getCharactersName() ;

    public abstract Response processShowSolveProblemRequest (SessionManager smgr, ShowSolveProblemEvent e, HintSequenceResponse r) throws Exception ;
    public abstract Response processClickCharacterEvent(SessionManager smgr, ClickCharacterEvent e) throws Exception;
    public abstract Response processContinueRequest (SessionManager smgr, ContinueEvent e, Response r) throws Exception;
    public abstract Response processInputResponse (SessionManager smgr, InputResponseEvent e, Response r) throws Exception;
    public abstract Response processAttempt (SessionManager smgr, AttemptEvent e, AttemptResponse r) throws Exception;
    public abstract Response processHintRequest (SessionManager smgr, HintEvent e, HintResponse r) throws Exception;
    public abstract Response processTranslateProblemRequest (SessionManager smgr, TranslateProblemEvent e, Response r) throws Exception;
    public abstract Response processNextProblemRequest (SessionManager smgr, NextProblemEvent e, Response r) throws Exception;
    public abstract Response processEndProblem (SessionManager smgr, EndProblemEvent e, Response r) throws Exception;
    public abstract Response processBeginProblem (SessionManager smgr, BeginProblemEvent e, Response r) throws Exception;
    public abstract Response processEndExample (SessionManager smgr, EndExampleEvent e, Response r) throws Exception;

    public Response processUncategorizedEvent (TutorHutEvent e, Response r) {
        clips.add("idle");
        this.addLearningCompanionToResponse(r);
        return r;
    }

    public void addEmotion (String emotion) {
        if ( ! clips.contains(emotion) )
            clips.add(emotion);
    }

    public void pushEmotion (String emotion) {
        clips.add(0, emotion);
    }

    public List<String> getClips() {
        return clips;
    }

    public void addCharacterControl (Response r) {
        JSONObject jo = r.getJSON();
        // eliminate the element if there are no clips (clips) to send
        if (clips.size() > 0) {
            for (String e: clips)
                jo.accumulate(LearningCompanion.LEARNING_COMPANION_JSON_ATTRIBUTE, getCharactersName()+"/"+e+".html");
            r.setCharacterControl(clips.get(0));
        }

    }

    public void addCharacterControl (JSONObject jo) {
        // eliminate the element if there are no clips (clips) to send
        if (clips.size() > 0) {
            for (String e: clips)
                jo.accumulate(LearningCompanion.LEARNING_COMPANION_JSON_ATTRIBUTE, getCharactersName()+"/"+e+".html");
        }

    }

    public void addLearningCompanionToResponse(Response r) {
        if (Settings.useLearningCompanions)  {
            addCharacterControl(r);

        }

    }


    public String getMessageSelectionStrategy () {
        return "programmatic";  // this indicates that the messages are selected by the Java class itself (as opposed to a rule-set)
    }





    protected void setStrategicHintLabel(Hint h) {
        h.setLabel(getCharactersName() + "_" + Hint.STRATEGIC_HINT_LABEL);
    }





}
