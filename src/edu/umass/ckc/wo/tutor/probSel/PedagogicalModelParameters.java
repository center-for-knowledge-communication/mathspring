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
public class PedagogicalModelParameters {
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
    public static final String PROBLEM_REUSE_INTERVAL_SESSIONS = "problemReuseIntervalSessions";
    public static final String PROBLEM_REUSE_INTERVAL_DAYS = "problemReuseIntervalDays";
    public static final String DIFFICULTY_RATE_PARAM = "difficultyRate";
    public static final String DISPLAY_MY_PROGRESS_PAGE = "displayMyProgressPage";
    public static final String EXTERNAL_ACTIVITY_TIME_THRESHOLD_MINS = "externalActivityTimeThresholdMins";
    public static final String EXAMPLE = "Example";
    public static final String PRACTICE = "Practice";


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
    private int externalActivityTimeThreshold;
    private boolean showExampleFirst;

    private boolean showAllExample;
    private boolean showTopicIntro;
    private boolean singleTopicMode = false;
    private int problemReuseIntervalSessions;
    private int problemReuseIntervalDays;
    private boolean showMPP=true;
    private String lessonStyle;

    public PedagogicalModelParameters (List<SCParam> params) {
        for (SCParam p : params)
            setParam(p);
    }

    private void setParam(SCParam p) {
        if (p.getName().equalsIgnoreCase(PROBLEM_REUSE_INTERVAL_SESSIONS))
            problemReuseIntervalSessions = Integer.parseInt(p.getValue());
        else if (p.getName().equalsIgnoreCase(PROBLEM_REUSE_INTERVAL_DAYS))
            problemReuseIntervalDays = Integer.parseInt(p.getValue());
        else if (p.getName().equalsIgnoreCase(DIFFICULTY_RATE_PARAM))
            difficultyRate = Double.parseDouble(p.getValue());
        else if (p.getName().equalsIgnoreCase(DISPLAY_MY_PROGRESS_PAGE))
            showMPP = Boolean.parseBoolean(p.getValue());
    }

    // overload the params of this with those given for class.
    public PedagogicalModelParameters overload(PedagogicalModelParameters classParams) {
        if (classParams == null) return this;
        if (classParams.getDifficultyRate() > 0)
            this.difficultyRate =classParams.getDifficultyRate();
        if (classParams.getMaxNumberProbs() > 0)
            this.maxNumberProbs =classParams.getMaxNumberProbs();
        if (classParams.getMinNumberProbs() > 0)
            this.minNumberProbs =classParams.getMinNumberProbs();
        if (classParams.getMaxTimeInTopic() > 0)
            this.maxTimeInTopic =classParams.getMaxTimeInTopic();
        if (classParams.getMinTimeInTopic() > 0)
            this.minTimeInTopic =classParams.getMinTimeInTopic();
        if (classParams.getContentFailureThreshold() > 0)
            this.contentFailureThreshold =classParams.getContentFailureThreshold();
        if (classParams.getTopicMastery() > 0)
            this.topicMastery =classParams.getTopicMastery();
        if (classParams.getExternalActivityTimeThreshold() > 0)
            this.externalActivityTimeThreshold =classParams.getExternalActivityTimeThreshold();
        if (classParams.getTopicIntroFrequency() != null)
            this.topicIntroFrequency =classParams.getTopicIntroFrequency();
        if (classParams.getTopicExampleFrequency() != null)
            this.topicExampleFrequency =classParams.getTopicExampleFrequency();
        if (classParams.getProblemReuseIntervalSessions() > 0)
            this.problemReuseIntervalSessions =classParams.getProblemReuseIntervalSessions();
        if (classParams.getProblemReuseIntervalDays() > 0)
            this.problemReuseIntervalDays =classParams.getProblemReuseIntervalDays();
        if (classParams.getLessonStyle() != null )
            this.lessonStyle = classParams.getLessonStyle();
        return this;

    }

