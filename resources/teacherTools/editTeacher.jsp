<%--
  Created by IntelliJ IDEA.
  User: david
  Date: Sep 22, 2011
  Time: 4:34:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />

<jsp:useBean id="teacher" scope="request" class="edu.umass.ckc.wo.beans.Teacher"/>

<div class="mainPageMargin">
<div align="left">

</div>
    <p>&nbsp;</p>

<div id="Layer2" align="center" style=" width:900px; height:450px; z-index:1">


<p class="whArial">
        <form name="form1" id="f1" method="post"
              action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditTeacherSetTeacher">
            <select name="teacherId">
                <c:forEach var="t" items="${teachers}">
                    <c:choose>
                        <c:when test="${t.id == teacherId}"> <option selected value="${t.id}">${t.name}</option> </c:when>
                        <c:otherwise> <option value="${t.id}">${t.name}</option> </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
            <input type="submit" name="submit" value="Set Teacher" />
        </form>
    <br><br>
        <b>Edit Teacher fields</b>
    <c:if test="${teacherId != -1}">
        <form name="form2" id="f2" method="post"
              action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditTeacherSubmit">

            <table width="600">
                <tr> <td width="50"><span class="whArial">ID</span></td>
                    <td> <span class="whArial"> <c:out value="${teacher.id}"/> </span> </td></tr>
                <tr> <td width="50"><span class="whArial">User Name</span></td>
                    <td> <input name="uname" type="text" value="<c:out value="${teacher.userName}"/>"> </td></tr>
                <tr> <td width="50"><span class="whArial">First Name</span></td>
                    <td> <input name="fname" type="text" value="<c:out value="${teacher.fname}"/>"> </td></tr>
                <tr> <td width="50"><span class="whArial">Last Name</span></td>
                    <td> <input name="lname" type="text" value="<c:out value="${teacher.lname}"/>"> </td></tr>
                <tr> <td width="50"><span class="whArial">Password</span></td>
                    <td> <input name="password" type="password"> </td></tr>

            </table>


          <input type="submit" name="submit" value="Save Changes" />
          <input type="hidden" name="teacherId"  value="${teacher.id}"/>

        </form>
    </c:if>
 <br>

    <p class="whArial">
    <c:out value="${message}"/>
    </p>




    </div>
</div>
</body>
</html>
