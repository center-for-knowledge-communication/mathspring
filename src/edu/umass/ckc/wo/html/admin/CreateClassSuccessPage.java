package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.tutor.Pedagogy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CreateClassSuccessPage implements View {
    private String school;
    private int schoolYear;
    private String name;
    private String town;
    private String section;
    private int classid;
    private int teachid;
    private String teacherName;
    private Collection pedagogies;





    public CreateClassSuccessPage(ClassInfo clInfo, List pedagogies) {

        this.classid = clInfo.getClassid();
        this.name = clInfo.getName();
        this.teachid=clInfo.getTeachid();
        this.teacherName=clInfo.getTeacherName();
        this.school = clInfo.getSchool();
        this.schoolYear=clInfo.getSchoolYear();
        this.town=clInfo.getTown();
        this.section=clInfo.getSection();
        this.pedagogies =pedagogies;

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
                "  <p align=\"center\" class=\"a2\"><font color=\"#FFFFFF\"><b><font face=\"Arial, Helvetica, sans-serif\">Class Successfully Created \n" +
                "</font></b></font></p>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action=" + AdminActions.CHOOSE_ACTIVITY + "\">\n" +

                "    <table width=\"334\" border=\"0\" height=\"98\">\n" +

                "<tr><td>Class ID:</td><td>"+this.classid+"</td></tr>\n"+
                "<tr><td>Class Name:</td><td>"+this.name+"</td></tr>\n"+
                "<tr><td>Teacher ID:</td><td>"+this.teachid+"</td></tr>\n"+
                "<tr><td>Teacher Name:</td><td>"+this.teacherName+"</td></tr>\n"+
                "<tr><td>School:</td><td>"+this.school+"</td></tr>\n"+
                "<tr><td>Year:</td><td>"+this.schoolYear+"</td></tr>\n"+
                "<tr><td>Town:</td><td>"+this.town+"</td></tr>\n"+
                "<tr><td>Section:</td><td>"+this.section+"</td></tr>\n"+
                "    </table><p/>\n" +

                "  <p align=\"center\" class=\"a2\"><font color=\"#FFFFFF\"><b><font face=\"Arial, Helvetica, sans-serif\">Selected Pedagogies \n" +
                "</font></b></font></p>\n" +

                "    <table width=\"334\" border=\"0\" height=\"98\">\n" +
                getPedagogiesAsTableRows() +
                "</table>\n"+
                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n" +
                "      <input type=\"submit\" name=\"Next\" value=\"Next\">\n" +

                "  </form>\n" +


                "</div> \n" +
                "</body>\n" +
                "</html>\n" +
                "";
    }


    private String getPedagogiesAsTableRows () {
        StringBuffer sb = new StringBuffer();
        Iterator itr = this.pedagogies.iterator();
        while (itr.hasNext()) {
            Pedagogy pedagogy = (Pedagogy) itr.next();
            String id = pedagogy.getId();
            String name = pedagogy.getName();
            sb.append("<tr><td width=\"40\">" + id + "</td><td width=\"305\">"+  name + "</td></tr>\n");
        }
        return sb.toString();

    }


}