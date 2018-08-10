package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;

public class AdminViewReportEvent extends AdminEvent {
  public static final String STATE = "state";

  public static final String CHOOSE_REPORT  = "chooseReport";
  public static final String CHOOSE_CLASS   = "chooseClass";
  public static final String CHOOSE_STUDENT = "chooseStudent";
  public static final String SHOW_REPORT     = "showReport";

  public static final String REPORT_ID    = "reportId";
  public static final String STUDENT_ID   = "studId";
  public static final String CLASS_ID   = "classId";
  public static final String PROBLEM_ID   = "probId";
  public static final String EXTRAPARAM = "extraParam" ;

  private String state;
  private int reportId;
  private int studId;
  private int probId;
  private String extraParam ;
  private int classId;  // only used when the state is chooseStudent

  public AdminViewReportEvent(ServletParams p) throws Exception {

    super(p);

    //every report event must have a state asscocitaed with it
    this.setState(p.getString(STATE));

    try {
      int _studId   = Integer.parseInt(p.getString(STUDENT_ID));
      this.setStudId(_studId);
    }
    catch (Exception ex) {
      this.setStudId(-1);
    }

     classId = p.getInt(CLASS_ID,-1);

    try {
      int _probId  = Integer.parseInt(p.getString(PROBLEM_ID));
      this.setProbId(_probId);
    }
    catch (Exception ex) {
      this.setProbId(-1);
    }


    try {
      int _reportId = Integer.parseInt(p.getString(REPORT_ID));
      this.setReportId(_reportId);
    }
    catch (Exception ex) {
      this.setReportId(-1);
    }

    try {
      String _extraParam = p.getString(EXTRAPARAM);
      this.setExtraParam(_extraParam);
    }
    catch (Exception ex) {
      this.setReportId(-1);
    }
  }

  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public int getReportId() {
    return reportId;
  }

  public String getExtraParam() {
      return extraParam ;
  }

  public void setReportId(int reportId) {
    this.reportId = reportId;
  }
  public void setExtraParam(String xtraParam) {
    this.extraParam = xtraParam;
  }
  public void setStudId(int studId) {
    this.studId = studId;
  }
  public int getStudId() {
    return studId;
  }

    public int getClassId() {
        return classId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }

    public int getProbId() {
        return probId;
    }

}
