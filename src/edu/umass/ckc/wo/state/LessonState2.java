package edu.umass.ckc.wo.state;

import edu.umass.ckc.wo.db.DbStateTableMgr;
import edu.umass.ckc.wo.util.State;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A minimal LessonState that should get much more of its variables from TopicState.   In the future every student should have a LessonState but only
 * those who use a Topic based strategy should have a TopicState.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonState2  extends State {


    private static final String CUR_PROBLEM = "curProblem";
    private static final String LAST_PROBLEM = "lastProblem";
    private static final String NEXT_PROBLEM = "nextProblem";
    private static final String CUR_PROBLEM_MODE = "curProblemMode";
    private static final String NEXT_PROBLEM_MODE = "nextProblemMode";
    private static final String LAST_PROBLEM_MODE = "lastProblemMode";
    private static final String CUR_PROBLEM_INDEX = "curProblemIndex";





    private static String TABLE_NAME = "studentlessonstate";
    private static String[] TABLE_COLS = new String[] { CUR_PROBLEM , LAST_PROBLEM , NEXT_PROBLEM , CUR_PROBLEM_MODE , NEXT_PROBLEM_MODE,
            LAST_PROBLEM_MODE, CUR_PROBLEM_INDEX
           } ;
    private int curProblem;
    private int lastProblem;
    private int nextProblem;
    private String curProblemMode;
    private String nextProblemMode;
    private String lastProblemMode;
    private String curProbType;
    private int curProblemIndex;
    private DbStateTableMgr dbWorker;




    public LessonState2(Connection conn) throws SQLException {
        this.conn = conn;
        dbWorker = new DbStateTableMgr(conn);

    }


    public void load( int studId) throws SQLException {
        this.objid = studId;

        try {
            dbWorker.load(studId,this, TABLE_NAME, TABLE_COLS, this.getClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IntrospectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void clearState (Connection conn, int studId) throws SQLException {
        DbStateTableMgr.clear(conn, TABLE_NAME, studId);

    }


//    public void extractProps(WoProps props) throws SQLException {
//        Map m = props.getMap();
//        this.curProblem = mapGetPropInt(m, CUR_PROBLEM, -1);
//        this.lastProblem = mapGetPropInt(m, LAST_PROBLEM, -1);
//        this.nextProblem = mapGetPropInt(m, NEXT_PROBLEM, -1);
//        this.curProblemMode = mapGetPropString(m, CUR_PROBLEM_MODE, null);
//        this.nextProblemMode = mapGetPropString(m, NEXT_PROBLEM_MODE, null);
//        this.lastProblemMode = mapGetPropString(m, LAST_PROBLEM_MODE, null);
//    }



    public void initializeState () throws SQLException {
        setCurProblem(-1);
        setLastProblem(-1);
        setNextProblem(-1);
        setCurProblemMode(null);
        setNextProblemMode(null);
        setLastProblemMode(null);

    }

    public boolean save () throws SQLException {
        dbWorker.save(this,this.objid, TABLE_NAME, TABLE_COLS, this.getClass());

        // because the topicmasterylevels are updated in the db by this classes access method, we don't need to write them when the
        // student model is saved.
        return true;
    }

    public void cleanupState () {

    }

    public void setCurProblem(int curProblem) throws SQLException {
        this.curProblem = curProblem;
    }

    public int getCurProblem() {
        return curProblem;
    }

    public void setLastProblem (int lastProblem) throws SQLException {
        this.lastProblem = lastProblem;
    }

    public int getLastProblem () {
        return this.lastProblem;
    }

    public void setNextProblem (int nextProblem) throws SQLException {
        this.nextProblem = nextProblem;
    }

    public int getNextProblem () {
        return this.nextProblem;
    }


    public void setCurProblemMode(String curProblemMode) throws SQLException {
        this.curProblemMode = curProblemMode;
    }

    public String getCurProblemMode() {
        return curProblemMode;
    }


    public void setNextProblemMode(String nextProblemMode) throws SQLException {
        this.nextProblemMode = nextProblemMode;
    }

    public String getNextProblemMode() {
        return nextProblemMode;
    }


    public void setLastProblemMode(String lastProblemMode) throws SQLException {
        this.lastProblemMode = lastProblemMode;
    }

    public String getLastProblemMode() {
        return lastProblemMode;
    }

    public int getCurProblemIndex() {
        return curProblemIndex;
    }

    public void setCurProblemIndex(int curProblemIndex) {
        this.curProblemIndex = curProblemIndex;
    }



}
