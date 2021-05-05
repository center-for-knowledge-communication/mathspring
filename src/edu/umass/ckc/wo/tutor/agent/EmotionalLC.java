package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.response.AttemptResponse;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.tutorhut.InputResponseEvent;
import edu.umass.ckc.wo.event.tutorhut.ContinueEvent;
import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;

import java.util.*;
import java.sql.SQLException;


/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:04:18 AM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank	04-28-21	Issue #432R2 add video and example messages to 'incorrectStrategic' message set
 */
public abstract class EmotionalLC extends BaseLearningCompanion
{

    public static final int INTERESTED = 0 ;
    public static final int BORED = 1 ;
    public static final int EXCITED = 2 ;
    public static final int DEPRESSED = 3 ;
    public static final int FRUSTRATED = 4 ;
    public static final int FLOW = 5 ;
    public static final int CONFIDENT = 6 ;
    public static final int ANXIOUS = 7 ;

    static final int HIGHEFFORT_TIME_THRESHOLD = 60000 ;
    static final int LOWEFFORT_TIME_THRESHOLD = 10000 ;

    public static final String[] connectors  = {
            "connector1",
            "connector2",
            "connector3",
            "connector4"
    }  ;

    public static final String[] anxietyVerbalAck  = {
            "anxVerbalAck1",
            "anxVerbalAck2"
    }  ;

    public static final String[] frustrationVerbalAck  = {
            "frustratedVerbalAck2",
            "anxFrustrVerbalAck1",
            "anxFrustrVerbalAck2",
            "anxFrustrVerbalAck3"
    }  ;

    public static final String[] frustrAnxAttribution = {
            "generalAttribution4", //I think that the most important thing is to have an open mind...
            "generalAttribution2", //Did you know that when we practice to learn new math skills...
            "generalAttribution5",  //I think that more important than getting the problem right...
            "noEffortAttribution2", //There is no one way to learn something new...
            "effortAttribution1", //Keep in mind that when you are struggling with a new idea or skill...
            "effortAttribution2", //Struggling in problems is actually a good thing...
    } ;

    public static final String[] generalAttribution = {
            "generalAttribution1", //Did you know that when we learn something new our brain...
            "generalAttribution2", //Did you know that when we practice to learn new math skills...
            "generalAttribution3", //Hey I found out that there are myths about math...
            "generalAttribution4", //I think that the most important thing is to have an open mind...
            "generalAttribution5"  //I think that more important than getting the problem right...
    } ;

    public static final String[] noEffortAttribution = {
            "noEffortAttribution2" //There is no one way to learn something new...
    } ;

    public static final String[] effortAttribution = {
            "effortAttribution1", //Keep in mind that when you are struggling with a new idea or skill...
            "effortAttribution2", //Struggling in problems is actually a good thing...
            "generalAttribution4", //I think that the most important thing is to have an open mind...
    } ;

    public static final String[] correctResponses = {
        "correct1",
        "correct2",
        "correct3",
        "correct4",
    } ;

    public static final String[] correctNeutral = {
        "correct5",
        "correct6"} ;

    public static final String[] correctEffortResponses = {
        "correctEffort1",
        "correctEffort2",
    } ;

    public static final String[] correctNoEffortResponses = {
    //    "correctNoEffort1",
    //    "correctNoEffort2",
            "correct5",
            "correct6",
    } ;


    public static final String[] incorrectStrategic = {
        "incorrect1",
        "incorrect2",
        "incorrect3",
        "incorrect4",
        "example",
        "video"
    } ;

    public static final String[] incorrectNeutral = {
        "incorrectAttribution1",
        "incorrect5",
        "incorrectAttribution2",
        "incorrectAttribution3",
        "incorrect6",
        "incorrectAttribution5"
    } ;

    public static final String[] incorrectEffortResponses = {
        "incorrectEffort1",
        "incorrectEffort2",
    } ;


    public static final String[] incorrectNoEffortResponses = {
        "incorrectNoEffort1",
        "incorrectNoEffort2",
    } ;

