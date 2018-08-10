package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;

/**
 * Created by marshall on 9/25/17.
 */
public class AdminFlushStrategyCacheEvent extends ActionEvent {

    public AdminFlushStrategyCacheEvent (ServletParams p) throws Exception {
        super(p);
    }

}
