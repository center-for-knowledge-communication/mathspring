package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.event.admin.AdminActions;
import ckc.servlet.servbase.View;

public class TeacherActivityPage implements View {
    private String id;
    private static final String src =
            "<html>\n" +
            "<head>\n" +
            "<title>Teacher Activities</title>\n" +
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
            "<img src=\"images/mcasbg.jpg\" width=\"957\" height=\"812\">\n" +
            "<div id=\"Layer1\" style=\"position:absolute; width:349px; height:333px; z-index:1; left: 544px; top: 345px\"> \n" +
            "  <p align=\"center\" class=\"a2\"><font color=\"#FFFFFF\">What would you like to do?<br>\n" +
            "  \n" +
            "    <br>\n" +
            "    <a href=\"WoAdmin?action=" + AdminActions.CREATE_CLASS1 + "\">Create a new class</a></p>\n" +
                    // Attempt to use PHP for processing some of these pages.   Worked but couldn't easily get
                    // Tomcat to process PHP pages which made system config too difficult.
                    // TODO eliminate the hardwiring.  Pass the ?teacherID=xxx.   Make it so I can switch back to Java with a simple config switch.
            //"    <a href=\"http://localhost/mcasprep/createClass.php?%TEACHER_ID%\">Create a new class</a></p>\n" +
//            "  <p align=\"center\"><a href=\"WoAdmin?action=" + AdminActions.CREATE_CLASS1 + "\"></a> <br>\n" +


            "  <p align=\"center\"><a href=\"WoAdmin?action=" + AdminActions.ALTER_CLASS1 + "\">Alter class</a> <br>\n" +

            "<a href=\"WoAdmin?action="+ AdminActions.VIEW_REPORT1 + "\">View reports about my classes</a></font></p>\n" +
            "  <p>&nbsp;</p>\n" +
            "</div> \n" +
            "</body>\n" +
            "</html>\n" +
            "";

    public TeacherActivityPage (String id) {
        this.id=id;
    }

    public String getView() throws Exception {
        return src.replace("%TEACHER_ID%",this.id);
    }
}
