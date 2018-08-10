package edu.umass.ckc.wo.strat;

import edu.umass.ckc.wo.db.DbStrategy;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by marshall on 6/14/17.
 */
public class StrategyMgr {


    /**
     * Get the students strategy from cache or from the db and load it in the cache.
     * @param conn
     * @param studId
     * @return
     * @throws SQLException
     */
    public static TutorStrategy getStrategy(Connection conn, int studId) throws Exception {

        int stratId = DbUser.getStudentStrategy(conn,studId);
        return getStrategyFromCache(conn,  stratId);
    }

    public static TutorStrategy getStrategyFromCache(Connection conn, int stratId) throws Exception {
        if (stratId == -1)
            return null;

        TutorStrategy strategy = StrategyCache.getInstance().getStrategy(stratId);

        if (Settings.useStrategyCaching && strategy == null) {
            strategy = DbStrategy.getStrategy(conn, stratId);
            StrategyCache.getInstance().putStrategy(stratId, strategy);
        }
        else if (strategy==null)
            strategy = DbStrategy.getStrategy(conn, stratId);
        return strategy;
    }
}
