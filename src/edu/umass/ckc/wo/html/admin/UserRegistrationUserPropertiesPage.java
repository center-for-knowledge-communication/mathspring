package edu.umass.ckc.wo.html.admin;


import edu.umass.ckc.wo.enumx.Actions;
import edu.umass.ckc.wo.event.admin.UserRegistrationClassSelectionEvent;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.util.ThreeTuple;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

/** Generate an html page which gathers each property that is in the UserProperties
 * table.
 */
public class UserRegistrationUserPropertiesPage implements View {
    String uri;
    String msg_;
    Connection conn_;
    UserRegistrationClassSelectionEvent event_;
    String startPage;

    public UserRegistrationUserPropertiesPage(String uri, UserRegistrationClassSelectionEvent e, String msg, Connection conn) {
        this.uri = uri;
        msg_ = msg;
        event_ = e;
        conn_ = conn;
        startPage=e.getStartPage();
    }


    public String genPropertyInputs (String epropName, String ipropName) {
        return
                "    <font color=\"#FFFFFF\">" + ipropName + "</font> \n" +
                        "    <input type=\"text\" name=\"" + epropName + "\"><br>\n";
    }

    public String getAllFields (List props) {
        Iterator iter = props.iterator();
        StringBuffer sb = new StringBuffer(64);
        while (iter.hasNext()) {
            ThreeTuple item = (ThreeTuple) iter.next();
            sb.append(genPropertyInputs((String) item.getSecond(),(String) item.getThird()));
        }
        return sb.toString();
    }

    public String getView () throws Exception {
        List props = UserRegistrationHandler.getUserProperties(conn_,event_.getClassId());
        String fields = getAllFields(props);
        return
                "<html>\n"+
                        "<head>\n"+
                        "<title>Extended Registration Info</title>\n"+
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
                        "</head>\n"+
                        "\n"+
                        "<body bgcolor=\"#FFFFFF\" background=\"images/background_clouds.gif\">\n"+
                        "<p><img src=\"images/logo_text.gif\" width=\"280\" height=\"72\"> </p>\n"+
                        "<p>&nbsp;</p>\n"+
                        "<p><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"><b>Please enter the following data</b></font></p>\n"+
                        "<form name=\"form1\" method=\"post\" action=\"" + uri + "\">\n"+
                        fields +
                        "  <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n"+
                        "    <input type=\"submit\" name=\"Submit\" value=\"Submit\">\n"+
                        "    <input type=\"hidden\" name=\"action\" value=\"" + Actions.createUser4 + "\">\n"+
                        "    <input type=\"hidden\" name=\"classId\" value=\"" + event_.getClassId() + "\">\n"+
                        "    <input type=\"hidden\" name=\"studId\" value=\"" + event_.getStudId() + "\">\n"+
                        "    <input type=\"hidden\" name=\"startPage\" value=\"" + startPage + "\">\n"+
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