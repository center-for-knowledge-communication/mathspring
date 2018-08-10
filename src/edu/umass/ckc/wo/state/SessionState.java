package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/4/13
 * Time: 9:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionState extends State {

    public static final String CUR_LOCATION = "st.curLocation";
    private static final String TIME = "st.time";
    private static final String NUM_PROBS_THIS_TUTOR_SESSION = "st.numProbsThisTutorSession";  // includes topicIntros
    private static final String NUM_REAL_PROBS_THIS_TUTOR_SESSION = "st.numRealProbsThisTutorSession";
    private static final String LAST_INTERVENTION_TIME = "st.lastInterventionTime";
    private static final String TIME_LAST_CHANGE = "st.timeLastChange";
    private static final String TUTOR_ENTRY_TIME = "st.tutorHutEntryTime";
    private static final String TEMP_PROBLEM_INDEX = "st.tempProblemIndex";
    // not sure how used
    private static final String EXTERNAL_ACTS_GIVEN = "st.externalActivitiesGiven";
    private static final String EXAMPLE_PROBLEMS_GIVEN = "st.exampleProblemsGiven";
    private static final String TOPIC_INTROS_SEEN = "st.topicIntrosSeen";
    private static final String TOPIC_EXAMPLES_SEEN = "st.topicExamplesSeen";
    private static final String NEXT_PROB_DESIRED_DIFFICULTY = "st.nextProbDesiredDifficulty";
    private static final String LC_CLIP_COUNTS = "st.lcClipCounts";
    private static final String LC_CLIPS_SEEN = "st.lcClipsSeen";
    private static final String PED_MOD_INTERNAL_STATE = "st.pedModelInternalState";




    private static String[] ALL_PROPS = new String[] {CUR_LOCATION,TIME,NUM_PROBS_THIS_TUTOR_SESSION, NUM_REAL_PROBS_THIS_TUTOR_SESSION,
            TUTOR_ENTRY_TIME,EXTERNAL_ACTS_GIVEN,EXAMPLE_PROBLEMS_GIVEN, TEMP_PROBLEM_INDEX, TOPIC_INTROS_SEEN, TOPIC_EXAMPLES_SEEN,LAST_INTERVENTION_TIME,
            TIME_LAST_CHANGE, NEXT_PROB_DESIRED_DIFFICULTY, LC_CLIP_COUNTS, LC_CLIPS_SEEN,PED_MOD_INTERNAL_STATE} ;
    private String curLocation;
    private long time;
    private long lastInterventionTime;
    private long timeLastChange;
    private int numProblemsThisTutorSession;
    private int numRealProblemsThisTutorSession;
    private long tutorEntryTime;
    private List<String> externalActivitiesGiven;
    private List<String> satHutExampleProblemsGiven;
    private int tempProblemIndex;
    private List<String> topicIntrosSeen;
    private List<String> topicExamplesSeen;
    private String nextProblemDesiredDifficulty;
    private List<String> lcClipsSeen;
    private List<String> lcClipCounts;
    private int curLesson;
    protected Map m;
    private String pedagogicalModelInternalState;


    public SessionState(Connection conn) {
        this.conn = conn;
    }

    public static void clearState (Connection conn, int objid) throws SQLException {
        for (String prop : ALL_PROPS)
            clearProp(conn,objid,prop)  ;
    }


    public void extractProps(WoProps props) throws SQLException {
        m= props.getMap();
        this.curLocation = mapGetPropString(m, CUR_LOCATION, this.curLocation); // default is where user last was
        this.time = mapGetPropLong(m, TIME, 0);
        this.numProblemsThisTutorSession = mapGetPropInt(m, NUM_PROBS_THIS_TUTOR_SESSION, 0);
        numRealProblemsThisTutorSession = mapGetPropInt(m, NUM_REAL_PROBS_THIS_TUTOR_SESSION, 0);
        lastInterventionTime = mapGetPropLong(m, LAST_INTERVENTION_TIME, 0);
        timeLastChange = mapGetPropLong(m, TIME_LAST_CHANGE, 0);
        tutorEntryTime = mapGetPropLong(m, TUTOR_ENTRY_TIME, 0);
        externalActivitiesGiven = mapGetPropList(m, EXTERNAL_ACTS_GIVEN);
        satHutExampleProblemsGiven = mapGetPropList(m, EXAMPLE_PROBLEMS_GIVEN);
        tempProblemIndex = mapGetPropInt(m, TEMP_PROBLEM_INDEX, -1);
        topicIntrosSeen = mapGetPropList(m, TOPIC_INTROS_SEEN);
        topicExamplesSeen = mapGetPropList(m, TOPIC_EXAMPLES_SEEN);
        nextProblemDesiredDifficulty = mapGetPropString(m, NEXT_PROB_DESIRED_DIFFICULTY);
        this.lcClipsSeen = mapGetPropList(m, LC_CLIPS_SEEN);
        this.lcClipCounts = mapGetPropList(m,LC_CLIP_COUNTS);
        this.pedagogicalModelInternalState = mapGetPropString(m,PED_MOD_INTERNAL_STATE);


    }

    public void setLastInterventionTime(long time) throws SQLException  {
        this.lastInterventionTime = time;
        setProp(this.objid,LAST_INTERVENTION_TIME,this.lastInterventionTime);
    }

    public long getLastInterventionTime() {
        return this.lastInterventionTime;
    }

    public int getLcClipCount (String clipName) {
        String countStr= mapGetPropKeyValue(m,LC_CLIPS_SEEN,LC_CLIP_COUNTS,clipName);
        if (countStr == null)
            return -1;
        else return Integer.parseInt(countStr);
    }

    public void  setLcClipCount (String clipName, int count) throws SQLException {
        mapSetPropKeyValue(m,LC_CLIPS_SEEN,LC_CLIP_COUNTS,clipName,Integer.toString(count));
    }



    public void initializeState () throws SQLException {

        setTime(0);
        setNumProblemsThisTutorSession(0);
        setNumRealProblemsThisTutorSession(0);
        setLastInterventionTime(0);
        setTimeLastChange(0);
        clearExternalActivitiesGiven();
        clearExamplesGiven();
        clearTopicIntrosSeen();
        clearTopicExamplesSeen();
        clearLCClipData();
        setTutorEntryTime(System.currentTimeMillis());
        clearProp(this.objid,PED_MOD_INTERNAL_STATE);
    }

    public String getCurLocation() {
        return curLocation;
    }

    public long getTime() {
        return time;
    }

    public int getNumProblemsThisTutorSession() {
        return numProblemsThisTutorSession;
    }

    public int getNumRealProblemsThisTutorSession() {
        return numRealProblemsThisTutorSession;
    }


    public long getTutorEntryTime () {
        return tutorEntryTime;
    }

    public List<String> getExternalActivitiesGiven () {
        return this.externalActivitiesGiven;
    }

    // return a list of problem ids as Strings
    public List<String> getExampleProblemsGiven() {
        return satHutExampleProblemsGiven;
    }

    public  void setCurLocation(String curLocation) throws SQLException {
        this.curLocation = curLocation;
        setProp(this.objid, CUR_LOCATION, curLocation);
    }

    public void setTime(long time) throws SQLException {
        this.time = time;
        setProp(this.objid, TIME, time);
    }

    public void setNumProblemsThisTutorSession(int n) throws SQLException {
        this.numProblemsThisTutorSession = n;
        setProp(this.objid,NUM_PROBS_THIS_TUTOR_SESSION,n);
    }

    public void setNumRealProblemsThisTutorSession(int n) throws SQLException {
        this.numRealProblemsThisTutorSession = n;
        setProp(this.objid,NUM_REAL_PROBS_THIS_TUTOR_SESSION,n);
    }



    public void setTutorEntryTime(long time) throws SQLException {
        tutorEntryTime = time;
        setProp(this.objid,TUTOR_ENTRY_TIME,tutorEntryTime);
    }

    public void addExternalActivityGiven (int id) throws SQLException {
        this.externalActivitiesGiven.add(Integer.toString(id));
        addProp(this.objid,EXTERNAL_ACTS_GIVEN,Integer.toString(id));
    }

    public void clearExternalActivitiesGiven () throws SQLException {
        externalActivitiesGiven.clear();
        clearProp(this.objid, EXTERNAL_ACTS_GIVEN);
    }

    public void addExampleProblemGiven(int probId) throws SQLException {
        this.satHutExampleProblemsGiven.add( Integer.toString(probId));
        //this.addSatProblemSolved(probId);
        // It does not need to be marked as solved as it is excluded together with the ommitted problems
        addProp(this.objid, EXAMPLE_PROBLEMS_GIVEN, probId);
    }

    public void clearExamplesGiven () throws SQLException {
        satHutExampleProblemsGiven.clear();
        clearProp(this.objid, EXAMPLE_PROBLEMS_GIVEN);
    }

    public int getTempProblemIndex() {
        return tempProblemIndex;
    }

    public void setTempProblemIndex(int tempProblemIndex) throws SQLException {
        this.tempProblemIndex = tempProblemIndex;
        setProp(this.objid,TEMP_PROBLEM_INDEX,tempProblemIndex);
    }


    public List<String> getTopicIntrosSeen() {
        return topicIntrosSeen;
    }

    public void clearTopicIntrosSeen () throws SQLException {
        this.topicIntrosSeen.clear();
        clearProp(this.objid, TOPIC_INTROS_SEEN);
    }

    public void addTopicIntrosSeen(int topicId) throws SQLException {
        this.topicIntrosSeen.add(Integer.toString(topicId));
        addProp(objid,TOPIC_INTROS_SEEN,topicId);
    }

    public boolean isTopicIntroSeen (int topicId) {
        String id = Integer.toString(topicId);
        for (String tid: topicIntrosSeen) {
            if (tid.equals(id))
                return true;
        }
        return false;
    }

    public void clearTopicExamplesSeen() throws SQLException {
        this.topicExamplesSeen.clear();
        clearProp(this.objid,TOPIC_EXAMPLES_SEEN);
    }

    public void addTopicExampleSeen(int topicId) throws SQLException {
        this.topicExamplesSeen.add(Integer.toString(topicId));
        addProp(objid,TOPIC_EXAMPLES_SEEN,topicId);
    }

    public boolean isTopicExampleSeen (int topicId) {
        String id = Integer.toString(topicId);
        for (String tid: topicExamplesSeen) {
            if (tid.equals(id))
                return true;
        }
        return false;
    }


    public String getNextProblemDesiredDifficulty() {
        return nextProblemDesiredDifficulty;
    }

    public void setNextProblemDesiredDifficulty(String nextProblemDesiredDifficulty) throws SQLException {
        this.nextProblemDesiredDifficulty = nextProblemDesiredDifficulty;
        setProp(this.objid,NEXT_PROB_DESIRED_DIFFICULTY,nextProblemDesiredDifficulty);
    }


    public List<String> getClips () {
        return this.lcClipsSeen;
    }

    public List<String> getClipCounters () {
        return this.lcClipCounts;
    }


    // Given a 1-based clip-number, update the woproperty at this position number (clipNumber) to
    // be count.
    // Then update the instance variable (0 based) that holds the list of clip counters corresponding
    // with whats in woproperties.
    public void incrClipCount (int clipNumber, int count) throws SQLException {
        setProp(this.objid,LC_CLIP_COUNTS,count,clipNumber);
        // need to alter this.clipCounters so that it has correct count at position clipNumber-1 (its 0-based)
        this.lcClipCounts.set(clipNumber-1,Integer.toString(count));

    }

    public void setClipCount (String clip, int count ) throws SQLException {
        addProp(this.objid,LC_CLIPS_SEEN,clip);
        addProp(this.objid,LC_CLIP_COUNTS,count);
        this.lcClipsSeen.add(clip);
        this.lcClipCounts.add(Integer.toString(count));
    }

    public void clearLCClipData() throws SQLException {
        this.lcClipCounts = null;
        this.lcClipsSeen = null;
        clearProp(objid,LC_CLIP_COUNTS);
        clearProp(objid,LC_CLIPS_SEEN);
    }


    public void setPedagogicalModelInternalState(String pedagogicalModelInternalState) throws SQLException {
        this.pedagogicalModelInternalState = pedagogicalModelInternalState;
        setProp(objid,PED_MOD_INTERNAL_STATE,pedagogicalModelInternalState);
    }

    public String getPedagogicalModelInternalState() {
        return pedagogicalModelInternalState;
    }

    public long getTimeLastChange() {
        return timeLastChange;
    }

    public void setTimeLastChange(long timeLastChange) throws SQLException {
        this.timeLastChange = timeLastChange;
        setProp(this.objid,TIME_LAST_CHANGE,this.timeLastChange);
    }
}
