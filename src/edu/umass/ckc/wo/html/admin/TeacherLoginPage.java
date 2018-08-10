package edu.umass.ckc.wo.html.admin;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.servlet.servbase.View;

public class TeacherLoginPage implements View {

    private String msg;

    private String getSrc (String msg) {
        return
            "<html>\n" +
            "<head>\n" +
            "<title>Untitled Document</title>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" +
            "<script language=\"JavaScript\">\n" +
            "<!--\n" +
            "function MM_reloadPage(setServletInfo) {  //reloads the window if Nav4 resized\n" +
            "  if (setServletInfo==true) with (navigator) {if ((appName==\"Netscape\")&&(parseInt(appVersion)==4)) {\n" +
            "    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}\n" +
            "  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();\n" +
            "}\n" +
            "MM_reloadPage(true);\n" +
            "// -->\n" +
            "</script>\n" +
            "</head>\n" +
            "\n" +
            "<body bgcolor=\"#FFFFFF\" text=\"#000000\">\n" +
            "<img src=\"images/mcasbg.jpg\" >\n" +
            "<div id=\"Layer1\" style=\"position:absolute; width:350px; height:191px; z-index:1; left: 529px; top: 350px; visibility: visible\"> \n" +
            "  <p align=\"center\"><font color=\"#FFFFFF\" size=\"3\" face=\"Arial, Helvetica, sans-serif\"><b>Please \n" +
            "    login or create a new user</b></font></p>\n" +
            "  <form name=\"form1\" method=\"post\" action=\"" + "WoAdmin?action="+ AdminActions.LOGIN +"\">\n" +
            "    <table width=\"348\" border=\"0\" height=\"72\">\n" +
            "      <tr>\n" +
            "        <td width=\"93\"><font color=\"#FFFFFF\">User Name:</font></td>\n" +
            "        <td width=\"245\"> \n" +
            "          <input type=\"text\" name=\"userName\" size=\"30\">\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "      <tr>\n" +
            "        <td width=\"93\"><font color=\"#FFFFFF\">PasswordAuthentication:</font></td>\n" +
            "        <td width=\"245\">\n" +
            "          <input type=\"password\" name=\"password\" size=\"30\">\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "    </table>\n" +
            "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; \n" +
            "      &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; \n" +
            "      <input type=\"submit\" name=\"login\" value=\"Login\">\n" +
            "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n" +
            "      <input type=\"submit\" name=\"register\" value=\"Register\">\n" +
            "      <br>\n" +
            "  </form>\n" +
            "  <br> <font color=\"#FFFF00\">" + msg + "</font> \n" +
            "  <p align=\"center\">&nbsp;</p>\n" +
            "  <p>&nbsp;</p>\n" +
            "</div> \n" +
            "</body>\n" +
            "</html>\n" +
            "";
    }

    public TeacherLoginPage(String msg) {
        this.msg = msg;

    }

    public String getView() throws Exception {
        return getSrc(msg);
    }
}
