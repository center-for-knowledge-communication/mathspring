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
public class WorkspaceState extends State {


    private static final String CUR_TOPIC = "wkspcst.curTopic";
    private static final String LAST_TOPIC = "wkspcst.lastTopic";

    // We need to keep a memory of the students full lesson state (lessonId, CU id, cluster id, cc std, prereq info
    // so that when they login again we know where they were in the lesson.

    private static final String CUR_LESSON = "wkspcst.curLesson";
    private static final String CUR_CU = "wkspcst.curCU";
    private static final String CUR_CLUSTER = "wkspcst.curCluster";
    private static final String CUR_STD = "wkspcst.curStd";
    private static final String CUR_PROB = "wkspcst.curProb";
    private static final String PREREQ_STD = "wkspcst.prereqStd";
    private static final String PREREQ_STD_STACK = "wkspcst.prereqStdStack";
    private static final String BROKEN_PROB_IDS = "wkspcst.brokenProbIds";


    private static final String POST_SURVEY_DONE = "wkspcst.postSurveyDone";

    private boolean postSurveyDone;
    private int curTopic;
    private int lastTopic;
    private int curLesson;
    private int curCU;
    private int curCluster;
    private String curStd;
    private int curProb;
    private String prereqStd;
    private List<String> prereqStdStack;
    private List<String> brokenProblemIds;

    public WorkspaceState(Connection conn) {
        this.conn = conn;
    }

    public static void clearState(Connection conn, int id) throws SQLException {

       clearProp(conn,id,CUR_LESSON);
       clearProp(conn,id,CUR_CU);
        clearProp(conn,id,CUR_CLUSTER);
        clearProp(conn,id,CUR_STD);
        clearProp(conn,id,PREREQ_STD);
        clearProp(conn,id,PREREQ_STD_STACK);
        clearProp(conn,id,BROKEN_PROB_IDS);
        clearProp(conn,id,POST_SURVEY_DONE);
        clearProp(conn,id,CUR_TOPIC);
        clearProp(conn,id,LAST_TOPIC);
    }

    public void extractProps(WoProps props) throws SQLException {
        Map m = props.getMap();
        this.curTopic = mapGetPropInt(m, CUR_TOPIC, -1);
        this.lastTopic = mapGetPropInt(m, LAST_TOPIC, -1);
        this.curLesson = mapGetPropInt(m,CUR_LESSON,-1);
        this.curCU = mapGetPropInt(m,CUR_CU,-1);
        this.curCluster = mapGetPropInt(m,CUR_CLUSTER,-1);
        this.curStd = mapGetPropString(m, CUR_STD, null);
        this.curProb = mapGetPropInt(m, CUR_PROB, -1);
        this.prereqStd = mapGetPropString(m, PREREQ_STD, null);
        this.prereqStdStack = mapGetPropList(m, PREREQ_STD_STACK);
        this.brokenProblemIds = mapGetPropList(m, BROKEN_PROB_IDS);
        this.postSurveyDone = mapGetPropBoolean(m, POST_SURVEY_DONE, false);
    }

    public int getCurTopic() {
        return curTopic;
    }

    public void setCurTopic(int pgroupID) throws SQLException {
        this.lastTopic = this.curTopic;
        this.curTopic = pgroupID;
        setProp(this.objid,LAST_TOPIC,this.lastTopic);
        setProp(this.objid, CUR_TOPIC,pgroupID);
    }

    public int getLastTopic() {
        return lastTopic;
    }

    // We save the curProb in both the workspace state and the lesson state.   It is in here so that on a new session
    // we can regain where the student was in the common core lesson structure.
    public int getCurProb() {
        return curProb;
    }

    public void setCurProb(int probId) throws SQLException {
        this.curProb = probId;
        setProp(this.objid, CUR_PROB,probId);
    }

    public int getCurLesson() {
        return curLesson;
    }

    public void setCurLesson(int curLesson) throws SQLException {
        this.curLesson = curLesson;
        setProp(objid,CUR_LESSON,curLesson);
    }

    public void addPrereqStdStack(String std) throws SQLException {
        this.prereqStdStack.add(0,std);
        addProp(objid,PREREQ_STD_STACK,std);
    }

    public List<String> getPrereqStdStack() {
        return prereqStdStack;
    }

    public void setPrereqStdStack(List<String> prereqStdStack) {
        this.prereqStdStack = prereqStdStack;
    }

    public void addBrokenProblemId (String pid) throws SQLException {
        this.brokenProblemIds.add(0,pid);
        addProp(objid,BROKEN_PROB_IDS,pid);
    }

    public List<String> getBrokenProblemIds () {
        return brokenProblemIds;
    }

    public String getPrereqStd() {
        return prereqStd;
    }

    public void setPrereqStd(String prereqStd) throws SQLException {
        this.prereqStd = prereqStd;
        setProp(objid,PREREQ_STD,prereqStd);
    }

    public String getCurStd() {
        return curStd;
    }

    public void setCurStd(String curStd) throws SQLException {
        this.curStd = curStd;
        setProp(objid,CUR_STD,curStd);
    }

    public int getCurCluster() {
        return curCluster;
    }

    public void setCurCluster(int curCluster) throws SQLException {
        this.curCluster = curCluster;
        setProp(this.objid,CUR_CLUSTER,curCluster);
    }

    public int getCurCU() {
        return curCU;
    }

    public void setCurCU(int curCU) throws SQLException {
        this.curCU = curCU;
        setProp(this.objid,CUR_CU,curCU);
    }

    public boolean isPostSurveyDone() {
        return postSurveyDone;
    }

    public void setPostSurveyDone(boolean postSurveyDone) throws SQLException {
        this.postSurveyDone = postSurveyDone;
        setProp(this.objid,POST_SURVEY_DONE,postSurveyDone);
    }
}
