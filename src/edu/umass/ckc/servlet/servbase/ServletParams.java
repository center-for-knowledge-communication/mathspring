package edu.umass.ckc.servlet.servbase;



import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Jul 12, 2004
 * Time: 12:07:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServletParams {
    private HttpServletRequest request;

    public ServletParams(HttpServletRequest req) {
        request = req;
    }

    public String getString (String key) {
        return request.getParameter(key);
    }

    public Map getMap () {
        return request.getParameterMap();
    }

    public String getMandatoryString (String key) throws Exception {
        String v = request.getParameter(key);
        if (v == null)
            throw new UserException("Expecting argument: " + key);
        else return v;
    }

    public String getString (String key, String dflt) {
        String r = request.getParameter(key);
        return (r == null) ? dflt : r;
    }

    public int getInt (String key) {
        return Integer.parseInt(request.getParameter(key).trim());
    }

    public double getDouble (String key) {
        return Double.parseDouble(request.getParameter(key).trim());
    }

     public long getLong (String key) {
        return Long.parseLong(request.getParameter(key).trim());
    }

    public int getMandatoryInt (String key) throws Exception {
        String I = request.getParameter(key);
        if (I == null)
            throw new UserException("Expecting argument: " + key);
        else return Integer.parseInt(I.trim());
    }

    public long getLong (String key, long dflt) {
        String r = request.getParameter(key);
        return (r == null || r.trim().equals("")) ? dflt : Long.parseLong(r.trim());
    }

    public int getInt (String key, int dflt) {
        String r = request.getParameter(key);
        return (r == null || r.trim().equals("")) ? dflt : Integer.parseInt(r.trim());
    }

    public float getFloat(String key, float dflt) {
        String r = request.getParameter(key);
        return (r == null || r.trim().equals("")) ? dflt : Float.parseFloat(r.trim());
    }

    public double getDouble(String key, double dflt) {
        String r = request.getParameter(key);
        return (r == null || r.trim().equals("")) ? dflt : Double.parseDouble(r.trim());
    }


    public String[] getStrings (String key) {
        return request.getParameterValues(key);
    }

    public String toString () {
        Enumeration en = request.getParameterNames();
        StringBuffer sb = new StringBuffer();
        while (en.hasMoreElements()) {
            String k = (String) en.nextElement();
            sb.append("[" + k + "," + request.getParameter(k) + "]");
        }
        return sb.toString();
    }

    public boolean getBoolean(String key, boolean b) {
        String r = request.getParameter(key);
        if (r != null)
            return r.trim().equalsIgnoreCase("true");
        else return b;
    }

    public boolean getMandatoryBoolean(String key) throws UserException {
        String r = request.getParameter(key);
        if (r != null)
            return r.equalsIgnoreCase("true");
        else
            throw new UserException("Expecting argument: " + key);
    }

    public long getMandatoryLong(String key) throws UserException {
        String r = request.getParameter(key);
        if (r != null)
            return Long.parseLong(r.trim());
        else throw new UserException("Expecting argument: " + key);
    }
}
