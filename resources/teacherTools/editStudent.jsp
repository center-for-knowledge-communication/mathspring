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

<jsp:useBean id="student" scope="request" type="edu.umass.ckc.wo.smgr.User"/>

<div class="mainPageMargin">
<div align="left">
    <form name="form10" id="form10" method="post"  action="<c:out value="${pageContext.request.contextPath}"/>
	/WoAdmin?action=AdminEditClassList">
        <input type="submit" name="Submit" value="Back to Student List" />
        <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId"  value="<c:out value="${classInfo.classid}"/>"/>
    </form>
</div>
    <p>&nbsp;</p>

<div id="Layer2" align="center" style=" width:900px; height:450px; z-index:1">

    <%--@elvariable id="classInfo" type="edu.umass.ckc.wo.beans.ClassInfo"--%>
    <%--@elvariable id="student" type="edu.umass.ckc.wo.smgr.User"--%>

<p class="whArial">
        <b>Edit Student fields</b>
    <form name="form1" id="f" method="post"
          action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterStudent">

        <table width="600">
            <tr> <td width="50"><span class="whArial">ID</span></td>
                <td> <span class="whArial"> <c:out value="${student.id}"/> </span> </td></tr>
            <tr> <td width="50"><span class="whArial">User Name</span></td>
                <td> <input name="uname" type="text" value="<c:out value="${student.uname}"/>"> </td></tr>
            <tr> <td width="50"><span class="whArial">First Name</span></td>
                <td> <input name="fname" type="text" value="<c:out value="${student.fname}"/>"> </td></tr>
            <tr> <td width="50"><span class="whArial">Password</span></td>
                <td> <input name="password" type="password" > </td></tr>
            <tr> <td width="50"><span class="whArial">Pedagogy Id</span></td>
                <%-- <td> <input name="pedagogyId" type="text" value="<c:out value="${student.pedagogyId}"/>"> </td></tr>   --%>

            <%--<tr><td> <select path="pedagogy" items="${pedagogies}" itemValue="${pedagogy.id}" itemLabel="${pedagogy.id}: ${pedagogy.name}" /> </td></tr>
                --%>


            <%--<tr><td style='border:0px solid black;'>
               <%-- <input type='text' name='$m'size='10'></input> --%>
                <td><select name="pedagogyId">;
                    <c:forEach var="pedagogy" items="${pedagogies}">
                     <%--<% for (var in pedagogies){  %>        --%>
                        <c:if test ="${pedagogy.id == student.pedagogyId}">
                            <option value='${pedagogy.id}' selected="selected">${pedagogy.id}: ${pedagogy.name}</option>
                        </c:if>
                        <c:if test = "${pedagogy.id != student.pedagogyId}">


                    <option value='${pedagogy.id}'>${pedagogy.id}: ${pedagogy.name}</option>
                        </c:if>
                   </c:forEach>
                    </select>   </td></tr>



               <%-- <c:forEach var="pedagogy" items="${pedagogies}">
                    <tr><td width="305"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"><c:out value="${pedagogy.id}: ${pedagogy.name}"/></font></td></tr>
                --%>
            <%--<tr><td valign="top" width="40">${pedagogy.description}</td>
                <td width="305"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"><c:out value="${pedagogy.description}"/></font></td></tr>
                 --%>
            <%--</c:forEach>
                   --%>



        </table>


      <input type="submit" name="submit" value="Save Changes" />
      <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
      <input type="hidden" name="classId"  value="<c:out value="${classInfo.classid}"/>"/>
      <input type="hidden" name="studId"  value="<c:out value="${student.id}"/>"/>
    </form>
 <br>

    <p class="whArial">
    <c:out value="${message}"/>
    </p>




    </div>
</div>
</body>
</html>
