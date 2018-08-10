package edu.umass.ckc.wo.emote;

import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 17, 2009
 * Time: 11:26:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmoteServerLink {
    private static Logger logger =   Logger.getLogger(EmoteServerLink.class);
    public static final String IP_ADDRESS = "ipAddress";
    public static final String STUDENT_ID = "studentId";
    public static final String TUTOR_LOGIN = "tutorLogin";
    public static final String TUTOR_LOGOUT = "tutorLogout";
    public static final String PROBLEM_DATA = "problemData";
    public static final String GET_AFFECT = "getAffect";

    public static final String PROB_BEGIN_TIME = "probBeginTime";
    public static final String PROB_ELAPSED_TIME = "probElapsedTime";
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String SOLVED_ON_FIRST_ATTEMPT = "solvedOnAttempt1";
    public static final String HINTS_SEEN = "hintsSeen";
    public static final String TIME_TO_FIRST_ATTEMPT = "timeToAttempt1";
    public static final String TIME_TO_SOLVE = "timeToSolve";
    public static final String TIME_IN_TUTOR = "timeInTutor";
    public static final String INCORRECT_ATTEMPTS = "incorrectAttempts";

    public static final String CLIENT_TIME = "timestamp";
    

            /**
     * Call the server with the given ?action=action and the param string
     * If there is an HTTP failure, a dialog will popup and the error will be logged and this will return null.
     *
     * @param action
     * @param params
     * @return whatever string the server returns or if theres an error, null
     * @throws org.jdom.JDOMException
     */

    public static String callServerGET(String action, String params) throws Exception {
        String actcmd = "?" + "action" + "=" + action;
        HttpURLConnection conn = null;
        InputStream stream = null;
        BufferedReader br = null;
        try {
//            String u1 = Settings.host + ":" + Settings.port + Settings.servlet + actcmd + params;
            String u1 = Settings.emoteServletURI + actcmd + params;
//            String u = formatSpaces(u1);
//            logger.info("GET url= " + u);
            URL url = new URL(u1);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            String errMsg = conn.getResponseMessage();
            if (responseCode != HttpURLConnection.HTTP_OK)
                logger.info("Communication with EmoteServlet at: " + u1 + "Error: " + errMsg + " Error code: " + responseCode);
                
            stream = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(stream));
            String res = null;
            while ((res = br.readLine()) != null) {
                sb.append(res + "\n");
            }
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            // eliminate this message because it clutters the log with a lot of backtraces
//            e.printStackTrace();
        } finally {
            conn.disconnect();
            br.close();
            stream.close();
        }
        return null;
    }

    /**
     *
     * @param studId
     * @param ipAddr
     * @param elapsedTime
     * @return  wristID
     */
    public static String login(int studId, String ipAddr, long elapsedTime) {
        try {
            String xml = callServerGET(TUTOR_LOGIN,
                    "&"+ STUDENT_ID + "=" +studId+
                    "&"+ IP_ADDRESS + "=" + ipAddr+
                    "&"+ CLIENT_TIME + "=" + elapsedTime );
            Element r = JDOMUtils.getRoot(xml);
            String wristId = r.getAttributeValue("val");
            return wristId;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "0"; // default wrist ID if things don't go well 
    }

    public static void logout(int studId, String ipAddr, long elapsedTime) {
        try {
            callServerGET(TUTOR_LOGOUT,
                    "&"+ STUDENT_ID + "=" +studId+
                    "&"+ IP_ADDRESS + "=" + ipAddr+
                    "&"+ CLIENT_TIME + "=" + elapsedTime );

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
