package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.response.*;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/13/14
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SemiEmpathicLC extends EmotionalLC {

    public Response processNextProblemRequest(SessionManager smgr, NextProblemEvent e, Response r) throws Exception {

        AffectStudentModel sm;
        try {
            sm = (AffectStudentModel) smgr.getStudentModel();
        } catch (Exception ex) {
            throw new DeveloperException("You must use an AffectStudentModel when learning companions are part of the pedagogy");
        }
        selectEmotions(sm,r,smgr); // adds media to clips property
        getBestClip(clips);
        addCharacterControl(r);
        return r;

    }




        public List<String> selectEmotions(AffectStudentModel m, Response r, SessionManager smgr) throws Exception {
            if (r instanceof InterventionResponse) {
                clips.add("idle");
                return clips;
            }
            else if (r instanceof ProblemResponse)
                clips = super.selectEmotions(m,r,smgr);
            // If no clips other than idle are returned by the super, do the below.
            if (!containsClips(clips)) {
                //Every 5 problems, it trains attributions
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
