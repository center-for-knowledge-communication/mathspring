package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminSubmitClassFormEvent;
import ckc.servlet.servbase.View;

public class CreateClassPage implements View {

    private String msg = "";
    private AdminSubmitClassFormEvent e;


    public CreateClassPage() {
        this("",null);
    }

    public CreateClassPage(String msg) {
        this(msg,null);
    }


    public CreateClassPage(String msg, AdminSubmitClassFormEvent e) {

        this.msg = msg;
        this.e = e;

    }

    public String getView() throws Exception {
        return
                "<html>\n" +
                "<head>\n" +
                "<title>UMass MCAS Tutor Class creation</title>\n" +
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
                "<style type=\"text/css\">\n" +
                "<!--\n" +
                " <style type=\"text/css\">\n" +
                "a:active {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n" +
                "a:hover {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFF00; text-decoration: underline}\n" +
                "a:link {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n" +
                "a:visited {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n" +
                "-->\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<body bgcolor=\"#FFFFFF\" text=\"#000000\">\n" +
                "<img src=\"images/mcasbg.jpg\" >\n" +
                "<div id=\"Layer1\" style=\"position:absolute; width:365px; height:375px; z-index:1; left: 544px; top: 345px\"> \n" +
                "  <p align=\"center\" class=\"a2\"><font color=\"#FFFFFF\"><b><font face=\"Arial, Helvetica, sans-serif\">Create \n" +
                "    a class</font></b></font></p>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action=" + AdminActions.CREATE_CLASS2 + "\">\n" +
                "    <table width=\"334\" border=\"0\" height=\"98\">\n" +
                "      <tr> \n" +
                "        <td width=\"102\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* Class \n" +
                "          Name:</font></td>\n" +
                "        <td width=\"222\"> \n" +
                "          <input type=\"text\" name=\"className\" size=\"30\" value=\"" +
                ((e == null) ? "" : e.getClassName())
                + "\">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"102\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* Town:</font></td>\n" +
                "        <td width=\"222\"> \n" +
                "          <input type=\"text\" name=\"town\" size=\"30\" value=\"" +
                ((e == null) ? "" : e.getTown()) +
                "\">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"102\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* School:</font></td>\n" +
                "        <td width=\"222\"> \n" +
                "          <input type=\"text\" name=\"school\" size=\"20\" value=\"" +
                ((e == null) ? "" : e.getSchool()) +
                "\">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"102\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* Year:</font></td>\n" +
                "        <td width=\"222\"> \n" +
                "          <input type=\"text\" name=\"year\" size=\"20\" value=\"" +
                ((e == null) ? "" : e.getSchoolYear()) +
                "\">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"102\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* Section:</font></td>\n" +
                "        <td width=\"222\"> \n" +
                "          <input type=\"text\" name=\"section\" size=\"20\" value=\"" +
                ((e == null) ? "" : e.getSection()) +
                "\">\n" +
                "        </td>\n" +
                "      </tr>\n" +

                "    </table>\n" +
                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n" +
                "      <input type=\"submit\" name=\"Submit\" value=\"Submit\">\n" +

                "  </form>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action=" + AdminActions.CHOOSE_ACTIVITY + "\">\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" name=\"activitySelect\" value=\"Back to Activity Selection\">\n" +
                "</form></p>" +
                "    <font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">* required field</p></font>\n" +
                "<font color=\"#FFFF00\" face=\"Arial, Helvetica, sans-serif\">" + msg + "</font>\n" +
                "</div> \n" +
                "</body>\n" +
                "</html>\n" +
                "";
    }


}
