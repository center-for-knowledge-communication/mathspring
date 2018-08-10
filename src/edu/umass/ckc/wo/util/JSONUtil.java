package edu.umass.ckc.wo.util;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 11/16/15
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONUtil {
    public static String changeSpecialChars(String json) {
        String s= json;
        if (s != null) {
            // Stuff coming from the CMS has a lot of garbage in the strings  that causes JSON parse errors.
            s = s.replace("\\n","");
            s = s.replace("\\r","");
            s= s.replace("\r","");
            s= s.replace("\n","");
            s= s.replace("\b","");
            s= s.replace("\t","");
            s= s.replace("\f","");
            s = s.replace("'", "");   // this is because we have to put this thing inside a String that begins/ends with '
            return s;
        }
        else return null;
    }
}
