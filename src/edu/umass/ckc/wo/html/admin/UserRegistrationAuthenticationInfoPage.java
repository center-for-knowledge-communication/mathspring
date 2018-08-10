package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.wo.enumx.Actions;

import edu.umass.ckc.wo.event.admin.UserRegistrationEvent;
import ckc.servlet.servbase.View;

/**
 * Generate an HTML page that gets authentication info from a user that is registering.
 */
public class UserRegistrationAuthenticationInfoPage implements View {

    String uri;
    String msg_=null;
    String startPage;

    public UserRegistrationAuthenticationInfoPage(String uri, String msg, UserRegistrationEvent e) {
        this.uri = uri;
        msg_ = msg;
        startPage=e.getStartPage();
    }

    public String getView () {
        return
                "<html>\n"+
                        "<head>\n"+
                        "<title>User Registration</title>\n"+
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
                        "</head>\n"+
                        "\n"+
                        "<body bgcolor=\"#FFFFFF\" background=\"images/background_clouds.gif\">\n"+
                        "<p><img src=\"images/logo_text.gif\" width=\"280\" height=\"72\"> </p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>Create a new user:</b></font></p>\n"+
                        "<form name=\"form1\" method=\"post\" action=\"" + uri + "\">\n"+
                        "  <p><font color=\"#FFFFFF\">First Name: </font> \n"+
                        "    <input type=\"hidden\" name=\"startPage\" value=\"" + startPage + "\"/>\n" +
                        "    <input type=\"text\" name=\"fname\">\n"+
                        "    &nbsp;&nbsp;<font color=\"#FFFFFF\">Last Name: \n"+
                        "    <input type=\"text\" name=\"lname\" size=\"30\">\n"+
                        "    </font> &nbsp;<br>\n"+
                        "    <font color=\"#FFFFFF\">User Name: </font> \n"+
                        "    <input type=\"text\" name=\"uname\">\n"+
                        "    &nbsp;<font color=\"#FFFFFF\">PasswordAuthentication: &nbsp; \n"+
                        "    <input type=\"password\" name=\"password\">\n"+
                        "    </font> &nbsp;<br>\n"+
                        "    <font color=\"#FFFFFF\">Email: &nbsp; \n"+
                        "    <input type=\"text\" name=\"email\">\n"+
                        "    </font>\n"+
                        "    <br><br><font color=\"#FFFFFF\">\n"+
                        "    <input type=\"checkbox\" name=\"testUser\" value=\"testUser\">This user is for testing the system</input>\n"+
                        "    </font></p>\n"+
                        "  <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n"+
                        "    <input type=\"submit\" name=\"Submit\" value=\"Submit\">\n"+
                        "    <input type=\"hidden\" name=\"action\" value=\"" + Actions.createUser2 + "\">\n"+
                        "    <br>\n"+
                        "  </p>\n"+
                        "  </form>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p>&nbsp; </p>\n"+
                        ((msg_ != null) ? "<br><font color=\"#FFFFFF\">" + msg_  : "") +
                        "</body>\n"+
                        "</html>\n"+
                        "";
    }
}
