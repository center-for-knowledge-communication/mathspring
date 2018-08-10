package edu.umass.ckc.wo.tutor.studmod;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/26/16
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentStandardMastery {
    private int studId;
    private String std;
    private double val;
    private int numProbs;

    public StudentStandardMastery(int studId, String std, double val, int numProbs) {
        this.studId = studId;
        this.std = std;
        this.val = val;
        this.numProbs = numProbs;
    }

    public int getStudId() {
        return studId;
    }

    public String getStd() {
        return std;
    }

    public double getVal() {
        return val;
    }

    public int getNumProbs() {
        return numProbs;
    }
}
