<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 29, 2008
  Time: 2:44:30 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="teacherToolsHeader.jsp" />

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="ttlogo.jsp" />


<div id="Layer1" align="center" style="background-color:#FFFFFF; opacity:0.6; filter:alpha(opacity=60);  border:1px solid gray; width:400px; height:333px; position:absolute; left: 25%; top: 25%; visibility: visible" >
  <p align="center"><font size="4" color="#000000">Registration Info</font></p>
  <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminTeacherRegistration">


    <table width="335" border="0" height="132">
      <tr>
        <td width="140"><font color="#000000">* First Name:</font></td>
        <td width="185">
          <input type="text" name="fname" size="30" >
        </td>
      </tr>
      <tr>
        <td width="140"><font color="#000000">* Last Name:</font></td>

        <td width="185">
          <input type="text" name="lname" size="30" >
        </td>
      </tr>
      <tr>
        <td width="140"><font color="#000000">* Password:</font></td>
        <td width="185">
          <input type="password" name="pw1">
        </td>

      </tr>
      <tr>
        <td width="140"><font color="#000000">* Retype Password:</font></td>
        <td width="185">
          <input type="password" name="pw2">
        </td>
      </tr>
      <tr>
        <td width="140"><font color="#000000">* Email address:</font></td>

        <td width="185">
          <input type="text" name="email">
        </td>
      </tr>
      <tr>
        <td width="140"><font color="#000000">Requested Username:</font></td>
        <td width="185">
          <input type="text" name="userName" maxlength="20" >
        </td>

      </tr>
    </table>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="submit" name="submit" value="Submit">
     <br><font color="#FF0000">* required fields</font>
     <br><font color="#FF0000"><c:out value="${message}"/></font>
    </p>
    </form>

  <p>&nbsp;</p>
</div>
</body>
</html>