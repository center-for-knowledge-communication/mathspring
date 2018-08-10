package edu.umass.ckc.wo.tutor.intervSel2;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbEmotionResponses;
import edu.umass.ckc.wo.db.DbGoalResponses;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.*;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/2/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AskGoalsIS extends NextProblemInterventionSelector  {




    private static Logger logger = Logger.getLogger(AskGoalsIS.class);
    private boolean askWhy=false;
    private MyState state;

    private String timeInterval ;
    private String probInterval ;
    private String numVals ;
    private String inputType = "freeAnswer"; // Text entry is what we want by default


    public AskGoalsIS(SessionManager smgr) throws SQLException {
        super(smgr);
        this.inputType = "freeAnswer";
        state = new MyState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
        this.pedagogicalModel=pedagogicalModel;
//        super.setServletInfo(smgr,pedagogicalModel);
        configure();
    }

    private void configure() {
        Element config = this.getConfigXML();
        timeInterval = getConfigParameter2("interruptIntervalMin");
        probInterval = getConfigParameter2("interruptIntervalProblems");
        inputType = getConfigParameter2("inputType");

    }


    @Override
    /**
     * We will pop up a dialog asking student their emotions.   This is determined by time between uses of this
     * intervention or number of problems.   Both are configured in the pedagogy for this selector.   It will
     * either use a slider or radio input depending on settings in the ped.
     */
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        NextProblemIntervention intervention = null;
        // If the period of time between interventions is exceeded OR num problems exceeded (whichever comes first) , ask
        // By request of ivon we only ask if the previous problem was practice
        boolean condition = e.isEnteringPracticeArea();
        if (condition) {
            intervention = new AskGoalsIntervention(inputType);
            long now = System.currentTimeMillis();
            state.setTimeOfLastIntervention(now);
            state.setNumProblemsSinceLastIntervention(0);
            return intervention;
        }

        else return null;
    }



    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // inherits selectIntervention which does a rememberIntervention.   I think this should remember the name of this class
    // and not the super class but maybe not.

    @Override
    /**
     * We've told the user that we want to switch topics and given him the option of staying in the current topic or moving to the
     * next one.   The parameters will be inside the event and will need to be retrieved.  Value of wantSwitch will either be
     *
     */
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        ServletParams params = e.getServletParams();
        long now = System.currentTimeMillis();
        // There are two state vars that control the next time this intervention can be played.  They are set when the intervention is selected
        // and then we overwrite the time based var to hold the time at which the intervention was answered.   This prevents situations where
        // the user sits in the intervention a LONG time and then submits it (which had the bug of generating the intervention again without
        // this fix)
        state.setTimeOfLastIntervention(now);
        processFreeAnswerInputs(e);
        return null;
    }

    private void processFreeAnswerInputs(InputResponseNextProblemInterventionEvent e) throws Exception {
        ServletParams params = e.getServletParams();
        String goals = params.getString(AskGoalsIntervention.GOALS);
//        String desiredResult = params.getString(AskEmotionFreeAnswerIntervention.RESULT);
        setUserInput(this, "<goals><![CDATA[" + goals + "]]></goals>", e);
        DbGoalResponses.saveResponse(conn,goals,smgr.getSessionNum(),smgr.getStudentId());
    }


    @Override
    public void problemGiven(Problem p) throws SQLException {
        state.setNumProblemsSinceLastIntervention(state.getNumProblemsSinceLastIntervention() + 1);
    }

    @Override
    public void newSession (int sessionId) throws SQLException {
        state.setTimeOfLastIntervention(System.currentTimeMillis());
        state.setLastInterventionIndex(-1); // position in the list of the last intervention shown
    }






    private class MyState extends State {
        private final String NUM_PROBS_SINCE_LAST_INTERVENTION =  AskGoalsIS.this.getClass().getSimpleName() + ".NumProbsSinceLastIntervention";
        private final String TIME_OF_LAST_INTERVENTION =  AskGoalsIS.this.getClass().getSimpleName() + ".TimeOfLastIntervention";
        private final String LAST_INTERVENTION_INDEX =  AskGoalsIS.this.getClass().getSimpleName() + ".LastInterventionIndex";
        int numProblemsSinceLastIntervention;
        int lastInterventionIndex; // keeps track of the index of the last Affect we asked about
        long timeOfLastIntervention;

        MyState (SessionManager smgr) throws SQLException {

            this.conn=smgr.getConnection();
            this.objid = smgr.getStudentId();
            WoProps props = smgr.getStudentProperties();
            Map m = props.getMap();
            numProblemsSinceLastIntervention =  mapGetPropInt(m,NUM_PROBS_SINCE_LAST_INTERVENTION,0);
            lastInterventionIndex =  mapGetPropInt(m,LAST_INTERVENTION_INDEX,-1);
            timeOfLastIntervention =  mapGetPropLong(m, TIME_OF_LAST_INTERVENTION, 0);
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
    }

}
