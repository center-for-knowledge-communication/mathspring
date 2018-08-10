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
public class LessonStrategyComponentParams extends PedagogicalModelParameters {
    public static final int MAX_NUM_PROBS_PER_TOPIC = 8;
    public static final int MIN_NUM_PROBS_PER_TOPIC = 3;
    public static final int MAX_TIME_IN_TOPIC = 10 * 60 * 1000;
    public static final int MIN_TIME_IN_TOPIC = 30 * 1000;
    public static final int CONTENT_FAILURE_THRESHOLD = 1;
    public static final double TOPIC_MASTERY = 0.85;
    public static final int DIFFICULTY_RATE = 2;
    public static final int EXTERNAL_ACTIVITY_TIME_TRHRESHOLD = 10;   // given in minutes
    public static final TopicModelParameters.frequency DEFAULT_TOPIC_INTRO_FREQ = TopicModelParameters.frequency.always;
    public static final TopicModelParameters.frequency DEFAULT_EXAMPLE_FREQ = TopicModelParameters.frequency.always;
    public static final int DEFAULT_PROBLEM_REUSE_INTERVAL_SESSIONS =3;
    public static final int DEFAULT_PROBLEM_REUSE_INTERVAL_DAYS =10;
    public static final boolean DEFAULT_SHOW_MPP =true;






    private TopicModelParameters.frequency topicIntroFrequency;
    private TopicModelParameters.frequency topicExampleFrequency;
    private String ccss;
    private double difficultyRate ; // this is the divisor that the problem selector uses to find increase/decrease its index into the
    private int maxNumberProbs ;   // list of available problems
    private int minNumberProbs ;
    private long maxTimeInTopic;   // this is in milliseconds
    private long minTimeInTopic;    // in milliseconds
    private int contentFailureThreshold ; // the number of times it will select a problem within this topic when it can't meet
    // the easier/harder/same criteria.   Once exceeded, jump topics
    private double topicMastery;
//    private int externalActivityTimeThreshold;
//    private boolean showExampleFirst;

//    private boolean showAllExample;
//    private boolean showTopicIntro;
//    private boolean singleTopicMode = false;
    private int problemReuseIntervalSessions;
    private int problemReuseIntervalDays;
    private boolean showMPP=true;
    private String lessonStyle;

    private List<SCParam> scParams;

    public LessonStrategyComponentParams(List<SCParam> params) {
        this.scParams = params;
    }



}