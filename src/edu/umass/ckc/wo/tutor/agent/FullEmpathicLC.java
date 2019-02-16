package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:04:18 AM
 * This Empathetic LC will show the emotion the student recently reported
 */
public abstract class FullEmpathicLC extends EmotionalLC {
    public final String frustratedCombo1 = "frustratedCombo1";
    public final String frustratedCombo2 = "frustratedCombo2";
    public final String anxiousCombo1 = "anxiousCombo1";
    public final String anxiousCombo2 = "anxiousCombo2";

    String[] emotions = new String[]{
            "interestHigh",
            "interestLow",   //Note: Studies during Spring 09 included these neg. clips
            "excitedHigh",
            "excitedLow",
            "frustrationHigh",
            "frustrationLow",
            "confidenceHigh",
            "confidenceLow"};

    /**
     * When a next-problem event happens, this character will select movie clips (clips) and insert
     * them into the response (r).
     *
     * @param
     * @return
     */
    public Response processNextProblemRequest(SessionManager smgr, NextProblemEvent e, Response r) throws Exception {
        AffectStudentModel sm;
        try {
            sm = (AffectStudentModel) smgr.getStudentModel();
        } catch (Exception ex) {
            throw new DeveloperException("You must use an AffectStudentModel when learning companions are part of the pedagogy");
        }
        selectEmotions(sm,r, smgr);  // adds media into clips property
        getBestClip(clips);
        addCharacterControl(r);
        return r;
    }


    public List<String> selectEmotions(AffectStudentModel sm, Response r, SessionManager smgr) throws Exception {

        // TopicIntros tend to have audio.   We can't return a clip that also has audio or there will be double audio.  Must return idle
        // When we are doing an intervention we don't want the character acting up either.
        if (r instanceof InterventionResponse) {
            clips.add("idle");
            return clips;
        }
        else if (r instanceof ProblemResponse)
            clips = super.selectEmotions(sm,r,smgr);
        // If no clips other than idle are returned by the super, do the below.
        if (!containsClips(clips)) {
            // only make the character react 25% of the time.
            if (java.lang.Math.random() < 0.25) {

                if (sm.getLastReportedEmotion().equals(AffectStudentModel.FRUSTRATED) || sm.getLastReportedEmotion().equals(AffectStudentModel.FRUSTACION) ) {
                    if (isHigh(sm.getLastReportedEmotionValue())) {    //Highly frustrated
                        if (java.lang.Math.random() < 0.5) {
                            clips.add(frustratedCombo1);
                            return clips;
                        } else {
                            clips.add(frustratedCombo2);
                            return clips;
                        }

                    } else if (isLow(sm.getLastReportedEmotionValue()))   //Not frustrated
                        clips.add(emotions[FLOW]);
                } else if (sm.getLastReportedEmotion().equals(AffectStudentModel.EXCITED)) {
                    if (isLow(sm.getLastReportedEmotionValue()))
                        clips.add(emotions[EXCITED]);
                } else if (sm.getLastReportedEmotion().equals(AffectStudentModel.INTERESTED)) {
                    if (isHigh(sm.getLastReportedEmotionValue()))
                        clips.add(emotions[INTERESTED]);
                }

                if (sm.getLastReportedEmotion().equals(AffectStudentModel.CONFIDENT)|| sm.getLastReportedEmotion().equals(AffectStudentModel.CONFIANZA)) {
                    if (isHigh(sm.getLastReportedEmotionValue()))
                        clips.add(emotions[CONFIDENT]);

                    else if (isLow(sm.getLastReportedEmotionValue())) {
                        if (java.lang.Math.random() < 0.5) {
                            clips.add(anxiousCombo1);
                            return clips;
                        } else {
                            clips.add(anxiousCombo2);
                            return clips;
                        }
                    }
                }
            }
            // If nothing from above is returned
            // 10% of the time it gives a message learning and attitudes
            if (java.lang.Math.random() < 0.10) {
                List genAttrList = Arrays.asList(generalAttribution);
                Collections.shuffle(genAttrList);

                clips.add((String) genAttrList.get(0));
            }

            if (clips.size() == 0) {
                clips.add("idle");
                return clips;
            }
        }
        return clips;
    }


    private boolean isLow(int emotionValue) {
        if ( emotionValue < 3 && emotionValue > 0 )
            return true ;

        return false ;
    }

    private boolean isHigh(int emotionValue) {
        if ( emotionValue > 3 )
            return true ;

        return false ;
    }
}
