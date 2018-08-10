package edu.umass.ckc.wo.exc;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/26/16
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCRuleException extends Exception {
    public LCRuleException(String s, Exception e) {
        super(s,e);
    }
}
