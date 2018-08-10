package edu.umass.ckc.servlet.servbase;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 16, 2004
 * Time: 3:51:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserException extends Exception {

    public UserException (String message) {
        super("User exception: " + message);
    }


}