    public PedagogicalModelParameters overload (PedagogyParams userParams) {
        if (userParams == null)
            return this;
        if (userParams.isShowIntro())
            this.topicIntroFrequency=TopicModelParameters.frequency.oncePerSession;
        String mode = userParams.getMode();
        if (mode != null && mode.equalsIgnoreCase("Example"))  {
            topicExampleFrequency = TopicModelParameters.frequency.always;
        }
        else if (mode != null && userParams.getMode().equalsIgnoreCase("Practice")) {
            topicExampleFrequency = TopicModelParameters.frequency.never;
        }
        else {
            topicExampleFrequency = TopicModelParameters.frequency.oncePerSession;
        }
        topicIntroFrequency = userParams.isShowIntro() ? TopicModelParameters.frequency.oncePerSession : TopicModelParameters.frequency.never;
        maxTimeInTopic = userParams.getMaxTime();
        minTimeInTopic = 0;
        maxNumberProbs = userParams.getMaxProbs();
        topicMastery = userParams.getMastery();
        minNumberProbs = 1;
        this.singleTopicMode = userParams.isSingleTopicMode();
        // If we get passed no topic from Assistments, then this translates into setting the maxtime in the topic to 0
        // so we'll show the one forced problem and out.
        if (userParams.getTopicId() == -1)
            this.setMaxTimeInTopic(0);
        return this;
    }


    /*
        This kind of overloads the params which are all defaults.  It takes the XML from inside the the pedagogy
        and overwrites the parameters that they tend to redefine.
     */
    public void setParameters(Element p) {
        Element c;
        String s;

        c = p.getChild(EXTERNAL_ACTIVITY_TIME_THRESHOLD_MINS);
        if (c != null) {
            s = c.getValue();
            int externalActivityTimeThresholdMins = Integer.parseInt(s);
            this.setExternalActivityTimeThreshold(externalActivityTimeThresholdMins);
        }

        c = p.getChild(PROBLEM_REUSE_INTERVAL_SESSIONS);
        if (c != null) {
            s = c.getValue();
            this.setProblemReuseIntervalSessions(s);
        }
        c = p.getChild(PROBLEM_REUSE_INTERVAL_DAYS);
        if (c != null) {
            s = c.getValue();
            this.setProblemReuseIntervalDays(s);
        }
        c = p.getChild(DISPLAY_MY_PROGRESS_PAGE);
        if (c != null) {
            s = c.getValue();
            boolean showMpp = Boolean.parseBoolean(s);
            this.setShowMPP(showMpp);
        }
    }





    public PedagogicalModelParameters(long maxTimeInTopic, int contentFailureThreshold, double topicMastery, int minNumberProbs,
                                      long minTimeInTopic, int difficultyRate, int externalActivityTimeThreshold, int maxNumberProbs,
                                      boolean showTopicIntro, boolean showExampleProblemFirst) {
        this.maxNumberProbs = maxNumberProbs;
        this.maxTimeInTopic = maxTimeInTopic;
        this.contentFailureThreshold = contentFailureThreshold;
        this.topicMastery = topicMastery;
        this.minNumberProbs= minNumberProbs;
        this.minTimeInTopic= minTimeInTopic;
        this.difficultyRate= difficultyRate;
        this.externalActivityTimeThreshold= externalActivityTimeThreshold;
        this.showExampleFirst= showExampleProblemFirst;
        this.showTopicIntro = showTopicIntro;
        this.topicIntroFrequency = DEFAULT_TOPIC_INTRO_FREQ;
        this.topicExampleFrequency = DEFAULT_EXAMPLE_FREQ;
        this.problemReuseIntervalSessions =DEFAULT_PROBLEM_REUSE_INTERVAL_SESSIONS;
        this.problemReuseIntervalDays =DEFAULT_PROBLEM_REUSE_INTERVAL_DAYS;
    }

    // Called with parameters read from TeacherAdmin's class config
    public PedagogicalModelParameters(long maxTimeInTopic, int contentFailureThreshold, double topicMastery, int minNumberProbs,
                                      long minTimeInTopic, double difficultyRate, int externalActivityTimeThreshold, int maxNumberProbs,
                                      boolean showTopicIntro, boolean showExampleProblemFirst, TopicModelParameters.frequency topicIntroFreq, TopicModelParameters.frequency exampleFreq,
                                      int probReuseIntervalSessions, int probReuseIntervalDays, String lessonStyle) {
        this.maxNumberProbs = maxNumberProbs;
        this.maxTimeInTopic = maxTimeInTopic;
        this.contentFailureThreshold = contentFailureThreshold;
        this.topicMastery = topicMastery;
        this.minNumberProbs= minNumberProbs;
        this.minTimeInTopic= minTimeInTopic;
        this.difficultyRate= difficultyRate;
        this.externalActivityTimeThreshold= externalActivityTimeThreshold;
        this.showExampleFirst= showExampleProblemFirst;
        this.showTopicIntro = showTopicIntro;
        this.topicIntroFrequency = topicIntroFreq;
        this.topicExampleFrequency = exampleFreq;
        this.problemReuseIntervalSessions = probReuseIntervalSessions;
        this.problemReuseIntervalDays = probReuseIntervalDays;
        this.lessonStyle = lessonStyle;
    }

