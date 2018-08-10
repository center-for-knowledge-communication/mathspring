package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.event.admin.AdminActions;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.woreports.TeachersClass;

import java.util.List;

public class SelectClassPage implements View {
    private List classes;
    private int teacherId;

    public SelectClassPage(List classes, int teacherId) {
        this.classes = classes;
        this.teacherId = teacherId;
    }

    private String buildhref (int classId, String name) {
        return "<a href=\"WoAdmin?action="+AdminActions.SELECT_CLASS + "&teacherId=" + teacherId + "&classId=" + classId + "\">" + name + "</a>";
    }
    //<font face="Arial, Helvetica, sans-serif" size="3" color="#FFFFFF">
    private String getRows() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < classes.size(); i++) {
            TeachersClass c = (TeachersClass) classes.get(i);
            sb.append("  <tr>\n" +

                    "    <td width=\"38\">" + "" + "</td>\n" +
                    "    <td width=\"212\">" + buildhref(c.getId(),c.getName()) + "</font></td>\n" +
                    "    <td width=\"50\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"#FFFFFF\">" + c.getYear() + "</font></td>\n" +
                    "    <td width=\"89\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"#FFFFFF\">" + c.getSection() + "</font></td>\n" +
                    "    <td width=\"192\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"#FFFFFF\">" + c.getSchool() + "</font></td>\n" +
                    "    <td width=\"174\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"#FFFFFF\">" + c.getTown() + "</font></td>\n" +

                    "  </tr>\n");

        }
        return sb.toString();
    }

    public String getView() throws Exception {
        return
                "<html>\n" +
                "<head>\n" +
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
                "<title>Wayang Outpost: Teacher Tools: Reports</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" +
                "</head>\n" +
                "\n" +
                "<body bgcolor=\"#904A4A\" text=\"#000000\" background=\"../images/background_clouds.gif\">\n" +
                "<p><b><font face=\"Arial, Helvetica, sans-serif\" size=\"7\" color=\"#FFFFFF\">Wayang \n" +
                "  Outpost </font></b><i><font face=\"Arial, Helvetica, sans-serif\" size=\"6\" color=\"#FFFFFF\">Teacher \n" +
                "  Tools</font></i></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><font face=\"Arial, Helvetica, sans-serif\" size=\"4\" color=\"#FFFFFF\">Click the \n" +
                "  on the class you'd like to get a report about</font></p>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action="+ AdminActions.SELECT_CLASS +"\">\n" +
                "<table width=\"781\" border=\"0\">\n" +
                getRows() +
                "</table>\n" +
                "</form>\n" +
                "<p>&nbsp; </p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
    }
}
