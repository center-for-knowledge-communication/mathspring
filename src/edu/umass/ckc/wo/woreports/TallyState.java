package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.wo.enumx.Actions;
import edu.umass.ckc.wo.enumx.StudentInputEnum;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Set;
import java.util.Iterator;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 20, 2005
 * Time: 4:04:22 PM
 */
public class TallyState {
    protected String curAction = "";
    protected int probSeqNum = 0;
    protected long totalHintTime = 0;
    protected boolean[] probSkills; // array of t/f for whether skill is involved in this prob
    protected int probMistakes = 0;
    protected int probHintsSeen = 0;
    int numProbsSeen=0;
    int numPreGiven = -1;
    int numPreCorrect = -1;
    int numPreIncorrect = -1;
    int numPostGiven = -1;
    int numPostCorrect = -1;
    int numPostIncorrect = -1;
    String preProblemSet = null;
    String postProblemSet = null;
    double pre_pct = 0.0;
    double post_pct = 0.0;
    double gain = 0.0;

    int totalMistakes = 0;
    long totalSolveTime = 0;
    double avgHintTime = 0.0;
    double avgProbMistakes;        // n ml  totalMistakes/probsSeen
    double avgProbHints;        // n ml   totalHintsSeen/probsSeen
    double avgSolveTime;        // n ml   totalSolveTime/probsSeen

    protected int version = -1;
    protected boolean givenAnswer = false;
    protected int solnPathLength = 0;  // this one cannot be done because we know nothing about which soln path is being used
    protected int totalHintsSeen = 0;
    protected long solveTime = 0;      // how long (from the beginning of prob) did it take to get a correct answer
    protected boolean isSolved = false;  // was the problem solved
    protected int[] skillHintCounters; // for each skill, the # hints have been seen
    protected boolean outputReady = true; // if the state is ready to output a row
    protected long hintBeginTime = 0; // time when the current hint began
    protected String lastState = null; // the state the user was in on the last call to tally
    protected String nextState = ""; // the state the user is in on this call to tally
    protected SkillMap skillMap = null;
    protected String[] skillNames;  // an array of semi-abstract skill names
    public static final String HINT_STATE = "hint";
    public static final String HINT_ACC = "hintAccepted";
    public static final String HINT_REJ = "hintRejected";
    public static final String BEGIN_PROBLEM = "beginProblem";
    public static final String END_PROBLEM = "endProblem";
    public static final String ATTEMPT = "attempt";
    public static final int NUM_OUTPUTCOLS = 23;
    private int studId;
    private int probId;
    private double probDiff;
    private int numAttempts = 0;
    private int timeToFirstEvent = 0;
    private String firstEvent = null;
    private int numHintsBeforeCorrect = -1;

    /**
     * todo
     * The pretest scores are in the database, in a table called "PencilPaperData".
     * - action name
     * - policy that it had (problem selector) will be in the pretest-posttest table.
     */

    public String[] getColHeaders() {
//        String[] row = new String[NUM_OUTPUTCOLS + skillNames.length * 2];
        String[] row = new String[NUM_OUTPUTCOLS];
        row[0] = "ProbSeqNum";
        row[1] = "totalHintTime";
        row[2] = "ProbMistakes";
        row[3] = "totalHintsSeen";
        row[4] = "ProbSolveTime";
        row[5] = "StudId";
        row[6] = "ProbId";
        row[7] = "ProbDifficulty";
        row[8] = "Action";
        row[9] = "AnswerGiven";
        row[10] = "ProbHintsSeen";
        row[11] = "PrePct";   // skipped problems are marked wrong in this pct
        row[12] = "Gain";   // (post - pre) / pre
        row[13] = "ProbSolved";
        row[14] = "TimeToFirstEvent";        // new
        row[15] = "firstEvent";         // new
        row[16] = "NumHintsBeforeCorrect";        // new
//        row[17] = "PostPct";        // new    don't need
//        row[18] = "numPreGiven";        // new     dn
//        row[19] = "numPreCorrect";        // new      dn
//        row[20] = "numPreIncorrect";        // new     dn
//        row[21] = "preProblemSet";        // new         dn
//        row[22] = "numPostGiven";        // new        dn
//        row[23] = "numPostCorrect";        // new        dn
//        row[24] = "numPostIncorrect";        // new     dn
//        row[25] = "postProblemSet";        // new         dn
        row[17] = "avgHintTime";        // n ml    totalHintTime/probsSeen
        row[18] = "avgProbMistakes";        // n ml  totalMistakes/probsSeen
        row[19] = "avgProbHints";        // n ml   totalNumHints/probsSeen
        row[20] = "avgSolveTime";        // n ml   totalSolveTime/probsSeen
        row[21] = "totalSolveTime";
        row[NUM_OUTPUTCOLS - 1] = "Version";
        // don't need skill stuff anymore.
//        int ix = 0;
//        for (int i = 0; i < probSkills.length; i++) {
//            row[ix = (NUM_OUTPUTCOLS + i)] = "\"" + skillNames[i] + "-UsedInProb" + "\"";
//        }
//        for (int i = 0, j = ix + 1; i < skillHintCounters.length; i++, j++) {
//            row[j] = "\"" + skillNames[i] + "-NumHintsSeen" + "\"";
//        }
        return row;
    }

