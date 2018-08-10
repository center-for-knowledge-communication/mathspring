package edu.umass.ckc.wo.enumx;

/**
 * these are the actions that can be initiated from the GUI
 */

public class ActionEnum extends StringEnum {

  private static final String li = "login";
  private static final String mrli = "mrlogin";
  private static final String lo = "logout";
  private static final String ga = "getActivity";
  private static final String advProbSolved = "adventureProblemSolved";
  private static final String re = "replay";
  private static final String getClasses = "getClasses";

  // these actions can be intiated from the Mental rotation application
  private static final String gv = "getVitals";
  private static final String sv = "setVitals";
  private static final String sr = "setResults";
  private static final String createClass = "createClass";
  private static final String getReport = "getReport";



  // these actions are used for the Transactions logged to the db
  public static final String beginProb = "beginProblem";
  public static final String endProb = "endProblem";
  public static final String hint = "hint";
  public static final String attempt = "attempt";
  /**
  The public instances.
  */
  public static final ActionEnum LOGIN = new ActionEnum(li);
  public static final ActionEnum MRLOGIN = new ActionEnum(mrli);
  public static final ActionEnum GET_ACTIVITY = new ActionEnum(ga);
  public static final ActionEnum ADV_PROB_SOLVED = new ActionEnum(advProbSolved);
  public static final ActionEnum LOGOUT = new ActionEnum(lo);
  public static final ActionEnum REPLAY = new ActionEnum(re);
  public static final ActionEnum GET_CLASSES = new ActionEnum(getClasses);

  public static final ActionEnum GET_VITALS = new ActionEnum(gv);
  public static final ActionEnum SET_VITALS = new ActionEnum(sv);
  public static final ActionEnum SET_RESULTS = new ActionEnum(sr);


/** Admin servlet actions */
  public static final ActionEnum CREATE_CLASS = new ActionEnum(createClass);
  public static final ActionEnum GET_REPORT = new ActionEnum(getReport);



  /**
  Private constructor.
  */
  private ActionEnum(String name) {
    super(name);
  }

  public static ActionEnum getInstance (String name) {
    if (name.equals(li))
      return LOGIN;
    else if (name.equals(mrli))
      return MRLOGIN;
    else if (name.equals(getClasses))
      return GET_CLASSES;
    else if (name.equals(lo))
      return LOGOUT;
    else if (name.equals(ga))
      return GET_ACTIVITY;
    else if (name.equals(re))
      return REPLAY;
    else if (name.equals(gv))
      return GET_VITALS;
    else if (name.equals(sv))
      return SET_VITALS;
    else if (name.equals(sr))
      return SET_RESULTS;
    else if (name.equals(advProbSolved))
      return ADV_PROB_SOLVED;
    // class admin servlet actions
    else if (name.equals(createClass))
      return CREATE_CLASS;
    else if (name.equals(getReport))
      return GET_REPORT;
    return null;
  }

}