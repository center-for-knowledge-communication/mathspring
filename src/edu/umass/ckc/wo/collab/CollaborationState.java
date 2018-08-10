package edu.umass.ckc.wo.collab;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rafael
 * Date: 2/2/16
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 * Collaboration state needs to exist separately, to ensure that it doesn't get cleared with student state.
 * However, it can't live in just CollaborationIS because it needs to be accessed by the Originator and Partner
 * InterventionSelectors as well. Although CollaborationState is passed through CollaborationManager, its
 * methods do not need to be synchronized because it's only being used by one student session.
 */
//TODO: Investigate why/when student state would get cleared
public class CollaborationState extends State {
    /** The maximum amount of time we allow a student to wait before being given a partner, in milliseconds */
    private long maxPartnerWaitPeriod;

    /** The time between collaboration interventions, in milliseconds */
    private long timeInterval;

    /** The maximum number of problems students can go before being offered a collaboration */
    private int probInterval;

    private final String NUM_PROBS_SINCE_LAST_INTERVENTION = getClass().getSimpleName() + ".NumProbsSinceLastIntervention";
    int numProblemsSinceLastIntervention;

    private final String TIME_OF_LAST_INTERVENTION = getClass().getSimpleName() + ".TimeOfLastIntervention";
    long timeOfLastIntervention;

    public CollaborationState(SessionManager smgr) {
        reloadSession(smgr);
    }

    public void reloadSession(SessionManager smgr) {
        this.conn = smgr.getConnection();
        this.objid = smgr.getStudentId();
        WoProps props = smgr.getStudentProperties();
        Map m = props.getMap();
        numProblemsSinceLastIntervention =  mapGetPropInt(m, NUM_PROBS_SINCE_LAST_INTERVENTION, 0);
        timeOfLastIntervention =  mapGetPropLong(m, TIME_OF_LAST_INTERVENTION, 0);
    }

    public void setNumProblemsSinceLastIntervention (int n) throws SQLException {
        this.numProblemsSinceLastIntervention = n;
        setProp(this.objid, NUM_PROBS_SINCE_LAST_INTERVENTION, n);
    }

    public int getNumProblemsSinceLastIntervention () {
        return this.numProblemsSinceLastIntervention;
    }

    public void setTimeOfLastIntervention(long timeOfLastIntervention) throws SQLException {
        this.timeOfLastIntervention = timeOfLastIntervention;
        setProp(this.objid, TIME_OF_LAST_INTERVENTION, timeOfLastIntervention);
    }

    /**
     * Sets the time of the last intervention to the current time, effectively starting
     * a cooldown on collaboration events of duration timeInterval.
     * @throws SQLException
     */
    public void triggerCooldown() throws SQLException {
        setTimeOfLastIntervention(System.currentTimeMillis());
    }

    public long getTimeOfLastIntervention() {
        return timeOfLastIntervention;
    }

    public long getMaxPartnerWaitPeriod() {
        return maxPartnerWaitPeriod;
    }

    public void setMaxPartnerWaitPeriod(long maxPartnerWaitPeriod) {
        this.maxPartnerWaitPeriod = maxPartnerWaitPeriod;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getProbInterval() {
        return probInterval;
    }

    public void setProbInterval(int probInterval) {
        this.probInterval = probInterval;
    }

    public boolean isTimeToCollab() {
        long delta = Math.max(0, System.currentTimeMillis() - timeOfLastIntervention);
        return delta >= timeInterval;
    }

    public boolean hasSeenEnoughProblemsForCollab() {
        return numProblemsSinceLastIntervention >= probInterval;
    }
}