package edu.umass.ckc.wo.tutor.probSel;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/7/16
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterleavedProblemSetParams {
    private int numTopicsToWait;  // how many topics must be explored before showing an interleaved problem set
    private int numTopicsToReview;  // how many topics to include in a problem set
    private int exploredProblemNum; // how many problems must be solved in a topic to be considered "fully explored" (for purposes of extracting review problems)
    private int exploredMinTime; // minimum time (minutes) to spend in a topic to be considered "fully explored"
    private int numProbsPerTopic; // the number of problems from each topic to place into the problem-set

    public InterleavedProblemSetParams() {
    }


    public int getNumTopicsToWait() {
        return numTopicsToWait;
    }

    public void setNumTopicsToWait(int numTopicsToWait) {
        this.numTopicsToWait = numTopicsToWait;
    }

    public int getExploredProblemNum() {
        return exploredProblemNum;
    }

    public void setExploredProblemNum(int exploredProblemNum) {
        this.exploredProblemNum = exploredProblemNum;
    }

    public int getExploredMinTime() {
        return exploredMinTime;
    }

    public void setExploredMinTime(int exploredMinTime) {
        this.exploredMinTime = exploredMinTime;
    }

    public int getNumProbsPerTopic() {
        return numProbsPerTopic;
    }

    public void setNumProbsPerTopic(int numProbsPerTopic) {
        this.numProbsPerTopic = numProbsPerTopic;
    }

    public int getNumTopicsToReview() {
        return numTopicsToReview;
    }

    public void setNumTopicsToReview(int numTopicsToReview) {
        this.numTopicsToReview = numTopicsToReview;
    }
}
