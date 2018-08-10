<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Sept 8, 2011
  Time: 1:05:03 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<jsp:include page="teacherToolsHeader.jsp" />     --%>
<jsp:include page="${sideMenu}" />

<script type="text/javascript">
    function validateDelete() {
        return confirm("Deleting classes can cause loss of research data. Are you sure you want to proceed?")
    }
</script>



<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--<jsp:useBean id="classes" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>--%>


<div id="Layer1" align="center">
  <p class="a2"><b><font face="Arial, Helvetica, sans-serif">Class Deletion</font></b></p>
    <form name="form1" method="post" onsubmit="return validateDelete();" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminDeleteClassesSubmit">
        <p>Select the classes you want to delete:</p>
        <table width="70%" border="1">
            <tr>
                <td height="22">Teacher</td>
                <td height="22">School</td>
                <td height="22">Class</td>
                <td height="22">Town</td>
                <td height="22">Choose</td>
            </tr>
            <%--@elvariable id="c" type="edu.umass.ckc.wo.beans.ClassInfo"--%>
            <c:forEach var="c" items="${classes}">
                <tr>
                    <td height="22">${c.teacherName}</td>
                    <td height="22">${c.school}</td>
                    <td height="22">${c.name}</td>
                    <td height="22">${c.town}</td>
                    <td height="22"><input type="checkbox" name="classToDelete" value="${c.classid}"></td>
                </tr>
            </c:forEach>
        </table>
    <br>




      <input type="hidden" name="teacherId" value="${teacherId}"/>
      <input type="submit" name="delete" value="Do the Delete" />
      <input type="submit" name="Cancel" value="Cancel" />

  </form>



  <p/>
      <font face="Arial, Helvetica, sans-serif"></font>
    <p/>

    <font  face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
</div>

<jsp:include page="wayangTempTail.jsp" />