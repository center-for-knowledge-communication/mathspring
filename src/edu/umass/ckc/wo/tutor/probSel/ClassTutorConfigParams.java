package edu.umass.ckc.wo.tutor.probSel;


import edu.umass.ckc.wo.strat.SCParam;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;
import org.jdom.Element;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Apr 18, 2011
 * Time: 9:42:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassTutorConfigParams extends LessonModelParameters {
    public static final int MAX_NUM_PROBS_PER_TOPIC = 8;
    public static final int MIN_NUM_PROBS_PER_TOPIC = 3;
    public static final int MAX_TIME_IN_TOPIC = 10 * 60 * 1000;
    public static final int MIN_TIME_IN_TOPIC = 30 * 1000;
    public static final int CONTENT_FAILURE_THRESHOLD = 1;
    public static final double TOPIC_MASTERY = 0.85;
    public static final String TOPIC_MASTERY_PARAM = "topicMastery";
    public static final String MAX_TIME_IN_TOPIC_SECS = "maxTimeInTopicSecs";
    public static final String MIN_TIME_IN_TOPIC_SECS = "minTimeInTopicSecs";
    public static final int DIFFICULTY_RATE = 2;
    public static final frequency DEFAULT_TOPIC_INTRO_FREQ = frequency.always;
    public static final frequency DEFAULT_EXAMPLE_FREQ = frequency.always;




    public enum frequency {
        never,
        oncePerSession,
        always
    }


    private frequency topicIntroFrequency;    //  These frequencies are no longer set in the lesson
    private frequency topicExampleFrequency;  // They are now set in the Interventions that are part of the lesson
    private String ccss;
    private String lessonStyle;
    private boolean singleTopicMode;
    private InterleavedProblemSetParams interleaveParams=null;  // If interleaved problem sets are part of the lesson, this will be non-null
    protected double difficultyRate = 2; // 2 gives the splitting denominator in the binary search using 1/2 as the fraction
    protected int contentFailureThreshold ;




    // USed to take in inputs from the teacher tools where we set the class config topic (lesson) parameters that get
    // inserted into the classconfig table.
    public ClassTutorConfigParams(long maxTimeInTopic, int contentFailureThreshold, double topicMastery, int minNumberProbs,
                                  long minTimeInTopic, double difficultyRate, int maxNumberProbs
    ) {
        this.maxProbs = maxNumberProbs;
        this.maxTimeMs = maxTimeInTopic;
        this.contentFailureThreshold = contentFailureThreshold;
        this.desiredMastery = topicMastery;
        this.minProbs= minNumberProbs;
        this.minTimeMs= minTimeInTopic;
        this.difficultyRate= difficultyRate;
        this.topicIntroFrequency = null;
        this.topicExampleFrequency = null;
        this.lessonStyle = "topics";
    }



    public ClassTutorConfigParams(boolean setDefaultValues) {
        if (setDefaultValues) {
            this.maxProbs = MAX_NUM_PROBS_PER_TOPIC;
            this.maxTimeMs = MAX_TIME_IN_TOPIC;
            this.contentFailureThreshold = CONTENT_FAILURE_THRESHOLD;
            this.desiredMastery = TOPIC_MASTERY;
            this.minProbs = MIN_NUM_PROBS_PER_TOPIC;
            this.minTimeMs = MIN_TIME_IN_TOPIC;
            this.difficultyRate = DIFFICULTY_RATE;
            this.topicIntroFrequency = DEFAULT_TOPIC_INTRO_FREQ;
            this.topicExampleFrequency = DEFAULT_EXAMPLE_FREQ;
        }
        else {
            this.maxProbs = -1;
            this.maxTimeMs = -1;
            this.contentFailureThreshold = -1;
            this.desiredMastery = -1;
            this.minProbs = -1;
            this.minTimeMs = -1;
            this.difficultyRate = -1;
            this.topicIntroFrequency = null;
            this.topicExampleFrequency = null;
        }
    }

    public void setContentFailureThreshold(int contentFailureThreshold) {
        this.contentFailureThreshold = contentFailureThreshold;
    }

    public void setDifficultyRate(double difficultyRate) {
        this.difficultyRate = difficultyRate;
    }

    public double getDifficultyRate() {
        return difficultyRate;
    }

    public int getContentFailureThreshold() {
        return contentFailureThreshold;
    }

    // gets the given TopicIntro frequency from a string
    public static frequency convertTopicIntroFrequency (String s) {
        if (s != null && !s.trim().equalsIgnoreCase(""))
            return frequency.valueOf(s);
        else return DEFAULT_TOPIC_INTRO_FREQ;
    }

    // gets the given TopicIntro frequency from a string
    public static frequency convertExampleFrequency (String s) {
        if (s != null && !s.trim().equalsIgnoreCase(""))
            return frequency.valueOf(s);
        else return DEFAULT_EXAMPLE_FREQ;
    }


    public boolean isSingleTopicMode() {
        return singleTopicMode;
    }


    public String getCcss(){
        return ccss;
    }

    public void setMaxTimeInTopicSecs(int maxTimeInTopicSecs) {
        this.maxTimeMs = maxTimeInTopicSecs * 1000;
    }



    public void setMinTimeInTopicSecs(int minTimeInTopicSecs) {

        this.minTimeMs = minTimeInTopicSecs * 1000;
    }

    public void setTopicIntroFrequency(String topicIntroFrequency) {
        this.topicIntroFrequency = ClassTutorConfigParams.frequency.valueOf(topicIntroFrequency);
    }

    public frequency getTopicIntroFrequency() {
        return topicIntroFrequency;
    }

    public void setTopicExampleFrequency(String topicExampleFrequency) {
        this.topicExampleFrequency = ClassTutorConfigParams.frequency.valueOf(topicExampleFrequency);
    }

    public frequency getTopicExampleFrequency() {
        return topicExampleFrequency;
    }


    public boolean isTopicLessonStyle() {
        return lessonStyle.equals("topics");
    }


    public void setLessonStyle(String lessonStyle) {
        this.lessonStyle = lessonStyle;
    }

    public String getLessonStyle() {
        return lessonStyle;
    }

    public boolean showInterleavedProblemSets () {
        return this.getInterleaveParams() != null;
    }

    public InterleavedProblemSetParams getInterleaveParams() {
        return interleaveParams;
    }
}