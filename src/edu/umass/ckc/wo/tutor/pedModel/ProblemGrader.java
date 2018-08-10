package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemAnswer;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/30/14
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemGrader {
    private static Logger logger = Logger.getLogger(ProblemGrader.class);
    private SessionManager smgr;


    public ProblemGrader(SessionManager smgr) {
        this.smgr = smgr;
    }

    public ProblemScore gradePerformance(Problem lastProb) throws Exception {
        Connection conn = smgr.getConnection();
        StudentState state = smgr.getStudentState();
        double time = state.getTimeToSolve() / 1000.0;  // convert from ms to seconds
        int mistakes = state.getNumMistakesOnCurProblem();
        int hints = state.getNumHintsBeforeCorrect();
        boolean isCorrect = state.isProblemSolved();
        ProblemScore score = new ProblemScore();
        score.setProblemBroken(state.isCurProbBroken()); // DM added to detect problems reported as broken so score reflects it.
        score.setMistakes(mistakes);
        score.setHints(hints);
        score.setCorrect(isCorrect);
        score.setSolveTimeSecs(time);
        if (lastProb == null)
            throw new DeveloperException("Last problem is null in ProblemGrader.gradePerformance");
        float[] expected = DbProblem.getExpectedBehavior(conn, lastProb.getId());
        if (expected != null) {
            float expMistakes = expected[1];
            float expHints = expected[0];
            float expTime = expected[2];
            score.setAvgMistakes(expMistakes);
            score.setAvgHints(expHints);
            score.setAvgSolveTime(expTime); 
        }

        return score;
    }



    public boolean isAttemptCorrect (int probId, String userInput) throws SQLException {
        //Problem p = new DbProblem().getProblem(smgr.getConnection(),probId);
        Problem p = ProblemMgr.getProblem(probId);

        // Note:  Auth tool has two places to put answer for a short answer problem.  If only one answer is
        // put in, it can be fetched using p.getAnswer().   If there are multiple forms of the answer, then use
        // p.getAnswers().   Code below checks for which one was used and then uses it/them for grading the user input
        if (p.isShortAnswer() && !p.isParametrized()) {
            List<ProblemAnswer> possibleAnswers = null;
            if (p.getAnswers().size()== 0) {
                possibleAnswers = new ArrayList<ProblemAnswer>();
                possibleAnswers.add(new ProblemAnswer(p.getAnswer(),probId));
            }
            else possibleAnswers = p.getAnswers();
            return findAnswerMatch(possibleAnswers,userInput);
        }
        else if (p != null) {
            if (p.isParametrized()) {
                if (p.isMultiChoice()) {
                    return smgr.getStudentState().getProblemAnswer().equalsIgnoreCase(userInput.trim());
                } else if(p.isMultiSelect()) {
                    return isMultiSelectAnswerCorrect(smgr.getStudentState().getProblemAnswer(), userInput);
                } else {
                    // Get the list and check if one element is equal to student input.  This list comes from the StudentState
                    // because it depends on the bindings selected for this problem and this student
                    List<String> possibleInputs = smgr.getStudentState().getPossibleShortAnswers();
                    List<ProblemAnswer> correctAnswers = new ArrayList<ProblemAnswer>();
                    // turn the String coming out of the student state into simple ProblemAnswer objects so we can use the grade method
                    for (String a: possibleInputs) {
                        correctAnswers.add(new ProblemAnswer(a.replaceAll("\\s+",""),probId));
                    }
                    return findAnswerMatch(correctAnswers,userInput);
                }
            }
            if(p.isMultiSelect()) {
                return isMultiSelectAnswerCorrect(p.getAnswer(), userInput);
            }
            return p.getAnswer().equalsIgnoreCase(userInput.replaceAll("\\s+",""));
        }
        return false;
    }

    private boolean isMultiSelectAnswerCorrect(String answer, String select) {
        char[] answerLetters = answer.toLowerCase().toCharArray();
        char[] selectLetters = select.trim().toLowerCase().toCharArray();
        Arrays.sort(answerLetters);
        Arrays.sort(selectLetters);
        return Arrays.equals(answerLetters, selectLetters);
    }

    private boolean findAnswerMatch (List<ProblemAnswer> possible, String studentInput) {
        for (ProblemAnswer a: possible) {
            if (a.grade(studentInput))
                return true;
        }
        return false;
    }



}
