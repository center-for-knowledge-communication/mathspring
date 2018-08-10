package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * A minimal LessonState that should get much more of its variables from TopicState.   In the future every student should have a LessonState but only
 * those who use a Topic based strategy should have a TopicState.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonState extends State {


    private static final String CUR_PROBLEM = "st.curProblem";
    private static final String LAST_PROBLEM = "st.lastProblem";
    private static final String NEXT_PROBLEM = "st.nextProblem";
    private static final String CUR_PROBLEM_MODE = "st.curProblemMode";
    private static final String NEXT_PROBLEM_MODE = "st.nextProblemMode";
    private static final String LAST_PROBLEM_MODE = "st.lastProblemMode";






    private static String[] ALL_PROPS = new String[] { CUR_PROBLEM , LAST_PROBLEM , NEXT_PROBLEM , CUR_PROBLEM_MODE , NEXT_PROBLEM_MODE,
            LAST_PROBLEM_MODE
           } ;
    private int curProblem;
    private int lastProblem;
    private int nextProblem;
    private String curProblemMode;
    private String nextProblemMode;
    private String lastProblemMode;
    private String curProbType;


    public LessonState(Connection conn) {
        this.conn = conn;
    }

    public static void clearState (Connection conn, int objid) throws SQLException {
        for (String prop : ALL_PROPS)
            clearProp(conn,objid,prop)  ;
    }


    public void extractProps(WoProps props) throws SQLException {
        Map m = props.getMap();
        this.curProblem = mapGetPropInt(m, CUR_PROBLEM, -1);
        this.lastProblem = mapGetPropInt(m, LAST_PROBLEM, -1);
        this.nextProblem = mapGetPropInt(m, NEXT_PROBLEM, -1);
        this.curProblemMode = mapGetPropString(m, CUR_PROBLEM_MODE, null);
        this.nextProblemMode = mapGetPropString(m, NEXT_PROBLEM_MODE, null);
        this.lastProblemMode = mapGetPropString(m, LAST_PROBLEM_MODE, null);
    }



    public void initializeState () throws SQLException {
        setCurProblem(-1);
        setLastProblem(-1);
        setNextProblem(-1);
        setCurProblemMode(null);
        setNextProblemMode(null);
        setLastProblemMode(null);

    }

    public void cleanupState () {

    }




    public void setCurProblem(int curProblem) throws SQLException {
        this.curProblem = curProblem;
        setProp(this.objid, CUR_PROBLEM, curProblem);
    }

    public int getCurProblem() {
        return curProblem;
    }

    public void setLastProblem (int lastProblem) throws SQLException {
        this.lastProblem = lastProblem;
        setProp(this.objid, LAST_PROBLEM, this.lastProblem);
    }

    public int getLastProblem () {
        return this.lastProblem;
    }

    public void setNextProblem (int nextProblem) throws SQLException {
        this.nextProblem = nextProblem;
        setProp(this.objid, NEXT_PROBLEM, this.nextProblem);
    }

    public int getNextProblem () {
        return this.nextProblem;
    }


    public void setCurProblemMode(String curProblemMode) throws SQLException {
        this.curProblemMode = curProblemMode;
        setProp(this.objid, CUR_PROBLEM_MODE, curProblemMode);
    }

    public String getCurProblemMode() {
        return curProblemMode;
    }






    public void setNextProblemMode(String nextProblemMode) throws SQLException {
        this.nextProblemMode = nextProblemMode;
        setProp(this.objid,NEXT_PROBLEM_MODE,nextProblemMode);
    }

    public String getNextProblemMode() {
        return nextProblemMode;
    }


    public void setLastProblemMode(String lastProblemMode) throws SQLException {
        this.lastProblemMode = lastProblemMode;
        setProp(this.objid, LAST_PROBLEM_MODE, lastProblemMode);
    }

    public String getLastProblemMode() {
        return lastProblemMode;
    }



}
