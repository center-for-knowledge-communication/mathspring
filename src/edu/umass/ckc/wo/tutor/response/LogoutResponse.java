package edu.umass.ckc.wo.tutor.response;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 12:06:38 PM
 */
public class LogoutResponse extends Response {

    public LogoutResponse () {
        buildJSON();
    }


     public String logEventName() {
         return "logout";
     }




}
