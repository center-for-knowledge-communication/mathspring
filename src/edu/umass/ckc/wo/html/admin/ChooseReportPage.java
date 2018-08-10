package edu.umass.ckc.wo.html.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChooseReportPage {
  private Variables v_;

  public String dbString = null;
  public String src = "<html>"+
                      "<HEAD>"+
                      "<link href=\"/wo/CascadeMenu.css\" rel=\"stylesheet\">"+
                      "<script language=\"javascript\" src=\"/wo/CascadeMenu.js\">"+
                      "</script>"+
                      "</HEAD>"+
                      "<BODY OnLoad=\"InitMenu()\" Onclick=\"HideMenu(menuBar)\" ID=\"Bdy\" bgColor=aliceblue>"+
                      "<font color=\"#800080\"><b>Wayang Outpost Report Generator</b></font></p>" +
                      "<div Id=\"menuBar\" class=\"menuBar\" >" +
                      "<div Id=\"r1\" class=\"Bar\" menu=\"r1c\"><b>Report 1</b><br><small>Detail problem info per student</small></div>"+
                      "<div Id=\"r2\" class=\"Bar\" menu=\"r2c\"><b>Report 2</b><br><small>Skill info per student</small></div>"+
                      "<div Id=\"r3\" class=\"Bar\" menu=\"r3c\"><b>Report 3</b><br><small>Problem info per class</small></div>"+
                      "<div Id=\"r4\" class=\"Bar\" menu=\"r4c\"><b>Report 4</b><br><small>Detail problem info per class</small></div>"+
                      "<div Id=\"r5\" class=\"Bar\" menu=\"r5c\"><b>Report 5</b><br><small>Skill info per class</small></div>"+
                      "<div Id=\"r6\" class=\"Bar\" menu=\"r6c\"><b>Report 6</b><br><small>Problem info per student</small></div>"+
  "</div>";



  public ChooseReportPage(Variables v, Connection conn, String studSQL, String classSQL) throws Exception {
    this.v_ = v;
    dbString = v.getUri() + "?action=AdminViewReport&state=final" ; // "http://localhost:8082/wo/servlet/WoAdminServlet?action=getReport&state=final";
    Statement classSt1 = conn.createStatement();
    Statement classSt2 = conn.createStatement();

    int i;
    ResultSet rs = classSt1.executeQuery(classSQL);
    for (i =1; i<=6; i++)
    {
      rs.beforeFirst();

      if (i<=2) {
        String studStr = "";
        src = src + createDivBlock("r"+ Integer.toString(i) + "c");
        while (rs.next()) {
          String sch = rs.getString("school");
          String cls = rs.getString("name");
          int id = rs.getInt("id");
          String str = "r" + Integer.toString(i) + "c" + Integer.toString(id);
          src = src + createDivBlock(str, str + "s",sch + " " + cls);
          String newSQL = studSQL + " WHERE Class.id = " + Integer.toString(id);
          ResultSet studRS = classSt2.executeQuery(newSQL);
          studStr = studStr + createDivBlock("r"+ Integer.toString(i) + "c"+ Integer.toString(id) + "s");
          while (studRS.next()) {
            String s = "r" + Integer.toString(i) + "c" + Integer.toString(id) + "s" + Integer.toString(studRS.getInt("id"));
            String fname = studRS.getString("fname");
            if (!(fname == null))
              fname = fname.trim();
            String lname = studRS.getString("lname");
            if (!(lname == null))
              lname = lname.trim();

            studStr = studStr+ createDivBlock(s, "",
                this.dbString + "&reportName=" +
                Integer.toString(i) + "&classId="+ Integer.toString(id) +
                "&studId=" + Integer.toString(studRS.getInt("id")),
                fname + " " + lname);
          } //student while

          studStr=studStr + "</div>";
        }//class while
  //      System.out.println("Adding: " + studStr);
        src = src + "</div>" + studStr;

      } //if
      else {
        String studStr = "";
        src = src + createDivBlock("r"+ Integer.toString(i) + "c");
        while (rs.next()) {
          String sch = rs.getString("school");
          String cls = rs.getString("name");
          int id = rs.getInt("id");
          String str = "r" + Integer.toString(i) + "c" + Integer.toString(id);
          src = src + createDivBlock(str, "",this.dbString + "&reportName=" + Integer.toString(i) +
                                             "&classId="+ Integer.toString(id),sch + " " + cls);

        }//class while
        src = src + "</div>";
      } //else
    }//for


  }

  private String createDivBlock(String id, String menu, String text) {
    return "<div Id=\"" + id +"\" class=\"menuItem\" menu=\"" + menu +"\"><small>" + text +"</small></div>";
  }

  private String createDivBlock(String id, String menu, String url, String text) {
    String str = "<div Id=\"" + id +"\" class=\"menuItem\" cmd=\"" + url + "\"><small>" + text +"</small></div>";
//    System.out.println(str);
    return str;
  }

  private String createDivBlock(String id) {
    return "<div Id=\"" + id +"\" class=\"menu\">";
  }




}

























