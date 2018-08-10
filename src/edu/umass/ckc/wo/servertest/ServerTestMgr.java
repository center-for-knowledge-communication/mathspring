package edu.umass.ckc.wo.servertest;

import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the regression testing of the server.  This class takes as input a baselineUserId to run a test from.  It
 * creates a test user and session, runs the server using EpisodicData table entries for the baselineUser as events.
 * <p/>
 * The tester can then use the reports facility to look at reports for a special class that contains testData and compare the
 * baseline user data to the testUser data.  If everything is working the reports for the two users should be
 * identical.   The tester can then remove evidence of the test user.
 * <p/>
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Mar 22, 2006
 * Time: 3:47:13 PM
 */
public class ServerTestMgr {
    private SessionManager smgr;
    private Connection conn;
    private int baselineId;
    private int classId;
    private int testUserId;
    private String testUserName;
    private int sessionId;
    protected String testGroupId = "100";
    private static String testUserPassword = "regressionTest";
    private String studentModelClassName ;

    private static String MOMS_NAME = "Regression_Test_Baseline";

    public ServerTestMgr() {
    }

    public void init() {
        try {
            DbUtil.loadDbDriver();
            this.conn = DbUtil.getAConnection();
            this.smgr = new SessionManager(conn);
            this.conn = conn;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



    public void setBaselineUser(int baselineUserId) {
        this.baselineId = baselineUserId;
        try {
            this.classId = smgr.getStudentClass(baselineUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // build a user in the test class
    public int createTestUser() throws Exception {
        String userName = getBaselineUserName(baselineId);
        int i = 0;
        String testUN = userName + "-RegressionTest";
        while ((testUserId = DbUser.getStudent(conn, testUN , testUserPassword)) != -1) {
            i++;
            testUN = userName + "-RegressionTest" + i;
        }
        testUserName = testUN;
        testUserId = DbUser.createUser(conn, userName, "RegressionTest", testUserName, testUserPassword, "", false, false);
        smgr.assignUserToGroup( testGroupId);
        return testUserId;
    }

    // remove all trace of the test user.
    public void removeTestUser() throws SQLException {
        DbUser.deleteStudent(conn, testUserId);

    }

    /**
     * log the test user in and return the sessionId
     *
     * @return
     */
    public int loginTestUser() throws Exception {
        smgr.serverTester_LoginStudent(testUserName, "", testUserPassword, System.currentTimeMillis(), null);
        sessionId = smgr.getSessionNum();
        smgr = new SessionManager(conn, sessionId);
        return sessionId;

    }

    public void driveServer() {
        EpisodicLogDriver dr = new EpisodicLogDriver();
        try {
            dr.init(conn, baselineId, sessionId);
            dr.interpretEpisodicLog();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private String getBaselineUserName(int baselineUserId) throws SQLException {
        String q = "select userName from student where id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, baselineUserId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }


    public String getBaselineOutput () throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class sm = Class.forName(this.studentModelClassName);
        Constructor cons = sm.getConstructor(new Class[] {Connection.class});
        StudentModel sm1 = (StudentModel) cons.newInstance(new Object[] {conn});
        sm1.load(conn, this.baselineId);
        StudentState st = new StudentState(conn, null);
//      st.loadFast(conn,this.baselineId);
        return sm1.toString() + "-----------------------------------\n" + st.toString();
//         AffectStudentModel bl = new AffectStudentModel(conn);
//         bl.load(conn, this.baselineId);
//        StudentState st = new StudentState(conn);
//        st.loadFast(conn,this.baselineId);
//        return bl.toString() + "-----------------------------------\n" + st.toString();

    }


    public String getRegressionTestOutput () throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class sm = Class.forName(this.studentModelClassName);
        Constructor cons = sm.getConstructor(new Class[] {Connection.class});
         StudentModel tu = (StudentModel) cons.newInstance(new Object[] {conn});
         tu.load(conn, this.testUserId);
        StudentState st = new StudentState(conn, null);
//        st.loadFast(conn,this.testUserId);

        return tu.toString() + "-----------------------------------\n" + st.toString();
    }



    public void dumpModels(int baselineId, int testId) {
        try {
            Class sm = Class.forName(this.studentModelClassName);
            Constructor cons = sm.getConstructor(new Class[] {Connection.class});
            StudentModel bl = (StudentModel) cons.newInstance(new Object[] {conn});
            bl.load(conn, baselineId);
            StudentModel test = (StudentModel) cons.newInstance(new Object[] {conn});
            test.load(conn, testId);
            Writer blw = getOutputStream(baselineId);
            Writer testw = getOutputStream(testId);
            blw.write(bl.toString());
            blw.close();
            testw.write(test.toString());
            testw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Writer getOutputStream(int id) throws IOException {
        return new FileWriter("U:\\wo\\regressionTestData\\SMI" + id + ".txt");
    }

    /**
     * We need to figure out the initial location of the baseline user (e.g. pretest or sat hut ) so that
     * we can place the test user in that same location to begin with.    Just scans the epidata to find
     * where the user stated
     *
     * @return
     */
    private String determineBaselineInitialState() throws SQLException {
        String q = "select activityName from episodicData2 where studId=? order by sessNum, elapsedTime";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, baselineId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String an = rs.getString(1);
            if (an.equals("pretestProblem"))
                return NavigationEvent.PREPOST_HUT;
            else if (an.equals("satProblem"))
                return NavigationEvent.SAT_HUT;
            else if (an.equals("posttestProblem"))
                return NavigationEvent.PREPOST_HUT;
        }
        return null;
    }

    /**
     * All regression test baseline users must have a momsName of 'Regression_Test_Baseline'
     *
     * @return a List of User objects
     * @throws SQLException
     */
    public List getBaseLineUsers() throws SQLException {
        String q = "select id,fname,lname,userName,momsName from student where momsName=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1,MOMS_NAME);
        ResultSet rs = ps.executeQuery();
        List results = new ArrayList();
        while (rs.next()) {
            int id = rs.getInt("id");
            String fn = rs.getString("fname");
            String ln = rs.getString("lname");
            String un = rs.getString("userName");
            String mn = rs.getString("momsName");
            results.add(new User(fn, ln, un, mn, "", id));
        }
        return results;
    }

    private  void setStudentModelClassName (int group) {
        if (group == 200)
           studentModelClassName = "edu.umass.ckc.wo.tutor.StudentModelMotivationalImpl";
        else
            studentModelClassName = "edu.umass.ckc.wo.tutor.AffectStudentModel";
    }

    public void runTest(User u, int group) throws Exception {
        testGroupId = Integer.toString(group);
        setStudentModelClassName(group);
        setBaselineUser(u.getId());
        int id = createTestUser();
        System.out.println("Running for regression studentId " + id);
        loginTestUser();
        smgr.getStudentState().setCurLocation(determineBaselineInitialState());
        driveServer();
    }

    public static void main(String[] args) {
        try {
            int baseLineId = 2302;
            ServerTestMgr m = new ServerTestMgr();

            m.init();
            m.setBaselineUser(baseLineId);
            int id = m.createTestUser();
            System.out.println("Running for regression studentId " + id);
            m.loginTestUser();
            m.smgr.getStudentState().setCurLocation(m.determineBaselineInitialState());
            m.driveServer();
            m.dumpModels(baseLineId, id);
            m.removeTestUser();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
