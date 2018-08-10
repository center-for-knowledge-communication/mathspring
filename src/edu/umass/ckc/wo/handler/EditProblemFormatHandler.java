package edu.umass.ckc.wo.handler;

import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.wo.event.admin.AdminEditProblemFormatEvent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rezecib on 4/10/2017.
 */
public abstract class EditProblemFormatHandler {
    public static final String JSP = "/teacherTools/editProblemFormat.jsp";

    public static void handleEvent(AdminEditProblemFormatEvent e, ServletContext sc, Connection conn, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<String> templates = getSettingsFromDatabase(conn, "quickauthformattemplates", "problemFormat", false);
        List<String> fonts = getSettingsFromDatabase(conn, "quickauthformatfonts", "font", true);
        List<String> colors = getSettingsFromDatabase(conn, "quickauthformatcolors", "color", true);
        req.setAttribute("templates", templates);
        req.setAttribute("fonts", fonts);
        req.setAttribute("colors", colors);
        int problemId = e.getProbId();
        String problemFormat = "null"; //this looks weird but it will be output directly by jsp
        if(problemId != -1) problemFormat = getProblemFormat(conn, problemId);
        req.setAttribute("problemFormat", problemFormat);
        req.getRequestDispatcher(JSP).forward(req, resp);
    }

    private static List<String> getSettingsFromDatabase(Connection conn, String table, String column, boolean addQuotes) throws SQLException {
        List<String> settings = new ArrayList<String>();
        String query = "SELECT " + column + " FROM " + table + " WHERE enabled = TRUE;";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            while(rs.next()) {
                String option = rs.getString(1);
                if(option != null) {
                    if(addQuotes) option = "'" + option + "'";
                    settings.add(option);
                }
            }
        } finally {
            if(rs != null) rs.close();
            if(ps != null) ps.close();
        }
        return settings;
    }

    private static String getProblemFormat(Connection conn, int problemId) throws SQLException {
        String query = "SELECT problemFormat FROM problem WHERE id=" + problemId + ";";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            while(rs.next()) {
                String problemFormat = rs.getString(1);
                if(problemFormat == null) problemFormat = "null";
                return problemFormat;
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return "null";
    }
}
