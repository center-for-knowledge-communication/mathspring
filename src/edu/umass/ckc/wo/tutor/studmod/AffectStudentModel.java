package edu.umass.ckc.wo.tutor.studmod;

import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbStateTableMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class AffectStudentModel extends BaseStudentModel {


    public static final long MIN_GUESS_TIME = 5000;
    public static final long MIN_AVGHINT_TIME = 6000;
    public static final int MAX_HINTS = 4;

    public static final String EXCITED = "Excitement";
    public static final String CONFIANZA = "Confianza";
    public static final String CONFIDENT = "Confidence";
    public static final String FRUSTRATED = "Frustration";
    public static final String FRUSTACION = "Frustration";
    public static final String INTERESTED = "Interest";
    public static final String HARDWORK = "Hardworking";

    public static final int MANY_HINTS = 0;
    public static final int QUICK_GUESSX = 1;
    public static final int NORMALX = 2;
    public static final int NEXT_PROB = 3;
    public static final int EMOTION_LOWER_BOUND=1;
    public static final int EMOTION_UPPER_BOUND=5;

    private static Logger logger = Logger.getLogger(AffectStudentModel.class);
    private static final String ABILITY = "ability";
    private static final String MOTIV1 = "motiv1";        // Unmotivated Hint Abuse
    private static final String MOTIV2 = "motiv2";        // Unmotivated Quick Guessing
    private static final String MOTIV3 = "motiv3";        // Motivated
    private static final String MOTIV4 = "motiv4";        // Clicking Next Problem Button
    private static final String LAST_EMOTION = "Emotion";
    private static final String LAST_EMOTION_VALUE = "EmotionValue";
    private static final String REPORTED_INTEREST = "ReportedInterest";
    private static final String INTEREST_REPORT_TIME = "InterestReportTime";
    private static final String REPORTED_FRUSTRATION = "ReportedFrustration";
    private static final String FRUSTRATION_REPORT_TIME = "FrustrationReportTime";
    private static final String REPORTED_EXCITEMENT = "ReportedExcitement";
    private static final String EXCITEMENT_REPORT_TIME = "ExcitementReportTime";
    private static final String REPORTED_CONFIDENCE = "ReportedConfidence";
    private static final String CONFIDENCE_REPORT_TIME = "ConfidenceReportTime";
    private static final String LAST_INTERVENTION_TIME = "LastInterVentionTime";
    private static final String TABLE_NAME = "affectstudentmodel";
    private static final String[] TABLE_COLS= new String[] {"informReward","studControlReward","lastEmotion",
            "lastEmotionValue","lastInterventionTime","reportedInterest","interestReportTime",
            "reportedFrustration","frustrationReportTime","reportedConfidence","confidenceReportTime",
            "reportedExcitement","excitementReportTime","ability","motiv_hintAbuse","motiv_quickGuess",
            "motiv_normal","motiv_problemSkipping"};

    public static final String INFORM_REWARD = "informReward";
    public static final String STUD_CONTROL_REWARD = "studControlReward";
    public static final String ASK_EMOTION_REWARD = "askEmotionReward";

    public static String HINT_ABUSE = "hintAbuse";
    public static String NORMAL = "normal";
    public static String QUICK_GUESS = "quickGuess";
    public static String PROBLEM_SKIPPING = "problemSkipping";

    //possibly optimistic values?
    private double informReward = 10;
    private double studControlReward = 10;
    private String lastEmotion = "NoEmotionYet";
    private int lastEmotionValue = -1;
    private long lastInterventionTime = 0;
    private int reportedInterest=0;
    private long interestReportTime=0;
    private int reportedFrustration=0;
    private long frustrationReportTime=0;
    private int reportedConfidence=0;
    private long confidenceReportTime=0;
    private int reportedExcitement=0;
    private long excitementReportTime=0;
    private String reportedReason=null;

    // IRT variables
    private double ability = 0.0;
    private double motiv_hintAbuse = 0.05;
    private double motiv_quickGuess = 0.15;
    private double motiv_normal = 0.75;
    private double motiv_problemSkipping = 0.05;


    public AffectStudentModel() {
    }

    // This is the constructor that is called by the newInstance code when a session gets created on each new event
    public AffectStudentModel (SessionManager smgr) throws Exception {
        super(smgr);
    }

    public AffectStudentModel(Connection conn) throws SQLException {
       super(conn);
       dbWorker = new DbStateTableMgr(conn);
    }

    public String getEngagementType() {

//        System.out.println("DEBUG:\t\tin AffectStudentModel.getEngagnementType - Ability: " + ability + "\tNormal: " + motiv_normal+ "\tHint Abuse: " + motiv_hintAbuse);
//        System.out.println("DEBUG:\t\tin AffectStudentModel.getEngagnementType - QuickGuess: " + motiv_quickGuess + "\tSkipping: " + motiv_problemSkipping);
        String type = NORMAL;
        double max = getMotiv_normal();
        if (getMotiv_hintAbuse() > max) {
            type = HINT_ABUSE;
            max = getMotiv_hintAbuse();
        }
        if (getMotiv_quickGuess() > max) {
            type = QUICK_GUESS;
            max = getMotiv_quickGuess();
        }
        if (getMotiv_problemSkipping() > max) {
            type = PROBLEM_SKIPPING;
            max = getMotiv_problemSkipping();
        }
        return type;
    }

    /**
     * Load all student model key/value pairs.
     * Called right after StudentModel is constructed.
     *
     * @param props
     * @param studId
     * @param classId
     * @throws SQLException
     */
    public void initOld(WoProps props, int studId, int classId) throws SQLException {
        super.init(props, studId, classId);
        Map m = props.getMap();
        this.setAbility(mapGetPropDouble(m, ABILITY, 0.0));
        this.setMotiv_hintAbuse(mapGetPropDouble(m, MOTIV1, 0.05));
        this.setMotiv_quickGuess(mapGetPropDouble(m, MOTIV2, 0.15));
        this.setMotiv_normal(mapGetPropDouble(m, MOTIV3, 0.75));
        this.setMotiv_problemSkipping(mapGetPropDouble(m, MOTIV4, 0.05));
        this.lastEmotion = this.mapGetPropString(m, LAST_EMOTION, "NoEmotionYet");
        this.lastEmotionValue = this.mapGetPropInt(m, LAST_EMOTION_VALUE, -1);
        this.lastInterventionTime = this.mapGetPropLong(m, LAST_INTERVENTION_TIME, 0);

        this.informReward = mapGetPropDouble(m, INFORM_REWARD, 10.0);
        this.studControlReward = mapGetPropDouble(m, STUD_CONTROL_REWARD, 10.0);
        this.confidenceReportTime = mapGetPropLong(m, CONFIDENCE_REPORT_TIME, 0);
        this.excitementReportTime = mapGetPropLong(m, EXCITEMENT_REPORT_TIME, 0);
        this.frustrationReportTime = mapGetPropLong(m, FRUSTRATION_REPORT_TIME, 0);
        this.interestReportTime = mapGetPropLong(m, INTEREST_REPORT_TIME, 0);
        this.reportedInterest = mapGetPropInt(m, REPORTED_INTEREST, 0);
        this.reportedConfidence = mapGetPropInt(m, REPORTED_CONFIDENCE, 0);
        this.reportedFrustration = mapGetPropInt(m, REPORTED_FRUSTRATION, 0);
        this.reportedExcitement = mapGetPropInt(m, REPORTED_EXCITEMENT, 0);
    }



    public void init(WoProps props, int studId, int classId) throws SQLException {
        super.init(props, studId, classId);
        try {
            dbWorker.load(studId,this, TABLE_NAME, TABLE_COLS, AffectStudentModel.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IntrospectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }




    /**
     * Called by ProblemSelector objects whenever a Problem is completed (user presses the nextProblem button).  This should not be called for
     * Motivational items; they are considered to be Interventions and should be passed to the interventionGiven method below.
     *
     * @param smgr
     * @throws SQLException
     */
    public void endProblem(SessionManager smgr, int studId, long probElapsedTime, long elapsedTime) throws Exception {
        super.endProblem(smgr, studId, probElapsedTime, elapsedTime);
//        setIRTvariables(smgr.getStudentState());
        problemHistory.updateEmotions(smgr,this.lastEmotion,this.lastEmotionValue);
       // smgr.getStudentState().endProblem(smgr, 0, 0, 0); // set/initialize student state variables based on newProblem event
    }


    // TODO IRT should be moved into a subclass 

//    private void setIRTvariables(StudentState state) throws SQLException {
//        // IRT Work Done Here!!!
//
//        // Don't Worry About What Student Does During an Intervention
//        // I'M ASSUMING ALL INTERVENTIONS WILL HAVE A PROBLEM_ID < 0 !!!!
//        //      AND THAT THERE ARE NO OTHER VALID PROBLEM_ID's < 0   !!!!
//        // Current Problem Number
//        int cur_prb_id = state.getCurProblem();
//        if (cur_prb_id >= 0) {
//            // Figure out Current Problem's "Response"
//            int cur_resp_obs = 0;
//            if (state.getFirstEvent() != null && state.getFirstEvent().equals(StudentState.CORRECT_ATTEMPT_EVENT))
//                cur_resp_obs = 1;
//
//            // Figure out Current Problem's "Observed Motivation Variable"
//            // 4 Values:  Unmotivated Hint Abuse, Unmotivated Quick Guess, Motivated, Just Asks for Next Problem
//            int cur_motiv_obs = -1;
//            if (state.getFirstEvent() == null)
//                cur_motiv_obs = NEXT_PROB;
//            else {
//                if ((state.getTimeToFirstEvent() <= MIN_GUESS_TIME) && (state.getFirstEvent().equals(StudentState.CORRECT_ATTEMPT_EVENT) || state.getFirstEvent().equals(StudentState.INCORRECT_ATTEMPT_EVENT)))
//                    cur_motiv_obs = QUICK_GUESSX;
//                else if (state.getNumHintsBeforeCorrect() >= MAX_HINTS) {
//                    long avgTimeInHints = state.getTimeInHintsBeforeCorrect() / ((long) state.getNumHintsBeforeCorrect());
//                    if (avgTimeInHints <= MIN_AVGHINT_TIME)
//                        cur_motiv_obs = MANY_HINTS;
//                    else
//                        cur_motiv_obs = NORMALX;
//                } else
//                    cur_motiv_obs = NORMALX;
//            }
//
//            // Now get information for previous problems
//            List motivObservations = state.getSatHutMotivObservations();        // ints (0,1,2,3)
//            List responses = state.getSatHutResponses();                // ints (0,1)
//            List problemOrder = state.getSatHutProblemOrder();            // ints (from problem p.getId())
//
//            // Get the Motivation Observations in an int[] Array
//            Object[] objArray = motivObservations.toArray();
//            int numPrevObs = motivObservations.size();
//            int[] motiv_obs = new int[numPrevObs + 1];
//            for (int i = 0; i < numPrevObs; i++) {
//                if (objArray[i] instanceof String)
//                    motiv_obs[i] = Integer.parseInt((String) objArray[i]);
//
//            }
//
//            motiv_obs[numPrevObs] = cur_motiv_obs;
//            // Now get the Smoothed (Hidden) Motivation Probabilities
//            double[][] smoothedMotiv = getSmoothedMotivation(motiv_obs);
//            // Update the Most Recent Motivation Values
//            setMotiv_hintAbuse(smoothedMotiv[MANY_HINTS][numPrevObs]);
//            setMotiv_quickGuess(smoothedMotiv[QUICK_GUESSX][numPrevObs]);
//            setMotiv_normal(smoothedMotiv[NORMALX][numPrevObs]);
//            setMotiv_problemSkipping(smoothedMotiv[NEXT_PROB][numPrevObs]);
//
//            // Get the A and B IRT Parameters for the Problems Seen
//            objArray = problemOrder.toArray();
//            double[] irtA = new double[numPrevObs + 1];
//            double[] irtB = new double[numPrevObs + 1];
//            for (int i = 0; i < numPrevObs; i++) {
//                int prb_id = Integer.parseInt((String) objArray[i]);
//                irtA[i] = this.getIRTParamA(prb_id);        // These methods need to be changed!!!!
//                irtB[i] = this.getIRTParamB(prb_id);        // I just need a way to get the IRT
//            }                                    // parameters given the problem_id.
//            irtA[numPrevObs] = this.getIRTParamA(cur_prb_id);
//            irtB[numPrevObs] = this.getIRTParamB(cur_prb_id);
//
//            // Get the Responses for the Problems Seen
//            Object[] oa2 = responses.toArray();
//            int[] resp_obs = new int[numPrevObs + 1];
//            for (int i = 0; i < numPrevObs; i++)
//                resp_obs[i] = Integer.parseInt((String) oa2[i]);
//            resp_obs[numPrevObs] = cur_resp_obs;
//
//            // Update Ability
//            setAbility(updateAbility(resp_obs, smoothedMotiv, irtA, irtB));
//        }
//
//    }


    /**
     * @param probId
     * @return the IRT params A for the given problem id.
     * @throws SQLException
     */
    public double getIRTParamA(int probId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select a from irtparams where probId=?");
        ps.setInt(1, probId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            double a = rs.getDouble(1);
            return a;
        }
        return 1;
    }

    /**
     * @param probId
     * @return the IRT param B for the given problem id.
     * @throws SQLException
     */
    public double getIRTParamB(int probId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select b from irtparams where probId=?");
        ps.setInt(1, probId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            double b = rs.getDouble(1);
            return b;
        }
        return 0;
    }

    private double[][] getSmoothedMotivation(int[] o) {
        // Define the CPTs
//        double[] prior = {0.1, 0.1, 0.8};
        double[] prior = {0.05, 0.15, 0.75, 0.05};
//        double[][] transMat = {{0.85, 0.05, 0.10},
//                               {0.05, 0.90, 0.05},
//                               {0.05, 0.05, 0.90}};
        double[][] transMat = {{0.6, 0.05, 0.3, 0.05},
                {0.05, 0.70, 0.2, 0.05},
                {0.05, 0.1, 0.8, 0.05},
                {0.05, 0.1, 0.35, 0.5}};
//        double[][] obsMat = {{0.70, 0.05, 0.25},
//                             {0.05, 0.70, 0.25},
//                             {0.05, 0.05, 0.90}};
        double[][] obsMat = {{0.8, 0.05, 0.1, 0.05},
                {0.05, 0.7, 0.2, 0.05},
                {0.05, 0.1, 0.8, 0.05},
                {0.04, 0.08, 0.08, 0.8}};

        // Get Size Information
        int numStates = 4;      // was previously 3
        int T = o.length;

        // Handle the Case if o == null (e.g. T == 0)
        if (T == 0) {
            // Set up Data Structures
            double[][] smooth = new double[numStates][1];
            smooth[MANY_HINTS][0] = prior[MANY_HINTS];
            smooth[QUICK_GUESSX][0] = prior[QUICK_GUESSX];
            smooth[NORMALX][0] = prior[NORMALX];
            smooth[NEXT_PROB][0] = prior[NEXT_PROB];
            // Return the Smoothed Probabilities
            return smooth;
        } else {
            // Set up Data Structures
            double[][] fwd = new double[numStates][T];
            double[][] bwd = new double[numStates][T];
            double[][] smooth = new double[numStates][T];
            double zsum = 0.0;

            // Run the Forward Procedure
            for (int i = 0; i < numStates; i++)
                fwd[i][0] = prior[i] * obsMat[i][o[0]];
            // Induction
            for (int t = 0; t <= T - 2; t++) {
                zsum = 0.0;
                for (int j = 0; j < numStates; j++) {
                    fwd[j][t + 1] = 0.0;
                    for (int i = 0; i < numStates; i++)
                        fwd[j][t + 1] += (fwd[i][t] * transMat[i][j]);
                    fwd[j][t + 1] *= obsMat[j][o[t + 1]];
                    zsum += fwd[j][t + 1];
                }
                for (int j = 0; j < numStates; j++)
                    fwd[j][t + 1] /= zsum;    // normalize to avoid underflow
            }

            // Run the Backward Procedure
            for (int i = 0; i < numStates; i++)
                bwd[i][T - 1] = 1.0;
            // Induction
            for (int t = T - 2; t >= 0; t--) {
                zsum = 0.0;
                for (int i = 0; i < numStates; i++) {
                    bwd[i][t] = 0.0;
                    for (int j = 0; j < numStates; j++)
                        bwd[i][t] += (bwd[j][t + 1] * transMat[i][j] * obsMat[j][o[t + 1]]);
                    zsum += bwd[i][t];
                }
                for (int i = 0; i < numStates; i++)
                    bwd[i][t] /= zsum;      // normalize to avoid underflow
            }

            // Create the Smoothed Probabilities
            for (int i = 0; i < T; i++) {
                zsum = 0.0;
                for (int j = 0; j < numStates; j++) {
                    smooth[j][i] = fwd[j][i] * bwd[j][i];
                    zsum += smooth[j][i];
                }
                for (int j = 0; j < numStates; j++)
                    smooth[j][i] /= zsum;
            }

            // Return the Smoothed Probabilities
            return smooth;
        }
    }

    private double updateAbility(int[] responses, double[][] smoothedMotiv, double[] irtA, double[] irtB) {
        // Number of Problems Performed
        int T = responses.length;

        // IRT Update Parameters
        double HPM = 0.0;
        double HPVAR = 1.0;
        double maxDelta = 0.5;
        double minDelta = 0.002;
        int maxIterations = 50;
        double ability = 0.0;
        double delta = 0.0;

        // Start the Iterations
        for (int i = 0; i < maxIterations; i++) {

            // Keep track of the Numerator and Denominator
            double sumnum = 0.0;
            double sumden = 0.0;

            // Go through the T problems
            for (int j = 0; j < T; j++) {
                // Calculate Motivated Probability of Correct Response
                double phat = getProbability2(ability, irtA[j], irtB[j]);
                double wij = phat * (1.0 - phat);
                double vij = ((double) responses[j]) - phat;
                sumnum = sumnum + smoothedMotiv[NORMALX][j] * irtA[j] * vij;
                sumden = sumden - smoothedMotiv[NORMALX][j] * irtA[j] * irtA[j] * wij;
            }

            // Update Delta Using Bayesian Techniques
            sumnum -= (ability - HPM) / HPVAR;
            sumden -= 1.0 / HPVAR;
            delta = sumnum / sumden;

            // Don't Update Ability More than MaxDelta
            if (Math.abs(delta) > maxDelta) {
                if (delta > 0.0)
                    delta = maxDelta;
                else
                    delta = -maxDelta;
            }
            ability = ability - delta;

            // Break For Loop if Change gets Too Small
            if (Math.abs(delta) < minDelta)
                break;
        }
        return ability;
    }

    private double getProbability2(double ability, double a, double b) {
        double p = 1.0 / (1.0 + Math.exp(-a * (ability - b)));
        if (p > 0.99999)
            p = 0.99999;
        else if (p < 0.00001)
            p = 0.00001;
        return p;
    }


    /**
     * Called by ProblemSelector objects whenever it selects a new Problem object to show.  This should not be called for
     * Motivational items; they are considered to be Interventions and should be passed to the interventionGiven method below.
     *
     * @param state
     * @param p
     * @throws SQLException
     */
    public void newProblem(StudentState state, Problem p) throws SQLException {

        super.newProblem(state, p);
    }


    /**
     * Called by HintSelector objects whenever a hint is selected.
     *
     * @param state
     * @param hint
     * @throws SQLException
     */
    public void hintGiven(StudentState state, Hint hint) throws SQLException {
        super.hintGiven(state, hint);
    }

    public void videoGiven(StudentState state) throws SQLException {
        super.videoGiven(state);
    }


    public void exampleGiven(StudentState state, int exampleId) throws SQLException {
        super.exampleGiven(state, exampleId);
    }

    /**
     * Called by PedagogicalModel objects whenever a student attempts to answer a question
     *
     * @param state
     * @param answer
     * @param isCorrect
     * @throws SQLException
     */
    public void studentAttempt(StudentState state, String answer, boolean isCorrect, long probElapsed) throws SQLException {
        super.studentAttempt(state, answer, isCorrect, probElapsed);
    }


    /**
     * Write the updated values back to the database.
     *
     * @return boolean
     */
    public boolean saveOld() throws SQLException {
        logger.debug("Save BEGIN");

        super.save(); // DM 6/09 all the variables in the BaseStudentModelOld were not getting saved because this
        // line was absent
        // make jeff's variables persist
        setProp(this.objid, ABILITY, this.getAbility());
        setProp(this.objid, MOTIV1, this.getMotiv_hintAbuse());
        setProp(this.objid, MOTIV2, this.getMotiv_quickGuess());
        setProp(this.objid, MOTIV3, this.getMotiv_normal());
        setProp(this.objid, MOTIV4, this.getMotiv_problemSkipping());

        if (lastEmotion != null) {
            setProp(this.objid, LAST_EMOTION, this.lastEmotion);
            setProp(this.objid, LAST_EMOTION_VALUE, this.lastEmotionValue);
        }
        setProp(this.objid, REPORTED_INTEREST, this.reportedInterest);
        setProp(this.objid, REPORTED_FRUSTRATION, this.reportedFrustration);
        setProp(this.objid, REPORTED_EXCITEMENT, this.reportedExcitement);
        setProp(this.objid, REPORTED_CONFIDENCE, this.reportedConfidence);
        setProp(this.objid, INTEREST_REPORT_TIME, this.interestReportTime);
        setProp(this.objid, FRUSTRATION_REPORT_TIME, this.frustrationReportTime);
        setProp(this.objid, EXCITEMENT_REPORT_TIME, this.excitementReportTime);
        setProp(this.objid, CONFIDENCE_REPORT_TIME, this.confidenceReportTime);

        setProp(this.objid, LAST_INTERVENTION_TIME, this.lastInterventionTime);
        setProp(this.objid, INFORM_REWARD, this.informReward);
        setProp(this.objid, STUD_CONTROL_REWARD, this.studControlReward);

        logger.debug("Save END");
        return true;


    }

    public boolean save() throws SQLException {
        logger.debug("Save BEGIN");
        super.save();
        long n = System.currentTimeMillis();
        dbWorker.save(this,this.objid, TABLE_NAME, TABLE_COLS, AffectStudentModel.class);
        logger.debug("Save END took:" + (System.currentTimeMillis()-n));
        return true;
    }

    public double getAbility() {
        return ability;
    }

    public double getMotiv_hintAbuse() {
        return motiv_hintAbuse;
    }

    public double getMotiv_quickGuess() {
        return motiv_quickGuess;
    }

    public double getMotiv_normal() {
        return motiv_normal;
    }

    public double getMotiv_problemSkipping() {
        return motiv_problemSkipping;
    }

    public double getStudControlReward() {
        return studControlReward;
    }

    public void setStudControlReward(double studControlReward) {
        this.studControlReward = studControlReward;
    }

    public double getInformReward() {
        return informReward;
    }

    public void setInformReward(double informReward) {
        this.informReward = informReward;
    }

    public void setLastReportedEmotion(String emotion, int value, long elapsedTime) throws SQLException {
        if (emotion.equals(CONFIDENT)) {
            this.reportedConfidence = value;
            this.confidenceReportTime = elapsedTime;
        } else if (emotion.equals(INTERESTED)) {
            this.reportedInterest = value;
            this.interestReportTime = elapsedTime;

        } else if (emotion.equals(EXCITED)) {
            this.reportedExcitement = value;
            this.excitementReportTime = elapsedTime;
        } else if (emotion.equals(FRUSTRATED)) {
            this.reportedFrustration = value;
            this.frustrationReportTime = elapsedTime;
        }
        if (emotion != null) {
            this.lastEmotion = emotion;
            this.lastEmotionValue = value;
        }

//        setProp(this.objid,LAST_EMOTION,this.lastEmotion);
//        setProp(this.objid,LAST_EMOTION_VALUE,this.lastEmotionValue);
    }





    public String getLastReportedEmotion() {
        return this.lastEmotion;
    }

    public int getLastReportedEmotionValue() {
        return this.lastEmotionValue;
    }


    public int getReportedInterest() {
        return reportedInterest;
    }

    public long getInterestReportTime() {
        return interestReportTime;
    }

    public int getReportedFrustration() {
        return reportedFrustration;
    }

    public long getFrustrationReportTime() {
        return frustrationReportTime;
    }

    public int getReportedConfidence() {
        return reportedConfidence;
    }

    public long getConfidenceReportTime() {
        return confidenceReportTime;
    }

    public int getReportedExcitement() {
        return reportedExcitement;
    }

    public long getExcitementReportTime() {
        return excitementReportTime;
    }



    public String getLastEmotion() {
        return lastEmotion;
    }

    public void setLastEmotion (String e) {
        lastEmotion = e;
    }

    public int getLastEmotionValue() {
        return this.lastEmotionValue;
    }


    public void clearTutorHutState () throws SQLException {
        super.clearTutorHutState();
        DbStateTableMgr.clear(conn, TABLE_NAME, objid);
    }

    /**
     * WoAdmin tool allows clearing the tutor hut of all info about student.    This will
     * reset all the student model info about his activity in the tutor hut.
     * @throws SQLException
     */
    public void clearTutorHutStateOld() throws SQLException {
        for (String s : TABLE_COLS) {
            clearProp(objid,s);
        }
    }

    public void setAbility(double ability) {
        this.ability = ability;
    }

    public void setMotiv_hintAbuse(double motiv_hintAbuse) {
        this.motiv_hintAbuse = motiv_hintAbuse;
    }

    public void setMotiv_quickGuess(double motiv_quickGuess) {
        this.motiv_quickGuess = motiv_quickGuess;
    }

    public void setMotiv_normal(double motiv_normal) {
        this.motiv_normal = motiv_normal;
    }

    public void setMotiv_problemSkipping(double motiv_problemSkipping) {
        this.motiv_problemSkipping = motiv_problemSkipping;
    }


    public long getLastInterventionTime() {
        return lastInterventionTime;
    }

    public void setLastInterventionTime(long lastInterventionTime) {
        this.lastInterventionTime = lastInterventionTime;
    }

    public void setLastEmotionValue(int lastEmotionValue) {
        this.lastEmotionValue = lastEmotionValue;
    }

    public void setReportedInterest(int reportedInterest) {
        this.reportedInterest = reportedInterest;
    }

    public void setInterestReportTime(long interestReportTime) {
        this.interestReportTime = interestReportTime;
    }

    public void setReportedFrustration(int reportedFrustration) {
        this.reportedFrustration = reportedFrustration;
    }

    public void setFrustrationReportTime(long frustrationReportTime) {
        this.frustrationReportTime = frustrationReportTime;
    }

    public void setReportedConfidence(int reportedConfidence) {
        this.reportedConfidence = reportedConfidence;
    }

    public void setConfidenceReportTime(long confidenceReportTime) {
        this.confidenceReportTime = confidenceReportTime;
    }

    public void setReportedExcitement(int reportedExcitement) {
        this.reportedExcitement = reportedExcitement;
    }

    public void setExcitementReportTime(long excitementReportTime) {
        this.excitementReportTime = excitementReportTime;
    }

    public static void main(String[] args) {
           int studId = Integer.parseInt(args[0]);
           int classId = Integer.parseInt(args[1]);
             DbUtil.loadDbDriver();
             try {
                 Connection conn = DbUtil.getAConnection();
                 AffectStudentModel m = new AffectStudentModel(conn);

                 m.init(null,studId,classId);
                 m.setLastEmotion("HiDave");
                 m.setInformReward(449.4);
                 m.setNumHintsTotal(14);
                 m.save();
             } catch (SQLException e) {
                 e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             }

         }

}