    public PedagogicalModelParameters() {
        this.maxNumberProbs = MAX_NUM_PROBS_PER_TOPIC;
        this.maxTimeInTopic = MAX_TIME_IN_TOPIC;
        this.contentFailureThreshold = CONTENT_FAILURE_THRESHOLD;
        this.topicMastery = TOPIC_MASTERY;
        this.minNumberProbs=MIN_NUM_PROBS_PER_TOPIC;
        this.minTimeInTopic=MIN_TIME_IN_TOPIC;
        this.difficultyRate=DIFFICULTY_RATE;
        this.externalActivityTimeThreshold = EXTERNAL_ACTIVITY_TIME_TRHRESHOLD;
        this.showExampleFirst= true;
        this.showTopicIntro = true;
        this.topicIntroFrequency = DEFAULT_TOPIC_INTRO_FREQ;
        this.topicExampleFrequency = DEFAULT_EXAMPLE_FREQ;
        this.problemReuseIntervalSessions =DEFAULT_PROBLEM_REUSE_INTERVAL_SESSIONS;
        this.problemReuseIntervalDays =DEFAULT_PROBLEM_REUSE_INTERVAL_DAYS;
        this.showMPP = DEFAULT_SHOW_MPP;
    }



    // gets the given TopicIntro frequency from a string
    public static TopicModelParameters.frequency convertTopicIntroFrequency (String s) {
        if (s != null && !s.trim().equalsIgnoreCase(""))
            return TopicModelParameters.frequency.valueOf(s);
        else return DEFAULT_TOPIC_INTRO_FREQ;
    }

    // gets the given TopicIntro TopicModelParameters.frequency from a string
    public static TopicModelParameters.frequency convertExampleFrequency (String s) {
        if (s != null && !s.trim().equalsIgnoreCase(""))
            return TopicModelParameters.frequency.valueOf(s);
        else return DEFAULT_EXAMPLE_FREQ;
    }

    public PedagogicalModelParameters(String mode, boolean showIntro, long maxtime, int maxprobs, boolean singleTopicMode) {
        this();
        if (mode.equalsIgnoreCase(EXAMPLE))  {
            showAllExample = true;
            showExampleFirst = true;
        }
        else if (mode.equalsIgnoreCase(PRACTICE)) {
            showExampleFirst = false;
            showAllExample = false;
        }
        else {
            showExampleFirst = true;
            showAllExample = false;
        }
        showTopicIntro = showIntro;
        maxTimeInTopic = maxtime;
        minTimeInTopic = 0;
        maxNumberProbs = maxprobs;
        minNumberProbs = 1;
        this.singleTopicMode = singleTopicMode;
    }


    // Assistments calls pass in a set of configuration params that seek to control a user's session.   These params override the ones
    // that are defined for the Assistments class that the user is in.
    public PedagogicalModelParameters(String mode, boolean showIntro, long maxtime, int maxprobs, boolean singleTopicMode, String ccss) {
        this();
        if (mode.equalsIgnoreCase(EXAMPLE))  {
            showAllExample = true;
            showExampleFirst = true;
        }
        else if (mode.equalsIgnoreCase(PRACTICE)) {
            showExampleFirst = false;
            showAllExample = false;
        }
        else {
            showExampleFirst = true;
            showAllExample = false;
        }
        showTopicIntro = showIntro;
        maxTimeInTopic = maxtime;
        minTimeInTopic = 0;
        maxNumberProbs = maxprobs;
        minNumberProbs = 1;
        this.singleTopicMode = singleTopicMode;
        this.ccss = ccss;
    }

    public int getMaxNumberProbs() {
        return maxNumberProbs;
    }

