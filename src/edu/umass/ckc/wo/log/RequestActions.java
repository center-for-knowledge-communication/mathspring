package edu.umass.ckc.wo.log;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Jan 30, 2009
 * Time: 9:46:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RequestActions {

    public static final String BEGIN_PROBLEM = "BeginProblem";
    public static final String RESUME_PROBLEM = "ResumeProblem";
    public static final String FORMALITY_BEGIN_PROBLEM = "Formality_BeginProblem";
    public static final String END_PROBLEM = "EndProblem";
    public static final String FORMALITY_END_PROBLEM = "Formality_EndProblem";
    public static final String SHOW_INTERVENTION = "ShowIntervention";
    public static final String END_INTERVENTION = "EndIntervention";
    public static final String BEGIN_XACT = "BeginExternalActivity";
    public static final String END_XACT = "EndExternalActivity";
    public static final String BEGIN_EXAMPLE = "BeginExample";
    public static final String END_EXAMPLE = "EndExample";
    public static final String NEXT_PROBLEM = "NextProblem";
    public static final String PREVIEW_PROBLEM = "previewProblem";
    public static final String HINT = "Hint";
    public static final String FORMALITY_HINT = "Formality_Hint";
    public static final String SHOW_EXAMPLE = "ShowExample";
    public static final String SHOW_VIDEO = "ShowVideo";
    public static final String SHOW_SOLVE_PROBLEM = "ShowSolveProblem";
    public static final String ATTEMPT = "Attempt";
    public static final String FORMALITY_ATTEMPT = "Formality_Attempt";
    public static final String CONTINUE = "Continue";
    public static final String INPUT_RESPONSE = "InputResponse";
    public static final String CLICK_CHARACTER = "ClickCharacter";
    public static final String ELIMINATE_CHARACTER = "EliminateCharacter";
    public static final String SHOW_CHARACTER = "ShowCharacter";
    public static final String MUTE_CHARACTER = "MuteCharacter";
    public static final String UN_MUTE_CHARACTER = "UnMuteCharacter";
    public static final String CLICK_TOOLBAR = "ClickToolbar";
    public static final String UNDO = "Undo";
    public static final String ARROW = "Arrow";
    public static final String CLEAR = "Clear";
    public static final String BEGIN_TEXT = "BeginText";
    public static final String END_TEXT = "EndText";
    public static final String BEGIN_DRAWING = "BeginDrawing";
    public static final String END_DRAWING = "EndDrawing";
    public static final String BEGIN_ERASING = "BeginErasing";
    public static final String END_ERASING = "EndErasing";
    public static final String POLL_TUTOR = "PollTutor";
    public static final String READ_PROBLEM = "ReadProblem";
    public static final String HOME = "Home";
     public static final String MPP = "MyProgressPage";
    public static final String FORMALITY_EXTERNAL_ACTIVITY = "Formality_ExternalActivity";
    public static final String CHOOSE_PEDAGOGY = "choosePedagogy";

    public static final String[] TUTOR_HUT_ACTIVITIES = { BEGIN_PROBLEM,FORMALITY_BEGIN_PROBLEM,END_PROBLEM, FORMALITY_END_PROBLEM,
            SHOW_INTERVENTION,END_INTERVENTION,  BEGIN_EXAMPLE,END_EXAMPLE, NEXT_PROBLEM,HINT,FORMALITY_HINT,SHOW_EXAMPLE,SHOW_VIDEO,
           SHOW_SOLVE_PROBLEM, ATTEMPT,FORMALITY_ATTEMPT,CONTINUE,INPUT_RESPONSE,CLICK_CHARACTER, ELIMINATE_CHARACTER,SHOW_CHARACTER,
              MUTE_CHARACTER, UN_MUTE_CHARACTER,CLICK_TOOLBAR,UNDO,ARROW,CLEAR, BEGIN_TEXT,END_TEXT,BEGIN_DRAWING, END_DRAWING,
               BEGIN_ERASING,END_ERASING,POLL_TUTOR,READ_PROBLEM,FORMALITY_EXTERNAL_ACTIVITY, BEGIN_XACT, END_XACT, MPP};



}
