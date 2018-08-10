package edu.umass.ckc.wo.handler;

import java.util.Vector;
import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: mbaldwin
 * Date: Mar 8, 2004
 * Time: 2:21:27 PM
 */
public class SQLUpdate {
    /*
     * takes a vector of strings and concatenates them to form a string of parameters for an sql query
     *
     * @param strings - a vector of strings to concatenate together
     * @return the concatenated result string
     */
    public static String concatToParams(Vector strings) {
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            result += (String) strings.elementAt(i);
            if (i != strings.size() - 1)
                result += (", ");
        }
        return result;
    }

    /*
     * adds quotes to the beginning and end of the given string
     *
     * @param str - a string to add quotes to
     * @return the quoted string
     */

    public static String addQuotes(String str) {
        return ("'" + str + "'");
    }

    /*
     * execute an SQL update
     *
     * @param q - an sql update query string
     * @param conn - the sql connection
     * @return the result of the update
     */
    public static boolean insertEntry(String q, Connection conn) {
        try {
            Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            s.executeUpdate(q);
        }
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
        return true;
    }
}
