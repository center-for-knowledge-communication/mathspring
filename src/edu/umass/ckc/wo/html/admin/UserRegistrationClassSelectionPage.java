package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.event.admin.UserRegistrationEvent;
import ckc.servlet.servbase.View;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Generate an HTML page that has a table of all the teachers/classes.   The user must select one.
 */
public class UserRegistrationClassSelectionPage implements View {

    String uri;
    Connection conn_;
    int studId_;
    String callbackAction_;
    String startPage;

    public UserRegistrationClassSelectionPage(String uri, int studId, Connection c, String callBackAction, UserRegistrationEvent e) {
        this.uri = uri;
        studId_ = studId;
        conn_ = c;
        callbackAction_ = callBackAction;
        startPage=e.getStartPage();
    }

    private String getRows () throws Exception {
        String q = "select teacher,school,schoolYear,name,town,id from Class where isActive=1 order by teacher,name,school";
        PreparedStatement ps = conn_.prepareStatement(q);
        StringBuffer sb = new StringBuffer(64);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String teacher= rs.getString(1);
            String school= rs.getString(2);
            int schoolYear= rs.getInt(3);
            String name= rs.getString(4);
            String town= rs.getString(5);
            int id = rs.getInt(6);
            sb.append(
                    "    <tr>\n"+
                            "      <td><font color=\"#FFFFFF\">" + teacher + "</td>\n"+
                            "      <td><font color=\"#FFFFFF\">" + school + "</td>\n"+
                            "      <td><font color=\"#FFFFFF\">" + name + "</td>\n"+
                            "      <td><font color=\"#FFFFFF\">" + schoolYear + "</td>\n"+
                            "      <td><font color=\"#FFFFFF\">" + town + "</td>\n"+
                            "      <td><font color=\"#FFFFFF\"><a href=\"" + uri + "?action="+ callbackAction_ +
                            "&classId=" + id + "&studId=" + studId_ +  "&startPage=" + startPage +
                            "\"><img src=\"images/arrow.gif\" width=\"18\" height=\"16\" border=\"0\"></a></td>\n"+
                            "    </tr>\n");
        }
        return sb.toString();
    }

    public String getView () throws Exception {
        return
                "<html>\n"+
                        "<head>\n"+
                        "<title>Untitled Document</title>\n"+
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
                        "</head>\n"+
                        "\n"+
                        "<body bgcolor=\"#FFFFFF\" background=\"images/background_clouds.gif\">\n"+
                        "<p><img src=\"images/logo_text.gif\" width=\"280\" height=\"72\"> </p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>Select what class \n"+
                        "  you are in:</b></font></p>\n"+
                        "<form name=\"form1\" method=\"post\" action=\"" + uri + "\">\n"+
                        "  <table width=\"70%\" border=\"1\">\n"+
                        "    <tr>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">Teacher</font></td>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">School</font></td>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">Class</font></td>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">School Yr</font></td>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">Town</font></td>\n"+
                        "      <td height=\"22\"><font color=\"#FFFFFF\">Choose</font></td>\n"+
                        "    </tr>\n"+
                        getRows() +
                        "  </table>\n"+
                        "  <p><font color=\"#FFFFFF\">Find the class you are in and click the &quot;choose&quot; \n"+
                        "    icon in the row of this table</font> </p>\n"+
                        "  <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n"+
                        "    <br>\n"+
                        "  </p>\n"+
                        "  </form>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p>&nbsp; </p>\n"+
                        "</body>\n"+
                        "</html>\n"+
                        "";
    }
}
