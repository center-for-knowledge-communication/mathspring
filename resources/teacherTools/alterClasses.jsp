<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 23, 2008
  Time: 3:43:56 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<jsp:include page="teacherToolsHeader.jsp" />       --%>
<jsp:include page="${sideMenu}" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<div class="mainPageMargin">
<div id="Layer1" style="position:absolute; width:600px; height:191px; z-index:1; left: 350px; top: 5px; visibility: visible">
  <p align="center"><font color="#FFFFFF" size="3" face="Arial, Helvetica, sans-serif"><b>Your classes</b></font></p>
    <table width="600">
      <tr>
      <td width="50"><span class="whArial">Class ID</span></td>
      <td width="180"><span class="whArial">Class Name</span></td>
      <td width="180"><span class="whArial">Teacher Name</span></td>
      <td width="60"><span class="whArial">Section</span></td>
	  <td width="60"><span class="whArial">Edit</span></td>
	  <td width="60"><span class="whArial">Delete</span></td>
      </tr>
        <c:forEach var="cl" items="${bean.classes}">
            <tr><td><span class="whArial"><c:out value="${cl.classid}"/></span></td>
            <td><span class="whArial"><c:out value="${cl.name}"/></span></td>
            <td><span class="whArial"><c:out value="${cl.teacherName}"/></span></td>
            <td><span class="whArial"><c:out value="${cl.section}"/></span></td> 
            <td><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassEdit&classId=<c:out value="${cl.classid}"/>&teacherId=<c:out value="${cl.teachid}"/>"><img  src='<c:out value="${pageContext.request.contextPath}"/>/images/edit.gif' alt="Edit Class"></a></td>
            <td><a onclick="return confirm('Are you sure you want to delete this class?');" href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminDeleteClass&classId=<c:out value="${cl.classid}"/>&teacherId=<c:out value="${cl.teachid}"/>"><img  src='<c:out value="${pageContext.request.contextPath}"/>/images/del.gif' alt="Delete Class"></a></td>
            </tr>
        </c:forEach>
    </table>

    <p>&nbsp;</p>
  </div>
</div>

<form name="form1" id="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminChooseActivity">
    <input type="submit" name="submit" value="Choose Another Activity" />
    <input type="hidden" name="teacherId"  value="<c:out value="${teacherId}"/>"/>
</form>
  </body>
</html>