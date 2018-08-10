package edu.umass.ckc.wo.ttmain.ttmodel;

import edu.umass.ckc.wo.content.Problem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nsmenon on 7/10/2017.
 */
public class PerProblemReportBean {

    private String problemId;
    private String problemName;
    private String imageURL;
    private String problemStandardAndDescription;
    private String problemURLWindow;

    private int noStudentsSeenProblem;
    private int percStudentsRepeated;
    private int percStudentsSkipped;
    private int percStudentsGaveUp;
    private int getPercStudentsSolvedEventually;
    public int getGetPercStudentsSolvedFirstTry;
    public int getGetPercStudentsSolvedSecondTry;
    private String mostIncorrectResponse;
    private String similarproblems;
    private String[] studentEffortsPerProblem;

    private Problem problem;

    public int lastStud = -1, attemptIx = 0, correctAttemptIx = 0;
    public boolean solved = false;
    public int nStudsSeen = 0, studEncounters = 0, nSkips = 0, nHints = 0;
    public long begTime = 0, probTime, firstActTime = 0;
    public boolean found = false;
    public int nA = 0, nB = 0, nC = 0, nD = 0;
    public boolean isExample=false;

    public String getProblemStandardAndDescription() {
        return problemStandardAndDescription;
    }

    public void setProblemStandardAndDescription(String problemStandardAndDescription) {
        this.problemStandardAndDescription = problemStandardAndDescription;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getNoStudentsSeenProblem() {
        return noStudentsSeenProblem;
    }

    public void setNoStudentsSeenProblem(int noStudentsSeenProblem) {
        this.noStudentsSeenProblem = noStudentsSeenProblem;
    }

    public int getPercStudentsRepeated() {
        return percStudentsRepeated;
    }

    public void setPercStudentsRepeated(int percStudentsRepeated) {
        this.percStudentsRepeated = percStudentsRepeated;
    }

    public int getPercStudentsSkipped() {
        return percStudentsSkipped;
    }

    public void setPercStudentsSkipped(int percStudentsSkipped) {
        this.percStudentsSkipped = percStudentsSkipped;
    }

    public int getPercStudentsGaveUp() {
        return percStudentsGaveUp;
    }

    public void setPercStudentsGaveUp(int percStudentsGaveUp) {
        this.percStudentsGaveUp = percStudentsGaveUp;
    }

    public int getGetPercStudentsSolvedEventually() {
        return getPercStudentsSolvedEventually;
    }

    public void setGetPercStudentsSolvedEventually(int getPercStudentsSolvedEventually) {
        this.getPercStudentsSolvedEventually = getPercStudentsSolvedEventually;
    }

    public int getGetGetPercStudentsSolvedFirstTry() {
        return getGetPercStudentsSolvedFirstTry;
    }

    public void setGetGetPercStudentsSolvedFirstTry(int getGetPercStudentsSolvedFirstTry) {
        this.getGetPercStudentsSolvedFirstTry = getGetPercStudentsSolvedFirstTry;
    }

    public int getGetGetPercStudentsSolvedSecondTry() {
        return getGetPercStudentsSolvedSecondTry;
    }

    public void setGetGetPercStudentsSolvedSecondTry(int getGetPercStudentsSolvedSecondTry) {
        this.getGetPercStudentsSolvedSecondTry = getGetPercStudentsSolvedSecondTry;
    }

    public String getMostIncorrectResponse() {
        return mostIncorrectResponse;
    }

    public void setMostIncorrectResponse(String mostIncorrectResponse) {
        this.mostIncorrectResponse = mostIncorrectResponse;
    }

    public String getProblemURLWindow() {
        return problemURLWindow;
    }

    public void setProblemURLWindow(String problemURLWindow) {
        this.problemURLWindow = problemURLWindow;
    }

    public String getSimilarproblems() {
        return similarproblems;
    }

    public void setSimilarproblems(String similarproblems) {
        this.similarproblems = similarproblems;
    }

    public String[] getStudentEffortsPerProblem() {
        return studentEffortsPerProblem;
    }

    public void setStudentEffortsPerProblem(String[] studentEffortsPerProblem) {
        this.studentEffortsPerProblem = studentEffortsPerProblem;
    }
}

