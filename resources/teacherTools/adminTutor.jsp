<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 29, 2008
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="adminNoClassSideMenu.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">

    function validateReload() {
        return confirm("You are about to reload all the problems in the tutor. Are you sure you want to proceed?")
    }

    function validateDelete(thing) {
        return confirm("Deleting " + thing + " can cause loss of research data. Are you sure you want to proceed?")
    }

</script>

<div class="mainPageMargin">

    <div align="left">

    </div>

    <p>&nbsp</p>

    <div id="Layer1" align="center" style="width:400px; height:375px; z-index:1;">
        <p class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Administrate Tutor</font></b></font></p>
        <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminSubmitClassForm">


            <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>"><p/>
            <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminReloadProblems&teacherId=${teacherId}" onclick="return validateReload()">Reload Problems...</a> <br>
            <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminDeleteClasses&teacherId=${teacherId}"  >Delete Classes</a>    <br>
            <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminDeleteTeachers&teacherId=${teacherId}" >Delete Teachers</a>    <br>
            <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminDeleteStudents&teacherId=${teacherId}"  >Delete Students</a>    <br>
            <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminEditTeacher&teacherId=${teacherId}"  >Edit Teacher</a>    <br>
            <p><c:out value="${message}"/></p>
        </form>
        <form style="display:inline" name="form2" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminMainPage&teacherId=<c:out value="${teacherId}"/>">
            <input type="submit" name="Submit" value="Back to teacher tools" />
        </form>
        <p/>



        <font color="#FF0000" face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
    </div>
</div>

</div>
</div>

</body>
</html>