package edu.umass.ckc.wo.handler;

import edu.umass.ckc.servlet.servbase.View;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Oct 21, 2008
 * Time: 10:06:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestDbHandler {
    public View handleEvent(ServletContext sc, Connection conn, HttpServletRequest request, HttpServletResponse response, final StringBuffer output) throws SQLException {
        String q = "select fname, lname, userName, password from teacher";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        String line = String.format("%20s %20s %15s %15s <br>", "Fname","Lname","userName","PW");
        output.append(line);
        while (rs.next()) {
            line = String.format("%20s %20s %15s %15s <br>", rs.getString(1), rs.getString(2),rs.getString(3),
                    rs.getString(4));
            output.append(line);
        }
        return new View () {
            public String getView() {
                return output.toString();
            }
        };
    }
}
