package edu.umass.ckc.wo.woreports;



import edu.umass.ckc.servlet.servbase.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChooseStudentPage implements View {


  public String src = "<html><head><title>Wayang Outpost - Reports</title></head>"+
                      "<body><p><font size=4>Please Select Student:</font></p>";
  public final String foot = "</body></html>";

  public ChooseStudentPage(Connection conn, String nextState, int repNum, int classId) throws Exception {


    String SQL = "SELECT Student.id, Student.fname, Student.lname " +
                 "FROM Student INNER JOIN Class ON Student.classId = Class.id " +
                 "WHERE Class.id=" + classId + " " +
                 "ORDER BY Student.lname, Student.fname, Student.id";

    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    while (rs.next()) {
      int    id      = rs.getInt("id");
      String fname   = rs.getString("fname");
      String lname   = rs.getString("lname");

      src = src +
            "<a href=\"WoAdmin?action=AdminViewReport&state="     + nextState +
            "&reportId="  + repNum +
            "&classId="   + classId +
            "&studId=" + id + "\">"+
            fname + " " + lname + "</a><br>";
    }

    src = src + foot;
  }

    public String getView() throws Exception {
        return src;
    }


}

























