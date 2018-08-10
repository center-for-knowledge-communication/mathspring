package edu.umass.ckc.wo.tutor.intervSel2;


import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.MyProgressNavigationIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
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
 * Date: 9/3/14
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyProgressNavigationForceIS extends NextProblemInterventionSelector {
    MyState state;
    Map<String,Bounds> emotionSettings = new HashMap<String, Bounds>(11);
    boolean checkEmotions=false;
    String emLowerBound=null;
    String emUpperBound=null;
    boolean isNotify;
    String when;
    String notifyHTML;
    String component;
    String action;


    int minIntervalBetweenMPPQueriesBasedOnAffect = 5 * 60 * 1000;  // default: we wait 5 minutes before we ask again about MPP after we show a dialog about it.
                                                                                         // assuming that the affect does not change.
    public MyProgressNavigationForceIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = new MyState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel)  {
        this.pedagogicalModel=pedagogicalModel;
        configure();

    }

    /**
     * Will not rework to take IS Params.  Just give it a config XML element.
     * Requires a config element to define the conditions that trigger this intervention.   Format is:
     * <config>
     *     <intervalCriteria type="t" val="v" emotion="e" lowerBound="lb" upperBound="ub"/>
     *
     *     <notifyDialog when="notify-before/after">Dialog text</notifyDialog>
     *     <component action="Show | Hide | Highlight">GUI Component</component>
     * </config>
     *
     *
     */
    private void configure() {
        Element config = this.getConfigXML();
        if (config != null) {
            List<Element> intervalCritiaElts = config.getChildren("intervalCriteria");
            for (Element elt: intervalCritiaElts) {
                String t = elt.getAttributeValue("type");
                String v = elt.getAttributeValue("val");

                if (t.equals("affect")) {
                    checkEmotions=true;
                    String em = elt.getAttributeValue("emotion");
                    String lb = elt.getAttributeValue("lowerBound");
                    String ub = elt.getAttributeValue("upperBound");
                    Bounds b = new Bounds(lb,ub);
                    emotionSettings.put(em,b);
                }

            }
            Element elt = config.getChild("minIntervalBetweenMPPQueriesBasedOnAffect");
            if (elt != null) {
                String minutes = elt.getText();
                this.minIntervalBetweenMPPQueriesBasedOnAffect = Integer.parseInt(minutes) * 60 * 1000;
            }
            elt = config.getChild("notifyDialog") ;
            if (elt != null) {
                when = elt.getAttributeValue("when");
                notifyHTML = elt.getTextTrim();
                isNotify=true;
            }
            elt = config.getChild("component");
            if (elt != null) {
                component = elt.getTextTrim() ;
                action = elt.getAttributeValue("action");
            }
            else isNotify = false;

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


     The below is an abstraction of ChangeGUIIntervention:
     What I want to have happen:  This will return the intervention.  The client will process it by seeing that it has notify-before and some dialog html.
     It should then pop up the dialog with the given text.  When the continue button is clicked, the interface is changed by performing the change action
     which is one of Show | Hide | Highlight on the specified GUI component (one of MPP, Hint, ReadProblem, Example, Video, etc)

     This provides a way to change the GUI and to notify the user that it is happening with a customized message either before or after the change happens.


     */
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        long now = System.currentTimeMillis();

        // We only want this intervention to come up after a practice problem because the MPP return-to-hut will break unless the problem was originally practice.
        if (smgr.getStudentState().getCurProblemMode() == null || !smgr.getStudentState().getCurProblemMode().equals(Problem.PRACTICE))
            return null;
        NextProblemIntervention intervention=null;

        long lastInterventionForEmotions = state.getTimeOfLastInterventionForEmotions();
        long timeSinceLastEmotionMPPIntervention = now - lastInterventionForEmotions;
        boolean c1,c2;
        c1 = checkEmotions && timeSinceLastEmotionMPPIntervention>= minIntervalBetweenMPPQueriesBasedOnAffect;
        if (c1) {
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
                    c2 =  emVal != 0 && b.within(emVal);
                    if (c2) {
                        state.setTimeOfLastInterventionForEmotions(now);
                         intervention =new MyProgressNavigationIntervention(isNotify, when,notifyHTML, false, component, action);
                        break;
                    }
                }
            }
        }


        return intervention;
    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



    private class MyState extends State {
        private final String TIME_OF_LAST_INTERVENTION_FOR_EMOTIONS =  MyProgressNavigationForceIS.this.getClass().getSimpleName() + ".TimeOfLastInterventionForEmotions";
        private final String LAST_INTERVENTION_INDEX =  MyProgressNavigationForceIS.this.getClass().getSimpleName() + ".LastInterventionIndex";

        int lastInterventionIndex; // keeps track of the index of the last Affect we asked about
        long timeOfLastInterventionForEmotions;

        MyState (SessionManager smgr) throws SQLException {

            this.conn=smgr.getConnection();
            this.objid = smgr.getStudentId();
            WoProps props = smgr.getStudentProperties();
            Map m = props.getMap();
            lastInterventionIndex =  mapGetPropInt(m, LAST_INTERVENTION_INDEX, -1);
            timeOfLastInterventionForEmotions =  mapGetPropLong(m, TIME_OF_LAST_INTERVENTION_FOR_EMOTIONS, 0);
//            if (timeOfLastIntervention ==0)
//                setTimeOfLastIntervention(System.currentTimeMillis());

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