    public String[] outputRow() {
        // eliminate output of columns where action != beginProblem:null
//        String[] row = new String[NUM_OUTPUTCOLS + skillNames.length * 2];
        String[] row = new String[NUM_OUTPUTCOLS];
        row[0] = Integer.toString(probSeqNum);            //n
        row[1] = Long.toString(totalHintTime);     //n
        row[2] = Integer.toString(probMistakes);  //n
        row[3] = Integer.toString(totalHintsSeen);   //n
        row[4] = Long.toString(solveTime);  // n
        row[5] = Integer.toString(studId);
        row[6] = Integer.toString(probId);
        row[7] = Double.toString(probDiff); // n
        row[8] = curAction;        //n
        row[9] = Boolean.toString(givenAnswer);    //n
        row[10] = Integer.toString(probHintsSeen);
        row[11] = Double.toString(pre_pct);       //n
        row[12] = Double.toString(gain);     // n
        row[13] = Boolean.toString(isSolved);
        row[14] = Integer.toString(timeToFirstEvent);  // new
        row[15] = firstEvent;              // new
        row[16] = Integer.toString(numHintsBeforeCorrect);    // new
//        row[17] = Double.toString(post_pct); //new
//        row[18] = Integer.toString(numPreGiven); // new
//        row[19] = Integer.toString(numPreCorrect); // new
//        row[20] = Integer.toString(numPreIncorrect); // new
//        row[21] = preProblemSet;
//        row[22] = Integer.toString(numPostGiven); // new
//        row[23] = Integer.toString(numPostCorrect); // new
//        row[24] = Integer.toString(numPostIncorrect); // new
//        row[25] = postProblemSet;
        row[17] = Double.toString((numProbsSeen != 0) ? ((double) totalHintTime/(double)numProbsSeen) : 0.0);        // n ml    totalHintTime/probsSeen
        row[18] = Double.toString((numProbsSeen != 0) ? ((double)totalMistakes/(double)numProbsSeen) : 0.0);        // n ml  totalMistakes/probsSeen
        row[19] = Double.toString((numProbsSeen != 0) ? ((double)totalHintsSeen/(double)numProbsSeen) : 0.0);        // n ml   totalHintsSeen/probsSeen
        row[20] = Double.toString((numProbsSeen != 0) ? ((double)totalSolveTime/(double)numProbsSeen) : 0.0);        // n ml   totalSolveTime/probsSeen
        row[21] = Long.toString(totalSolveTime);
        row[NUM_OUTPUTCOLS - 1] = Integer.toString(version);
//        int ix = 0;
//        for (int i = 0; i < probSkills.length; i++) {
//            boolean probSkill = probSkills[i];
//            row[ix = (NUM_OUTPUTCOLS + i)] = Boolean.toString(probSkill);
//        }
//        for (int i = 0, j = ix + 1; i < skillHintCounters.length; i++, j++) {
//            int skillHintCounter = skillHintCounters[i];
//            row[j] = Integer.toString(skillHintCounter);
//        }
        return row;
    }

    public boolean isOutputReady() {
        return outputReady;
    }