    public void setMaxNumberProbs(int maxNumberProbs) {
        this.maxNumberProbs = maxNumberProbs;
    }

    public long getMaxTimeInTopic() {
        return maxTimeInTopic;
    }

    public void setMaxTimeInTopic(long maxTimeInTopic) {
        this.maxTimeInTopic = maxTimeInTopic;
    }

    public int getMaxTimeInTopicMinutes () {
        return (int) (maxTimeInTopic / 60000);
    }


    public void setMaxTimeInTopicMinutes (double minutes) {
        maxTimeInTopic = (long) (minutes * 1000 * 60);
    }

    public void setMinTimeInTopicMinutes (double minutes) {
        minTimeInTopic = (long) (minutes * 1000 * 60);
    }

    public int getContentFailureThreshold() {
        return contentFailureThreshold;
    }

    public void setContentFailureThreshold(int contentFailureThreshold) {
        this.contentFailureThreshold = contentFailureThreshold;
    }

    public double getTopicMastery() {
        return topicMastery;
    }

    public void setTopicMastery(double topicMastery) {
        this.topicMastery = topicMastery;
    }

    public int getMinNumberProbs() {
        return minNumberProbs;
    }

    public long getMinTimeInTopic() {
        return minTimeInTopic;
    }

    public long getMinTimeInTopicMinutes() {
        return minTimeInTopic/60000;
    }

    public double getDifficultyRate() {
        return difficultyRate;
    }

    public void setDifficultyRate(int difficultyRate) {
        this.difficultyRate = difficultyRate;
    }

    public int getExternalActivityTimeThreshold() {
        return externalActivityTimeThreshold;
    }

    public void setExternalActivityTimeThreshold(int externalActivityTimeThreshold) {
        this.externalActivityTimeThreshold = externalActivityTimeThreshold;
    }

    public boolean isShowExampleFirst() {
        return showExampleFirst;
    }

    public void setShowExampleFirst(boolean showExampleFirst) {
        this.showExampleFirst = showExampleFirst;
    }

    public boolean isShowTopicIntro() {
        return showTopicIntro;
    }

    public void setShowTopicIntro(boolean showTopicIntro) {
        this.showTopicIntro = showTopicIntro;
    }

    public boolean isShowAllExample() {
        return showAllExample;
    }



    public boolean isSingleTopicMode() {
        return singleTopicMode;
    }


    public String getCcss(){
        return ccss;
    }

    public void setMaxTimeInTopicSecs(int maxTimeInTopicSecs) {
        this.maxTimeInTopic = maxTimeInTopicSecs * 1000;
    }

    public void setMinNumberProbs(int minNumberProbs) {
        this.minNumberProbs = minNumberProbs;
    }



    public void setMinTimeInTopicSecs(int minTimeInTopicSecs) {
        this.minTimeInTopic = minTimeInTopicSecs * 1000;
    }

    public void setTopicIntroFrequency(String topicIntroFrequency) {
        this.topicIntroFrequency = TopicModelParameters.frequency.valueOf(topicIntroFrequency);
    }

    public TopicModelParameters.frequency getTopicIntroFrequency() {
        return topicIntroFrequency;
    }

    public void setTopicExampleFrequency(String topicExampleFrequency) {
        this.topicExampleFrequency = TopicModelParameters.frequency.valueOf(topicExampleFrequency);
    }

    public TopicModelParameters.frequency getTopicExampleFrequency() {
        return topicExampleFrequency;
    }

    public void setProblemReuseIntervalSessions(String problemReuseIntervalSessions) {
        this.problemReuseIntervalSessions = Integer.parseInt(problemReuseIntervalSessions);
    }

    public int getProblemReuseIntervalSessions() {
        return problemReuseIntervalSessions;
    }

    public void setProblemReuseIntervalDays(String problemReuseIntervalDays) {
        this.problemReuseIntervalDays = Integer.parseInt(problemReuseIntervalDays);
    }

    public int getProblemReuseIntervalDays() {
        return problemReuseIntervalDays;
    }

    public void setProblemReuseIntervalDays(int problemReuseIntervalDays) {
        this.problemReuseIntervalDays = problemReuseIntervalDays;
    }

    public boolean isShowMPP() {
        return showMPP;
    }

    public void setShowMPP(boolean showMPP) {
        this.showMPP = showMPP;
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


}