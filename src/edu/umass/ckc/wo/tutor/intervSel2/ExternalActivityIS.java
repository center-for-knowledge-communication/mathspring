package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.content.ExternalActivity;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbExternalActivity;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.ExternalActivityAskIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import org.jdom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:37 PM
 *
 * This will offer external activities (sometimes call mini-games) to the user within a topic.
 * We find the available activities within the topic and select one.   This will either be forced on the
 * user or offered to the them (based on the configuration mode).  There are 3 variables that control
 * the selection interval:
 * 1. frequencyPct: x   Select an xact as a random event that occurs x% of the time.
 * 2. numProblemsBetweenOffers: p After solving p problems (not necessarily within one topic), make a selection
 * 3. numMinutesBetweenOffers: m  After m minutes, make a selection
 * mode: force / ask controls whether the user can elect not to go to the activity
 * allowRepeat: true/false controls whether this will repeat activities that the user has already been given.   It will
 * only repeat an activity once all the activities for a topic have been shown.   Once all have been shown once, this will
 * randomly choose external activities within the selection interval
 *
 * Sample config:
 *  <config>
         <numProblemsBetweenOffers>10</numProblemsBetweenOffers>
         <mode>ask</mode>
         <allowRepeat>false</allowRepeat>
    </config>
 */
public class ExternalActivityIS extends NextProblemInterventionSelector {
    public static final String FORCE = "force";
    public static final String ASK = "ask";
    // for now we leave this getting a default value which is non-zero so if no other method of
    // configuring is used, this will be used with this number.
    private double percentTimeToSelectXact = Settings.externalActivityPercentage;
    private int numProblemsBetweenOffers; // num problems to wait before selecting external activity
    private int numMinutesBetweenOffers; // wait time in minutes before/between external activity selections
    private boolean allowRepeat; // do we allow an external activity to be repeated
    private String mode =  FORCE;
    private MyState state;



    public ExternalActivityIS(SessionManager smgr) throws SQLException {

        super(smgr);
        this.state = new MyState(smgr);
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) throws SQLException {
        this.pedagogicalModel=pedagogicalModel;
        this.allowRepeat = false;
        this.mode = FORCE;
        configure();
    }

    private void configure() {

        // Configuration should only use ONE OF : frequencyPct, numProblemsBetweenOffers, numMinutesBetweenOffers.
        // the percentage of times that it should choose an external act.


        String freqpct = getConfigParameter2("frequencyPct");
        if (freqpct != null)
            percentTimeToSelectXact = Double.parseDouble(freqpct);
        String x = getConfigParameter2("numProblemsBetweenOffers");
        if (x != null)
            numProblemsBetweenOffers = Integer.parseInt(x);
        x = getConfigParameter2("numMinutesBetweenOffers");
        //Note: This can lead to some undesirable behavior if there is only one xact in the topic and they
        // are allowed to repeat often.   If that can happen make sure that this ASKs about the activity rather
        // than FORCEs it.
        if (x != null)
            numMinutesBetweenOffers = Integer.parseInt(x);
        x = getConfigParameter2("allowRepeat");
        if (x != null)
            allowRepeat = Boolean.parseBoolean(x);
        String m = getConfigParameter2("mode");
        // will be force or ask
        if (m != null)
            mode = m;
    }

    /**
     * Uses the method defined in the configuration to determine if we should attempt to
     * select an activity.   Methods are percentTime, waitTime, numProblems
     * @return
     */
    private boolean shouldSelectActivity () {
        boolean c=false;
        if (numProblemsBetweenOffers > 0) {
            int probsSinceLast = state.getNumProblemsSinceLastIntervention();
            c = probsSinceLast >= numProblemsBetweenOffers;
        }
        else if (numMinutesBetweenOffers > 0) {
            long timeOfLast = state.getTimeOfLastIntervention();
            long mstimeSinceLast = System.currentTimeMillis() - timeOfLast;

            c = mstimeSinceLast >= (this.numMinutesBetweenOffers * 60 * 1000);
        }
        else if (percentTimeToSelectXact > 0) {
            double r = new Random(System.currentTimeMillis()).nextDouble();
            c= r < ( percentTimeToSelectXact / 100.0);
        }
        return c;
    }

