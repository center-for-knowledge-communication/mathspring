package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.settings.UnitTestSettings;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.TestSessionManager;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/13/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Test()
public class TestLessonMgr {

    private SessionManager smgr;
    private Connection conn;

    @BeforeClass
    public void setUp () {
        try {
            UnitTestSettings.DBHOST = "rose.cs.umass.edu";
            TestSessionManager tsmgr = new TestSessionManager();
            smgr = tsmgr.setUpTestSession();
            conn = smgr.getConnection();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @AfterClass
    public void tearDown () throws Exception {
        // get rid of anything added to the db

    }

    @Test()
    public void testLoadContent () throws Exception {
        CCContentMgr m = CCContentMgr.getInstance();
        LessonMgr lm = LessonMgr.getInstance();
        List<Lesson> lessons = lm.getClassLessons(conn,110);
        Assert.assertEquals(lessons.size(),2);
    }

}
