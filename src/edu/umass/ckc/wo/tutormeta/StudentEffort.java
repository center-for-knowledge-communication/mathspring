package edu.umass.ckc.wo.tutormeta;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 7/16/14
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentEffort {
    private String eff;

    public StudentEffort (String e1, String e2, String e3) {

        eff = e1+"-"+e2+"-"+e3;
    }

    public String getEffortDescription () {
        return eff;
    }

    public String toString () {
        return getEffortDescription();
    }
}
