package edu.umass.ckc.wo.login.interv;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.beans.ClassConfig;
import edu.umass.ckc.wo.content.PrePostProblemDefn;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbPrePost;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 3:38 PM
 * This will show a posttest if there is one and the student needs to complete it.
 * See superclass doc.
 *
 */
public class Posttest extends Pretest {


    public Posttest(SessionManager smgr) throws SQLException {
        super(smgr);
        this.testType = DbPrePost.POSTTEST;
   }

    public void init (SessionManager smgr, PedagogicalModel pm) throws Exception {
        super.init(smgr,pm);
        // TODO may want to add some config options for start/end of test.
        if (configXML != null) {
            ;
        }
    }


    /**
     * Posttest is on when the classConfig for this student is turned on.
     * @return
     */
    @Override
    public boolean isTestOn () throws SQLException {
        ClassConfig cc = DbClass.getClassConfig(smgr.getConnection(),classId);
        return cc.isShowPostSurvey();
    }




}
