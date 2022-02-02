package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbEmotionResponses;
import edu.umass.ckc.wo.db.DbStudentProblemHistory;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.interventions.AskEmotionFreeAnswerIntervention;
import edu.umass.ckc.wo.interventions.AskEmotionRadioIntervention;
import edu.umass.ckc.wo.interventions.AskEmotionSliderIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
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
 * 
 * Frank	02-01-22	Issue #424 - add a new column and update it when an emotion value has been entered by an intervention
 */
public class AskEmotionIS extends NextProblemInterventionSelector  {


    public static final String EXCITED = AffectStudentModel.EXCITED;
    public static final String CONFIDENT = AffectStudentModel.CONFIDENT;
    public static final String FRUSTRATED = AffectStudentModel.FRUSTRATED;
    public static final String INTERESTED = AffectStudentModel.INTERESTED;
    public static final String HARDWORK = AffectStudentModel.HARDWORK;

    private static Logger logger = Logger.getLogger(AskEmotionIS.class);
    private List<Emotion> emotions;
    private boolean askWhy=false;
    private boolean askAboutSkipping=false; // Adds in a question about skipping problems
    private MyState state;

    private String timeInterval ;
    private String probInterval ;
    private String numVals ;
    private String inputType ;
    private String question ;
    private String questionHeader ;