    /**
     * When a next-problem event happens, this character will select movie clips (clips) and insert
     * them into the response (r).
     * @param smgr
     * @param e
     * @param r
     * @return
     */
//    public Response processNextProblemRequest(SessionManager smgr, NextProblemEvent e, Response r) throws Exception {
//        super.processNextProblemRequest(smgr,e,r);
//        // the above line might add characterControl clips to the response
//        List<String> l = selectEmotions(smgr.getStudentModel());
//        String[] clips = l.toArray(new String[l.size()]);
//        for (String c: clips)
//            addEmotion(c);
//
//        if (r instanceof ProblemResponse ) {
//            Problem p = ((ProblemResponse) r).getProblem();
//            // if content runs out, p can be null
//            if (p == null)
//                return r;
//            if (p.isExample() && getClips().indexOf("idea") != -1)
//                getClips().remove("idea");
//        }
//        // a hack.   Flash doesn't want idea followed by idle and the selectEmotions is returning
//        // idle as a failsafe default.
//        if (getClips().indexOf("idea") != -1)
//            getClips().remove("idle");
//        // if idle is in other than the last position , remove it and add it to the end of the list
//        if (getClips().indexOf("idle") != -1 && getClips().indexOf("idle") == getClips().size()-1)  {
//            getClips().remove("idle");
//            addEmotion("idle");
//        }
//        addLearningCompanionToResponse(r);
//        return r;
//    }




    public Response processInputResponse(SessionManager smgr, InputResponseEvent e, Response r) throws Exception {
        // the above line might add characterControl clips to the response
        List<String> l = selectEmotions(smgr.getStudentModel(),r);
        String[] myclips = l.toArray(new String[l.size()]);
        for (String c: myclips)
            addEmotion(c);
        // a hack.   Flash doesn't want idea followed by idle and the selectEmotions is returning
        // idle as a failsafe default.
        if (getClips().indexOf("idea") != -1)
            getClips().remove("idle");

        if (getClips().indexOf("idle") != -1 && getClips().indexOf("idle") == getClips().size()-1)  {
            getClips().remove("idle");
            addEmotion("idle");
        }
        getBestClip(clips);
        addLearningCompanionToResponse(r);
        return r;
    }

    public Response processContinueRequest(SessionManager smgr, ContinueEvent e, Response r) throws Exception {
        // the above line might add characterControl clips to the response
        List<String> l = selectEmotions(smgr.getStudentModel(),r);
        String[] myclips = l.toArray(new String[l.size()]);
        for (String c: myclips)
            addEmotion(c);
        // a hack.   Flash doesn't want idea followed by idle and the selectEmotions is returning
        // idle as a failsafe default.
        if (getClips().indexOf("idea") != -1)
            getClips().remove("idle");

        if (getClips().indexOf("idle") != -1 && getClips().indexOf("idle") == getClips().size()-1)  {
            getClips().remove("idle");
            addEmotion("idle");
        }
        getBestClip(clips);
        addLearningCompanionToResponse(r);
        return r;
    }

    public List<String> selectEmotions(StudentModel sm, Response r) throws Exception {
        return new ArrayList<String>();
    }

    public String getEmotionToDisplay(AffectStudentModel sm) {return null;}

    // fills the counts array (which runs parallel to mcsToChooseFrom) with the counts
    // returns the index of the minimum in this array.
    public int getCounts (String[] mcsToChooseFrom, List<String> clipsSeen, List<String> clipCounters, int[] counts) {
        int minIndex = 0;
        for (int j=0; j<mcsToChooseFrom.length; j++) {
            String clip = mcsToChooseFrom[j];
            for (int i=0; i<clipsSeen.size(); i++) {
                String mc = clipsSeen.get(i);
                String count = clipCounters.get(i);
                if (mc.equals(clip)) {
                    counts[j] = Integer.parseInt(count);
                    break;
                }
            }
            if (counts[j] < counts[minIndex])
                minIndex = j;
        }
        return minIndex;
    }

    // alter the history by incrementing the clips counter (possibly inserting clip/counter if not there already)
    private void incrementClipCountsInState (StudentState st, List<String> clipNames, String clip, int count) throws SQLException {
        int pos =0;
        // look for the clip in the list of clips in the history.
        // if found, increment its counter and exit
        for (int i=0;i<clipNames.size();i++) {
            if (clipNames.get(i).equals(clip)) {
                // increment the count of the clip at position i+1 (position is 1-based in woprops)
                st.incrClipCount(i+1,count);
                return;
            }
        }
        // if the clip is not in the history, then insert it and the counter
        st.setClipCount(clip,count);
    }


