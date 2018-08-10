package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User names and passwords
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 12:57:04 PM
 */
public class ReportUserNamesAndPasswords extends Report {

    public ReportUserNamesAndPasswords() {
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String neck = "<table border=1 cellspacing=1 cellpadding=1>\n" +
            " <tr>\n" +
            "<th width=140>Student</th>\n" +
            "  <th>User Name</th>\n" +
            "  <th>PasswordAuthentication</th>\n" +
            " </tr>\n" +
            "<tbody id='data'>\n";
        StringBuffer rows = new StringBuffer();

        ClassInfo cl = DbClass.getClass(conn,classId);
        String className = getClassName(cl);

        this.src.append(generateHeader("User names and passwords for " + className));

        this.src.append("<h3>User Names and Passwords for " + className + "</h3>\n");
        addNavLinks(classId,cl.getTeachid());


        String SQL = "SELECT Student.userName, Student.password, Student.fname, Student.lname  " +
            "FROM Student " +
            "WHERE Student.classId = " + Integer.toString(classId) + " " +
            "ORDER BY Student.lname;";
        Statement studSt = conn.createStatement();
        ResultSet rs2 = studSt.executeQuery(SQL);
        while (rs2.next()) {
            String fname = rs2.getString("Student.fname");
            String lname = rs2.getString("Student.lname");
            String username = rs2.getString("Student.userName");
            String password = rs2.getString("Student.password");
            rows.append("<td> " + lname + ", " + fname + "</td>\n" +
                        "   <td>" + username + "</td>\n" +
                        "   <td>" + password + "</td>\n" ) ;
             rows.append("</tr>\n");
         }

         this.src.append(neck);
         this.src.append(rows);
         this.src.append(foot) ;
        return this;
    }
}
