<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Dec 1, 2009
  Time: 12:18:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Wayang Outpost</title>
    <% String ipAddr = request.getParameter("ipAddr");  %>

    <script language="JavaScript">
        function launchWindow()
        {
            var wx = screen.width / 2 - 400;
            var wy = screen.height / 2 - 300;
            attributes = "scrollbars=no,resizable=no,width=800,height=600,left=" + wx + ",top=" + wy;
            newwin = window.open("WoServlet?showLoginScreen=true&ipAddr=<% out.print(ipAddr); %>","Wayang_Outpost",attributes);
            newwin.focus();
        }
    </script>
</head>

  <body bgcolor="8f4a4a">
  
  <table width="640" height="480" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr> 
        <td> <a href="http://www.surveymonkey.com/s/9X95529"> Before-Wayang Survey </a> </td>
    </tr>
    <tr>
        <td> <a href="http://www.surveymonkey.com/s/FZK2NCT"> After-Wayang Survey </a> </td>
    </tr>

    <tr>
      <td height="345" valign="top"><a href="#" onClick="javascript:launchWindow();">
        <img src="images/splash.gif" width="783" height="523" border="0"></a></td>

    </tr>
    <tr>
    <td align="center">
        <font color="#FFFFFF" size="2" face="Verdana, Arial, Helvetica, sans-serif">&copy; 2003 University of Massachusetts, Amherst</font>
    </td>
    </tr>
  </table>

  </body>

  </html>

  
  </body>
</html>