    public AskEmotionIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = new MyState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
        this.pedagogicalModel=pedagogicalModel;
//        super.setServletInfo(smgr,pedagogicalModel);
        configure();
    }

    private void configure() {
        emotions = new ArrayList<Emotion>();
        Element config = this.getConfigXML();
        timeInterval = getConfigParameter2("interruptIntervalMin");
        probInterval = getConfigParameter2("interruptIntervalProblems");
        numVals = getConfigParameter2("numVals");
        inputType = getConfigParameter2("inputType");
        if (config != null) {
            List<Element> emotElts = config.getChildren("emotion");
            if (emotElts != null) {
                for (Element em: emotElts) {
                    String n = em.getAttributeValue("name");
                    Emotion e = new Emotion(n);

                    List<Element> labels = em.getChildren("label");
                    for (Element lab: labels)
                        e.addLabel(lab.getTextTrim(),Integer.parseInt(lab.getAttributeValue("val")));
                    emotions.add(e);
                }
            }
            String askWhyTxt = getConfigParameter2("askWhy");
            if (askWhyTxt != null) {
                boolean b = Boolean.parseBoolean(askWhyTxt);
                this.askWhy= b;
            }
            String askAboutSkip = getConfigParameter2("askAboutSkipping");
            if (askAboutSkip != null) {
                boolean b = Boolean.parseBoolean(askWhyTxt);
                this.askAboutSkipping= b;
            }
        }
        question = getConfigParameter2("question");
        questionHeader = getConfigParameter2("questionHeader");

    }


    @Override
    /**
     * We will pop up a dialog asking student their emotions.   This is determined by time between uses of this
     * intervention or number of problems.   Both are configured in the pedagogy for this selector.   It will
     * either use a slider or radio input depending on settings in the ped.
     */
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        long now = System.currentTimeMillis();

        long timeSinceLastQueryMin = (now - state.getTimeOfLastIntervention())/(1000*60); // convert ms to min
        // If no AskEmot interventions have been given this session, set time of last intervention to now so that we have a
        // base time to check against for determining the first time this returns an intervention
        if (state.getTimeOfLastIntervention() <= 0) {
            timeSinceLastQueryMin = 0;
            state.setTimeOfLastIntervention(now);
        }
        // THis is the mode of the problem asked just prior to the nextProblem button being clicked (so its still the current prob).
        String curProbMode = smgr.getStudentState().getCurProblemMode();
        if (curProbMode == null)
            return null;
        int numAttempts= smgr.getStudentState().getNumAttemptsOnCurProblem();
        boolean skippedProblem = numAttempts < 1;
        int problemsSinceLastQuery =  state.getNumProblemsSinceLastIntervention();

        NextProblemIntervention intervention=null;

        int timeIntervalMin = timeInterval  != null ? Integer.parseInt(timeInterval) : -1;
        int numProbsInterval = probInterval != null ? Integer.parseInt(probInterval) : -1;
        // If the period of time between interventions is exceeded OR num problems exceeded (whichever comes first) , ask
        // By request of ivon we only ask if the previous problem was practice
        boolean condition = curProbMode.equals(Problem.PRACTICE) &&
                ((timeIntervalMin > -1 && timeSinceLastQueryMin >= timeIntervalMin) ||
                        (numProbsInterval > -1 && problemsSinceLastQuery >= numProbsInterval)) ;
        if (condition) {
            Emotion emotionToQuery;
            if (inputType.equals("slider"))  {
                emotionToQuery= getEmotionToQueryRandom();
                intervention = new AskEmotionSliderIntervention(emotionToQuery,this.numVals,this.askWhy, askAboutSkipping, this.questionHeader, this.question, skippedProblem, smgr.getLocale());
            }
            else if (inputType.equals("freeAnswer"))
                intervention = new AskEmotionFreeAnswerIntervention(askAboutSkipping, skippedProblem, smgr.getLocale());
            else   {
                emotionToQuery= getEmotionToQueryRandom();
                intervention = new AskEmotionRadioIntervention(emotionToQuery, this.askWhy, askAboutSkipping, skippedProblem, smgr.getLocale());
            }

            state.setTimeOfLastIntervention(now);
            state.setNumProblemsSinceLastIntervention(0);
            return intervention;
        }

        else return null;
    }


    private Emotion getEmotionToQueryInCycle() throws SQLException {
        int ix = state.getLastInterventionIndex();
        ix++;
        if (ix >= emotions.size()) ix=0;
        state.setLastInterventionIndex(ix);
        return emotions.get(ix) ;
    }

    private Emotion getEmotionToQueryRandom() throws SQLException {
        int n = emotions.size();
        int ix = new Random(System.currentTimeMillis()).nextInt(n);
        return emotions.get(ix);
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
        if (inputType.equals("freeAnswer")) {
            processFreeAnswerInputs(e);
            return null;
        }
        String emotion = params.getString(AskEmotionSliderIntervention.EMOTION);
        String level = params.getString(AskEmotionSliderIntervention.LEVEL);
        if (level == null)
            level = "-1";
        int levelInt = Integer.parseInt(level);
        logger.debug("User reports emotion " + emotion + " level is " + level);

        StudentModel sm = smgr.getStudentModel();
        // The AffectStudentModel is the place where this emotion data is being kept.   It only keeps the last reported emotion.
        // The emotionInterventionResponse table keeps
               
        if (sm instanceof AffectStudentModel  && levelInt > 0) {
            ((AffectStudentModel) sm).setLastReportedEmotion(emotion,levelInt,e.getElapsedTime());
            int id = DbStudentProblemHistory.getMostRecentStudentProblemHistoryRecord(conn,smgr.getStudentId());
            DbStudentProblemHistory.updateEmotionAsked(conn, id);                    
        }
        String reason = params.getString(AskEmotionSliderIntervention.REASON);
        String skipFreq = params.getString(AskEmotionFreeAnswerIntervention.SKIP_FREQ,"");
        String skipReason = params.getString(AskEmotionFreeAnswerIntervention.SKIP_REASON,"");
        // build XML like <interventionInput class="%AskEmotionIS"> <emotion> .... </emotion> </interventionInput>
        if (this.askAboutSkipping)
            setUserInput(this, "<emotion name=\"" + emotion + "\" level=\"" + levelInt + "\">" +
                    "<skipInfo><frequency>" +skipFreq+ "</frequency><reason><![CDATA[" + skipReason + "]]></reason></skipInfo>" +
                    "<![CDATA[" + reason + "]]></emotion>", e);
        else {
            setUserInput(this, "<emotion name=\"" + emotion + "\" level=\"" + levelInt + "\">" +
                    "<![CDATA[" + reason + "]]></emotion>", e);
            skipFreq=null;
            skipReason=null;
        }
        DbEmotionResponses.saveResponse(conn,emotion,levelInt,reason,smgr.getSessionNum(),smgr.getStudentId(), null, null, null,null, skipFreq, skipReason);
        return null;  // no more interventions to return.
    }

    private void processFreeAnswerInputs(InputResponseNextProblemInterventionEvent e) throws Exception {
        ServletParams params = e.getServletParams();
        String feeling = params.getString(AskEmotionFreeAnswerIntervention.FEELING);
        String reason = params.getString(AskEmotionFreeAnswerIntervention.REASON);
        String goal = params.getString(AskEmotionFreeAnswerIntervention.GOAL);
        String skipFreq = params.getString(AskEmotionFreeAnswerIntervention.SKIP_FREQ,"");
        String skipReason = params.getString(AskEmotionFreeAnswerIntervention.SKIP_REASON,"");
//        String desiredResult = params.getString(AskEmotionFreeAnswerIntervention.RESULT);
        if (this.askAboutSkipping)
            setUserInput(this, "<emotion><howDoYouFeel><![CDATA[" + feeling + "]]></howDoYouFeel><reason><![CDATA[" + reason + "]]></reason>" +
                    "<goal><![CDATA[" + goal + "]]></goal><skipInfo><frequency>" +skipFreq+ "</frequency><reason><![CDATA[" + skipReason + "]]></reason></skipInfo></emotion>", e);
        else {
            setUserInput(this, "<emotion><howDoYouFeel><![CDATA[" + feeling + "]]></howDoYouFeel></emotion>", e);
            skipFreq=null;
            skipReason=null;
        }
        DbEmotionResponses.saveResponse(conn,"",0,feeling,smgr.getSessionNum(),smgr.getStudentId(), null, reason, goal, null, skipFreq, skipReason);
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



    public class Emotion {
        private String name;
        private List<String> labels;  // a list of labels to put on the choices
        private List<Integer> vals;   // the values to send back when a choice is made

        Emotion (String name) {
            this.name =name;
            labels = new ArrayList<String>();
            vals = new ArrayList<Integer>();
        }

        void addLabel (String l, int val) {
            labels.add(l);
            vals.add(val);
        }

        public String getName() {
            return name;
        }

        public List<String> getLabels() {
            return labels;
        }

        public List<Integer> getVals() {
            return vals;
        }
    }




    private class MyState extends State {
        private final String NUM_PROBS_SINCE_LAST_INTERVENTION =  AskEmotionIS.this.getClass().getSimpleName() + ".NumProbsSinceLastIntervention";
        private final String TIME_OF_LAST_INTERVENTION =  AskEmotionIS.this.getClass().getSimpleName() + ".TimeOfLastIntervention";
        private final String LAST_INTERVENTION_INDEX =  AskEmotionIS.this.getClass().getSimpleName() + ".LastInterventionIndex";
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