    public String getLeastUsedClip (SessionManager smgr, String[] selectableClipNames) throws SQLException {
        List<String> desiredClips = new ArrayList<String>();
        for (String c: selectableClipNames)
            desiredClips.add(c);
        StudentState st = smgr.getStudentState();
        // get the students clip history
        List<String> seenClips = st.getClips();
        desiredClips.removeAll(seenClips); // desired set now has seen clips removed
        // All clips have been seen at least once and now we want the least-used clip.
        // The student state (sessionState actually) has two lists (clips and clipcounts) that keep a counter of how many times each clip has been shown
        //  Select the least used clip.
        if (desiredClips.size() == 0 && selectableClipNames.length>0)
        {
            int min=st.getLcClipCount(selectableClipNames[0]); // initial min value is the counter for the first clipname
            String minClipName = selectableClipNames[0];    // initial minClipName is the first item
            // Go through all the clips in the selectable list and figure out which 0has the min counter.   Remember the clip name and the counter
            for (String clipName : selectableClipNames)   {
                int n = st.getLcClipCount(clipName);
                if (n < min) {
                    min = n;
                    minClipName = clipName;
                }
            }
            // increment the counter for the clip that was min.
            st.setLcClipCount(minClipName,min+1);
//            incrementClipCountsInState(st,seenClips,minClipName,min+1);
            return minClipName;
        }
        // Some clips have not been used, so we pick one at random and insert it into the state with a count
        else
        {
            int ix = new Random().nextInt(desiredClips.size());
            String clip=desiredClips.get(ix);
//            incrementClipCountsInState(st,seenClips,clip,1);
            st.setLcClipCount(clip,1);
            return clip;

        }

    }

    public String getRandomItem(String[] stringArray) {
        int chosen_one ;
        chosen_one = (new Double (java.lang.Math.random() * stringArray.length)).intValue() ;
        return stringArray[chosen_one] ;
    }



//    public String AppendBehavior (String prior, String newBehavior) {
//
//        if ( prior == null )
//            return newBehavior ;
//
//        return prior + ", " + newBehavior ;
//    }



    // DM this has been modified so that it doesn't randomly pick an emotion (movie clip).   It uses
    // woproperties to keep counters of each movie clip the user has seen and then shows the least used one.
    public Response processAttempt (SessionManager smgr, AttemptEvent attemptEvent, AttemptResponse r) throws Exception {
        String userInput = attemptEvent.getUserInput() ;
        StudentState state = smgr.getStudentState();
        // N.B. The Pedagogical Model that calls this must grade the user input and set the isCorrect value
        // within the AttemptEvent - we no longer rely on what Flash passes for isCorrect
        //If it is an irncorrect response.
        if ( ! attemptEvent.isCorrect())   {
            if ( smgr.getStudentState().getNumAttemptsOnCurProblem() == 2 ) { //Only the first time
                // If there was No Effort: chosen incorrectly, fast, on the first attempt
                if ( java.lang.Math.random() > .5 ) {//state.getProbElapsedTime() < LOWEFFORT_TIME_THRESHOLD ) {
                    r=getIncorrectNoEffortResponse(r, smgr) ;
                    getBestClip(clips);
                    addLearningCompanionToResponse(r);
                    return r;
                }

                //Incorrect with Effort: Incorrect on the first attempt, but spent a long time on the problem so far
                if (  java.lang.Math.random() > .5 ) { //state.getProbElapsedTime() > HIGHEFFORT_TIME_THRESHOLD ) {
                    r= getIncorrectEffortResponse(r, smgr) ;
                    getBestClip(clips);
                    addLearningCompanionToResponse(r);
                    return r;
                }
                r= getIncorrectResponse(r, smgr) ;
                getBestClip(clips);
                addLearningCompanionToResponse(r);
                return r;
            }

            addEmotion("idle") ;
            getBestClip(clips);
            addLearningCompanionToResponse(r);
            return r ;
        }
        else {

            // If there was No Effort: answer one answer fast, and it was correct
            if ( state.getProbElapsedTime() < LOWEFFORT_TIME_THRESHOLD && state.getNumAttemptsOnCurProblem() == 1) {
                 r= getCorrectNoEffortResponse(r, smgr) ;
                getBestClip(clips);
                addLearningCompanionToResponse(r);
                return r;
            }
            //Correct with Effort: More than 30 seconds in the problem, and has seen help
            if (   /*state.getProbElapsedTime() > HIGHEFFORT_TIME_THRESHOLD && */ state.getNumHintsGivenOnCurProblem() > 0 &&
                    state.getNumAttemptsOnCurProblem()<3) {
                r= getCorrectEffortResponse(r, smgr) ;
                getBestClip(clips);
                addLearningCompanionToResponse(r);
                return r;
            }

        }
        r= getCorrectResponse(r, smgr) ;
        getBestClip(clips);
        addLearningCompanionToResponse(r);
        return r;
    }

