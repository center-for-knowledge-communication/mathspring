package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.beans.ClassConfig;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.SQLException;

/**
 * <p>Title: csdfw</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class NavigationHandler {
    private ServletContext sc;
    //  private DbServletSession inv;
    private Connection conn;
    private String testType;
    private String data;
    private SessionManager smgr;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String client;
    public static final String ACK = "ack";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String RANDOM = "random";
    public static final String TEST_LENGTH = "test_length";
    public static final String TEST_DEFINITION = "test_definition";
    public static final String TEST_TIME = "test_time";
    public static final String UNLIMITED_TIME = "0";
    public static final String PRE_TEST_TIME = "30";
    public static final String POST_TEST_TIME = "30";
    public static final String RANDOM_SAT_TEST_LENGTH = "20"; // # problems in the randomly created SAT test
    public static final String RANDOM_MFR_TEST_LENGTH = "20"; // # problems in the randomly created MFR test
    public static final String RANDOM_MR_TEST_LENGTH = "20"; // # problems in the randomly created MR test

    public NavigationHandler(ServletContext sc, SessionManager smgr, Connection conn, HttpServletRequest req, HttpServletResponse resp) {
        this.sc = sc;
        this.conn = conn;
        this.smgr = smgr;
        this.request = req;
        this.response = resp;
    }

    public View handleRequest(NavigationEvent e) throws Exception {
//        smgr.setClient(e.getClient()) ;
        if (e.getTo().equals(NavigationEvent.PROGRESS_WINDOW)) {
            new TutorLogger(smgr).logMPP(e);
            new MyProgressHandler(sc, smgr, conn, request, response).handleRequest(e);
            return null;
        }

        // if the activity includes data (ie the last activity is complete), get the data & test type
        // and then process the data appropriately
        if (e.isActivityComplete()) {
            testType = e.getTestType();
            data = e.getData();
            // process the results of the last activity
            // pre/post tests are no longer having batch results stored, but MFR and MR may be so I leave this call in.

            // the XML sent from adventures is sometimes bogus and is meaningless to us so we no longer process it; we lose it.
            if (e.getFrom().equals(NavigationEvent.A1) || e.getFrom().equals(NavigationEvent.A2) || e.getFrom().equals(NavigationEvent.A3))
                ;
            else
                processData(conn, testType, data, e.getElapsedTime(), e.getSessionId()); // testType is the last activity, data contains the results
        }
        // set the current activity - this is not used by the tutor but maybe someday it will
        smgr.getStudentState().setCurLocation(e.getTo());
        // The tutor needs to initialize its state when user first enters it.
        if (e.getTo().equalsIgnoreCase(NavigationEvent.SAT_HUT)) {
            smgr.getStudentState().setTutorEntryTime(System.currentTimeMillis());
            smgr.getStudentState().newSession(smgr);
        }
//    smgr.setCurInterfaceState(e.getSessionId(), e.getTo());
        // return the results for the current activity
        return getCurrentActivityView(e.getTo());
    }

    //   No longer storing batch results for pre/post tests
    private void processData(Connection conn, String testType, String data, long elapsedTime, int sessionId) throws Exception {
        if (testType.equals(NavigationEvent.PRE))
            ;
//      smgr.setPreTestCompleted(sessionId, true);
        else if (testType.equals(NavigationEvent.POST))
            ;

//      smgr.setPostTestCompleted(sessionId,true);
        parseData(data); // verify that its valid XML
        insertResultInDb(sessionId, testType, elapsedTime, data);
    }

    private void parseData(String xml) throws Exception {
        SAXBuilder builder = new SAXBuilder(false);

        Document doc = null;
        try {
            doc = builder.build(new StringReader(xml));
        } catch (JDOMException ex) {
            UserException newe = new UserException("XML Parser error: " + ex.getMessage() +
                    "\nXML cannot be parsed: " + xml);
            throw newe;
        }

    }

    private View getCurrentActivityView(String curAct) throws Exception {


        if (curAct.equals(NavigationEvent.MFR_HUT))
            return getMFRHutView();
        else if (curAct.equals(NavigationEvent.MR_HUT))
            return getMRHutView();
        else if (curAct.equals(NavigationEvent.SAT_HUT)) {
            smgr.getStudentState().setTutorEntryTime(System.currentTimeMillis());
            return getSatHutView();
        } else if (curAct.equals(NavigationEvent.PREPOST_HUT))
            return getPrePostHutView();
        else if (curAct.equals(NavigationEvent.VILLAGE))
            return getVillageView();

        else
            return getDefaultView();

    }


    private View getMFRHutView() {
        return new View() {
            public String getView() {
                StringBuffer res = new StringBuffer(64);
                res.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
                res.append("&" + NavigationEvent.TEST_TYPE + "=" + RANDOM + "\n");
                res.append("&" + TEST_LENGTH + "=" + RANDOM_MFR_TEST_LENGTH + "\n");
                res.append("&" + TEST_TIME + "=" + UNLIMITED_TIME + "\n");
                return res.toString();
            }
        };
    }

    private View getMRHutView() {
        return new View() {
            public String getView() {
                StringBuffer res = new StringBuffer(64);
                res.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
                res.append("&" + NavigationEvent.TEST_TYPE + "=" + RANDOM + "\n");
                res.append("&" + TEST_LENGTH + "=" + RANDOM_MR_TEST_LENGTH + "\n");
                res.append("&" + TEST_TIME + "=" + UNLIMITED_TIME + "\n");
                return res.toString();
            }
        };
    }

    private View getSatHutView() {
        return new View() {
            public String getView() {
                StringBuffer res = new StringBuffer(64);
                res.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
                res.append("&" + NavigationEvent.TEST_TYPE + "=" + RANDOM + "\n");
                res.append("&" + TEST_LENGTH + "=" + RANDOM_SAT_TEST_LENGTH + "\n");
                res.append("&" + TEST_TIME + "=" + UNLIMITED_TIME + "\n");
                return res.toString();
            }
        };
    }

    // pull problems from the db (using the form column).  For the pre-test use a random selection.
    // For the post use the test that wasn't used for the pre-test
    // for this student.  In either case if both have been used, then use a random selection.
    private View getPrePostHutView() throws Exception {
        return new PrePostTestHandler(sc, smgr, conn);
    }

//    private boolean pretestRequired() throws SQLException {
//        int givePretest = DbClass.getClassPrePostTest(smgr.getConnection(), smgr.getStudentClass(smgr.getStudentId()));
//        return givePretest==1;
//
//    }


    private View getVillageView() {
        return new View() {
            public String getView() throws SQLException {
                boolean pretestComplete = smgr.getStudentState().getPretestCompleted();
                boolean posttestComplete = smgr.getStudentState().getPosttestCompleted();
                StringBuffer res = new StringBuffer(64);
                res.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
                int poolId = DbClass.getPretestPool(conn, smgr.getClassID());
                ClassConfig cc = DbClass.getClassConfig(conn, smgr.getClassID());
                // pre is on only if its not already complete and its not set to be off
                boolean pre_On = !pretestComplete && poolId != 0 && cc.getPretest() != ClassConfig.OFF;
                // post is on only if 1. pretest is complete and post test is set to come on after pretest complete
                //                    2. posttest is turned on
                boolean post_On = (pretestComplete && poolId != 0 && (cc.getPosttest() == ClassConfig.ON_AFTER_PRETEST) ||
                        cc.getPosttest() == ClassConfig.ON);
                // fantasy on only if 1. pretest is done and fantasy set to come on when pretest is done
                //                     2.  posttest is done and fantasy set to come on when posttest is done
                //                      3.  fantasy set to on
                boolean fantasy_On = (pretestComplete && cc.getFantasy() == ClassConfig.ON_AFTER_PRETEST) ||
                        (posttestComplete && cc.getFantasy() == ClassConfig.ON_AFTER_POSTTEST) ||
                        cc.getFantasy() == ClassConfig.ON;
                boolean mfr_On = (pretestComplete && cc.getMfr() == ClassConfig.ON_AFTER_PRETEST) ||
                        (posttestComplete && cc.getMfr() == ClassConfig.ON_AFTER_POSTTEST) ||
                        cc.getMfr() == ClassConfig.ON;
                boolean spatial_On = (pretestComplete && cc.getSpatial() == ClassConfig.ON_AFTER_PRETEST) ||
                        (posttestComplete && cc.getSpatial() == ClassConfig.ON_AFTER_POSTTEST) ||
                        cc.getSpatial() == ClassConfig.ON;
                boolean tutoring_On = (pretestComplete && cc.getTutoring() == ClassConfig.ON_AFTER_PRETEST) ||
                        (posttestComplete && cc.getTutoring() == ClassConfig.ON_AFTER_POSTTEST) ||
                        // if the class is not in a pretest pool,  there is no pretest so we must tutor
                        (poolId == 0 && cc.getTutoring() == ClassConfig.ON_AFTER_PRETEST) ||
                        (poolId == 0 && cc.getTutoring() == ClassConfig.ON_AFTER_POSTTEST) ||
                        cc.getTutoring() == ClassConfig.ON;

                // dm 9/11 kind of a hack:  If no huts are on the system is worthless, so turn on the tutoring hut
                // This is actually serving a useful purpose in the situation where a class has only one hut activated: the
                // pretest.   When the pretest is complete all the huts will be off.   We'd like to have the tutoring
                // hut appear.   This line is simpler than adding logic above to make that happen and has
                // other beneficial effects so the hack is justified
                if (!pre_On && !post_On && !tutoring_On && !fantasy_On && !mfr_On && !spatial_On)
                    tutoring_On = true;
                res.append(String.format("&pretest=%d\n&posttest=%d\n&fantasy=%d\n&mfr=%d\n&spatial=%d\n&tutoring=%d",
                        (pre_On ? 1 : 0), (post_On ? 1 : 0), (fantasy_On ? 1 : 0), (mfr_On ? 1 : 0), (spatial_On ? 1 : 0), (tutoring_On ? 1 : 0)));
                return res.toString();
            }
        };
    }


    private View getDefaultView() {
        return new View() {
            public String getView() {
                StringBuffer res = new StringBuffer(64);
                res.append(ACK + "=" + TRUE);
                return res.toString();
            }
        };
    }

    // stick the results in the database.
    private void insertResultInDb(int sessNum, String testType, long time, String results) throws Exception {
        Connection conn = this.conn;
        String q = "insert into ActivityData (sessionId, testType, time, data) values (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, sessNum);
        ps.setString(2, testType);
        ps.setLong(3, time);
        if (results != null)
            ps.setString(4, results);
        else
            ps.setNull(4, Types.VARCHAR);
        ps.execute();
    }


}