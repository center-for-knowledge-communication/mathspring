
package edu.umass.ckc.wo.html.admin;


import edu.umass.ckc.wo.event.admin.UserRegistrationEvent;
import edu.umass.ckc.servlet.servbase.View;

public class UserRegistrationCompletePage implements View {
    String uri;
    private static final String WO_LOGIN_SWF = "WoLogin.swf";
    String startPage;


    public UserRegistrationCompletePage(String uri, UserRegistrationEvent e) {
        this.uri = uri;
        this.startPage = e.getStartPage();
    }

    public String getView () {
        return
                "<html>\n"+
                        "<head>\n"+
                        "<title>User Registration Complete</title>\n"+
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
                        "</head>\n"+
                        "\n"+
                        "<body bgcolor=\"#FFFFFF\" background=\"images/background_clouds.gif\">\n"+
                        "<p><img src=\"images/logo_text.gif\" width=\"280\" height=\"72\"> </p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>User created. </b></font></p>\n"+
                        "<p><a href=\"WoLoginServlet?action="+ startPage+  "\"> <font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>Click here to login </b></font></a></p>\n"+
                        "<p>&nbsp;</p>\n"+
//"<p><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>Click <a href=\"" + v_.getFlashUri(WO_LOGIN_SWF) + " \"><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">here</a> to login to Wayang Outpost</b></font></p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p>&nbsp; </p>\n"+
                        "</body>\n"+
                        "</html>\n"+
                        "";
    }
}
