package edu.umass.ckc.wo.event;


import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.UserException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class NavigationEvent extends SessionEvent {
  // param names
  private static final String TO = "to";
  private static final String FROM = "from";
  private static final String ELAPSED_TIME = "elapsedTime";
  private static final String CLICK_TIME = "clickTime";
  private static final String PROB_ELAPSED_TIME = "probElapsedTime";
  private static final String PROBID = "probId";
  private static final String TOPICID = "topicId";
  private static final String DATA = "data"; // optional
  private static final String CLIENT = "clientVersion";
  public static final String TEST_TYPE = "test_type"; // optional
  public static final String LC = "learningCompanion"; // optional

  // these are the possible test types
  public static final String SAT = "sat";
  public static final String PRE = "pre";
  public static final String POST = "post";
  public static final String MR = "mental_rotation";
  public static final String MFR = "math_fact_retrieval";

  public static final String NONE = "none"; // used to handle situation where user is disallowed to enter a given hut
                                            // and needs to nav back to village
  public static final String[] validTestTypes = new String[] {SAT,
  PRE, POST, MR,MFR , NONE
    };


  // these are the possible states (settings for from and to)
  public static final String VILLAGE = "village";
  public static final String MFR_HUT = "math_fact_hut";
  public static final String MR_HUT = "mental_rotation_hut";
  public static final String PROGRESS_WINDOW = "my_progress";
  public static final String PREPOST_HUT = "test_hut"; // pre/post test hut
  public static final String SAT_HUT = "sat_hut";
  public static final String A1 = "adventure_1";
  public static final String A2 = "adventure_2";
  public static final String A3 = "adventure_3";
  public static final String ORAN_TREE = "orangutan_tree";
  public static final String INFO_HUT = "info_kiosk";

  public static final String[] validStates = new String[] {VILLAGE,
    MFR_HUT, MR_HUT, PREPOST_HUT,SAT_HUT,A1,A2,A3,ORAN_TREE,INFO_HUT,PROGRESS_WINDOW
    };

  //variables that report the state of the village to the interface

    public static final String VILLAGESTATE = "villagestate";
    public static final String PRETEST = "pretest";
    public static final String TEACH = "teach";
    public static final String POSTTEST = "posttest";
    public static final String DEFAULT = "default";

    public static final String EXTRASSTATE = "extrasstate";
    public static final String EXTRASON = "extrason";
    public static final String EXTRASOFF = "extrasoff";


  private String from;
  private String to;
  private String testType;
  private String data;
  private long elapsedTime;
  private long clickTime; // timestamp given by Javascript
  private long probElapsedTime;
  private String probId;
  private String topicId;
  private String clientVersion;
    private String lc;


    // only for debugging
    public  NavigationEvent (int sessId, String from, String to, String time, String client) {
        this.from =from;
        this.to=to;
        this.elapsedTime =Long.parseLong(time);
        this.sessionId = sessId;
        this.clientVersion = client ;
    }



  public NavigationEvent(ServletParams p) throws Exception {
    super(p);
    this.setFrom(p.getString(FROM));
    this.setTo(p.getString(TO));
    this.setElapsedTime(p.getString(ELAPSED_TIME,"0"));
    this.clickTime = p.getLong(CLICK_TIME,0);
    this.setProbElapsedTime(p.getString(PROB_ELAPSED_TIME,"0"));
    this.setProbId(p.getString(PROBID,null));
    this.setTopicId(p.getString(TOPICID,null));
    this.setClient(p.getString(CLIENT));
    if (!validateState(this.getTo(),'t')) {
      throw new UserException("Unknown 'to' state.  You must use one of: " +
                          getValidStates());
    }
    // the 2 fields below are optionally included
    // They correspond to the user having completed an activity in
    // the interface.  The result of that activity is sent in DATA and
    // the type of that activity is sent in TEST_TYPE
    this.setTestType(p.getString(TEST_TYPE,null));
//    if (this.getTestType() != null && this.getTestType().equals(NONE))
//        this.setTestType(null);
    if (this.getTestType() != null && !validateTestType(this.getTestType()))
      throw new UserException("Unknown 'test_type' value: [" + this.getTestType() + "].  You must use one of: " +
                          getValidTestTypes());
    this.setData(p.getString(DATA,null));
    // one cannot be null and the other have a value
    if ((this.getData() == null && this.getTestType() == null) ||
        (this.getData() != null && this.getTestType() != null))
      ;
    else throw new UserException("both test_type and data must be provided");
    this.setData(p.getString(DATA, null));
      this.lc = p.getString(LC,null);
  }

  public String getValidStates () {
    StringBuffer sb = new StringBuffer();
    for (int i=0;i<validStates.length;i++)
      sb.append(validStates[i] + " ");
    return sb.toString();
  }

  public String getValidTestTypes () {
    StringBuffer sb = new StringBuffer();
    for (int i=0;i<validTestTypes.length;i++)
      sb.append(validTestTypes[i] + " ");
    return sb.toString();
  }

  // return whether this event indicates the start of a new activity
  // this will be when the data param is not sent (ie data==null)
  public boolean isActivityStarting () {
    return this.getData() == null ;
  }

  // return whether this event indicates the completion of an activity
  // this will be when the data param is  sent (ie data!=null)
  public boolean isActivityComplete () {
    return !isActivityStarting();
  }

  // this will validate the given test type and if valid will correct the
  // member variable.  That way later dispatching on the mode will not have to
  // use equalsIgnoreCase
  private boolean validateTestType (String tt) {
    for (int i=0;i<validTestTypes.length;i++)
      if (validTestTypes[i].equalsIgnoreCase(tt)) {
        this.setTestType(validTestTypes[i]);
        return true;
      }
    return false;
  }

  // this will validate the given stateName and if valid will correct the
  // member variable according to the mode.  That way later dispatching on the mode will not have to
  // use equalsIgnoreCase
  private boolean validateState (String stateName, char mode) {
    for (int i=0;i<validStates.length;i++)
      if (validStates[i].equalsIgnoreCase(stateName)) {
        if (mode == 't') this.setTo(validStates[i]);
        else if (mode == 'f') this.setFrom(validStates[i]);
        return true;
      }
    return false;
  }

  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public void setTo(String to) {
    this.to = to;
  }
  public String getTo() {
    return to;
  }
  public void setTestType(String testType) {
    this.testType = testType;
  }
  public String getTestType() {
    return testType;
  }
  public void setData(String data) {
    this.data = data;
  }
  public String getData() {
    return data;
  }
  public void setElapsedTime(String elapsedTime) {
      if (elapsedTime != null)
        this.elapsedTime = Long.parseLong(elapsedTime);

  }
  public long getElapsedTime() {
    return elapsedTime;
  }    
  public String getClient() {
    return this.clientVersion ;
  }
  public void setClient(String client) {
      this.clientVersion = client ;
  }

  public long getClickTime() {
    return clickTime;
  }

  public long getProbElapsedTime() {
        return probElapsedTime;
    }

    public void setProbElapsedTime(String probElapsedTime) {
        if (probElapsedTime != null)
            this.probElapsedTime = Long.parseLong(probElapsedTime);
    }

    public String getProbId() {
        return probId;
    }

    public void setProbId(String probId) {
        this.probId = probId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getLc() {
        return lc;
    }
}
