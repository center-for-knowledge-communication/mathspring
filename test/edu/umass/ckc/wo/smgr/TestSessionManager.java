package edu.umass.ckc.wo.smgr;

import edu.umass.ckc.wo.admin.LessonMap;
import edu.umass.ckc.wo.admin.PedMap;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.settings.UnitTestSettings;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;

import java.io.FileInputStream;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 4/7/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSessionManager {

    /**
     * Builds a SessionManager that acts like a session for a guest user.
     * It depends on
     * @param dbHost
     * @return
     * @throws Exception
     */
    public SessionManager buildTestSessionManager (String dbHost, Connection conn) throws Exception {
        if (conn == null)
            conn = DbUtil.getAConnection(dbHost);
        int studId = UserRegistrationHandler.registerTemporaryUser(conn, edu.umass.ckc.wo.db.DbClass.GUEST_USER_CLASS_NAME, User.UserType.guest);
        SessionManager smgr = new SessionManager(conn).guestLoginSession(studId);
        return smgr;
    }

    public SessionManager setUpTestSession () throws Exception {
        Settings.useHybridTutor = true;
        FileInputStream str = new FileInputStream(UnitTestSettings.PROJECT_RESOURCES + "pedagogies.xml");
        FileInputStream str2 = new FileInputStream(UnitTestSettings.PROJECT_RESOURCES + "lessons.xml");
        Settings.pedagogyGroups = new PedMap(str);
        Settings.lessonMap = new LessonMap(str2);
        Connection conn = DbUtil.getAConnection(UnitTestSettings.DBHOST);
        if (!ProblemMgr.isLoaded())  {
            ProblemMgr.loadProbs(conn);
        }
        return buildTestSessionManager(null,conn);
    }

}
