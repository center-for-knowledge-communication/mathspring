package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.interventions.ShowMPPIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import org.jdom.Element;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/2/14
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyProgressPageIS extends NextProblemInterventionSelector {
    MyState state;
    int interruptIntervalMin=-1;
    int interruptIntervalProblems = -1;
    Map<String,Bounds> emotionSettings = new HashMap<String, Bounds>(11);
    boolean checkEmotions=false;
    String emLowerBound=null;
    String emUpperBound=null;

    public MyProgressPageIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = new MyState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel)  {
        this.pedagogicalModel=pedagogicalModel;
        if (getParameter("selectProblem", this.params) != null)
            this.setBuildProblem(Boolean.parseBoolean(getParameter("selectProblem",this.params)));
        configure();

    }

    /**
     * Only configured using a config XML element like:
     * <config>
     *     <intervalCriteria type="numProblems | time | affect" val=v emotion=e lowerBound=l upperBound=u></intervalCriteria>
     * </config>

     */
    private void configure() {
        Element config = this.getConfigXML();
        if (config != null) {
            List<Element> intervalCritiaElts = config.getChildren("intervalCriteria");
            for (Element elt: intervalCritiaElts) {
                String t = elt.getAttributeValue("type");
                String v = elt.getAttributeValue("val");

                if (t.equals("numProblems"))
                    interruptIntervalProblems = Integer.parseInt(v);
                else if (t.equals("time"))
                    interruptIntervalMin = Integer.parseInt(v);
                else if (t.equals("affect")) {
                    checkEmotions=true;
                    String em = elt.getAttributeValue("emotion");
                    String lb = elt.getAttributeValue("lowerBound");
                    String ub = elt.getAttributeValue("upperBound");
                    Bounds b = new Bounds(lb,ub);
                    emotionSettings.put(em,b);
                }

            }
        }
    }

    @Override
    /**
     * The pedagogy configures the behavior of this selector with elements like:
     *
     * a <intervalCriteria type="numProblems" val="6"/>
     b <intervalCriteria type="time" val="10"/>
     c <intervalCriteria type="affect" emotion="Interest" lowerbound="3" upperbound="5"/>
     d <intervalCriteria type="affect" emotion="Confidence"  upperbound="3"/>

     You can include all three, or as few as 1.   The meaning is whichever of a or b occurs first (number of probs == 6 or time == 10 min)
     and then MPP is displayed for 1 problem.   If c is included, then whenever this condition arises (interest>= 3 <= 5) we show MPP for 1 problem.
     If other emotions are checked (d) we can see that whenever confidence is <= 3 we will show MPP.

     THe flaw with the emotions stuff is that it works off reported emotions which only change when the user is prompted about a specific emotion.
     So if this finds that confidence was reported to be 2 and we have a condition that says show the MPP if confidence <= 3,  this intervention
     will keep showing the MPP until the user is again queried about confidence AND he raises his confidence level.   So there is an interrelationship
     between the use of this intervention and the AskEmotion intervention (and presumably the tutor which would attempt to ameliorate the emotion that
     is out of whack)
     */
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        long now = System.currentTimeMillis();
        long timeSinceLastMPPDisplay = (now - state.getTimeOfLastIntervention())/(1000*60); // convert ms to min
        if (state.getTimeOfLastIntervention() <= 0) {
            timeSinceLastMPPDisplay = 0;
            state.setTimeOfLastIntervention(now);
        }
        int problemsSinceLastQuery =  state.getNumProblemsSinceLastIntervention();
        state.setNumProblemsSinceLastIntervention(problemsSinceLastQuery+1);


        NextProblemIntervention intervention=null;

        if ((this.interruptIntervalMin > -1 && timeSinceLastMPPDisplay >= this.interruptIntervalMin) ||
                (this.interruptIntervalProblems > -1 && problemsSinceLastQuery >= this.interruptIntervalProblems)) {
            intervention = new ShowMPPIntervention(this.isBuildProblem());
        }
        long lastInterventionForEmotions = state.getTimeOfLastInterventionForEmotions();
        int threshold = 2 * 60 * 1000;  // period of time to wait before showing MPP based on emotions again.
        long timeSinceLastEmotionMPPIntervention = now - lastInterventionForEmotions;
        if (checkEmotions && timeSinceLastEmotionMPPIntervention>= threshold) {
            StudentModel sm = smgr.getStudentModel();

            if (sm instanceof AffectStudentModel  ) {
                AffectStudentModel asm =  ((AffectStudentModel) sm);
                for (String emotion : emotionSettings.keySet() ) {
                    int emVal=0;  // note that Student Model returns 0 for an emotion until student reports a value
                    if (emotion.equals(AffectStudentModel.CONFIDENT))
                        emVal = asm.getReportedConfidence();
                    else if (emotion.equals(AffectStudentModel.FRUSTRATED))
                        emVal = asm.getReportedFrustration();
                    else if (emotion.equals(AffectStudentModel.INTERESTED))
                        emVal = asm.getReportedInterest();
                    else if (emotion.equals(AffectStudentModel.EXCITED))
                        emVal = asm.getReportedExcitement();
                    Bounds b = emotionSettings.get(emotion);
                    if (emVal != 0 && b.within(emVal)) {
                        state.setTimeOfLastInterventionForEmotions(now);
                        intervention = new ShowMPPIntervention(this.isBuildProblem());
                        break;
                    }
                }
            }
        }
        if (intervention != null) {
            state.setTimeOfLastIntervention(now);
            state.setNumProblemsSinceLastIntervention(0);
        }

        return intervention;
    }

    //   This intervention selector is an exception to how they should work.  It returns an intervention that asks the student if they want to see
    // the MPP (or it may just force them to).  Either way, the students input response or continue button, does not cause an event to be sent back to this
    // for processing.   The client code handles the input and pops up the MPP with no call to the server.  This means an anomaly in the log file because
    // MPP events will follow the ShowIntervention.

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



    private class MyState extends State {
        private final String NUM_PROBS_SINCE_LAST_INTERVENTION =  MyProgressPageIS.this.getClass().getSimpleName() + ".NumProbsSinceLastIntervention";
        private final String TIME_OF_LAST_INTERVENTION =  MyProgressPageIS.this.getClass().getSimpleName() + ".TimeOfLastIntervention";
        private final String TIME_OF_LAST_INTERVENTION_FOR_EMOTIONS =  MyProgressPageIS.this.getClass().getSimpleName() + ".TimeOfLastInterventionForEmotions";
        private final String LAST_INTERVENTION_INDEX =  MyProgressPageIS.this.getClass().getSimpleName() + ".LastInterventionIndex";
        int numProblemsSinceLastIntervention;
        int lastInterventionIndex; // keeps track of the index of the last Affect we asked about
        long timeOfLastIntervention;
        long timeOfLastInterventionForEmotions;

        MyState (SessionManager smgr) throws SQLException {

            this.conn=smgr.getConnection();
            this.objid = smgr.getStudentId();
            WoProps props = smgr.getStudentProperties();
            Map m = props.getMap();
            numProblemsSinceLastIntervention =  mapGetPropInt(m,NUM_PROBS_SINCE_LAST_INTERVENTION,0);
            lastInterventionIndex =  mapGetPropInt(m, LAST_INTERVENTION_INDEX, -1);
            timeOfLastIntervention =  mapGetPropLong(m, TIME_OF_LAST_INTERVENTION, 0);
            timeOfLastInterventionForEmotions =  mapGetPropLong(m, TIME_OF_LAST_INTERVENTION_FOR_EMOTIONS, 0);
//            if (timeOfLastIntervention ==0)
//                setTimeOfLastIntervention(System.currentTimeMillis());

        }

        void setNumProblemsSinceLastIntervention (int n) throws SQLException {
            this.numProblemsSinceLastIntervention = n;
            setProp(this.objid,NUM_PROBS_SINCE_LAST_INTERVENTION,n);
        }

        int getNumProblemsSinceLastIntervention () {
            return this.numProblemsSinceLastIntervention;
        }

        private long getTimeOfLastIntervention() {
            return timeOfLastIntervention;
        }

        private void setTimeOfLastIntervention(long timeOfLastIntervention) throws SQLException {
            this.timeOfLastIntervention = timeOfLastIntervention;
            setProp(this.objid,TIME_OF_LAST_INTERVENTION,timeOfLastIntervention);
        }

        private int getLastInterventionIndex() {
            return lastInterventionIndex;
        }

        private void setLastInterventionIndex(int lastInterventionIndex) throws SQLException {
            this.lastInterventionIndex = lastInterventionIndex;
            setProp(this.objid,LAST_INTERVENTION_INDEX,lastInterventionIndex);
        }

        private long getTimeOfLastInterventionForEmotions() {
            return timeOfLastInterventionForEmotions;
        }

        private void setTimeOfLastInterventionForEmotions(long timeOfLastInterventionForEmotions) throws SQLException {
            this.timeOfLastInterventionForEmotions = timeOfLastInterventionForEmotions;
            setProp(this.objid,TIME_OF_LAST_INTERVENTION_FOR_EMOTIONS,timeOfLastInterventionForEmotions);
        }
    }

    private class Bounds {
        int lower=AffectStudentModel.EMOTION_LOWER_BOUND;
        int upper=AffectStudentModel.EMOTION_UPPER_BOUND;

        Bounds(String lower, String upper) {
            if (lower != null)
                this.lower = Integer.parseInt(lower);
            if (upper != null)
                this.upper = Integer.parseInt(upper);
        }

        int getLower() {
            return lower;
        }

        int getUpper() {
            return upper;
        }

        boolean within (int x) {
            return x >= lower && x <= upper;
        }
    }
}
