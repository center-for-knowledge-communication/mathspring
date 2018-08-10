package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.event.admin.AdminActions;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.tutor.Pedagogy;

public class SelectPedagogyPage implements View {

    private String msg = "";
    private Pedagogy[] pedagogies;





    public SelectPedagogyPage(String msg, Pedagogy[] pedagogies) {

        this.msg = msg;
        this.pedagogies = pedagogies;

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
                "<img src=\"images/mcasbg.jpg\">\n" +
                "<div id=\"Layer1\" style=\"position:absolute; width:365px; height:375px; z-index:1; left: 544px; top: 345px\"> \n" +
                "  <p align=\"center\" class=\"a2\"><font color=\"#FFFFFF\"><b><font face=\"Arial, Helvetica, sans-serif\">Select \n" +
                "    Pedagogies</font></b></font></p>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action=" + AdminActions.CREATE_CLASS3 + "\">\n" +
                "    <table width=\"334\" border=\"0\" height=\"98\">\n" +

                getPedagogiesAsTableRows()+

                "    </table>\n" +
                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n" +
                "      <input type=\"submit\" name=\"Submit\" value=\"Submit\">\n" +

                "  </form>\n" +
      
                "<font color=\"#FFFF00\" face=\"Arial, Helvetica, sans-serif\">" + msg + "</font>\n" +
                "</div> \n" +
                "</body>\n" +
                "</html>\n" +
                "";
    }


    private String getPedagogiesAsTableRows () {
        StringBuffer sb = new StringBuffer("<tr><td width=\"40\"><input type=\"checkbox\" " +
                "name=\"0\"/><td width=\"305\"><font color=\"#FFFFFF\" " +
                "face=\"Arial, Helvetica, sans-serif\">Default</font></td></tr>\n");
        for (Pedagogy pedagogy : this.pedagogies) {
            String id = pedagogy.getId();
            String name = pedagogy.getName();
            sb.append("<tr><td width=\"40\"><input type=\"checkbox\" name=\"" + id + "\" /></td>");
            sb.append("<td width=\"305\"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">" + name + "</font></td></tr>\n");
        }
        return sb.toString();

    }


}