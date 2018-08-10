package edu.umass.ckc.wo.html.admin;

import ckc.servlet.servbase.View;


public class AdminPage implements View {


  public AdminPage (String msg) {
    msg_ = msg;
  }

  public String getView () {
    return
    "<html>\n"+
    "<head>\n"+
    "<title>Wayang Class creation</title>\n"+
    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
    "</head>\n"+
    "\n"+
    "<body bgcolor=\"#FFFFFF\">\n"+
    "<p><font size=\"6\">Wayang Outpost</font> </p>\n"+
    "<p>Create Class</p>\n"+
    "<form name=\"form1\" method=\"post\" action=\"WoAdminServlet\">\n"+
    "  <p>First Name: \n"+
    "    <input type=\"text\" name=\"fname\" size=\"50\">\n"+
    "    <br>\n"+"LastName: \n" +
    "    <input type=\"text\" name=\"lname\" size=\"50\">\n"+
    "    <br>\n"+"PasswordAuthentication: \n" +
    "    <input type=\"password\" name=\"password1\" size=\"50\">\n"+
    "    <br>\n"+ "Retype PasswordAuthentication: \n" +
    "    <input type=\"password\" name=\"password2\" size=\"50\">\n"+
    "    <br>\n"+
    "    Class Name: \n"+
    "    <input type=\"text\" name=\"className\" size=\"50\">\n"+
    "    <br>\n"+
    "    School: \n"+
    "    <input type=\"text\" name=\"school\" size=\"50\">\n"+
    "    <br>\n"+
    "    Town: \n"+
    "    <input type=\"text\" name=\"town\" size=\"50\">\n"+
    "    <br>\n"+
    "    School Year (200X): \n"+
    "    <input type=\"text\" name=\"schoolYear\" size=\"4\" maxlength=\"4\">\n"+
    "    <br>\n"+
    "  </p>\n"+
    "  <p>\n"+
    "    <input type=\"submit\" name=\"Submit\" value=\"Submit\">\n"+
    "  </p>\n"+
    "	<input type=\"hidden\" name=\"action\" value=\"CreateClass2\">\n"+
    "</form>\n"+
    "<p>&nbsp;</p>\n" + msg_ +
    "</body>\n"+
    "</html>\n";
  }
  public void setMsg_(String msg_) {
    this.msg_ = msg_;
  }
  public String getMsg_() {
    return msg_;
  }

  private String msg_;
}
