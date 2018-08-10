package edu.umass.ckc.wo.state;

//import com.google.gson.Gson;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbStateTableMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.event.tutorhut.BeginProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/9/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemState extends State {
    ProblemState2 pojo;
    private DbStateTableMgr dbWorker;


    public ProblemState(Connection conn) throws SQLException {
        this.conn = conn;
        pojo = new ProblemState2();
        dbWorker = new DbStateTableMgr(conn);
    }

    public void load( int studId) throws SQLException {
        this.objid = studId;

        try {
            dbWorker.load(studId,pojo, ProblemState2.TABLE_NAME, ProblemState2.TABLE_COLS, pojo.getClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IntrospectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        pojo.setShortAnswerList();
    }

    /**
     * Persist this object to the db.  This is called during StudentModel.save.
     * @return
     * @throws SQLException
     */
    public boolean save () throws SQLException {
        dbWorker.save(pojo,this.objid, ProblemState2.TABLE_NAME, ProblemState2.TABLE_COLS, pojo.getClass());

        // because the topicmasterylevels are updated in the db by this classes access method, we don't need to write them when the
        // student model is saved.
        return true;
    }

    public static void clearState (Connection conn, int studId) throws SQLException {
        DbStateTableMgr.clear(conn, ProblemState2.TABLE_NAME, studId);
    }


    // This is called when a problem is put on-screen in Flash.
    public void beginProblem(SessionManager smgr, BeginProblemEvent e) throws SQLException {
        pojo.setStartTime(e.getElapsedTime());
    }

    public ProblemState2 getPojo () {
        return this.pojo;
    }

    public void initializeState () throws SQLException {
        pojo.initializeState();
    }

    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            WoProps woProps = new WoProps(conn);
            woProps.load(28436);   // aft89
            SessionManager smgr = new SessionManager(conn);
            StudentState studState = new StudentState(conn, smgr);
            studState.setObjid(28436);
            studState.extractProps(woProps);  // pull out student state props from all properties
            ExtendedStudentState extStudState = new ExtendedStudentState(smgr);
            ProblemState ps = studState.getProblemState();
            ProblemState2 pojo = ps.getPojo();
//            Gson gson = new Gson();
//            String json = gson.toJson(pojo);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
