package edu.umass.ckc.wo.html.admin;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminTeacherRegistrationEvent;
import ckc.servlet.servbase.View;

public class TeacherRegistrationPage implements View {
    private String msg;
    public static final int INITIAL_STATE = -1;
    public static final int PASSWORD_MISMATCH = 0;
    public static final int MISSING_REQUIRED_FIELDS = 1;
    public static final int SUCCESS = 2;
    private AdminTeacherRegistrationEvent event;
    private int errCode = -1;

    public TeacherRegistrationPage(int errCode, String msg, AdminTeacherRegistrationEvent eventInfo) {
        setMsg(msg);
        setErrCode(errCode);
        setEvent(eventInfo);
    }

    public TeacherRegistrationPage() {
        setMsg("");
        setErrCode(-1);
        setEvent(null);
    }

    public AdminTeacherRegistrationEvent getEvent() {
        return event;
    }

    public void setEvent(AdminTeacherRegistrationEvent event) {
        this.event = event;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }


    public String getView() throws Exception {
        return "<html>\n" +
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
                "<img src=\"images/bg.jpg\" width=\"957\" height=\"812\">\n" +
                "<div id=\"Layer1\" style=\"position:absolute; width:349px; height:333px; z-index:1; left: 544px; top: 345px\">\n" +
                "  <p align=\"center\"><font size=\"4\" color=\"#FFFFFF\">Registration Info</font></p>\n" +
                "  <form name=\"form1\" method=\"post\" action=\"WoAdmin?action=" + AdminActions.REGISTER + "\">\n" +
                "   \n" +
                "    <table width=\"335\" border=\"0\" height=\"132\">\n" +
                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">* First Name:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"text\" name=\"fname\" size=\"30\" " +
                ((errCode > INITIAL_STATE) ? ("value=\"" + this.event.getFname() + "\"") : "") +
                ">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">* Last Name:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"text\" name=\"lname\" size=\"30\" " +
                ((errCode > INITIAL_STATE) ? ("value=\"" + this.event.getLname() + "\"") : "") +
                ">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">* PasswordAuthentication:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"password\" name=\"pw1\">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">* Retype PasswordAuthentication:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"password\" name=\"pw2\">\n" +
                "        </td>\n" +
                "      </tr>\n" +

                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">* Email address:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"text\" name=\"email\"" +
                ((errCode > INITIAL_STATE) ? ("value=\"" + this.event.getEmail() + "\"") : "") +
                ">\n" +
                "        </td>\n" +
                "      </tr>\n" +

                "      <tr> \n" +
                "        <td width=\"140\"><font color=\"#FFFFFF\">Requested Username:</font></td>\n" +
                "        <td width=\"185\">\n" +
                "          <input type=\"text\" name=\"userName\" maxlength=\"20\" " +
                 ((errCode > INITIAL_STATE) ? ("value=\"" + this.event.getUn() + "\"") : "") +
                ">\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
                "      <input type=\"submit\" name=\"submit\" value=\"Submit\">\n" +
                "     <br><font color=\"#FFFFFF\">* required fields</font>\n" +
                "     <br><font color=\"#FFFF00\"> " + getMsg() + "</font>\n" +
                "    </p>\n" +
                "    </form>\n" +
                "  <p>&nbsp;</p>\n" +
                "</div> \n" +
                "</body>\n" +
                "</html>\n" +
                "";
    }
}
