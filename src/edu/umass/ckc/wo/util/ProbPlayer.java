package edu.umass.ckc.wo.util;

import edu.umass.ckc.wo.tutor.Settings;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Oct 21, 2008
 * Time: 2:57:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProbPlayer {

    public static String getURLToProbPlayerOld(HttpServletRequest servletRequest) {
        StringBuffer url = servletRequest.getRequestURL();  // something like http://cadmium.cs.umass.edu/woj/WoAdmin
        String uri;
        // if the URL has a port then we are probably debugging and its someting like:
        // http://localhost:8082/woj/WoAdmin?...
        // so all we want is the part up to the end of localhost
        if (url.lastIndexOf(":") > 4)
//            uri = url.substring(0,url.lastIndexOf(":"));
        // when debugging we don't have a probplayer.swf on the local machine and must get it off the cadmium server.
              uri= "http://cadmium.cs.umass.edu";    // TODO this should be moved to the web.xml
        else
            // o/w
            // strip off woj/WoAdmin and add in  wayang2/flash/Problems/probplayer.swf
            // JSP script will add in query string of something like ?questionNum=078
            uri = url.substring(0, url.lastIndexOf("/woj"));
        uri += Settings.PROB_PLAYER_REQUEST_PATH;
        return uri;
    }

    public static String getURLToProbPlayer() {
        return Settings.probplayerPath;

    }

    public static String getURLToProbPreviewer() {
        return Settings.probPreviewerPath;

    }


}