    /**
     * Must call this method before using the TallyState object.  It reads the skill and semiabstract skills and links
     * and builds a mapping
     *
     * @param conn
     * @throws SQLException
     */
    public void init(Connection conn) throws SQLException {
        skillMap = SkillMap.makeSkillMap(conn);
        Set skillSet = skillMap.getAbstractSkillNames();
        skillNames = new String[skillSet.size()];
        Iterator itr = skillSet.iterator();
        int i = 0;
        while (itr.hasNext()) {
            String s = (String) itr.next();
            skillNames[i++] = s;
        }
        skillHintCounters = new int[skillNames.length];
        probSkills = new boolean[skillNames.length];
    }

    public void tally(Connection conn, int studId, int pid, String action, String userInput, boolean isCorrect, int elapsedTime,
                      int probElapsed, String hintStep, int hintId, double probDifficulty,
                      double prepct, double gain, int version, int excelRowNum, double postpct, int numPreGiven, int numPostGiven,
                      int numPreCorrect, int numPreIncorrect, int numPostCorrect, int numPostIncorrect, String preProblemSet,
                      String postProblemSet) throws SQLException {

        if (pid == 5)
            System.out.print("");
        setNextState(action, userInput);
        setPrePost(version, prepct, gain, postpct, numPreGiven, numPreCorrect, numPreIncorrect, numPostGiven, numPostCorrect, numPostIncorrect, preProblemSet, postProblemSet);

        if (userInput != null)
            userInput = userInput.replace(',', '%');
        this.curAction = action + ":" + userInput;
        this.studId = studId;
        this.probId = pid;
        this.probDiff = probDifficulty;
        // if last state was hint and this state is non-hint update total hint time regardless
        if (lastState != null && lastState.equals(HINT_STATE) && !nextState.equals(HINT_STATE))
            totalHintTime += elapsedTime - hintBeginTime;

        outputReady=false;
        if (nextState.equals(END_PROBLEM))
            outputReady=true;
        if (nextState.equals(BEGIN_PROBLEM) ||
                (lastState == null && nextState.equals(END_PROBLEM))) {  // the first problem request looks like this
            probSeqNum++;
            numProbsSeen++;
            solveTime = 0;
            probMistakes = 0;
            probHintsSeen = 0;
            numAttempts = 0;
            isSolved = false;
            timeToFirstEvent = 0;
            firstEvent = null;
            givenAnswer = false;
            numHintsBeforeCorrect = 0;
            setProbSkills(conn, pid);
            setPrePostValues(conn, studId);

        }
        // asked for hint when previous state was not hint
        else if (nextState.equals(HINT_STATE) && !lastState.equals(HINT_STATE)) {
            totalHintsSeen++;
            probHintsSeen++;
            if (firstEvent == null) {
                firstEvent = HINT_STATE;
                timeToFirstEvent = probElapsed;
            }
            if (!givenAnswer && hintStep != null)
                givenAnswer = hintStep.toLowerCase().startsWith("choose");
            hintBeginTime = elapsedTime;
            int skillId = getHintSkill(conn, hintId);
            updateSkillHintCounters(skillId);
        }
        // another consecutive hint asked for
        else if (nextState.equals(HINT_STATE) && lastState.equals(HINT_STATE)) {
            totalHintsSeen++;
            probHintsSeen++;
            if (!givenAnswer && (hintStep != null))
                givenAnswer = hintStep.toLowerCase().startsWith("choose");

            totalHintTime += elapsedTime - hintBeginTime;
            hintBeginTime = elapsedTime;
            int skillId = getHintSkill(conn, hintId);
            updateSkillHintCounters(skillId);


        } else if (nextState.equals(ATTEMPT) && !isCorrect) {
            probMistakes++;
            totalMistakes++;
            if (firstEvent == null) {
                firstEvent = ATTEMPT;
                timeToFirstEvent = probElapsed;
            }
            numAttempts++;
        } else if (nextState.equals(ATTEMPT) && isCorrect) {
            solveTime = probElapsed;
            totalSolveTime += solveTime;
            isSolved = true;
            numHintsBeforeCorrect = probHintsSeen;
            if (firstEvent == null) {
                firstEvent = ATTEMPT;
                timeToFirstEvent = probElapsed;
            }
            numAttempts++;
        } else if (nextState.equals(END_PROBLEM) && lastState.equals(HINT_STATE)) {
            // if the problem isn't solved add all the time spent in the problem to the total solve time
            // We must do this or the average solve time for the student will not accurately reflect the fact
            // that they are failing to solve problems
            if (!isSolved) {
                solveTime=probElapsed;
                totalSolveTime += probElapsed;
            }

        } else if (nextState.equals(HINT_REJ) || nextState.equals(HINT_ACC)) {
            outputReady = false;
            return;
        }
        // new
        else if (nextState.equals(END_PROBLEM)) {


            // if the problem isn't solved add all the time spent in the problem to the total solve time
            // We must do this or the average solve time for the student will not accurately reflect the fact
            // that they are failing to solve problems
            if (!isSolved) {
                solveTime = probElapsed; //
                totalSolveTime += probElapsed;
            }

        }
        else {
            System.out.println("In an undefined transition from lastState=" + lastState + " to nextState=" + nextState + " userInput=" + userInput);
            lastState = nextState;
            outputReady = false;
            return;
        }
        lastState = nextState;
    }

