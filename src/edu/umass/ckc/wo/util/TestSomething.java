package edu.umass.ckc.wo.util;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by marshall on 4/10/18.
 */
public class TestSomething {

    public static void main(String[] args) {
        try {
            TestSomething s = new TestSomething();
            Connection conn = DbUtil.getAConnection("rose.cs.umass.edu");
            ProblemMgr.loadProbs(conn);
            List<Integer> ids = ProblemMgr.getTopicProblemIds(73);
            s.removeTestProblems(ids);
            System.out.println(ids);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void removeTestProblems(List<Integer> topicProbs) throws SQLException {
        Iterator<Integer> itr = topicProbs.iterator();
        while (itr.hasNext()) {
            int pid = itr.next();
            if (ProblemMgr.isTestProb(pid))
                itr.remove();
        }
    }
}
