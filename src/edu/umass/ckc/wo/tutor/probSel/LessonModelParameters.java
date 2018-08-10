package edu.umass.ckc.wo.tutor.probSel;


import edu.umass.ckc.wo.strat.SCParam;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;
import org.jdom.Element;

import java.util.List;

/**
 * Handles lessons that are not the standard Topic based teaching.   We use this for a Common Core approach (when the lessondefinition has a style=ccss
 * User: marshall
 * Date: Apr 18, 2011
 * Time: 9:42:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class LessonModelParameters {

    public static final String MAX_NUMBER_PROBS = "maxNumberProbs";
    public static final String MIN_NUMBER_PROBS = "minNumberProbs";
    public static final String MAX_TIME_MINUTES = "maxTimeMinutes";
    public static final String MIN_TIME_MINUTES = "minTimeMinutes";

    private String ccss;
    protected long maxTimeMs = 15 * 60 * 1000;
    protected long minTimeMs = 3 * 60 * 1000;
//    protected int maxTimeMinutes = 15;  // default to 15 minute s
//    protected int minTimeMinutes = 3;  // default to 3 minute s
    protected int maxProbs = 10;            // default to 10 problems
    protected int minProbs = 1;            // default to 1 problem
    protected double desiredMastery = 0.95;  // default to 95%


    // The below should not be here because they are not part of what goes on during the events related
    // to processing the TopicModel.  These are about selecting problems which is PedagogicalModel processing
    // and lives in the PedagogicalModelParameters.   The only reason these are here is because the orderTopics.jsp
    // allows the user to tweak these values and this is the value (erroneously) being sent into that.


    public LessonModelParameters () {

    }

    /*    Given something like from the lessonDefinition table
        <controlParameters>
            <minNumberProbs>3</minNumberProbs>
            <maxNumberProbs>10</maxNumberProbs>
            <maxTimeMinutes>15</maxTimeMinutes>
            <minTimeMinutes>3</minTimeMinutes>
            <difficultyRate>3</difficultyRate>
            <contentFailureThreshold>2</contentFailureThreshold>
        </controlParameters>
     */
    public LessonModelParameters (Element controlElt) {
        Element maxProbsElt = controlElt.getChild(MAX_NUMBER_PROBS);
        if (maxProbsElt != null)
            this.maxProbs =  Integer.parseInt(maxProbsElt.getTextTrim());
        Element minProbsElt = controlElt.getChild(MIN_NUMBER_PROBS);
        if (maxProbsElt != null)
            this.minProbs =  Integer.parseInt(minProbsElt.getTextTrim());
        Element maxTimeElt = controlElt.getChild(MAX_TIME_MINUTES);
        if (maxTimeElt != null)
            setMaxTimeMinutes(Integer.parseInt(maxTimeElt.getTextTrim()));
        Element minTimeElt = controlElt.getChild(MIN_TIME_MINUTES);
        if (minTimeElt != null)
            setMinTimeMinutes(Integer.parseInt(minTimeElt.getTextTrim()));

    }

    /** WHen using TUtorStrategy to build the LessonModel, the params are given in a list of
     * SCParam objects and it is simpler to build this LessonModelParameters from it
     * @param scParams
     */
    public LessonModelParameters (List<SCParam> scParams) {
        for (SCParam p: scParams) {
            setParam(p);
        }
    }

    protected void setParam(SCParam p) {
        if (p.getName().equalsIgnoreCase(MAX_NUMBER_PROBS))
            this.maxProbs = Integer.parseInt(p.getValue());
        else if (p.getName().equalsIgnoreCase(MIN_NUMBER_PROBS))
            this.minProbs = Integer.parseInt(p.getValue());
        else if (p.getName().equalsIgnoreCase(MAX_TIME_MINUTES))
            this.setMaxTimeMinutes(Integer.parseInt(p.getValue()));
        else if (p.getName().equalsIgnoreCase(MIN_TIME_MINUTES))
            this.setMinTimeMinutes(Integer.parseInt(p.getValue()));


    }

    // overload the params of this with those given for class.
    public LessonModelParameters overload(ClassTutorConfigParams classParams) {
        if (classParams.getMaxProbs() > 0)
            this.maxProbs =  classParams.getMaxProbs();
        if (classParams.getMinProbs() > 0)
            this.minProbs =  classParams.getMinProbs();
        if (classParams.getMaxTimeMinutes() > 0)
            this.maxTimeMs =  classParams.getMaxTimeMs();
        if (classParams.getMinTimeMinutes() > 0)
            this.minTimeMs =  classParams.getMinTimeMs();
        if (classParams.getDesiredMastery() > 0)
            this.desiredMastery = classParams.getDesiredMastery();

        return this;
    }

    public LessonModelParameters overload (PedagogyParams userParams) {
        this.ccss = userParams.getCcss();
        setMaxTimeMinutes(userParams.getMaxTimeMinutes()) ;
        this.maxProbs = userParams.getMaxProbs() ;
        this.desiredMastery = userParams.getMastery(); // It's not a topic mastery in this case.  It will be a mastery for a standard
        return this;
    }

    public String getCcss() {
        return ccss;
    }

    public long getMaxTimeMs() {
        return maxTimeMs;
    }

    public long getMinTimeMs() {
        return minTimeMs;
    }

    public int getMaxTimeMinutes() {
        return (int) (this.maxTimeMs == -1 ? -1 : (this.maxTimeMs / (1000*60)));
    }

    public long getMinTimeMinutes() {
        return (int) (this.minTimeMs == -1 ? -1 : (this.minTimeMs / (1000*60)));
    }

    public void setMinTimeMinutes (int minutes) {
        this.minTimeMs = minutes * 60 * 1000;
    }

    public void setMaxTimeMinutes (int minutes) {
        this.maxTimeMs = minutes * 60 * 1000;
    }

    public int getMaxProbs() {
        return maxProbs;
    }

    public double getDesiredMastery() {
        return desiredMastery;
    }

    public int getMinProbs() {
        return minProbs;
    }






    public void setMaxTimeMs(long maxTimeMs) {
        this.maxTimeMs = maxTimeMs;
    }

    public void setMinTimeMs(long minTimeMs) {
        this.minTimeMs = minTimeMs;
    }

    public void setMaxProbs(int maxProbs) {
        this.maxProbs = maxProbs;
    }

    public void setMinProbs(int minProbs) {
        this.minProbs = minProbs;
    }

    public void setDesiredMastery(double desiredMastery) {
        this.desiredMastery = desiredMastery;
    }


}