    private void setPrePost(int version, double prepct, double gain, double postpct, int numPreGiven, int numPreCorrect, int numPreIncorrect, int numPostGiven, int numPostCorrect, int numPostIncorrect, String preProblemSet, String postProblemSet) {
        this.version = version;
        this.pre_pct = prepct;
        this.gain = gain;
        this.post_pct = postpct;
        this.numPreGiven = numPreGiven;
        this.numPreCorrect = numPreCorrect;
        this.numPreIncorrect = numPreIncorrect;

        this.numPostGiven = numPostGiven;
        this.numPostCorrect = numPostCorrect;
        this.numPostIncorrect = numPostIncorrect;
        this.preProblemSet = preProblemSet;
        this.postProblemSet = postProblemSet;

    }


    private int getHintSkill(Connection conn, int hintId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select skillId from hint where id=? ";
            ps = conn.prepareStatement(q);
            ps.setInt(1, hintId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("skillId");
            }
        } finally {
            ps.close();
            rs.close();
        }
        return -1;
    }

    private void setPrePostValues(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select pre_pct,gain,version from pencilpaperdata where studId=? ";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.pre_pct = rs.getDouble("pre_pct");
                this.gain = rs.getDouble("gain");
                this.version = rs.getInt("version");
            }
        } finally {
            rs.close();
            ps.close();
        }
    }

    /**
     * Set the skill booleans for the given prob Id
     *
     * @param conn
     * @param probId
     */
    private void setProbSkills(Connection conn, int probId) throws SQLException {
        clearProbSkills();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select skillId from hint where problemId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, probId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int skillId = rs.getInt(1);
                String absSkillName = skillMap.getHighLevelSkillName(skillId);
                int index = getSkillIndex(absSkillName);
                if (index != -1)
                    probSkills[index] = true;
            }
        } finally {
//            rs.close();
//            ps.close();
        }
    }

    private void clearProbSkills() {
        for (int i = 0; i < probSkills.length; i++) {
            probSkills[i] = false;
        }
    }

    private int getSkillIndex(String highSkillName) {
        for (int i = 0; i < skillNames.length; i++) {
            String skillName = skillNames[i];
            if (skillName.equals(highSkillName))
                return i;
        }
        return -1;
    }

    // from the given hintStep, determine the name of the semi-abstract skill.  Use
    // that name to find the index of the skill.  THen use that index to find the counter to increment
    private void updateSkillHintCounters(int hintId) {
        String highSkillName = skillMap.getHighLevelSkillName(hintId);
        int ix = getSkillIndex(highSkillName);
        if (ix != -1)
            skillHintCounters[ix]++;
    }

    private void setNextState(String action, String userInput) {
        if (action != null && action.equals(Actions.beginProblem))
            nextState = BEGIN_PROBLEM;
        else if (userInput != null && userInput.equals(StudentInputEnum.HELP.toString()))
            nextState = HINT_STATE;
        else if (action != null && action.equals(Actions.endProblem))
            nextState = END_PROBLEM;
        else if (action != null && action.equals(Actions.attempt))
            nextState = ATTEMPT;
        else if (action != null && (action.equals(Actions.hintAccepted)))
            nextState = HINT_ACC;
        else if (action != null && (action.equals(Actions.hintRejected)))
            nextState = HINT_REJ;
        else {
            System.out.println("don't know next state from action=" + action + " userInput=" + userInput + ".");
        }
    }


}
