package edu.umass.ckc.wo.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Sep 8, 2005
 * Time: 2:18:25 PM
 */
public class GetClassHandler {

    public String handleRequest (Connection conn) throws Exception {
        String q = "select id,teacher,school,schoolYear,name,town from Class order by id desc";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        StringBuffer sb = new StringBuffer(128);
        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> <classes>\n");
        while (rs.next()) {
            int id = rs.getInt(1);
            String teacher = rs.getString(2);
            String school = rs.getString(3);
            int yr = rs.getInt(4);
            String name = rs.getString(5);
            String town = rs.getString(6);
            sb.append("<class id=\"" + id + "\" teacher=\"" + teacher + "\" school=\"" + school +
                    "\" schoolYear=\"" + yr + "\" name=\"" + name + "\" town=\"" + town + "\"/>\n");

        }
        sb.append("</classes>");
        return sb.toString();
    }
}
