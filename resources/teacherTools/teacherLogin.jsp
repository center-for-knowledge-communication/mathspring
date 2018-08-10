<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 23, 2008
  Time: 3:43:56 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="teacherToolsHeader.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="ttlogo.jsp" />


  <div id="Layer1" style="background-color:#FFFFFF; opacity:0.6; filter:alpha(opacity=60);  border:1px solid gray; position:absolute; left: 25%; top: 25%; visibility: visible">
    <p align="center"><font color="#000000" size="5" face="Arial, Helvetica, sans-serif">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome to MathSpring Teacher Tools &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </font></p>
        

      <p align="center"><font color="#000000" size="3" face="Arial, Helvetica, sans-serif">
          a portal for content control and feedback
          </font></p>

      <p align="center"><font color="#000000" size="3" face="Arial, Helvetica, sans-serif">
          <b>Login/New User</b></font></p>


  <form align="center" name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminTeacherLogin">
    <table width="348" border="0" height="72">
      <tr>
        <td width=130>&nbsp;</td>

        <td width="93"><font color="#000000" class="Arial">User Name:</font></td>
        <td width="115">
          <input type="text" name="userName" size="30">
        </td>
      </tr>
      <tr>
        <td width=130>&nbsp;</td>
        <td width="93"><font color="#000000" class="Arial">Password:</font></td>

        <td width="115">
          <input type="password" name="password" size="30">
        </td>
      </tr>
    </table>
    <p align="center">
      <input type="submit" name="login" value="Login">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="submit" name="register" value="Register">

      <br>
  </form>
  <br> 
  <p align="center"><font color="#FF0000"><c:out value="${message}"/></font></p>
  <p>&nbsp;</p>
      <p align="center" ><font color="#000000"><a target="_blank" href="http://cadmium.cs.umass.edu/drop/WayangCheatSheet-College.pdf">FAQ</a>
              about Wayang  </font>
      </p>
  </div>
  </body>
</html>