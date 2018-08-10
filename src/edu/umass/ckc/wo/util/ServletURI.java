package edu.umass.ckc.wo.util;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/14/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServletURI {

    // Given a request return something like http://localhost:8082/woj2/WoAdmin
    public static String getURI (HttpServletRequest req) {
               StringBuffer uri = req.getRequestURL(); // like http://localhost:8082/woj2/WoAdmin
        String servPath = req.getServletPath();  // like /WoAdmin

        Pattern pattern = Pattern.compile("(^(.)*?)"+servPath);
        Matcher m = pattern.matcher(uri.toString());
        m.find();
        String path = m.group(1);
        return path + servPath;
    }
}
