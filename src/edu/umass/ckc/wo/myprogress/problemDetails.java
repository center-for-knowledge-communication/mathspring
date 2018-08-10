package edu.umass.ckc.wo.myprogress;

import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dovanrai
 * Date: 9/10/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class problemDetails {

    int problemId;
    String problemName;
    String effort;
    int numAttemptsToSolve;
    boolean solved;
    int numHints;
    String ccstds;
    String ssURL;
    byte[] snapshot;

    public problemDetails(int id, String name, String effort_s, int num_AttemptsToSolve, int num_Hints, byte[] snapshot, boolean isSolved){

        problemId=id;
        problemName=name;
        effort=effort_s;
        numAttemptsToSolve=num_AttemptsToSolve;
        numHints=num_Hints;
        this.solved = isSolved;
        Problem p = ProblemMgr.getProblemByName(name);
        this.ssURL = p.getScreenshotUrl();
        this.snapshot = new org.apache.commons.codec.binary.Base64().encode(snapshot);

    }

    public problemDetails(int id, String name, String effort_s, int num_AttemptsToSolve,int num_Hints, byte[] snapshot,
                          List<CCStandard> standards, boolean isSolved){
        this(id,name,effort_s,num_AttemptsToSolve,num_Hints, snapshot, isSolved);
        StringBuilder sb = new StringBuilder();
        setCCStds(standards, sb);
    }

    private void setCCStds(List<CCStandard> standards, StringBuilder sb) {
        ccstds = "";
        if (standards == null)  {
            return;
        }
        else {
            for (CCStandard std: standards) {
                sb.append(std.getCode() + ", ");
            }
            if (standards.size() > 0)
                ccstds = sb.toString().substring(0,sb.length()-2);
        }
    }

    public boolean isSolved() {
        return solved;
    }

    public int getProblemId() {
        return problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public String getEffort() {
        return effort;
    }

    public int getNumAttemptsToSolve() {
        return numAttemptsToSolve;
    }

    public int getNumHints() {
        return numHints;
    }

    public String getCcstds () {
        return ccstds;
    }

    public String getScreenshotURL() {
        return ssURL;
    }

    public String getSnapshot() {
        if (snapshot != null)
            return new String(this.snapshot);
        return null;
    }
}
