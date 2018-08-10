package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePostState extends State {

//    stuff about pre/post tests.   This should go in a different table once the test is done.

    public static final String PRE_TEST_PROBLEM_SET = "st.pretestProblemSet";
    public static final String POST_TEST_PROBLEM_SET = "st.posttestProblemSet";
    public static final String PRE_TEST_PROBLEMS_GIVEN = "st.pretestProblemsGiven";
    public static final String POST_TEST_PROBLEMS_GIVEN = "st.posttestProblemsGiven";

    // If we get rid of pre/post huts,  these can go away.
    public static final String PRE_TEST_COMPLETED = "st.pretestCompleted";
    public static final String SAT_HUT_COMPLETED = "st.sathutCompleted";
    public static final String POST_TEST_COMPLETED = "st.posttestCompleted";
    // these are included here to allow a pre/post test to span sessions
    private static final String CUR_PRE_PROBLEM = "st.curPreProblem";
    private static final String CUR_POST_PROBLEM = "st.curPostProblem";

    private static String[] ALL_PROPS = new String[] {PRE_TEST_PROBLEM_SET, POST_TEST_PROBLEM_SET, PRE_TEST_PROBLEMS_GIVEN, POST_TEST_PROBLEMS_GIVEN ,PRE_TEST_COMPLETED,SAT_HUT_COMPLETED ,
            POST_TEST_COMPLETED, CUR_PRE_PROBLEM, CUR_POST_PROBLEM};

    private int pretestProblemSet = -1;
    private int posttestProblemSet = -1;
    private List<String> pretestProblemsGiven;
    private List<String> posttestProblemsGiven;
    private boolean pretestCompleted = false;
    private boolean posttestCompleted = false;
    private boolean satHutCompleted = false;

    private int curPreProblem = -1;
    private int curPostProblem = -1;


    public PrePostState(Connection conn) {
        this.conn = conn;
    }

    public static void clearState (Connection conn, int objid) throws SQLException {
        for (String prop : ALL_PROPS)
            clearProp(conn,objid,prop)  ;
    }

    public void clearPreData (Connection conn) throws SQLException {
        clearProp(conn,objid,PRE_TEST_PROBLEMS_GIVEN);
        clearProp(conn,objid,PRE_TEST_COMPLETED);
    }

    public void clearPostData (Connection conn) throws SQLException {
        clearProp(conn,objid,POST_TEST_PROBLEMS_GIVEN);
        clearProp(conn,objid,POST_TEST_COMPLETED);
    }

    public void extractProps(WoProps props) throws SQLException {
        Map m = props.getMap();
        this.pretestProblemSet = mapGetPropInt(m, PRE_TEST_PROBLEM_SET, -1);
        this.posttestProblemSet = mapGetPropInt(m, POST_TEST_PROBLEM_SET, -1);
        this.pretestCompleted = mapGetPropBoolean(m, PRE_TEST_COMPLETED, false);
        this.satHutCompleted = mapGetPropBoolean(m, SAT_HUT_COMPLETED, false);
        this.posttestCompleted = mapGetPropBoolean(m, POST_TEST_COMPLETED, false);
        pretestProblemsGiven = mapGetPropList(m, PRE_TEST_PROBLEMS_GIVEN);
        posttestProblemsGiven = mapGetPropList(m, POST_TEST_PROBLEMS_GIVEN);


        this.curPreProblem = mapGetPropInt(m, CUR_PRE_PROBLEM, -1);
        this.curPostProblem = mapGetPropInt(m, CUR_POST_PROBLEM, -1);


    }

    public int getCurPostProblem() {
        return curPostProblem;
    }

    public int getCurPreProblem() {
        return curPreProblem;
    }

    public void setCurPreProblem(int curPreProblem) throws SQLException {
        this.curPreProblem = curPreProblem;
        setProp(this.objid, CUR_PRE_PROBLEM, curPreProblem);
    }

    public void setCurPostProblem(int curPostProblem) throws SQLException {
        this.curPostProblem = curPostProblem;
        setProp(this.objid, CUR_POST_PROBLEM, curPostProblem);
    }

    public void setPretestCompleted(boolean b) throws SQLException {
        this.pretestCompleted = b;
        setProp(this.objid, PRE_TEST_COMPLETED, b);
    }

    public void setSatHutCompleted(boolean b) throws SQLException {
        this.satHutCompleted = b;
        setProp(this.objid, SAT_HUT_COMPLETED, b);
    }

    public void setPosttestCompleted(boolean b) throws SQLException {
        this.posttestCompleted = b;
        setProp(this.objid, POST_TEST_COMPLETED, b);
    }

    public void setPretestProblemSet(int problemSet) throws SQLException {
        this.pretestProblemSet = problemSet;
        setProp(this.objid, PRE_TEST_PROBLEM_SET, problemSet);
    }

    public void setPosttestProblemSet(int problemSet) throws SQLException {
        this.posttestProblemSet = problemSet;
        setProp(this.objid, POST_TEST_PROBLEM_SET, problemSet);
    }

    public void addPretestProblem(int probId) throws SQLException {
        this.pretestProblemsGiven.add(Integer.toString(probId));   // dm 2/08 fixed - was added Integer to end of list of Strings
        addProp(this.objid, PRE_TEST_PROBLEMS_GIVEN, probId);
    }

    public void addPosttestProblem(int probId) throws SQLException {
        this.posttestProblemsGiven.add(Integer.toString(probId)); // dm 2/08 fixed - was added Integer to end of list of Strings

        addProp(this.objid, POST_TEST_PROBLEMS_GIVEN, probId);
    }

    public boolean getPretestCompleted() {
        return pretestCompleted;
    }

    public boolean getSatHutCompleted() {
        return satHutCompleted;
    }

    public boolean getPosttestCompleted() {
        return posttestCompleted;
    }

    public int getPretestProblemSet() {
        return pretestProblemSet;
    }

    public int getPosttestProblemSet() {
        return posttestProblemSet;
    }

    public List getPretestProblemsGiven() {
        return pretestProblemsGiven;
    }

    public List getPosttestProblemsGiven() {
        return posttestProblemsGiven;
    }


    public boolean isPretestCompleted() {
        return pretestCompleted;
    }

    public boolean isPosttestCompleted() {
        return posttestCompleted;
    }

    public boolean isSatHutCompleted(){
        return false;
    }




    
}
