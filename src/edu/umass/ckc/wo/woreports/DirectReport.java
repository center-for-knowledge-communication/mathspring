package edu.umass.ckc.wo.woreports;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 24, 2005
 * Time: 4:12:06 PM
 */
public class DirectReport extends Report {

    Connection conn;
    String dbPrefix = "jdbc:mysql";
    String dbHost = "cadmium.cs.umass.edu";
//    String dbHost = Settings.dbHost;
    String dbSource = "wayangoutpostdb";
    String dbDriver = "com.mysql.jdbc.Driver";
    String dbUser = "WayangServer";
    String dbPassword = "jupiter";


    public DirectReport () {
        loadDbDriver();
        try {
            conn=getConnection();
        } catch(Exception e) {
        }
    }

    private void loadDbDriver() {
        try {
            Driver d = (Driver) Class.forName(dbDriver).newInstance(); // MySql
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Connection getConnection() throws SQLException {
        String url;
        if (dbPrefix.equals("jdbc:mysql"))
            url = dbPrefix + "://" + dbHost + "/" + dbSource + "?user=" + dbUser + "&password=" + dbPassword; // preferred by MySQL
        else // JDBCODBCBridge
            url = dbPrefix + ":" + dbSource;
//        url = "jdbc:mysql://localhost:3306/test";
//        url = "jdbc:mysql://localhost/rashidb"; // this works
        try {
            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

    public View createReport(Connection conn, int id, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
        return this;
    }
}
