package edu.umass.ckc.wo.exc;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 16, 2004
 * Time: 3:51:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeveloperException extends Exception {

    public DeveloperException(String message) {
        super("Developer failure: " + message);
    }


}