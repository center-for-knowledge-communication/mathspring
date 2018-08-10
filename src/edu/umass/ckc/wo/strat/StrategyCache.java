package edu.umass.ckc.wo.strat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marshall on 6/14/17.
 *
 * Tutoring Strategies are loaded on in-demand basis.  If a student is assigned to a strategy that isn't in the cache, we load it from
 * the database.   Commands for flushing the cache are also provided so that strategies can be modified and have the changes picked up by
 * live users.
 */
public class StrategyCache {

    private static StrategyCache instance=null;

    public static StrategyCache getInstance () {
        if (instance == null)
            instance = new StrategyCache();
        return instance;

    }

    private Map<Integer,TutorStrategy> cache = new HashMap<Integer,TutorStrategy>();

    /**
     * Given a strategy id return the TutorStrategy for it or null.
     * @param stratId
     * @return
     */
    public TutorStrategy getStrategy (int stratId) {
        TutorStrategy s = this.cache.get(stratId);
        return s;
    }

    public void putStrategy (int stratId, TutorStrategy strat) {
        this.cache.put(stratId,strat);
    }

    /**
     * Flush the cache.   This is connected to the AdminFlushStrategyCacheEvent so that we can dump the cache.   Typically this might be done
     * if someone were to change the definition of a strategy used by a class while that class is using the tutor.
     */
    public void flush () {
        cache = new HashMap<Integer,TutorStrategy>();
    }

    /**
     * Allows the removal of a single strategy from the cache.  This enables the msadmin tool to flush individual
     * strategies so that changes can be made to the live system.
     * @param strategyId
     */
    public void flushStrategy (int strategyId) {
        cache.remove(strategyId);
    }


}
