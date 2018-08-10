package edu.umass.ckc.wo.woreports;



import ckc.servlet.servbase.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChooseClassPage implements View {


  private String src = "<html><head><title>Wayang Outpost - Reports</title></head>"+
                      "<body><p><font size=4>Please Select Class:</font></p>";
  public final String foot = "</body></html>";

  public ChooseClassPage(Connection conn, String nextState, int repNum)  throws Exception {

    String SQL = "SELECT * "+
                 "FROM Class ORDER BY id desc";

    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    while (rs.next()) {
      int    id      = rs.getInt("id");
      String school  = rs.getString("school");
      String teacher = rs.getString("teacher");
      String name    = rs.getString("name");
      String year    = rs.getString("schoolYear");

      src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state="+ nextState + "&reportId="+repNum+"&classId="+id+"\">"+
          school + " - " + teacher + " - " + name + " - " + year + " " + "</a></p>";
    }

    src = src + foot;
  }

    public String getView() throws Exception {
        return src;
    }

}

























