package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:04:18 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RandomEmpathicLC extends EmotionalLC {

    String[] emotions = new String[]{
            "high-interest",
            "low-interest",     //Note: Studies during Spring 09 included these neg. clips
            "high-excitement",
            "low-excitement",
            "high-frustration",
            "low-frustration",
            "high-confidence",
            "low-confidence"};

    String[] emotion_states = new String[]{
            "frustrated",
            "excited",
            "interested",
            "confident"};

    public List<String> selectEmotions(StudentModel sm) {
        List<String> r = new ArrayList<String>();
        String fake_emotion = getRandomEmotion();
        double fake_emotion_value = (java.lang.Math.random() * 4) + 1; //min=1, max=5

        String behaviorToReturn = null;


        if (java.lang.Math.random() < 0.5) {

            if (fake_emotion.equals("frustrated")) {
                if (fake_emotion_value > 3) {
                    if (java.lang.Math.random() < 0.5) {
                        r.add(emotions[FRUSTRATED]);
                        r.add(getRandomItem(frustrationVerbalAck));
                        r.add(getRandomItem(connectors));
                        r.add(getRandomItem(frustrAnxAttribution));

                        return r ;
                    }
                    if (java.lang.Math.random() < 0.5) {
                        r.add(getRandomItem(frustrationVerbalAck));
                        r.add(getRandomItem(connectors));
                        r.add(getRandomItem(frustrAnxAttribution));

                        return r;
                    }
                } else if (fake_emotion_value < 3)
                    r.add(emotions[FLOW]);
            }


            if (fake_emotion.equals("excited")) {
                if (fake_emotion_value < 3)
                    r.add(emotions[EXCITED]);
//                else if (fake_emotion_value > 3)
//                    r.add(clips[DEPRESSED]);
            }

            if (fake_emotion.equals("interested")) {
                if (fake_emotion_value > 3)
                    r.add(emotions[INTERESTED]);
//                else if (fake_emotion_value < 3)
//                    r.add(clips[BORED]);
            }

            if (fake_emotion.equals("confident")) {
                if (fake_emotion_value > 3)
                    r.add(emotions[CONFIDENT]);
                
                else if (fake_emotion_value < 3) {
                    if (java.lang.Math.random() < 0.5)  {
                        r.add(emotions[ANXIOUS]);
                        r.add(getRandomItem(anxietyVerbalAck));
                        r.add(getRandomItem(connectors));
                        r.add(getRandomItem(frustrAnxAttribution));

                        return r ;
                    }

                    if (java.lang.Math.random() < 0.5) {
                        r.add(getRandomItem(anxietyVerbalAck));
                        r.add(getRandomItem(connectors));
                        r.add(getRandomItem(frustrAnxAttribution));

                        return r;
                    }
                }
            }
        }

        //Every 5 problems, it trains attributions
        if (java.lang.Math.random() < 0.10) {
            List genAttrList = Arrays.asList(generalAttribution);
            Collections.shuffle(genAttrList);

            r.add((String) genAttrList.get(0));
        }

        if (r.size() == 0) {
            r.add("idle");

        }
        return r;
    }


//    public String getEmotionToDisplay(AffectStudentModel sm) {
//
//        String fake_emotion = getRandomEmotion();
//        double fake_emotion_value = (java.lang.Math.random() * 4) + 1; //min=1, max=5
//
//        String behaviorToReturn = null;
//
//
//        if (java.lang.Math.random() < 0.5) {
//
//            if (fake_emotion.equals("frustrated")) {
//                if (fake_emotion_value > 3) {
//                    if (java.lang.Math.random() < 0.5)
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, clips[FRUSTRATED]);
//
//                    if (java.lang.Math.random() < 0.5) {
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(frustrationVerbalAck));
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(connectors));
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(frustrAnxAttribution));
//
//                        return behaviorToReturn;
//                    }
//                } else if (fake_emotion_value < 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[FLOW]);
//            }
//
//
//            if (fake_emotion.equals("excited")) {
//                if (fake_emotion_value < 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[EXCITED]);
//                else if (fake_emotion_value > 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[DEPRESSED]);
//            }
//
//            if (fake_emotion.equals("interested")) {
//                if (fake_emotion_value > 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[INTERESTED]);
//                else if (fake_emotion_value < 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[BORED]);
//            }
//
//            if (fake_emotion.equals("confident")) {
//                if (fake_emotion_value > 3)
//                    behaviorToReturn = AppendBehavior(behaviorToReturn, clips[CONFIDENT]);
//                else if (fake_emotion_value < 3) {
//                    if (java.lang.Math.random() < 0.5)
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, clips[ANXIOUS]);
//
//                    if (java.lang.Math.random() < 0.5) {
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(anxietyVerbalAck));
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(connectors));
//                        behaviorToReturn = AppendBehavior(behaviorToReturn, getRandomItem(frustrAnxAttribution));
//
//                        return behaviorToReturn;
//                    }
//                }
//            }
//        }
//
//        //Every 5 problems, it trains attributions
//        if (java.lang.Math.random() < 0.10) {
//            List genAttrList = Arrays.asList(generalAttribution);
//            Collections.shuffle(genAttrList);
//
//            behaviorToReturn = AppendBehavior(behaviorToReturn, (String) genAttrList.get(0));
//        }
//
//        if (behaviorToReturn == null)
//            return "idle";
//
//        return behaviorToReturn;
//    }


    private String getRandomEmotion() {
        List emotionList = Arrays.asList(emotion_states);
        Collections.shuffle(emotionList);

        return (String) emotionList.get(0);
    }
}
