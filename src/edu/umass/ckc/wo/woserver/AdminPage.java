package edu.umass.ckc.wo.woserver;


public class AdminPage {
    public static final String src =
"<html>\n"+
"<head>\n"+
"<title>UMass MCAS Tutor: Class creation</title>\n"+
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
"</head>\n"+
"\n"+
"<body bgcolor=\"#FFFFFF\">\n"+
"<p><font size=\"6\">UMass MCAS Tutor</font> </p>\n"+
"<p>Create Class</p>\n"+
"<form name=\"form1\" method=\"post\" action=\"WoAdmin\">\n"+
"  <p>Teacher Name: \n"+
"    <input type=\"text\" name=\"teacherName\" size=\"50\">\n"+
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
"	<input type=\"hidden\" name=\"state\" value=\"final\">\n"+
"	<input type=\"hidden\" name=\"action\" value=\"AdminCreateClass\">\n"+
"</form>\n"+
"<p>&nbsp;</p>\n"+
"</body>\n"+
"</html>\n"+
"";
}
