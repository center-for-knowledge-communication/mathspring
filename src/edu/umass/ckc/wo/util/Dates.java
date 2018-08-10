package edu.umass.ckc.wo.util;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/3/16
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dates {

    public static int computeDayDiff(Date now, Date probBeginTime) {
        long msDif = now.getTime() - probBeginTime.getTime();
        long secs = msDif / 1000;
        long mins = secs / 60;
        long hrs = mins / 60;
        int days = (int) hrs / 24;
        return days;
    }
}
