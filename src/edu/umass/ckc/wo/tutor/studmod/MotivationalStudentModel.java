package edu.umass.ckc.wo.tutor.studmod;

public class MotivationalStudentModel  {
    /*
    private static Logger logger = Logger.getLogger(MotivationalStudentModel.class);

    private static final String STUD_FNAME = "fname";
    static final int GENDER_PROP_ID = 3;
    public static final int INCOMPLETE_HISTORY = -1;
    private static final String RECENTATTEMPT_NOHINT = "RecentAttemptsNoHint";
    private static final String RECENTATTEMPT_HINT = "RecentAttemptsHint";
    private static final String PREVIOUS_ATTEMPT_NOHINT = "PreviousAttemptsNoHint";

    private static final String FIRSTATTEMPT_NOHINT = "FirstAttemptsNoHint";
    private static final String FIRSTATTEMPT_HINT = "FirstAttemptsHint";
    private static final String NUM_PROBS_SOLVED = "numProbsSolved";
    private static final String MOTIVATED = "motivated";
    private static final String SELF_CONFIDENT = "self-confident";
    public static final int HISTORY_LENGTH = 5;
    private static final int FIRST_ATT_INCORRECT = 0;
    private static final int FIRST_ATT_CORRECT = 1;
    private static final boolean HIGH = true;
    private static final boolean LOW = false;
    static final boolean debugthisclass = false;

    public static final String avgAttemptsInAssistedProblem_SQL = "inchp_ct";
    public static final String avgTimeInProblem_ms_SQL = "secsp_ct";
    public static final String Serious_SQL = "ser_cat";
    public static final String gender_SQL = "gender"; // zero is male, 1 is female
    public static final String avgTimeBetweenAttempts_SQL = "tmbet_ct"; // milliseconds
    public static final String avgHintsGivenPerProb_SQL = "avgh_ct";
    public static final String avgHintsGivenPerAssistedProb_SQL = "avghtshp";
    public static final String perc_helpedProblems_SQL = "pc_hp_ct";
    public static final String perc_hintsAfterSolve_SQL = "p_afc_ct";
    public static final String avgAttemptsInProb_SQL = "incap_ct";
    public static final String pretestNumCorrect_SQL = "preco_ct";
    public static final String pretestNumIncorrect_SQL = "pr_in_ct";
    public static final String perc_hintsBeforeAttempt_SQL = "p_hba_ct";
    public static final String avgTimeInAssistedProblem_ms_SQL = "scshp_ct"; //milliseconds
    public static final String ChallengeAttitude_SQL = "challeng";
    public static final String OtherApproachesAttitude_SQL = "otherapp";
    public static final String NotCareHelpAttitude_SQL = "nocareh";
    public static final String GetOverWithAttitude_SQL = "fast";
    public static final String LearnedPerception_SQL = "lrn_cat";
    public static final String HelpfulPerception_SQL = "hlp_cat";
    public static final String ReturnPerception_SQL = "ret_cat";
    public static final String LikedPerception_SQL = "lik_cat";
    public static final String LearningEstimation_SQL = "g_cor_ct";
    public static final String FearOfWrong_SQL = "nowrong";

    public double P_ChallengeAttitude;
    public double P_Serious;
    public double P_OtherApproachesAttitude;
    public double P_NotCareHelpAttitude;
    public double P_GetOverWithAttitude;
    public double P_LearnedPerception;
    public double P_HelpfulPerception;
    public double P_ReturnPerception;
    public double P_LikedPerception;
    public double P_LearningEstimation;
    public double P_FearOfWrong;



    public static final double ChallengeAttitude_Threshold = 0.5524;
    public static final double OtherApproachesAttitude_Threshold = 0.2621;
    public static final double NotCareHelpAttitude_Threshold = 0.1739;
    public static final double GetOverWithAttitude_Threshold = 0.1458;
    public static final double LearnedPerception_Threshold = 0.7785;

    public static final double HelpfulPerception_Threshold = 0.5245;
    public static final double ReturnPerception_Threshold = 0.6440;
    public static final double LikedPerception_Threshold = 0.7491;
    public static final double LearningEstimation_Threshold = 0.4604;
    public static final double FearOfWrong_Threshold = 0.1658;
    public static final double Serious_Threshold = 0.62;

// 13 Behavioral Variables
    public static final double avgAttemptsInAssistedProblem_Threshold = 1.06;
    public static final double avgTimeInProblem_ms_Threshold = 55000;
    public static final double gender_Threshold = 0.5; // zero is male, 1 is female
    public static final double avgTimeBetweenAttempts_Threshold = 3807; // milliseconds
    public static final double avgHintsGivenPerProb_Threshold = 0.71;
    public static final double avgHintsGivenPerAssistedProb_Threshold = 2.38;
    public static final double perc_helpedProblems_Threshold = 0.34;
    public static final double perc_hintsAfterSolve_Threshold = 0.03;
    public static final double avgAttemptsInProb_Threshold = 1.05;
    public static final double pretestNumCorrect_Threshold = 11;
    public static final double pretestNumIncorrect_Threshold = 10;
    public static final double perc_hintsBeforeAttempt_Threshold = 0.91;
    public static final double avgTimeInAssistedProblem_ms_Threshold = 76300; //milliseconds

    public boolean motivated;
    public boolean self_confident;
    public boolean problemSolved = true;
//    public int numProbsSolved = 0; // must be updated when user exits a problem
//    public int numProbsSinceLastIntervention = 0;    // TODO moved to StudentState
    public String fname; //Gets initialized in the setServletInfo function
    private int recentProbsAttempt1NoHint_1=0;
    private int recentProbsAttempt1NoHint_2=0;
    private int recentProbsAttempt1NoHint_3=0;
    private int recentProbsAttempt1NoHint_4=0;
    private int recentProbsAttempt1NoHint_5=0;
//    private Vector RecentProbs_FirstAttempt_NoHint; //Records the correctness of first attempt for those problems with no hints
    private Vector RecentProbs_FirstAttempt_WithPossibleHint; //Records the correctness of first attempt for the last problems
    private Vector previousProbs_FirstAttempt_NoHint;//tracks the problems before Recent problems

    private Vector FirstProbs_FirstAttempt_NoHint; //Records the correctness of first attempt for those problems with no hints
    private Vector FirstProbs_FirstAttempt_WithPossibleHint; //Records the correctness of first attempt for the last problems
    private double perc_helpedProblems = 0;
    private double perc_hintsAfterSolve = 0;
    private double perc_hintsBeforeAttempt = 0;

    public MotivationalStudentModel(Connection conn) throws SQLException {
        super(conn);
    }


    public void doLoad (int studId) throws SQLException {
        //get whether the problem is solved
        this.problemSolved = true;

        super.load(conn, studId);

        fname = getStudentFirstName();
        //numProbsSolved is the same as totalProblemsSolved in the StudentState
//        numProbsSolved = getPropInt(this.objid, NUM_PROBS_SOLVED, 0);

        RecentProbs_FirstAttempt_WithPossibleHint = new Vector();


// Still have to load the WithPossibleHint
        for (int i = 0; i < HISTORY_LENGTH; i++) {
            int prop = getPropInt(this.objid, RECENTATTEMPT_NOHINT + (i + 1), -1);
            if (prop == -1) // If there is no data saved for this element
                break;
            RecentProbs_FirstAttempt_NoHint.addElement(new Integer(prop));
        }

        for (int i = 0; i < HISTORY_LENGTH; i++) {
            int prop = getPropInt(this.objid, RECENTATTEMPT_HINT + (i + 1), -1);
            if (prop == -1) // If there is no data saved for this element
                break;
            RecentProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(prop));
        }

        previousProbs_FirstAttempt_NoHint = new Vector();
        for (int i = 0; i < HISTORY_LENGTH; i++) {
            int prop = getPropInt(this.objid, PREVIOUS_ATTEMPT_NOHINT + (i + 1), -1);
            if (prop == -1) // If there is no data saved for this element
                break;
            previousProbs_FirstAttempt_NoHint.addElement(new Integer(prop));
        }

        FirstProbs_FirstAttempt_NoHint = new Vector();
        FirstProbs_FirstAttempt_WithPossibleHint = new Vector();

        for (int i = 0; i < HISTORY_LENGTH; i++) {
            int prop = getPropInt(this.objid, FIRSTATTEMPT_NOHINT + (i + 1), -1);
            if (prop == -1) // If there is no data saved for this element
                break;
            FirstProbs_FirstAttempt_NoHint.addElement(new Integer(prop));
        }
        for (int i = 0; i < HISTORY_LENGTH; i++) {
            int prop = getPropInt(this.objid, FIRSTATTEMPT_HINT + (i + 1), -1);
            if (prop == -1) // If there is no data saved for this element
                break;
            FirstProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(prop));
        }

        //Define some more variables that are a function of the other ones
        perc_helpedProblems = (this.numProbsSeen == 0) ? 0 : this.numProbsReceivedAssistance / (new Double(numProbsSeen)).doubleValue();

        perc_hintsAfterSolve = (this.numHintsTotal == 0) ? 0 : this.numHintsAfterSolveTotal / (new Double(numHintsTotal)).doubleValue();  // not totally right
        perc_hintsBeforeAttempt = (numHintsTotal == 0) ? 0 : (numHintsTotal - numHintsAfterSolveTotal) / (new Double(numHintsTotal)).doubleValue();  // not totally right

        loadMotivationalVariables();
    }

    public void load (Connection conn, int studId) throws SQLException {
        super.load(conn,studId);
        doLoad(studId);
    }


    public void setServletInfo(WoProps props, int studId, int classId) throws SQLException {
        super.setServletInfo(props, studId, classId);
        doLoad(studId);

    }

    boolean Challenge() {
        return (P_ChallengeAttitude > ChallengeAttitude_Threshold) ? true : false;
    }

    boolean OtherApproaches() {
        return (this.P_OtherApproachesAttitude >
                this.OtherApproachesAttitude_Threshold) ? true : false;
    }

    boolean NotCareHelp() {
        return (P_NotCareHelpAttitude > this.NotCareHelpAttitude_Threshold) ? true : false;
    }

    boolean GetOverWith() {
        return (P_GetOverWithAttitude > this.GetOverWithAttitude_Threshold) ? true : false;
    }

    boolean LearnedPerception() {
        return (P_LearnedPerception > this.LearnedPerception_Threshold) ? HIGH :
                LOW;
    }

    boolean HelpfulPerception() {
        return (P_HelpfulPerception > this.HelpfulPerception_Threshold) ? HIGH :
                LOW;
    }

    boolean Return() {
        return (P_ReturnPerception > this.ReturnPerception_Threshold) ? HIGH : LOW;
    }

    boolean Liked() {
        return (P_LikedPerception > this.LikedPerception_Threshold) ? HIGH : LOW;
    }

    boolean LearningEstimation() {
        return (P_LearningEstimation > this.LearningEstimation_Threshold) ? HIGH :
                LOW;
    }

    boolean FearOfWrong() {
        return (P_FearOfWrong > FearOfWrong_Threshold) ? true : false;
    }

    boolean Serious() {
        return (P_Serious > Serious_Threshold) ? true : false;
    }

    private String getBehaviorRestrictions() {
        StringBuffer s = new StringBuffer();

        s.append(avgAttemptsInAssistedProblem_SQL + "=" +
                (avgAttemptsInAssistedProb <=
                avgAttemptsInAssistedProblem_Threshold ? "0" : "1") + " and ");

        s.append(avgTimeInProblem_ms_SQL + "=" +
                (avgTimeInProb <= avgTimeInProblem_ms_Threshold ? "0" : "1") +
                " and ");

        s.append(gender_SQL + "=" +
                (getGenderVal() >= gender_Threshold ? "0" : "1") + " and ");

        s.append(avgTimeBetweenAttempts_SQL + "=" +
                (avgTimeBetweenAttempts <=
                avgTimeBetweenAttempts_Threshold ? "0" : "1") + " and ");

        s.append(avgHintsGivenPerProb_SQL + "=" +
                (avgHintsGivenPerProb <=
                avgHintsGivenPerProb_Threshold ? "0" : "1") + " and ");

        s.append(avgHintsGivenPerAssistedProb_SQL + "=" +
                (avgHintsGivenPerAssistedProb <=
                avgHintsGivenPerAssistedProb_Threshold ? "0" : "1") + " and ");

        s.append(perc_helpedProblems_SQL + "=" +
                (perc_helpedProblems <= perc_helpedProblems_Threshold ? "0" : "1") + " and ");

        s.append(perc_hintsAfterSolve_SQL + "=" +
                (perc_hintsAfterSolve <= perc_hintsAfterSolve_Threshold ? "0" : "1") + " and ");

        s.append(avgAttemptsInProb_SQL + "=" +
                (avgAttemptsInProb <= avgAttemptsInProb_Threshold ? "0" : "1") + " and ");

        s.append(pretestNumCorrect_SQL + "=" +
                (pretestNumCorrect <= pretestNumCorrect_Threshold ? "0" : "1") + " and ");

        s.append(pretestNumIncorrect_SQL + "=" +
                (pretestNumIncorrect <= pretestNumIncorrect_Threshold ? "0" : "1") + " and ");

        s.append(perc_hintsBeforeAttempt_SQL + "=" +
                (perc_hintsBeforeAttempt <= perc_hintsBeforeAttempt_Threshold ? "0" : "1") + " and ");

        s.append(avgTimeInAssistedProblem_ms_SQL + "=" +
                (avgTimeInAssistedProb <= avgTimeInAssistedProblem_ms_Threshold ? "0" : "1"));

        if (debugthisclass)
            System.out.println(s);

        return s.toString();
    }

    // Estimate motivational variables based on behaviors
    private void loadMotivationalVariables() {

        String behavior_restrictions = getBehaviorRestrictions();
        String q = new String("Select * from bbn_inferences where " + behavior_restrictions);

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next()) {
                P_ChallengeAttitude = rs.getDouble(ChallengeAttitude_SQL);
                P_Serious = rs.getDouble(Serious_SQL);
                P_OtherApproachesAttitude = rs.getDouble(OtherApproachesAttitude_SQL);
                P_NotCareHelpAttitude = rs.getDouble(NotCareHelpAttitude_SQL);
                P_GetOverWithAttitude = rs.getDouble(GetOverWithAttitude_SQL);
                P_LearnedPerception = rs.getDouble(LearnedPerception_SQL);
                P_HelpfulPerception = rs.getDouble(HelpfulPerception_SQL);
                P_ReturnPerception = rs.getDouble(ReturnPerception_SQL);
                P_LikedPerception = rs.getDouble(LikedPerception_SQL);
                P_LearningEstimation = rs.getDouble(LearningEstimation_SQL);
                P_FearOfWrong = rs.getDouble(FearOfWrong_SQL);
            }
        } catch (SQLException e) {
        }
        motivated = getMotivationEstimation();
        self_confident = getSelfConfidenceEstimate();
        debug();
    }

    private boolean getSelfConfidenceEstimate() {
        if (!FearOfWrong() && ((LearnedPerception() == LearningEstimation()) || LearnedPerception() == HIGH))  // Not lowLearnedPerception&LearnedEstimate=high
            return true;

        return false;
    }


    private boolean getMotivationEstimation() {
        int motiv_evidence = 0;
        if (LearnedPerception())
            motiv_evidence++;
        if (OtherApproaches())
            motiv_evidence++;
        if (Serious())
            motiv_evidence++;
        if (!GetOverWith())
            motiv_evidence++;
        if (!NotCareHelp())
            motiv_evidence++;
        if (HelpfulPerception())
            motiv_evidence++;
        if (Return())
            motiv_evidence++;
        if (Liked())
            motiv_evidence++;

        if (this.debugthisclass)
            System.out.println("Motivation evidence=" + motiv_evidence);

        if (motiv_evidence >= 3)
            return true;

        return false;
    }


    public void newProblem(StudentState state, Problem problem) throws
            SQLException {
        super.newProblem(state, problem);

    }



    public void hintGiven(StudentState state, Hint hint) throws SQLException {
        super.hintGiven(state, hint);
    }

    public void helpAidGiven(StudentState state) throws SQLException {
        super.helpAidGiven(state);
    }


    public void studentAttempt(StudentState state, String answer,
                               boolean isCorrect, long probElapsed) throws SQLException {
        super.studentAttempt(state, answer, isCorrect, probElapsed);



        if (isCorrect)
            this.numProbsSolved++;
        // If the problem was solved correctly in the first attempt
        if (state.getNumAttemptsOnCurProblem() == 1 && isCorrect) {
            // If there are no hints seen until now
            if (state.getNumHintsGivenOnCurProblem() == 0) {
                if (this.FirstProbs_FirstAttempt_NoHint.size() < HISTORY_LENGTH)
                    this.FirstProbs_FirstAttempt_NoHint.addElement(new Integer(FIRST_ATT_CORRECT)); //add the new element at the end of the queue

                if (this.RecentProbs_FirstAttempt_NoHint.size() >= HISTORY_LENGTH) {
                    //here we remove the oldest element of recentProbs, and enter into previousProbs
                    Object prevProb = this.RecentProbs_FirstAttempt_NoHint.remove(0); //remove the oldest element
                    if (this.previousProbs_FirstAttempt_NoHint.size() >= HISTORY_LENGTH) {
                        this.previousProbs_FirstAttempt_NoHint.remove(0);
                    }
                    this.previousProbs_FirstAttempt_NoHint.addElement(prevProb);
                }
                this.RecentProbs_FirstAttempt_NoHint.addElement(new Integer(FIRST_ATT_CORRECT)); //add the new element at the end of the queue
            }

            // Regardless of the hints seen, but the problem was answered correctly at first
            if (this.FirstProbs_FirstAttempt_WithPossibleHint.size() < HISTORY_LENGTH)
                this.FirstProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(FIRST_ATT_CORRECT)); //add the new element at the end of the queue
            if (this.RecentProbs_FirstAttempt_WithPossibleHint.size() >= HISTORY_LENGTH) {
                this.RecentProbs_FirstAttempt_WithPossibleHint.remove(0); //remove the oldest element
            }
            this.RecentProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(FIRST_ATT_CORRECT)); //add the new element at the end of the queue
        }
        // If the first attempt to solve this problem was incorrect
        if (state.getNumAttemptsOnCurProblem() == 1 && !isCorrect) {

// Doesn't matter if hints were seen or not, because the problem was solved incorrectly
            if (FirstProbs_FirstAttempt_NoHint.size() < HISTORY_LENGTH)
                this.FirstProbs_FirstAttempt_NoHint.addElement(new Integer(FIRST_ATT_INCORRECT)); //add the new element at the end of the queue

            // Regardless of whether they saw hints or not, add a zero to the queue of each vector
            if (this.FirstProbs_FirstAttempt_WithPossibleHint.size() < HISTORY_LENGTH)
                this.FirstProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(FIRST_ATT_INCORRECT)); //add the new element at the end of the queue

            if (this.RecentProbs_FirstAttempt_NoHint.size() >= HISTORY_LENGTH) {
                Object prevProb = this.RecentProbs_FirstAttempt_NoHint.remove(0); //remove the oldest element
                    if (this.previousProbs_FirstAttempt_NoHint.size() >= HISTORY_LENGTH) {
                        this.previousProbs_FirstAttempt_NoHint.remove(0);
                    }
                    this.previousProbs_FirstAttempt_NoHint.addElement(prevProb);
            }
            this.RecentProbs_FirstAttempt_NoHint.addElement(new Integer(FIRST_ATT_INCORRECT)); //add the new element at the end of the queue

            if (this.RecentProbs_FirstAttempt_WithPossibleHint.size() >= HISTORY_LENGTH) {
                this.RecentProbs_FirstAttempt_WithPossibleHint.remove(0); //remove the oldest element
            }
            this.RecentProbs_FirstAttempt_WithPossibleHint.addElement(new Integer(FIRST_ATT_INCORRECT)); //add the new element at the end of the queue
        }
    }



    public void interventionGiven(StudentState state, Intervention intervention) throws
            SQLException {
        super.interventionGiven(state, intervention);
    }


    public boolean save() throws SQLException {

        logger.debug("Save BEGIN ");
        super.save();
        long n = System.currentTimeMillis();

        for (int i = 1; i <= HISTORY_LENGTH; i++) {
            int property = -1;
            if (i <= RecentProbs_FirstAttempt_NoHint.size())
                property = ((Integer) RecentProbs_FirstAttempt_NoHint.elementAt(i - 1)).intValue();
            setProp(this.objid, RECENTATTEMPT_NOHINT + i, property);

            property = -1;
            if (i <= previousProbs_FirstAttempt_NoHint.size())
                property = ((Integer) previousProbs_FirstAttempt_NoHint.elementAt(i - 1)).intValue();
            setProp(this.objid, PREVIOUS_ATTEMPT_NOHINT + i, property);

            property = -1;
            if (i <= FirstProbs_FirstAttempt_NoHint.size())
                property = ((Integer) FirstProbs_FirstAttempt_NoHint.elementAt(i - 1)).intValue();
            setProp(this.objid, FIRSTATTEMPT_NOHINT + i, property);

            property = -1;
            if (i <= RecentProbs_FirstAttempt_WithPossibleHint.size())
                property = ((Integer) RecentProbs_FirstAttempt_WithPossibleHint.elementAt(i - 1)).intValue();
            setProp(this.objid, this.RECENTATTEMPT_HINT + i, property);

            property = -1;
            if (i <= FirstProbs_FirstAttempt_WithPossibleHint.size())
                property = ((Integer) FirstProbs_FirstAttempt_WithPossibleHint.elementAt(i - 1)).intValue();
            setProp(this.objid, FIRSTATTEMPT_HINT + i, property);
        }

        setProp(this.objid, NUM_PROBS_SOLVED, numProbsSolved);
        setProp(this.objid, MOTIVATED, Math.random() > .5 ? true : false);
        setProp(this.objid, SELF_CONFIDENT, Math.random() > .5 ? true : false);
        logger.debug("Save DONE took :" + (System.currentTimeMillis() - n));

        return true;
    }



    public Vector getSkillNames() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector skillnames = new Vector();

        try {
            String q = "select name from abstractskill";
            ps = conn.prepareStatement(q);

            rs = ps.executeQuery();
            if (rs.next()) {
                skillnames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
        }

        return skillnames;
    }

    public double getPercCorrectRecentProbs_NoHint() {
        int accum_corr = 0;
        double count = 0;

        if (RecentProbs_FirstAttempt_NoHint.size() < HISTORY_LENGTH)
            return INCOMPLETE_HISTORY;

        for (int i = 0; i < RecentProbs_FirstAttempt_NoHint.size() || i == HISTORY_LENGTH - 1; i++) {
            accum_corr += ((Integer) RecentProbs_FirstAttempt_NoHint.elementAt(i)).intValue();
            count++;
        }
        return accum_corr / count;
    }

    public double getPercCorrectPrevProbs_NoHint() {
            int accum_corr = 0;
            double count = 0;

            if (previousProbs_FirstAttempt_NoHint.size() < HISTORY_LENGTH)
                return INCOMPLETE_HISTORY;

            for (int i = 0; i < previousProbs_FirstAttempt_NoHint.size() || i == HISTORY_LENGTH - 1; i++) {
                accum_corr += ((Integer) previousProbs_FirstAttempt_NoHint.elementAt(i)).intValue();
                count++;
            }
            return accum_corr / count;
        }


    public double getPercCorrectRecentProbs_WithPossibleHint() {
        int accum_corr = 0;
        double count = 0;

        if (this.RecentProbs_FirstAttempt_WithPossibleHint.size() < HISTORY_LENGTH)
            return -1;

        for (int i = 0; i < RecentProbs_FirstAttempt_WithPossibleHint.size() || i == HISTORY_LENGTH - 1; i++) {
            accum_corr += ((Integer) RecentProbs_FirstAttempt_WithPossibleHint.elementAt(i)).intValue();
            count++;
        }
        return accum_corr / count;
    }

    public double getPercCorrectFirstProbs_WithPossibleHint() {
        int accum_corr = 0;
        double count = 0;

        if (FirstProbs_FirstAttempt_WithPossibleHint.size() < HISTORY_LENGTH)
            return INCOMPLETE_HISTORY;

        for (int i = 0; i < FirstProbs_FirstAttempt_WithPossibleHint.size() || i == HISTORY_LENGTH - 1; i++) {
            accum_corr += ((Integer) FirstProbs_FirstAttempt_WithPossibleHint.elementAt(i)).intValue();
            count++;
        }
        return accum_corr / count;
    }


    public double getPercCorrectFirstProbs_NoHint() {
        int accum_corr = 0;
        double count = 0;

        if (FirstProbs_FirstAttempt_NoHint.size() < HISTORY_LENGTH)
            return INCOMPLETE_HISTORY;

        for (int i = 0; i < RecentProbs_FirstAttempt_NoHint.size() || i == HISTORY_LENGTH - 1; i++) {
            accum_corr += ((Integer) FirstProbs_FirstAttempt_NoHint.elementAt(i)).intValue();
            count++;
        }
        return accum_corr / count;
    }

    private double getGenderVal() {
        if (gender.trim().startsWith("f")) //female student
            return 1;

        if (gender.trim().startsWith("m")) //male student
            return 0;

        String g = "unknown";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select value from UserPropVal where propid=" + GENDER_PROP_ID +
                    " and studid=" + this.objid;

            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next())
                g = rs.getString("value");
        } catch (SQLException e) {
        }

        if (((g.trim()).toLowerCase()).startsWith("f"))
            return 1;

        if (((g.trim()).toLowerCase()).startsWith("m"))
            return 0;

        if (this.debugthisclass)
            System.out.println("Gender not understood: " + g + ". Returning random gender.");

        return (Math.random());
    }


    private void debug() {
        if (this.debugthisclass) {
            System.out.println("Motivated = " + motivated);
//      System.out.println("       Because positive attitude = " + (Challenge() || OtherApproaches() || Serious()) ) ;
            System.out.println("            LearnedPerception = " + LearnedPerception());
            System.out.println("                Because P(LearnedPerception)=" + P_LearnedPerception);
            System.out.println("            OtherApproaches = " + OtherApproaches());
            System.out.println("                Because P(OtherApproaches)=" + P_OtherApproachesAttitude);
            System.out.println("            Serious = " + Serious());
            System.out.println("                Because P(Serious)=" + P_Serious);

//      System.out.println("       Because negative attitude = " + (GetOverWith() || NotCareHelp())) ;
            System.out.println("            GetOverWith = " + GetOverWith());
            System.out.println("                Because P(GetOverWith)=" + P_GetOverWithAttitude);
            System.out.println("            NotCareHelp = " + NotCareHelp());
            System.out.println("                Because P(NotCareHelp)=" + P_NotCareHelpAttitude);

//      System.out.println("       Because positive perception = " + (HelpfulPerception() || Return() || Liked())) ;
            System.out.println("            HelpfulPerception = " + HelpfulPerception());
            System.out.println("                Because P(HelpfulPerception)=" + P_HelpfulPerception);
            System.out.println("            Return = " + Return());
            System.out.println("                Because P(Return)=" + P_ReturnPerception);
            System.out.println("            Liked = " + Liked());
            System.out.println("                Because P(Liked)=" + P_LikedPerception);
            System.out.println();

            System.out.println("Self-confidence = " + this.self_confident + " because ");
            System.out.println("            Fear of wrong = " + FearOfWrong());
            System.out.println("                Because P(Fear of wrong)=" + this.P_FearOfWrong);
            System.out.println("            LearnedPerception = " + LearnedPerception());
            System.out.println("                Because P(LearnedPerception)=" + P_LearnedPerception);
            System.out.println("            Learning = " + this.LearningEstimation());
            System.out.println("                Because P(Learning)=" + this.P_LearningEstimation);
            System.out.println();

            System.out.println("Behaviors:");

            System.out.println("avgAttemptsInAssistedProblem =" + avgAttemptsInAssistedProb + "=" + (avgAttemptsInAssistedProb <=
                    avgAttemptsInAssistedProblem_Threshold ? "Low" : "High"));

            System.out.println("avgTimeInProb =" + avgTimeInProb + "=" + (avgTimeInProb <=
                    avgTimeInProblem_ms_Threshold ? "Low" : "High"));

            System.out.println("gender =" + getGenderVal());

            System.out.println("avgTimeBetweenAttempts =" + avgTimeBetweenAttempts + "=" + (avgTimeBetweenAttempts <=
                    avgTimeBetweenAttempts_Threshold ? "Low" : "High"));

            System.out.println("avgHintsGivenPerProb =" + avgHintsGivenPerProb + "=" + (avgHintsGivenPerProb <=
                    avgHintsGivenPerProb_Threshold ? "Low" : "High"));

            System.out.println("avgHintsGivenPerAssistedProb =" + avgHintsGivenPerAssistedProb + "=" + (avgHintsGivenPerAssistedProb <=
                    avgHintsGivenPerAssistedProb_Threshold ? "Low" : "High"));

            System.out.println("perc_helpedProblems =" + perc_helpedProblems + "=" + (perc_helpedProblems <=
                    perc_helpedProblems_Threshold ? "Low" : "High"));

            System.out.println("perc_hintsAfterSolve =" + perc_hintsAfterSolve + "=" + (perc_hintsAfterSolve <=
                    perc_hintsAfterSolve_Threshold ? "Low" : "High"));

            System.out.println("avgAttemptsInProb =" + avgAttemptsInProb + "=" + (avgAttemptsInProb <=
                    avgAttemptsInProb_Threshold ? "Low" : "High"));

            System.out.println("pretestNumCorrect =" + pretestNumCorrect + "=" + (pretestNumCorrect <=
                    pretestNumCorrect_Threshold ? "Low" : "High"));

            System.out.println("pretestNumIncorrect =" + pretestNumIncorrect + "=" + (pretestNumIncorrect <=
                    pretestNumIncorrect_Threshold ? "Low" : "High"));

            System.out.println("perc_hintsBeforeAttempt =" + perc_hintsBeforeAttempt + "=" + (perc_hintsBeforeAttempt <=
                    perc_hintsBeforeAttempt_Threshold ? "Low" : "High"));

            System.out.println("avgTimeInAssistedProblem_ms =" + avgTimeInAssistedProb + "=" + (avgTimeInAssistedProb <=
                    avgTimeInAssistedProblem_ms_Threshold ? "Low" : "High"));
        }
    }

 public void clearTutorHutState() throws SQLException {
    clearProp(objid, RECENTATTEMPT_NOHINT );
    clearProp(objid, RECENTATTEMPT_HINT );
    clearProp(objid, PREVIOUS_ATTEMPT_NOHINT );
    clearProp(objid, FIRSTATTEMPT_NOHINT );
    clearProp(objid, FIRSTATTEMPT_HINT );
    clearProp(objid, NUM_PROBS_SOLVED );
    clearProp(objid, MOTIVATED );
    clearProp(objid, SELF_CONFIDENT );
    clearProp(objid, avgAttemptsInAssistedProblem_SQL );
    clearProp(objid, avgTimeInProblem_ms_SQL );
    clearProp(objid, Serious_SQL );
    clearProp(objid, gender_SQL );
    clearProp(objid, avgTimeBetweenAttempts_SQL );
    clearProp(objid, avgHintsGivenPerProb_SQL );
    clearProp(objid, avgHintsGivenPerAssistedProb_SQL );
    clearProp(objid, perc_helpedProblems_SQL );
    clearProp(objid, perc_hintsAfterSolve_SQL );
    clearProp(objid, avgAttemptsInProb_SQL );
    clearProp(objid, pretestNumCorrect_SQL );
    clearProp(objid, pretestNumIncorrect_SQL );
    clearProp(objid, perc_hintsBeforeAttempt_SQL );
    clearProp(objid, avgTimeInAssistedProblem_ms_SQL );
    clearProp(objid, ChallengeAttitude_SQL );
    clearProp(objid, OtherApproachesAttitude_SQL );
    clearProp(objid, NotCareHelpAttitude_SQL );
    clearProp(objid, GetOverWithAttitude_SQL );
    clearProp(objid, LearnedPerception_SQL );
    clearProp(objid, HelpfulPerception_SQL );
    clearProp(objid, ReturnPerception_SQL );
    clearProp(objid, LikedPerception_SQL );
    clearProp(objid, LearningEstimation_SQL );
    clearProp(objid, FearOfWrong_SQL );

    }

    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection();
            new DbStateTableMgr(conn).printCols("foo");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    */

}
