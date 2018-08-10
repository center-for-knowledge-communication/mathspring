package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by marshall on 9/25/17.
 */
public class AdminFlushSingleStrategyFromCacheEvent extends ActionEvent {
    private int strategyId;

    public AdminFlushSingleStrategyFromCacheEvent(ServletParams p) throws Exception {
        super(p);
        this.strategyId = p.getInt("strategyId");
    }

    public int getStrategyId () {
        return this.strategyId;
    }

}
