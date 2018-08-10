package edu.umass.ckc.wo.woreports;

public class ReportProblem {

    int id;
    String name;

    //These variables will be averages for each problem, across a group of students
    double avg_timeToAttempt;
    double avg_timeToSolve;
    double avg_timeToHint;
    double avg_numHints;
    double avg_skipped;
    double avg_incAttempts;
    double avg_solvedFirstAtt;
    double avg_timeToFirstAction;

    private double timesSeen;


    public ReportProblem(int i) {
        id = i;
        avg_timeToAttempt = 0.0;
        avg_timeToSolve = 0.0;
        avg_timeToHint = 0.0;
        avg_numHints = 0.0;
        name = " ";
        avg_skipped = 0.0;
        avg_incAttempts = 0.0;
        avg_solvedFirstAtt = 0.0;
        avg_timeToFirstAction = 0;
    }

    public ReportProblem(String s) {
        id = -1;
        avg_timeToAttempt = 0.0;
        avg_timeToSolve = 0.0;
        avg_timeToHint = 0.0;
        avg_numHints = 0.0;
        name = s;
        avg_skipped = 0.0;
        avg_incAttempts = 0.0;
        avg_solvedFirstAtt = 0.0;
        avg_timeToFirstAction = 0;
    }

    public void seen(int att, int solve, int hint, int nhint) {
        timesSeen++;

        // Make a new average including this new instance, for each of the variables

        avg_timeToAttempt = ((avg_timeToAttempt * (timesSeen - 1)) + att) / timesSeen;
        avg_timeToSolve = ((avg_timeToSolve * (timesSeen - 1)) + solve) / timesSeen;
        avg_timeToHint = ((avg_timeToHint * (timesSeen - 1)) + hint) / timesSeen;
        avg_numHints = ((avg_numHints * (timesSeen - 1)) + nhint) / timesSeen;

        int timeToFirstAction = att;

        if (nhint > 0 && hint < att)
            timeToFirstAction = hint;

        avg_timeToFirstAction = ((avg_timeToFirstAction * (timesSeen - 1)) + timeToFirstAction) / timesSeen;
    }

    public void seen(int timeatt, int timetosolve, int timehint, int nhint, int num_attempts) {
        seen(timeatt, timetosolve, timehint, nhint);

        if (timetosolve > 0)
            avg_skipped = ((avg_skipped * (timesSeen - 1)) + 0) / timesSeen;
        else
            avg_skipped = ((avg_skipped * (timesSeen - 1)) + 1) / timesSeen;

        if (num_attempts > 1 && timetosolve > 0) //problem has been avg_solved and student entered at least one incorrect
            avg_incAttempts = ((avg_incAttempts * (timesSeen - 1)) + (num_attempts - 1)) / timesSeen;

        else if (num_attempts > 0 && timetosolve == 0)  // problem was avg_skipped
            avg_incAttempts = ((avg_incAttempts * (timesSeen - 1)) + num_attempts) / timesSeen;

        if (timetosolve > 0 && num_attempts == 1 && nhint == 0)
            avg_solvedFirstAtt = ((avg_solvedFirstAtt * (timesSeen - 1)) + 1) / timesSeen;
        else
            //Student did not answer correctly after the first attempt (either used hint, or saw incorrect answer, or avg_skipped)
            avg_solvedFirstAtt = ((avg_solvedFirstAtt * (timesSeen - 1)) + 0) / timesSeen;

        int timeToFirstAction = timeatt;
        if (nhint > 0 && timehint < timeatt)
            timeToFirstAction = timehint;

        avg_timeToFirstAction = ((avg_timeToFirstAction * (timesSeen - 1)) + timeToFirstAction) / timesSeen;
    }

/* --Ivon 7/28/06   -- I don't think this method is ever used
  public void seen(int att, int solve, int hint, int nhint, int noHelp, int avg_skipped) {
  timesSeen++;
  avg_timeToAttempt = ((avg_timeToAttempt * (timesSeen-1)) + att)     / timesSeen;
  avg_timeToSolve   = ((avg_timeToSolve   * (timesSeen-1)) + solve)   / timesSeen;
  avg_timeToHint    = ((avg_timeToHint    * (timesSeen-1)) + hint)    / timesSeen;
  avg_numHints      = ((avg_numHints      * (timesSeen-1)) + nhint)   / timesSeen;
  avg_solvedNoHelp  = ((avg_solvedNoHelp  * (timesSeen-1)) + noHelp)  / timesSeen;
  avg_solvedNoHelp  = ((avg_solvedNoHelp  * (timesSeen-1)) + noHelp)  / timesSeen;
  }
 */

    public double getTimesSeen() {
        return timesSeen;
    }

    public double getAvgNumIncorrect() {
        return avg_incAttempts;
    }

    public double getAvgTimeToSolve() {
        return avg_timeToSolve;
    }

    public double getAvgTimeToHint() {
        return avg_timeToHint;
    }

    public double getAvgTimeToAttempt() {
        return avg_timeToAttempt;
    }

    public double getAvgNumHints() {
        return avg_numHints;
    }

    public double getAvgTimeToAction() {
        return this.avg_timeToFirstAction;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAvgSkipped() {
        return avg_skipped;
    }

    public double getAvgSolvedFirstAtt() {
        return avg_solvedFirstAtt;
    }

}
