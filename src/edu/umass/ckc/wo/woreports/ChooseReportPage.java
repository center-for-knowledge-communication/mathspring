package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.servlet.servbase.View;


public class ChooseReportPage implements View {


  private String src = "<html><head><title>Wayang Outpost - Reports</title></head>"+
                      "<body><p><font size=4>Please Select Report:</font></p>";
  private  final String foot = "</body></html>";

  public ChooseReportPage() throws Exception {

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=1\">Report 1</a>" +
          "<br>Student Info per problem</br></p>";

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=2\">Report 2</a>" +
          "<br>Skill Info per Student</br></p>";

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=3\">Report 3</a>" +
          "<br>Class Summary - Overall</br></p>";

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=4\">Report 4</a>" +
      "<br>Class Summary - Per Problem</br></p>";

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=5\">Report 5</a>" +
      "<br>Class Summary - Per Student</br></p>";

    src = src + "<p> Class Summary - Per Student Per Problem <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=6\"> [HTML] </a>" +
        " <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=9\"> [CSV] </a>" +
        "<br>  </br> </p> " ;

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=7\">Report 7</a>" +
      "<br>Class Summary - Per Student Per Skill (it will take a couple of minutes)</br></p>";

    src = src + "<p> <a href=\"WoAdmin?action=AdminViewReport&state=chooseClass&reportId=8\">Report 8</a>" +
      "<br>Ivon's new report</br></p>";

    src = src + foot;
  }

    public String getView() throws Exception {
        return src;
    }

}

























