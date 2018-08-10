package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.settings.UnitTestSettings;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.TestSessionManager;
import edu.umass.ckc.wo.tutor.pedModel.TopicSelectorImpl;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 7/30/14
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Test()
public class TestCCContentMgr {

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
        m.loadContent(conn);
        Assert.assertTrue(m.getClusters().size() == 147, "Correct Number of clusters");
        Assert.assertEquals(m.getStandards().size(),509, "Correct Number of standards that have legal clusters");
    }

}
