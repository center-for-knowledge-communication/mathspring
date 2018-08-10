package edu.umass.ckc.wo.tutor.pedModel;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/31/14
 * Time: 7:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemScore {
    private int mistakes;
    private int hints;
    private boolean correct;
    private double solveTimeSecs;
    private float avgMistakes;
    private float avgHints;
    private float avgSolveTime;

    private boolean problemBroken;  // DM ugly hack to fulfil request of Ivon about making broken problem result in a next Problem requests.

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public int getHints() {
        return hints;
    }

    public void setHints(int hints) {
        this.hints = hints;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public double getSolveTimeSecs() {
        return solveTimeSecs;
    }

    public void setSolveTimeSecs(double solveTimeSecs) {
        this.solveTimeSecs = solveTimeSecs;
    }

    public float getAvgMistakes() {
        return avgMistakes;
    }

    public void setAvgMistakes(float avgMistakes) {
        this.avgMistakes = avgMistakes;
    }

    public float getAvgHints() {
        return avgHints;
    }

    public void setAvgHints(float avgHints) {
        this.avgHints = avgHints;
    }

    public float getAvgSolveTime() {
        return avgSolveTime;
    }

    public void setAvgSolveTime(float avgSolveTime) {
        this.avgSolveTime = avgSolveTime;
    }

    public boolean isProblemBroken() {
        return problemBroken;
    }

    public void setProblemBroken(boolean problemBroken) {
        this.problemBroken = problemBroken;
    }
}
