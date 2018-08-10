package edu.umass.ckc.wo.woserver;

import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;
import com.fasterxml.jackson.databind.deser.Deserializers;
import edu.umass.ckc.wo.db.DbSession;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.protocol.HTTP;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by marshall on 10/18/17.
 */
public class GritMouseServlet extends BaseServlet {


    public String getDataSource(ServletConfig servletConfig) {
        return servletConfig.getServletContext().getInitParameter("wodb.datasource");
    }

    private JSONObject lineToObj (String line) {
        System.out.println(line);
        return new JSONObject();
    }
//

//    public void doPost(HttpServletRequest request,
//                                 HttpServletResponse response) throws IOException {

    public boolean handleRequest(Connection conn, HttpServletRequest request,
                                 HttpServletResponse response, ServletParams params, StringBuffer output) throws Exception {

//        String sessId = request.getParameter("sessionId");
        int sessId = params.getInt("sessionId");
        // Dead sessions return an HTTP 400 to indicate an error.
        if (!DbSession.isActive(conn,sessId)) {
            response.setContentType("application/json");
            response.setStatus(400, "The Session is no longer active");
            return true;
        }
        String mouseData = params.getString("mouseData");
        JSONObject allPoints =  JSONObject.fromObject(mouseData);
        JSONArray listOfPoints = allPoints.getJSONArray("points");
        saveMouseData(conn,sessId,listOfPoints);
        JSONObject res = new JSONObject();
        response.setContentType("application/json");
        res.element("status",200);
        Writer w = response.getWriter();
        w.append(res.toString());
        response.setStatus(200);
        return true;
    }

    // return (x,y,time, 'action')
    private String getValueList (int sessionId, JSONObject dataPoint) {
        // by default no action is provided which means its a mouse MOVE action.  If action is provided, we assume its a click
        return "(" + sessionId + "," + dataPoint.get("x") + "," + dataPoint.get("y") + "," +
                dataPoint.get("t") + "," + (dataPoint.get("action") != null ? "'click'" : "'move'") + "),";
    }

    private void saveMouseData (Connection conn, int sessionId, JSONArray points) throws SQLException {
        StringBuilder sb = new StringBuilder();
        if (points.isEmpty())
            return;
        for (int i = 0; i < points.size(); i++) {
            String val = getValueList(sessionId, points.getJSONObject(i));
            sb.append(val);
        }
        // Need to remove the trailing comma
        sb.deleteCharAt(sb.length()-1);
        // sb is now the values string that goes on the end of a SQL insert
        PreparedStatement ps = null;
        try {
            String q = "insert into mousedata (sessionId, x,y,time,action) values " + sb.toString();
            ps = conn.prepareStatement(q);
            ps.executeUpdate();
        } finally {
            if (ps != null)
                ps.close();
        }
    }



}
