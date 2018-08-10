package edu.umass.ckc.wo.woreports;

public class ReportSkill {

  int id;
  String name;
  double numHints;
  double solvedFirstAtt;
  double skipped;

  private int timesSeen;


  public ReportSkill(int i) {
    this.id = i;
    numHints = 0.0;
    solvedFirstAtt = 0.0;
    name = " ";
    skipped = 0.0;
  }


  public ReportSkill(String s) {
    this.id = -1;
    numHints = 0.0;
    solvedFirstAtt = 0.0;
    this.name = s;
    skipped = 0.0;
  }


  public void seen(int nhint, int firstAtt, int skipped) {
    this.timesSeen++;
    this.numHints      = ((this.numHints      * (timesSeen-1)) + nhint)   / timesSeen;
    this.solvedFirstAtt  = ((this.solvedFirstAtt  * (timesSeen-1)) + firstAtt)  / timesSeen;
    this.skipped       = ((this.skipped       * (timesSeen-1)) + skipped) / timesSeen;
  }


  public int getTimesSeen() {
    return timesSeen;
  }
  public double getNumHints() {
    return numHints;
  }
  public int getId() {
    return id;
  }
  public double getSolvedFirstAtt() {
    return solvedFirstAtt*100;
  }
  public String getName() {
    return name;
  }
  public double getSkipped() {
    return skipped;
  }

}
