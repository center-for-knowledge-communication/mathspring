package edu.umass.ckc.wo.content;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 9/15/14
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemStats {
    private int pid;
    private int n=0;
    private double meanHints=0;
    private double meanAttempts=0;
    private double meanTimeSecs=0;
    private double varianceNumeratorHints =0;
    private double varianceNumeratorAttempts =0;
    private double varianceNumeratorTime =0;

    public ProblemStats (int pid) {
        this.pid = pid;
    }

    public ProblemStats(int pid, int n, double meanHints, double meanAttempts, double meanTimeSecs,
                        double varianceNumHints, double varianceNumAttempts, double varianceNumTimeSecs) {
        this.pid = pid;
        this.n = n;
        this.meanHints = meanHints;
        this.meanAttempts = meanAttempts;
        this.meanTimeSecs = meanTimeSecs;
        this.varianceNumeratorHints = varianceNumHints;
        this.varianceNumeratorAttempts = varianceNumAttempts;
        this.varianceNumeratorTime = varianceNumTimeSecs;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getMeanHints() {
        return meanHints;
    }

    public void setMeanHints(double meanHints) {
        this.meanHints = meanHints;
    }

    public double getMeanAttempts() {
        return meanAttempts;
    }

    public void setMeanAttempts(double meanAttempts) {
        this.meanAttempts = meanAttempts;
    }

    public double getMeanTimeSecs() {
        return meanTimeSecs;
    }

    public void setMeanTimeSecs(double meanTimeSecs) {
        this.meanTimeSecs = meanTimeSecs;
    }

    public double getVarianceNumeratorHints() {
        return varianceNumeratorHints;
    }

    public void setVarianceNumeratorHints(double varianceNumeratorHints) {
        this.varianceNumeratorHints = varianceNumeratorHints;
    }

    public double getVarianceNumeratorAttempts() {
        return varianceNumeratorAttempts;
    }

    public void setVarianceNumeratorAttempts(double varianceNumeratorAttempts) {
        this.varianceNumeratorAttempts = varianceNumeratorAttempts;
    }

    public double getVarianceNumeratorTime() {
        return varianceNumeratorTime;
    }

    public void setVarianceNumeratorTime(double varianceNumeratorTime) {
        this.varianceNumeratorTime = varianceNumeratorTime;
    }
}