    protected AttemptResponse getIncorrectEffortResponse(AttemptResponse r, SessionManager smgr) throws SQLException {
         if ( java.lang.Math.random()<0.35 ) {
//            r.addEmotion(getRandomItem(EmotionalLC.incorrectEffortResponses)) ;
            addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectEffortResponses)) ;
            return r ;
         }

         if ( java.lang.Math.random()<0.35 ) {
//             r.addEmotion(getRandomItem(EmotionalLC.incorrectStrategic)) ;
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectStrategic)) ;
//             r.addEmotion(getRandomItem(EmotionalLC.effortAttribution)) ;
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.effortAttribution)) ;
             return r ;
         }
         return getIncorrectResponse(r, smgr) ;
     }


     protected AttemptResponse getIncorrectResponse(AttemptResponse r, SessionManager smgr) throws SQLException {

         if ( java.lang.Math.random()<0.3 ) {
            addEmotion("idle") ;
            return r ;
         }

         if ( java.lang.Math.random()<0.5 ) {
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectStrategic)) ;
             return r ;
         }

         addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectNeutral)) ;
//   Too much general Attribution!
//         r.addEmotion(getLeastUsedClip(smgr,EmotionalLC.generalAttribution)) ;
         return r ;
     }



    protected AttemptResponse getCorrectResponse(AttemptResponse r, SessionManager smgr) throws SQLException {
         if ( java.lang.Math.random()<0.3 ) {
            addEmotion("idle") ;
            return r ;
         }

          if ( java.lang.Math.random()<0.6 ) {
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.correctResponses)) ;
             return r ;
         }

         addEmotion(getLeastUsedClip(smgr,EmotionalLC.correctNeutral)) ;
// Too much general Attribution!
//         r.addEmotion(getLeastUsedClip(smgr,EmotionalLC.generalAttribution)) ;
         return r ;
     }



    protected AttemptResponse getCorrectNoEffortResponse(AttemptResponse r, SessionManager smgr) throws SQLException {
         if ( java.lang.Math.random()<0.25 )
             return getCorrectResponse(r, smgr) ;

//         r.addEmotion(getRandomItem(EmotionalLC.correctNoEffortResponses)) ;
         addEmotion(getLeastUsedClip(smgr,EmotionalLC.correctNoEffortResponses)) ;
         return r ;
     }



    protected AttemptResponse getCorrectEffortResponse(AttemptResponse r, SessionManager smgr) throws SQLException {
         if ( java.lang.Math.random()<0.25 )
             return getCorrectResponse(r, smgr) ;

//         r.addEmotion(getRandomItem(EmotionalLC.correctEffortResponses)) ;
         addEmotion(getLeastUsedClip(smgr,EmotionalLC.correctEffortResponses)) ;
         return r ;
     }


     //Avoid being neutral when no effort and incorrect
     protected AttemptResponse getIncorrectNoEffortResponse(AttemptResponse r, SessionManager smgr) throws SQLException {

         if ( java.lang.Math.random()<0.25 ) {
//             r.addEmotion(getRandomItem(EmotionalLC.incorrectNoEffortResponses)) ;
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectNoEffortResponses)) ;
             return r ;
         }

         if ( java.lang.Math.random()<0.15 ) {
//             r.addEmotion(getRandomItem(EmotionalLC.noEffortAttribution));
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.noEffortAttribution));
             return r ;
         }

         if ( java.lang.Math.random()<0.15 ) {
//             addEmotion(getRandomItem(EmotionalLC.incorrectStrategic)) ;
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.incorrectStrategic)) ;
//             addEmotion(getRandomItem(EmotionalLC.noEffortAttribution)) ;
             addEmotion(getLeastUsedClip(smgr,EmotionalLC.noEffortAttribution)) ;
         }

         return getIncorrectResponse(r, smgr) ;
     }


}