    // When an external activity is given,  update the internal state so that its counters
    // and timers are reset.
    private void updateState (ExternalActivity ea) throws SQLException {
        state.setExternalActivityId(ea.getId());
        if (numProblemsBetweenOffers > 0) {
            state.setNumProblemsSinceLastIntervention(0);
        }
        else if (numMinutesBetweenOffers > 0) {
            state.setTimeOfLastIntervention(System.currentTimeMillis());
        }


    }


    @Override
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        double r = new Random(System.currentTimeMillis()).nextDouble();
        int curTopicId = studentState.getCurTopic();
        double m = studentModel.getTopicMastery(curTopicId);
        int curProbId = smgr.getStudentState().getCurProblem();

        if (curProbId > 0 &&
                shouldSelectActivity())  {
            ExternalActivity ea = (ExternalActivity) getExternalActivity(m);
            if (ea != null) {
                if (this.mode.equals(ASK)) {
                    String instr = ea.getInstructions();
                    if (instr == null || instr.equals(""))
                        instr = "Would you like to try a new type of activity?";
                    else
                        instr = "Below are some instructions for a new type of activity.<br><br>" + instr +
                                "<br><br>Would you like to try this activity?";
                    ea.setInstructions(instr);
                }
                ea.setDestinationIS(this.getClass().getName());
                ea.setAskMode(mode);
                updateState(ea);

            }
            return ea;
        }
        else
            return null;
    }




    public ExternalActivity selectExternalActivity() throws SQLException {
        // using the current topic find an external activity that applies to this topic and has not been
        // shown to this student
        int topicId = smgr.getStudentState().getCurTopic();
        int studId = smgr.getStudentId();

        // Get all the external activities about this topic.
        List<Integer> xactIds = DbExternalActivity.getActivitiesForTopic(conn,topicId);
        List<Integer> xactIdsGiven = DbExternalActivity.getActivitiesForStudent(conn,studId);
        // select an xact that hasn't been given previously
        for (int xid: xactIds) {
            boolean fail = false;
            for (int gid: xactIdsGiven)
                if (xid == gid) {
                    fail = true;
                    break;
                }
             if (! fail) {
                ExternalActivity ea = DbExternalActivity.getExternalActivity(conn,xid);
                return ea;
             }
        }
        // if we are allowed to repeat external activities and they've all been given, show a random one.
        // Note: This can lead to some undesirable behavior if there is only one in the topic and they
        // are allowed to repeat often.   If that can happen make sure that this OFFERs the activity rather
        // than FORCEs it.
        if (allowRepeat && xactIds.size() > 0) {
            int ix = new Random().nextInt(xactIds.size());
            int xid = xactIds.get(ix);
            ExternalActivity ea = DbExternalActivity.getExternalActivity(conn,xid);
            return ea;
        }
        return null;


    }


    private NextProblemIntervention getExternalActivity (double topicMastery) throws SQLException {
        ExternalActivity ea= selectExternalActivity();
        if (ea != null)
            studentState.addExternalActivityGiven(ea.getId());
        return ea;

    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    /**  The external activity instructions have been presented
     *   If the mode is ASK, then we check the user input to see if they answered YES about seeing the ext act.
     *   If the mode is FORCE, we just return it.
     */
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        String input = e.getUserInput();
        if ( (this.mode.equals(ASK) && input != null && input.equalsIgnoreCase("yes")) || this.mode.equals(FORCE) ) {
            int xactId = state.getExternalActivityId();
            ExternalActivity ea = DbExternalActivity.getExternalActivity(smgr.getConnection(),xactId);
            // Sets userInput XML in the intervention so that the logger can put it into the userInput field of the eventlog
            if (input.equalsIgnoreCase("yes"))
                setUserInput(this, "<userResponse><![CDATA[yes]]></userResponse>", e);
            else
                setUserInput(this, "<userResponse><![CDATA[ok]]></userResponse>", e);
            ea.setInstructions(null);  // this will indicate to the client code that it should show the activity rather than instructions.
            return new InterventionResponse(ea); }
        else  {
            setUserInput(this, "<userResponse><![CDATA[no]]></userResponse>", e);
            return null;
        }
    }


    @Override
    // Necessary to keep this informed about when problems are given so it can update its internal states
    // counters and timers
    public void problemGiven(Problem p) throws SQLException {
        state.setNumProblemsSinceLastIntervention(state.getNumProblemsSinceLastIntervention() + 1);
    }

    @Override
    // When a new session starts we reset counters and timers
    public void newSession (int sessionId) throws SQLException {
        state.setTimeOfLastIntervention(System.currentTimeMillis());
        state.setNumProblemsSinceLastIntervention(0);
    }




    private class MyState extends State {
        private final String XACTID = ExternalActivityIS.this.getClass().getSimpleName() + ".currentExternalActivityId";

        private final String NUM_PROBS_SINCE_LAST_INTERVENTION = ExternalActivityIS.this.getClass().getSimpleName() + ".NumProbsSinceLastIntervention";
        private final String TIME_OF_LAST_INTERVENTION = ExternalActivityIS.this.getClass().getSimpleName() + ".TimeOfLastIntervention";
        private final String LAST_INTERVENTION_INDEX = ExternalActivityIS.this.getClass().getSimpleName() + ".LastInterventionIndex";
        int numProblemsSinceLastIntervention;
        int lastInterventionIndex; // keeps track of the index of the last Affect we asked about
        long timeOfLastIntervention;
        int externalActivityId; // the id of the xact that we are giving

        MyState(SessionManager smgr) throws SQLException {

            this.conn = smgr.getConnection();
            this.objid = smgr.getStudentId();
            WoProps props = smgr.getStudentProperties();
            Map m = props.getMap();
            numProblemsSinceLastIntervention = mapGetPropInt(m, NUM_PROBS_SINCE_LAST_INTERVENTION, 0);
            lastInterventionIndex = mapGetPropInt(m, LAST_INTERVENTION_INDEX, -1);
            timeOfLastIntervention = mapGetPropLong(m, TIME_OF_LAST_INTERVENTION, 0);
            externalActivityId = mapGetPropInt(m, XACTID, -1);
//            if (timeOfLastIntervention ==0)
//                setTimeOfLastIntervention(System.currentTimeMillis());

        }

        void setNumProblemsSinceLastIntervention(int n) throws SQLException {
            this.numProblemsSinceLastIntervention = n;
            setProp(this.objid, NUM_PROBS_SINCE_LAST_INTERVENTION, n);
        }

        int getNumProblemsSinceLastIntervention() {
            return this.numProblemsSinceLastIntervention;
        }

        private long getTimeOfLastIntervention() {
            return timeOfLastIntervention;
        }

        private void setTimeOfLastIntervention(long timeOfLastIntervention) throws SQLException {
            this.timeOfLastIntervention = timeOfLastIntervention;
            setProp(this.objid, TIME_OF_LAST_INTERVENTION, timeOfLastIntervention);
        }

        private int getLastInterventionIndex() {
            return lastInterventionIndex;
        }

        private void setLastInterventionIndex(int lastInterventionIndex) throws SQLException {
            this.lastInterventionIndex = lastInterventionIndex;
            setProp(this.objid, LAST_INTERVENTION_INDEX, lastInterventionIndex);
        }

        private int getExternalActivityId() {
            return externalActivityId;
        }

        private void setExternalActivityId(int xactId) throws SQLException {
            this.externalActivityId = xactId;
            setProp(this.objid, XACTID, xactId);
        }
    }




//    private class MyState extends State {
//        private final String XACTID =  ExternalActivityIS.this.getClass().getSimpleName() + ".currentExternalActivityId";
//
//        int externalActivityId; // the id of the xact that we are giving
//
//        MyState (SessionManager smgr) throws SQLException {
//
//            this.conn=smgr.getConnection();
//            this.objid = smgr.getStudentId();
//            WoProps props = smgr.getStudentProperties();
//            Map m = props.getMap();
//            externalActivityId =  mapGetPropInt(m, XACTID, -1);
////            if (timeOfLastIntervention ==0)
////                setTimeOfLastIntervention(System.currentTimeMillis());
//
//        }
//
//        private int getExternalActivityId() {
//            return externalActivityId;
//        }
//
//        private void setExternalActivityId(int xactId) throws SQLException {
//            this.externalActivityId = xactId;
//            setProp(this.objid,XACTID,xactId);
//        }
//
//    }

}
